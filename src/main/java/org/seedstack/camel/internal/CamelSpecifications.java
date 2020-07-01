/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.camel.internal;

import static org.seedstack.shed.reflect.AnnotationPredicates.classOrAncestorAnnotatedWith;
import static org.seedstack.shed.reflect.ClassPredicates.classImplements;
import static org.seedstack.shed.reflect.ClassPredicates.classModifierIs;

import java.lang.reflect.Modifier;
import java.util.function.Predicate;
import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.RoutesBuilder;
import org.seedstack.camel.CamelComponent;
import org.seedstack.camel.CamelContextInitializer;
import org.seedstack.camel.CamelEndpoint;

public final class CamelSpecifications {
    /**
     * The routes builder specification. It accepts all non abstract classes implementing
     * {@link org.apache.camel.RoutesBuilder}.
     */
    public static final Predicate<Class<?>> ROUTES_BUILDER =
            classImplements(RoutesBuilder.class).and(
                    classModifierIs(Modifier.ABSTRACT).negate());

    /**
     * The processor specification. It accepts all non abstract classes implementing {@link org.apache.camel.Processor}
     */
    public static final Predicate<Class<?>> PROCESSOR =
            classImplements(Processor.class).and((classModifierIs(
                    Modifier.ABSTRACT).negate()));

    /**
     * The Component specification. It accepts all non abstract classes implementing {@link org.apache.camel.Component}
     * and annotated with {@link org.seedstack.camel.CamelComponent}
     */
    public static final Predicate<Class<?>> COMPONENT =
            classImplements(Component.class).and((classModifierIs(
                    Modifier.ABSTRACT).negate()).and(classOrAncestorAnnotatedWith(CamelComponent.class, false)));

    /**
     * The Endpoint specification. It accepts all non abstract classes implementing {@link org.apache.camel.Endpoint}
     * and annotated with {@link org.seedstack.camel.CamelEndpoint}
     */
    public static final Predicate<Class<?>> ENDPOINT =
            classImplements(Endpoint.class).and((classModifierIs(
                    Modifier.ABSTRACT).negate()).and(classOrAncestorAnnotatedWith(CamelEndpoint.class, false)));

    /**
     * The producer specification. It accepts all non abstract classes implementing {@link org.apache.camel.Producer}
     */
    public static final Predicate<Class<?>> PRODUCER =
            classImplements(Producer.class).and((classModifierIs(
                    Modifier.ABSTRACT).negate()));

    /**
     * The consumer specification. It accepts all non abstract classes implementing {@link org.apache.camel.Consumer}
     */
    public static final Predicate<Class<?>> CONSUMER =
            classImplements(Consumer.class).and((classModifierIs(
                    Modifier.ABSTRACT).negate()));

    /**
     * The predicate specification. It accepts all non abstract classes Implementing {@link org.apache.camel.Predicate}
     */
    public static final Predicate<Class<?>> PREDICATE =
            classImplements(org.apache.camel.Predicate.class).and((classModifierIs(
                    Modifier.ABSTRACT).negate()));

    /**
     * Initializers specifications. Accepts All non abstract classes implementing {@link CamelContextInitializer}
     */
    public static final Predicate<Class<?>> INITIALIZERS =
            classImplements(CamelContextInitializer.class)
                    .and((classModifierIs(Modifier.ABSTRACT).negate()));
}
