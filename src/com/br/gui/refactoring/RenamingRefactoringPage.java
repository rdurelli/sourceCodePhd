package com.br.gui.refactoring;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;

public class RenamingRefactoringPage extends WizardPage {
	
	private Text text;

	private String nameClass;
	
	/**
	 * Create the wizard.
	 */
	public RenamingRefactoringPage(String nameClass) {
		super("wizardPage");
		setTitle("Rename KDMEntity.");
		setDescription("Renaming.");
		this.nameClass = nameClass;
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		Label lblNewName = new Label(container, SWT.NONE);
		lblNewName.setBounds(10, 10, 63, 14);
		lblNewName.setText("New name:");
		
		
		text = new Text(container, SWT.BORDER);
		text.setBounds(78, 5, 468, 19);
		text.setText(getNameClass());
		text.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (!text.getText().isEmpty()) {
					System.out.println("O Valor Ž " + text.getText());
					setNameClass(text.getText());
			          setPageComplete(true);
			        }

				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	
	
	public void setNameClass(String nameClass) {
		this.nameClass = nameClass;
	}
	
	public String getNameClass() {
		return nameClass;
	}
}
