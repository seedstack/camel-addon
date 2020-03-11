package org.seedstack.camel.fixtures;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.assertj.core.api.Assertions;
import org.seedstack.seed.Configuration;
import org.seedstack.seed.Logging;
import org.slf4j.Logger;

public class BasicProcessor implements Processor {

    @Logging
    private Logger logger;

    @Configuration("config.test.value")
    private String configTestValue;

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Received in processor : [{}], injected configuration value : [{}]",exchange.getIn().getBody(), configTestValue);
    }
}
