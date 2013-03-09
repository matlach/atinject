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
import org.atinject.core.websocket.dto.WebSocketNotification;
import org.atinject.core.websocket.dto.WebSocketRequest;
import org.atinject.core.websocket.dto.WebSocketResponse;
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
        List<Class<? extends DTO>> dtoClasses = dtoRegistryExtension.getClasses();
        javascript = "";
        for (Class<? extends DTO> dtoClass : dtoClasses){
            javascript = javascript + generate(dtoClass);
        }
        
        javascript = javascript + "var classAlias = new Array();" + NEWLINE;
        for (Class<? extends DTO> dtoClass : dtoClasses){ // should only get non abstract class ?
            javascript = javascript + generateAlias(dtoClass);
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
        else if (dtoClass.equals(WebSocketRequest.class)){
            return generateBaseWebSocketRequest(WebSocketRequest.class);
        }
        else if (dtoClass.equals(WebSocketNotification.class)){
            return generateBaseWebSocketNotification(WebSocketNotification.class);
        }
        else if (dtoClass.equals(WebSocketResponse.class)){
            return generateBaseWebSocketResponse(WebSocketResponse.class);
        }
        else if (WebSocketRequest.class.isAssignableFrom(dtoClass)){
            return generateRequest((Class<? extends WebSocketRequest>) dtoClass);
        }
        else if (WebSocketNotification.class.isAssignableFrom(dtoClass)){
            return generateNotification((Class<? extends WebSocketNotification>) dtoClass);
        }
        else if (WebSocketResponse.class.isAssignableFrom(dtoClass)){
            return generateResponse((Class<? extends WebSocketResponse>) dtoClass);
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
    public static String generateBaseWebSocketRequest(Class<WebSocketRequest> baseWebSocketRequestClass){
        return generateComplexDTO(baseWebSocketRequestClass);
    }
    
    public static String generateBaseWebSocketNotification(Class<WebSocketNotification> baseWebSocketNotificationClass){
        return generateComplexDTO(baseWebSocketNotificationClass);
    }
    
    public static String generateBaseWebSocketResponse(Class<WebSocketResponse> baseWebSocketResponseClass){
        return generateComplexDTO(baseWebSocketResponseClass);
    }
    
    public static String generateRequest(Class<? extends WebSocketRequest> webSocketRequestClass){
        return generateComplexDTO(webSocketRequestClass);
    }
    
    public static String generateResponse(Class<? extends WebSocketResponse> webSocketResponseClass){
        return generateComplexDTO(webSocketResponseClass);
    }
    
    public static String generateNotification(Class<? extends WebSocketNotification> webSocketNotificationClass){
        return generateComplexDTO(webSocketNotificationClass);
    }

    public static final String complexDtoTemplate =
"function ${dtoClassSimpleName}() {" + NEWLINE +
"    this._class = \"${dtoClassCanonicalName}\"" + NEWLINE +
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
"${dtoClassSimpleName}.prototype.set${capitalizedProperty} = function(${property}) {" + NEWLINE +
"    this.${property} = ${property};" + NEWLINE +
"    return this;" + NEWLINE +
"};" + NEWLINE +
"" +  NEWLINE;
    
    private static String generateComplexDTO(Class<? extends DTO> dtoClass){
        String dtoClassSimpleName = dtoClass.getSimpleName();
        String dtoClassCanonicalName = dtoClass.getCanonicalName();
        String dtoSuperClassSimpleName = dtoClass.getSuperclass().getSimpleName();
        
        String getterSetters = "";
        for (Field field : getDeclaredNonTransientNonStaticFields(dtoClass)){
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
                .replace("${dtoClassCanonicalName}", dtoClassCanonicalName)
                .replace("${dtoSuperClassSimpleName}", dtoSuperClassSimpleName)
                .replace("${dtoGetterSetters}", getterSetters);
        
        return generated;
    }
    
    public static final String aliasTemplate =
"classAlias[\"${dtoClassCanonicalName}\"] = ${dtoClassSimpleName};" + NEWLINE;
    
    public static String generateAlias(Class<? extends DTO> dtoClass){
        String dtoClassCanonicalName = dtoClass.getCanonicalName();
        String dtoClassSimpleName = dtoClass.getSimpleName();
        return aliasTemplate
                .replace("${dtoClassCanonicalName}", dtoClassCanonicalName)
                .replace("${dtoClassSimpleName}", dtoClassSimpleName);
    }
    
    private static List<Field> getDeclaredNonTransientNonStaticFields(Class<?> clazz){
        List<Field> fields = new ArrayList<>(clazz.getDeclaredFields().length);
        for (Field field : clazz.getDeclaredFields()){
            if (Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers())){
                // skip transient and static field
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
