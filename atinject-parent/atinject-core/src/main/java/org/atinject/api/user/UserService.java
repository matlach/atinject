package org.atinject.api.user;

import org.atinject.api.user.entity.UserEntity;

public interface UserService
{
    UserEntity getUser(String userUUID);
    
    UserEntity addUser(String name, String password);
    
    void updateUser(UserEntity user);
    
    void removeUser(String userUUID);
    
    void removeUser(UserEntity user);
}
