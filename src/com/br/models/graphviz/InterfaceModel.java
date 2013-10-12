package com.br.models.graphviz;

import java.util.ArrayList;

public class InterfaceModel extends Elements{

	
	
	@Override
	public boolean equals(Object other) {
		
		boolean result;
	    if((other == null) || (getClass() != other.getClass())){
	        result = false;
	    } // end if
	    else{
	    	InterfaceModel interfaceModel = (InterfaceModel)other;
	        result = this.getName().equals(interfaceModel.getName());
	    } // end else

	    return result;
	}
}
