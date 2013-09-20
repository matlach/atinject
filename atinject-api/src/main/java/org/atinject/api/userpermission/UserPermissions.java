package org.atinject.api.userpermission;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.atinject.api.rolepermission.entity.RolePermissions;

public class UserPermissions {

	private String userId;
	private Set<String> permissions;
	
	public UserPermissions() {
		this.permissions = new HashSet<>();
	}
	
	public String getUserId() {
		return userId;
	}
	
	public UserPermissions setUserId(String userId) {
		this.userId = userId;
		return this;
	}
	
	public Set<String> getPermissions(){
		return Collections.unmodifiableSet(permissions);
	}
	
	public UserPermissions addPermission(String permission) {
		this.permissions.add(permission);
		return this;
	}
	
	public UserPermissions removePermission(String permission) {
		this.permissions.remove(permission);
		return this;
	}
	
	public boolean hasPermission(String permission) {
		return this.permissions.contains(permission);
	}
}
