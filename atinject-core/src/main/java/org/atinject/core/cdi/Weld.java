package org.atinject.core.cdi;

import org.atinject.core.security.UserSecurityService;
import org.atinject.core.transaction.TransactionServices;
import org.atinject.core.validation.HibernateValidatorValidationServices;
import org.jboss.weld.bootstrap.api.Bootstrap;
import org.jboss.weld.bootstrap.spi.Deployment;
import org.jboss.weld.resources.spi.ResourceLoader;
import org.jboss.weld.security.spi.SecurityServices;
import org.jboss.weld.validation.spi.ValidationServices;

public class Weld extends org.jboss.weld.environment.se.Weld
{
    @Override
    protected Deployment createDeployment(ResourceLoader resourceLoader, Bootstrap bootstrap)
    {
        Deployment deployment = super.createDeployment(resourceLoader, bootstrap);
        deployment.getServices().add(TransactionServices.class, new TransactionServices());
        deployment.getServices().add(SecurityServices.class, new UserSecurityService());
        deployment.getServices().add(ValidationServices.class, new HibernateValidatorValidationServices());
        return deployment;
    }
}
