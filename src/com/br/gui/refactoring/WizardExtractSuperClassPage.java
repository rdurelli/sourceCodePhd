package com.br.gui.refactoring;

import java.util.Iterator;
import java.util.LinkedHashSet;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class WizardExtractSuperClassPage extends WizardPage {
	private Table table;
	private Text text;
	private LinkedHashSet<ExtractSuperClassInfo> extractSuperClassInfo = null;

	/**
	 * Create the wizard.
	 */
	public WizardExtractSuperClassPage(LinkedHashSet<ExtractSuperClassInfo> extractSuperClassInfo) {
		super("wizardPage");
		this.extractSuperClassInfo = extractSuperClassInfo;
		setImageDescriptor(ResourceManager.getPluginImageDescriptor("JrubyEclipsePlugin", "icons/SuperExtractClass.gif"));
		setTitle("Extract Superclass");
		setDescription("You have two or more classes with similar features. Create a superclass and move the common features to the superclass.");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 58, 520, 110);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnClass = new TableColumn(table, SWT.NONE);
		tblclmnClass.setWidth(180);
		tblclmnClass.setText("Class has similar feature with");
		
		TableColumn tblclmnClasses = new TableColumn(table, SWT.NONE);
		tblclmnClasses.setWidth(199);
		tblclmnClasses.setText("Class");
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(140);
		tblclmnNewColumn.setText("Attributes");
		
		Label lblSimilarFeaturesAmong = new Label(container, SWT.NONE);
		lblSimilarFeaturesAmong.setBounds(10, 38, 285, 14);
		lblSimilarFeaturesAmong.setText("Similar Features among the selected classes:");
		
		Label lblClassName = new Label(container, SWT.NONE);
		lblClassName.setBounds(10, 10, 78, 14);
		lblClassName.setText("Class name:");
		
		text = new Text(container, SWT.BORDER);
		text.setBounds(82, 5, 438, 19);
		
		fillTable(table);
	}
	
	
	private void fillTable(Table table) {
		
	
		Iterator<ExtractSuperClassInfo> iterator = this.extractSuperClassInfo.iterator();
		
			while (iterator.hasNext()) {
				
				ExtractSuperClassInfo classInfo = iterator.next();
				
				TableItem item = new TableItem(table, SWT.NONE);
				
				item.setText(new String[]{classInfo.getTo().getName(), classInfo.getFrom().getName(), classInfo.getAttributeToExtract()});
				
			}				
				
				table.setRedraw(true);
	}
	
	
	public Text getText() {
		return text;
	}
	
	
		
		
	
	
}
