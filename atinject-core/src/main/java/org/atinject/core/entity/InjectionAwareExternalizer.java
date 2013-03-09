package org.atinject.core.entity;

import java.lang.reflect.Field;

import javax.inject.Inject;

import org.atinject.core.cdi.BeanManagerExtension;

public class InjectionAwareExternalizer {

    public InjectionAwareExternalizer(){
        try{
            Class<?> clazz = this.getClass();
            while (! clazz.equals(Object.class)){
                for (Field field : clazz.getDeclaredFields()){
                    if (field.isAnnotationPresent(Inject.class)){
                        // TODO manage qualifiers as well
                        boolean accessible = field.isAccessible();
                        field.setAccessible(true);
                        try{
                            field.set(this, BeanManagerExtension.getReference(field.getDeclaringClass()));
                        }
                        finally{
                            field.setAccessible(accessible);
                        }
                    }
                }
                clazz = clazz.getSuperclass();
            }
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
