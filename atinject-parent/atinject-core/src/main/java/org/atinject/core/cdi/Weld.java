package org.atinject.core.cdi;

import org.atinject.core.transaction.InfinispanTransactionServices;
import org.jboss.weld.bootstrap.api.Bootstrap;
import org.jboss.weld.bootstrap.spi.Deployment;
import org.jboss.weld.resources.spi.ResourceLoader;
import org.jboss.weld.transaction.spi.TransactionServices;

public class Weld extends org.jboss.weld.environment.se.Weld
{
    @Override
    protected Deployment createDeployment(ResourceLoader resourceLoader, Bootstrap bootstrap)
    {
        Deployment deployment = super.createDeployment(resourceLoader, bootstrap);
        deployment.getServices().add(TransactionServices.class, new InfinispanTransactionServices());
        return deployment;
    }
}
