package com.br.tests;

import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeFactory;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.eclipse.gmt.modisco.omg.kdm.core.KDMEntity;
import org.eclipse.gmt.modisco.omg.kdm.structure.Layer;
import org.eclipse.gmt.modisco.omg.kdm.structure.StructureFactory;
import org.eclipse.gmt.modisco.omg.kdm.structure.StructureModel;

public class TesteStructurePackage {

	
	
	public static void main(String[] args) {
	
		StructureModel structureModel = StructureFactory.eINSTANCE.createStructureModel();
		structureModel.setName("TEste");
		
		ArrayList<Layer> allLayersOfTheSystem = new ArrayList<Layer>();
		
		Package packageVIEW = CodeFactory.eINSTANCE.createPackage();
		packageVIEW.setName("com.br.view");
		
		Package packageController = CodeFactory.eINSTANCE.createPackage();
		packageController.setName("com.br.controller");
		
		Package packageModel = CodeFactory.eINSTANCE.createPackage();
		packageModel.setName("com.br.model");
		
		ArrayList<ClassUnit> classToRepresentaVIEW = new ArrayList<ClassUnit>();
		ArrayList<ClassUnit> classToRepresentaController = new ArrayList<ClassUnit>();
		ArrayList<ClassUnit> classToRepresentaModel = new ArrayList<ClassUnit>();
		
		ClassUnit myClassTest = createClass("Graphic");
		ClassUnit myClassTest2 = createClass("GraphicAction");
		ClassUnit myClassTest3 = createClass("TableGUI");
		
		ClassUnit myClassTest4 = createClass("Controller1");
		ClassUnit myClassTest5 = createClass("Controller2");
		ClassUnit myClassTest6 = createClass("Controller3");
		
		ClassUnit myClassTest7 = createClass("Model1");
		ClassUnit myClassTest8 = createClass("Model2");
		ClassUnit myClassTest9 = createClass("Model3");
		
		classToRepresentaVIEW.add(myClassTest);
		classToRepresentaVIEW.add(myClassTest2);
		classToRepresentaVIEW.add(myClassTest3);
		
		setRelatationshipBetweenPackagesAndClasses(packageVIEW, classToRepresentaVIEW);
		
		classToRepresentaController.add(myClassTest4);
		classToRepresentaController.add(myClassTest5);
		classToRepresentaController.add(myClassTest6);
		
		setRelatationshipBetweenPackagesAndClasses(packageController, classToRepresentaController);
		
		classToRepresentaModel.add(myClassTest7);
		classToRepresentaModel.add(myClassTest8);
		classToRepresentaModel.add(myClassTest9);
		
		setRelatationshipBetweenPackagesAndClasses(packageModel, classToRepresentaModel);
		
		Layer layerView = createLayer("view");
		
		Layer layerController = createLayer("controller");
		
		Layer layerModel = createLayer("Model");
		
		allLayersOfTheSystem.add(layerView);
		allLayersOfTheSystem.add(layerController);
		allLayersOfTheSystem.add(layerModel);
		
		
		setRelationshipBetweenLayersAndClasses(layerView, classToRepresentaVIEW);
		setRelationshipBetweenLayersAndClasses(layerController, classToRepresentaController);
		setRelationshipBetweenLayersAndClasses(layerModel, classToRepresentaModel);
		
		
		addLayerToTheStructureModel(allLayersOfTheSystem, structureModel);
		
		
		EList<KDMEntity> classOFTheLayer = layerView.getImplementation();
		System.out.println("________________________________________________________________");
		System.out.println("Layer "+ layerView.getName());
		System.out.println("Quantidade de classes no Layer " + layerView.getImplementation().size());
		System.out.println("Classes of it:");
		
		for (KDMEntity kdmEntity : classOFTheLayer) {
			ClassUnit classUnit = (ClassUnit) kdmEntity;
			
			System.out.println(classUnit.getName());
		}
		System.out.println("________________________________________________________________");
		removeAClassOfALayer(layerView, myClassTest3);
		
		System.out.println("________________________________________________________________");
		System.out.println("Layer "+ layerView.getName());
		System.out.println("Quantidade de classes no Layer " + layerView.getImplementation().size());
		System.out.println("Classes of it:");
		System.out.println("________________________________________________________________");
		for (KDMEntity kdmEntity : classOFTheLayer) {
			ClassUnit classUnit = (ClassUnit) kdmEntity;
			
			System.out.println(classUnit.getName());
		}
		
		System.out.println("________________________________________________________________");
		showPackageDetails(packageVIEW);
		System.out.println("________________________________________________________________");
		
	}
	
	
	public static ClassUnit createClass(String name) {
		
		ClassUnit myClassTest = CodeFactory.eINSTANCE.createClassUnit();
		myClassTest.setName(name);
	
		return myClassTest;
		
	}
	
	public static Layer createLayer (String name) {
		
		Layer layer = StructureFactory.eINSTANCE.createLayer();
		layer.setName(name);
		
		return layer;
	}
	
	
	public static void setRelatationshipBetweenPackagesAndClasses (Package kdmPackage, ArrayList<ClassUnit> classes) {
		
		for (ClassUnit classUnit : classes) {
			kdmPackage.getOwnedElement().add(classUnit);

		}
		
	}
	
	public static void setRelationshipBetweenLayersAndClasses(Layer layer, ArrayList<ClassUnit> classes) {
		
		for (ClassUnit classUnit : classes) {
			layer.getImplementation().add(classUnit);
			
		}
		
	}
	
	public static void addLayerToTheStructureModel (ArrayList<Layer> layers, StructureModel structureModel) {
		
		for (Layer layer : layers) {
			structureModel.getStructureElement().add(layer);
		}
		
	}
	
	public static void removeAClassOfALayer (Layer layer, ClassUnit classToRemove) {
		
		if (classToRemove != null && layer != null) {
			
			layer.getImplementation().remove(classToRemove);
			
		}
		
	}
	
	public static void showPackageDetails (Package packagesKDM) {
		
		EList<KDMEntity> classes = packagesKDM.getOwnedElement();
		
		for (KDMEntity kdmEntity : classes) {
			
			ClassUnit classUnit = (ClassUnit) kdmEntity; 
			System.out.println(classUnit.getName());
		}
		
	}
	
	
}
