package com.br.wizards;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.wizard.Wizard;

import com.br.utils.ProjectSelectedToModernize;
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
		
		
		
		Job job = new Job("Modernizing the Project: " + ProjectSelectedToModernize.projectSelected.getElementName()) {
			  @Override
			  protected IStatus run(IProgressMonitor monitor) {
				// Set total number of work units
					monitor.beginTask("Modernizing", 1000);
	 
					for (int i = 0; i < 1000; i++) {
						try {
							Thread.sleep(1000);
							
							if (i < 150) {
								
								monitor.subTask("Getting all information" + i);
								
							} else if ( ( i > 150 ) && (i < 200) ) {
								
								monitor.subTask("Reading the KDM information" + i);
								
							} else if ( ( i > 200 ) && (i < 230) ) {
								
								monitor.subTask("Starting the algorithm" + i);
								
							} else if ( ( i > 230 ) && (i < 300) ) {
								
								monitor.subTask("Creating all entities" + i);
								
							} else if ( ( i > 300 ) && (i < 350) ) {
								
								monitor.subTask("Creating all Packages" + i);
								
							} else if ( ( i > 350 ) && (i < 453) ) {
								
								monitor.subTask("Creating all ClassUnits" + i);
								
							} else if ( ( i > 453 ) && (i < 550) ) {
								
								monitor.subTask("Creating all MethodsUnits and RelationShips" + i);
								
	
							} else {
								
								monitor.subTask("Performing the algorithm" + i);
								
							}
							
							
							// Report that 10 units are done
							monitor.worked(1);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
					return Status.OK_STATUS;
				}
			};

			// Start the Job
			job.schedule(); 
		
		
		
		return true;
	}
	
	
	private  void setChoseModernization() {
		
		ReestructuringToBeRealized.modernizeToDAO = this.page.getBtnRadioButton().getSelection();
		
		ReestructuringToBeRealized.modernizeToJPA = this.page.getBtnModernizeToJpa().getSelection();
		
		ReestructuringToBeRealized.modernizeToRESTFULL = this.page.getBtnModernizeToRestufull().getSelection();
		
		ReestructuringToBeRealized.modernizeToRESTFULLMobile = this.page.getBtnCheckButton().getSelection();
		
		
	}
	

}
