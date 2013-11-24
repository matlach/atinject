package org.atinject.api.permission;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.atinject.api.permission.entity.Permissions;
import org.atinject.core.enumeration.AbstractEnumerationExtension;
import org.atinject.core.logging.LoggerFactory;
import org.slf4j.Logger;

public class DefaultPermissionExtension extends AbstractEnumerationExtension implements PermissionExtension {

    private Logger logger = LoggerFactory.getLogger(DefaultPermissionExtension.class);
    
    private Map<String, Class<? extends Permissions>> permissions;
    
    public DefaultPermissionExtension(){
        permissions = new LinkedHashMap<>();
    }
    
    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<? extends Permissions> event) {
        logger.info("merging '{}' to permissions", event.getAnnotatedType().getJavaClass());
        Field[] fields = event.getAnnotatedType().getJavaClass().getDeclaredFields();
        for (Field field : fields) {
            if (! isPublicStaticFinalString(field)) {
                throw new ExceptionInInitializerError(
                        "field '" + field.getName() + "' must be declared public static final String ...");
            }
            String value = getFieldValueAsString(field);
            if (! field.getName().equals(value)) {
                throw new ExceptionInInitializerError(
                        "field '" + field.getName() + "' value do not match field name");
            }
            if (permissions.containsKey(field.getName())) {
                throw new ExceptionInInitializerError(
                        "duplicate permission '" + field.getName() + "' found");
            }
            permissions.put(field.getName(), event.getAnnotatedType().getJavaClass());
        }
     }
    
    @Override
    public Set<String> getAllPermission() {
        return permissions.keySet();
    }
}
