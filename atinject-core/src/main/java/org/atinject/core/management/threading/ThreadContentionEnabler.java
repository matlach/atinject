package org.atinject.core.management.threading;

import java.lang.management.ThreadMXBean;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.startup.Startup;
import org.slf4j.Logger;

@ApplicationScoped
@Startup
public class ThreadContentionEnabler {

    @Inject
    private Logger logger;
    
    @Inject
    private ThreadMXBean threadMXBean;
    
    @PostConstruct
    public void initialize() {
        if (threadMXBean.isThreadContentionMonitoringSupported()) {
            threadMXBean.setThreadContentionMonitoringEnabled(true);
            logger.info("thread contention monitoring has been enabled");
        }
    }
}
