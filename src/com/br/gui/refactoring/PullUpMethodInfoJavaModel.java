package com.br.gui.refactoring;

import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.MethodDeclaration;

public class PullUpMethodInfoJavaModel {

	private ClassDeclaration to;
	
	private ClassDeclaration from;
	
	private ClassDeclaration superElement;
	
	private String methodToExtract;

	private MethodDeclaration methodDeclarationTo;
	
	private MethodDeclaration methodDeclarationFROM;
	
	public PullUpMethodInfoJavaModel() {
		
	}
		
	public PullUpMethodInfoJavaModel(ClassDeclaration to, ClassDeclaration from,
			String attributeToExtract) {
		super();
		this.to = to;
		this.from = from;
		this.methodToExtract = attributeToExtract;
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

	public String getMethodToExtract() {
		return methodToExtract;
	}

	public void setMethodToExtract(String methodToExtract) {
		this.methodToExtract = methodToExtract;
	}
	
	public MethodDeclaration getMethodDeclarationTo() {
		return methodDeclarationTo;
	}

	public void setMethodDeclarationTo(MethodDeclaration methodUnitTo) {
		this.methodDeclarationTo = methodUnitTo;
	}

	public MethodDeclaration getMethodDeclarationFROM() {
		return methodDeclarationFROM;
	}

	public void setMethodDeclarationFROM(MethodDeclaration methodUnitFROM) {
		this.methodDeclarationFROM = methodUnitFROM;
	}

	@Override
	public boolean equals(Object obj) {
		
		
		return  ((obj instanceof PullUpMethodInfoJavaModel) && 
				(((PullUpMethodInfoJavaModel)obj).getTo()) == this.getTo() && 
				(((PullUpMethodInfoJavaModel)obj).getFrom()) == this.getFrom() && 
				(((PullUpMethodInfoJavaModel)obj).getMethodToExtract()).equals(this.getMethodToExtract()));
	}
	
	public ClassDeclaration getSuperElement() {
		return superElement;
	}
	
	public void setSuperElement(ClassDeclaration superElement) {
		this.superElement = superElement;
	}
	
}
