package org.atinject.integration;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class WeldRunner extends BlockJUnit4ClassRunner {
    
    private Weld weld;
    private WeldContainer container;
 
    public WeldRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }
 
    @Override
    public void run(RunNotifier notifier) {
        initializeWeld();
        super.run(notifier);
        shutdownWeld();
    }
     
    @Override
    protected Object createTest() throws Exception {
        return container
                   .instance()
                   .select(getTestClass().getJavaClass())
                   .get();
    }
 
    private void initializeWeld() {
        weld = new Weld();
        container = weld.initialize();
    }
 
    private void shutdownWeld() {
        weld.shutdown();
    }
}