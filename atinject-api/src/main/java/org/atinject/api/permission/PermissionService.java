package org.atinject.api.permission;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.atinject.api.permission.entity.Permission;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class PermissionService extends Service {

    @PostConstruct
    public void initialize(){
        
    }
    
    public Permission getPrivilege(String privilege){
        return null;
    }
}
