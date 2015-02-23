package org.atinject.core.lock;

import java.util.UUID;

import org.atinject.core.tiers.Service;

@Service
public class DummyServiceWithPessimisticLock {

	public void lockUser(@PessimisticLock("user") UUID userId) {
		
	}
}
