package org.atinject.core.tiers;

import org.atinject.core.profiling.Profile;
import org.atinject.core.thread.ThreadTracker;
import org.atinject.core.tiers.exception.HandleCacheStoreException;

@HandleCacheStoreException
@Profile
@ThreadTracker
public abstract class CacheStore {


}
