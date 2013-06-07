package org.atinject.api.role;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.atinject.api.role.enumeration.Roles;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class RoleService extends Service {

    @PostConstruct
    public void initialize(){
        
    }
    
    public boolean isGuest(int role){
        return Roles.isGuest(role);
    }
    
    public boolean isRegistered(int role){
        return Roles.isGuest(role);
    }
    
    public boolean isAdmin(int role){
        return Roles.isGuest(role);
    }
}
