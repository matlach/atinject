package org.atinject.api.systemproperty;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

/**
 * note: this extension is designed to be placed first in META-INF/services/javax.enterprise.inject.spi.Extension file
 */
public class SystemPropertyExtension implements Extension {

    void onBeforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd) {

    }
}
