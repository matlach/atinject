package org.atinject.core.security;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.atinject.core.cache.CacheExtension;
import org.atinject.core.transaction.InMemoryTransactionServices;
import org.atinject.integration.ArquillianIT;
import org.atinject.integration.DefaultDeployment;
import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

public class MessageDigesterIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
    	return new DefaultDeployment(ArquillianIT.class)
			.appendEmptyBeansXml()
			.appendJavaxEnterpriseInjectSpiExtension(CacheExtension.class)
			.appendOrgJBossWeldBootstrapApiService(InMemoryTransactionServices.class)
			.appendResource("arquillian-logback.xml", "logback.xml")
			.appendResource("arquillian-jgroups.xml", "jgroups.xml")
			.getArchive();
    }
    
    @Inject
    private MD5Digester md5Digester;
    
    @Inject
    private SHA1Digester sha1Digester;
    
    @Inject
    private SHA224Digester sha224Digester;
    
    @Inject
    private SHA256Digester sha256Digester;

    @Inject
    private SHA384Digester sha384Digester;
    
    @Inject
    private SHA512Digester sha512Digester;
    
    @Test
    @InSequence(1)
    public void testMD5Digest() {
        // see wikipedia : http://en.wikipedia.org/wiki/MD5
        String digestedPassword = md5Digester.digest("The quick brown fox jumps over the lazy dog");
        String expectedDigestedPassword = "9e107d9d372bb6826bd81d3542a419d6";
        Assertions.assertThat(digestedPassword).isEqualTo(expectedDigestedPassword);
    }
    
    @Test
    @InSequence(2)
    public void testSHA1Digest() {
        // see wikipedia : http://en.wikipedia.org/wiki/SHA-1
        String digestedPassword = sha1Digester.digest("The quick brown fox jumps over the lazy dog");
        String expectedDigestedPassword = "2fd4e1c67a2d28fced849ee1bb76e7391b93eb12";
        Assertions.assertThat(digestedPassword).isEqualTo(expectedDigestedPassword);
    }
    
    @Test
    @InSequence(3)
    public void testSHA224Digest() {
        // see wikipedia : http://en.wikipedia.org/wiki/SHA-224
        String digestedPassword = sha224Digester.digest("The quick brown fox jumps over the lazy dog");
        String expectedDigestedPassword = "730e109bd7a8a32b1cb9d9a09aa2325d2430587ddbc0c38bad911525";
        Assertions.assertThat(digestedPassword).isEqualTo(expectedDigestedPassword);
    }
    
    @Test
    @InSequence(4)
    public void testSHA256Digest() {
        // see wikipedia : http://en.wikipedia.org/wiki/SHA-256
        String digestedPassword = sha256Digester.digest("The quick brown fox jumps over the lazy dog");
        String expectedDigestedPassword = "d7a8fbb307d7809469ca9abcb0082e4f8d5651e46d3cdb762d02d0bf37c9e592";
        Assertions.assertThat(digestedPassword).isEqualTo(expectedDigestedPassword);
    }
    
    @Test
    @InSequence(5)
    public void testSHA384Digest() {
        // see wikipedia : http://en.wikipedia.org/wiki/SHA-384
        String digestedPassword = sha384Digester.digest("The quick brown fox jumps over the lazy dog");
        String expectedDigestedPassword = "ca737f1014a48f4c0b6dd43cb177b0afd9e5169367544c494011e3317dbf9a509cb1e5dc1e85a941bbee3d7f2afbc9b1";
        Assertions.assertThat(digestedPassword).isEqualTo(expectedDigestedPassword);
    }
    
    @Test
    @InSequence(6)
    public void testSHA512Digest() {
        // see wikipedia : http://en.wikipedia.org/wiki/SHA-512
        String digestedPassword = sha512Digester.digest("The quick brown fox jumps over the lazy dog");
        String expectedDigestedPassword = "07e547d9586f6a73f73fbac0435ed76951218fb7d0c8d788a309d785436bbb642e93a252a954f23912547d1e8a3b5ed6e1bfd7097821233fa0538f3db854fee6";
        Assertions.assertThat(digestedPassword).isEqualTo(expectedDigestedPassword);
    }
}
