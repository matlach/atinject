package org.atinject.core.cache;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Collectors;

import org.atinject.core.profiling.Profile;
import org.atinject.core.thread.ThreadTracker;
import org.atinject.core.tiers.exception.HandleCacheException;
import org.infinispan.AdvancedCache;
import org.infinispan.container.entries.CacheEntry;
import org.infinispan.context.Flag;
import org.infinispan.filter.KeyValueFilter;

@HandleCacheException
@Profile
@ThreadTracker
public class LocalCache<K, V> implements Iterable<CacheEntry<K, V>>{

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
    
    public V get(K key) {
        return cache.get(key);
    }

    public Map<K, V> getAll(K... keys) {
        return getAll(Arrays.asList(keys));
    }
    
    public Map<K, V> getAll(Collection<K> keys) {
        return keys.stream()
                .collect(Collectors.toMap(key -> key, key -> get(key)));
    }
    
    public boolean lockInterruptibly(K key) {
    	return cacheWithZeroLockAcquisitionTimeoutAndFailSilently.lock(key);
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
    
    public int size() {
        return cache.size();
    }

    public Set<K> keySet() {
    	return cache.keySet();
    }
    
    public Collection<V> values() {
        return cache.values();
    }

    @Override
	public Iterator<CacheEntry<K, V>> iterator() {
    	return iterator((key, value, metadata) -> true);
    }
    
    public Iterator<CacheEntry<K, V>> iterator(KeyValueFilter<? super K, ? super V> filter) {
    	return cache.filterEntries(filter).iterator();
    }
    
    @Override
	public Spliterator<CacheEntry<K, V>> spliterator() {
    	return spliterator((key, value, metadata) -> true);
    }
    
    public Spliterator<CacheEntry<K, V>> spliterator(KeyValueFilter<? super K, ? super V> filter) {
    	return cache.filterEntries(filter).spliterator();
    }
    
    public void clear() {
        cache.clear();
    }
    
    public AdvancedCache<K, V> unwrap() {
        return cache;
    }
}
