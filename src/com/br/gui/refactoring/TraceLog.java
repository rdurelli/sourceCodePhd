package com.br.gui.refactoring;

import java.util.ArrayList;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;

import com.br.tests.SampleHandler;
import com.br.trace.refactoring.PersisteTraceLogRefactoring;
import com.br.trace.refactoring.TrackLog;

public class TraceLog extends ViewPart {

	public static final String ID = "com.br.gui.refactoring.TraceLog"; //$NON-NLS-1$
	private Table table;

	public TraceLog() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		table.setBounds(10, 30, 515, 130);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnAuthor = new TableColumn(table, SWT.NONE);
		tblclmnAuthor.setWidth(157);
		tblclmnAuthor.setText("Author");
		
		TableColumn tblclmnProject = new TableColumn(table, SWT.NONE);
		tblclmnProject.setWidth(158);
		tblclmnProject.setText("Project");
		
		TableColumn tblclmnChanges = new TableColumn(table, SWT.NONE);
		tblclmnChanges.setWidth(314);
		tblclmnChanges.setText("Changes");
		
		populateTableOfTraces(table);
		
		SampleHandler testeSampleHandler = new SampleHandler();
		testeSampleHandler.execute();
		
		TableItem tableItem = new TableItem(table, SWT.NONE);
		tableItem.setText(new String[] {"Rafael", "Legacy_System_To_Test", "Move Field"});
		
		TableItem tableItem_1 = new TableItem(table, SWT.NONE);
		tableItem_1.setText(new String[] {"Rafael", "Legacy_System_To_Test", "Move Method"});
		
		TableItem tableItem_2 = new TableItem(table, SWT.NONE);
		tableItem_2.setText(new String[] {"Rafael", "Legacy_System_To_Test", "Extract Class"});
		
		TableItem tableItem_3 = new TableItem(table, SWT.NONE);
		tableItem_3.setText(new String[] {"Rafael", "Legacy_System_To_Test", "Extract SuperClass"});
		
		TableItem tableItem_4 = new TableItem(table, SWT.NONE);
		tableItem_4.setText(new String[] {"Rafael", "Legacy_System_To_Test", "Encapsulate Field"});
		
		TableItem tableItem_5 = new TableItem(table, SWT.NONE);
		tableItem_5.setText(new String[] {"Rafael", "Legacy_System_To_Test", "Collapse Hierarchy"});
		
		Label lblChangins = new Label(container, SWT.NONE);
		lblChangins.setBounds(10, 10, 219, 14);
		lblChangins.setText("Changes made \u200B\u200Bduring the refactorings:");

		createActions();
		initializeToolBar();
		initializeMenu();
	}

	
	private void populateTableOfTraces (Table table) {
		
		ArrayList<TrackLog> trackLogs = PersisteTraceLogRefactoring.getAllTraces();
		
		for (TrackLog trackLog : trackLogs) {
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setText(new String[] {trackLog.getAuthor(), trackLog.getProject(), trackLog.getChange_refactoring()});
		}

		
		
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
