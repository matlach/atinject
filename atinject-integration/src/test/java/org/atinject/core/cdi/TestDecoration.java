package org.atinject.core.cdi;

import javax.inject.Inject;

import org.atinject.integration.IntegrationTest;
import org.junit.Test;

public class TestDecoration extends IntegrationTest
{

    @Inject TestServiceInterface service;
    
    @Test
    public void testDecoration(){
        System.out.println(service.getString());
    }
}
