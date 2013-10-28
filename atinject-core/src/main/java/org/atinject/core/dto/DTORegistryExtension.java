package org.atinject.core.dto;

import java.util.List;

import javax.enterprise.inject.spi.Extension;

public interface DTORegistryExtension extends Extension {

    List<Class<? extends DTO>> getClasses();
}
