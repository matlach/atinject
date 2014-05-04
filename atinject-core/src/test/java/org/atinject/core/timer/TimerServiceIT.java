package org.atinject.core.timer;

import javax.inject.Inject;

import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.slf4j.Logger;

public class TimerServiceIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return createDefaultArchive(TimerServiceIT.class);
    }
    
    @Inject
    private Logger logger;

    @Inject
    private TimerService timerService;
    
    private static int count;
    
    @Test
    public void testScheduleAt() throws Exception {
        timerService.toString();
        while(count < 10){
            Thread.sleep(1000L);
        }
    }
    
    @Schedule(seconds={-1}, minutes={-1}, hours={-1})
    public void eachSecond(){
        logger.info("count {}", count++);
    }

}
