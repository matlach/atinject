package org.atinject.api.role.enumeration;

public final class SimpleRoles implements Roles {

    // weblogic : admin, operator, deployer, monitor, app tester,anonymous, others
    
    public static final String GUEST = "GUEST";
    public static final String REGISTERED = "REGISTERED";
    public static final String ADMIN = "ADMIN";

    private SimpleRoles() {
        
    }
}
