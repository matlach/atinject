package org.atinject.core.validation;

import javax.validation.ValidatorFactory;

import org.jboss.weld.validation.spi.ValidationServices;

public class HibernateValidatorValidationServices implements ValidationServices
{

    @Override
    public ValidatorFactory getDefaultValidatorFactory()
    {
        return null;
    }
    
    @Override
    public void cleanup()
    {
        // nothing to cleanup
    }

}
