package org.atinject.api.role;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.atinject.core.tiers.Service;

@Service
public class RoleService {

    @Inject
    RoleExtension roleExtension;

    // TODO we should allow dynamic roles
    private Set<String> roles;

    @PostConstruct
    public void initialize() {
        roles = roleExtension.getAllRole();
    }

    public Set<String> getAllRole() {
        return roles;
    }

    public boolean isRole(String role) {
        return roles.contains(role);
    }
}
