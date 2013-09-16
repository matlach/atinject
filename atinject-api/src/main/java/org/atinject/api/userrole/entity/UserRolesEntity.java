package org.atinject.api.userrole.entity;

import java.util.HashSet;
import java.util.Set;

import org.atinject.core.entity.Entity;

public class UserRolesEntity extends Entity {

    private static final long serialVersionUID = 1L;

    private String userId;
    
    private Set<String> roles;
    
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
    
    public UserRolesEntity addRole(String role){
    	roles.add(role);
        return this;
    }
    
    public UserRolesEntity removeRole(String role){
    	roles.remove(role);
        return this;
    }
    
    public boolean containsRole(String role){
    	return roles.contains(role);
    }
}
