package org.atinject.core.cdi;

import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.CDIProvider;

import org.atinject.core.transaction.InMemoryTransactionServices;
import org.jboss.weld.bean.builtin.BeanManagerProxy;
import org.jboss.weld.bootstrap.api.Bootstrap;
import org.jboss.weld.bootstrap.spi.Deployment;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.jboss.weld.resources.spi.ResourceLoader;
import org.jboss.weld.transaction.spi.TransactionServices;

public class JavaSECDIProvider implements CDIProvider {

    private final Weld weld;
    private final WeldContainer weldContainer;
    private final CDIObject cdiObject;
    
    public JavaSECDIProvider(){
        weld = new Weld(){
            @Override
            protected Deployment createDeployment(ResourceLoader resourceLoader, Bootstrap bootstrap)
            {
                Deployment deployment = super.createDeployment(resourceLoader, bootstrap);
                deployment.getServices().add(TransactionServices.class, new InMemoryTransactionServices());
                return deployment;
            }
        };
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
