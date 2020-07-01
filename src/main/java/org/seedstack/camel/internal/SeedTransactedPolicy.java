/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.camel.internal;

import org.apache.camel.NamedNode;
import org.apache.camel.Processor;
import org.apache.camel.spi.RouteContext;
import org.apache.camel.spi.TransactedPolicy;

public class SeedTransactedPolicy implements TransactedPolicy {
    @Override
    public void beforeWrap(RouteContext routeContext, NamedNode definition) {
        // nothing to do
    }

    @Override
    public Processor wrap(RouteContext routeContext, Processor processor) {
        // TODO: implement transactional behavior
        return null;
    }
}
