package org.atinject.core.concurrent;

import java.util.HashMap;
import java.util.Map;

public final class InheritableContext {

    private static final ThreadLocal<Map<Object, Object>> context = new InheritableThreadLocal<>();
    
    private InheritableContext() {
        
    }
    
    public static Map<Object, Object> get() {
        return context.get();
    }
    
    public static Object get(Object key) {
        Map<Object, Object> contextMap = context.get();
        if (contextMap == null) {
            return null;
        }
        return contextMap.get(key);
    }
    
    public static void set(Object key, Object value) {
        Map<Object, Object> contextMap = context.get();
        if (contextMap == null) {
            contextMap = new HashMap<>();
            context.set(contextMap);
        }
        contextMap.put(key, value);
    }
    
    public static void remove() {
        context.remove();
    }
    
    public static void remove(Object key) {
        Map<Object, Object> contextMap = context.get();
        if (contextMap == null) {
            throw new NullPointerException();
        }
        contextMap.remove(key);
    }
}
