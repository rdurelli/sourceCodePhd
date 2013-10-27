package com.br.catalogue.refactorings.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gmt.modisco.omg.kdm.code.AbstractCodeElement;
import org.eclipse.gmt.modisco.omg.kdm.code.AbstractCodeRelationship;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeElement;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeItem;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeModel;
import org.eclipse.gmt.modisco.omg.kdm.code.Extends;
import org.eclipse.gmt.modisco.omg.kdm.code.Implements;
import org.eclipse.gmt.modisco.omg.kdm.code.InterfaceUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.MethodUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableUnit;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Attribute;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Segment;

import com.br.models.graphviz.AttributeModel;
import com.br.models.graphviz.Elements;
import com.br.models.graphviz.MethodModel;
import com.br.models.graphviz.PackageModel;

public class PopulateKDMIntoMemory {

	private ArrayList<Elements> classes = new ArrayList<Elements>();
	
	private Segment segment = null;
	
	private CodeModel codeModel = null;
	
	private List<PackageModel> allPackages = new ArrayList<PackageModel>();
	
	
	public PopulateKDMIntoMemory(Segment segment) {
		
		this.segment = segment;
		this.codeModel = (CodeModel) this.segment.getModel().get(0);
		run(this.codeModel.getCodeElement());
		createParentRelationShip(this.codeModel.getCodeElement());
		createAggregationRelationShip(this.codeModel.getCodeElement());
		
	}
	
	
	private void createRelationShipBetweenPackage () {
		
		for (int i = 1; i < allPackages.size(); i++) {
			
			allPackages.get(i-1).setPack(allPackages.get(i));
			
		}
		
	}
	
	
	public List<Elements> run (EList<AbstractCodeElement> codeElement, PackageModel... packageToAdd) {
		
		
		if (codeElement == null) {
			
			return this.classes;
		} 
		else {
			
			
			for (AbstractCodeElement abstractCodeElement : codeElement) {
			
				
				
				if (abstractCodeElement instanceof Package) {
				
					PackageModel packageModel = new PackageModel();
					
					
					Package packageKDM = (Package)abstractCodeElement;
					 
					packageModel.setName(packageKDM.getName());
					
					allPackages.add(packageModel);
					
					System.out.println("Package Model " + packageKDM.getName());
					
//					packageComposto = packageComposto + packageKDM.getName();
					
//					if (packageToAdd.length > 0 && packageToAdd != null) {
//						
//						packageModel.setPack(packageToAdd[0]);
//						
//					}
					
					
					//obtem o nome do packote cria um PackageModel e set seu nome..
					
					run (((Package) abstractCodeElement).getCodeElement());
					
					allPackages.clear();
					
				} else if (abstractCodeElement instanceof ClassUnit) {
					
//					System.out.println("O pacote composto é " + packageComposto);
					
					ClassUnit classObtida = (ClassUnit) abstractCodeElement;
					
					System.out.println(allPackages);
					
					createRelationShipBetweenPackage ();
					
					
					String nameComplete = "";
					for (PackageModel packaName : allPackages) {
						nameComplete += packaName.getName()+".";
					}
					
//					
//					System.out.println(nameComplete);
//					System.out.println(allPackages.get(0));
					
					allPackages.get(0).setCompleteName(nameComplete);
					
					Elements classModel = new Elements();
					classModel.setName(((ClassUnit) abstractCodeElement).getName());
					classModel.setIsClass(true);
					classModel.setPackageModel(allPackages.get(0));
					
					
					this.classes.add(classModel);
					
					EList<CodeItem> codeElements = classObtida.getCodeElement();
				
					
					ArrayList<AttributeModel> attributes = new ArrayList<AttributeModel>();
					ArrayList<MethodModel> methods = new ArrayList<MethodModel>();
					
					for (CodeItem codeItem : codeElements) {
						
						if (codeItem instanceof StorableUnit) {
							
							StorableUnit storable = (StorableUnit) codeItem;
							
							Attribute attributeKDM = storable.getAttribute().get(0);
							
							AttributeModel attribute =  new AttributeModel();
							
							attribute.setName(storable.getName());
							
							if (attributeKDM.getValue().contains("private")) {
								
								attribute.setAccesibility("-");
								
							} else if (attributeKDM.getValue().contains("public")) {
								
								attribute.setAccesibility("+");
								
							} else if (attributeKDM.getValue().contains("protected")) {
								
								attribute.setAccesibility("#");
								
							} else if (attributeKDM.getValue().contains("none")) {
								
								attribute.setAccesibility(" ");
							}
							
							attribute.setType(storable.getType().getName());
							
							attributes.add(attribute);
							
						}
						else if (codeItem instanceof MethodUnit) {
							
							MethodUnit methodRecupered = (MethodUnit) codeItem;
							
							Attribute attributeKDM = methodRecupered.getAttribute().get(0);
							
							MethodModel method = new MethodModel();
							method.setName(methodRecupered.getName()+"()");
							
							if (attributeKDM.getValue().contains("private")) {
								
								method.setAccesibility("-");
								
							} else if (attributeKDM.getValue().contains("public")) {
								
								method.setAccesibility("+");
								
							} else if (attributeKDM.getValue().contains("protected")) {
								
								method.setAccesibility("#");
								
							} else if (attributeKDM.getValue().contains("none")) {
								
								method.setAccesibility(" ");
							}
							
							methods.add(method);
							
						}
					}
					
					classModel.setMethods(methods);
					classModel.setAttributes(attributes);
					
				} else if (abstractCodeElement instanceof InterfaceUnit) {
					
					InterfaceUnit interfaceObtida = (InterfaceUnit) abstractCodeElement;
					
					Elements interfaceModel = new Elements();
					interfaceModel.setName(((InterfaceUnit) abstractCodeElement).getName());
					
					interfaceModel.setIsInterface(true);
					
					createRelationShipBetweenPackage ();
					
					// arrumar aqui.....
					interfaceModel.setPackageModel(allPackages.get(0));
					
//					allPackages.clear();
					
					this.classes.add(interfaceModel);
					
					EList<CodeItem> codeElements = interfaceObtida.getCodeElement();
				
					
					ArrayList<AttributeModel> attributes = new ArrayList<AttributeModel>();
					ArrayList<MethodModel> methods = new ArrayList<MethodModel>();
					
					for (CodeItem codeItem : codeElements) {
						
						if (codeItem instanceof StorableUnit) {
							
							StorableUnit storable = (StorableUnit) codeItem;
							
							Attribute attributeKDM = storable.getAttribute().get(0);
							
							AttributeModel attribute =  new AttributeModel();
							
							attribute.setName(storable.getName());
							
							if (attributeKDM.getValue().contains("private")) {
								
								attribute.setAccesibility("-");
								
							} else if (attributeKDM.getValue().contains("public")) {
								
								attribute.setAccesibility("+");
								
							} else if (attributeKDM.getValue().contains("protected")) {
								
								attribute.setAccesibility("#");
								
							} else if (attributeKDM.getValue().contains("none")) {
								
								attribute.setAccesibility(" ");
							}
							
							attribute.setType(storable.getType().getName());
							
							attributes.add(attribute);
							
						}
						else if (codeItem instanceof MethodUnit) {
							
							MethodUnit methodRecupered = (MethodUnit) codeItem;
							
							Attribute attributeKDM = methodRecupered.getAttribute().get(0);
							
							MethodModel method = new MethodModel();
							method.setName(methodRecupered.getName()+"()");
							
							if (attributeKDM.getValue().contains("private")) {
								
								method.setAccesibility("-");
								
							} else if (attributeKDM.getValue().contains("public")) {
								
								method.setAccesibility("+");
								
							} else if (attributeKDM.getValue().contains("protected")) {
								
								method.setAccesibility("#");
								
							} else if (attributeKDM.getValue().contains("none")) {
								
								method.setAccesibility(" ");
							}
							
							methods.add(method);
							
						}
					}
					
					interfaceModel.setMethods(methods);
					interfaceModel.setAttributes(attributes);
					
				}
				
			}
			
			
		}
		
		return this.classes;
	}
	
	
	private void createParentRelationShip (EList<AbstractCodeElement> codeElement) {
		
		
		Elements classModelFROMRecuperatedInterface = null;
		ArrayList<Elements> interfaceParents = new ArrayList<Elements>();
		
		if (codeElement == null) {
			
			return;
		} else{
			
			for (AbstractCodeElement abstractCodeElement : codeElement) {
				
				if (abstractCodeElement instanceof Package) {
					
					//obtem o nome do packote cria um PackageModel e set seu nome..
				createParentRelationShip(((Package) abstractCodeElement).getCodeElement());
				} else if (abstractCodeElement instanceof ClassUnit) { 
					
					ClassUnit classUnit = (ClassUnit) abstractCodeElement;
					
					EList<AbstractCodeRelationship> relationships = classUnit.getCodeRelation();
					
					for (AbstractCodeRelationship abstractCodeRelationship : relationships) {
						
						if (abstractCodeRelationship instanceof Extends) {
							
							Extends extendsKMD = (Extends) abstractCodeRelationship;
							
							String to = extendsKMD.getTo().getName();
							
							Elements classModelTO = new Elements();
							classModelTO.setName(to);
							
							String from = extendsKMD.getFrom().getName();
							
							Elements classModelFROM = new Elements();
							classModelFROM.setName(from);
							
							Elements classModelFROMRecuperated = (Elements)getClasses().get(getClasses().indexOf(classModelFROM));
							
							Elements classModelTORecuperated = (Elements)getClasses().get(getClasses().indexOf(classModelTO));
							classModelFROMRecuperated.setParent(classModelTORecuperated);
							
//							System.out.println("Contém a classe TO " + getClasses().contains(classModelTO));
//							System.out.println("O index é " + getClasses().indexOf(classModelTO));
//							System.out.println("A class TO é  " + getClasses().get(getClasses().indexOf(classModelTO)).getName());
//							
//							System.out.println("Contém a classe FROM " + getClasses().contains(classModelFROM));
//							System.out.println("O index é " + getClasses().indexOf(classModelFROM));
//							System.out.println("A class FROM é  " + getClasses().get(getClasses().indexOf(classModelFROM)).getName());
//							for (ClassModel classToVerify : getClasses()) {
//								
//								if (classToVerify.getName().equals(to)) {
//									
////									getClasses().i
//								}
//								
//							}
						} else if (abstractCodeRelationship instanceof Implements) {
							
							
							
//							ArrayList<Elements> classesModelsTEster = getClasses();
//							
							Implements implementsKMD = (Implements) abstractCodeRelationship;
//							
							String to = implementsKMD.getTo().getName();
//							
							System.out.println("TO é " + to);
//							
							Elements interfaceModelTO = new Elements();
							interfaceModelTO.setName(to);
							
							String from = implementsKMD.getFrom().getName();
//							
							Elements classModelFROM = new Elements();
							classModelFROM.setName(from);
//							
							System.out.println("FROM é " + from);
//							
							classModelFROMRecuperatedInterface = (Elements)getClasses().get(getClasses().indexOf(classModelFROM));						
							
							Elements interfaceModelTORecuperated = (Elements)getClasses().get(getClasses().indexOf(interfaceModelTO));

							System.out.println(interfaceModelTORecuperated);
							
							interfaceParents.add(interfaceModelTORecuperated);
							
							
							//							classModelFROMRecuperated.setParent(classModelTORecuperated);
//							
//							
//							System.out.println("Contém a classe TO " + getClasses().contains(classModelTO));
//							System.out.println("O index é " + getClasses().indexOf(classModelTO));
//							System.out.println("A class TO é  " + getClasses().get(getClasses().indexOf(classModelTO)).getName());
//							
//							System.out.println("Contém a classe FROM " + getClasses().contains(classModelFROM));
//							System.out.println("O index é " + getClasses().indexOf(classModelFROM));
//							System.out.println("A class FROM é  " + getClasses().get(getClasses().indexOf(classModelFROM)).getName());
							
						}
						
					}

				}
		
			}
			if (classModelFROMRecuperatedInterface != null) {
				
				classModelFROMRecuperatedInterface.setInterfaceParents(interfaceParents);
			
			}
		}
	}
	
	private void createAggregationRelationShip(EList<AbstractCodeElement> codeElement){
		
		if (codeElement == null) {
			
			return;
		} else {
			
			for (AbstractCodeElement abstractCodeElement : codeElement) {
				
				if (abstractCodeElement instanceof Package) {
					
					createAggregationRelationShip(((Package) abstractCodeElement).getCodeElement());
				
				}else if (abstractCodeElement instanceof ClassUnit){
					
					
					
					ClassUnit classUnit = (ClassUnit) abstractCodeElement;
					
					ArrayList<Elements> aggregation = new ArrayList<Elements>();
					
					EList<CodeItem> codeItems = classUnit.getCodeElement();
					
					if (codeItems != null) {
						

						Elements classThatContainTheAggregation = new Elements();
						classThatContainTheAggregation.setName(classUnit.getName());
						
						int indexClassThatContainTheAggregation = getClasses().indexOf(classThatContainTheAggregation);
						
						Elements classToPutTheAggregation = (Elements)getClasses().get(indexClassThatContainTheAggregation);
						
						for (CodeItem codeItem : codeItems) {
							
							if (codeItem instanceof StorableUnit) {
								
								StorableUnit storableUnitToCkeck = (StorableUnit) codeItem;
								
								
								if (storableUnitToCkeck.getType() instanceof ClassUnit) {
									
									String type = storableUnitToCkeck.getType().getName();
									
									if (!type.equals("String")) {
										
										
										Elements classModelAggregation = new Elements();
										classModelAggregation.setName(type);
										
										int indexAggregation = getClasses().indexOf(classModelAggregation);
										
										
										Elements theAggregation = (Elements)getClasses().get(indexAggregation);
										aggregation.add(theAggregation);
										
										System.out.println("O indexe é " + getClasses().indexOf(classModelAggregation));
										System.out.println("A classeUnit é que tem o attribute é " + classUnit.getName());
										System.out.println("O TYPE das classes são : " + storableUnitToCkeck.getType().getName());
										
										System.out.println("Index aggregation " + indexAggregation);
										System.out.println("Index da classe que contem a aggregation " + indexClassThatContainTheAggregation);
									}
									
									
									
								}
								
								
								
							}
							
						}
						classToPutTheAggregation.setAggregation(aggregation);
						
					}
					
				}
				
			}
			
		}
		
	}
	
	public ArrayList<Elements> getClasses() {
		return classes;
	}
	
	
}
