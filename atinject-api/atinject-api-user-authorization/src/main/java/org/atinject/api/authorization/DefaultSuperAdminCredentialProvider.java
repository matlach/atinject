package org.atinject.api.authorization;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DefaultSuperAdminCredentialProvider implements SuperAdminCredentialProvider {

    public static final String SUPER_ADMIN_USERNAME = "admin";
    public static final String SUPER_ADMIN_PASSWORD = "admin";
	
    @Override
    public String getUsername() {
        return SUPER_ADMIN_USERNAME;
    }

    @Override
    public String getPassword() {
        return SUPER_ADMIN_PASSWORD;
    }
}
