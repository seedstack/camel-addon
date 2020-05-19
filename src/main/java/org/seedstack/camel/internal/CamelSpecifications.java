/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.camel.internal;


import static org.seedstack.shed.reflect.ClassPredicates.classImplements;
import static org.seedstack.shed.reflect.AnnotationPredicates.classOrAncestorAnnotatedWith;
import static org.seedstack.shed.reflect.ClassPredicates.classModifierIs;

import java.lang.reflect.Modifier;

import org.apache.camel.*;
import org.kametic.specifications.Specification;
import org.seedstack.camel.CamelComponent;
import org.seedstack.camel.CamelContextInitializer;
import org.seedstack.camel.CamelEndpoint;
import org.seedstack.seed.core.internal.utils.SpecificationBuilder;

public final class CamelSpecifications {
    /**
     * The routes builder specification. It accepts all non abstract classes implementing
     * {@link org.apache.camel.RoutesBuilder}.
     */
    public static final Specification<Class<?>> ROUTES_BUILDER = new SpecificationBuilder<>(
            classImplements(RoutesBuilder.class)
                    .and(classModifierIs(Modifier.ABSTRACT).negate())).build();

    /**
     * The processor specification. It accepts all non abstract classes implementing {@link org.apache.camel.Processor}
     */
    public static final Specification<Class<?>> PROCESSOR =new SpecificationBuilder<>(
            classImplements(Processor.class).and((classModifierIs(Modifier.ABSTRACT).negate()))).build();

    /**
     * The Component specification. It accepts all non abstract classes implementing {@link org.apache.camel.Component}
     * and annotated with {@link org.seedstack.camel.CamelComponent}
     */
    public static final Specification<Class<?>> COMPONENT = new SpecificationBuilder<>(
            classImplements(Component.class).and((classModifierIs(Modifier.ABSTRACT).negate())
            .and(classOrAncestorAnnotatedWith(CamelComponent.class, false)))).build();

    /**
     * The Endpoint specification. It accepts all non abstract classes implementing {@link org.apache.camel.Endpoint}
     * and annotated with {@link org.seedstack.camel.CamelEndpoint}
     */
    public static final Specification<Class<?>> ENDPOINT = new SpecificationBuilder<>(
            classImplements(Endpoint.class).and((classModifierIs(Modifier.ABSTRACT).negate())
            .and(classOrAncestorAnnotatedWith(CamelEndpoint.class, false)))).build();

    /**
     * The producer specification. It accepts all non abstract classes implementing {@link org.apache.camel.Producer}
     */
    public static final Specification<Class<?>> PRODUCER = new SpecificationBuilder<>(
            classImplements(Producer.class).and((classModifierIs(Modifier.ABSTRACT).negate()))).build();
    /**
     * The consumer specification. It accepts all non abstract classes implementing {@link org.apache.camel.Consumer}
     */
    public static final Specification<Class<?>> CONSUMER = new SpecificationBuilder<>(
            classImplements(Consumer.class).and((classModifierIs(Modifier.ABSTRACT).negate()))).build();

    /**
     * The predicate specification. It accepts all non abstract classes Implementing {@link org.apache.camel.Predicate}
     */
    public static final Specification<Class<?>> PREDICATE = new SpecificationBuilder<>(
            classImplements(Predicate.class).and((classModifierIs(Modifier.ABSTRACT).negate()))).build();

    /**
     * Initializers specifications. Accepts All non abstract classes implementing {@link CamelContextInitializer}
     */
    public static final Specification<Class<?>> INITIALIZERS= new SpecificationBuilder<>(
            classImplements(CamelContextInitializer.class).and((classModifierIs(Modifier.ABSTRACT).negate()))).build();
}
