package org.atinject.core.tiers;

import org.atinject.core.exception.HandleCacheStoreException;
import org.atinject.core.profiling.Profile;
import org.atinject.core.thread.ThreadTracker;

@HandleCacheStoreException
@Profile
@ThreadTracker
public abstract class CacheStore {


}
