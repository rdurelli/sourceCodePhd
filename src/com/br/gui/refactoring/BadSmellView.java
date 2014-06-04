package com.br.gui.refactoring;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.widgets.TableColumn;

public class BadSmellView extends ViewPart {

	public static final String ID = "com.br.gui.refactoring.BadSmellView"; //$NON-NLS-1$

	public BadSmellView() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		TableTree tableTree = new TableTree(container, SWT.BORDER | SWT.FULL_SELECTION);
		tableTree.setBounds(22, 24, 500, 81);
		
		Table table = tableTree.getTable();
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	    {
	    	TableColumn tblclmnClass = new TableColumn(tableTree.getTable(), SWT.NONE);
	    	tblclmnClass.setWidth(65);
	    	tblclmnClass.setText("Class");
	    }
	    {
	    	TableColumn tblclmnBadSmell = new TableColumn(tableTree.getTable(), SWT.NONE);
	    	tblclmnBadSmell.setWidth(127);
	    	tblclmnBadSmell.setText("Recommend Bad Smell");
	    }
	    {
	    	TableColumn tblclmnSloc = new TableColumn(tableTree.getTable(), SWT.NONE);
	    	tblclmnSloc.setWidth(41);
	    	tblclmnSloc.setText("SLOC");
	    }
	    {
	    	TableColumn tblclmnNumberOfMethod = new TableColumn(tableTree.getTable(), SWT.NONE);
	    	tblclmnNumberOfMethod.setWidth(120);
	    	tblclmnNumberOfMethod.setText("Number of Method");
	    }
	    {
	    	TableColumn tblclmnNumberOfAttributes = new TableColumn(tableTree.getTable(), SWT.NONE);
	    	tblclmnNumberOfAttributes.setWidth(150);
	    	tblclmnNumberOfAttributes.setText("Number of Attributes");
	    }
	    
	    putDataInTheTable(tableTree);
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

	private void putDataInTheTable(TableTree tableTree) {
		
		TableTreeItem parent = new TableTreeItem(tableTree, SWT.NONE |SWT.CHECK);
		parent.setText(0, "Pessoa");
	    parent.setText(1, "Recommended Refactoring");
	    parent.setText(2, "500 SLOC");
	    parent.setText(3, "15");
	    parent.setText(4, "4");
	    
	    String [] refactoringsToBeApplied = {"Extract Class", "Extract Super Class", "Extract SubClass"};
	    
	    for (int i = 0; i < refactoringsToBeApplied.length; i++) {
	    	
		    TableTreeItem child = new TableTreeItem(parent, SWT.NONE|SWT.CHECK);
//	      child.setText(0, "Child " + (j + 1));
	      child.setText(1, refactoringsToBeApplied[i]);
//	      child.setText(2, "More child data");
	    	
		}

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
