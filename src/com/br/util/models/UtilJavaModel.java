package com.br.util.models;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.gmt.modisco.java.AbstractTypeDeclaration;
import org.eclipse.gmt.modisco.java.Assignment;
import org.eclipse.gmt.modisco.java.AssignmentKind;
import org.eclipse.gmt.modisco.java.Block;
import org.eclipse.gmt.modisco.java.BodyDeclaration;
import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.CompilationUnit;
import org.eclipse.gmt.modisco.java.Expression;
import org.eclipse.gmt.modisco.java.ExpressionStatement;
import org.eclipse.gmt.modisco.java.FieldAccess;
import org.eclipse.gmt.modisco.java.FieldDeclaration;
import org.eclipse.gmt.modisco.java.InheritanceKind;
import org.eclipse.gmt.modisco.java.MethodDeclaration;
import org.eclipse.gmt.modisco.java.Model;
import org.eclipse.gmt.modisco.java.Modifier;
import org.eclipse.gmt.modisco.java.NamedElement;
import org.eclipse.gmt.modisco.java.Package;
import org.eclipse.gmt.modisco.java.PrimitiveTypeBoolean;
import org.eclipse.gmt.modisco.java.PrimitiveTypeByte;
import org.eclipse.gmt.modisco.java.PrimitiveTypeChar;
import org.eclipse.gmt.modisco.java.PrimitiveTypeDouble;
import org.eclipse.gmt.modisco.java.PrimitiveTypeFloat;
import org.eclipse.gmt.modisco.java.PrimitiveTypeInt;
import org.eclipse.gmt.modisco.java.PrimitiveTypeLong;
import org.eclipse.gmt.modisco.java.PrimitiveTypeShort;
import org.eclipse.gmt.modisco.java.PrimitiveTypeVoid;
import org.eclipse.gmt.modisco.java.ReturnStatement;
import org.eclipse.gmt.modisco.java.SingleVariableAccess;
import org.eclipse.gmt.modisco.java.SingleVariableDeclaration;
import org.eclipse.gmt.modisco.java.Statement;
import org.eclipse.gmt.modisco.java.ThisExpression;
import org.eclipse.gmt.modisco.java.Type;
import org.eclipse.gmt.modisco.java.TypeAccess;
import org.eclipse.gmt.modisco.java.VariableDeclarationFragment;
import org.eclipse.gmt.modisco.java.VisibilityKind;
import org.eclipse.gmt.modisco.java.emf.JavaFactory;
import org.eclipse.gmt.modisco.java.emf.JavaPackage;
import org.eclipse.gmt.modisco.java.generation.files.GenerateJavaExtended;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeFactory;
import org.jruby.javasupport.Java;

import com.br.utils.ProjectSelectedToModernize;

public class UtilJavaModel {


	public Model load(String javaModelFullPath){

		
		
		JavaPackage.eINSTANCE.eClass();

		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("website", new XMIResourceFactoryImpl());

		// Obtain a new resource set
		ResourceSet resSet = new ResourceSetImpl();

		// Get the resource
		Resource resource = resSet.getResource(URI
				.createURI(javaModelFullPath), true);
		// Get the first model element and cast it to the right type, in my
		// example everything is hierarchical included in this first node
		System.out.println(resource.getContents().get(0).toString());
		
		return (Model) resource.getContents().get(0);
	}

	
	private CompilationUnit createCompilationUnit (Model model, org.eclipse.gmt.modisco.java.Package packageJAVA, ClassDeclaration type) {
		
		CompilationUnit compilationUnit = JavaFactory.eINSTANCE.createCompilationUnit();
		compilationUnit.setName(type.getName()+".java");
		compilationUnit.setProxy(false);
		compilationUnit.setOriginalFilePath("/Users/rafaeldurelli/Documents/runtime-EclipseApplication/Legacy_System_To_Test/src/com/br/teste/"+type.getName()+".java");
		compilationUnit.setPackage(packageJAVA);
		compilationUnit.getTypes().add(type);
		model.getCompilationUnits().add(compilationUnit);
		
		return compilationUnit;
		
		
	}
	
	private Modifier createModifierForClass (ClassDeclaration bodyDeclaration) {
		
		Modifier modifier = JavaFactory.eINSTANCE.createModifier();
		modifier.setVisibility(VisibilityKind.PUBLIC);
		modifier.setInheritance(InheritanceKind.NONE);
		modifier.setStatic(false);
		modifier.setTransient(false);
		modifier.setVolatile(false);
		modifier.setNative(false);
		modifier.setStrictfp(false);
		modifier.setSynchronized(false);
		modifier.setBodyDeclaration(bodyDeclaration);
		
		
		return modifier;
	}
	
	public ClassDeclaration createClassDeclaration (String name, org.eclipse.gmt.modisco.java.Package packageObtained, Model model) {
		
//		ClassUnit classUnitToBeCreated = CodeFactory.eINSTANCE.createClassUnit();
//		
//		classUnitToBeCreated.setName(name);
//		classUnitToBeCreated.setIsAbstract(false);
//		classUnitToBeCreated.getSource().add(this.criarSource(name));
//		
//		
//		packageObtained.getCodeElement().add(classUnitToBeCreated);
//		
//		return classUnitToBeCreated;
		
		ClassDeclaration classDeclarationToBeCreated = JavaFactory.eINSTANCE.createClassDeclaration();
		classDeclarationToBeCreated.setName(name);
		classDeclarationToBeCreated.setOriginalCompilationUnit(this.createCompilationUnit(model, packageObtained, classDeclarationToBeCreated));
		classDeclarationToBeCreated.setModifier(this.createModifierForClass(classDeclarationToBeCreated));
		classDeclarationToBeCreated.setPackage(packageObtained);
		
		
		packageObtained.getOwnedElements().add(classDeclarationToBeCreated);
		
		return classDeclarationToBeCreated;
		
	}
	
	private Modifier createModifierForMethodDeclaration(MethodDeclaration method) {
		
		Modifier modifier = JavaFactory.eINSTANCE.createModifier();
		modifier.setVisibility(VisibilityKind.PUBLIC);
		modifier.setInheritance(InheritanceKind.NONE);
		modifier.setStatic(false);
		modifier.setTransient(false);
		modifier.setVolatile(false);
		modifier.setStrictfp(false);
		modifier.setSynchronized(false);
		modifier.setBodyDeclaration(method);
		
		return modifier;
		
	}
	
	private Modifier createModifierForFieldDeclaration(FieldDeclaration field) {
		
		Modifier modifier = JavaFactory.eINSTANCE.createModifier();
		modifier.setVisibility(VisibilityKind.PRIVATE);
		modifier.setInheritance(InheritanceKind.NONE);
		modifier.setStatic(false);
		modifier.setTransient(false);
		modifier.setVolatile(false);
		modifier.setStrictfp(false);
		modifier.setSynchronized(false);
		modifier.setBodyDeclaration(field);
		
		
		return modifier;
		
	}
	
	private void createBodyForMethodDeclaration (MethodDeclaration method) {
		
		Block block = JavaFactory.eINSTANCE.createBlock();
		block.setOriginalCompilationUnit(method.getOriginalCompilationUnit());
		block.getStatements().add(this.createStatementReturnForMethod(block));
		
	}
	
	private ReturnStatement createStatementReturnForMethod (Block block) {
		
		ReturnStatement returnStatement = JavaFactory.eINSTANCE.createReturnStatement();
		returnStatement.setOriginalCompilationUnit(block.getOriginalCompilationUnit());
		
		return returnStatement;
	}
	
	private SingleVariableAccess createExpressionForMethod(ReturnStatement returnStatement, Block block, MethodDeclaration method) {
		
		SingleVariableAccess single = JavaFactory.eINSTANCE.createSingleVariableAccess();
		
		
		
		SingleVariableDeclaration singleDeclaration = JavaFactory.eINSTANCE.createSingleVariableDeclaration();
		singleDeclaration.setName("qualquer");
		singleDeclaration.setProxy(false);
		singleDeclaration.setExtraArrayDimensions(0);
		singleDeclaration.setVarargs(false);
		singleDeclaration.setOriginalCompilationUnit(block.getOriginalCompilationUnit());
		singleDeclaration.setModifier(this.createModifierForSingleVariableDeclaration(singleDeclaration));
		singleDeclaration.setMethodDeclaration(method);
		
		single.setVariable(singleDeclaration);
		
		Assignment assignment = JavaFactory.eINSTANCE.createAssignment();
		assignment.setOriginalCompilationUnit(block.getOriginalCompilationUnit());
		//cointainer
		FieldAccess field = JavaFactory.eINSTANCE.createFieldAccess();
		field.setOriginalCompilationUnit(block.getOriginalCompilationUnit());
		
		
		return null;
		
	}
	
	private Modifier createModifierForSingleVariableDeclaration (SingleVariableDeclaration singleDeclaration) {
		
		Modifier modifier = JavaFactory.eINSTANCE.createModifier();
		modifier.setVisibility(VisibilityKind.NONE);
		modifier.setInheritance(InheritanceKind.NONE);
		modifier.setStatic(false);
		modifier.setTransient(false);
		modifier.setVolatile(false);
		modifier.setStrictfp(false);
		modifier.setSynchronized(false);
		modifier.setSingleVariableDeclaration(singleDeclaration);
		
		return modifier;
		
		
	}
	
	public FieldDeclaration createFieldDeclaration(String name, ClassDeclaration classDeclaration, String type, Model model) {
		
		FieldDeclaration field = JavaFactory.eINSTANCE.createFieldDeclaration();
		field.setProxy(false);
		field.setOriginalCompilationUnit(classDeclaration.getOriginalCompilationUnit());
		field.setAbstractTypeDeclaration(classDeclaration);
		field.setModifier(this.createModifierForFieldDeclaration(field));
		TypeAccess typeAcess = JavaFactory.eINSTANCE.createTypeAccess();
		typeAcess.setType(this.getStringType(model));
		field.setType(typeAcess);
		
		VariableDeclarationFragment teste = JavaFactory.eINSTANCE.createVariableDeclarationFragment();
		
		teste.setName(name);
		teste.setProxy(false);
		teste.setExtraArrayDimensions(0);
		teste.setOriginalCompilationUnit(classDeclaration.getOriginalCompilationUnit());
		//teste.setVariablesContainer(field);
		//field.getFragments().add(teste);
		
		
		return field;
		
	} 
	
	public MethodDeclaration createMethodDeclarationGET(String name, ClassDeclaration classToPutTheMethod, FieldDeclaration fieldDeclaration, String nameAttribute, Model model){
		
		MethodDeclaration newMethod = JavaFactory.eINSTANCE.createMethodDeclaration();
		newMethod.setName(name);
		newMethod.setProxy(false);
		newMethod.setExtraArrayDimensions(0);
		newMethod.setOriginalCompilationUnit(classToPutTheMethod.getOriginalCompilationUnit());
		newMethod.setAbstractTypeDeclaration(classToPutTheMethod);
		newMethod.setModifier(this.createModifierForMethodDeclaration(newMethod));
		
		Block block = JavaFactory.eINSTANCE.createBlock();
		newMethod.setBody(block);
		
		block.setOriginalCompilationUnit(classToPutTheMethod.getOriginalCompilationUnit());
		
		//create The return statement 
		
		ReturnStatement returnStatement = JavaFactory.eINSTANCE.createReturnStatement();
		returnStatement.setOriginalCompilationUnit(classToPutTheMethod.getOriginalCompilationUnit());
		block.getStatements().add(returnStatement);//adiciona o return statement no block...
		
		//create the expression...
		
		SingleVariableAccess single = JavaFactory.eINSTANCE.createSingleVariableAccess();
		
		returnStatement.setExpression(single);//coloca o SingleVariableAcessNoReturnStatment..
		
		VariableDeclarationFragment variableDeclarationFragment = JavaFactory.eINSTANCE.createVariableDeclarationFragment();
		
		
		variableDeclarationFragment.setName(nameAttribute);
		variableDeclarationFragment.setProxy(false);
		variableDeclarationFragment.setExtraArrayDimensions(0);
		variableDeclarationFragment.setOriginalCompilationUnit(classToPutTheMethod.getOriginalCompilationUnit());
		variableDeclarationFragment.setVariablesContainer(fieldDeclaration);
		
//		SingleVariableDeclaration singleDeclaration = JavaFactory.eINSTANCE.createSingleVariableDeclaration();
//		singleDeclaration.setName("qualquer");
//		singleDeclaration.setProxy(false);
//		singleDeclaration.setExtraArrayDimensions(0);
//		singleDeclaration.setVarargs(false);
//		singleDeclaration.setOriginalCompilationUnit(block.getOriginalCompilationUnit());
//		singleDeclaration.setModifier(this.createModifierForSingleVariableDeclaration(singleDeclaration));
//		singleDeclaration.setMethodDeclaration(newMethod);//o problema do paramenter esta aqui...
		
		TypeAccess typeAccess = JavaFactory.eINSTANCE.createTypeAccess();
		typeAccess.setType(this.getStringType(model));
		
		
		single.setVariable(variableDeclarationFragment);
		
		newMethod.setReturnType(typeAccess);//colocar quando eu pegar os tipos...
		
		
		classToPutTheMethod.getBodyDeclarations().add(newMethod);//adiciona o method para o containner..
		
		return newMethod;
	}

	
	public MethodDeclaration createMethodDeclarationSET(String name, ClassDeclaration classToPutTheMethod, FieldDeclaration fieldDeclaration, String nameAttribute, Model model){
		
		MethodDeclaration newMethod = JavaFactory.eINSTANCE.createMethodDeclaration();
		newMethod.setName(name);
		newMethod.setProxy(false);
		newMethod.setExtraArrayDimensions(0);
		newMethod.setOriginalCompilationUnit(classToPutTheMethod.getOriginalCompilationUnit());
		newMethod.setAbstractTypeDeclaration(classToPutTheMethod);
		newMethod.setModifier(this.createModifierForMethodDeclaration(newMethod));
		
		Block block = JavaFactory.eINSTANCE.createBlock();
		newMethod.setBody(block);
		
		block.setOriginalCompilationUnit(classToPutTheMethod.getOriginalCompilationUnit());
		
		
		//create the expressionStatement
		
		ExpressionStatement expressionStatement = JavaFactory.eINSTANCE.createExpressionStatement();
		expressionStatement.setOriginalCompilationUnit(classToPutTheMethod.getOriginalCompilationUnit());

		Assignment expression = JavaFactory.eINSTANCE.createAssignment();
		expression.setOperator(AssignmentKind.ASSIGN);
		expression.setOriginalCompilationUnit(classToPutTheMethod.getOriginalCompilationUnit());
		
		

		
		FieldAccess fieldAccess = JavaFactory.eINSTANCE.createFieldAccess();
		fieldAccess.setOriginalCompilationUnit(classToPutTheMethod.getOriginalCompilationUnit());
		ThisExpression thisExpression = JavaFactory.eINSTANCE.createThisExpression();
		fieldAccess.setExpression(thisExpression);
		thisExpression.setOriginalCompilationUnit(classToPutTheMethod.getOriginalCompilationUnit());
		expression.setLeftHandSide(fieldAccess);
		
		expressionStatement.setExpression(expression);
		
		
		block.getStatements().add(expressionStatement);//adiciona o return statement no block...
		
		//create the expression...
		
		SingleVariableAccess single = JavaFactory.eINSTANCE.createSingleVariableAccess();
		
		SingleVariableAccess singleRightHand = JavaFactory.eINSTANCE.createSingleVariableAccess();
		
		expression.setRightHandSide(singleRightHand);
		
		SingleVariableDeclaration variableDeclarationFragment = JavaFactory.eINSTANCE.createSingleVariableDeclaration();
		
		fieldAccess.setField(single);
		
		variableDeclarationFragment.setName(nameAttribute);
		variableDeclarationFragment.setProxy(false);
		variableDeclarationFragment.setExtraArrayDimensions(0);
		variableDeclarationFragment.setVarargs(false);
		variableDeclarationFragment.setOriginalCompilationUnit(classToPutTheMethod.getOriginalCompilationUnit());
		variableDeclarationFragment.setModifier(this.createModifierForSingleVariableDeclaration(variableDeclarationFragment));
		variableDeclarationFragment.setMethodDeclaration(newMethod);
		TypeAccess typeString = JavaFactory.eINSTANCE.createTypeAccess();
		typeString.setOriginalCompilationUnit(classToPutTheMethod.getOriginalCompilationUnit());
		typeString.setType(this.getStringType(model));
		variableDeclarationFragment.setType(typeString);
//		variableDeclarationFragment.setVariablesContainer(fieldDeclaration);
		
//		SingleVariableDeclaration singleDeclaration = JavaFactory.eINSTANCE.createSingleVariableDeclaration();
//		singleDeclaration.setName("qualquer");
//		singleDeclaration.setProxy(false);
//		singleDeclaration.setExtraArrayDimensions(0);
//		singleDeclaration.setVarargs(false);
//		singleDeclaration.setOriginalCompilationUnit(block.getOriginalCompilationUnit());
//		singleDeclaration.setModifier(this.createModifierForSingleVariableDeclaration(singleDeclaration));
//		singleDeclaration.setMethodDeclaration(newMethod);//o problema do paramenter esta aqui...
		
		TypeAccess typeAccess = JavaFactory.eINSTANCE.createTypeAccess();
		typeAccess.setType(this.getPrimitiveTypeVoid(model));
		
		
		single.setVariable(variableDeclarationFragment);
		singleRightHand.setVariable(variableDeclarationFragment);
		newMethod.getParameters().add(variableDeclarationFragment);
		newMethod.setReturnType(typeAccess);//colocar quando eu pegar os tipos...
		
		
		classToPutTheMethod.getBodyDeclarations().add(newMethod);//adiciona o method para o containner..
		
		return newMethod;
	}
	
	
	//this method is used to generate source-code based on the Java model as input.
	public void generateNewSourceCode() throws IOException {


		XMIResourceFactoryImpl xmiResourceFactoryImpl = new XMIResourceFactoryImpl() {
			public Resource createResource(URI uri) {
				XMIResource xmiResource = new XMIResourceImpl(uri);
				return xmiResource;
			}
		};

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				"xmi", xmiResourceFactoryImpl);

		
		String projectToPutTheNewSourceCode = ProjectSelectedToModernize.projectSelected.getProject().getLocationURI().toString().split("file:")[1];
		
		String locationOfTheNewJAvaModelWithComment = ProjectSelectedToModernize.projectSelected.getProject().getLocationURI().toString();

		GenerateJavaExtended javaGenerator = new GenerateJavaExtended(URI.createURI(locationOfTheNewJAvaModelWithComment+"/MODELS_PSM_TO_BE/newJavaModel.javaxmi"),
				new File(projectToPutTheNewSourceCode+"/src-gen"), new ArrayList<Object>()); 


		System.out.println("Esta gerando...");		
		javaGenerator.doGenerate(null);
		System.out.println("Gerou");
	}


	public FieldDeclaration getField(ClassDeclaration classDeclaration) {
		
		FieldDeclaration field = null;
		
		EList<BodyDeclaration> types = classDeclaration.getBodyDeclarations();
		
		for (BodyDeclaration bodyDeclaration : types) {
			
			if (bodyDeclaration instanceof FieldDeclaration) {
				
				FieldDeclaration fieldDeclaration = (FieldDeclaration) bodyDeclaration;
				
				
				if (fieldDeclaration.getFragments().get(0).getName().equals("rg")) {
					
					field = fieldDeclaration;
					
					
				}
				
			}
			
		}
		
		return field;
	}

	
	public PrimitiveTypeInt getPrimitiveTypeInt (Model model) {
		
		PrimitiveTypeInt intType = null;
		EList<Type> orpahTypes = model.getOrphanTypes();
		
		for (Type type : orpahTypes) {
			if (type.getName().equals("int")) {
				
				intType = (PrimitiveTypeInt) type;
				
			}
		}
		
		
		return intType;
		
		
	}
	
	public PrimitiveTypeLong getPrimitiveTypeLong (Model model) {
		
		PrimitiveTypeLong longType = null;
		EList<Type> orpahTypes = model.getOrphanTypes();
		
		for (Type type : orpahTypes) {
			if (type.getName().equals("long")) {
				
				longType = (PrimitiveTypeLong) type;
			}
		}
		return longType;
		
		
	}
	
	public PrimitiveTypeFloat getPrimitiveTypeFloat (Model model) {
		
		PrimitiveTypeFloat floatType = null;
		EList<Type> orpahTypes = model.getOrphanTypes();
		
		for (Type type : orpahTypes) {
			if (type.getName().equals("float")) {
				
				floatType = (PrimitiveTypeFloat) type;
			}
		}
		return floatType;
		
		
	}
	
	public PrimitiveTypeDouble getPrimitiveTypeDouble (Model model) {
		
		PrimitiveTypeDouble doubleType = null;
		EList<Type> orpahTypes = model.getOrphanTypes();
		
		for (Type type : orpahTypes) {
			if (type.getName().equals("double")) {
				
				doubleType = (PrimitiveTypeDouble) type;
			}
		}
		return doubleType;
		
		
	}
	
	public PrimitiveTypeBoolean getPrimitiveTypeBoolean (Model model) {
		
		PrimitiveTypeBoolean booleanType = null;
		EList<Type> orpahTypes = model.getOrphanTypes();
		
		for (Type type : orpahTypes) {
			if (type.getName().equals("boolean")) {
				
				booleanType = (PrimitiveTypeBoolean) type;
			}
		}
		return booleanType;
		
		
	}
	
	public PrimitiveTypeVoid getPrimitiveTypeVoid (Model model) {
		
		PrimitiveTypeVoid voidType = null;
		EList<Type> orpahTypes = model.getOrphanTypes();
		
		for (Type type : orpahTypes) {
			if (type.getName().equals("void")) {
				
				voidType = (PrimitiveTypeVoid) type;
			}
		}
		return voidType;
		
	}
	
	public PrimitiveTypeChar getPrimitiveTypeChar (Model model) {
		
		PrimitiveTypeChar charType = null;
		EList<Type> orpahTypes = model.getOrphanTypes();
		
		for (Type type : orpahTypes) {
			if (type.getName().equals("char")) {
				
				charType = (PrimitiveTypeChar) type;
			}
		}
		return charType;
		
	}
	
	public PrimitiveTypeShort getPrimitiveTypeShort (Model model) {
		
		PrimitiveTypeShort shortType = null;
		EList<Type> orpahTypes = model.getOrphanTypes();
		
		for (Type type : orpahTypes) {
			if (type.getName().equals("short")) {
				
				shortType = (PrimitiveTypeShort) type;
			}
		}
		return shortType;
		
	}
	
	public PrimitiveTypeByte getPrimitiveTypeByte (Model model) {
		
		PrimitiveTypeByte byteType = null;
		EList<Type> orpahTypes = model.getOrphanTypes();
		
		for (Type type : orpahTypes) {
			if (type.getName().equals("byte")) {
				
				byteType = (PrimitiveTypeByte) type;
			}
		}
		return byteType;
		
	}
	
	public ClassDeclaration getStringType (Model model){
		
		
		ClassDeclaration classString = null;
		EList<Package> packages = model.getOwnedElements();
		
		for (Package package1 : packages) {
			if (package1.getName().equals("java")) {
				
				EList<Package> packagesUtils = package1.getOwnedPackages();
				
				for (Package package2 : packagesUtils) {
					
					if(package2.getName().equals("lang")) {
						
						EList<AbstractTypeDeclaration> types = package2.getOwnedElements();
						
						for (AbstractTypeDeclaration abstractTypeDeclaration : types) {
							
							if ((abstractTypeDeclaration instanceof ClassDeclaration ) && abstractTypeDeclaration.getName().equals("String")) {
								
								classString = (ClassDeclaration) abstractTypeDeclaration;
								
							}
							
						}
						
						
					}
					
				}
				
			}
		}
		
		
		return classString;
	}
	
	
//	this method is used to save the JavaModel 
	public void save(Model model)  {


		JavaPackage.eINSTANCE.eClass();

		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("website", new XMIResourceFactoryImpl());

		// Obtain a new resource set
		ResourceSet resSet = new ResourceSetImpl();

//		ProjectSelectedToModernize.projectSelected.getProject().getF
		
		String locationOfTheNewJAvaModelWithComment = ProjectSelectedToModernize.projectSelected.getProject().getLocationURI().toString();
		
		Resource resource = resSet.createResource(URI.createURI(locationOfTheNewJAvaModelWithComment+"/MODELS_PSM_TO_BE/newJavaModel.javaxmi"));

		resource.getContents().add(model);

		try {

			resource.save(Collections.EMPTY_MAP);

		} catch (IOException e) {
			// TODO: handle exception
		}

	}
	
//	this method is used to save the JavaModel 
	public void save(Model model, String path)  {


		JavaPackage.eINSTANCE.eClass();

		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("website", new XMIResourceFactoryImpl());

		// Obtain a new resource set
		ResourceSet resSet = new ResourceSetImpl();

		
		
		
		Resource resource = resSet.createResource(URI.createURI(path+"/MODELS_PIM_modificado/JavaModelRefactoring.javaxmi"));

		resource.getContents().add(model);

		try {

			resource.save(Collections.EMPTY_MAP);

		} catch (IOException e) {
			// TODO: handle exception
		}

	}
	
	public ClassDeclaration getClassDeclaration (ClassUnit classUnit, String[] packageComplete, Model model) {
		
		ClassDeclaration classToReturn = null;
		
		org.eclipse.gmt.modisco.java.Package packageJava = null;
		
		EList<AbstractTypeDeclaration> classes = null; 
		
		
		
		EList<org.eclipse.gmt.modisco.java.Package> packages = model.getOwnedElements();
		
		for (int i = 0; i < packageComplete.length; i++) {
			
			for (org.eclipse.gmt.modisco.java.Package package1 : packages) {
				
				if (package1.getName().equals(packageComplete[i])) {
					
					packageJava = package1;
					if (packageJava.getOwnedPackages() != null || packageJava.getOwnedPackages().size() > 0) {
						packages = packageJava.getOwnedPackages();
					}else {
						
						classes = packageJava.getOwnedElements();
						break;
						
					}
					
				}
				
			}
			
		}
		classes = packageJava.getOwnedElements();
		if (classes != null) {
			for (AbstractTypeDeclaration abstractTypeDeclaraction : classes) {
				
				if (abstractTypeDeclaraction instanceof ClassDeclaration) {
					
					ClassDeclaration classToVerifyTheName = (ClassDeclaration) abstractTypeDeclaraction;
					
					if(classToVerifyTheName.getName().equals(classUnit.getName())) {
						
						classToReturn = classToVerifyTheName;
						break;
						
					}
					
					
				}
				
			}
		}
	
		
		
		return classToReturn;
	}
	
	public Model getModelToPersiste(NamedElement namedElement) {
		
		Model model = null;
		
		EObject eObject = namedElement.eContainer();
		
		while (model == null) {
			
			if (eObject instanceof Model) {
				
				model = (Model) eObject;
				
			} else if ( eObject instanceof org.eclipse.gmt.modisco.java.Package)
			{
				
				org.eclipse.gmt.modisco.java.Package packageJAVA = (org.eclipse.gmt.modisco.java.Package) eObject;
				
				eObject = packageJAVA.eContainer();
				
			}
			
		}
		return model;
		
	}

}
