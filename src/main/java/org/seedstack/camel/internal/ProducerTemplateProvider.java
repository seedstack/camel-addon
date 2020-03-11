/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.camel.internal;

import javax.inject.Provider;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;

class ProducerTemplateProvider implements Provider<ProducerTemplate> {
    private final CamelContext camelContext;

    ProducerTemplateProvider(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @Override
    public ProducerTemplate get() {
        return camelContext.createProducerTemplate();
    }
}
