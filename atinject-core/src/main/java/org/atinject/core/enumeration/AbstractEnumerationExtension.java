package org.atinject.core.enumeration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;


public abstract class AbstractEnumerationExtension {

    protected boolean isInterface(Class<?> clazz) {
        return clazz.isInterface();
    }
    
    protected boolean isPublicStaticFinalString(Field field) {
        return isPublic(field) && isStatic(field) && isFinal(field) && isString(field);
    }
    
    protected boolean isPublic(Field field) {
        return Modifier.isPublic(field.getModifiers());
    }
    
    protected boolean isStatic(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }
    
    protected boolean isFinal(Field field) {
        return Modifier.isFinal(field.getModifiers());
    }
    
    protected boolean isString(Field field) {
        return String.class.equals(field.getType());
    }
    
    protected String getFieldValueAsString(Field field){
        try {
            return (String) field.get(null);
        }
        catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
