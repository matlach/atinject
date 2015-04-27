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
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes(value= {"*"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class EntityBuilderProcessor extends AbstractProcessor { 

	private Filer filer;
	private Messager messager;

	@Override
	public synchronized void init(ProcessingEnvironment env) {
		filer = env.getFiler();
		messager = env.getMessager();
	}

	private static final String CLASS_TEMPLATE =
	"package ${package};" +
	"${imports}" +
	"${new.line}" +
	"public ${simple.class.name}Builder implements Builder<${simple.class.name}> {${new.line}" +
	"${tab}private ${simple.class.name} entity;${new.line}" +
	"${new.line}" +
	"${builder.constructors}" +
	"${static.builder.methods}" +
	"${fluent.methods}" +
	"${build.method}" +
	"}";
	
	private static final String IMPORT_STATEMENT =
	"import ${canonical.class.name};{new.line}";
	
	private static final String CONSTRUCTOR_TEMPLATE =
	"${tab}public ${simple.class.name}(${constructor.arguments}) {${new.line}" +
	"${tab}${tab}entity = new ${simple.class.name}(${new.entity.arguments});${new.line}" +
	"${tab}}${new.line}";
	
	private static final String BUILD_METHOD_TEMPLATE =
	"${tab}@Override${new.line}" +
	"${tab}public build() {${new.line}" +
	"${tab}${tab}return entity;${new.line}" +
	"${tab}}${new.line}";
	
	@Override
	public boolean process(Set<? extends TypeElement> elements, RoundEnvironment env) {
		for (Element element : env.getRootElements()) {

			if (!element.getSimpleName().toString().endsWith("Entity")) {
				continue;
			}

			String entityBuilderClassName = element.getSimpleName().toString() + "Builder";
			String entityBuilderClassContent = CLASS_TEMPLATE
					.replace("${new.line}", "\n")
					.replace("${tab}", "    ")
					.replace("${package}", "TODO")
					.replace("${imports}", "TODO")
					.replace("${simple.name}", entityBuilderClassName)
					.replace("${builder.constructors}", "TODO")
					.replace("${static.builder.methods}", "TODO")
					.replace("${fluent.methods}", "TODO")
					.replace("${build.method}", "TODO");
			
			try {
				JavaFileObject file = filer.createSourceFile("silly/" + entityBuilderClassName, element);
				file.openWriter()
					.append(entityBuilderClassContent)
					.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}

		}

		return true;
	}
}