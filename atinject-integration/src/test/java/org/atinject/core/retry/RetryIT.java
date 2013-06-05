package org.atinject.core.retry;

import javax.inject.Inject;

import org.atinject.api.facebook.FacebookService;
import org.atinject.integration.IntegrationTest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RetryIT extends IntegrationTest {

    @Inject private FacebookService facebookService;
    
    @Test(expected=Exception.class)
    public void testThisShouldGetRetry(){
        facebookService.callGraphAPI();
    }
}
