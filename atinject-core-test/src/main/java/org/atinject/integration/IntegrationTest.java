package org.atinject.integration;

import java.util.HashSet;
import java.util.Set;

//import org.atinject.core.cache.CustomJGroupsLoggerFactory;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Assume;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
public abstract class IntegrationTest {
    
	static {
//		System.setProperty(org.jgroups.Global.CUSTOM_LOG_FACTORY, CustomJGroupsLoggerFactory.class.getName());
//		System.setProperty("user.timezone", value);
//		System.setProperty("user.country", value);
//		System.setProperty("user.language", value);
//		System.setProperty("user.country.format", value);
//		System.setProperty("user.language,format", value);
//		System.setProperty("user.country.display", value);
//		System.setProperty("user.language.display", value);
	}
	
    private static Logger logger = LoggerFactory.getLogger(IntegrationTest.class);

    private static Set<String> inFailureTests = new HashSet<>();
    
    @Rule public TestRule watchman = new TestWatcher() {

        @Override
        protected void starting(Description description) {
            logger.info("starting {}", description.getMethodName());
            
            InSequence inSequence = description.getAnnotation(InSequence.class);
            if (inSequence == null) {
            	throw new RuntimeException("@InSequence annotation usage is mendatory");
            }
            if (inFailureTests.contains(description.getClassName())) {
            	Assume.assumeTrue("test sequence has been broken", false);
            }
        }

        @Override
        protected void succeeded(Description description) {
        	logger.info("finished {}", description.getMethodName());
        }

        @Override
        protected void failed(Throwable e, Description description) {
        	inFailureTests.add(description.getClassName());
        	logger.info("finished {}", description.getMethodName());
        }
    };
    
}
