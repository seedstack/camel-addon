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
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.seedstack.seed.SeedException;
import org.seedstack.seed.core.internal.AbstractSeedPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CamelPlugin extends AbstractSeedPlugin {
    private static final Logger LOGGER = LoggerFactory.getLogger(CamelPlugin.class);
    private final Set<Class<? extends RoutesBuilder>> routesBuilderClasses = new HashSet<>();
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
                .build();
    }

    @Override
    protected InitState initialize(InitContext initContext) {
        camelContext = new DefaultCamelContext();
        initContext.scannedTypesBySpecification()
                .get(CamelSpecifications.ROUTES_BUILDER)
                .stream()
                .filter(RoutesBuilder.class::isAssignableFrom)
                .forEach(candidate -> {
                    Class<? extends RoutesBuilder> routesBuilderClass = candidate.asSubclass(RoutesBuilder.class);
                    if (!Modifier.isAbstract(routesBuilderClass.getModifiers())) {
                        routesBuilderClasses.add(routesBuilderClass);
                    }
                });
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Detected {} Camel route builder(s): {}", routesBuilderClasses.size(), routesBuilderClasses);
        } else {
            LOGGER.info("Detected {} Camel route builder(s), enable DEBUG logging to see details",
                    routesBuilderClasses.size());
        }
        return InitState.INITIALIZED;
    }

    @Override
    public Object nativeUnitModule() {
        return new CamelModule(camelContext, routesBuilderClasses);
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
