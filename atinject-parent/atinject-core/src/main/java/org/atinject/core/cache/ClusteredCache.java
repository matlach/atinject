package org.atinject.core.cache;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.atinject.core.tiers.AbstractCacheStore;
import org.infinispan.AdvancedCache;
import org.infinispan.context.Flag;
import org.infinispan.distexec.mapreduce.Collator;
import org.infinispan.distexec.mapreduce.MapReduceTask;
import org.infinispan.distexec.mapreduce.Mapper;
import org.infinispan.distexec.mapreduce.Reducer;
import org.infinispan.util.concurrent.NotifyingFuture;

public abstract class ClusteredCache<K, V> extends AbstractCacheStore
{

    @Inject
    private ClusteredCacheManager cacheManager;
    
    private AdvancedCache<K, V> cache;
    
    @PostConstruct
    public void initialize()
    {
        String cacheName = this.getClass().getAnnotation(CacheName.class).value();
        this.cache = (AdvancedCache<K, V>) cacheManager.getCache(cacheName).getAdvancedCache();
        this.cache = cache.withFlags(Flag.IGNORE_RETURN_VALUES);
    }

    protected V get(K key)
    {
        return cache.get(key);
    }
    
    protected Map<K, V> getAll(K... keys)
    {
        return getAll(Arrays.asList(keys));
    }
    
    protected Map<K, V> getAll(Collection<K> keys){
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
    
    protected void lock(K key){
        cache.lock(key);
    }
    
    protected void lock(K... keys)
    {
        cache.lock(keys);
    }
    
    protected void lock(Collection<K> keys){
        cache.lock(keys);
    }
    
    protected void put(K key, V value)
    {
        cache.put(key, value);
    }
    
    protected void putAll(Map<K, V> m)
    {
        cache.putAll(m);
    }

    /**
     * Note : if pessimistic transaction are used, ensure {@link #lock(key)} has been called before
     */
    protected void remove(K key)
    {
        cache.remove(key);
    }
    
    protected void removeAll(K... keys)
    {
        removeAll(Arrays.asList(keys));
    }
    
    protected void removeAll(Collection<K> keys){
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
    
    protected int size()
    {
        return cache.size();
    }

    protected Collection<V> values()
    {
        return cache.values();
    }

    protected void clear()
    {
        cache.clear();
    }

    protected <KOut, VOut> Map<KOut, VOut> performMapReduce(
            Mapper<K,V,KOut,VOut> mapper,
            Reducer<KOut, VOut> reducer){
        return new MapReduceTask<K, V, KOut, VOut>(cache)
                .mappedWith(mapper)
                .reducedWith(reducer)
                .execute();
    }
    
    protected <KOut, VOut, R> R performMapReduce(
            Mapper<K,V,KOut,VOut> mapper,
            Reducer<KOut, VOut> reducer,
            Collator<KOut, VOut, R> collator){
        return new MapReduceTask<K, V, KOut, VOut>(cache)
                .mappedWith(mapper)
                .reducedWith(reducer)
                .execute(collator);
    }
}
