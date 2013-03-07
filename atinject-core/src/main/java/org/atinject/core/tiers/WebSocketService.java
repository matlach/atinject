package org.atinject.core.tiers;

import org.atinject.core.exception.HandleControllerException;
import org.atinject.core.profiling.Profile;
import org.atinject.core.thread.ThreadTracker;
import org.atinject.core.transaction.Transactional;

@HandleControllerException
@Profile
@ThreadTracker
@Transactional
public abstract class WebSocketService {


}
