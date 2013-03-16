package org.atinject.core.cdi;

import javax.enterprise.inject.spi.CDI;

import org.jboss.weld.bean.builtin.BeanManagerProxy;
import org.jboss.weld.environment.se.WeldContainer;

public class CDIProvider implements javax.enterprise.inject.spi.CDIProvider {

    private final Weld weld;
    private final WeldContainer weldContainer;
    private final CDIObject cdiObject;
    
    public CDIProvider(){
        weld = new Weld();
        weldContainer = weld.initialize();
        
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
            @Override
            public void run() {
                weld.shutdown();
            }}));
        
        cdiObject = new CDIObject((BeanManagerProxy) weldContainer.getBeanManager());
    }
    
    @Override
    public CDI<Object> getCDI() {
        return cdiObject;
    }

}
