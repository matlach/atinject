package org.atinject.api.user;

import org.atinject.api.user.entity.UserEntity;

public interface UserCacheStore
{

    public void putUser(UserEntity user);
    
}
