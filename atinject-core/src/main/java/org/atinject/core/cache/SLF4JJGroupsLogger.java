package org.atinject.core.cache;

import org.atinject.core.logging.LoggerFactory;
import org.jgroups.logging.Log;
import org.slf4j.Logger;

public class SLF4JJGroupsLogger implements Log {

	private Logger logger;
	
	public SLF4JJGroupsLogger(Class<?> clazz) {
		logger = LoggerFactory.getLogger(clazz);
	}

	public SLF4JJGroupsLogger(String category) {
		logger = LoggerFactory.getLogger(category);
	}

	@Override
	public boolean isFatalEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	@Override
	public void fatal(String msg) {
		logger.error(msg);
	}

	@Override
	public void fatal(String msg, Object... args) {
		logger.error(msg, args);
	}

	@Override
	public void fatal(String msg, Throwable throwable) {
		logger.error(msg, throwable);
	}

	@Override
	public void error(String msg) {
		logger.error(msg);
	}

	@Override
	public void error(String format, Object... args) {
		logger.error(format, args);
	}

	@Override
	public void error(String msg, Throwable throwable) {
		logger.error(msg, throwable);
	}

	@Override
	public void warn(String msg) {
		logger.warn(msg);
	}

	@Override
	public void warn(String msg, Object... args) {
		logger.warn(msg, args);
	}

	@Override
	public void warn(String msg, Throwable throwable) {
		logger.warn(msg, throwable);
	}

	@Override
	public void info(String msg) {
		logger.info(msg);
	}

	@Override
	public void info(String msg, Object... args) {
		logger.info(msg, args);
	}

	@Override
	public void debug(String msg) {
		logger.debug(msg);
	}

	@Override
	public void debug(String msg, Object... args) {
		logger.debug(msg, args);
	}

	@Override
	public void debug(String msg, Throwable throwable) {
		logger.debug(msg, throwable);
	}

	@Override
	public void trace(Object msg) {
		if (logger.isTraceEnabled()) {
			logger.trace(String.valueOf(msg));
		}
	}

	@Override
	public void trace(String msg) {
		logger.trace(msg);
	}

	@Override
	public void trace(String msg, Object... args) {
		logger.trace(msg, args);
	}

	@Override
	public void trace(String msg, Throwable throwable) {
		logger.trace(msg, throwable);
	}

	@Override
	public void setLevel(String level) {
		// unsupported operation
		// no-op
	}

	@Override
	public String getLevel() {
		if (logger.isErrorEnabled()) {
			return "error";
		}
		if (logger.isWarnEnabled()) {
			return "warn";
		}
		if (logger.isInfoEnabled()) {
			return "info";
		}
		if (logger.isDebugEnabled()) {
			return "debug";
		}
		if (logger.isTraceEnabled()) {
			return "trace";
		}
		return "off";
	}

}
