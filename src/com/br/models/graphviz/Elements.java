package com.br.models.graphviz;

import java.util.ArrayList;

public class Elements {

	private static int number = 0;
	
	private String name;
	
	private int numberClass = 0;
	
	private boolean isClass = false;
	
	private boolean isInterface = false;
	
	private ArrayList<AttributeModel> attributes;
	
	private ArrayList<MethodModel> methods;
	
	private ArrayList<Elements> interfaceParents;
	
	private Elements parent;
	
	private ArrayList<Elements> aggregation;

	private PackageModel packageModel;
	
	public Elements() {
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

	public Elements getParent() {
		return parent;
	}

	public void setParent(Elements parent) {
		this.parent = parent;
	}

	public ArrayList<Elements> getAggregation() {
		return aggregation;
	}

	public void setAggregation(ArrayList<Elements> aggregation) {
		this.aggregation = aggregation;
	}
	
	public int getNumberClass() {
		return numberClass;
	}
	
	public void setNumberClass(int numberClass) {
		this.numberClass = numberClass;
	}
	
	public ArrayList<MethodModel> getMethods() {
		return methods;
	}
	
	public void setMethods(ArrayList<MethodModel> methods) {
		this.methods = methods;
	}
	
	public PackageModel getPackageModel() {
		return packageModel;
	}
	
	public void setPackageModel(PackageModel packageModel) {
		this.packageModel = packageModel;
	}
	
	public boolean isClass() {
		return isClass;
	}

	public void setIsClass(boolean isClass) {
		this.isClass = isClass;
	}

	public boolean isInterface() {
		return isInterface;
	}

	public void setIsInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}
	
	public ArrayList<Elements> getInterfaceParents() {
		return interfaceParents;
	}
	
	public void setInterfaceParents(ArrayList<Elements> interfaceParents) {
		this.interfaceParents = interfaceParents;
	}
	
	
	@Override
	public boolean equals(Object other) {
		
		boolean result;
	    if((other == null) || (getClass() != other.getClass())){
	        result = false;
	    } // end if
	    else{
	    	Elements otherClassModel = (Elements)other;
	        result = this.getName().equals(otherClassModel.getName());
	    } // end else

	    return result;
	}
	
}

