/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.camel.fixtures;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.seedstack.jms.JmsMessageListener;
import org.seedstack.seed.Bind;

@JmsMessageListener(connection = "connection1", destinationName = "camelQueue")
@Bind
@Singleton
public class TestJmsListener implements MessageListener {
    private final CountDownLatch latch = new CountDownLatch(1);
    private String received;

    @Override
    public void onMessage(Message message) {
        try {
            received = ((TextMessage) message).getText();
            latch.countDown();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public String waitReceived() throws InterruptedException {
        latch.await(1000, TimeUnit.MILLISECONDS);
        return received;
    }
}
