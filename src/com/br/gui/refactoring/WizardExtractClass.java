package com.br.gui.refactoring;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.Type;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeItem;
import org.eclipse.gmt.modisco.omg.kdm.code.Datatype;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableUnit;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class WizardExtractClass extends WizardPage {
	private Text text;
	private Table table;
	private Text text_1;

	private ClassUnit classUnitToExtract;
	
	private ClassDeclaration classDeclarationToExtract;
	
	private Button btnCreateGettersAnd;
	
	
	/**
	 * Create the wizard.
	 */
	public WizardExtractClass(ClassUnit classUnitToExtract, ClassDeclaration classDeclarationToExtract) {
		super("wizardPage");
		this.classUnitToExtract = classUnitToExtract;
		this.classDeclarationToExtract = classDeclarationToExtract;
		setImageDescriptor(ResourceManager.getPluginImageDescriptor("JrubyEclipsePlugin", "icons/GIFTESTE.gif"));
		setTitle("Refactoring Extract Class");
		setDescription("You have one class doing work that should be done by two.");
		
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		Label lblClassName = new Label(container, SWT.NONE);
		lblClassName.setBounds(10, 10, 69, 14);
		lblClassName.setText("Class name:");
		
		text = new Text(container, SWT.BORDER);
		text.setBounds(83, 5, 432, 19);
		text.setText(this.classUnitToExtract.getName()+"Data");
		
		Label lblDestination = new Label(container, SWT.NONE);
		lblDestination.setBounds(10, 50, 69, 14);
		lblDestination.setText("Destination:");
		
		Button btnTopLevelClass = new Button(container, SWT.RADIO);
		btnTopLevelClass.setSelection(true);
		btnTopLevelClass.setBounds(83, 47, 101, 18);
		btnTopLevelClass.setText("Top level class");
		
		Button btnNestedClassIn = new Button(container, SWT.RADIO);
		btnNestedClassIn.setText("Nested class in "+ this.classUnitToExtract.getName());
		btnNestedClassIn.setBounds(190, 47, 390, 18);
		
		Label lblSelectFieldsFor = new Label(container, SWT.NONE);
		lblSelectFieldsFor.setBounds(10, 86, 174, 14);
		lblSelectFieldsFor.setText("Select fields for extracted class:");
		
		table = new Table(container, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION | SWT.V_SCROLL);
		table.setBounds(10, 106, 412, 115);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnType = new TableColumn(table, SWT.NONE);
		tblclmnType.setWidth(200);
		tblclmnType.setText("Type");
		
		TableColumn tblclmnName = new TableColumn(table, SWT.NONE);
		tblclmnName.setWidth(263);
		tblclmnName.setText("Name");
		
		Button btnEdit = new Button(container, SWT.NONE);
		btnEdit.setBounds(428, 106, 94, 28);
		btnEdit.setText("Edit...");
		
		btnCreateGettersAnd = new Button(container, SWT.CHECK);
		btnCreateGettersAnd.setBounds(10, 227, 161, 18);
		btnCreateGettersAnd.setText("Create getters and setters");
		btnCreateGettersAnd.setSelection(false);
		
		Label lblFieldName = new Label(container, SWT.NONE);
		lblFieldName.setBounds(10, 251, 69, 14);
		lblFieldName.setText("Field name:");
		
		text_1 = new Text(container, SWT.BORDER);
		text_1.setBounds(83, 246, 432, 19);
		text_1.setText("data");
		
		fillTable(table);
		
		Button button = new Button(container, SWT.CHECK);
		button.setBounds(45, 137, 94, 18);
		button.setText("Check Button");
	}
	
	
	public Table getTable() {
		
		return table;
	}
	
	public Text getFieldName() {
		return text_1;
	}
	
	public Text getNameNewClass() {
		return text;
	}
	
	
	public Button getCreateGettersAndSetters() {
		return btnCreateGettersAnd;
	}
	
	
	private void fillTable(Table table) {
		
		EList<CodeItem> attributes = this.classUnitToExtract.getCodeElement();
		
		for (CodeItem codeItem : attributes) {
			if (codeItem instanceof StorableUnit) {
				
				StorableUnit storableUnit = (StorableUnit) codeItem;
				
				Datatype type = storableUnit.getType();
				
				TableItem item = new TableItem(table, SWT.NONE);
				
				item.setText(new String[]{type.getName(), storableUnit.getName()});
				
				
			}
		}
		table.setRedraw(true);
		
		
	}
}
