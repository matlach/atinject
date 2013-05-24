package org.atinject.api.userlockout;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.core.tiers.Service;

@ApplicationScoped
public class UserLockoutService extends Service {

    // Threshold 5times, duration 30mins, reset duration 5 minutes.
}
