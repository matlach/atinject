package org.atinject.core.authorization;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.core.tiers.Service;

@ApplicationScoped
public class AuthorizationService extends Service {
    // this service should provides caching to lower the performance impact of applying authorization
}
