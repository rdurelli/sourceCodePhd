package com.br.gui.refactoring;

import java.util.LinkedHashSet;

import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.Model;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.eclipse.jface.wizard.Wizard;

import com.br.util.models.UtilJavaModel;
import com.br.util.models.UtilKDMModel;

public class WizardPullUpField extends Wizard {

	
	private WizardPullUpFieldPage page1 = null;
	private LinkedHashSet<PullUpFieldInfo> pullUpFieldInfo = null;
	private LinkedHashSet<PullUpFieldInfoJavaModel> extractSuperClassInfoJavaModel = null;
	
	private UtilKDMModel utilKDMMODEL = new UtilKDMModel();
	private UtilJavaModel utilJavaModel = new UtilJavaModel();
	private Package packageToPutTheNewClass = null;
	private org.eclipse.gmt.modisco.java.Package packageToPutTheNewClassJavaModel = null;
	private Model model = null;
	private String URIProject = null;
	
	public WizardPullUpField(LinkedHashSet<PullUpFieldInfo> extractSuperClassInfo, LinkedHashSet<PullUpFieldInfoJavaModel> extractSuperClassInfoJavaModel, Package packageToPutTheNewClass, org.eclipse.gmt.modisco.java.Package packageToPutTheNewClassJavaModel, Model model, String URIProject) {
		setWindowTitle("Extract Superclass");
		this.pullUpFieldInfo = extractSuperClassInfo;
		this.extractSuperClassInfoJavaModel = extractSuperClassInfoJavaModel;
		this.page1 = new WizardPullUpFieldPage(this.pullUpFieldInfo);
		this.packageToPutTheNewClass = packageToPutTheNewClass;
		this.packageToPutTheNewClassJavaModel = packageToPutTheNewClassJavaModel;
		this.model = model;
		this.URIProject = URIProject;
	}

	@Override
	public void addPages() {
		this.addPage(this.page1 );
	}

	@Override
	public boolean performFinish() {
		
		utilKDMMODEL.actionPullUpField(pullUpFieldInfo);
		
//		String nameOfTheNewClass = this.page1.getText().getText();
//		
//		ClassUnit superClassExtractedCreated = utilKDMMODEL.createClassUnit(nameOfTheNewClass, this.packageToPutTheNewClass);
//		
//		ClassDeclaration superClassExtractedCreatedJavaModel = utilJavaModel.createClassDeclaration(nameOfTheNewClass, this.packageToPutTheNewClassJavaModel, this.model);
//		
//		utilJavaModel.createSuperExtractClass(superClassExtractedCreatedJavaModel, extractSuperClassInfoJavaModel, model, URIProject);
//		
//		utilKDMMODEL.createSuperExtractClass(superClassExtractedCreated, extractSuperClassInfo);
		
		return true;
	}

}
