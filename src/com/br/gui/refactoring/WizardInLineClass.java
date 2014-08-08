package com.br.gui.refactoring;


import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.FieldDeclaration;
import org.eclipse.gmt.modisco.java.Model;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableUnit;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Segment;
import org.eclipse.jface.wizard.Wizard;

import com.br.util.models.UtilJavaModel;
import com.br.util.models.UtilKDMModel;

public class WizardInLineClass extends Wizard {

	
	private WizardInLineClassPage page1 = null;

	
	private UtilKDMModel utilKDMMODEL = new UtilKDMModel();
	private UtilJavaModel utilJavaModel = new UtilJavaModel();
	
	
	private String URIProject = null;
	
	private ClassUnit classUnitSelectedToInline1 = null;
	private ClassUnit classUnitSelectedToInline2 = null;
	
	private StorableUnit storableUnitLink = null;
	
	private ClassDeclaration classDeclarationSelectedToInLine1 = null;
	private ClassDeclaration classDeclarationSelectedToInLine2 = null;
	
	private FieldDeclaration fieldDeclarationLink = null;
	
	private Package packageToRemoveTheClass;
	private org.eclipse.gmt.modisco.java.Package packageToRemoveTheClassJava;
	
	private Segment segment = null;
	private Model model = null;
	
	
	public WizardInLineClass(ClassUnit classUnitSelectedToInline1, ClassUnit classUnitSelectedToInline2, ClassDeclaration classDeclarationSelectedToInLine1, ClassDeclaration classDeclarationSelectedToInLine2, Model model, String URIProject, Package packageToRemoveTheClass, org.eclipse.gmt.modisco.java.Package packageToRemoveTheClassJava, StorableUnit storableUnitLink, FieldDeclaration fieldDeclarationLink, Segment segment) {
		setWindowTitle("InLine Class");
		
		this.classUnitSelectedToInline1 = classUnitSelectedToInline1;
		this.classUnitSelectedToInline2 = classUnitSelectedToInline2;
		
		this.classDeclarationSelectedToInLine1 = classDeclarationSelectedToInLine1;
		this.classDeclarationSelectedToInLine2 = classDeclarationSelectedToInLine2;
		
		this.packageToRemoveTheClass = packageToRemoveTheClass;
		this.packageToRemoveTheClassJava = packageToRemoveTheClassJava;
		
		this.storableUnitLink = storableUnitLink;
		this.fieldDeclarationLink = fieldDeclarationLink;
		
		this.page1 = new WizardInLineClassPage(classUnitSelectedToInline1, classUnitSelectedToInline2);
		this.model = model;
		this.segment = segment;
		this.URIProject = URIProject;
	}

	@Override
	public void addPages() {
		this.addPage(this.page1);
	}

	@Override
	public boolean performFinish() {
		utilKDMMODEL.actionInLineClass(classUnitSelectedToInline1, classUnitSelectedToInline2, packageToRemoveTheClass, storableUnitLink, segment);
		utilJavaModel.actionInLineClass(classDeclarationSelectedToInLine1, classDeclarationSelectedToInLine2, packageToRemoveTheClassJava, fieldDeclarationLink, model);
		return true;
	}

}
