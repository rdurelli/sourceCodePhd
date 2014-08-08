package com.br.utils;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.FieldDeclaration;
import org.eclipse.gmt.modisco.java.MethodDeclaration;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.MethodUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableUnit;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Segment;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.br.refactoring.dsl.refactoring.Model;
import com.br.refactoring.dsl.refactoring.MovingFeaturesBetweenObjects;
import com.br.refactoring.dsl.refactoring.Refactoring;
import com.br.refactoring.dsl.refactoring.RenameAttribute;
import com.br.refactoring.dsl.refactoring.RenameClass;
import com.br.refactoring.dsl.refactoring.RenameFeature;
import com.br.refactoring.dsl.refactoring.RenameMethod;
import com.br.refactoring.dsl.refactoring.Type;
import com.br.refactoring.xtext.DslStandaloneSetup;
import com.br.util.models.UtilJavaModel;
import com.br.util.models.UtilKDMModel;
import com.google.inject.Injector;

public class ReadRefactoringCatalogueFile {

	
	public static void read() {

		new org.eclipse.emf.mwe.utils.StandaloneSetup().setPlatformUri("../");
		Injector injector = new DslStandaloneSetup().createInjectorAndDoEMFRegistration();
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		//colocar aqui para ser din‰mico.....
		Resource resource = resourceSet.getResource(URI.createURI("platform:/resource/Legacy_System_To_Test/src/catalogueRefactoring.refactoring"), true);
		Resource resource2 = resourceSet.getResource(URI.createURI("platform:/resource/Legacy_System_To_Test/src/application.refactoring"), true);// precisa carregar tbm para obter o nome..
		Model model = (Model) resource.getContents().get(0);
		EList<Type> typesRefactoring = model.getContains();
		
		
		UtilJavaModel utilJavaModel = new UtilJavaModel();
		UtilKDMModel utilKDMModel = new UtilKDMModel();
		
		Segment segment = utilKDMModel.load("platform:/resource/Legacy_System_To_Test/MODELS_PIM_modificado/KDMRefactoring.xmi");
		org.eclipse.gmt.modisco.java.Model modelJava = utilJavaModel.load("platform:/resource/Legacy_System_To_Test/MODELS_PIM_modificado/JavaModelRefactoring.javaxmi");

		
		for (Type type : typesRefactoring) {
			if (type instanceof Refactoring) {
				
				
				if (type instanceof RenameFeature) {
					
					EList<RenameFeature> allRefactorinRelatedToRename = ((RenameFeature) type).getAllRefactorings();
					
					for (RenameFeature renameFeature : allRefactorinRelatedToRename) {
					
						if (renameFeature instanceof RenameClass) {
							RenameClass renameClass = (RenameClass) renameFeature;
//							System.out.println("A classe to be renamed is " +renameClass.getClassToBeRename().getName());
							
							String classNameToBeRenamed = renameClass.getClassToBeRename().getName();
							
							String newNameOfTheClass = renameClass.getNewName();
							
							ClassUnit classUnit = utilKDMModel.getClassUnit(segment, classNameToBeRenamed);
							String [] packageKDM = utilKDMModel.getCompletePackageName(classUnit);
							
							ClassDeclaration classDeclaration = utilJavaModel.getClassDeclaration(classUnit, packageKDM, modelJava);
							
							
							
							System.out.println(classDeclaration);
							
							System.out.println(classUnit);
							
							
							
							//apply the refactoring rename here.....
							
							
							
//							utilJavaModel.getC
						
						
							
							System.out.println("O novo nome da classe ser‡ " +renameClass.getNewName());
							
						} else if (renameFeature instanceof RenameAttribute) {
							
							RenameAttribute renameAttribute = (RenameAttribute) renameFeature;
							
							String classThatOwnsTheAttribute = renameAttribute.getSourceClass().getName();
							
							String attributeToBeRenamed = renameAttribute.getAttributeToBeRename().getName();
							
							String newNameOfTheAttribute = renameAttribute.getNewName();
							
							
							ClassUnit classUnit = utilKDMModel.getClassUnit(segment, classThatOwnsTheAttribute);
							String [] packageKDM = utilKDMModel.getCompletePackageName(classUnit);
							
							ClassDeclaration classDeclaration = utilJavaModel.getClassDeclaration(classUnit, packageKDM, modelJava);
							
							StorableUnit storableUnitToBeRenamed = utilKDMModel.getStorablesUnitByName(classUnit, attributeToBeRenamed);
							FieldDeclaration fieldDeclarationToBeRenamed = utilJavaModel.getFieldDeclarationByName(classDeclaration, attributeToBeRenamed);
							
							
							System.out.println(classUnit);
							System.out.println(classDeclaration);
							
							System.out.println(storableUnitToBeRenamed);
							
							System.out.println(fieldDeclarationToBeRenamed);
							
						} else if (renameFeature instanceof RenameMethod) {
							
							RenameMethod renameMethod = (RenameMethod) renameFeature;
							
							String classThatOwnsTheMethodToBeRenamed = renameMethod.getSourceClass().getName();
							
							String methodToBeRenamed = renameMethod.getMethodToBeRename().getName();
							
							String newNameOfTheMethod = renameMethod.getNewName();
									
							ClassUnit classUnit = utilKDMModel.getClassUnit(segment, classThatOwnsTheMethodToBeRenamed);
							String [] packageKDM = utilKDMModel.getCompletePackageName(classUnit);
							
							ClassDeclaration classDeclaration = utilJavaModel.getClassDeclaration(classUnit, packageKDM, modelJava);
							
							MethodUnit methodUnitToBeRenamed = utilKDMModel.getMethodsUnitByName(classUnit, methodToBeRenamed);
							MethodDeclaration methodDeclaration = utilJavaModel.getMethodDeclarationByName(classDeclaration, methodToBeRenamed);
							
							
							
						}
						
					}
					
					
					
				}
				if (type instanceof MovingFeaturesBetweenObjects) {
					
				}
				
//				System.out.println(type.toString());
			}
		}
		
	}
	
}
