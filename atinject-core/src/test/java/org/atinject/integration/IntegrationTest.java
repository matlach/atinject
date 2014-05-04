package org.atinject.integration;

import java.util.HashMap;
import java.util.Map;

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
    
    private static Logger logger = LoggerFactory.getLogger(IntegrationTest.class);

    private static Map<String, Integer> inSequenceCounter = new HashMap<>();
    
    @Rule public TestRule watchman = new TestWatcher() {

        @Override
        protected void starting(Description description) {
            logger.info("starting {}", description.getMethodName());
            
            InSequence inSequence = description.getAnnotation(InSequence.class);
            if (inSequence != null) {
                int expectedSequenceValue = inSequence.value();
                if (! inSequenceCounter.containsKey(description.getClassName())) {
                    inSequenceCounter.put(description.getClassName(), Integer.valueOf(1));
                }
                int actualSequenceValue = inSequenceCounter.get(description.getClassName()).intValue();
                Assume.assumeTrue(actualSequenceValue == expectedSequenceValue);
            }
        }

        @Override
        protected void finished(Description description) {
            InSequence inSequence = description.getAnnotation(InSequence.class);
            if (inSequence != null) {
                int actualSequenceValue = inSequenceCounter.get(description.getClassName()).intValue();
                int nextSequenceValue = actualSequenceValue + 1;
                inSequenceCounter.put(description.getClassName(), Integer.valueOf(nextSequenceValue));
            }
            
            logger.info("finished {}", description.getMethodName());
        }
    };
    
    public static JavaArchive createDefaultArchive(Class<? extends IntegrationTest> integrationTestClass) {
    	JavaArchive archive= ShrinkWrap.create(JavaArchive.class, integrationTestClass.getSimpleName() + ".jar")
	    	.addPackages(true, "org.atinject")
	    	.addAsManifestResource("arquillian-beans.xml", "beans.xml")
	        .addAsManifestResource("arquillian-validation.xml", "validation.xml")
	        .addAsManifestResource("arquillian-javax.enterprise.inject.spi.Extension", "services/javax.enterprise.inject.spi.Extension")
	        .addAsResource("arquillian-logback.xml", "logback.xml");
    	
    	logger.info(archive.toString(Formatters.VERBOSE));
    	
    	return archive;
    }
    
}
