package org.atinject.core.cdi;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TestService implements TestServiceInterface
{

    @Override
    public String getString(){
        return "world";
    }
}
