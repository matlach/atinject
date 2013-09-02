package org.atinject.integration;

import org.atinject.core.cdi.JavaSECDIProvider;



public class Bootstrap {

    public static void main(String[] args) throws Exception
    {
        // TODO create a wrapper class to provide default system properties
        // TODO this wrapper class should be instantiated by reflection using a well known system property like
        // -Dorg.atinject.bootstrap.custom.system.properties=org.atinject....
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
        
        new JavaSECDIProvider();
        
        // TODO inject here a "version service"
        System.out.println("booting atinject core version 1");
    }
	
}
