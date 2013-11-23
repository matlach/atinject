package org.atinject.api.rolepermission.entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.atinject.core.entity.Entity;

public class RolePermissions extends Entity {

    private static final long serialVersionUID = 1L;

    private String role;
    private Set<String> permissions;

    public RolePermissions() {
        this.permissions = new HashSet<>();
    }

    public String getRole() {
        return role;
    }

    public RolePermissions setRole(String role) {
        this.role = role;
        return this;
    }

    public Set<String> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }

    public RolePermissions addPermission(String permission) {
        this.permissions.add(permission);
        return this;
    }

    public RolePermissions removePermission(String permission) {
        this.permissions.remove(permission);
        return this;
    }

    public boolean hasPermission(String permission) {
        return this.permissions.contains(permission);
    }
}
