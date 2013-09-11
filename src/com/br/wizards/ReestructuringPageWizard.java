package com.br.wizards;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


import org.eclipse.ui.PlatformUI;

import com.br.utils.LocationOfFiles;
import com.br.utils.ProjectSelectedToModernize;

public class ReestructuringPageWizard extends WizardPage implements Listener{
	
	private Text text;
	
	private Button btnRadioButton ;
	
	private Button btnModernizeToJpa;

	private Button btnModernizeToRestufull;
	
	private Button btnCheckButton;
	
	private Boolean generateAndroid = false;
	
	IStatus destinationStatus;
	
	/**
	 * Create the wizard.
	 */
	public ReestructuringPageWizard() {
		super("wizardPage");
		setTitle("Modernization");
		setDescription("Please select an option to realize the modernization of your legacy system.");
		destinationStatus = new Status(IStatus.OK, "not_used", 0, "", null);
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setBounds(20, 10, 299, 14);
		lblNewLabel.setText("Please select the KDM file to realize the modernization:");
		
		Label lblKdmFile = new Label(container, SWT.NONE);
		lblKdmFile.setBounds(20, 30, 53, 14);
		lblKdmFile.setText("KDM file:");
		
		text = new Text(container, SWT.BORDER);
		
		if (LocationOfFiles.LOCATION_KDM_MODEL_CREATED == null) {
			
			text.setText("Please browse the KDMModel to realize the modernization");
			
		}
		else {
			
			text.setText(LocationOfFiles.LOCATION_KDM_MODEL_CREATED);	
			
		}
		
		text.addListener(SWT.KeyUp, this);
		
		
		
		text.setBounds(77, 25, 403, 19);
		
		Button btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				FileDialog fileDialog = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
				  
				  // Set the text
				    fileDialog.setText("Select the KDMModel to realize the modernization.");
				    // Set filter on .txt files
				    fileDialog.setFilterExtensions(new String[] { "*.kdm" });
				    // Put in a readable name for the filter
				    fileDialog.setFilterNames(new String[] { "KDM(*.KDM)" });
				    // Open Dialog and save result of selection
				    String selected = fileDialog.open();
				    
				    text.setText(selected);
				   
				    
				
			}
		});
		btnNewButton.setBounds(675, 23, 94, 28);
		btnNewButton.setText("Browse...");
		
		Label lblPleaseSelectAn = new Label(container, SWT.NONE);
		lblPleaseSelectAn.setBounds(20, 66, 285, 14);
		lblPleaseSelectAn.setText("Please select an option to realize the modernization:");
		
		Button btnLoadCurrentProject = new Button(container, SWT.NONE);
		
		btnLoadCurrentProject.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
//				System.out.println("Sim eu fui selecionado");
				
				text.setText(ProjectSelectedToModernize.projectSelected.getResource().getLocationURI().toString()+"/MODELS_PIM/KDMMODEL.kdm");
			
			}
		});
		
		btnLoadCurrentProject.setBounds(486, 23, 183, 28);
		btnLoadCurrentProject.setText("Load Current KDM's Project");
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setBounds(20, 86, 149, 89);
		
		btnRadioButton = new Button(composite, SWT.RADIO);
		btnRadioButton.addListener(SWT.Selection, this);
		btnRadioButton.setBounds(0, 10, 121, 18);
		btnRadioButton.setText("Modernize to DAO");
		btnRadioButton.setSelection(true);
		
		btnModernizeToJpa = new Button(composite, SWT.RADIO);
		btnModernizeToJpa.addListener(SWT.Selection, this);
		btnModernizeToJpa.setBounds(0, 37, 121, 18);
		btnModernizeToJpa.setText("Modernize to JPA");
		
		btnModernizeToRestufull = new Button(composite, SWT.RADIO);
		btnModernizeToRestufull.addListener(SWT.Selection, this);
		btnModernizeToRestufull.setBounds(0, 61, 121, 18);
		btnModernizeToRestufull.setText("Modernize to RESTUFULL");
		
		btnCheckButton = new Button(container, SWT.CHECK);
		btnCheckButton.setEnabled(false);
		btnCheckButton.setBounds(20, 176, 375, 18);
		btnCheckButton.setText("Would you like to create an Android Application to test the REST?");
		
		
		
	}
	
	
	
	public Text getText() {
		return text;
	}
	
	
	public Button getBtnRadioButton() {
		return btnRadioButton;
	}
	
	public Button getBtnModernizeToJpa() {
		return btnModernizeToJpa;
	}
	
	public Button getBtnModernizeToRestufull() {
		return btnModernizeToRestufull;
	}
	
	public Button getBtnCheckButton() {
		return btnCheckButton;
	}

	@Override
	public void handleEvent(Event event) {
		
		
		// Initialize a variable with the no error status
	    Status status = new Status(IStatus.OK, "not_used", 0, "", null);
	    
	    if ((event.widget == text)) {
	        if ((text.getText().equals("Please browse the KDMModel to realize the modernization")) || !text.getText().contains(".kdm"))
	            status = new Status(IStatus.ERROR, "not_used", 0, 
	                "Please the KDMmodel must be provided to realize the modernization", null);        
	        destinationStatus = status;
	    }
	    
	    
	    
	    // If the event is triggered by any of the date fields  set
	    // corresponding status variable to the right value
	    if ((event.widget == btnRadioButton) || (event.widget == btnModernizeToJpa)
	    	|| (event.widget == btnModernizeToRestufull)) {
			if	(text.getText().equals("Please browse the KDMModel to realize the modernization") || !text.getText().contains(".kdm")) 
	            status = new Status(IStatus.ERROR, "not_used", 0, 
	                "Please the KDMmodel must be provided to realize the modernization", null);	                
			destinationStatus = status;
			
	    }
	    
	    
	    
	 // Show the most serious error
	    applyToStatusLine(findMostSevere());
	    getWizard().getContainer().updateButtons();
	    
	    if (event.widget == btnModernizeToRestufull) {
	    	
	    	this.btnCheckButton.setEnabled(true);
	    	
	    	
	    }
	    
	    if (event.widget == btnRadioButton || event.widget == btnModernizeToJpa) {
	    	
	    	this.btnCheckButton.setEnabled(false);
	    	
	    }
		
		
	}
	
	
	private IStatus findMostSevere()
	{
		
		if (destinationStatus.matches(IStatus.ERROR))
			return destinationStatus;
		else return destinationStatus;	
	}
	
	/**
	 * Applies the status to the status line of a dialog page.
	 */
	private void applyToStatusLine(IStatus status) {
		String message= status.getMessage();
		if (message.length() == 0) message= null;
		switch (status.getSeverity()) {
			case IStatus.OK:
				setErrorMessage(null);
				setMessage(message);
				break;
			case IStatus.WARNING:
				setErrorMessage(null);
				setMessage(message, WizardPage.WARNING);
				break;				
			case IStatus.INFO:
				setErrorMessage(null);
				setMessage(message, WizardPage.INFORMATION);
				break;			
			default:
				setErrorMessage(message);
				setMessage(null);
				break;		
		}
	}	
	
	
}