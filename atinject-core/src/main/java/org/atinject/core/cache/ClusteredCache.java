package org.atinject.core.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.infinispan.commons.util.concurrent.NotifyingFuture;
import org.infinispan.distexec.mapreduce.Collator;
import org.infinispan.distexec.mapreduce.MapReduceTask;
import org.infinispan.distexec.mapreduce.Mapper;
import org.infinispan.distexec.mapreduce.Reducer;
import org.infinispan.remoting.rpc.RpcManager;

public abstract class ClusteredCache<K, V> extends LocalCache<K, V> {
    
    @Override
    public Map<K, Optional<V>> getAll(Collection<K> keys) {
        Map<K, NotifyingFuture<V>> futures = new HashMap<>(keys.size());
        for (K key : keys) {
            NotifyingFuture<V> future = cache.getAsync(key);
            futures.put(key, future);
        }
        
        Map<K, Optional<V>> values = new HashMap<>(keys.size());
        for (Entry<K, NotifyingFuture<V>> entry : futures.entrySet()) {
            try {
                V value = entry.getValue().get();
                values.put(entry.getKey(), Optional.ofNullable(value));
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            catch (ExecutionException e) {
                // log exception as error
                // swallow exception
                values.put(entry.getKey(), Optional.empty());
            }
        }
        return values;
    }
    
    @Override
    public void putAll(Map<K, V> entries) {
        NotifyingFuture<Void> future = cache.putAllAsync(entries);
        try {
            future.get();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        catch (ExecutionException e) {
            // log exception as error
            // swallow exception
        }
    }

    @Override
    public void removeAll(Collection<K> keys) {
        Map<K, NotifyingFuture<V>> futures = new HashMap<>(keys.size());
        for (K key : keys) {
            NotifyingFuture<V> future = cache.removeAsync(key);
            futures.put(key, future);
        }
        
        for (Entry<K, NotifyingFuture<V>> entry : futures.entrySet()) {
            try {
                entry.getValue().get();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            catch (ExecutionException e) {
                // log exception as error
                // swallow exception
            }
        }
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
    
    public RpcManager getRpcManager() {
        return cache.getRpcManager();
    }
}
