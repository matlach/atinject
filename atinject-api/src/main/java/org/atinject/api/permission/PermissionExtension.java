package org.atinject.api.permission;

import java.util.Set;

import javax.enterprise.inject.spi.Extension;

public interface PermissionExtension extends Extension {

	Set<String> getAllPermission();
}
