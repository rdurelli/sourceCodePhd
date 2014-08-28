package com.br.gui.refactoring;

import org.eclipse.gmt.modisco.java.ASTNode;
import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.NamedElement;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.core.KDMEntity;
import org.eclipse.jface.wizard.Wizard;

import com.br.util.models.UtilJavaModel;
import com.br.util.models.UtilKDMModel;

public class RefactoringNameWizard extends Wizard {

	private RenamingRefactoringPage page1 = null; 
	
	private KDMEntity entityUnitToSetTheName = null;
	
	private NamedElement namedElementToRename = null;
	
	private boolean isClass = false;
	
	private String oldName = null;
	
	private UtilKDMModel utilKDMMODEL = new UtilKDMModel();
	
	private UtilJavaModel utilJavaModel = new UtilJavaModel();
	
	
	public RefactoringNameWizard(String nameClasse, KDMEntity entityUnitToSetTheName, NamedElement namedElementToRename, boolean isClassToBeRenamed) {
		setWindowTitle("Rename");
		this.oldName = nameClasse;
		this.page1 = new RenamingRefactoringPage(nameClasse);
		this.entityUnitToSetTheName = entityUnitToSetTheName;
		this.namedElementToRename = namedElementToRename;
		this.isClass = isClassToBeRenamed;
	}

	@Override
	public void addPages() {
		addPage(page1);
	}

	@Override
	public boolean performFinish() {
		
		
		
		if (!oldName.equals(page1.getNameClass())) {
						
			String newName = page1.getNameClass();
			
			utilKDMMODEL.actionRenameKDMEntity(this.entityUnitToSetTheName, newName);
			
			utilJavaModel.actionRenameElement(this.namedElementToRename, newName);
			
			
		}
		
		return true;
	}

}
