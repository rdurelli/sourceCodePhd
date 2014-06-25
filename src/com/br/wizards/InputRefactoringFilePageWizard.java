package com.br.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

public class InputRefactoringFilePageWizard extends WizardPage {
	private Text text;
	private Text text_1;

	/**
	 * Create the wizard.
	 */
	public InputRefactoringFilePageWizard() {
		super("wizardPage");
		setTitle("Select both catalogue.refactoring and the KDM file.");
		setDescription("Select the file catalogue.refactoring and the respective KDM file to perform the defined refactorings.");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setBounds(10, 10, 456, 14);
		lblNewLabel.setText("Select the catalogue.refactoring file that contains all defined refactorings.");
		
		text = new Text(container, SWT.BORDER);
		text.setBounds(102, 34, 264, 19);
		
		Label lblRefactoringFile = new Label(container, SWT.NONE);
		lblRefactoringFile.setBounds(10, 37, 86, 14);
		lblRefactoringFile.setText("Refactoring file:");
		
		Button btnBrowser = new Button(container, SWT.NONE);
		btnBrowser.setBounds(372, 30, 94, 28);
		btnBrowser.setText("Browser");
		
		Label lblKdmFile = new Label(container, SWT.NONE);
		lblKdmFile.setBounds(10, 91, 59, 14);
		lblKdmFile.setText("KDM file:");
		
		text_1 = new Text(container, SWT.BORDER);
		text_1.setBounds(65, 86, 301, 19);
		
		Button btnBrowser_1 = new Button(container, SWT.NONE);
		btnBrowser_1.setBounds(372, 84, 94, 28);
		btnBrowser_1.setText("Browser");
	}
}
