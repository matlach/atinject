package org.atinject.api.userrole.entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.atinject.api.role.enumeration.Roles;
import org.atinject.core.entity.Entity;

public class UserRolesEntity extends Entity {

    private static final long serialVersionUID = 1L;

    private UUID userId;
    
    private Set<String> roles;
    
    public UserRolesEntity(){
        this.roles = new HashSet<>();
    }
    
    public UUID getUserId() {
        return userId;
    }

    public UserRolesEntity setUserId(UUID userId) {
        this.userId = userId;
        return this;
    }
    
    public Set<String> getRoles(){
        return Collections.unmodifiableSet(roles);
    }
    
    public UserRolesEntity addRole(String role){
        roles.add(role);
        return this;
    }
    
    public UserRolesEntity removeRole(String role){
        roles.remove(role);
        return this;
    }
    
    public <R extends Enum<?> & Roles> boolean containsRole(R role){
    	return containsRole(role.name());
    }
    
    public boolean containsRole(String role){
        return roles.contains(role);
    }

	@Override
	public Object getId() {
		// TODO Auto-generated method stub
		return null;
	}
}
