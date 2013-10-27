package com.br.gui.refactoring;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

public class RenameClassUnit extends ViewPart {

	public static final String ID = "com.br.gui.refactoring.RenameClassUnit"; //$NON-NLS-1$
	private Text text;

	public RenameClassUnit() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		{
			Label lblNewName = new Label(container, SWT.NONE);
			lblNewName.setBounds(10, 10, 68, 14);
			lblNewName.setText("New name:");
		}
		{
			text = new Text(container, SWT.BORDER);
			text.setBounds(76, 5, 217, 19);
		}
		{
			Button btnFinish = new Button(container, SWT.NONE);
			btnFinish.setBounds(299, 3, 94, 28);
			btnFinish.setText("Finish");
		}

		createActions();
		initializeToolBar();
		initializeMenu();
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

}
