package org.atinject.core.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import org.infinispan.AdvancedCache;
import org.infinispan.context.Flag;
import org.infinispan.util.concurrent.NotifyingFuture;

public class InfinispanCache<K, V>
{

    private final AdvancedCache<K, V> cache;
    
    public InfinispanCache(AdvancedCache<K, V> cache)
    {
        this.cache = cache;
    }

    public V get(K key)
    {
        return cache.get(key);
    }
    
    public Map<K, V> getAll(K... keys)
    {
        Map<K, NotifyingFuture<V>> futures = new HashMap<>(keys.length);
        for (K key : keys)
        {
            NotifyingFuture<V> future = cache.getAsync(key);
            futures.put(key, future);
        }
        
        Map<K, V> values = new HashMap<>(keys.length);
        for (Entry<K, NotifyingFuture<V>> entry : futures.entrySet())
        {
            try
            {
                V value = entry.getValue().get();
                values.put(entry.getKey(), value);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
            catch (ExecutionException e)
            {
                // log exception as error
                // swallow exception
            }
        }
        return values;
    }
    
    public void lock(K... keys)
    {
        cache.lock(keys);
    }
    
    public void put(K key, V value)
    {
        cache.withFlags(Flag.IGNORE_RETURN_VALUES).put(key, value);
    }
    
    public void putAll(Map<K, V> m)
    {
        cache.withFlags(Flag.IGNORE_RETURN_VALUES).putAll(m);
    }

    public void remove(K key)
    {
        cache.withFlags(Flag.IGNORE_RETURN_VALUES).remove(key);
    }
    
    public void removeAll(K... keys)
    {
        Map<K, NotifyingFuture<V>> futures = new HashMap<>(keys.length);
        for (K key : keys)
        {
            NotifyingFuture<V> future = cache.withFlags(Flag.IGNORE_RETURN_VALUES).removeAsync(key);
            futures.put(key, future);
        }
        
        for (Entry<K, NotifyingFuture<V>> entry : futures.entrySet())
        {
            try
            {
                entry.getValue().get();
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
            catch (ExecutionException e)
            {
                // log exception as error
                // swallow exception
            }
        }
    }
    
    public int size()
    {
        return cache.size();
    }

    public Collection<V> values()
    {
        return cache.values();
    }

    public void clear()
    {
        cache.clear();
    }
}
