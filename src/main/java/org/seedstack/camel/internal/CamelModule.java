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
import org.apache.camel.RoutesBuilder;

class CamelModule extends AbstractModule {
    private final Set<Class<? extends RoutesBuilder>> routesBuilderClasses;

    CamelModule(Set<Class<? extends RoutesBuilder>> routesBuilderClasses) {
        this.routesBuilderClasses = routesBuilderClasses;
    }

    @Override
    protected void configure() {
        Multibinder<RoutesBuilder> routesBuilderBinder = Multibinder.newSetBinder(binder(), RoutesBuilder.class);
        routesBuilderClasses.forEach(cl -> routesBuilderBinder.addBinding().to(cl));
    }
}
