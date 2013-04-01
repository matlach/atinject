package org.atinject.core.security;

import javax.inject.Inject;

import org.atinject.integration.IntegrationTest;
import org.junit.Assert;
import org.junit.Test;

public class PasswordDigesterIT extends IntegrationTest {

    @Inject private SimplePasswordDigester passwordDigester;
    
    @Test
    public void testSHA1Digest(){
        // see wikipedia : http://en.wikipedia.org/wiki/SHA-1
        String digestedPassword = passwordDigester.digest("The quick brown fox jumps over the lazy dog");
        String expectedDigestedPassword = "2fd4e1c67a2d28fced849ee1bb76e7391b93eb12";
        Assert.assertEquals(expectedDigestedPassword, digestedPassword);
    }
}
