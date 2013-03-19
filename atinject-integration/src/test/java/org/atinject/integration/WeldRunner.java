package org.atinject.integration;

import javax.enterprise.inject.spi.CDI;

import org.atinject.core.cdi.CDIProvider;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class WeldRunner extends BlockJUnit4ClassRunner {
    
    static{
        CDI.setCDIProvider(new CDIProvider());
    }
    
    public WeldRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }
 
    @Override
    public void run(RunNotifier notifier) {
        super.run(notifier);
    }
     
    @Override
    protected Object createTest() throws Exception {
        return CDI.current().select(getTestClass().getJavaClass()).get();
    }
 
}