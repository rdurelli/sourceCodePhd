package com.br.gui.refactoring;

import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class WizardInLineClassPage extends WizardPage {
	private Table table;

	private ClassUnit classUnitSelectedToInline1 = null;
	private ClassUnit classUnitSelectedToInline2 = null;
	
	/**
	 * Create the wizard.
	 */
	public WizardInLineClassPage(ClassUnit classUnitSelectedToInline1, ClassUnit classUnitSelectedToInline2) {
		super("wizardPage");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor("JrubyEclipsePlugin", "icons/InLine.gif"));
		setTitle("InLine Class");
		setDescription("A class isn't doing very much. \nMove all its features into another class and delete it.");
		this.classUnitSelectedToInline1 = classUnitSelectedToInline1;
		this.classUnitSelectedToInline2 = classUnitSelectedToInline2;
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(122, 10, 315, 53);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(149);
		tblclmnNewColumn.setText("Class to hold all features");
		
		TableColumn tblclmnClass = new TableColumn(table, SWT.NONE);
		tblclmnClass.setWidth(164);
		tblclmnClass.setText("Class isn't doing very much.");
		
		this.fillTable(table);
	}
	
	private void fillTable(Table table) {
				
				TableItem item = new TableItem(table, SWT.NONE);
				
				item.setText(new String[]{this.classUnitSelectedToInline1.getName(), this.classUnitSelectedToInline2.getName()});
				
				table.setRedraw(true);
	}
	
}
