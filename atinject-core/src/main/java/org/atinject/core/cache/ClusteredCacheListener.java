package org.atinject.core.cache;

import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import org.atinject.core.logging.LoggerFactory;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntriesEvicted;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryActivated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryInvalidated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryLoaded;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryModified;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryPassivated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryVisited;
import org.infinispan.notifications.cachelistener.annotation.TransactionCompleted;
import org.infinispan.notifications.cachelistener.annotation.TransactionRegistered;
import org.infinispan.notifications.cachelistener.event.CacheEntriesEvictedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryActivatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryInvalidatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryLoadedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryModifiedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryPassivatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryRemovedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryVisitedEvent;
import org.infinispan.notifications.cachelistener.event.Event;
import org.infinispan.notifications.cachelistener.event.TransactionCompletedEvent;
import org.infinispan.notifications.cachelistener.event.TransactionRegisteredEvent;
import org.infinispan.transaction.xa.GlobalTransaction;
import org.slf4j.Logger;

@Listener
public class ClusteredCacheListener<K, V>
{
    private Logger logger = LoggerFactory.getLogger(ClusteredCacheListener.class);

    private ConcurrentMap<GlobalTransaction, Queue<Event<K, V>>> transactions = new ConcurrentHashMap<>();

    @TransactionRegistered
    public void onTransactionRegisteredEvent(TransactionRegisteredEvent<K, V> event) {
        logger.info("transaction '{}' registered", event.getGlobalTransaction().getId());
        transactions.put(event.getGlobalTransaction(), new ConcurrentLinkedQueue<Event<K, V>>());
    }

    @CacheEntryModified
    public void onCacheEntryModifiedEvent(CacheEntryModifiedEvent<K, V> event) {
        if (event.isOriginLocal()) {
            logger.info("entry '{}' modified", event.getKey());
        }
    }

    @CacheEntryCreated
    public void onCacheEntryCreatedEvent(CacheEntryCreatedEvent<K, V> event) {
        logger.info("entry '{}' created", event.getKey());
    }

    @CacheEntryRemoved
    public void onCacheEntryRemovedEvent(CacheEntryRemovedEvent<K, V> event) {
        logger.info("entry '{}' removed", event.getKey());
    }

    @CacheEntryVisited
    public void onCacheEntryVisitedEvent(CacheEntryVisitedEvent<K, V> event) {
        logger.info("entry '{}' visited", event.getKey());
    }

    @CacheEntryLoaded
    public void onCacheEntryLoadedEvent(CacheEntryLoadedEvent<K, V> event) {
        logger.info("entry '{}' loaded", event.getKey());
    }

    @CacheEntryActivated
    public void onCacheEntryActivatedEvent(CacheEntryActivatedEvent<K, V> event) {
        logger.info("entry '{}' activated", event.getKey());
    }

    @CacheEntryPassivated
    public void onCacheEntryPassivatedEvent(CacheEntryPassivatedEvent<K, V> event) {
        logger.info("entry '{}' passivated", event.getKey());
    }

    @CacheEntryInvalidated
    public void onCacheEntryInvalidatedEvent(CacheEntryInvalidatedEvent<K, V> event) {
        logger.info("entry '{}' invalidated", event.getKey());
    }

    @TransactionCompleted
    public void onTransactionCompletedEvent(TransactionCompletedEvent<K, V> event) {
        logger.info("transaction '{}' completed", event.getGlobalTransaction().getId());
        transactions.remove(event.getGlobalTransaction());
    }

    @CacheEntriesEvicted
    public void onCacheEntriesEvictedEvent(CacheEntriesEvictedEvent<K, V> event) {
        for (Entry<K, V> entry : event.getEntries().entrySet()) {
            logger.info("entry '{}' evicted", entry.getKey());
        }
    }
}
