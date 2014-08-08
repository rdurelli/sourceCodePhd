package com.br.constraint;

import org.eclipse.gmt.modisco.omg.kdm.code.MethodUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableUnit;

public class ConstraintClassUnit {

	private StorableUnit storableUnit;
	
	private MethodUnit methodUnit;
	
	private String classUnitThatWasRemoved;

	
	public ConstraintClassUnit() {
	
	}
	
	public ConstraintClassUnit(StorableUnit storableUnit,
			MethodUnit methodUnit, String classUnitThatWasRemoved) {
		this.storableUnit = storableUnit;
		this.methodUnit = methodUnit;
		this.classUnitThatWasRemoved = classUnitThatWasRemoved;
	}

	public StorableUnit getStorableUnit() {
		return storableUnit;
	}

	public void setStorableUnit(StorableUnit storableUnit) {
		this.storableUnit = storableUnit;
	}

	public MethodUnit getMethodUnit() {
		return methodUnit;
	}

	public void setMethodUnit(MethodUnit methodUnit) {
		this.methodUnit = methodUnit;
	}

	public String getClassUnitThatWasRemoved() {
		return classUnitThatWasRemoved;
	}

	public void setClassUnitThatWasRemoved(String classUnitThatWasRemoved) {
		this.classUnitThatWasRemoved = classUnitThatWasRemoved;
	}
	
}
