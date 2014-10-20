package org.atinject.api.role;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.atinject.api.role.enumeration.Roles;
import org.atinject.core.enumeration.AbstractEnumerationExtension;
import org.atinject.core.logging.LoggerFactory;
import org.slf4j.Logger;

public class DefaultRoleExtension extends AbstractEnumerationExtension implements RoleExtension {

    private Logger logger = LoggerFactory.getLogger(DefaultRoleExtension.class);

    private Map<String, Class<?>> roles;

    public DefaultRoleExtension() {
        roles = new LinkedHashMap<>();
    }

    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<Roles> event) {
        logger.info("merging '{}' to roles", event.getAnnotatedType().getJavaClass());
        List<Field> fields = getNonSyntheticFields(event.getAnnotatedType().getJavaClass());
        for (Field field : fields) {
            if (!isPublicStaticFinalString(field)) {
                throw new ExceptionInInitializerError(
                        "field '" + field.getName() + "' must be declared public static final String ...");
            }
            String value = getFieldValueAsString(field);
            if (!field.getName().equals(value)) {
                throw new ExceptionInInitializerError(
                        "field '" + field.getName() + "' value do not match field name");
            }
            if (roles.containsKey(field.getName())) {
                throw new ExceptionInInitializerError(
                        "duplicate role '" + field.getName() + "' found");
            }
            roles.put(field.getName(), event.getAnnotatedType().getJavaClass());
        }
    }

    @Override
    public Set<String> getAllRole() {
        return roles.keySet();
    }
}
