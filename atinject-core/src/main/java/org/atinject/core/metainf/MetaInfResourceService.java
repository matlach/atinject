package org.atinject.core.metainf;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.annotation.PostConstruct;

import org.atinject.core.tiers.Service;

@Service
public class MetaInfResourceService {

	private static final String FILE_PROTOCOL = "file";
	private static final String JAR_PROTOCOL = "jar";
	
	private List<MetaInfResource> metaInfResources;
	
	@PostConstruct
	public void initialize() {
		metaInfResources = new ArrayList<>();
		try {
			Enumeration<URL> systemResourceUrls = getClass().getClassLoader().getResources("META-INF");
			while (systemResourceUrls.hasMoreElements()) {
				URL systemResourceUrl = systemResourceUrls.nextElement();
				if (FILE_PROTOCOL.equals(systemResourceUrl.getProtocol())) {
					Path systemResourcePath = Paths.get(systemResourceUrl.toURI());
					Files.walk(systemResourcePath)
						.filter(path -> !Files.isDirectory(path))
						.forEach(path -> metaInfResources.add(new MetaInfResource(path)));
				}
				else if (JAR_PROTOCOL.equals(systemResourceUrl.getProtocol())) {
					// TODO
				}
			}
		}
		catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<MetaInfResource> getMetaInfResources() {
		return Collections.unmodifiableList(metaInfResources);
	}
	
}
