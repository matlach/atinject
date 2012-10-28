package org.atinject.core.websocket;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.atinject.api.session.Session;

public class WebSocketExtension implements Extension
{
 
    private Map<Class<? extends BaseWebSocketRequest>, WebSocketMessageMethod> messages;
    private WebSocketOpenMethod open;
    private WebSocketCloseMethod close;
    
    public WebSocketExtension()
    {
        messages = new HashMap<>();
    }
    
    static class WebSocketMessageMethod
    {
        private Method webSocketMessageMethod;
        private boolean injectSessionParameter;
        private int webSocketRequestParameterIndex;
        private int sessionParameterIndex;
    }
    
    static class WebSocketOpenMethod
    {
        private Method openMethod;
        private boolean injectSessionParameter;
    }
    
    static class WebSocketCloseMethod
    {
        private Method closeMethod;
        private boolean injectSessionParameter;
    }
    
    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> pat) {

        Class<T> clazz = pat.getAnnotatedType().getJavaClass();
        for (Method method : clazz.getMethods())
        {
            processMethod(method);
        }
        
     }
    
    private void processMethod(Method method)
    {
        if (method.isAnnotationPresent(WebSocketMessage.class))
        {
            processWebSocketMessageMethod(method);
        }
        else if (method.isAnnotationPresent(WebSocketOpen.class))
        {
            processWebSocketOpenMethod(method);
        }
        else if (method.isAnnotationPresent(WebSocketClose.class))
        {
            processWebSocketCloseMethod(method);
        }
    }
    
    private void processWebSocketMessageMethod(Method method)
    {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 0)
        {
            throw new RuntimeException();
        }
        int parameterIndex = 0;
        for (Class<?> parameterType : parameterTypes)
        {
            if (BaseWebSocketRequest.class.isAssignableFrom(parameterType))
            {
                
            }
            else if (Session.class.isAssignableFrom(parameterType))
            {
                
            }
            else
            {
                throw new RuntimeException();
            }
            parameterIndex = parameterIndex + 1;
        }
    }
    
    private void processWebSocketOpenMethod(Method method)
    {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length > 1)
        {
            throw new RuntimeException();
        }
        if (parameterTypes.length == 1)
        {
            if (! Session.class.isAssignableFrom(parameterTypes[0]))
            {
                throw new RuntimeException();
            }
        }
    }
    
    private void processWebSocketCloseMethod(Method method)
    {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length > 1)
        {
            throw new RuntimeException();
        }
        if (parameterTypes.length == 1)
        {
            if (! Session.class.isAssignableFrom(parameterTypes[0]))
            {
                throw new RuntimeException();
            }
        }
    }
}
