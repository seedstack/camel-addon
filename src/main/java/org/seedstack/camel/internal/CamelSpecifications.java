/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.camel.internal;

import static org.seedstack.shed.reflect.ClassPredicates.classImplements;
import static org.seedstack.shed.reflect.ClassPredicates.classModifierIs;

import java.lang.reflect.Modifier;

import org.apache.camel.Processor;
import org.apache.camel.RoutesBuilder;
import org.kametic.specifications.Specification;
import org.seedstack.seed.core.internal.utils.SpecificationBuilder;

public final class CamelSpecifications {
    /**
     * The routes builder specification. It accepts all non abstract classes implementing
     * {@link org.apache.camel.RoutesBuilder}.
     */
    public static final Specification<Class<?>> ROUTES_BUILDER = new SpecificationBuilder<>(
            classImplements(RoutesBuilder.class).and(classModifierIs(Modifier.ABSTRACT).negate())).build();

    /**
     * The processor specification. It accepts all non abstract classes implementing {@link org.apache.camel.Processor}
     */
    public static final Specification<Class<?>> PROCESSOR =new SpecificationBuilder<>(
            classImplements(Processor.class).and((classModifierIs(Modifier.ABSTRACT).negate()))).build();
}
