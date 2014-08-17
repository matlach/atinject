package org.atinject.annotation;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes(value= {"*"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class SillyProcessor extends AbstractProcessor { 

	private Filer filer;
	private Messager messager;

	@Override
	public void init(ProcessingEnvironment env) {
		filer = env.getFiler();
		messager = env.getMessager();
	}

	@Override
	public boolean process(Set<? extends TypeElement> elements, RoundEnvironment env) {

		for (Element element : env.getRootElements()) {

			if (element.getSimpleName().toString().startsWith("Silly")) {
				// We don't want generate new silly classes 
				// for auto-generated silly classes
				continue;
			}

			if (element.getSimpleName().toString().startsWith("T")) {
				messager.printMessage(Diagnostic.Kind.WARNING, 
					"This class name starts with a T!", 
					element);
				messager.printMessage(Diagnostic.Kind.NOTE, 
						"This class name starts with a T!", 
						element);
				messager.printMessage(Diagnostic.Kind.OTHER, 
						"This class name starts with a T!", 
						element);
			}

			String sillyClassName = "Silly" + element.getSimpleName();
			String sillyClassContent = 
					"package silly;\n" 
				+	"public class " + sillyClassName + " {\n"
				+	"	public String foobar;\n"
				+	"}";

			JavaFileObject file = null;

			try {
				file = filer.createSourceFile(
						"silly/" + sillyClassName, 
						element);
				file.openWriter()
					.append(sillyClassContent)
					.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return true;
	}
}