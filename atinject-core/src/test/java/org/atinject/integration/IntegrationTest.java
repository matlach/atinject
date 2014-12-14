package org.atinject.integration;

import java.util.HashSet;
import java.util.Set;

import org.atinject.core.cache.CustomJGroupsLoggerFactory;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
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
		System.setProperty(org.jgroups.Global.CUSTOM_LOG_FACTORY, CustomJGroupsLoggerFactory.class.getName());
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
    
    public static JavaArchive createDefaultArchive(Class<? extends IntegrationTest> integrationTestClass) {
    	JavaArchive archive= ShrinkWrap.create(JavaArchive.class, integrationTestClass.getSimpleName() + ".jar")
	    	.addPackages(true, "org.atinject")
	    	.addAsManifestResource("arquillian-beans.xml", "beans.xml")
	        .addAsManifestResource("arquillian-validation.xml", "validation.xml")
	        .addAsManifestResource("arquillian-javax.enterprise.inject.spi.Extension", "services/javax.enterprise.inject.spi.Extension")
	        .addAsManifestResource("arquillian-org.jboss.weld.bootstrap.api.Service", "services/org.jboss.weld.bootstrap.api.Service")
	        .addAsResource("arquillian-logback.xml", "logback.xml")
	        .addAsResource("arquillian-jgroups.xml", "jgroups.xml");
    	
    	logger.info(archive.toString(Formatters.VERBOSE));
    	
    	return archive;
    }
    
}
