package org.atinject.core.enumeration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;


public abstract class AbstractEnumerationExtension {

    protected boolean isInterface(Class<?> clazz) {
        return clazz.isInterface();
    }
    
    protected List<Field> getNonSyntheticFields(Class<?> clazz) {
        List<Field> nonSyntheticFields = new ArrayList<>();
        for (Field field : clazz.getFields()) {
            if (! field.isSynthetic()) {
                nonSyntheticFields.add(field);
            }
        }
        return nonSyntheticFields;
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
