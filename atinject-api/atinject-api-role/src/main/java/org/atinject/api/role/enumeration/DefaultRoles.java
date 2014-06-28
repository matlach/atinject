package org.atinject.api.role.enumeration;

public final class DefaultRoles implements Roles {

    // weblogic : admin, operator, deployer, monitor, app tester,anonymous, others
    
    public static final String GUEST = "GUEST";
    public static final String REGISTERED = "REGISTERED";
    public static final String ADMIN = "ADMIN";
    public static final String SUPER_ADMIN = "SUPER_ADMIN";

    private DefaultRoles() {
        
    }
}
