package org.atinject.core;

import javax.enterprise.inject.spi.CDI;

import org.atinject.core.cdi.JavaSECDIProvider;



public class Bootstrap {

    public static void main(String[] args) throws Exception
    {
        System.setProperty("user.timezone", "GMT");
        System.setProperty("infinispan.unsafe.allow_jdk8_chm", "true");

        Thread shutdownThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    System.in.read();
                    System.exit(0);
                }
                catch (Exception e)
                {
                    // swallow
                }
            }
        });
        shutdownThread.setDaemon(true);
        shutdownThread.start();
        
        CDI.setCDIProvider(new JavaSECDIProvider());
    }
	
}