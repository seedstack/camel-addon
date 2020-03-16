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

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.kametic.specifications.Specification;
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

    private final Set<Class<? extends RoutesBuilder>> routesBuilderClasses = new HashSet<>();
    private final Set<Class<? extends Processor>> processorClasses= new HashSet<>();
    private final Set<Class<? extends Component>> componentClasses= new HashSet<>();
    private final Set<Class<? extends Endpoint>> endpointClasses= new HashSet<>();

    private CamelContext camelContext;
    @Inject
    private Set<RoutesBuilder> routesBuilder;

    @Override
    public String name() {
        return "camel";
    }

    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests() {
        return classpathScanRequestBuilder()
                .specification(CamelSpecifications.ROUTES_BUILDER)
                .specification(CamelSpecifications.PROCESSOR)
                .specification(CamelSpecifications.COMPONENT)
                .specification(CamelSpecifications.ENDPOINT)
                .build();
    }

    @Override
    protected InitState initialize(InitContext initContext) {
        camelContext = new DefaultCamelContext();
        initializeClassesSet(initContext, RouteBuilder.class, CamelSpecifications.ROUTES_BUILDER, routesBuilderClasses, ROUTE_BUILDER_LOGS_DESCRIPTION);
        initializeClassesSet(initContext, Processor.class, CamelSpecifications.PROCESSOR, processorClasses, PROCESSOR_LOGS_DESCRIPTION);
        initializeClassesSet(initContext, Component.class, CamelSpecifications.COMPONENT, componentClasses, COMPONENT_LOGS_DESCRIPTION);
        initializeClassesSet(initContext, Endpoint.class, CamelSpecifications.ENDPOINT, endpointClasses, ENDPOINT_LOGS_DESCRIPTION);

        return InitState.INITIALIZED;
    }

    private <T>void initializeClassesSet(InitContext initContext, Class<? extends T> managedClass, Specification specification, Set<Class<? extends T>> classSet, String classesLogDescription){
        initContext.scannedTypesBySpecification()
                .get(specification)
                .forEach(candidate ->{
                    Class<? extends T> candidateClass = candidate.asSubclass(managedClass);
                    classSet.add(candidateClass);
                });
        /*
        initContext.scannedTypesBySpecification()
                .getOrDefault(specification,new ArrayList<>())
                .stream()
                .filter(managedClass::isAssignableFrom)
                .forEach(candidate ->{
                    Class<? extends T> candidateClass = candidate.asSubclass(managedClass);
                    classSet.add(candidateClass);
                });*/
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Detected {} Camel {}: {}", classSet.size(), classesLogDescription, classSet);
        } else {
            LOGGER.info("Detected {} Camel {}, enable DEBUG logging to see details",
                    classSet.size(), classesLogDescription);
        }
    }

    @Override
    public Object nativeUnitModule() {
        return new CamelModule(camelContext, routesBuilderClasses, processorClasses,componentClasses, endpointClasses);
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
        LOGGER.info("Starting Camel context");
        camelContext.start();
    }

    @Override
    public void stop() {
        LOGGER.info("Stopping Camel context");
        camelContext.stop();
    }
}
