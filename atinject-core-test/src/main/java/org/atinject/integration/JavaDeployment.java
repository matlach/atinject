package org.atinject.integration;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.decorator.Decorator;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Stereotype;
import javax.enterprise.inject.spi.Extension;
import javax.interceptor.Interceptor;
import javax.interceptor.Interceptors;

import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filter;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans11.BeansDescriptor;
import org.jboss.weld.bootstrap.api.Service;

public abstract class JavaDeployment {

	private static final String JAVA_ARCHIVE_FILE_EXTENSION = ".jar";
	private static final String BEANS_XML_ARCHIVE_PATH = "beans.xml";
	private static final String JAVAX_ENTERPRISE_INJECT_SPI_EXTENSION_ARCHIVE_PATH = "services/javax.enterprise.inject.spi.Extension";
	private static final String ORG_JBOSS_WELD_API_BOOTSTRAP_API_SERVICE_ARCHIVE_PATH = "services/org.jboss.weld.bootstrap.api.Service";
	
	protected JavaArchive archive;
	
	public JavaDeployment(Class<? extends IntegrationTest> integrationTestClass) {
		archive = createNamedJavaArchive(getArchiveName(integrationTestClass));
		appendIntegrationTestClassHierarchy(integrationTestClass);
	}
	
    public static JavaArchive createNamedJavaArchive(String archiveName) {
    	return ShrinkWrap.create(JavaArchive.class, archiveName);
    }
    
	private void appendIntegrationTestClassHierarchy(Class<? extends IntegrationTest> integrationTestClass) {
		Class<?> clazz = integrationTestClass;
		while(clazz != Object.class) {
			appendClass(clazz);
			clazz = clazz.getSuperclass();
		}
	}
	
	public JavaArchive getArchive() {
		return archive;
	}
	
	protected String getArchiveName(Class<?> clazz) {
		return clazz.getSimpleName() + JAVA_ARCHIVE_FILE_EXTENSION;
	}
	
	public JavaDeployment appendClass(Class<?> clazz) {
		appendClass(archive, clazz);
		return this;
	}
	
	public JavaDeployment appendClasses(Class<?>... classes) {
		appendClasses(archive, classes);
		return this;
	}
	
	public JavaDeployment appendPackage(String packages) {
		appendPackage(archive, packages);
		return this;
	}
	
	public JavaDeployment appendPackage(Filter<ArchivePath> filter, String packages) {
		appendPackage(archive, filter, packages);
		return this;
	}
	
    public JavaDeployment appendBeansXml(Asset beansXml) {
    	addBeansXml(archive, beansXml);
    	return this;
    }
    
    public JavaDeployment appendResource(String resource, String path) {
    	appendResource(archive, resource, path);
    	return this;
    }

    @SafeVarargs
    public final JavaDeployment appendJavaxEnterpriseInjectSpiExtension(Class<? extends Extension>... extensions) {
    	return appendJavaxEnterpriseInjectSpiExtension(createJavaxEnterpriseSpiExtension(extensions));
    }
    
    public JavaDeployment appendJavaxEnterpriseInjectSpiExtension(Asset javaxEnterpriseInjectSpiExtension) {
    	addJavaxEnterpriseInjectSpiExtension(archive, javaxEnterpriseInjectSpiExtension);
    	return this;
    }
    
    @SafeVarargs
	public final JavaDeployment appendOrgJBossWeldBootstrapApiService(Class<? extends Service>... services) {
    	return appendOrgJBossWeldBootstrapApiService(createOrgJBossWeldBootstrapApiService(services));
    }
    
    public JavaDeployment appendOrgJBossWeldBootstrapApiService(Asset asset) {
    	addOrgJBossWeldBootstrapApiService(archive, asset);
    	return this;
    }
    
	public JavaDeployment appendBeansXml(Class<?>... classes) {
		appendBeansXml(createBeansXml(classes));
		appendClasses(classes);
		return this;
	}
	
	public JavaDeployment appendEmptyBeansXml() {
		appendBeansXml(createEmptyBeansXml());
		return this;
	}
	
	public static void appendClass(JavaArchive archive, Class<?> clazz) {
		archive.addClass(clazz);
	}
	
	public static void appendClasses(JavaArchive archive, Class<?>... classes) {
		archive.addClasses(classes);
	}
	
	public static void appendPackage(JavaArchive archive, String packages) {
		archive.addPackages(true, packages);
	}
	
	public static void appendPackage(JavaArchive archive, Filter<ArchivePath> filter, String packages) {
		archive.addPackages(true, filter, packages);
	}
	
	public static void appendResource(JavaArchive archive, String resourceName, String target) {
		archive.addAsResource(resourceName, target);
	}
    
    public static void addBeansXml(JavaArchive archive, Asset beansXml) {
    	archive.addAsManifestResource(beansXml, BEANS_XML_ARCHIVE_PATH);
    }
    
    public static void addJavaxEnterpriseInjectSpiExtension(JavaArchive archive, Asset javaxEnterpriseInjectSpiExtension) {
    	archive.addAsManifestResource(javaxEnterpriseInjectSpiExtension, JAVAX_ENTERPRISE_INJECT_SPI_EXTENSION_ARCHIVE_PATH);
    }
    
    public static void addOrgJBossWeldBootstrapApiService(JavaArchive archive, Asset orgJBossWeldBootstrapApiService) {
    	archive.addAsManifestResource(orgJBossWeldBootstrapApiService, ORG_JBOSS_WELD_API_BOOTSTRAP_API_SERVICE_ARCHIVE_PATH);
    }
    
    @SafeVarargs
    public static Asset createOrgJBossWeldBootstrapApiService(Class<? extends Service>... services) {
    	return createOrgJBossWeldBootstrapApiService(Arrays.asList(services));
    }
    
    public static Asset createOrgJBossWeldBootstrapApiService(Collection<Class<? extends Service>> services) {
    	return new StringAsset(
    			services.stream()
        			.map((extension) -> extension.getName())
        			.collect(Collectors.joining(System.lineSeparator())));
    }
    
    public static Asset createEmptyBeansXml() {
    	return new StringAsset(
    			Descriptors.create(BeansDescriptor.class)
    				.exportAsString());
    }
    
    public static Asset createBeansXml(Class<?>... classes) {
    	return createBeansXml(Arrays.asList(classes));
    }
    
    public static Asset createBeansXml(List<Class<?>> classes) {
    	return new StringAsset(
		    	Descriptors.create(BeansDescriptor.class)
		    		.getOrCreateAlternatives()
		    			.clazz(adapt(getAlternatives(classes)))
		    			.stereotype(adapt(getStereotypes(classes)))
		    			.up()
		    		.getOrCreateDecorators()
		    			.clazz(adapt(getDecorators(classes)))
		    			.up()
		    		.getOrCreateInterceptors()
		    			.clazz(adapt(getInterceptors(classes)))
		    			.up()
	    			.exportAsString());
    }
    
    private static List<Class<?>> getAlternatives(List<Class<?>> classes) {
    	return classes.stream()
    			.filter((clazz) ->
    				clazz.isAnnotationPresent(Alternative.class) &&
    				!clazz.isAnnotationPresent(Stereotype.class))
				.collect(Collectors.toList());
    }
    
    private static List<Class<?>> getStereotypes(List<Class<?>> classes) {
    	return classes.stream()
    			.filter((clazz) ->
    				clazz.isAnnotationPresent(Stereotype.class))
				.collect(Collectors.toList());
    }
    
    private static List<Class<?>> getDecorators(List<Class<?>> classes) {
    	return classes.stream()
    			.filter((clazz) ->
    				clazz.isAnnotationPresent(Decorator.class))
				.collect(Collectors.toList());
    }
    
    private static List<Class<?>> getInterceptors(List<Class<?>> classes) {
    	return classes.stream()
    			.filter((clazz) ->
    				clazz.isAnnotationPresent(Interceptor.class) ||
    				clazz.isAnnotationPresent(Interceptors.class))
    			.collect(Collectors.toList());
    }
    
    private static String[] adapt(Collection<Class<?>> classes) {
    	return classes.stream()
    			.map((clazz) -> clazz.getName())
    			.toArray(String[]::new);
    }
    
    @SafeVarargs
    public static Asset createJavaxEnterpriseSpiExtension(Class<? extends Extension>... extensions) {
    	return createJavaxEnterpriseSpiExtension(Arrays.asList(extensions));
    }
    
    public static Asset createJavaxEnterpriseSpiExtension(Collection<Class<? extends Extension>> extensions) {
    	return new StringAsset(
    			extensions.stream()
        			.map((extension) -> extension.getName())
        			.collect(Collectors.joining(System.lineSeparator())));
    }

}
