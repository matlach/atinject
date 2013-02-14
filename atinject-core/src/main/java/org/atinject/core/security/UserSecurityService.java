package org.atinject.core.security;

import java.security.Principal;

import org.jboss.weld.security.spi.SecurityServices;

public class UserSecurityService implements SecurityServices
{

    @Override
    public Principal getPrincipal()
    {
        // TODO make User implements Principal ?
        return null;
    }
    
    @Override
    public void cleanup()
    {
        // nothing to cleanup
    }

}
