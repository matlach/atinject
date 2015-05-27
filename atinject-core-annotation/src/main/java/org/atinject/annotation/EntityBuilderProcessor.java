package org.atinject.annotation;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
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
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

@SupportedAnnotationTypes(value= {"*"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class EntityBuilderProcessor extends AbstractProcessor { 

	private ProcessingEnvironment processingEnvironment;
	private Filer filer;
	private Messager messager;

	@Override
	public synchronized void init(ProcessingEnvironment env) {
		processingEnvironment = env;
		filer = env.getFiler();
		messager = env.getMessager();
	}

	@Override
	public boolean process(Set<? extends TypeElement> elements, RoundEnvironment env) {
		for (Element element : env.getRootElements()) {
			if (isEntity(element)) {
				processEntity((TypeElement) element);
			}
		}
		return true;
	}
	
	private boolean isEntity(Element element) {
		return element.getKind().isClass() &&
			   element.getSimpleName().toString().endsWith("Entity");
	}
	
	private void processEntity(TypeElement entityTypeElement) {
		try {
			if (entityTypeElement.getModifiers().contains(Modifier.ABSTRACT)) {
				// skip
				return;
			}
			
			JCodeModel cm = new JCodeModel();
			String entityBuilderClassName = entityTypeElement.getQualifiedName().toString() + "Builder";
			JClass entityClass = cm.ref(entityTypeElement.getQualifiedName().toString());
			JClass builderInterface = cm.ref("org.atinject.builder.Builder").narrow(entityClass);
			JDefinedClass builderClass = cm
					._class(entityBuilderClassName)
					._implements(builderInterface);
			
			// builder protected constructor
			builderClass.constructor(JMod.NONE);
			// builder static builder method
			builderClass.method(JMod.PUBLIC + JMod.STATIC, builderClass, "builder").body()._return(JExpr._new(builderClass));
			
			JMethod builderBuildMethod = builderClass.method(JMod.PUBLIC, entityClass, "build");
			JBlock builderBuildMethodBody = builderBuildMethod.body();
			
			JVar builderBuildMethodNewInstanceVar = builderBuildMethodBody.decl(entityClass, "newInstance", JExpr._new(entityClass));
			for (Element enclosedElement : entityTypeElement.getEnclosedElements()) {
				if (enclosedElement.getKind().isField()) {
					VariableElement variableElement = (VariableElement) enclosedElement;
					if ("serialVersionUID".equals(variableElement.getSimpleName())) {
						// skip
						continue;
					}
//					skip?
//					if (variableElement.getModifiers().contains(Modifier.STATIC) ||
//						variableElement.getModifiers().contains(Modifier.TRANSIENT)) {
//						// skip
//						continue;
//					}
					
					//TODO skip transient
					JClass builderFieldClass = cm.ref(((VariableElement)enclosedElement).asType().toString());
					JFieldVar builderField = builderClass.field(JMod.PRIVATE, builderFieldClass, enclosedElement.getSimpleName().toString());
					
					// field fluent setter
					JMethod fluentSetter = builderClass.method(JMod.PUBLIC, builderClass, enclosedElement.getSimpleName().toString());
					JVar fluentSetterParameter = fluentSetter.param(JMod.NONE, builderFieldClass, enclosedElement.getSimpleName().toString());
					fluentSetter.body()
						.assign(JExpr._this().ref(builderField), fluentSetterParameter)
						._return(JExpr._this());
					
					// builder method assign
					// TODO remove if statement if field is @NotNull or a primitive
					builderBuildMethodBody._if(builderField.eq(JExpr._null()).not())._then()
						.invoke(builderBuildMethodNewInstanceVar, "set" + enclosedElement.getSimpleName().toString().substring(0, 1).toUpperCase() + enclosedElement.getSimpleName().toString().substring(1))
							.arg(builderField);
				}
			}
			
			builderBuildMethodBody._return(builderBuildMethodNewInstanceVar);
			
			
			Path tempDir = Files.createTempDirectory(entityBuilderClassName);
			cm.build(tempDir.toFile());
			Path generatedContentPath = tempDir.resolve(entityBuilderClassName.replace(".", "/").concat(".java"));
			String classContent = new String(Files.readAllBytes(generatedContentPath), Charset.forName("UTF-8"));
			
			JavaFileObject file = filer.createSourceFile(entityBuilderClassName, entityTypeElement);
			file.openWriter()
				.append(classContent)
				.close();
		}
		catch (Exception e) {
			messager.printMessage(Kind.ERROR, e.getMessage());
			e.printStackTrace();
		}
	}
	
}