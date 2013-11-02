package com.br.tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.ecore.*;
import org.eclipse.gmt.modisco.java.Model;
import org.eclipse.gmt.modisco.java.emf.JavaPackage;
import org.eclipse.gmt.modisco.java.generation.files.GenerateJavaExtended;

public class Generation {

	
	
	public Model loadTeste(){
		
		
		JavaPackage.eINSTANCE.eClass();
		
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
	    Map<String, Object> m = reg.getExtensionToFactoryMap();
	    m.put("website", new XMIResourceFactoryImpl());
		
	    // Obtain a new resource set
	    ResourceSet resSet = new ResourceSetImpl();
	    
	 // Get the resource
	    Resource resource = resSet.getResource(URI
	        .createURI("file:/Users/rafaeldurelli/Documents/workspace/GeneratingJavaFromJavaModel/website/teste.javaxmi"), true);
	    // Get the first model element and cast it to the right type, in my
	    // example everything is hierarchical included in this first node
	    System.out.println(resource.getContents().get(0).toString());
	    return (Model) resource.getContents().get(0);
	}
	
	//this method is used to generate source-code based on the Java model as input.
	public void generate() throws IOException {
		
		
		XMIResourceFactoryImpl xmiResourceFactoryImpl = new XMIResourceFactoryImpl() {
			public Resource createResource(URI uri) {
				XMIResource xmiResource = new XMIResourceImpl(uri);
				return xmiResource;
			}
		};

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				"xmi", xmiResourceFactoryImpl);

		
		//TesteModisco.javaxmi
		
		//Generate_JavaStructures jcva = new Generate_JavaStructures(modelURI, targetFolder, arguments)
		
		
		GenerateJavaExtended javaGenerator = new GenerateJavaExtended(URI.createFileURI("/Users/rafaeldurelli/Documents/workspace/GeneratingJavaFromJavaModel/website/novo/JavaModelRefactoring.javaxmi"),
				new File("/Users/rafaeldurelli/Desktop/src/codigoGerado"), new ArrayList<Object>()); 
		
		//Generate_JavaStructures javaGenerator = new Generate_JavaStructures(
				//URI.createFileURI("/Users/rafaeldurelli/Documents/workspaceMODISCO/TesteGeneralization/website/TesteModisco.javaxmi"),
				//new File("/Users/rafaeldurelli/Desktop/src/codigoGerado"), new ArrayList<Object>());
		
		
				System.out.println("Esta gerando...");		
				javaGenerator.doGenerate(null);
				System.out.println("Gerou");
	}
	
	
	public void save(Model model)  {
		
		
		JavaPackage.eINSTANCE.eClass();
		
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
	    Map<String, Object> m = reg.getExtensionToFactoryMap();
	    m.put("website", new XMIResourceFactoryImpl());
		
	    // Obtain a new resource set
	    ResourceSet resSet = new ResourceSetImpl();
	    
	    Resource resource = resSet.createResource(URI.createURI("file:/Users/rafaeldurelli/Documents/workspace/GeneratingJavaFromJavaModel/website/novo/novo.javaxmi"));
		
	    resource.getContents().add(model);
	    
	    try {
			
	    	resource.save(Collections.EMPTY_MAP);
	    	
		} catch (IOException e) {
			// TODO: handle exception
		}
		
	}
	
}
