package com.br.parser.refactoring;

import java.util.ArrayList;

public class ClassModelRefactoring {

	private String packageName;
	
	private String className;
	
	private ArrayList<AttributeModelRefactoring> attributes = new ArrayList<AttributeModelRefactoring>();
	
	private ArrayList<MethodModelRefactoring> methods = new ArrayList<MethodModelRefactoring>();

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public ArrayList<AttributeModelRefactoring> getAttributes() {
		return attributes;
	}

	public void setAttributes(ArrayList<AttributeModelRefactoring> attributes) {
		this.attributes = attributes;
	}

	public ArrayList<MethodModelRefactoring> getMethods() {
		return methods;
	}

	public void setMethods(ArrayList<MethodModelRefactoring> methods) {
		this.methods = methods;
	}
	
	
	
}
