package org.atinject.api.role;

import java.util.Set;

import javax.enterprise.inject.spi.Extension;

public interface RoleExtension extends Extension {

    Set<String> getAllRole();
}
