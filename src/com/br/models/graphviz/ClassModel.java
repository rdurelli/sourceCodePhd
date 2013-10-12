package com.br.models.graphviz;

import java.util.ArrayList;


public class ClassModel extends Elements{

	
	private ClassModel parent;
	
	private ArrayList<InterfaceModel> interfaceParents;
	
	public ClassModel getParent() {
		return parent;
	}

	public void setParent(ClassModel parent) {
		this.parent = parent;
	}
	
	public ArrayList<InterfaceModel> getInterfaceParents() {
		return interfaceParents;
	}
	
	public void setInterfaceParents(ArrayList<InterfaceModel> interfaceParents) {
		this.interfaceParents = interfaceParents;
	}
	
	@Override
	public boolean equals(Object other) {
		
		boolean result;
	    if((other == null) || (getClass() != other.getClass())){
	        result = false;
	    } // end if
	    else{
	        ClassModel otherClassModel = (ClassModel)other;
	        result = this.getName().equals(otherClassModel.getName());
	    } // end else

	    return result;
	}
	
}
