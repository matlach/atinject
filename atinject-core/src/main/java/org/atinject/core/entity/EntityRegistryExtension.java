package org.atinject.core.entity;

import java.util.List;

import javax.enterprise.inject.spi.Extension;

public interface EntityRegistryExtension extends Extension {

    List<Class<? extends Entity>> getClasses();
}
