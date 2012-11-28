package org.atinject.core.profiling;

import javax.inject.Inject;

import org.softee.management.annotation.Description;
import org.softee.management.annotation.MBean;
import org.softee.management.annotation.ManagedOperation;
import org.softee.management.annotation.ManagedOperation.Impact;

@MBean(objectName="")
@Description("")
public class ProfilingServiceMBean
{

    @Inject
    private ProfilingService profilingService;
    
    @ManagedOperation(Impact.ACTION)
    @Description("")
    public void reset()
    {
        profilingService.reset();
    }
}
