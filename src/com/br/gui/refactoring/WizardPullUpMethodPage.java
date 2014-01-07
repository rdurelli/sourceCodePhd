package com.br.gui.refactoring;

import java.util.Iterator;
import java.util.LinkedHashSet;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gmt.modisco.omg.kdm.code.ParameterUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Signature;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class WizardPullUpMethodPage extends WizardPage {
	private Table table;
	private LinkedHashSet<PullUpMethodInfo> pullUpMethodInfoInfo = null;

	/**
	 * Create the wizard.
	 */
	public WizardPullUpMethodPage(LinkedHashSet<PullUpMethodInfo> pullUpMethodInfoInfo) {
		super("wizardPage");
		this.pullUpMethodInfoInfo = pullUpMethodInfoInfo;
		setImageDescriptor(ResourceManager.getPluginImageDescriptor("JrubyEclipsePlugin", "icons/PullUpMethod.gif"));
		setTitle("Pull Up Method");
		setDescription("You have methods with identical results on subclasses. Move them to the superclass.");
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
		tblclmnNewColumn.setText("Methods");
		
		TableColumn tblclmnNewSuper = new TableColumn(table, SWT.NONE);
		tblclmnNewSuper.setWidth(140);
		tblclmnNewSuper.setText("Inheritance");
		
		Label lblSimilarFeaturesAmong = new Label(container, SWT.NONE);
		lblSimilarFeaturesAmong.setBounds(10, 38, 285, 14);
		lblSimilarFeaturesAmong.setText("Similar Features among the selected classes:");
		
		fillTable(table);
	}
	
	
	private void fillTable(Table table) {
		
	
		Iterator<PullUpMethodInfo> iterator = this.pullUpMethodInfoInfo.iterator();
			
			while (iterator.hasNext()) {
				
				PullUpMethodInfo classInfo = iterator.next();
				
				TableItem item = new TableItem(table, SWT.NONE);
				
				
				item.setText(new String[]{classInfo.getTo().getName(), classInfo.getFrom().getName(), classInfo.getMethodToExtract()+" "+fillArgumentsMethod((Signature)classInfo.getMethodUnitFROM().getCodeElement().get(0)), classInfo.getSuperElement().getName()});
				
				
				
			}				
				
			
			table.setRedraw(true);
	}
	
	private String fillArgumentsMethod (Signature signature) {
		
		
		String args = "(";
		
		EList<ParameterUnit> paraments = signature.getParameterUnit();
		
		for (int i = 1; i < paraments.size(); i++) {
			
			
			args += paraments.get(i).getType().getName() +" " + paraments.get(i).getName();
			
			
			
		}
		
		args += ")";
		return args;
		
	}
	
}
