package org.atinject.integration;

import org.atinject.core.logging.LoggerFactory;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

@RunWith(WeldRunner.class)
public abstract class IntegrationTest
{
    Logger logger = LoggerFactory.getLogger(WeldRunner.class);
    
    @Rule public TestRule watchman = new TestWatcher() {

        @Override
        protected void starting(Description description) {
            logger.info("starting {}", description.getMethodName());
        }

        @Override
        protected void finished(Description description) {
            logger.info("finished {}", description.getMethodName());
        }
    };
}
