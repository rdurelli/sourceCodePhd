package com.br.gui.refactoring;

import java.util.List;

import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableUnit;

public class PullDownFieldInfo {

	private List<ClassUnit> classesToPullDown;

	private StorableUnit storableUnitToPullDown;
	
	public PullDownFieldInfo() {
		
	}
	
	public PullDownFieldInfo(List<ClassUnit> classesToPullDown,
			StorableUnit storableUnitToPullDown) {
		super();
		this.classesToPullDown = classesToPullDown;
		this.storableUnitToPullDown = storableUnitToPullDown;
	}

	public List<ClassUnit> getClassesToPullDown() {
		return classesToPullDown;
	}

	public void setClassesToPullDown(List<ClassUnit> classesToPullDown) {
		this.classesToPullDown = classesToPullDown;
	}

	public StorableUnit getStorableUnitToPullDown() {
		return storableUnitToPullDown;
	}

	public void setStorableUnitToPullDown(StorableUnit storableUnitToPullDown) {
		this.storableUnitToPullDown = storableUnitToPullDown;
	}
	
	
	
}
