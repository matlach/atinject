package org.atinject.core.websocket.server;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.tiers.WebSocketService;
import org.atinject.core.tiers.WebSocketServiceRegistryExtension;
import org.slf4j.Logger;

@ApplicationScoped
public class WebSocketServiceToJavascript {

    @Inject
    private Logger logger;
    
    @Inject
    private WebSocketServiceRegistryExtension webSocketServiceRegistryExtension;
    
    private String javascript;
    
    private static final String NEWLINE = "\r\n";
    
    @PostConstruct
    public void initialize(){
        List<Class<? extends WebSocketService>> webSocketServiceClasses = webSocketServiceRegistryExtension.getClasses();
        
        javascript = "";
        for (Class<? extends WebSocketService> webSocketServiceClass : webSocketServiceClasses){
            javascript = javascript + generate(webSocketServiceClass);
        }
        
        // save generated javascript to file ?
        // that way we could ease frontend development ?
    }
    
    private static final String webSocketServiceTemplate =
"function ${webSocketServiceClassSimpleName}(){" + NEWLINE +
"};" + NEWLINE +
"${webSocketServiceMethods}" + NEWLINE +
"var ${uncapitalizedWebSocketServiceClassSimpleName} = new ${webSocketServiceClassSimpleName}();" + NEWLINE +
"" + NEWLINE;
    
    private static final String webSocketServiceMethod =
"${webSocketServiceClassSimpleName}.prototype.on${dtoClassSimpleName} = function(${uncapitalizedDtoClassSimpleName}){" + NEWLINE +
"    alert(getUserResponse.getClass());" + NEWLINE +
"}" + NEWLINE +
"";   
    
    public String generate(Class<? extends WebSocketService> webSocketServiceClass){
        return null;
    }
    
    private static final String addEventListenerTemplate = "";
    
    private static final String dispatchEventTemplate = "";
}
