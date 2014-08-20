package com.br.constraint;

import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.MethodUnit;

public class ConstraintAfterToRemoveAClassUnitOnMethodUnit extends GenericConstraint {

	private MethodUnit methodUnit;
	
	private Boolean isConstructor;
	
	private ClassUnit classThatOwnsTheIrregularMethodUnit;
	
	private Integer numberOfTheLine;
	
	private String classUnitThatWasRemoved;

	public MethodUnit getMethodUnit() {
		return methodUnit;
	}

	public void setMethodUnit(MethodUnit methodUnit) {
		this.methodUnit = methodUnit;
	}

	public Boolean getIsConstructor() {
		return isConstructor;
	}

	public void setIsConstructor(Boolean isConstructor) {
		this.isConstructor = isConstructor;
	}

	public ClassUnit getClassThatOwnsTheIrregularMethodUnit() {
		return classThatOwnsTheIrregularMethodUnit;
	}

	public void setClassThatOwnsTheIrregularMethodUnit(
			ClassUnit classThatOwnsTheIrregularMethodUnit) {
		this.classThatOwnsTheIrregularMethodUnit = classThatOwnsTheIrregularMethodUnit;
	}

	public Integer getNumberOfTheLine() {
		return numberOfTheLine;
	}

	public void setNumberOfTheLine(Integer numberOfTheLine) {
		this.numberOfTheLine = numberOfTheLine;
	}

	public String getClassUnitThatWasRemoved() {
		return classUnitThatWasRemoved;
	}

	public void setClassUnitThatWasRemoved(String classUnitThatWasRemoved) {
		this.classUnitThatWasRemoved = classUnitThatWasRemoved;
	}
	
	
	


}
