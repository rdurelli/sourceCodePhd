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
import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.CompilationUnit;
import org.eclipse.gmt.modisco.java.InheritanceKind;
import org.eclipse.gmt.modisco.java.Model;
import org.eclipse.gmt.modisco.java.Modifier;
import org.eclipse.gmt.modisco.java.NamedElement;
import org.eclipse.gmt.modisco.java.Package;
import org.eclipse.gmt.modisco.java.VisibilityKind;
import org.eclipse.gmt.modisco.java.emf.JavaFactory;
import org.eclipse.gmt.modisco.java.emf.JavaPackage;
import org.eclipse.gmt.modisco.java.generation.files.GenerateJavaExtended;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeFactory;

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
