package org.atinject.core.lock;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.atinject.core.cache.LocalCache;
import org.atinject.core.cache.ProducedCacheRegistry;

@ApplyPessimisticLocking
@Interceptor
public class ApplyPessimisticLockingInterceptor {

	@Inject
	private ProducedCacheRegistry producedCacheRegistry;
	
    @AroundInvoke
    public Object validateMethod(InvocationContext ctx) throws Exception {
    	Map<LocalCache<Object, Object>, List<Object>> locks = new LinkedHashMap<>();
    	int parameterIndex = 0;
    	for (Parameter parameter : ctx.getMethod().getParameters()) {
    		if (parameter.isAnnotationPresent(PessimisticLock.class)) {
    			String cacheName = parameter.getAnnotation(PessimisticLock.class).value();
    			LocalCache<Object, Object> cache = producedCacheRegistry.getCache(cacheName);
    			locks.putIfAbsent(cache, new ArrayList<>());
    			locks.get(cache).add(ctx.getParameters()[parameterIndex]);
    		}
    		parameterIndex = parameterIndex + 1;
    	}

    	for (Entry<LocalCache<Object, Object>, List<Object>> entry : locks.entrySet()) {
    		entry.getKey().lockAll(entry.getValue());
    	}
        return ctx.proceed();
    }
}
