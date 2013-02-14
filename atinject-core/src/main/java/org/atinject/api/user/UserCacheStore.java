package org.atinject.api.user;

import org.atinject.api.user.entity.UserEntity;

public interface UserCacheStore
{

    UserEntity getUser(String userId);

    /**
     * Note : this will perform a map reduce search
     */
    UserEntity getUserByName(String name);
    
    void lockUser(String userId);
    
    void putUser(UserEntity user);
    
    void removeUser(UserEntity user);
    
}
