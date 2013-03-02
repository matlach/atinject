package org.atinject.core.tiers;

import org.atinject.core.exception.ServiceExceptionHandler;
import org.atinject.core.profiling.Profile;
import org.atinject.core.transaction.Transactional;

@ServiceExceptionHandler
@Transactional
@Profile
public abstract class Service {


}
