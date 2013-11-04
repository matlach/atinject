package org.atinject.core.cache;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DefaultLoadFactor {

    public int loadFactor() {
        return 1;
    }
}
