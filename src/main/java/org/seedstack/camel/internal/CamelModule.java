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

import java.util.Set;

import org.apache.camel.*;

class CamelModule extends AbstractModule {
    private final CamelContext camelContext;
    private final Set<Class<? extends RoutesBuilder>> routesBuilderClasses;
    private final Set<Class<? extends Processor>> processorClasses;
    private final Set<Class<? extends Component>> componentClasses;
    private final Set<Class<? extends Endpoint>> endPointClasses;



    CamelModule(CamelContext camelContext, Set<Class<? extends RoutesBuilder>> routesBuilderClasses, Set<Class<? extends Processor>> processorClasses, Set<Class<? extends Component>> componentClasses,Set<Class<? extends Endpoint>> endPointClasses) {
        this.camelContext = camelContext;
        this.routesBuilderClasses = routesBuilderClasses;
        this.processorClasses=processorClasses;
        this.componentClasses=componentClasses;
        this.endPointClasses=endPointClasses;
    }

    @Override
    protected void configure() {
        bind(ProducerTemplate.class).toProvider(new ProducerTemplateProvider(camelContext));
        Multibinder<RoutesBuilder> routesBuilderBinder = Multibinder.newSetBinder(binder(), RoutesBuilder.class);
        routesBuilderClasses.forEach(cl -> routesBuilderBinder.addBinding().to(cl));

        processorClasses.forEach(this::bind);
        componentClasses.forEach(this::bind);
        endPointClasses.forEach(this::bind);
    }
}
