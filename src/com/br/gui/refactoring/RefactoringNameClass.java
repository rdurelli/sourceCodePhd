package com.br.gui.refactoring;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class RefactoringNameClass extends ViewPart {

	public static final String ID = "org.eclipse.wb.swt.RefactoringNameClass"; //$NON-NLS-1$
	private Text text;

	public RefactoringNameClass() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		Label newClass = new Label(container, SWT.NONE);
		newClass.setBounds(49, 181, 95, 14);
		newClass.setText("New Class name:");
		
		text = new Text(container, SWT.BORDER);
		text.setToolTipText("Please, provide the new name.");
		text.setBounds(146, 178, 291, 19);
		
		Button btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnNewButton.setBounds(449, 175, 94, 26);
		btnNewButton.setText("rename.");
		
		Label lblPackages = new Label(container, SWT.NONE);
		lblPackages.setToolTipText("Kdm's Packages");
		lblPackages.setBounds(49, 10, 71, 14);
		lblPackages.setText("Package(s):");
		
		Label lblClasses = new Label(container, SWT.NONE);
		lblClasses.setToolTipText("Kdm's ClassUnits");
		lblClasses.setBounds(331, 10, 59, 14);
		lblClasses.setText("Class(es):");
		
		ListViewer listViewer = new ListViewer(container, SWT.BORDER | SWT.V_SCROLL);
		List list = listViewer.getList();
		list.setBounds(49, 30, 212, 145);
		
		ListViewer listViewer_1 = new ListViewer(container, SWT.BORDER | SWT.V_SCROLL);
		List list_1 = listViewer_1.getList();
		list_1.setBounds(331, 30, 212, 145);
		
		Button button = new Button(container, SWT.NONE);
		button.setBounds(267, 78, 50, 28);
		button.setText(">>");

		
		
		
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