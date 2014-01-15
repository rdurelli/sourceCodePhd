package com.br.gui.refactoring;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.FieldDeclaration;
import org.eclipse.gmt.modisco.java.Model;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableUnit;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.TableItem;

import com.br.actions.PullDownFieldClass;
import com.br.util.models.UtilJavaModel;
import com.br.util.models.UtilKDMModel;

public class WizardPushDownField extends Wizard {

	
	private WizardPushDownFieldPage page1 = null;
	private ClassUnit pullDownFieldInfo = null;
	private ArrayList<ClassUnit> inheritance = null;
	private ClassDeclaration pullDownFieldClassJavaModel;
	private ArrayList<ClassDeclaration> inheritanceJavaModel;
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
	
	public WizardPushDownField(ClassUnit pullDownFieldClass, ArrayList<ClassUnit> inheritance, ClassDeclaration pullDownFieldClassJavaModel, ArrayList<ClassDeclaration> inheritanceJavaModel ) {
		setWindowTitle("Extract Superclass");
		this.pullDownFieldInfo = pullDownFieldClass;
		this.inheritance = inheritance;
		
		this.pullDownFieldClassJavaModel = pullDownFieldClassJavaModel;
		this.inheritanceJavaModel = inheritanceJavaModel;
		
		
//		this.extractSuperClassInfoJavaModel = extractSuperClassInfoJavaModel;
		this.page1 = new WizardPushDownFieldPage(this.pullDownFieldInfo, inheritance);
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
		
		List<StorableUnit> selectedStorableUnit = new ArrayList<StorableUnit>();
		
		List<FieldDeclaration> selectedFieldDeclaration = new ArrayList<FieldDeclaration>();
		
		TableItem[] selectedItem = this.page1.getTable().getSelection();
		
		System.out.println(this.page1.getTable().getSelectionIndex());
		
		for (int i = 0; i < selectedItem.length; i++) {
			
			String nameOfstorableUnitSelected = selectedItem[i].getText(1);
			StorableUnit storableIdentified = utilKDMMODEL.getStorablesUnitByName(pullDownFieldInfo, nameOfstorableUnitSelected);
			FieldDeclaration fieldDeclarationIdentified = utilJavaModel.getFieldDeclarationByName(pullDownFieldClassJavaModel, nameOfstorableUnitSelected);
			
			if (storableIdentified != null && fieldDeclarationIdentified != null) {
				
				selectedStorableUnit.add(storableIdentified);
				selectedFieldDeclaration.add(fieldDeclarationIdentified);
				
			}
			
			
			System.out.println(selectedItem[i].getText(1));
		}
		
		if (selectedStorableUnit.size() != 0 && selectedFieldDeclaration.size() != 0) {
			
			
			utilKDMMODEL.actionPullDownField(pullDownFieldInfo, inheritance, selectedStorableUnit);
			utilJavaModel.actionPullDownField(pullDownFieldClassJavaModel, inheritanceJavaModel, selectedFieldDeclaration);
			
		} else {
			
			MessageDialog.openError(null, "Error", "Please be sure you have selected at least a StorableUnit to realize the Pull Down Field.");
			
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
