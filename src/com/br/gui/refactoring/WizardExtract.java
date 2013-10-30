package com.br.gui.refactoring;

import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.jface.wizard.Wizard;

public class WizardExtract extends Wizard {

	private WizardExtractClass page1 = null; 
	
	private ClassUnit classUnitToExtract;
	
	
	public WizardExtract(ClassUnit classUnitToExtract) {
		setWindowTitle("Extract ClassUnit");
		this.classUnitToExtract = classUnitToExtract;
	}

	@Override
	public void addPages() {
		addPage(new WizardExtractClass(this.classUnitToExtract));
	}

	@Override
	public boolean performFinish() {
		return true;
	}

}
