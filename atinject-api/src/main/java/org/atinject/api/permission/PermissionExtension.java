package org.atinject.api.permission;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.atinject.api.permission.entity.Permissions;
import org.atinject.core.dto.DTORegistryExtension;
import org.atinject.core.logging.LoggerFactory;
import org.slf4j.Logger;

public class PermissionExtension implements Extension {

	private Logger logger = LoggerFactory.getLogger(DTORegistryExtension.class);
	
	private HashMap<String, Class<Permissions>> permissions;
	
	public PermissionExtension(){
		permissions = new HashMap<>();
	}
	
	<T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> event) {
		
        if (Permissions.class.isAssignableFrom(event.getAnnotatedType().getJavaClass())){
        	logger.info("merging '{}' to permissions", event.getAnnotatedType().getJavaClass());
        	Field[] fields = event.getAnnotatedType().getJavaClass().getDeclaredFields();
        	for (Field field : fields) {
        		if (! Modifier.isPublic(field.getModifiers())) {
        			throw new ExceptionInInitializerError("field '" + field.getName() + "' must be declared public static final String ...");
        		}
        		if (! Modifier.isStatic(field.getModifiers())) {
        			throw new ExceptionInInitializerError("field '" + field.getName() + "' must be declared public static final String ...");
        		}
        		if (! Modifier.isFinal(field.getModifiers())) {
        			throw new ExceptionInInitializerError("field '" + field.getName() + "' must be declared public static final String ...");
        		}
        		if (! String.class.equals(field.getType())) {
        			throw new ExceptionInInitializerError("field '" + field.getName() + "' must be declared public static final String ...");
        		}
        		String value = getFieldValueAsString(field);
        		if (! field.getName().equals(value)) {
        			throw new ExceptionInInitializerError("field '" + field.getName() + "' value do not match field name");
        		}
        		if (permissions.containsKey(field.getName())) {
        			throw new ExceptionInInitializerError("duplicate permission '" + field.getName() + "' found");
        		}
        		permissions.put(field.getName(), (Class<Permissions>) event.getAnnotatedType().getJavaClass());
        	}
        }
     }
	
	private String getFieldValueAsString(Field field){
		try {
			return (String) field.get(null);
		}
		catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Set<String> getAllPermission(){
		return permissions.keySet();
	}
}
