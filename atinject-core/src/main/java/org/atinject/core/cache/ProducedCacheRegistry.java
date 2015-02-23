package org.atinject.core.cache;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProducedCacheRegistry {

	private Map<String, LocalCache<?, ?>> caches;
	
	@PostConstruct
	public void initialize() {
		caches = new LinkedHashMap<>();
	}
	
	public void addCache(String name, LocalCache<?, ?> cache) {
		caches.put(name, cache);
	}
	
	public <K, V> LocalCache<K, V> getCache(String name) {
		return (LocalCache<K, V>) caches.get(name);
	}
}
