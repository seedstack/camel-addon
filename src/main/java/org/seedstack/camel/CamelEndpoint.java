/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.camel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class level annotation.
 * <br>Associate a name to a custom endpoint class in the camel context
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CamelEndpoint {
    /**
     * Provides the endPoint name to be referenced in the camel context
     * @return String
     */
    public String endPointUri();
}
