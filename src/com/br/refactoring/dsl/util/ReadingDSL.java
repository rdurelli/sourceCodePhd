package com.br.refactoring.dsl.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.FieldDeclaration;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.MethodUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableUnit;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Segment;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.br.refactoring.dsl.refactoring.Attribute;
import com.br.refactoring.dsl.refactoring.DealingWithGeneralization;
import com.br.refactoring.dsl.refactoring.EncapsulateField;
import com.br.refactoring.dsl.refactoring.ExtractClass;
import com.br.refactoring.dsl.refactoring.InlineClass;
import com.br.refactoring.dsl.refactoring.Model;
import com.br.refactoring.dsl.refactoring.MoveAttribute;
import com.br.refactoring.dsl.refactoring.MoveMethod;
import com.br.refactoring.dsl.refactoring.MovingFeaturesBetweenObjects;
import com.br.refactoring.dsl.refactoring.OrganizingData;
import com.br.refactoring.dsl.refactoring.PullUpAttribute;
import com.br.refactoring.dsl.refactoring.PullUpMethod;
import com.br.refactoring.dsl.refactoring.PushDownAttribute;
import com.br.refactoring.dsl.refactoring.PushDownMethod;
import com.br.refactoring.dsl.refactoring.Refactoring;
import com.br.refactoring.dsl.refactoring.RenameAttribute;
import com.br.refactoring.dsl.refactoring.RenameClass;
import com.br.refactoring.dsl.refactoring.RenameFeature;
import com.br.refactoring.dsl.refactoring.RenameMethod;
import com.br.refactoring.dsl.refactoring.ReplaceDataValueWithObject;
import com.br.refactoring.dsl.refactoring.Type;
import com.br.refactoring.xtext.DslStandaloneSetup;
import com.br.util.models.UtilJavaModel;
import com.br.util.models.UtilKDMModel;
import com.google.inject.Injector;
import com.kenai.jffi.Array;


public class ReadingDSL {

	private static UtilKDMModel utilKDMModel = new UtilKDMModel();
	
	private static UtilJavaModel utilJavaModel = new UtilJavaModel();
	
	
	public static void readXTextToApplyTheRefactoring(String applicationDslFileToBeRead, String catalogueRefactoringDslFileToBeRead, String pathToTheKDMFile, String pathToTheJavaModel, String projectURI) {
		
		Segment segment = utilKDMModel.load(pathToTheKDMFile);
		
		utilJavaModel.setUtilKDMModel(utilKDMModel);//tive que fazer isso estava dando erro de mem—ria..
		
		org.eclipse.gmt.modisco.java.Model modelJava = utilJavaModel.load(pathToTheJavaModel);
		
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
							
							
							ClassUnit classUnit = utilKDMModel.getClassUnit(segment, renameClass.getClassToBeRename().getName());
							
							System.out.println(classUnit.getName());
							
							System.out.println(segment.getModel().size());
							
							
						} else if (renameFeature instanceof RenameAttribute) {
							
							RenameAttribute renameAttribute = (RenameAttribute) renameFeature; 
							
							ClassUnit classUnitToRenameTheAttribute = utilKDMModel.getClassUnit(segment, renameAttribute.getSourceClass().getName());
							
							StorableUnit storableUnit = utilKDMModel.getStorablesUnitByName(classUnitToRenameTheAttribute, renameAttribute.getAttributeToBeRename().getName());
							
							
							System.out.println(classUnitToRenameTheAttribute.getName());
							
							System.out.println(storableUnit.getName());	
							
						} else if (renameFeature instanceof RenameMethod) {
							
							RenameMethod renameMethod = (RenameMethod) renameFeature;
							
							ClassUnit classUnitToRenameTheMethod = utilKDMModel.getClassUnit(segment, renameMethod.getSourceClass().getName());
							
							MethodUnit methodUnit = utilKDMModel.getMethodsUnitByName(classUnitToRenameTheMethod, renameMethod.getMethodToBeRename().getName());
							
							System.out.println(methodUnit);
							
						}
						
					}
					
				} else if (type instanceof MovingFeaturesBetweenObjects) {
					
					EList<MovingFeaturesBetweenObjects> allRefactoringsRelatedToMovingFeaturesBetweenObjects = ((MovingFeaturesBetweenObjects) type).getAllRefactorings();
					
					for (MovingFeaturesBetweenObjects movingFeaturesBetweenObjects : allRefactoringsRelatedToMovingFeaturesBetweenObjects) {
						
						if (movingFeaturesBetweenObjects instanceof ExtractClass) {
							
							ExtractClass extractClass = (ExtractClass) movingFeaturesBetweenObjects;
							
							ClassUnit classUnitToExtract = utilKDMModel.getClassUnit(segment, extractClass.getSourceClass().getName());
							
							String [] packageKDM = utilKDMModel.getCompletePackageName(classUnitToExtract); //method utilizado para obter o nome do Pacote, deve-se passar umq classUnit.
							
							ClassDeclaration classDeclarationToExtract = utilJavaModel.getClassDeclaration(classUnitToExtract, packageKDM, modelJava);
							
							String nameToTheNewClass = extractClass.getNewName();
							
							ArrayList<StorableUnit> storableUnits = new ArrayList<StorableUnit>();
							
							ArrayList<FieldDeclaration> fieldDeclarations = new ArrayList<FieldDeclaration>();
							
							EList<Attribute> allAttributes = extractClass.getAttributesToBeMoved();
							
							for (Attribute attribute : allAttributes) {
							
								StorableUnit storableToSetIntoTheList  = utilKDMModel.getStorablesUnitByName(classUnitToExtract, attribute.getName());
								FieldDeclaration fieldDeclaration = utilJavaModel.getFieldDeclarationByName(classDeclarationToExtract, attribute.getName());
								fieldDeclarations.add(fieldDeclaration);
								storableUnits.add(storableToSetIntoTheList);
								
							}
							
							
							
							System.out.println("OU");
							
							utilKDMModel.actionExtractClass(classUnitToExtract, storableUnits, nameToTheNewClass);
							utilJavaModel.actionExtractClass(classDeclarationToExtract, fieldDeclarations, nameToTheNewClass, classUnitToExtract);
							
							
						} else if (movingFeaturesBetweenObjects instanceof MoveAttribute) {
							
							MoveAttribute moveAttribute = (MoveAttribute) movingFeaturesBetweenObjects;
							
							ClassUnit sourceClass = utilKDMModel.getClassUnit(segment, moveAttribute.getSourceClass().getName());
							
							ClassUnit targetClass = utilKDMModel.getClassUnit(segment, moveAttribute.getTargetClass().getName());
							
							StorableUnit attributeToBeMoved = utilKDMModel.getStorablesUnitByName(sourceClass, moveAttribute.getAttributeToBeMoved().getName());
							
							//fazer alguma coisa aqui tenho que colocar todos os refactorings na class UTILKDMMOdel dai Ž s— chamar...
							
							
							
						} else if (movingFeaturesBetweenObjects instanceof MoveMethod) {
							
							MoveMethod moveMethod = (MoveMethod) movingFeaturesBetweenObjects;
							
							ClassUnit sourceClass = utilKDMModel.getClassUnit(segment, moveMethod.getSourceClass().getName());
							
							ClassUnit targetClass = utilKDMModel.getClassUnit(segment, moveMethod.getTargetClass().getName());
							
							MethodUnit methodToBeMoved = utilKDMModel.getMethodsUnitByName(sourceClass, moveMethod.getMethodToBeMoved().getName());
							
							//fazer alguma coisa aqui tenho que colocar todos os refactorings na class UTILKDMMOdel dai Ž s— chamar...
							
						} else if (movingFeaturesBetweenObjects instanceof InlineClass) {
							
							InlineClass inLineClass = (InlineClass) movingFeaturesBetweenObjects;
							
							ClassUnit classToGetAllFeatures = utilKDMModel.getClassUnit(segment, inLineClass.getClassToGetAllFeatures().getName());
							
							ClassUnit classToRemove = utilKDMModel.getClassUnit(segment, inLineClass.getClassToRemove().getName());
							
							//fazer alguma coisa aqui tenho que colocar todos os refactorings na class UTILKDMMOdel dai Ž s— chamar...
							
						}
						
					}
					
				} else if (type instanceof DealingWithGeneralization) {
					
					EList<DealingWithGeneralization> allRefactoringsRelatedToDealingWithGeneralization = ((DealingWithGeneralization) type).getAllRefactorings();
					
					for (DealingWithGeneralization dealingWithGeneralization : allRefactoringsRelatedToDealingWithGeneralization) {
						
						if (dealingWithGeneralization instanceof PullUpAttribute) {
							
							PullUpAttribute pullUpAttribute = (PullUpAttribute) dealingWithGeneralization;
							
							ClassUnit sourceClass = utilKDMModel.getClassUnit(segment, pullUpAttribute.getSourceClass().getName());
							
							ClassUnit targetClass = utilKDMModel.getClassUnit(segment, pullUpAttribute.getTargetClass().getName());
							
							StorableUnit attributeToPullUp = utilKDMModel.getStorablesUnitByName(sourceClass, pullUpAttribute.getAttributeToBePulled().getName());
							
							//fazer alguma coisa aqui tenho que colocar todos os refactorings na class UTILKDMMOdel dai Ž s— chamar...
							
							
						} else if (dealingWithGeneralization instanceof PushDownAttribute) {
							
							PushDownAttribute pushDownAttribute = (PushDownAttribute) dealingWithGeneralization;
							
							ClassUnit sourceClass = utilKDMModel.getClassUnit(segment, pushDownAttribute.getSourceClass().getName());
							
							ClassUnit targetClass = utilKDMModel.getClassUnit(segment, pushDownAttribute.getTargetClass().getName());
							
							StorableUnit attributeToPushDown = utilKDMModel.getStorablesUnitByName(sourceClass, pushDownAttribute.getAttributeToBePushed().getName());
							
							//fazer alguma coisa aqui tenho que colocar todos os refactorings na class UTILKDMMOdel dai Ž s— chamar...
							
							
						} else if (dealingWithGeneralization instanceof PushDownMethod) {
							
							PushDownMethod pushDownMethod = (PushDownMethod) dealingWithGeneralization;
							
							ClassUnit sourceClass = utilKDMModel.getClassUnit(segment, pushDownMethod.getSourceClass().getName());
							
							ClassUnit targetClass = utilKDMModel.getClassUnit(segment, pushDownMethod.getTargetClass().getName());
							
							MethodUnit methodToPushDown = utilKDMModel.getMethodsUnitByName(sourceClass, pushDownMethod.getMethodToBePushed().getName());
							
							//fazer alguma coisa aqui tenho que colocar todos os refactorings na class UTILKDMMOdel dai Ž s— chamar...
						
							
						} else if (dealingWithGeneralization instanceof PullUpMethod) {
							
							PullUpMethod pullUpMethod = (PullUpMethod) dealingWithGeneralization;
							
							ClassUnit sourceClass = utilKDMModel.getClassUnit(segment, pullUpMethod.getSourceClass().getName());
							
							ClassUnit targetClass = utilKDMModel.getClassUnit(segment, pullUpMethod.getTargetClass().getName());
							
							MethodUnit methodToPullUp = utilKDMModel.getMethodsUnitByName(sourceClass, pullUpMethod.getMethodToBePulled().getName());
							
							//fazer alguma coisa aqui tenho que colocar todos os refactorings na class UTILKDMMOdel dai Ž s— chamar...
							
							
						}
						
					}
					
				} else if (type instanceof OrganizingData) {
					
					EList<OrganizingData> allRefactoringsRelatedToOrganizingData = ((OrganizingData) type).getAllRefactorings();
					
					for (OrganizingData organizingData : allRefactoringsRelatedToOrganizingData) {
						
						if (organizingData instanceof ReplaceDataValueWithObject) {
							
							ReplaceDataValueWithObject replaceDataValueWithObject = (ReplaceDataValueWithObject) organizingData;
							
							ClassUnit sourceClass = utilKDMModel.getClassUnit(segment, replaceDataValueWithObject.getSourceClass().getName());
							
							StorableUnit attributeToReplaceDataWithObject = utilKDMModel.getStorablesUnitByName(sourceClass, replaceDataValueWithObject.getAttributeToReplaceDataWithObject().getName());
							
							String nameOfTheNewClass = attributeToReplaceDataWithObject.getName();
							
							List<StorableUnit> storableUnits = new ArrayList<StorableUnit>();
							
							EList<Attribute> allNewAttributes = replaceDataValueWithObject.getNewAttributes();
							
							System.out.println("AQUI");
							
//							utilKDMModel.re
							
						} else if (organizingData instanceof EncapsulateField) {
							
							EncapsulateField encapsulateField = (EncapsulateField) organizingData;
							
							ClassUnit sourceClass = utilKDMModel.getClassUnit(segment, encapsulateField.getSourceClass().getName());
							
							StorableUnit attributeToEncapsulate = utilKDMModel.getStorablesUnitByName(sourceClass, encapsulateField.getAttributeToEncapsulate().getName());
						
							utilKDMModel.actionEncapsulateField(sourceClass, attributeToEncapsulate, modelJava);
							
							//fazer alguma coisa aqui tenho que colocar todos os refactorings na class UTILKDMMOdel dai Ž s— chamar...
							
							System.out.println("AQUI");
						}
						
					}
					
				}
				
			}
			
			utilKDMModel.save(segment, projectURI);
			
			
			utilJavaModel.save(modelJava, projectURI);
			
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
