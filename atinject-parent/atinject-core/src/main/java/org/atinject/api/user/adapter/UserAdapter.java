package org.atinject.api.user.adapter;

import org.atinject.api.user.dto.User;
import org.atinject.api.user.entity.UserEntity;

public interface UserAdapter
{

    User userEntityToUser(UserEntity entity);
}
