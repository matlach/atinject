package org.atinject.api.role;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.tiers.Service;

@ApplicationScoped
public class RoleService extends Service {

	@Inject RoleExtension roleExtension;
	
	private Set<String> roles;
	
    @PostConstruct
    public void initialize(){
        roles = roleExtension.getAllRole();
    }
    
    public Set<String> getAllRole(){
    	return roles;
    }
    
    public boolean isRole(String role) {
    	return roles.contains(role);
    }
}
