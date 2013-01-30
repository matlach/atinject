package org.atinject.api.user;

import org.atinject.api.user.entity.UserEntity;

public interface UserCacheStore
{

    UserEntity getUser(String userUUID);
    
    void lockUser(String userUUID);
    
    void putUser(UserEntity user);
    
    void removeUser(UserEntity user);
}
