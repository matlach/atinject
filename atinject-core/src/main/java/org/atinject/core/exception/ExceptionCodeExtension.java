package org.atinject.core.exception;

import java.util.Set;

import javax.enterprise.inject.spi.Extension;

public interface ExceptionCodeExtension extends Extension {

	Set<String> getAllExceptionCode();
}
