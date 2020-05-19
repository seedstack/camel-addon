/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.camel.fixtures;

import org.apache.camel.CamelContext;
import org.apache.camel.Processor;
import org.junit.Assert;
import org.seedstack.camel.CamelContextInitializer;
import org.seedstack.seed.Logging;
import org.slf4j.Logger;

import javax.inject.Inject;

/**
 * Should be called in integration tests.
 * Test that the parameter is filled and the injection is OK.
 */
public class CustomInitializer implements CamelContextInitializer {

    @Logging
    private Logger logger;

    @Inject
    private BasicProcessor basicProcessor;

    @Override
    public void initialize(CamelContext camelContext) {
        logger.info("Custom Initializer test");
        Assert.assertNotNull(camelContext);
        Assert.assertNotNull(basicProcessor);
        Assert.assertFalse(camelContext.isStarted());
        logger.info("All tests done in Camel custom initializer");
    }
}
