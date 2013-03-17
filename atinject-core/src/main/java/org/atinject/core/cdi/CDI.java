package org.atinject.core.cdi;

import java.lang.annotation.Annotation;
import java.util.Iterator;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.TypeLiteral;

/**
 * offer delegates on {@link javax.enterprise.inject.spi.CDI#current()}
 */
public class CDI {

    private static javax.enterprise.inject.spi.CDI<Object> cdi =
            javax.enterprise.inject.spi.CDI.current();
    
    public static Iterator<Object> iterator() {
        return cdi.iterator();
    }
    
    public static Object get() {
        return cdi.get();
    }
    
    public static Instance<Object> select(Annotation... qualifiers) {
        return cdi.select(qualifiers);
    }
    
    public static <U> Instance<U> select(Class<U> subtype, Annotation... qualifiers) {
        return cdi.select(subtype, qualifiers);
    }
    
    public static BeanManager getBeanManager() {
        return cdi.getBeanManager();
    }
    
    public static <U> Instance<U> select(TypeLiteral<U> subtype, Annotation... qualifiers) {
        return cdi.select(subtype, qualifiers);
    }
    
    public static boolean isUnsatisfied() {
        return cdi.isUnsatisfied();
    }
    
    public static boolean isAmbiguous() {
        return cdi.isAmbiguous();
    }
    
    public static void destroy(Object instance) {
        cdi.destroy(instance);
    }
    
}
