package com.br.wizards;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.Wizard;

import com.br.utils.ReestructuringToBeRealized;

public class ReestructuringWizard extends Wizard {

	
	protected ReestructuringPageWizard page;
	
	public ReestructuringWizard() {
		super();
		setWindowTitle("Modernization");
		setNeedsProgressMonitor(true);
		
	}

	@Override
	public void addPages() {
		
		this.page = new ReestructuringPageWizard();
		addPage(page);
		
	}

	@Override
	public boolean performFinish() {
		
		
		System.out.println("DAO " +  page.getBtnRadioButton().getSelection());
		
		System.out.println("JPA " +  page.getBtnModernizeToJpa().getSelection());
		
		System.out.println("RESTFULL " +  page.getBtnModernizeToRestufull().getSelection());
		
		System.out.println(" O Ckeck box getEnable " + page.getBtnCheckButton().getEnabled());
		
		System.out.println(" O Ckeck box is visible " + page.getBtnCheckButton().isVisible());
		
		System.out.println(" O Ckeck box isEnable " + page.getBtnCheckButton().isEnabled());
		
		System.out.println(" O Ckeck box is selected " + page.getBtnCheckButton().getSelection());
		
		this.setChoseModernization();
		
		
		
		
		System.out.println("DAO " + ReestructuringToBeRealized.modernizeToDAO);
		
		System.out.println("JPA " + ReestructuringToBeRealized.modernizeToJPA);
		
		System.out.println("RESt " + ReestructuringToBeRealized.modernizeToRESTFULL);
		
		System.out.println("REStMobile " + ReestructuringToBeRealized.modernizeToRESTFULLMobile);
		
		return true;
	}
	
	
	private  void setChoseModernization() {
		
		ReestructuringToBeRealized.modernizeToDAO = this.page.getBtnRadioButton().getSelection();
		
		ReestructuringToBeRealized.modernizeToJPA = this.page.getBtnModernizeToJpa().getSelection();
		
		ReestructuringToBeRealized.modernizeToRESTFULL = this.page.getBtnModernizeToRestufull().getSelection();
		
		ReestructuringToBeRealized.modernizeToRESTFULLMobile = this.page.getBtnCheckButton().getSelection();
		
		
	}
	

}
