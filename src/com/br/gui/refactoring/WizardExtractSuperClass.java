package com.br.gui.refactoring;

import java.util.LinkedHashSet;

import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.eclipse.jface.wizard.Wizard;

import com.br.util.models.UtilKDMModel;

public class WizardExtractSuperClass extends Wizard {

	
	private WizardExtractSuperClassPage page1 = null;
	private LinkedHashSet<ExtractSuperClassInfo> extractSuperClassInfo = null;
	
	private UtilKDMModel utilKDMMODEL = new UtilKDMModel();
	private Package packageToPutTheNewClass = null;
	
	public WizardExtractSuperClass(LinkedHashSet<ExtractSuperClassInfo> extractSuperClassInfo, Package packageToPutTheNewClass) {
		setWindowTitle("Extract Superclass");
		this.extractSuperClassInfo = extractSuperClassInfo;
		this.page1 = new WizardExtractSuperClassPage(this.extractSuperClassInfo);
		this.packageToPutTheNewClass = packageToPutTheNewClass;
	}

	@Override
	public void addPages() {
		this.addPage(this.page1 );
	}

	@Override
	public boolean performFinish() {
		
		String nameOfTheNewClass = this.page1.getText().getText();
		
		ClassUnit superClassExtractedCreated = utilKDMMODEL.createClassUnit(nameOfTheNewClass, this.packageToPutTheNewClass);
		
		
		utilKDMMODEL.createSuperExtractClass(superClassExtractedCreated, extractSuperClassInfo);
		
		return true;
	}

}
