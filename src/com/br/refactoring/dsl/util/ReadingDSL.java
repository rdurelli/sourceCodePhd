package com.br.refactoring.dsl.util;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Segment;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.br.refactoring.dsl.refactoring.Model;
import com.br.refactoring.dsl.refactoring.MovingFeaturesBetweenObjects;
import com.br.refactoring.dsl.refactoring.Refactoring;
import com.br.refactoring.dsl.refactoring.RenameClass;
import com.br.refactoring.dsl.refactoring.RenameFeature;
import com.br.refactoring.dsl.refactoring.Type;
import com.br.refactoring.xtext.DslStandaloneSetup;
import com.br.util.models.UtilKDMModel;
import com.google.inject.Injector;


public class ReadingDSL {

	private static UtilKDMModel utilKDMModel = new UtilKDMModel();
	
	
	public static void readXTextToApplyTheRefactoring(String applicationDslFileToBeRead, String catalogueRefactoringDslFileToBeRead, String pathToTheKDMFile) {
		
		new org.eclipse.emf.mwe.utils.StandaloneSetup().setPlatformUri("../");
		Injector injector = new DslStandaloneSetup().createInjectorAndDoEMFRegistration();
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		Resource resource = resourceSet.getResource(URI.createURI(catalogueRefactoringDslFileToBeRead), true);
		Resource resource2 = resourceSet.getResource(URI.createURI(applicationDslFileToBeRead), true);// precisa carregar tbm para obter o nome..
		Model model = (Model) resource.getContents().get(0);
		EList<Type> typesRefactoring = model.getContains();
		
		for (Type type : typesRefactoring) {
			
			if (type instanceof Refactoring) {
				
				
				if (type instanceof RenameFeature) {
					
					EList<RenameFeature> allRefactoringsRelatedToRenamedFeature = ((RenameFeature) type).getAllRefactorings();
					
					for (RenameFeature renameFeature : allRefactoringsRelatedToRenamedFeature) {
						
						if (renameFeature instanceof RenameClass) {
							
							RenameClass renameClass = (RenameClass) renameFeature;
							
							Segment segment = utilKDMModel.load(pathToTheKDMFile);
							
							
							ClassUnit classUnit = utilKDMModel.getClassUnit(segment, renameClass.getClassToBeRename().getName());
							
							System.out.println(classUnit.getName());
							
							System.out.println(segment.getModel().size());
							
							
						}
						
					}
					
				}
				
			}
			
		}
		
	}
	
	public static void read(String dslFileToBeRead) {

		new org.eclipse.emf.mwe.utils.StandaloneSetup().setPlatformUri("../");
		Injector injector = new DslStandaloneSetup().createInjectorAndDoEMFRegistration();
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		Resource resource = resourceSet.getResource(URI.createURI("platform:/resource/TesteLabes/src/catalogue.refactoring"), true);
		Resource resource2 = resourceSet.getResource(URI.createURI("platform:/resource/TesteLabes/src/teste2.refactoring"), true);// precisa carregar tbm para obter o nome..
		Model model = (Model) resource.getContents().get(0);
		EList<Type> typesRefactoring = model.getContains();
		for (Type type : typesRefactoring) {
			if (type instanceof Refactoring) {
				
				
				if (type instanceof RenameFeature) {
					
					EList<RenameFeature> allRefactorinRelatedToRename = ((RenameFeature) type).getAllRefactorings();
					
					for (RenameFeature renameFeature : allRefactorinRelatedToRename) {
					
						if (renameFeature instanceof RenameClass) {
							RenameClass renameClass = (RenameClass) renameFeature;
							System.out.println("A classe to be renamed is " +renameClass.getClassToBeRename().getName());
							System.out.println("O novo nome da classe ser‡ " +renameClass.getNewName());
							
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
