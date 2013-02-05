package org.atinject.core.validation;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;
import org.jboss.weld.validation.spi.ValidationServices;

public class HibernateValidatorValidationServices implements ValidationServices
{

    @Override
    public ValidatorFactory getDefaultValidatorFactory()
    {
        return Validation.byProvider(HibernateValidator.class).configure().buildValidatorFactory();
    }
    
    @Override
    public void cleanup()
    {
        // nothing to cleanup
    }

}
