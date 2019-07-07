package com.chunfen.wx;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chunfen.wx
 */
public class Log4jTest {

    Logger logger = LoggerFactory.getLogger(Log4jTest.class);

    @Test
    public void info() throws Exception{
        logger.info("test {}", "info");
//        logger.debug("test {}", "debug");
//        logger.error("test {}", "error");
//        logger.warn("test {}", "warn");
//        logger.trace("test {}", "trace");
    }
}
