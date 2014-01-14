package com.br.gui.refactoring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableUnit;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import com.br.actions.PullDownFieldClass;
import com.br.util.models.UtilKDMModel;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public class WizardPushDownFieldPage extends WizardPage {
	private Table table;
	private ClassUnit classToApplyThePullDownField = null;
	private ArrayList<ClassUnit> inheritance;
	private UtilKDMModel utilKDMModel = new UtilKDMModel();

	/**
	 * Create the wizard.
	 */
	public WizardPushDownFieldPage(ClassUnit pullDownFieldInfoInfo, ArrayList<ClassUnit> inheritance) {
		super("wizardPage");
		this.classToApplyThePullDownField = pullDownFieldInfoInfo;
		this.inheritance = inheritance;
		setImageDescriptor(ResourceManager.getPluginImageDescriptor("JrubyEclipsePlugin", "icons/PushDownField.gif"));
		setTitle("Pull Down Field");
		setDescription("Two subclasses have the same field. Move the field to the superclass.");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.BORDER | SWT.NO_BACKGROUND | SWT.NO_RADIO_GROUP);

		setControl(container);
		
		table = new Table(container, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		table.setBounds(10, 58, 309, 110);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnModifier = new TableColumn(table, SWT.NONE);
		tblclmnModifier.setWidth(72);
		tblclmnModifier.setText("Modifier");
		
		TableColumn tblclmnClass = new TableColumn(table, SWT.NONE);
		tblclmnClass.setWidth(129);
		tblclmnClass.setText("Member");
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(109);
		tblclmnNewColumn.setText("Action");
		
		Label lblSimilarFeaturesAmong = new Label(container, SWT.NONE);
		lblSimilarFeaturesAmong.setBounds(10, 38, 285, 14);
		lblSimilarFeaturesAmong.setText("Speficy actions for members:");
		
		Tree tree = new Tree(container, SWT.BORDER | SWT.FULL_SELECTION);
		tree.setBounds(425, 58, 109, 110);
		
		
		fillTable(table, tree);
		
		Button btnSelectAll = new Button(container, SWT.NONE);
		btnSelectAll.setBounds(325, 58, 94, 28);
		btnSelectAll.setText("Select All");
		
		Button btnDesElectAll = new Button(container, SWT.NONE);
		btnDesElectAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnDesElectAll.setText("Deselect All");
		btnDesElectAll.setBounds(325, 92, 94, 28);
		
		
	        
	        Label lblInheritance = new Label(container, SWT.NONE);
	        lblInheritance.setBounds(425, 38, 73, 14);
	        lblInheritance.setText("Inheritance:");
	}
	
	
	private void fillTable(Table table, Tree tree) {
		
		List<StorableUnit> storables = utilKDMModel.getStorablesUnit(classToApplyThePullDownField);
	
		for (StorableUnit storableUnit : storables) {
			
			TableItem item = new TableItem(table, SWT.NONE);
//			
			item.setText(new String[]{storableUnit.getAttribute().get(0).getValue(), storableUnit.getName(), "Pull down"});
			
		}
		
		TreeItem item = new TreeItem(tree, SWT.NONE);
	     item.setText(new String[] { classToApplyThePullDownField.getName()});
	     
	     for (ClassUnit classInheritance : inheritance) {
		
	    	 TreeItem subItem = new TreeItem(item, SWT.NONE);
		        subItem
		            .setText(new String[] { classInheritance.getName() });
	    	 
		}
	}
	
	
	public Table getTable() {
		return table;
	}
}
