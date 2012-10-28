package org.atinject.core.cdi;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

public class BeanManagerExtension implements Extension
{

    private static BeanManager beanManager;
    
    public void onBeforeBeanDiscovery(@Observes BeforeBeanDiscovery event, BeanManager beanManager)
    {
        BeanManagerExtension.beanManager = beanManager;
    }
    
    public static BeanManager getBeanManager()
    {
        return beanManager;
    }
}
