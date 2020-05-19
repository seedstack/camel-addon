/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.camel;

import org.apache.camel.CamelContext;

/**
 * Interface for Camel context initializer
 * <br>Implement initialize method for needed camel component initializations
 */
public interface CamelContextInitializer {
    /**
     * This method is executed at startup. Seedstack providing the Camel context.
     * <br>It is executed before camel context start.
     * @param camelContext Default Camel context ( Provided by seedstack)
     */
    void initialize(CamelContext camelContext);
}
