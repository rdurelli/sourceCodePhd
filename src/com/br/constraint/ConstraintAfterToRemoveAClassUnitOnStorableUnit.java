package com.br.constraint;

import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableUnit;

public class ConstraintAfterToRemoveAClassUnitOnStorableUnit extends GenericConstraint {

	private StorableUnit storableUnit;
	
	private ClassUnit classThatOwnsTheIrregularStorableUnit;
	
	private Integer numberOfTheLine;
	
	private String classUnitThatWasRemoved;

	
	public ConstraintAfterToRemoveAClassUnitOnStorableUnit() {
	
	}

	public ConstraintAfterToRemoveAClassUnitOnStorableUnit(StorableUnit storableUnit,
			ClassUnit classThatOwnsTheIrregularStorableUnit,
			Integer numberOfTheLine, String classUnitThatWasRemoved) {
		super();
		this.storableUnit = storableUnit;
		this.classThatOwnsTheIrregularStorableUnit = classThatOwnsTheIrregularStorableUnit;
		this.numberOfTheLine = numberOfTheLine;
		this.classUnitThatWasRemoved = classUnitThatWasRemoved;
	}


	public StorableUnit getStorableUnit() {
		return storableUnit;
	}

	public void setStorableUnit(StorableUnit storableUnit) {
		this.storableUnit = storableUnit;
	}

	public String getClassUnitThatWasRemoved() {
		return classUnitThatWasRemoved;
	}

	public void setClassUnitThatWasRemoved(String classUnitThatWasRemoved) {
		this.classUnitThatWasRemoved = classUnitThatWasRemoved;
	}

	public Integer getNumberOfTheLine() {
		return numberOfTheLine;
	}

	public void setNumberOfTheLine(Integer numberOfTheLine) {
		this.numberOfTheLine = numberOfTheLine;
	}

	public ClassUnit getClassThatOwnsTheIrregularStorableUnit() {
		return classThatOwnsTheIrregularStorableUnit;
	}

	public void setClassThatOwnsTheIrregularStorableUnit(
			ClassUnit classThatOwnsTheIrregularStorableUnit) {
		this.classThatOwnsTheIrregularStorableUnit = classThatOwnsTheIrregularStorableUnit;
	}
	
}
