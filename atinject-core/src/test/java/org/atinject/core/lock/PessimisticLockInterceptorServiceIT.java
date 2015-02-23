package org.atinject.core.lock;

import java.util.UUID;

import javax.inject.Inject;

import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

public class PessimisticLockInterceptorServiceIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return createDefaultArchive(PessimisticLockInterceptorServiceIT.class);
    }
	
	@Inject
	private DummyServiceWithPessimisticLock dummyServiceWithPessimisticLock;
	
	@Test @InSequence(1)
	public void testLock() {
		dummyServiceWithPessimisticLock.lockUser(UUID.randomUUID());
	}
}
