package org.atinject.core.retry;

import javax.inject.Inject;

import org.atinject.api.facebook.FacebookService;
import org.atinject.integration.IntegrationTest;
import org.junit.Test;

public class RetryIT extends IntegrationTest {

    @Inject private FacebookService facebookService;
    
    @Test(expected=Exception.class)
    public void testThisShouldGetRetry(){
        facebookService.callGraphAPI();
    }
}
