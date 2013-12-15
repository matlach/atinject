package org.atinject.api.permission;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.tiers.Service;

@ApplicationScoped
public class PermissionService extends Service {

    @Inject
    PermissionExtension permissionExtension;

    // TODO we should allow dynamic permissions
    private Set<String> permissions;

    @PostConstruct
    public void initialize() {
        permissions = permissionExtension.getAllPermission();
    }

    public Set<String> getAllPermission() {
        return permissions;
    }

    public boolean isPermission(String permission) {
        return permission.contains(permission);
    }
}
