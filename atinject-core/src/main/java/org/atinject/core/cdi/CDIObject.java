package org.atinject.core.cdi;

import java.lang.annotation.Annotation;
import java.util.Iterator;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.TypeLiteral;

import org.jboss.weld.bean.builtin.BeanManagerProxy;

public class CDIObject extends CDI<Object> {

    private BeanManagerProxy beanManager;
    
    public CDIObject(BeanManagerProxy beanManager){
        this.beanManager = beanManager;
    }
    
    @Override
    public Instance<Object> select(Annotation... qualifiers) {
        return getInstance().select(qualifiers);
    }

    @Override
    public <U> Instance<U> select(Class<U> subtype, Annotation... qualifiers) {
        return getInstance().select(subtype, qualifiers);
    }

    @Override
    public <U> Instance<U> select(TypeLiteral<U> subtype, Annotation... qualifiers) {
        return getInstance().select(subtype, qualifiers);
    }

    @Override
    public boolean isUnsatisfied() {
        return getInstance().isUnsatisfied();
    }

    @Override
    public boolean isAmbiguous() {
        return getInstance().isAmbiguous();
    }

    @Override
    public void destroy(Object instance) {
        getInstance().destroy(instance);
    }

    @Override
    public Iterator<Object> iterator() {
        return getInstance().iterator();
    }

    @Override
    public Object get() {
        return getInstance().get();
    }

    @Override
    public BeanManager getBeanManager() {
        return beanManager;
    }
    
    private Instance<Object> getInstance() {
        return beanManager.delegate().instance();
    }

}
