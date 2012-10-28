package org.atinject.api.user;

import org.atinject.api.user.entity.UserEntity;

public interface UserService
{
    UserEntity getUser(String userUUID);
}
