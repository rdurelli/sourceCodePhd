package com.br.gui.refactoring;

import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.jface.wizard.Wizard;

public class RefactoringNameWizard extends Wizard {

	private RenamingRefactoringPage page1 = null;
	
	private ClassUnit classUnitToSetTheName = null;
	
	private String oldName = null;
	
	public RefactoringNameWizard(String nameClasse, ClassUnit classUnitToSetTheName) {
		setWindowTitle("Rename");
		this.oldName = nameClasse;
		this.page1 = new RenamingRefactoringPage(nameClasse);
		this.classUnitToSetTheName = classUnitToSetTheName;
	}

	@Override
	public void addPages() {
		addPage(page1);
	}

	@Override
	public boolean performFinish() {
		
		
		
		if (!oldName.equals(page1.getNameClass())) {
		
			System.out.println("O valor que esta no campo Ž " + page1.getNameClass());
			
			this.classUnitToSetTheName.setName(page1.getNameClass());
			
		}
		
		return true;
	}

}
