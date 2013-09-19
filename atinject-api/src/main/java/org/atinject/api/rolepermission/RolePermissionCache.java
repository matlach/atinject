package org.atinject.api.rolepermission;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.rolepermission.entity.RolePermissions;
import org.atinject.core.cache.CacheName;
import org.atinject.core.cache.ClusteredCache;
import org.atinject.core.tiers.CacheStore;

@ApplicationScoped
public class RolePermissionCache extends CacheStore {

	/*@Inject @CacheName("rolepermission")*/ ClusteredCache<String, RolePermissions> cache;
	
	public RolePermissions getRolePermissions(String role) {
		return cache.get(role);
	}
	
	public void putRolePermissions(RolePermissions rolePermissions) {
		cache.put(rolePermissions.getRole(), rolePermissions);
	}
}
