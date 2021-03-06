/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.camel.internal;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import java.util.Map;
import java.util.Set;

import org.apache.camel.*;
import org.apache.camel.spi.TransactedPolicy;
import org.seedstack.camel.CamelContextInitializer;

class CamelModule extends AbstractModule {
    private final CamelContext camelContext;
    private final Set<Class<? extends RoutesBuilder>> routesBuilderClasses;
    private final Set<Class<? extends Processor>> processorClasses;
    private final Set<Class<? extends Component>> componentClasses;
    private final Set<Class<? extends Endpoint>> endPointClasses;
    private final Set<Class<? extends Producer>> producerClasses;
    private final Set<Class<? extends Consumer>> consumerClasses;
    private final Set<Class<? extends Predicate>> predicateClasses;
    private final Set<Class<? extends CamelContextInitializer>> initializerClasses;
    private final GuiceBeanRepository guiceBeanRepository;
    private final GuiceInjector guiceInjector;

    CamelModule(CamelContext camelContext, Set<Class<? extends RoutesBuilder>> routesBuilderClasses,
            Set<Class<? extends Processor>> processorClasses, Set<Class<? extends Component>> componentClasses,
            Set<Class<? extends Endpoint>> endPointClasses, Set<Class<? extends Producer>> producerClasses,
            Set<Class<? extends Consumer>> consumerClasses, Set<Class<? extends Predicate>> predicateClasses,
            Set<Class<? extends CamelContextInitializer>> initializerClasses,
            GuiceBeanRepository guiceBeanRepository, GuiceInjector guiceInjector) {
        this.camelContext = camelContext;
        this.routesBuilderClasses = routesBuilderClasses;
        this.processorClasses=processorClasses;
        this.componentClasses=componentClasses;
        this.endPointClasses=endPointClasses;
        this.producerClasses=producerClasses;
        this.consumerClasses=consumerClasses;
        this.predicateClasses=predicateClasses;
        this.initializerClasses=initializerClasses;
        this.guiceBeanRepository = guiceBeanRepository;
        this.guiceInjector = guiceInjector;
    }


    @Override
    protected void configure() {
        bind(TransactedPolicy.class).to(SeedTransactedPolicy.class);
        bind(ProducerTemplate.class).toProvider(new ProducerTemplateProvider(camelContext));

        //Set binding for routes
        Multibinder<RoutesBuilder> routesBuilderBinder = Multibinder.newSetBinder(binder(), RoutesBuilder.class);
        routesBuilderClasses.forEach(cl -> routesBuilderBinder.addBinding().to(cl));
        //Set binding for components
        Multibinder<Component> componentSetBinder=Multibinder.newSetBinder(binder(),Component.class);
        componentClasses.forEach(componentClass-> componentSetBinder.addBinding().to(componentClass));
        //Set binding for Endpoints
        Multibinder<Endpoint> endpointSetBinder=Multibinder.newSetBinder(binder(), Endpoint.class);
        endPointClasses.forEach(endpointClass-> endpointSetBinder.addBinding().to(endpointClass));
        //Set bindings for initializers
        Multibinder<CamelContextInitializer> initializerSetBinder=Multibinder.newSetBinder(binder(), CamelContextInitializer.class);
        initializerClasses.forEach(initializerClass-> initializerSetBinder.addBinding().to(initializerClass));

        //Unitary bindings
        processorClasses.forEach(this::bind);
        componentClasses.forEach(this::bind);
        endPointClasses.forEach(this::bind);
        producerClasses.forEach(this::bind);
        consumerClasses.forEach(this::bind);
        predicateClasses.forEach(this::bind);
        initializerClasses.forEach(this::bind);

        // Injection of Guice support classes
        requestInjection(guiceBeanRepository);
        requestInjection(guiceInjector);
    }
}
