package org.atinject.core.websocket.server;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.dto.DTO;
import org.atinject.core.dto.DefaultDTORegistryExtension;
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
    private DefaultDTORegistryExtension dtoRegistryExtension;
    
    private String javascript;
    
    private static final String NEWLINE = "\r\n";
    
    private static final String BASE_JAVASCRIPT_PATH = "/Users/mathieu/Desktop/generated-code/";
    
    @PostConstruct
    public void initialize(){
        
        validateDtoClassSimpleNameDuplicate();
        
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
    
    public void validateDtoClassSimpleNameDuplicate(){
        Set<String> dtoClassesSimpleName = new LinkedHashSet<>();
        List<Class<? extends DTO>> dtoClasses = dtoRegistryExtension.getClasses();
        for (Class<? extends DTO> dtoClass : dtoClasses){
            if (! dtoClassesSimpleName.add(dtoClass.getSimpleName())){
                // TODO add which classes are responsible
                // TODO throw only after full analysis
                throw new RuntimeException("duplicate found");
            }
        }
    }
    
    public String getGeneratedJavascript(){
        return this.javascript;
    }
    
    private static void saveJavascript(String path, String javascript){
        try {
            PrintWriter writer = new PrintWriter(path);
            writer.write(javascript);
            writer.close();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    
    public String generate(Class<? extends DTO> dtoClass){
        if (dtoClass.equals(DTO.class)){
            String javascript = generateDTO(DTO.class);
            String path = BASE_JAVASCRIPT_PATH + DTO.class.getSimpleName() + ".js";
            saveJavascript(path, javascript);
            return javascript;
        }
        else if (dtoClass.equals(WebSocketRequest.class)){
            String javascript = generateBaseWebSocketRequest(WebSocketRequest.class);
            String path = BASE_JAVASCRIPT_PATH + WebSocketRequest.class.getSimpleName() + ".js";
            saveJavascript(path, javascript);
            return javascript;
        }
        else if (dtoClass.equals(WebSocketNotification.class)){
            String javascript = generateBaseWebSocketNotification(WebSocketNotification.class);
            String path = BASE_JAVASCRIPT_PATH + WebSocketNotification.class.getSimpleName() + ".js";
            saveJavascript(path, javascript);
            return javascript;
        }
        else if (dtoClass.equals(WebSocketResponse.class)){
            String javascript = generateBaseWebSocketResponse(WebSocketResponse.class);
            String path = BASE_JAVASCRIPT_PATH + WebSocketResponse.class.getSimpleName() + ".js";
            saveJavascript(path, javascript);
            return javascript;
        }
        else if (WebSocketRequest.class.isAssignableFrom(dtoClass)){
            String javascript = generateRequest((Class<? extends WebSocketRequest>) dtoClass);
            String path = BASE_JAVASCRIPT_PATH + dtoClass.getSimpleName() + ".js";
            saveJavascript(path, javascript);
            return javascript;
        }
        else if (WebSocketNotification.class.isAssignableFrom(dtoClass)){
            String javascript = generateNotification((Class<? extends WebSocketNotification>) dtoClass);
            String path = BASE_JAVASCRIPT_PATH + dtoClass.getSimpleName() + ".js";
            saveJavascript(path, javascript);
            return javascript;
        }
        else if (WebSocketResponse.class.isAssignableFrom(dtoClass)){
            String javascript = generateResponse((Class<? extends WebSocketResponse>) dtoClass);
            String path = BASE_JAVASCRIPT_PATH + dtoClass.getSimpleName() + ".js";
            saveJavascript(path, javascript);
            return javascript;
        }
        else if (DTO.class.isAssignableFrom(dtoClass)){
            String javascript = generateComplexDTO(dtoClass);
            String path = BASE_JAVASCRIPT_PATH + dtoClass.getSimpleName() + ".js";
            saveJavascript(path, javascript);
            return javascript;
        }
        throw new RuntimeException();
    }
    
    public static final String dtoTemplate =
"define([]," + NEWLINE +
"function(){" + NEWLINE +
"    function ${dtoClassSimpleName}() {" + NEWLINE +
"    };" + NEWLINE +
"    " + NEWLINE +
"    ${dtoClassSimpleName}.prototype.getClass = function() {" + NEWLINE +
"        return this.${jsonTypeInfoProperty};" + NEWLINE +
"    };" + NEWLINE +
"    " + NEWLINE +
"    return ${dtoClassSimpleName};" + NEWLINE +
"});" + NEWLINE;
    
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
"define([${dtoDependenciesName}]," + NEWLINE +
"function(${dtoDependenciesValue}){" + NEWLINE +
"    function ${dtoClassSimpleName}() {" + NEWLINE +
"        this._class = \"${dtoClassCanonicalName}\"" + NEWLINE +
"    };" + NEWLINE +
"" + NEWLINE +
"    ${dtoClassSimpleName}.prototype = new ${dtoSuperClassSimpleName}();" + NEWLINE +
"" + NEWLINE +
"    ${dtoGetterSetters}" +  NEWLINE +
"    " + NEWLINE +
"    return ${dtoClassSimpleName};" + NEWLINE +
"});" + NEWLINE;
    
    public static final String getterTemplate =
"    ${dtoClassSimpleName}.prototype.get${capitalizedProperty} = function() {" + NEWLINE +
"        return this.${property};" + NEWLINE +
"    };" + NEWLINE +
"" +  NEWLINE;
    
    public static final String setterTemplate =
"    ${dtoClassSimpleName}.prototype.set${capitalizedProperty} = function(${property}) {" + NEWLINE +
"        this.${property} = ${property};" + NEWLINE +
"        return this;" + NEWLINE +
"    };" + NEWLINE +
"" +  NEWLINE;
    
    private static String generateComplexDTO(Class<? extends DTO> dtoClass){
        String dtoClassSimpleName = dtoClass.getSimpleName();
        String dtoClassCanonicalName = dtoClass.getCanonicalName();
        String dtoSuperClassSimpleName = dtoClass.getSuperclass().getSimpleName();
        
        Set<Class<?>> dependencies = getDependencies(dtoClass);
        
        String dtoDependenciesName = "";
        for (Class<?> dependency : dependencies){
            dtoDependenciesName = dtoDependenciesName + "\"" + dependency.getSimpleName() + "\", ";
        }
        if (! dtoDependenciesName.isEmpty()){
            dtoDependenciesName = dtoDependenciesName.substring(0, dtoDependenciesName.lastIndexOf(","));
        }
        
        String dtoDependenciesValue = "";
        for (Class<?> dependency : dependencies){
            dtoDependenciesValue = dtoDependenciesValue + dependency.getSimpleName() + ", ";
        }
        if (! dtoDependenciesName.isEmpty()){
            dtoDependenciesValue = dtoDependenciesValue.substring(0, dtoDependenciesValue.lastIndexOf(","));
        }
        
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
                .replace("${dtoDependenciesName}", dtoDependenciesName)
                .replace("${dtoDependenciesValue}", dtoDependenciesValue)
                .replace("${dtoClassSimpleName}", dtoClassSimpleName)
                .replace("${dtoClassCanonicalName}", dtoClassCanonicalName)
                .replace("${dtoSuperClassSimpleName}", dtoSuperClassSimpleName)
                .replace("${dtoGetterSetters}", getterSetters);
        
        return generated;
    }
    
public static final String CLASS_ALIAS_TEMPLATE =
"define([${dtoDependenciesName}]," + NEWLINE +
"function(${dtoDependenciesValue}){" + NEWLINE +
"    var classAlias = new Array();" + NEWLINE +
"    ${dtoAliases}" + NEWLINE +
"    return classAlias;" + NEWLINE +
"});" + NEWLINE;

    public String generateRequireJs(){
        List<Class<? extends DTO>> dtoClasses = dtoRegistryExtension.getClasses();
        
        String dtoDependenciesName = "";
        for (Class<? extends DTO> dtoClass : dtoClasses){
            dtoDependenciesName = dtoDependenciesName + "\"" + dtoClass.getSimpleName() + "\", ";
        }
        dtoDependenciesName = dtoDependenciesName.substring(0, dtoDependenciesName.lastIndexOf(","));

        String dtoDependenciesValue = "";
        for (Class<? extends DTO> dtoClass : dtoClasses){
            dtoDependenciesName = dtoDependenciesName + "\"" + dtoClass.getSimpleName() + "\", ";
        }
        dtoDependenciesName = dtoDependenciesName.substring(0, dtoDependenciesName.lastIndexOf(","));        
        
        return CLASS_ALIAS_TEMPLATE
                .replace("${dtoDependenciesName}", dtoDependenciesName)
                .replace("${dtoDependenciesValue}", dtoDependenciesValue)
                .replace("${dtoAliases}", "");
    }
    
    public static final String CLASS_ALIAS_ARRAY_TEMPLATE =
"    ${dtoClassSimpleName}: \"src/org/atinject/dto/${dtoClassSimpleName}\"," + NEWLINE;
    
    public static String generateRequireJsPaths(Class<? extends DTO> dtoClass){
        String dtoClassSimpleName = dtoClass.getSimpleName();
        return CLASS_ALIAS_ARRAY_TEMPLATE
                .replace("${dtoClassSimpleName}", dtoClassSimpleName);
    }
    
    public static final String aliasTemplate =
"classAlias[\"${dtoClassCanonicalName}\"] = ${dtoClassSimpleName};" + NEWLINE;
    
    public String generateAlias(Class<? extends DTO> dtoClass){
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
    
    private static Set<Class<?>> getDependencies(Class<? extends DTO> dtoClass){
        Set<Class<?>> dependencies = new LinkedHashSet<>();
        dependencies.add(dtoClass.getSuperclass());
        for (Field field : getDeclaredNonTransientNonStaticFields(dtoClass)){
            if (DTO.class.isAssignableFrom(field.getType())){
                dependencies.add(field.getType());
            }
        }
        return dependencies;
    }
    
    private static String capitalizeFirstLetter(String string){
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }
}
