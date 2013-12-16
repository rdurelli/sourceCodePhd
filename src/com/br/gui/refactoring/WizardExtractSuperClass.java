package com.br.gui.refactoring;

import java.util.LinkedHashSet;

import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.Model;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.eclipse.jface.wizard.Wizard;

import com.br.util.models.UtilJavaModel;
import com.br.util.models.UtilKDMModel;

public class WizardExtractSuperClass extends Wizard {

	
	private WizardExtractSuperClassPage page1 = null;
	private LinkedHashSet<ExtractSuperClassInfo> extractSuperClassInfo = null;
	private LinkedHashSet<ExtractSuperClassInfoJavaModel> extractSuperClassInfoJavaModel = null;
	
	private UtilKDMModel utilKDMMODEL = new UtilKDMModel();
	private UtilJavaModel utilJavaModel = new UtilJavaModel();
	private Package packageToPutTheNewClass = null;
	private org.eclipse.gmt.modisco.java.Package packageToPutTheNewClassJavaModel = null;
	private Model model = null;
	
	public WizardExtractSuperClass(LinkedHashSet<ExtractSuperClassInfo> extractSuperClassInfo, LinkedHashSet<ExtractSuperClassInfoJavaModel> extractSuperClassInfoJavaModel, Package packageToPutTheNewClass, org.eclipse.gmt.modisco.java.Package packageToPutTheNewClassJavaModel, Model model) {
		setWindowTitle("Extract Superclass");
		this.extractSuperClassInfo = extractSuperClassInfo;
		this.extractSuperClassInfoJavaModel = extractSuperClassInfoJavaModel;
		this.page1 = new WizardExtractSuperClassPage(this.extractSuperClassInfo);
		this.packageToPutTheNewClass = packageToPutTheNewClass;
		this.packageToPutTheNewClassJavaModel = packageToPutTheNewClassJavaModel;
		this.model = model;
	}

	@Override
	public void addPages() {
		this.addPage(this.page1 );
	}

	@Override
	public boolean performFinish() {
		
		String nameOfTheNewClass = this.page1.getText().getText();
		
		ClassUnit superClassExtractedCreated = utilKDMMODEL.createClassUnit(nameOfTheNewClass, this.packageToPutTheNewClass);
		
		ClassDeclaration superClassExtractedCreatedJavaModel = utilJavaModel.createClassDeclaration(nameOfTheNewClass, this.packageToPutTheNewClassJavaModel, this.model);
		
		utilJavaModel.createSuperExtractClass(superClassExtractedCreatedJavaModel, extractSuperClassInfoJavaModel);
		
		utilKDMMODEL.createSuperExtractClass(superClassExtractedCreated, extractSuperClassInfo);
		
		return true;
	}

}
