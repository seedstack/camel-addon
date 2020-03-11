/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.camel.fixtures;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class BasicCamelRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        errorHandler(deadLetterChannel("mock:error"));
        from("direct:a").to("mock:NoEnd");
    }
}
