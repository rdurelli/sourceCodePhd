package com.br.wizards;

import org.eclipse.jface.wizard.Wizard;

public class InputRefactoringFileWizard extends Wizard {

	protected InputRefactoringFilePageWizard page;
	
	public InputRefactoringFileWizard() {
		setWindowTitle("New Wizard");
	}

	@Override
	public void addPages() {
	
		this.page = new InputRefactoringFilePageWizard();
		addPage(page);
	
	}

	@Override
	public boolean performFinish() {
		//o c—digo vai aqui 
		return false;
	}

}
