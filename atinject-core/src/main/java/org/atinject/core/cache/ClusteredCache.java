package org.atinject.core.cache;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import org.infinispan.AdvancedCache;
import org.infinispan.Cache;
import org.infinispan.context.Flag;
import org.infinispan.distexec.mapreduce.Collator;
import org.infinispan.distexec.mapreduce.MapReduceTask;
import org.infinispan.distexec.mapreduce.Mapper;
import org.infinispan.distexec.mapreduce.Reducer;
import org.infinispan.util.concurrent.NotifyingFuture;

public class ClusteredCache<K, V>
{
    
    private AdvancedCache<K, V> cache;
    
    public ClusteredCache(Cache<K, V> cache){
        this.cache = cache.getAdvancedCache()
                .withFlags(Flag.IGNORE_RETURN_VALUES);
    }

    public V get(K key)
    {
        return cache.get(key);
    }
    
    public Map<K, V> getAll(K... keys)
    {
        return getAll(Arrays.asList(keys));
    }
    
    public Map<K, V> getAll(Collection<K> keys){
        Map<K, NotifyingFuture<V>> futures = new HashMap<>(keys.size());
        for (K key : keys)
        {
            NotifyingFuture<V> future = cache.getAsync(key);
            futures.put(key, future);
        }
        
        Map<K, V> values = new HashMap<>(keys.size());
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
    
    public void lock(K key){
        cache.lock(key);
    }
    
    public void lock(K... keys)
    {
        cache.lock(keys);
    }
    
    public void lock(Collection<K> keys){
        cache.lock(keys);
    }
    
    public void put(K key, V value)
    {
        cache.put(key, value);
    }
    
    public void putAll(Map<K, V> m)
    {
        cache.putAll(m);
    }

    /**
     * Note : if pessimistic transaction are used, ensure {@link #lock(key)} has been called before
     */
    public void remove(K key)
    {
        cache.remove(key);
    }
    
    public void removeAll(K... keys)
    {
        removeAll(Arrays.asList(keys));
    }
    
    public void removeAll(Collection<K> keys){
        Map<K, NotifyingFuture<V>> futures = new HashMap<>(keys.size());
        for (K key : keys)
        {
            NotifyingFuture<V> future = cache.removeAsync(key);
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

    public <KOut, VOut> Map<KOut, VOut> performMapReduce(
            Mapper<K,V,KOut,VOut> mapper,
            Reducer<KOut, VOut> reducer){
        return new MapReduceTask<K, V, KOut, VOut>(cache)
                .mappedWith(mapper)
                .reducedWith(reducer)
                .execute();
    }
    
    public <KOut, VOut, R> R performMapReduce(
            Mapper<K,V,KOut,VOut> mapper,
            Reducer<KOut, VOut> reducer,
            Collator<KOut, VOut, R> collator){
        return new MapReduceTask<K, V, KOut, VOut>(cache)
                .mappedWith(mapper)
                .reducedWith(reducer)
                .execute(collator);
    }
}
