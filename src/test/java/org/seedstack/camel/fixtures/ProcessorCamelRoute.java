package org.seedstack.camel.fixtures;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import javax.inject.Inject;

public class ProcessorCamelRoute extends RouteBuilder {

    @Inject
    private BasicProcessor basicProcessor;

    @Override
    public void configure() throws Exception {
        errorHandler(deadLetterChannel("mock:error"));
        from("direct:b").process(basicProcessor);
    }
}
