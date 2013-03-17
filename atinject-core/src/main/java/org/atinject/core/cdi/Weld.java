package org.atinject.core.cdi;

import org.atinject.core.transaction.InMemoryTransactionServices;
import org.atinject.core.validation.FailFastValidationServices;
import org.jboss.weld.bootstrap.api.Bootstrap;
import org.jboss.weld.bootstrap.spi.Deployment;
import org.jboss.weld.resources.spi.ResourceLoader;
import org.jboss.weld.validation.spi.ValidationServices;

public class Weld extends org.jboss.weld.environment.se.Weld
{
    @Override
    protected Deployment createDeployment(ResourceLoader resourceLoader, Bootstrap bootstrap)
    {
        Deployment deployment = super.createDeployment(resourceLoader, bootstrap);
        deployment.getServices().add(org.jboss.weld.transaction.spi.TransactionServices.class, new InMemoryTransactionServices());
        deployment.getServices().add(ValidationServices.class, new FailFastValidationServices());
        return deployment;
    }
}
