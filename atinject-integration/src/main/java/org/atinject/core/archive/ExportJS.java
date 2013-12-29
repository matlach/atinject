package org.atinject.core.archive;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.atinject.core.dto.DTO;
import org.atinject.core.websocket.dto.WebSocketNotification;
import org.atinject.core.websocket.dto.WebSocketRequest;
import org.atinject.core.websocket.dto.WebSocketResponse;
import org.atinject.core.websocket.dto.WebSocketResponseException;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

public class ExportJS {

    public static void main(String[] args) throws Exception {
        new ExportJS().initialize();
    }
    
    private List<Class<?>> classes;
    
    private List<Class<? extends DTO>> dtoClasses;
    
    private static final String NEWLINE = "\r\n";
    
    private String BASE_JAVASCRIPT_PATH;
    
    public void initialize() throws Exception{

        BASE_JAVASCRIPT_PATH = Paths.get("src/main/resources/webapp/scripts/atinject").toFile().getAbsolutePath();
        
        JavaArchive atinjectArchive = ShrinkWrap.create(JavaArchive.class, "atinject.jar") 
                .addPackages(true, "org.atinject");
        
        classes = new ArrayList<>();
        dtoClasses = new ArrayList<>();
        Map<ArchivePath, Node> content = atinjectArchive.getContent();
        for (Entry<ArchivePath, Node> entry : content.entrySet()) {
            String nodePath = entry.getValue().getPath().get();
            if (nodePath.contains(".class")) {
                nodePath = nodePath.replace(".class", "").replace("/", ".");
                nodePath = nodePath.substring(1);
                
                Class<?> clazz = Class.forName(nodePath);
                classes.add(clazz);
                if (DTO.class.isAssignableFrom(clazz)) {
                    if (nodePath.contains("$")){
                        throw new RuntimeException("internal class are not allowed" + clazz);
                    }
                    dtoClasses.add((Class<? extends DTO>) clazz);
                }
            }
        }
        sortByModule(dtoClasses);
        
        validateDtoClassSimpleNameDuplicate();
        
        generateDTOPrototypes();
        
        generateClassAlias();
        
    }
    
    public void generateDTOPrototypes() {
        for (Class<? extends DTO> dtoClass : dtoClasses){
            generate(dtoClass);
        }        
    }
    
    public void generateClassAlias() {
        String javascript = generateClassAliasJs();
        String path = BASE_JAVASCRIPT_PATH + "/" + "CLASS_ALIAS" + ".js";
        saveJavascript(path, javascript);
    }
    
    public void validateDtoClassSimpleNameDuplicate(){
        Set<String> dtoClassesSimpleName = new LinkedHashSet<>();
        for (Class<? extends DTO> dtoClass : dtoClasses){
            if (! dtoClassesSimpleName.add(dtoClass.getSimpleName())){
                // TODO add which classes are responsible
                // TODO throw only after full analysis
                throw new RuntimeException("duplicate found");
            }
        }
    }
    
    private static void saveJavascript(String path, String javascript){
        File file = new File(path);
        file.getParentFile().mkdirs();
        try (PrintWriter writer = new PrintWriter(file);) {
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
            String path = BASE_JAVASCRIPT_PATH + "/" + DTO.class.getSimpleName() + ".js";
            saveJavascript(path, javascript);
            return javascript;
        }
        else if (dtoClass.equals(WebSocketRequest.class)){
            String javascript = generateBaseWebSocketRequest(WebSocketRequest.class);
            String path = BASE_JAVASCRIPT_PATH + "/" + WebSocketRequest.class.getSimpleName() + ".js";
            saveJavascript(path, javascript);
            return javascript;
        }
        else if (dtoClass.equals(WebSocketNotification.class)){
            String javascript = generateBaseWebSocketNotification(WebSocketNotification.class);
            String path = BASE_JAVASCRIPT_PATH + "/" + WebSocketNotification.class.getSimpleName() + ".js";
            saveJavascript(path, javascript);
            return javascript;
        }
        else if (dtoClass.equals(WebSocketResponse.class)){
            String javascript = generateBaseWebSocketResponse(WebSocketResponse.class);
            String path = BASE_JAVASCRIPT_PATH + "/" + WebSocketResponse.class.getSimpleName() + ".js";
            saveJavascript(path, javascript);
            return javascript;
        }
        else if (dtoClass.equals(WebSocketResponseException.class)){
            String javascript = generateComplexDTO(WebSocketResponseException.class);
            String path = BASE_JAVASCRIPT_PATH + "/" + WebSocketResponseException.class.getSimpleName() + ".js";
            saveJavascript(path, javascript);
            return javascript;
        }
        else if (WebSocketRequest.class.isAssignableFrom(dtoClass)){
            String javascript = generateRequest((Class<? extends WebSocketRequest>) dtoClass);
            String path = BASE_JAVASCRIPT_PATH + "/" + getModule(dtoClass) + "/" + dtoClass.getSimpleName() + ".js";
            saveJavascript(path, javascript);
            return javascript;
        }
        else if (WebSocketNotification.class.isAssignableFrom(dtoClass)){
            String javascript = generateNotification((Class<? extends WebSocketNotification>) dtoClass);
            String path = BASE_JAVASCRIPT_PATH + "/" + getModule(dtoClass) + "/" + dtoClass.getSimpleName() + ".js";
            saveJavascript(path, javascript);
            return javascript;
        }
        else if (WebSocketResponse.class.isAssignableFrom(dtoClass)){
            String javascript = generateResponse((Class<? extends WebSocketResponse>) dtoClass);
            String path = BASE_JAVASCRIPT_PATH + "/" + getModule(dtoClass) + "/" + dtoClass.getSimpleName() + ".js";
            saveJavascript(path, javascript);
            return javascript;
        }
        else if (DTO.class.isAssignableFrom(dtoClass)){
            String javascript = generateComplexDTO(dtoClass);
            String path = BASE_JAVASCRIPT_PATH + "/" + getModule(dtoClass) + "/" + dtoClass.getSimpleName() + ".js";
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
"    ${dtoClassSimpleName}.prototype.getClassSimpleName = function() {" + NEWLINE +
"        this.getClass().split(\".\").slice(-1)[0]" + NEWLINE + // TODO this will need some kind of optimization
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
        
        Set<Class<? extends DTO>> dependencies = getDependencies(dtoClass);
        
        String dtoDependenciesName = "";
        for (Class<? extends DTO> dependency : dependencies){
            dtoDependenciesName = dtoDependenciesName + "\"atinject/" + getModule(dependency) + "/" + dependency.getSimpleName() + "\", ";
        }
        if (! dtoDependenciesName.isEmpty()){
            dtoDependenciesName = dtoDependenciesName.replace("//", "/");
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

    public String generateClassAliasJs(){
        
        String dtoDependenciesName = "";
        for (Class<? extends DTO> dtoClass : dtoClasses){
            dtoDependenciesName = dtoDependenciesName + "\"atinject/" + getModule(dtoClass) + "/" + dtoClass.getSimpleName() + "\", ";
        }
        dtoDependenciesName = dtoDependenciesName.replace("//", "/");
        dtoDependenciesName = dtoDependenciesName.substring(0, dtoDependenciesName.lastIndexOf(","));

        String dtoDependenciesValue = "";
        for (Class<? extends DTO> dtoClass : dtoClasses){
            dtoDependenciesValue = dtoDependenciesValue + dtoClass.getSimpleName() + ", ";
        }
        dtoDependenciesValue = dtoDependenciesValue.substring(0, dtoDependenciesValue.lastIndexOf(","));        
        
        String dtoAliases = "";
        for (Class<? extends DTO> dtoClass : dtoClasses){
            dtoAliases = dtoAliases + generateAlias(dtoClass);
        }
        
        return CLASS_ALIAS_TEMPLATE
                .replace("${dtoDependenciesName}", dtoDependenciesName)
                .replace("${dtoDependenciesValue}", dtoDependenciesValue)
                .replace("${dtoAliases}", dtoAliases);
    }
    
    public static final String CLASS_ALIAS_ARRAY_TEMPLATE =
"    ${dtoClassSimpleName}: \"src/org/atinject/dto/${dtoClassSimpleName}\"," + NEWLINE;
    
    public static String generateClassAliasJsPaths(Class<? extends DTO> dtoClass){
        String dtoClassSimpleName = dtoClass.getSimpleName();
        return CLASS_ALIAS_ARRAY_TEMPLATE
                .replace("${dtoClassSimpleName}", dtoClassSimpleName);
    }
    
    public static final String aliasTemplate =
"    classAlias[\"${dtoClassCanonicalName}\"] = ${dtoClassSimpleName};" + NEWLINE;
    
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
    
    private static Set<Class<? extends DTO>> getDependencies(Class<? extends DTO> dtoClass){
        Set<Class<? extends DTO>> dependencies = new LinkedHashSet<>();
        dependencies.add((Class<? extends DTO>) dtoClass.getSuperclass());
        for (Field field : getDeclaredNonTransientNonStaticFields(dtoClass)){
            if (DTO.class.isAssignableFrom(field.getType())){
                dependencies.add((Class<? extends DTO>) field.getType());
            }
        }
        return dependencies;
    }
    
    private static String capitalizeFirstLetter(String string){
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }
    
    private static void sortByModule(List<Class<? extends DTO>> dtoClasses) {
        Collections.sort(dtoClasses, new Comparator<Class<? extends DTO>>() {
            @Override
            public int compare(Class<? extends DTO> o1, Class<? extends DTO> o2) {
                return getModule(o1).compareTo(getModule(o2));
            }
        });
    }
    
    private static String getModule(Class<? extends DTO> dtoClass) {
        if (DTO.class.equals(dtoClass)) {
            return "";
        }
        if (WebSocketRequest.class.equals(dtoClass)) {
            return "";
        }
        if (WebSocketResponse.class.equals(dtoClass)) {
            return "";
        }
        if (WebSocketResponseException.class.equals(dtoClass)) {
            return "";
        }
        if (WebSocketNotification.class.equals(dtoClass)) {
            return "";
        }
        // if following guideline, it should always be global.namespace.module.dto.DtoClass
        String[] canonicalNameParts = dtoClass.getCanonicalName().split("\\.");
        return canonicalNameParts[canonicalNameParts.length -1 -2];
    }
}
