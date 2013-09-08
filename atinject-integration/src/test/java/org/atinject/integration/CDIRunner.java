package org.atinject.integration;


import org.atinject.core.cdi.CDI;
import org.atinject.core.cdi.JavaSECDIProvider;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class CDIRunner extends BlockJUnit4ClassRunner {
    
    static{
        new JavaSECDIProvider();
    }
    
    public CDIRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }
 
    @Override
    public void run(RunNotifier notifier) {
        super.run(notifier);
    }
     
    @Override
    protected Object createTest() throws Exception {
        return CDI.select(getTestClass().getJavaClass()).get();
    }
 
}