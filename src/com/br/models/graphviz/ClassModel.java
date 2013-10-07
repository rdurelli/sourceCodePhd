package com.br.models.graphviz;

import java.util.ArrayList;

public class ClassModel {

	private static int number = 0;
	
	private String name;
	
	private int numberClass = 0;
	
	private ArrayList<AttributeModel> attributes;
	
	private ArrayList<MethodModel> methods;
	
	private ClassModel parent;
	
	private ArrayList<ClassModel> aggregation;

	public ClassModel() {
		this.numberClass = number++;
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<AttributeModel> getAttributes() {
		return attributes;
	}

	public void setAttributes(ArrayList<AttributeModel> attributes) {
		this.attributes = attributes;
	}

	public ClassModel getParent() {
		return parent;
	}

	public void setParent(ClassModel parent) {
		this.parent = parent;
	}

	public ArrayList<ClassModel> getAggregation() {
		return aggregation;
	}

	public void setAggregation(ArrayList<ClassModel> aggregation) {
		this.aggregation = aggregation;
	}
	
	public int getNumberClass() {
		return numberClass;
	}
	
	public ArrayList<MethodModel> getMethods() {
		return methods;
	}
	
	public void setMethods(ArrayList<MethodModel> methods) {
		this.methods = methods;
	}
	
}
