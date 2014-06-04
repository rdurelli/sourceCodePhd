package com.br.parser.refactoring;


import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.parsetree.reconstr.Serializer;

import com.br.refactoring.dsl.refactoring.Attribute;
import com.br.refactoring.dsl.refactoring.BasicType;
import com.br.refactoring.dsl.refactoring.Class;
import com.br.refactoring.dsl.refactoring.ClassType;
import com.br.refactoring.dsl.refactoring.Method;
import com.br.refactoring.dsl.refactoring.Model;
import com.br.refactoring.dsl.refactoring.RefactoringFactory;
import com.br.refactoring.xtext.DslRuntimeModule;
import com.br.refactoring.xtext.DslStandaloneSetup;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class CreateModelRefactoringProgrammatically {

	public static void main(String[] args) {
	
		CharStream stream = null;
		try {
			stream = new org.antlr.runtime.ANTLRFileStream(
					"/Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/antlr-generated/a/b/c/Person.java");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		JavaLexer lexer = new JavaLexer(stream);

		TokenStream tokenStream = new CommonTokenStream(lexer);

		JavaParser parser = new JavaParser(tokenStream);

		try {
			parser.compilationUnit();
			// parser.packageDeclaration();
			// parser.importDeclaration();
			// parser.typeDeclaration();
			// parser.classDeclaration();
			// parser.fieldDeclaration();
			// parser.genericMethodOrConstructorRest();
			// parser.methodDeclaration();
			// System.out.println(parser.getNames());
			System.out.println("O nome da classe Ž tal "
					+ parser.getClassModelName());

			System.out.println("O nome do pacote Ž tal "
					+ parser.getPackageModelName());

			ArrayList<AttributeModelRefactoring> modelAt = parser
					.getAttributesInfo();

			for (AttributeModelRefactoring attributeModelRefactoring : modelAt) {
				System.out
						.println(attributeModelRefactoring.getAttributeName());
				System.out
						.println(attributeModelRefactoring.getAttributeType());
			}

			ArrayList<MethodModelRefactoring> modelMethod = parser
					.getMethodName();

			for (MethodModelRefactoring methodModelRefactoring : modelMethod) {
				System.out.println(methodModelRefactoring.getMethodSignature());
			}

			// System.out.println(parser.getImports());
		} catch (RecognitionException e) {

			e.printStackTrace();
		}
	}

	//primeiroDeve chamar esse metodo (NOT used anymore, it was used when I was using the xtext API.)
	public void callParserToGetAllClasses(String fileJavaLocation,
			ArrayList<Class> allClasses) {

		CharStream stream = null;
		try {
			stream = new org.antlr.runtime.ANTLRFileStream(fileJavaLocation);
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}

		JavaLexer lexer = new JavaLexer(stream);

		TokenStream tokenStream = new CommonTokenStream(lexer);

		JavaParser parser = new JavaParser(tokenStream);

		try {
			parser.compilationUnit();

			Class classToWork = RefactoringFactory.eINSTANCE.createClass();

			String className = parser.getClassModelName();

			classToWork.setName(className);

			allClasses.add(classToWork);
	

		} catch (RecognitionException e) {

			e.printStackTrace();
		}

	}

	public void callParser(String fileJavaLocation, Model model, ArrayList<Class> allClasses) {

		CharStream stream = null;
		try {
			stream = new org.antlr.runtime.ANTLRFileStream(fileJavaLocation);
		} catch (IOException e1) {

			e1.printStackTrace();
		}

		JavaLexer lexer = new JavaLexer(stream);

		TokenStream tokenStream = new CommonTokenStream(lexer);

		JavaParser parser = new JavaParser(tokenStream);

		try {
			parser.compilationUnit();

			String className = parser.getClassModelName();
			
			ArrayList<AttributeModelRefactoring> attributes = parser
					.getAttributesInfo();

			ArrayList<MethodModelRefactoring> methods = parser
					.getMethodName();
					
			createModel(model, "StringAMudar", "tambemAMudar", className, allClasses, attributes, methods);
			
		} catch (RecognitionException e) {

			e.printStackTrace();
		}

	}

	private void createModel(Model model, String path, String projectName, String className,
			ArrayList<Class> allClasses,
			ArrayList<AttributeModelRefactoring> attributes,
			ArrayList<MethodModelRefactoring> methods) {

		// create the Class of the refactoring.ecore
		// classRefactoring.setName(className);//set the class's name
		//
		Class classToWork = null;

		for (Class cla : allClasses) {
			if (cla.getName().equals(className)) {

				classToWork = cla;

			}
		}

		if (classToWork != null) {

			model.getContains().add(classToWork);
			
			if (attributes != null) {// if there is attribute so perform the if

				for (AttributeModelRefactoring attrs : attributes) {

					String attributeName = attrs.getAttributeName();
					
						String[] arrayString = attributeName.split(".=.");
						
						
						
						if (arrayString != null) {
							
							attributeName = arrayString[0];
							
						} 
						
						attributeName.trim();
						
						if (attributeName.endsWith(";")) {
							String modified = attributeName.replace(";", "");
							attributeName = modified;
							
						}
	
					
					
//					attributeName
					
					System.out.println(attributeName);
					
					String attributeType = attrs.getAttributeType();
					
					System.out.println(attributeType);
					
					boolean isBasicType = false;

					// typeName = ('string' | 'int' | 'boolean' | 'float' |
					// 'char' | 'byte' | 'short' | 'long' | 'double')
					if (attributeType.equals("String")) {

						attributeType = "string";
						isBasicType = true;

					} else if (attributeType.equals("byte")
							|| attributeType.equals("Byte")) {

						attributeType = "byte";
						isBasicType = true;

					} else if (attributeType.equals("int")
							|| attributeType.equals("Integer")) {

						attributeType = "int";
						isBasicType = true;

					} else if (attributeType.equals("boolean")
							|| attributeType.equals("Boolean")) {

						attributeType = "boolean";
						isBasicType = true;

					} else if (attributeType.equals("float")
							|| attributeType.equals("Float")) {

						attributeType = "float";
						isBasicType = true;

					} else if (attributeType.equals("char")
							|| attributeType.equals("Character")) {

						attributeType = "char";
						isBasicType = true;

					} else if (attributeType.equals("short")
							|| attributeType.equals("Short")) {

						attributeType = "short";
						isBasicType = true;

					} else if (attributeType.equals("long")
							|| attributeType.equals("Long")) {

						attributeType = "long";
						isBasicType = true;

					} else if (attributeType.equals("double")
							|| attributeType.equals("Double")) {

						attributeType = "double";
						isBasicType = true;

					}

					Attribute attribute = RefactoringFactory.eINSTANCE
							.createAttribute();
					attribute.setName(attributeName);
					classToWork.getAttributes().add(attribute);
					if (isBasicType) {

						BasicType basicType = RefactoringFactory.eINSTANCE
								.createBasicType();
						basicType.setTypeName(attributeType);
						attribute.setElementType(basicType);
//						classToWork.getAttributes().add(attribute);

					} else {
						Class classReferenciaToBeAttribute = null;
						for (Class classRefe : allClasses) {
							
							System.out.println(classRefe.getName());
							
							if (classRefe.getName().equals(attrs.getAttributeType())) {

								classReferenciaToBeAttribute = classRefe;

							}
						}
						
						if (classReferenciaToBeAttribute != null) {
							
							ClassType classType = RefactoringFactory.eINSTANCE.createClassType();
							classType.setClassType(classReferenciaToBeAttribute);
						
							attribute.setElementType(classType);
							System.out.println("setou o elementType");
							
						}
						
						
					}

				}

			}
			if (methods != null) {
				
				for (MethodModelRefactoring methodModelRefactoring : methods) {
					Method method = RefactoringFactory.eINSTANCE.createMethod();
					String methodSignature = methodModelRefactoring.getMethodSignature();
					method.setName(methodSignature);
					classToWork.getMethods().add(method);
				}
				
			}
		}
	}

	
	public void callParserIFile(IFile fileToCreate, String fileJavaLocation, StringBuffer contents) {

		CharStream stream = null;
		try {
			stream = new org.antlr.runtime.ANTLRFileStream(fileJavaLocation);
		} catch (IOException e1) {

			e1.printStackTrace();
		}

		JavaLexer lexer = new JavaLexer(stream);

		TokenStream tokenStream = new CommonTokenStream(lexer);

		JavaParser parser = new JavaParser(tokenStream);

		try {
			parser.compilationUnit();

			String className = parser.getClassModelName();
			
			ArrayList<AttributeModelRefactoring> attributes = parser
					.getAttributesInfo();

			ArrayList<MethodModelRefactoring> methods = parser
					.getMethodName();
					
			
			saveIFile(fileToCreate, contents, className, attributes, methods);
	
		} catch (RecognitionException e) {
			e.printStackTrace();
		}

	}
	
	public void saveIFile (IFile fileToCreate, StringBuffer contents, String className, ArrayList<AttributeModelRefactoring> attributes,
			ArrayList<MethodModelRefactoring> methods ) {
		
		contents.append("\n\nClass ");
		contents.append( className+ " {");
		createAttributeIFile(contents, attributes);
		if (methods != null) {
			contents.append("\n\n");
			createMethodIFile(contents, methods);
		}
		contents.append("\n}");
		
		
	}
	
	private void createMethodIFile(StringBuffer contents,
			ArrayList<MethodModelRefactoring> methods) {
		
		for (MethodModelRefactoring methodModelRefactoring : methods) {
			contents.append("\n def " + methodModelRefactoring.getMethodSignature());
		}
		
	}

	private StringBuffer createAttributeIFile (StringBuffer contents, ArrayList<AttributeModelRefactoring> attributes ) {
		
		for (AttributeModelRefactoring attributeModelRefactoring : attributes) {
		
			String attributeName = attributeModelRefactoring.getAttributeName();
			
			String[] arrayString = attributeName.split(".=.");
			
			if (arrayString != null) {
				
				attributeName = arrayString[0];
				
			} 
			
			attributeName.trim();
			
			if (attributeName.endsWith(";")) {
				String modified = attributeName.replace(";", "");
				attributeName = modified;
				
			}
			
			String attributeType = attributeModelRefactoring.getAttributeType();

			// typeName = ('string' | 'int' | 'boolean' | 'float' |
			// 'char' | 'byte' | 'short' | 'long' | 'double')
			if (attributeType.equals("String")) {

				attributeType = "string";

			} else if (attributeType.equals("byte")
					|| attributeType.equals("Byte")) {

				attributeType = "byte";

			} else if (attributeType.equals("int")
					|| attributeType.equals("Integer")) {

				attributeType = "int";


			} else if (attributeType.equals("boolean")
					|| attributeType.equals("Boolean")) {

				attributeType = "boolean";
	

			} else if (attributeType.equals("float")
					|| attributeType.equals("Float")) {

				attributeType = "float";
	

			} else if (attributeType.equals("char")
					|| attributeType.equals("Character")) {

				attributeType = "char";
				

			} else if (attributeType.equals("short")
					|| attributeType.equals("Short")) {

				attributeType = "short";
				

			} else if (attributeType.equals("long")
					|| attributeType.equals("Long")) {

				attributeType = "long";
				

			} else if (attributeType.equals("double")
					|| attributeType.equals("Double")) {

				attributeType = "double";
				

			}
			
			contents.append("\n @ "  +attributeType + " " + attributeName );
			
		}
		
		return contents;
		
		
	}
	
	//Not used, I have tried save the file by using the API of Xtext programmatically, but it seems not to work..In fact, it worked well, but not for cross reference...
	public void saveXText(Model model) {
		
		DslStandaloneSetup.doSetup();
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet
				.createResource(URI
						.createURI("platform:/resource/TesteLabes/src/CriadoProgrammatically.refactoring"));
		Injector injector = Guice.createInjector(new DslRuntimeModule());
		Serializer serializer = injector.getInstance(Serializer.class);  
		
		String s = serializer.serialize(model);
		
		System.out.println(s);
		
		
		resource.getContents().add(model);
		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {

			e.printStackTrace();
		}
		
	}
	
}
