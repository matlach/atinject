package org.atinject.api.role;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.atinject.api.role.entity.Role;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class RoleService extends Service {

    @PostConstruct
    public void initialize(){
        
    }
    
    public Role getRole(){
        return null;
    }
}
