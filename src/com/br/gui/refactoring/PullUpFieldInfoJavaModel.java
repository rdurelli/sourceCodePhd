package com.br.gui.refactoring;

import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.FieldDeclaration;


public class PullUpFieldInfoJavaModel {

	private ClassDeclaration to;
	
	private ClassDeclaration from;
	
	private ClassDeclaration superElement;
	
	private String attributeToExtract;

	private FieldDeclaration fieldDeclarationTo;
	
	private FieldDeclaration fieldDeclarationFROM;
	
	public PullUpFieldInfoJavaModel() {
		
	}
		
	public PullUpFieldInfoJavaModel(ClassDeclaration to, ClassDeclaration from,
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
	
	public FieldDeclaration getFieldDeclarationTo() {
		return fieldDeclarationTo;
	}

	public void setFieldDeclarationTo(FieldDeclaration storableUnitTo) {
		this.fieldDeclarationTo = storableUnitTo;
	}

	public FieldDeclaration getFieldDeclarationFROM() {
		return fieldDeclarationFROM;
	}

	public void setFieldDeclarationFROM(FieldDeclaration storableUnitFROM) {
		this.fieldDeclarationFROM = storableUnitFROM;
	}

	@Override
	public boolean equals(Object obj) {
		
		
		return  ((obj instanceof PullUpFieldInfoJavaModel) && 
				(((PullUpFieldInfoJavaModel)obj).getTo()) == this.getTo() && 
				(((PullUpFieldInfoJavaModel)obj).getFrom()) == this.getFrom() && 
				(((PullUpFieldInfoJavaModel)obj).getAttributeToExtract()).equals(this.getAttributeToExtract()));
	}
	
	public ClassDeclaration getSuperElement() {
		return superElement;
	}
	
	public void setSuperElement(ClassDeclaration superElement) {
		this.superElement = superElement;
	}
	
}
