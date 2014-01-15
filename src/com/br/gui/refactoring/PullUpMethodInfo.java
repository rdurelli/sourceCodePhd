package com.br.gui.refactoring;

import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.MethodUnit;
import org.eclipse.gmt.modisco.omg.kdm.core.KDMEntity;

public class PullUpMethodInfo {

	private ClassUnit to;
	
	private ClassUnit from;
	
	private KDMEntity superElement;
	
	private String methodToExtract;

	private MethodUnit methodUniTo;
	
	private MethodUnit methodUniFROM;
	
	public PullUpMethodInfo() {
		
	}
		
	public PullUpMethodInfo(ClassUnit to, ClassUnit from,
			String attributeToExtract) {
		super();
		this.to = to;
		this.from = from;
		this.methodToExtract = attributeToExtract;
	}



	public ClassUnit getTo() {
		return to;
	}

	public void setTo(ClassUnit to) {
		this.to = to;
	}

	public ClassUnit getFrom() {
		return from;
	}

	public void setFrom(ClassUnit from) {
		this.from = from;
	}

	public String getMethodToExtract() {
		return methodToExtract;
	}

	public void setMethodToExtract(String methodToExtract) {
		this.methodToExtract = methodToExtract;
	}
	
	public MethodUnit getMethodUnitTo() {
		return methodUniTo;
	}

	public void setMethodUnitTo(MethodUnit methodUnitTo) {
		this.methodUniTo = methodUnitTo;
	}

	public MethodUnit getMethodUnitFROM() {
		return methodUniFROM;
	}

	public void setMethodUnitFROM(MethodUnit methodUnitFROM) {
		this.methodUniFROM = methodUnitFROM;
	}

	@Override
	public boolean equals(Object obj) {
		
		
		return  ((obj instanceof PullUpMethodInfo) && 
				(((PullUpMethodInfo)obj).getTo()) == this.getTo() && 
				(((PullUpMethodInfo)obj).getFrom()) == this.getFrom() && 
				(((PullUpMethodInfo)obj).getMethodToExtract()).equals(this.getMethodToExtract()));
	}
	
	public KDMEntity getSuperElement() {
		return superElement;
	}
	
	public void setSuperElement(KDMEntity superElement) {
		this.superElement = superElement;
	}
	
}
