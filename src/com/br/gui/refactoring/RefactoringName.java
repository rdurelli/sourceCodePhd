package com.br.gui.refactoring;

import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class RefactoringName extends Dialog {

	protected Object result;
	protected Shell shlRenameClassunit;
	private Text text;

	private ClassUnit classUnit;
	
	public RefactoringName(Shell parent, ClassUnit classUnit) {
		super(parent);
		setText("Rename ClassUnit.");
		this.classUnit = classUnit;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlRenameClassunit.open();
		shlRenameClassunit.layout();
		Display display = getParent().getDisplay();
		while (!shlRenameClassunit.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlRenameClassunit = new Shell(getParent(), getStyle());
		shlRenameClassunit.setSize(449, 167);
		shlRenameClassunit.setText("Rename ClassUnit.");
		
		Label lblNewLabel = new Label(shlRenameClassunit, SWT.NONE);
		lblNewLabel.setBounds(10, 10, 63, 14);
		lblNewLabel.setText("New name:");
		
		text = new Text(shlRenameClassunit, SWT.BORDER);
		text.setBounds(74, 5, 365, 19);
		text.setText(this.classUnit.getName());
		
		Button btnFinish = new Button(shlRenameClassunit, SWT.NONE);
		btnFinish.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				System.out.println("O novo valor da Classe Ž " + text.getText());
				
			}
		});
		btnFinish.setText("Finish");
		btnFinish.setBounds(345, 107, 94, 28);

	}
}
