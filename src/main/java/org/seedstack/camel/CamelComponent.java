/*
 *  Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *   License, v. 2.0. If a copy of the MPL was not distributed with this
 *   file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.camel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class level annotation.
 * <br>Associate a name to a custom component class in the camel context
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CamelComponent {
    /**
     * Component name registered in camel context.
     * <br>For example @CamelComponent(componentName="myComponent") will reference a "myComponent:..."
     *
     * @return String the component name in camel context
     */
    public String componentName();
}
