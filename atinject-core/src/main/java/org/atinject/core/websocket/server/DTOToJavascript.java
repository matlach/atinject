package org.atinject.core.websocket.server;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.dto.DTO;
import org.atinject.core.dto.DTORegistryExtension;
import org.atinject.core.websocket.dto.BaseWebSocketNotification;
import org.atinject.core.websocket.dto.BaseWebSocketRequest;
import org.atinject.core.websocket.dto.BaseWebSocketResponse;
import org.slf4j.Logger;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@ApplicationScoped
public class DTOToJavascript
{
    @Inject
    private Logger logger;
    
    @Inject
    private DTORegistryExtension dtoRegistryExtension;
    
    private String javascript;
    
    private static final String NEWLINE = "\r\n";
    
    @PostConstruct
    public void initialize(){
        List<Class<? extends DTO>> dtoClasses = dtoRegistryExtension.getDTOClasses();
        javascript = "";
        for (Class<? extends DTO> dtoClass : dtoClasses){
            javascript = javascript + generate(dtoClass);
        }
        // save generated javascript to file ?
        // that way we could ease frontend development ?
    }
    
    public String getGeneratedJavascript(){
        return this.javascript;
    }
    
    public String generate(Class<? extends DTO> dtoClass){
        if (dtoClass.equals(DTO.class)){
            return generateDTO(DTO.class);
        }
        else if (dtoClass.equals(BaseWebSocketRequest.class)){
            return generateBaseWebSocketRequest(BaseWebSocketRequest.class);
        }
        else if (dtoClass.equals(BaseWebSocketNotification.class)){
            return generateBaseWebSocketNotification(BaseWebSocketNotification.class);
        }
        else if (dtoClass.equals(BaseWebSocketResponse.class)){
            return generateBaseWebSocketResponse(BaseWebSocketResponse.class);
        }
        else if (BaseWebSocketRequest.class.isAssignableFrom(dtoClass)){
            return generateRequest((Class<? extends BaseWebSocketRequest>) dtoClass);
        }
        else if (BaseWebSocketNotification.class.isAssignableFrom(dtoClass)){
            return generateNotification((Class<? extends BaseWebSocketNotification>) dtoClass);
        }
        else if (BaseWebSocketResponse.class.isAssignableFrom(dtoClass)){
            return generateResponse((Class<? extends BaseWebSocketResponse>) dtoClass);
        }
        else if (DTO.class.isAssignableFrom(dtoClass)){
            return generateComplexDTO(dtoClass);
        }
        throw new RuntimeException();
    }
    
    public static final String dtoTemplate =
"function ${dtoClassSimpleName}() {" + NEWLINE +
"};" + NEWLINE +
"" + NEWLINE +
"${dtoClassSimpleName}.prototype.getClass = function() {" + NEWLINE +
"    return this.${jsonTypeInfoProperty};" + NEWLINE +
"}" + NEWLINE;
    
    /**
     * expected result :
     * <pre>
     * function DTO() {
     * };
     *
     * DTO.prototype.getClass = function() {
     *     return this._class;
     * };
     * </pre>
     */
    public static String generateDTO(Class<DTO> dtoClass){
        JsonTypeInfo jsonTypeInfoAnnotation = dtoClass.getAnnotation(JsonTypeInfo.class);
        String dtoClassSimpleName = dtoClass.getSimpleName();
        String jsonTypeInfoProperty = jsonTypeInfoAnnotation.property();
        
        String generated = dtoTemplate
                .replace("${dtoClassSimpleName}", dtoClassSimpleName)
                .replace("${jsonTypeInfoProperty}", jsonTypeInfoProperty);
        return generated;
    }
    
    /**
     * expected result :
     * <pre>
     * function BaseWebSocketRequest() {
     * };
     * 
     * BaseWebSocketRequest.prototype = new DTO();
     * 
     * BaseWebSocketRequest.prototype.getRequestId = function() {
     *     return this.requestId;
     * };
     * 
     * BaseWebSocketRequest.prototype.setRequestId = function(requestId) {
     *     this.requestId = requestId;
     *     return this;
     * };
     * </pre>
     */
    public static String generateBaseWebSocketRequest(Class<BaseWebSocketRequest> baseWebSocketRequestClass){
        return generateComplexDTO(baseWebSocketRequestClass);
    }
    
    public static String generateBaseWebSocketNotification(Class<BaseWebSocketNotification> baseWebSocketNotificationClass){
        return generateComplexDTO(baseWebSocketNotificationClass);
    }
    
    public static String generateBaseWebSocketResponse(Class<BaseWebSocketResponse> baseWebSocketResponseClass){
        return generateComplexDTO(baseWebSocketResponseClass);
    }
    
    public static String generateRequest(Class<? extends BaseWebSocketRequest> webSocketRequestClass){
        return generateComplexDTO(webSocketRequestClass);
    }
    
    public static String generateResponse(Class<? extends BaseWebSocketResponse> webSocketResponseClass){
        return generateComplexDTO(webSocketResponseClass);
    }
    
    public static String generateNotification(Class<? extends BaseWebSocketNotification> webSocketNotificationClass){
        return generateComplexDTO(webSocketNotificationClass);
    }

    public static final String complexDtoTemplate =
"function ${dtoClassSimpleName}() {" + NEWLINE +
"};" + NEWLINE +
"" + NEWLINE +
"${dtoClassSimpleName}.prototype = new ${dtoSuperClassSimpleName}();" + NEWLINE +
"" + NEWLINE +
"${dtoGetterSetters}" +  NEWLINE;
    
    public static final String getterTemplate =
"${dtoClassSimpleName}.prototype.get${capitalizedProperty} = function() {" + NEWLINE +
"    return this.${property};" + NEWLINE +
"};" + NEWLINE +
"" +  NEWLINE;
    public static final String setterTemplate =
"${dtoClassSimpleName}.prototype.get${capitalizedProperty} = function(${property}) {" + NEWLINE +
"    this.${property} = ${property};" + NEWLINE +
"    return this;" + NEWLINE +
"};" + NEWLINE +
"" +  NEWLINE;
    
    private static String generateComplexDTO(Class<? extends DTO> dtoClass){
        String dtoClassSimpleName = dtoClass.getSimpleName();
        String dtoSuperClassSimpleName = dtoClass.getSuperclass().getSimpleName();
        
        String getterSetters = "";
        for (Field field : getDeclaredNonTransientFields(dtoClass)){
            String capitalizedProperty = capitalizeFirstLetter(field.getName());
            String property = field.getName();
            String getter = getterTemplate
                    .replace("${dtoClassSimpleName}", dtoClassSimpleName)
                    .replace("${capitalizedProperty}", capitalizedProperty)
                    .replace("${property}", property);
            String setter = setterTemplate
                    .replace("${dtoClassSimpleName}", dtoClassSimpleName)
                    .replace("${capitalizedProperty}", capitalizedProperty)
                    .replace("${property}", property);
            getterSetters = getterSetters + getter + setter;
        }
        String generated = complexDtoTemplate
                .replace("${dtoClassSimpleName}", dtoClassSimpleName)
                .replace("${dtoSuperClassSimpleName}", dtoSuperClassSimpleName)
                .replace("${dtoGetterSetters}", getterSetters);
        
        return generated;
    }
    
    private static List<Field> getDeclaredNonTransientFields(Class<?> clazz){
        List<Field> fields = new ArrayList<>(clazz.getDeclaredFields().length);
        for (Field field : clazz.getDeclaredFields()){
            if (Modifier.isTransient(field.getModifiers())){
                // skip transient field
                continue;
            }
            fields.add(field);
        }
        return fields;
    }
    
    private static String capitalizeFirstLetter(String string){
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }
}
