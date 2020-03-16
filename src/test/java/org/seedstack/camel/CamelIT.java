/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.camel;

import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.seedstack.camel.fixtures.BasicCamelComponent;
import org.seedstack.camel.fixtures.BasicCamelRoute;
import org.seedstack.seed.Logging;
import org.seedstack.seed.testing.ConfigurationProperty;
import org.seedstack.seed.testing.junit4.SeedITRunner;
import org.seedstack.shed.reflect.ClassPredicates;
import org.slf4j.Logger;

@RunWith(SeedITRunner.class)
@ConfigurationProperty(name="config.test.value",value="Test value")
public class CamelIT {

    @Inject
    private ProducerTemplate producerTemplate;

    @Inject
    private BasicCamelComponent basicCamelComponent;

    @Logging
    private Logger logger;

    /**
     * The route builder(s) should have been detected during initialization and attached to the Camel context.<br>
     * The producer template should have been injected successfully and created from the Camel context.
     */
    @Test
    public void basicCamelRoute() {
        Assertions.assertThat(producerTemplate).as("Check producer template is injected").isNotNull();
        producerTemplate.sendBody("direct:a", "World");
    }

    /**
     * Tests that the injector is Injected in the route builder<br>
     */
    @Test
    public void usingProcessor(){
        producerTemplate.sendBody("direct:b", "Test processor");
    }

    @Test
    public void testComponentInjection(){
        Assertions.assertThat(basicCamelComponent).isNotNull();
    }
}
