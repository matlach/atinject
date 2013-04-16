package org.atinject.api.userrole.entity;

import java.util.HashSet;
import java.util.Set;

import org.atinject.api.role.enumeration.Role;
import org.atinject.core.entity.Entity;

public class UserRolesEntity extends Entity {

    private static final long serialVersionUID = 1L;

    private String userId;
    
    private Set<Role> roles;
    
    public UserRolesEntity(){
        this.roles = new HashSet<>();
    }
    
    public String getUserId() {
        return userId;
    }

    public UserRolesEntity setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public Set<Role> getRoles(){
        return this.roles;
    }
    
    public UserRolesEntity addRole(Role role){
        getRoles().add(role);
        return this;
    }
    
    public UserRolesEntity removeRole(Role role){
        getRoles().remove(role);
        return this;
    }
}
