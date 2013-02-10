package org.atinject.api.user;

import org.atinject.api.user.entity.UserEntity;

public interface UserService
{
    UserEntity getUser(String userId);
    
    UserEntity addUser(String name, String password);
    
    void updateUser(UserEntity user);
    
    void removeUser(String userId);
    
    void removeUser(UserEntity user);
}
