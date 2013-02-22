package org.atinject.tool;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.atinject.api.user.dto.GetUserRequest;
import org.atinject.core.dto.DTO;
import org.atinject.core.websocket.dto.BaseWebSocketRequest;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

public class DTOToJavascript
{
    public static void main(String[] args){
        System.out.println(generateDTO());
        System.out.println(generateBaseWebSocketRequest());
        System.out.println(generateBaseWebSocketNotification());
        System.out.println(generateBaseWebSocketResponse());
        
    }
    
    public static final String dtoTemplate =
"function ${dtoClassSimpleName}() {" +
"};" +
"" +
"${dtoClassSimpleName}.prototype.getClass = function() {" +
"    return this.${jsonTypeInfoProperty};" +
"}";
    
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
    public static String generateDTO(){
        Class<DTO> dtoClass = DTO.class;
        JsonTypeInfo jsonTypeInfoAnnotation = dtoClass.getAnnotation(JsonTypeInfo.class);
        String dtoClassSimpleName = dtoClass.getSimpleName();
        String jsonTypeInfoProperty = jsonTypeInfoAnnotation.property();
        
        String generated = dtoTemplate
                .replace("${dtoClassSimpleName}", dtoClassSimpleName)
                .replace("${jsonTypeInfoProperty}", jsonTypeInfoProperty);
        return generated;
    }
    
    public static final String complexDtoTemplate =
"function ${dtoClassSimpleName}() {" +
"};" +
"" +
"${dtoClassSimpleName}.prototype = new ${dtoSuperClassSimpleName}();" +
"" +
"${dtoGetterSetters}";
    
    public static final String getterTemplate =
"${dtoClassSimpleName}.prototype.get${capitalizedProperty} = function() {" +
"    return this.${property};" +
"};" +
"";
    public static final String setterTemplate =
"${dtoClassSimpleName}.prototype.get${capitalizedProperty} = function(${property}) {" +
"    this.${property} = ${property};" +
"    return this;" +
"};" +
"";
    
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
    public static String generateBaseWebSocketRequest(){
        Class<BaseWebSocketRequest> baseWebSocketRequestClass = BaseWebSocketRequest.class;
        String dtoClassSimpleName = baseWebSocketRequestClass.getSimpleName();
        String dtoSuperClassSimpleName = baseWebSocketRequestClass.getSuperclass().getSimpleName();
        
        String getterSetters = null;
        for (Field field : baseWebSocketRequestClass.getDeclaredFields()){
            if (Modifier.isTransient(field.getModifiers())){
                // skip transient field
                continue;
            }
            field.setAccessible(true);
            String capitalizedProperty = Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
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
    
    public static String generateBaseWebSocketNotification(){
        return "";
    }
    
    public static String generateBaseWebSocketResponse(){
        return "";
    }
    
    public static String generateRequest(){
        Class<GetUserRequest> requestClass = GetUserRequest.class;
        return "";
    }
    
    public static String generateResponse(){
        return "";
    }
    
    public static String generateNotification(){
        return "";
    }
    
}
