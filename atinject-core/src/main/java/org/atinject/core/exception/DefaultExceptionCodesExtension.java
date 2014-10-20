package org.atinject.core.exception;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.atinject.core.enumeration.AbstractEnumerationExtension;
import org.atinject.core.logging.LoggerFactory;
import org.slf4j.Logger;

public class DefaultExceptionCodesExtension extends AbstractEnumerationExtension {

    private Logger logger = LoggerFactory.getLogger(DefaultExceptionCodesExtension.class);
    
    private Map<String, Class<?>> exceptionCodes;
    
    public DefaultExceptionCodesExtension(){
        exceptionCodes = new LinkedHashMap<>();
    }
    
    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<ExceptionCodes> event) {
        logger.info("merging '{}' to exception codes", event.getAnnotatedType().getJavaClass());
        List<Field> fields = getNonSyntheticFields(event.getAnnotatedType().getJavaClass());
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
            if (exceptionCodes.containsKey(field.getName())) {
                throw new ExceptionInInitializerError(
                        "duplicate exception code '" + field.getName() + "' found");
            }
            exceptionCodes.put(field.getName(), event.getAnnotatedType().getJavaClass());
        }
     }
    
    public Set<String> getAllExceptionCode() {
        return exceptionCodes.keySet();
    }
}
