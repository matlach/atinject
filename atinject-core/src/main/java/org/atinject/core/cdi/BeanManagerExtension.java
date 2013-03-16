package org.atinject.core.cdi;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.CDI;

import org.atinject.core.logging.LoggerFactory;
import org.slf4j.Logger;

public class BeanManagerExtension
{
    
    private static Logger logger = LoggerFactory.getLogger(BeanManagerExtension.class);
    
    public static <T> T getReference(Class<T> klass)
    {
        if(CDI.current().getBeanManager() == null)
        {
            logger.warn("returning null reference for bean '{}', bean manager is null", klass);
            return null;
        }
        Set<Bean<?>> beans = CDI.current().getBeanManager().getBeans(klass);
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
        CreationalContext<?> creationalContext = CDI.current().getBeanManager().createCreationalContext(bean);

        return klass.cast(CDI.current().getBeanManager().getReference(bean, klass, creationalContext));
    }

    public static void fireEvent(Object event, Annotation... qualifiers)
    {
        CDI.current().getBeanManager().fireEvent(event, qualifiers);
    }
    
    public static CreationalContext createUnboundCreationalContext(){
        return CDI.current().getBeanManager().createCreationalContext(null);
    }
    
}
