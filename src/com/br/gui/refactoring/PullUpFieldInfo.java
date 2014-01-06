package com.br.gui.refactoring;

import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableUnit;
import org.eclipse.gmt.modisco.omg.kdm.core.KDMEntity;

public class PullUpFieldInfo {

	private ClassUnit to;
	
	private ClassUnit from;
	
	private KDMEntity superElement;
	
	private String attributeToExtract;

	private StorableUnit storableUnitTo;
	
	private StorableUnit storableUnitFROM;
	
	public PullUpFieldInfo() {
		
	}
		
	public PullUpFieldInfo(ClassUnit to, ClassUnit from,
			String attributeToExtract) {
		super();
		this.to = to;
		this.from = from;
		this.attributeToExtract = attributeToExtract;
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

	public String getAttributeToExtract() {
		return attributeToExtract;
	}

	public void setAttributeToExtract(String attributeToExtract) {
		this.attributeToExtract = attributeToExtract;
	}
	
	public StorableUnit getStorableUnitTo() {
		return storableUnitTo;
	}

	public void setStorableUnitTo(StorableUnit storableUnitTo) {
		this.storableUnitTo = storableUnitTo;
	}

	public StorableUnit getStorableUnitFROM() {
		return storableUnitFROM;
	}

	public void setStorableUnitFROM(StorableUnit storableUnitFROM) {
		this.storableUnitFROM = storableUnitFROM;
	}

	@Override
	public boolean equals(Object obj) {
		
		
		return  ((obj instanceof PullUpFieldInfo) && 
				(((PullUpFieldInfo)obj).getTo()) == this.getTo() && 
				(((PullUpFieldInfo)obj).getFrom()) == this.getFrom() && 
				(((PullUpFieldInfo)obj).getAttributeToExtract()).equals(this.getAttributeToExtract()));
	}
	
	public KDMEntity getSuperElement() {
		return superElement;
	}
	
	public void setSuperElement(KDMEntity superElement) {
		this.superElement = superElement;
	}
	
}
