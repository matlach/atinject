package org.atinject.api.permission;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.atinject.api.permission.entity.Permissions;
import org.atinject.core.tiers.Service;

@Service
public class PermissionService {

    @Inject
    PermissionExtension permissionExtension;

    // TODO we should allow dynamic permissions
    private Set<String> permissions;

    @PostConstruct
    public void initialize() {
        permissions = permissionExtension.getAllPermission();
    }

    public <P extends Enum<?> & Permissions> Set<P> getAllStaticPermission() {
        return null;
    }
    
    public Set<String> getAllPermission() {
        return permissions;
    }

    public boolean isPermission(String permission) {
        return permission.contains(permission);
    }
}
