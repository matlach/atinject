package org.atinject.annotation;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.junit.Ignore;
import org.junit.Test;

public class EntityBuilderAnnotationProcessorTest {

	@Test @Ignore("run manually to test")
	public void runAnnoationProcessor() throws Exception {
		String source = "src/test/java";

		Iterable<JavaFileObject> files = getSourceFiles(source);

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

		CompilationTask task = compiler.getTask(new PrintWriter(System.out), null, null, null, null, files);
		task.setProcessors(Arrays.asList(new EntityBuilderProcessor()));

		task.call();
	}

	private Iterable<JavaFileObject> getSourceFiles(String path) throws Exception {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager files = compiler.getStandardFileManager(null, null, null);

		files.setLocation(StandardLocation.SOURCE_PATH, Arrays.asList(new File(path)));

		Set<Kind> fileKinds = Collections.singleton(Kind.SOURCE);
		return files.list(StandardLocation.SOURCE_PATH, "", fileKinds, true);
	}
}
