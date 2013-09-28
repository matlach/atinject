package org.atinject.core.archive;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.atinject.core.integration.Bootstrap;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolvedArtifact;

public class PackageArchive {

    public static void main(String[] args) throws Exception { 
        new PackageArchive().export();
    }
    
    public void export() throws Exception {
        
        Path exportPath = Paths.get(getExportPath());
        File exportDirectory = exportPath.toFile();
        if (exportDirectory.exists()) {
            Files.walkFileTree(exportPath, new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
           
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (exc != null) {
                        throw exc;
                    }
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        exportDirectory.mkdirs();
        
        Path libPath = Paths.get(getExportPath() + "/" + getLibFolderName());
        File libDirectory = libPath.toFile();
        libDirectory.mkdirs();
        
        File logbackXmlFile = Paths.get("src/main/resources").resolve("logback.xml").toFile().getAbsoluteFile();
        File validationXmlFile = Paths.get("src/main/resources").resolve("META-INF/validation.xml").toFile();
        File beansXmlFile = Paths.get("src/main/resources").resolve("META-INF/beans.xml").toFile();
        File javaxEnterpriseInjectSpiExtensionFile = Paths.get("src/main/resources").resolve("META-INF/services/javax.enterprise.inject.spi.Extension").toFile();
        
        JavaArchive atinjectArchive = ShrinkWrap.create(JavaArchive.class, "atinject.jar") 
                .addPackages(true, "org.atinject")
                .addAsManifestResource(beansXmlFile, "beans.xml")
                .addAsManifestResource(validationXmlFile, "validation.xml")
                .addAsManifestResource(javaxEnterpriseInjectSpiExtensionFile, "services/javax.enterprise.inject.spi.Extension")
                .addAsResource(logbackXmlFile, "/logback.xml");
        
        Path atinjectPath = exportPath.resolve("atinject.jar");
        
        atinjectArchive.as(ZipExporter.class).exportTo(atinjectPath.toFile(), true);
        
        String javaCommandTemplate= "" +
"java -cp ${classpath} ${arguments} ${main}";
        
        String unixClasspath = getClasspath().replace(';', ':');
        String windowsClasspath = getClasspath();
        String arguments = getArguments();
        String main = getMain().getName();
        
        String javaUnixCommand = javaCommandTemplate
            .replace("${classpath}", unixClasspath)
            .replace("${arguments}", arguments)
            .replace("${main}", main);
        
        Path atinjectSh = exportPath.resolve("atinject.sh");
        Files.write(atinjectSh, javaUnixCommand.getBytes());
        atinjectSh.toFile().setExecutable(true);
        
        String javaWindowsCommand = javaCommandTemplate
                .replace("${classpath}", windowsClasspath)
                .replace("${arguments}", arguments)
                .replace("${main}", main);
            
        Path atinjectBat = exportPath.resolve("atinject.bat");
        Files.write(atinjectSh, javaWindowsCommand.getBytes());
        atinjectBat.toFile().setExecutable(true);
        
        List<MavenResolvedArtifact> libArtifacts = getLibArtifacts();
        for (MavenResolvedArtifact libArtifact : libArtifacts) {
            Files.copy(
                    libArtifact.asFile().toPath(),
                    libPath.resolve(libArtifact.asFile().getName()),
                    StandardCopyOption.COPY_ATTRIBUTES);
        }
    }
    
    public List<MavenResolvedArtifact> getAllArtifacts() {
        return Arrays.asList(Maven.resolver()
                .loadPomFromFile("pom.xml")
                .importCompileAndRuntimeDependencies()
                .resolve().withTransitivity().asResolvedArtifact());
    }
    
    public List<MavenResolvedArtifact> getLibArtifacts() {
        List<MavenResolvedArtifact> atinjectArtifacts = new ArrayList<>();
        for (MavenResolvedArtifact artifact : getAllArtifacts()){
            if (! artifact.getCoordinate().getGroupId().equals("org.atinject")){
                atinjectArtifacts.add(artifact);
            }
        }
        return atinjectArtifacts;
    }
    
    public List<MavenResolvedArtifact> getAtinjectArtifacts() {
        List<MavenResolvedArtifact> atinjectArtifacts = new ArrayList<>();
        for (MavenResolvedArtifact artifact : getAllArtifacts()){
            if (artifact.getCoordinate().getGroupId().equals("org.atinject")){
                atinjectArtifacts.add(artifact);
            }
        }
        return atinjectArtifacts;
    }
    
    public String getExportPath() {
        return "/Users/mathieu/Desktop/atinject-package";
    }
    
    public Class<?> getMain() {
        return Bootstrap.class;
    }
    
    public String getClasspath() {
        return "./" + getAtinjectJarName() + ";" + getLibFolderName() + "/*";
    }
    
    public String getArguments() {
        return "";
    }
    
    public String getAtinjectJarName() {
        return "atinject.jar";
    }
    
    public String getLibFolderName() {
        return "lib";
    }
}
