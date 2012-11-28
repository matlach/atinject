package org.atinject.api.user;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.user.entity.UserEntity;
import org.atinject.core.infinispan.CacheName;
import org.atinject.core.tiers.AbstractCacheStore;
import org.atinject.core.transaction.InfinispanTransactional;
import org.infinispan.Cache;
import org.infinispan.context.Flag;

@InfinispanTransactional
@ApplicationScoped
public class UserCacheStoreImpl extends AbstractCacheStore implements UserCacheStore
{

    @Inject @CacheName("user")
    private Cache<String, UserEntity> cache;
    
    @Override
    public void putUser(UserEntity user)
    {
        cache.getAdvancedCache().withFlags(Flag.IGNORE_RETURN_VALUES).put(user.getUuid(), user);
    }
    
}
