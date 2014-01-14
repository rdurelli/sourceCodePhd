package com.br.gui.refactoring;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.Model;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.MethodUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableUnit;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.TableItem;

import com.br.actions.PullDownFieldClass;
import com.br.util.models.UtilJavaModel;
import com.br.util.models.UtilKDMModel;

public class WizardPushDownMethod extends Wizard {

	
	private WizardPushDownMethodPage page1 = null;
	private ClassUnit pullDownMethodInfo = null;
	private ArrayList<ClassUnit> inheritance = null;
	private LinkedHashSet<ExtractSuperClassInfoJavaModel> extractSuperClassInfoJavaModel = null;//arrrumar aqui
	
	private UtilKDMModel utilKDMMODEL = new UtilKDMModel();
	private UtilJavaModel utilJavaModel = new UtilJavaModel();
	private Package packageToPutTheNewClass = null;
	private org.eclipse.gmt.modisco.java.Package packageToPutTheNewClassJavaModel = null;
	private Model model = null;
	private String URIProject = null;
	
//	public WizardPushDownField(ClassUnit pullDownFieldClass, ArrayList<ClassUnit> inheritance, LinkedHashSet<ExtractSuperClassInfoJavaModel> extractSuperClassInfoJavaModel, Package packageToPutTheNewClass, org.eclipse.gmt.modisco.java.Package packageToPutTheNewClassJavaModel, Model model, String URIProject) {
//		setWindowTitle("Extract Superclass");
//		this.pullDownFieldInfo = pullDownFieldClass;
//		this.inheritance = inheritance;
//		this.extractSuperClassInfoJavaModel = extractSuperClassInfoJavaModel;
//		this.page1 = new WizardPushDownFieldPage(this.pullDownFieldInfo, inheritance);
//		this.packageToPutTheNewClass = packageToPutTheNewClass;
//		this.packageToPutTheNewClassJavaModel = packageToPutTheNewClassJavaModel;
//		this.model = model;
//		this.URIProject = URIProject;
//	}
	
	public WizardPushDownMethod(ClassUnit pullDownMethodClass, ArrayList<ClassUnit> inheritance) {
		setWindowTitle("Extract Superclass");
		this.pullDownMethodInfo = pullDownMethodClass;
		this.inheritance = inheritance;
//		this.extractSuperClassInfoJavaModel = extractSuperClassInfoJavaModel;
		this.page1 = new WizardPushDownMethodPage(this.pullDownMethodInfo, inheritance);
//		this.packageToPutTheNewClass = packageToPutTheNewClass;
//		this.packageToPutTheNewClassJavaModel = packageToPutTheNewClassJavaModel;
//		this.model = model;
//		this.URIProject = URIProject;
	}
	

	@Override
	public void addPages() {
		this.addPage(this.page1 );
	}

	@Override
	public boolean performFinish() {
		
		List<MethodUnit> selectedMethodUnit = new ArrayList<MethodUnit>();
		
		TableItem[] selectedItem = this.page1.getTable().getSelection();
		
		for (int i = 0; i < selectedItem.length; i++) {
			
			String nameOfMethodUnitSelected[] = selectedItem[i].getText(1).split("\\(");
			
			MethodUnit methodUniIdentified = utilKDMMODEL.getMethodsUnitByName(pullDownMethodInfo, nameOfMethodUnitSelected[0]);
			
			
			if (methodUniIdentified != null) {
				
				selectedMethodUnit.add(methodUniIdentified);
				
			}
			
			
			System.out.println(selectedItem[i].getText(1));
		}
		
		if (selectedMethodUnit.size() != 0) {
			
			
			utilKDMMODEL.actionPullDownMethod(pullDownMethodInfo, inheritance, selectedMethodUnit);
			
		} else {
			
			MessageDialog.openError(null, "Error", "Please be sure you have selected at least a MethodUnit to realize the Pull Down Method.");
			
		}
		
//		utilKDMMODEL.actionPullUpField(pullDownFieldInfo);
		
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
