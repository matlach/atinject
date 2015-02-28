package org.atinject.core.cache;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.infinispan.AdvancedCache;
import org.infinispan.context.Flag;

//@HandleCacheException
//@Profile
//@ThreadTracker
public class LocalCache<K, V> {

    protected AdvancedCache<K, V> cache;
    protected AdvancedCache<K, V> cacheWithZeroLockAcquisitionTimeoutAndFailSilently;
    
    LocalCache<K, V> withCache(org.infinispan.Cache<K, V> cache) {
        if (this.cache != null) {
            throw new IllegalStateException("cannot reassign cache");
        }
        this.cache = cache.getAdvancedCache()
                .withFlags(Flag.IGNORE_RETURN_VALUES);
        cacheWithZeroLockAcquisitionTimeoutAndFailSilently = this.cache
        		.withFlags(Flag.ZERO_LOCK_ACQUISITION_TIMEOUT, Flag.FAIL_SILENTLY);
        return this;
    }
    
    public Optional<V> get(K key) {
        return Optional.ofNullable(cache.get(key));
    }

    public Map<K, Optional<V>> getAll(K... keys) {
        return getAll(Arrays.asList(keys));
    }
    
    public Map<K, Optional<V>> getAll(Collection<K> keys) {
        return keys.stream()
                .collect(Collectors.toMap(key -> key, key -> get(key)));
    }
    
    public boolean lockInterruptibly(K key) {
    	return cacheWithZeroLockAcquisitionTimeoutAndFailSilently.lock(key);
    }
    
    public void lock(K key) {
        cache.lock(key);
    }
    
    public void lockAll(K... keys) {
        cache.lock(keys);
    }
    
    public void lockAll(Collection<K> keys) {
        cache.lock(keys);
    }
    
    public void put(K key, V value) {
        cache.put(key, value);
    }

    public void putAll(Entry<K, V>... entries) {
        putAll(Arrays.asList(entries));
    }

    public void putAll(Collection<Entry<K, V>> entries) {
    	// TODO eclipse bug
    	// java 8 syntax should be
    	// entries.stream().collect(Collectors.toMap(Entry::getKey(), Entry::getValue()));
    	Map<K, V> map = new HashMap<>();
    	for (Entry<K, V> entry : entries) {
    		map.put(entry.getKey(), entry.getValue());
    	}
    	putAll(map);
    }
    
    public void putAll(Map<K, V> entries) {
        cache.putAll(entries);
    }

    public void remove(K key) {
        cache.remove(key);
    }
    
    public void removeAll(K... keys) {
        removeAll(Arrays.asList(keys));
    }
    
    public void removeAll(Collection<K> keys) {
        keys.forEach(key -> cache.remove(key));
    }
    
    public Stream<Entry<K, V>> stream() {
    	return cache.entrySet().stream();
    }
    
    public Stream<Entry<K, V>> parallelStream() {
    	return cache.entrySet().parallelStream();
    }
    
    public Stream<K> keys() {
    	return cache.keySet().stream();
    }
    
    public Stream<K> parallelKeys() {
    	return cache.keySet().parallelStream();
    }
    
    public Stream<V> values() {
    	return cache.values().stream();
    }
    
    public Stream<V> parallelValues() {
    	return cache.values().parallelStream();
    }
    
    public void clear() {
        cache.clear();
    }
    
    public AdvancedCache<K, V> unwrap() {
        return cache;
    }

}
