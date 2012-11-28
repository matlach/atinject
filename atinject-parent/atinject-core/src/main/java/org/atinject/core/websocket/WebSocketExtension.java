package org.atinject.core.websocket;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.atinject.api.session.Session;

public class WebSocketExtension implements Extension
{
 
    private Map<Class<? extends BaseWebSocketRequest>, WebSocketMessageMethod> messages;
    private WebSocketOpenMethod open; // TODO list<WebSocket...>
    private WebSocketCloseMethod close; // TODO list<WebSocket...>
    
    public WebSocketExtension()
    {
        messages = new LinkedHashMap<>();
    }
    
    static class WebSocketMessageMethod
    {
        private Method webSocketMessageMethod;
        private boolean injectSessionParameter;
        private boolean webSocketRequestFirstParameter;
        private Object target;
        
        public Method getWebSocketMessageMethod()
        {
            return webSocketMessageMethod;
        }
        public void setWebSocketMessageMethod(Method webSocketMessageMethod)
        {
            this.webSocketMessageMethod = webSocketMessageMethod;
        }
        public boolean isInjectSessionParameter()
        {
            return injectSessionParameter;
        }
        public void setInjectSessionParameter(boolean injectSessionParameter)
        {
            this.injectSessionParameter = injectSessionParameter;
        }
        public boolean isWebSocketRequestFirstParameter()
        {
            return webSocketRequestFirstParameter;
        }
        public void setWebSocketRequestFirstParameter(boolean webSocketRequestFirstParameter)
        {
            this.webSocketRequestFirstParameter = webSocketRequestFirstParameter;
        }
        public Object getTarget()
        {
            return target;
        }
        public void setTarget(Object target)
        {
            this.target = target;
        }
    }
    
    static class WebSocketOpenMethod
    {
        private Method openMethod;
        private boolean injectSessionParameter;
        private Object target;
        
        public Method getOpenMethod()
        {
            return openMethod;
        }
        public void setOpenMethod(Method openMethod)
        {
            this.openMethod = openMethod;
        }
        public boolean isInjectSessionParameter()
        {
            return injectSessionParameter;
        }
        public void setInjectSessionParameter(boolean injectSessionParameter)
        {
            this.injectSessionParameter = injectSessionParameter;
        }
        public Object getTarget()
        {
            return target;
        }
        public void setTarget(Object target)
        {
            this.target = target;
        }
    }
    
    static class WebSocketCloseMethod
    {
        private Method closeMethod;
        private boolean injectSessionParameter;
        private Object target;
        
        public Method getCloseMethod()
        {
            return closeMethod;
        }
        public void setCloseMethod(Method closeMethod)
        {
            this.closeMethod = closeMethod;
        }
        public boolean isInjectSessionParameter()
        {
            return injectSessionParameter;
        }
        public void setInjectSessionParameter(boolean injectSessionParameter)
        {
            this.injectSessionParameter = injectSessionParameter;
        }
        public Object getTarget()
        {
            return target;
        }
        public void setTarget(Object target)
        {
            this.target = target;
        }
    }
    
    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> event) {

        Class<T> clazz = event.getAnnotatedType().getJavaClass();
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
        if (! BaseWebSocketResponse.class.isAssignableFrom(method.getReturnType()))
        {
            throw new RuntimeException();
        }
        
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 0)
        {
            throw new RuntimeException();
        }
        if (parameterTypes.length > 2)
        {
            throw new RuntimeException();
        }
        
        WebSocketMessageMethod webSocketMessageMethod = new WebSocketMessageMethod();
        webSocketMessageMethod.setWebSocketMessageMethod(method);
        int parameterIndex = 0;
        for (Class<?> parameterType : parameterTypes)
        {
            if (BaseWebSocketRequest.class.isAssignableFrom(parameterType))
            {
                if (messages.containsKey(parameterType))
                {
                    throw new RuntimeException();
                }
                webSocketMessageMethod.setWebSocketRequestFirstParameter(parameterIndex == 0);
                messages.put((Class<? extends BaseWebSocketRequest>) parameterType, webSocketMessageMethod);
            }
            else if (Session.class.isAssignableFrom(parameterType))
            {
                webSocketMessageMethod.setInjectSessionParameter(true);
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
        if (open != null)
        {
            throw new RuntimeException();
        }
        
        if (method.getReturnType() != Void.class)
        {
            throw new RuntimeException();
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length > 1)
        {
            throw new RuntimeException();
        }
        
        WebSocketOpenMethod webSocketOpenMethod = new WebSocketOpenMethod();
        webSocketOpenMethod.setOpenMethod(method);
        if (parameterTypes.length == 1)
        {
            if (! Session.class.isAssignableFrom(parameterTypes[0]))
            {
                throw new RuntimeException();
            }
            webSocketOpenMethod.setInjectSessionParameter(true);
        }
        
        open = webSocketOpenMethod;
    }
    
    private void processWebSocketCloseMethod(Method method)
    {
        if (close != null)
        {
            throw new RuntimeException();
        }
        
        if (method.getReturnType() != Void.class)
        {
            throw new RuntimeException();
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length > 1)
        {
            throw new RuntimeException();
        }
        
        WebSocketCloseMethod webSocketCloseMethod = new WebSocketCloseMethod();
        webSocketCloseMethod.setCloseMethod(method);
        if (parameterTypes.length == 1)
        {
            if (! Session.class.isAssignableFrom(parameterTypes[0]))
            {
                throw new RuntimeException();
            }
            webSocketCloseMethod.setInjectSessionParameter(true);
        }
        
        close = webSocketCloseMethod;
    }
    
    public WebSocketMessageMethod getWebSocketMessageMethod(Class<? extends BaseWebSocketRequest> request)
    {
        return messages.get(request);
    }
    
    public WebSocketOpenMethod getWebSocketOpenMethod()
    {
        return open;
    }

    public WebSocketCloseMethod getWebSocketCloseMethod()
    {
        return close;
    }
    
    public void onAfterDeploymentValidation(@Observes AfterDeploymentValidation event, BeanManager manager)
    {
        for (WebSocketMessageMethod webSocketMessageMethod : messages.values())
        {
            Bean<?> bean = manager.getBeans(webSocketMessageMethod.getWebSocketMessageMethod().getDeclaringClass()).iterator().next();
            Object target = manager.getReference(bean, bean.getBeanClass(), manager.createCreationalContext(bean));
            webSocketMessageMethod.setTarget(target);
        }
        
        if (open != null)
        {
            Bean<?> bean = manager.getBeans(open.getOpenMethod().getDeclaringClass()).iterator().next();
            Object target = manager.getReference(bean, bean.getBeanClass(), manager.createCreationalContext(bean));
            open.setTarget(target);
        }
        
        if (close != null)
        {
            Bean<?> bean = manager.getBeans(close.getCloseMethod().getDeclaringClass()).iterator().next();
            Object target = manager.getReference(bean, bean.getBeanClass(), manager.createCreationalContext(bean));
            close.setTarget(target);
        }
    }
}
