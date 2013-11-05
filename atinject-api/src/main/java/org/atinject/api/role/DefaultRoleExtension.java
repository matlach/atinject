package org.atinject.api.role;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.atinject.api.role.enumeration.Roles;
import org.atinject.core.dto.DefaultDTORegistryExtension;
import org.atinject.core.logging.LoggerFactory;
import org.slf4j.Logger;

public class DefaultRoleExtension implements RoleExtension {

	private Logger logger = LoggerFactory.getLogger(DefaultDTORegistryExtension.class);
	
	private Map<String, Class<? extends Roles>> roles;
	
	public DefaultRoleExtension() {
		roles = new HashMap<>();
	}
	
	<T extends Roles> void processAnnotatedType(@Observes ProcessAnnotatedType<? extends Roles> event) {
		
    	logger.info("merging '{}' to roles", event.getAnnotatedType().getJavaClass());
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
    		if (roles.containsKey(field.getName())) {
    			throw new ExceptionInInitializerError("duplicate role '" + field.getName() + "' found");
    		}
    		roles.put(field.getName(), event.getAnnotatedType().getJavaClass());
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
	
	@Override
	public Set<String> getAllRole() {
		return roles.keySet();
	}
}
