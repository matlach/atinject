package org.atinject.core.cdi;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

import org.atinject.core.logging.LoggerFactory;
import org.slf4j.Logger;

public class BeanManagerExtension implements Extension
{

    // TODO in weld 2.0, bean manager should be accessed statically by using CDI.current().getBeanManager();
    
    private static BeanManager beanManager;
    
    private static Logger logger = LoggerFactory.getLogger(BeanManagerExtension.class);
    
    public void onBeforeBeanDiscovery(@Observes BeforeBeanDiscovery event, BeanManager beanManager)
    {
        // keep reference to bean manager the earliest possible
        BeanManagerExtension.beanManager = beanManager;
    }
    
    public static <T> T getReference(Class<T> klass)
    {
        if(beanManager == null)
        {
            logger.warn("returning null reference for bean '{}', bean manager is null", klass);
            return null;
        }
        Set<Bean<?>> beans = beanManager.getBeans(klass);
        if (beans.isEmpty())
        {
            logger.warn("returning null reference for bean '{}'", klass);
            return null;
        }
        if (beans.size() > 1)
        {
            throw new RuntimeException("more than one bean detected for class '" + klass + "'");
        }

        Bean<?> bean = beans.iterator().next();
        CreationalContext<?> creationalContext = beanManager.createCreationalContext(bean);

        return klass.cast(beanManager.getReference(bean, klass, creationalContext));
    }

    public static void fireEvent(Object event, Annotation... qualifiers)
    {
        beanManager.fireEvent(event, qualifiers);
    }
    
}
