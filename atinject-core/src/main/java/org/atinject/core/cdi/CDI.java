package org.atinject.core.cdi;

import java.lang.annotation.Annotation;
import java.util.Iterator;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.TypeLiteral;

/**
 * offer delegates on {@link javax.enterprise.inject.spi.CDI#current()}
 */
public final class CDI {

    private static javax.enterprise.inject.spi.CDI<Object> cdi =
            javax.enterprise.inject.spi.CDI.current();
    
    private CDI() {
        
    }
    
    /**
     * delegates to {@link javax.enterprise.inject.spi.CDI#iterator()}
     */
    public static Iterator<Object> iterator() {
        return cdi.iterator();
    }
    
    /**
     * delegates to {@link javax.enterprise.inject.spi.CDI#get()}
     */
    public static Object get() {
        return cdi.get();
    }
    
    /**
     * delegates to {@link javax.enterprise.inject.spi.CDI#select(Annotation...)}
     */
    public static Instance<Object> select(Annotation... qualifiers) {
        return cdi.select(qualifiers);
    }
    
    /**
     * delegates to {@link javax.enterprise.inject.spi.CDI#select(Class, Annotation...)}
     */
    public static <U> Instance<U> select(Class<U> subtype, Annotation... qualifiers) {
        return cdi.select(subtype, qualifiers);
    }
    
    /**
     * delegates to {@link javax.enterprise.inject.spi.CDI#getBeanManager()}
     */
    public static BeanManager getBeanManager() {
        return cdi.getBeanManager();
    }
    
    /**
     * delegates to {@link javax.enterprise.inject.spi.CDI#select(TypeLiteral, Annotation...)}
     */
    public static <U> Instance<U> select(TypeLiteral<U> subtype, Annotation... qualifiers) {
        return cdi.select(subtype, qualifiers);
    }
    
    /**
     * delegates to {@link javax.enterprise.inject.spi.CDI#isUnsatisfied()}
     */
    public static boolean isUnsatisfied() {
        return cdi.isUnsatisfied();
    }
    
    /**
     * delegates to {@link javax.enterprise.inject.spi.CDI#isAmbiguous()}
     */
    public static boolean isAmbiguous() {
        return cdi.isAmbiguous();
    }
    
    /**
     * delegates to {@link javax.enterprise.inject.spi.CDI#destroy(Object)}
     */
    public static void destroy(Object instance) {
        cdi.destroy(instance);
    }
    
    public static <T> CreationalContext<T> createUnboundCreationalContext() {
        return createUnboundCreationalContext(getBeanManager());
    }
    
    public static <T> CreationalContext<T> createUnboundCreationalContext(BeanManager beanManager) {
    	return beanManager.createCreationalContext(null);
    }
}
