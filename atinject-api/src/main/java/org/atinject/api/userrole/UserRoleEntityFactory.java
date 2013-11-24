package org.atinject.api.userrole;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.api.userrole.entity.UserRolesEntity;
import org.atinject.core.entity.EntityFactory;

@ApplicationScoped
public class UserRoleEntityFactory extends EntityFactory {

    public UserRolesEntity newUserRoles(){
        return new UserRolesEntity();
    }
}
