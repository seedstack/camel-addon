/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.camel.internal;

import com.google.inject.ConfigurationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.inject.Inject;
import org.apache.camel.spi.BeanRepository;

public class GuiceBeanRepository implements BeanRepository {
    @Inject
    private com.google.inject.Injector injector;

    @Override
    public Object lookupByName(String name) {
        return null;
    }

    @Override
    public <T> T lookupByNameAndType(String name, Class<T> type) {
        return getOptionalInstance(type).orElse(null);
    }

    @Override
    public <T> Map<String, T> findByTypeWithName(Class<T> type) {
        Map<String, T> map = new HashMap<>();
        getOptionalInstance(type).ifPresent(i -> map.put("", i));
        return map;
    }

    @Override
    public <T> Set<T> findByType(Class<T> type) {
        Set<T> set = new HashSet<>();
        getOptionalInstance(type).ifPresent(set::add);
        return set;
    }

    private <T> Optional<T> getOptionalInstance(Class<T> type) {
        try {
            return Optional.ofNullable(injector.getInstance(type));
        } catch (ConfigurationException e) {
            return Optional.empty();
        }
    }

}
