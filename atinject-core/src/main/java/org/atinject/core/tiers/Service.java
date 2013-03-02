package org.atinject.core.tiers;

import org.atinject.core.exception.HandleServiceException;
import org.atinject.core.profiling.Profile;
import org.atinject.core.thread.ThreadTracker;
import org.atinject.core.transaction.Transactional;

@HandleServiceException
@Profile
@ThreadTracker
@Transactional
public abstract class Service {


}
