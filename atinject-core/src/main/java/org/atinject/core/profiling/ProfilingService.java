package org.atinject.core.profiling;

import javax.interceptor.InvocationContext;

public interface ProfilingService
{

    void addProfiling(InvocationContext invocationContext, long t);

    void reset();
}
