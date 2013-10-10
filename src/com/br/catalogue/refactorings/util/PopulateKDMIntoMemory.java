package com.br.catalogue.refactorings.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gmt.modisco.omg.kdm.code.AbstractCodeElement;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeElement;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeItem;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeModel;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableUnit;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Attribute;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Segment;

import com.br.models.graphviz.AttributeModel;
import com.br.models.graphviz.ClassModel;

public class PopulateKDMIntoMemory {

	private ArrayList<ClassModel> classes = new ArrayList<ClassModel>();
	
	private Segment segment = null;
	
	private CodeModel codeModel = null;
	
	
	public PopulateKDMIntoMemory(Segment segment) {
		
		this.segment = segment;
		this.codeModel = (CodeModel) this.segment.getModel().get(0);
		run(this.codeModel.getCodeElement());
		
		
	}
	
	public List<ClassModel> run (EList<AbstractCodeElement> codeElement, ClassUnit... classUnit) {
		
		
		if (codeElement == null) {
			
			return this.classes;
		} 
		else {
			
			for (AbstractCodeElement abstractCodeElement : codeElement) {
				
				if (abstractCodeElement instanceof Package) {
					
					
					//obtem o nome do packote cria um PackageModel e set seu nome..
					
					run (((Package) abstractCodeElement).getCodeElement());
					
				} else if (abstractCodeElement instanceof ClassUnit) {
					
					ClassUnit classObtida = (ClassUnit) abstractCodeElement;
					
					ClassModel classModel = new ClassModel();
					classModel.setName(((ClassUnit) abstractCodeElement).getName());
					
					this.classes.add(classModel);
					
					EList<CodeItem> codeElements = classObtida.getCodeElement();
				
					
					ArrayList<AttributeModel> attributes = new ArrayList<AttributeModel>();
					
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
					}
					classModel.setAttributes(attributes);
					
				}
				
			}
			
			
		}
		
		return this.classes;
	}
	
	public ArrayList<ClassModel> getClasses() {
		return classes;
	}
	
	
}
