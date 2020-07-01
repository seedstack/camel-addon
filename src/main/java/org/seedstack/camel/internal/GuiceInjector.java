/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.camel.internal;

import com.google.inject.ConfigurationException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import javax.inject.Inject;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.spi.Injector;

public class GuiceInjector implements Injector {
    @Inject
    private com.google.inject.Injector injector;
    private final Injector fallback;

    public GuiceInjector(Injector fallback) {
        this.fallback = fallback;
    }

    @Override
    public <T> T newInstance(Class<T> type) {
        return getInstance(type);
    }

    @Override
    public <T> T newInstance(Class<T> type, String factoryMethod) {
        T answer = null;
        try {
            // lookup factory method
            Method fm = type.getMethod(factoryMethod);
            if (Modifier.isStatic(fm.getModifiers()) && Modifier.isPublic(fm.getModifiers()) && fm.getReturnType() == type) {
                Object obj = fm.invoke(null);
                answer = type.cast(obj);
            }
        } catch (Exception e) {
            throw new RuntimeCamelException("Error invoking factory method: " + factoryMethod + " on class: " + type,
                    e);
        }
        return answer;
    }

    @Override
    public <T> T newInstance(Class<T> type, boolean postProcessBean) {
        return getInstance(type);
    }

    @Override
    public boolean supportsAutoWiring() {
        return true;
    }

    private <T> T getInstance(Class<T> type) {
        try {
            return injector.getInstance(type);
        } catch (ConfigurationException e) {
            return fallback.newInstance(type);
        }
    }
}
