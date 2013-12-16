package com.br.gui.refactoring;

import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.FieldDeclaration;

public class ExtractSuperClassInfoJavaModel {
	
	private ClassDeclaration to;
	
	private ClassDeclaration from;
	
	private String attributeToExtract;

	private FieldDeclaration fieldDeclarationtTo;
	
	private FieldDeclaration fieldDeclarationtFROM;
	
	public ExtractSuperClassInfoJavaModel() {
		
	}
		
	public ExtractSuperClassInfoJavaModel(ClassDeclaration to, ClassDeclaration from,
			String attributeToExtract) {
		super();
		this.to = to;
		this.from = from;
		this.attributeToExtract = attributeToExtract;
	}



	public ClassDeclaration getTo() {
		return to;
	}

	public void setTo(ClassDeclaration to) {
		this.to = to;
	}

	public ClassDeclaration getFrom() {
		return from;
	}

	public void setFrom(ClassDeclaration from) {
		this.from = from;
	}

	public String getAttributeToExtract() {
		return attributeToExtract;
	}

	public void setAttributeToExtract(String attributeToExtract) {
		this.attributeToExtract = attributeToExtract;
	}
	
	public FieldDeclaration getStorableUnitTo() {
		return fieldDeclarationtTo;
	}

	public void setStorableUnitTo(FieldDeclaration storableUnitTo) {
		this.fieldDeclarationtTo = storableUnitTo;
	}

	public FieldDeclaration getStorableUnitFROM() {
		return fieldDeclarationtFROM;
	}

	public void setStorableUnitFROM(FieldDeclaration storableUnitFROM) {
		this.fieldDeclarationtFROM = storableUnitFROM;
	}

	@Override
	public boolean equals(Object obj) {
		
		
		return  ((obj instanceof ExtractSuperClassInfo) && 
				(((ExtractSuperClassInfo)obj).getTo()) == this.getTo() && 
				(((ExtractSuperClassInfo)obj).getFrom()) == this.getFrom() && 
				(((ExtractSuperClassInfo)obj).getAttributeToExtract()).equals(this.getAttributeToExtract()));
	}

}
