/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.camel.internal;

import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.Context;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.seedstack.camel.CamelComponent;
import org.seedstack.camel.CamelContextInitializer;
import org.seedstack.camel.CamelEndpoint;
import org.seedstack.seed.SeedException;
import org.seedstack.seed.core.internal.AbstractSeedPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CamelPlugin extends AbstractSeedPlugin {
    private static final Logger LOGGER = LoggerFactory.getLogger(CamelPlugin.class);
    private static final String ROUTE_BUILDER_LOGS_DESCRIPTION="route builder(s)";
    private static final String PROCESSOR_LOGS_DESCRIPTION="processor(s)";
    private static final String COMPONENT_LOGS_DESCRIPTION="component(s)";
    private static final String ENDPOINT_LOGS_DESCRIPTION="endpoint(s)";
    private static final String PRODUCER_LOGS_DESCRIPTION="producer(s)";
    private static final String CONSUMER_LOGS_DESCRIPTION="consumer(s)";
    private static final String PREDICATE_LOGS_DESCRIPTION="predicate(s)";
    private static final String INITIALIZERS_LOGS_DESCRIPTION="initializer(s)";

    private final Set<Class<? extends RoutesBuilder>> routesBuilderClasses = new HashSet<>();
    private final Set<Class<? extends Processor>> processorClasses= new HashSet<>();
    private final Set<Class<? extends Component>> componentClasses= new HashSet<>();
    private final Set<Class<? extends Endpoint>> endpointClasses= new HashSet<>();
    private final Set<Class<? extends Producer>> producerClasses= new HashSet<>();
    private final Set<Class<? extends Consumer>> consumerClasses= new HashSet<>();
    private final Set<Class<? extends Predicate>> predicateClasses= new HashSet<>();
    private final Set<Class<? extends CamelContextInitializer>> initializerClasses = new HashSet<>();
    private GuiceBeanRepository guiceBeanRepository;
    private GuiceInjector guiceInjector;
    private CamelContext camelContext;

    @Inject
    private Set<RoutesBuilder> routesBuilder;
    @Inject
    private Set<Component> customCamelComponents;
    @Inject
    private Set<Endpoint> customCamelEndpoints;
    @Inject
    private Set<CamelContextInitializer> camelContextInitializers;

    @Override
    public String name() {
        return "camel";
    }

    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests() {
        return classpathScanRequestBuilder()
                .predicate(CamelSpecifications.ROUTES_BUILDER)
                .predicate(CamelSpecifications.PROCESSOR)
                .predicate(CamelSpecifications.COMPONENT)
                .predicate(CamelSpecifications.ENDPOINT)
                .predicate(CamelSpecifications.PREDICATE)
                .predicate(CamelSpecifications.INITIALIZERS)
                .build();
    }

    @Override
    protected InitState initialize(InitContext initContext) {
        guiceBeanRepository = new GuiceBeanRepository();
        camelContext = new DefaultCamelContext(guiceBeanRepository);
        guiceInjector = new GuiceInjector(camelContext.getInjector());
        camelContext.setInjector(guiceInjector);

        initializeClassesSet(initContext, RouteBuilder.class, CamelSpecifications.ROUTES_BUILDER, routesBuilderClasses, ROUTE_BUILDER_LOGS_DESCRIPTION);
        initializeClassesSet(initContext, Processor.class, CamelSpecifications.PROCESSOR, processorClasses, PROCESSOR_LOGS_DESCRIPTION);
        initializeClassesSet(initContext, Component.class, CamelSpecifications.COMPONENT, componentClasses, COMPONENT_LOGS_DESCRIPTION);
        initializeClassesSet(initContext, Endpoint.class, CamelSpecifications.ENDPOINT, endpointClasses, ENDPOINT_LOGS_DESCRIPTION);
        initializeClassesSet(initContext, Producer.class, CamelSpecifications.PRODUCER, producerClasses, PRODUCER_LOGS_DESCRIPTION);
        initializeClassesSet(initContext, Consumer.class, CamelSpecifications.CONSUMER, consumerClasses, CONSUMER_LOGS_DESCRIPTION);
        initializeClassesSet(initContext, Predicate.class, CamelSpecifications.PREDICATE, predicateClasses, PREDICATE_LOGS_DESCRIPTION);
        initializeClassesSet(initContext, CamelContextInitializer.class, CamelSpecifications.INITIALIZERS, initializerClasses, INITIALIZERS_LOGS_DESCRIPTION);

        return InitState.INITIALIZED;
    }

    private <T>void initializeClassesSet(InitContext initContext, Class<? extends T> managedClass, java.util.function.Predicate<Class<?>> specification, Set<Class<? extends T>> classSet, String classesLogDescription){
        initContext.scannedTypesByPredicate()
                .getOrDefault(specification, new ArrayList<>())
                .forEach(candidate ->{
                    Class<? extends T> candidateClass = candidate.asSubclass(managedClass);
                    classSet.add(candidateClass);
                });
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Detected {} Camel {}: {}", classSet.size(), classesLogDescription, classSet);
        } else {
            LOGGER.info("Detected {} Camel {}, enable DEBUG logging to see details",
                    classSet.size(), classesLogDescription);
        }
    }

    @Override
    public Object nativeUnitModule() {
        return new CamelModule(camelContext, routesBuilderClasses, processorClasses,componentClasses, endpointClasses, producerClasses,consumerClasses,predicateClasses, initializerClasses, guiceBeanRepository, guiceInjector);
    }

    @Override
    public void start(Context context) {
        LOGGER.info("Adding route(s) to Camel context");
        routesBuilder.forEach(routesBuilder -> {
            try {
                routesBuilder.addRoutesToCamelContext(camelContext);

            } catch (Exception e) {
                throw SeedException.wrap(e, CamelErrorCode.ERROR_BUILDING_CAMEL_CONTEXT);
            }
        });
        LOGGER.info("Adding component(s) to Camel context");
        customCamelComponents.forEach(componentClass->{
            //Get the annotated component name
            CamelComponent camelAnnotation =componentClass.getClass().getAnnotation(CamelComponent.class);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Adding custom component to the Camel context : {}", camelAnnotation.componentName());
            }
            camelContext.addComponent(camelAnnotation.componentName(),componentClass);
        });
        LOGGER.info("Adding endpoint(s) to Camel context");
        customCamelEndpoints.forEach(endpointClass->{
            CamelEndpoint classAnnotation = endpointClass.getClass().getAnnotation(CamelEndpoint.class);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Adding custom endpoint uri to camel context : {}", classAnnotation.endPointUri());
            }
            try {
                camelContext.addEndpoint(classAnnotation.endPointUri(), endpointClass);
            } catch(Exception e){
                throw SeedException.wrap(e, CamelErrorCode.ERROR_BUILDING_CAMEL_CONTEXT);
            }
        });
        LOGGER.info("Starting application camel context initializers");
        camelContextInitializers.forEach(initializer -> initializer.initialize(camelContext));
        LOGGER.info("Starting Camel context");
        camelContext.start();
    }

    @Override
    public void stop() {
        LOGGER.info("Stopping Camel context");
        camelContext.stop();
    }
}
