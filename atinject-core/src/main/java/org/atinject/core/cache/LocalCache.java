package org.atinject.core.cache;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.atinject.core.profiling.Profile;
import org.atinject.core.thread.ThreadTracker;
import org.atinject.core.tiers.exception.HandleCacheException;
import org.infinispan.AdvancedCache;
import org.infinispan.context.Flag;

@HandleCacheException
@Profile
@ThreadTracker
public class LocalCache<K, V> {

    protected AdvancedCache<K, V> cache;
    
    LocalCache<K, V> withCache(org.infinispan.Cache<K, V> cache) {
        if (this.cache != null) {
            throw new IllegalStateException("cannot reassign cache");
        }
        this.cache = cache.getAdvancedCache()
                .withFlags(Flag.IGNORE_RETURN_VALUES);
        return this;
    }
    
    public V get(K key) {
        return cache.get(key);
    }
    
    public Map<K, V> getAll(K... keys) {
        return getAll(Arrays.asList(keys));
    }
    
    public Map<K, V> getAll(Collection<K> keys){
        Map<K, V> map = new HashMap<>(keys.size());
        for (K key : keys) {
            map.put(key, get(key));
        }
        return map;
    }
    
    public boolean lockInterruptibly(K key) {
    	return cache.withFlags(Flag.ZERO_LOCK_ACQUISITION_TIMEOUT, Flag.FAIL_SILENTLY).lock(key);
    }
    
    public boolean lock(K key) {
        return cache.lock(key);
    }
    
    public boolean lockAll(K... keys) {
        return cache.lock(keys);
    }
    
    public boolean lockAll(Collection<K> keys) {
        return cache.lock(keys);
    }
    
    public void put(K key, V value) {
        cache.put(key, value);
    }
    
    public void putAll(Entry<K, V>... entries) {
        Map<K, V> map = new HashMap<>(entries.length);
        for (Entry<K, V> entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
        putAll(map);
    }
    
    public void putAll(Map<K, V> entries) {
        cache.putAll(entries);
    }
    
    /**
     * Note : if pessimistic transaction are used, ensure {@link #lock(key)} has been called before
     */
    public void remove(K key) {
        cache.remove(key);
    }
    
    public void removeAll(K... keys) {
        removeAll(Arrays.asList(keys));
    }
    
    public void removeAll(Collection<K> keys) {
        for (K key : keys) {
            cache.remove(key);
        }
    }
    
    public int size() {
        return cache.size();
    }

    public Set<K> keySet() {
    	return cache.keySet();
    }
    
    public Collection<V> values() {
        return cache.values();
    }

    public void clear() {
        cache.clear();
    }
    
    public AdvancedCache<K, V> unwrap() {
        return cache;
    }
}
