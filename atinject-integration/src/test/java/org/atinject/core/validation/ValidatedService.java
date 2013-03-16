package org.atinject.core.validation;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.constraints.NotNull;

import org.atinject.core.tiers.Service;

@ApplicationScoped
public class ValidatedService extends Service
{

    public void validateNotNull(@NotNull String mustNotBeNull){
        
    }
}
