package org.atinject.core.uuid;

import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UUIDPatternMatcher {

	private Pattern version1;
	private Pattern version4;
	
	@PostConstruct
	public void initialize() {
		version1 = Pattern.compile("/^[0-9a-f]{8}-[0-9a-f]{4}-[1][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i");
		version4 = Pattern.compile("/^[0-9a-f]{8}-[0-9a-f]{4}-[4][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i");
	}
	
	public boolean isVersion1UUID(String uuid) {
		return version1.matcher(uuid).matches();
	}
	
	public boolean isVersion4UUID(String uuid) {
		return version4.matcher(uuid).matches();
	}
}
