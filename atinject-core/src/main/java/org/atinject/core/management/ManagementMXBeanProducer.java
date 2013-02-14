package org.atinject.core.management;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryManagerMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.List;

import javax.enterprise.inject.Produces;
import javax.management.MBeanServer;

public class ManagementMXBeanProducer
{

    @Produces
    public ClassLoadingMXBean classLoadingMXBean()
    {
        return ManagementFactory.getClassLoadingMXBean();
    }
    
    @Produces
    public CompilationMXBean compilationMXBean()
    {
        return ManagementFactory.getCompilationMXBean();
    }
    
    @Produces
    public GarbageCollectorMXBean garbageCollectorMXBean()
    {
        List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean garbageCollectorMXBean : garbageCollectorMXBeans)
        {
            garbageCollectorMXBean.getName();
            return garbageCollectorMXBean;
        }
        return null;
    }
    
    @Produces
    public MemoryManagerMXBean memoryManagerMXBean()
    {
        List<MemoryManagerMXBean> memoryManagerMXBeans = ManagementFactory.getMemoryManagerMXBeans();
        for (MemoryManagerMXBean memoryManagerMXBean : memoryManagerMXBeans)
        {
            memoryManagerMXBean.getName();
            return memoryManagerMXBean;
        }
        return null;
    }
    
    @Produces
    public MemoryMXBean memoryMXBean()
    {
        return ManagementFactory.getMemoryMXBean();
    }
    
    @Produces
    public MemoryPoolMXBean memoryPoolMXBean()
    {
        List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans)
        {
            memoryPoolMXBean.getName();
            return memoryPoolMXBean;
        }
        return null;
    }
    
    @Produces
    public OperatingSystemMXBean operatingSystemMXBean()
    {
        return ManagementFactory.getOperatingSystemMXBean();
    }
    
    @Produces
    public MBeanServer platformMBeanServer()
    {
        return ManagementFactory.getPlatformMBeanServer();
    }
    
    @Produces
    public RuntimeMXBean runtimeMXBean()
    {
        return ManagementFactory.getRuntimeMXBean();
    }
    
    @Produces
    public ThreadMXBean threadMXBean()
    {
        return ManagementFactory.getThreadMXBean();
    }
}
