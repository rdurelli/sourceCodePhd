package com.br.wizards;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.Wizard;

public class ReestructuringWizard extends Wizard {

	
	protected ReestructuringPageWizard page;
	
	public ReestructuringWizard() {
		super();
		setWindowTitle("New Wizard");
		setNeedsProgressMonitor(true);
		
	}

	@Override
	public void addPages() {
		
		this.page = new ReestructuringPageWizard();
		addPage(page);
		
	}

	@Override
	public boolean performFinish() {
		
		System.out.println(" O TEXTO Ž " + page.getText().getText());
		
		
		if (page.getText().getText().contains(".kdm")) {
			
			System.out.println("Sim contain");
			
		}else
		{
			
			System.out.println("N‹o tem");
		}
		
		Status status = new Status(IStatus.OK, "not_used", 0, "", null);
		
		if (page.getText().getText().equals("Please browse the KDMModel to realize the modernization")) {
			
	
			
			status = new Status(IStatus.ERROR, "not_used", 0, 
			           "Departure and destination cannot be the same", null);
			
		}
		
		System.out.println("DAO " +  page.getBtnRadioButton().getSelection());
		
		System.out.println("JPA" +  page.getBtnModernizeToJpa().getSelection());
		
		System.out.println("RESTFULL" +  page.getBtnModernizeToRestufull().getSelection());
		
		return true;
	}
	
	
	

}
