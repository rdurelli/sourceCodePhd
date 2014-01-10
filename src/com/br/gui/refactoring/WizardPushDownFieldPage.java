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

import com.br.actions.PullDownFieldClass;

public class WizardPushDownFieldPage extends WizardPage {
	private Table table;
	private LinkedHashSet<PullDownFieldInfo> pullUpFieldInfoInfo = null;

	/**
	 * Create the wizard.
	 */
	public WizardPushDownFieldPage(LinkedHashSet<PullDownFieldInfo> pullDownFieldInfoInfo) {
		super("wizardPage");
		this.pullUpFieldInfoInfo = pullDownFieldInfoInfo;
		setImageDescriptor(ResourceManager.getPluginImageDescriptor("JrubyEclipsePlugin", "icons/GifsPullUp.gif"));
		setTitle("Pull Up Field");
		setDescription("Two subclasses have the same field. Move the field to the superclass.");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 58, 555, 110);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnClass = new TableColumn(table, SWT.NONE);
		tblclmnClass.setWidth(180);
		tblclmnClass.setText("Class has similar feature with");
		
		TableColumn tblclmnClasses = new TableColumn(table, SWT.NONE);
		tblclmnClasses.setWidth(140);
		tblclmnClasses.setText("Class");
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(109);
		tblclmnNewColumn.setText("Attributes");
		
		TableColumn tblclmnNewSuper = new TableColumn(table, SWT.NONE);
		tblclmnNewSuper.setWidth(140);
		tblclmnNewSuper.setText("Inheritance");
		
		Label lblSimilarFeaturesAmong = new Label(container, SWT.NONE);
		lblSimilarFeaturesAmong.setBounds(10, 38, 285, 14);
		lblSimilarFeaturesAmong.setText("Similar Features among the selected classes:");
		
		fillTable(table);
	}
	
	
	private void fillTable(Table table) {
		
	
//		Iterator<PullUpFieldInfo> iterator = this.pullUpFieldInfoInfo.iterator();
//		
//			while (iterator.hasNext()) {
//				
//				PullUpFieldInfo classInfo = iterator.next();
//				
//				TableItem item = new TableItem(table, SWT.NONE);
//				
//				item.setText(new String[]{classInfo.getTo().getName(), classInfo.getFrom().getName(), classInfo.getAttributeToExtract(), classInfo.getSuperElement().getName()});
//				
//			}				
//				
//				table.setRedraw(true);
	}
	
	
}
