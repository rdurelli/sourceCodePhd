package com.br.gui.refactoring;

import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeItem;
import org.eclipse.gmt.modisco.omg.kdm.code.MethodUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableUnit;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.br.util.models.UtilKDMModel;

public class WizardExtract extends Wizard {

	private WizardExtractClass page1 = null; 
	
	private ClassUnit classUnitToExtract;
	
	private Package packageKDM;
	
	
	public WizardExtract(ClassUnit classUnitToExtract) {
		setWindowTitle("Extract ClassUnit");
		this.classUnitToExtract = classUnitToExtract;
		this.page1 = new WizardExtractClass(this.classUnitToExtract);
	}

	@Override
	public void addPages() {
		addPage(this.page1);
	}

	@Override
	public boolean performFinish() {
		
		System.out.println(this.page1.getCreateGettersAndSetters().getSelection());
		
		this.packageKDM = (Package)this.classUnitToExtract.eContainer();
		
		UtilKDMModel utilKDM = new UtilKDMModel();
		
		ClassUnit newClassUnit = utilKDM.createClassUnit(this.page1.getNameNewClass().getText(), this.packageKDM);
		
		
		ArrayList<StorableUnit> storableUnit = this.getSelectedStorableUnit();
		
		addStorableUnitToTheNewClass(newClassUnit, storableUnit);
		
		ArrayList<MethodUnit> getAndSet = getMethodsGettersAndSetters(storableUnit);
		
		addGetterAndSetterToTheNewClass(newClassUnit, getAndSet);
		
		System.out.println(getAndSet.size());
		
		System.out.println(storableUnit.size());
		
		return true;
	}
	
	
	private ArrayList<MethodUnit> getMethodsGettersAndSetters(ArrayList<StorableUnit> storableUnit) {
		
		EList<CodeItem> codeItems = this.classUnitToExtract.getCodeElement();
		ArrayList<MethodUnit> gettersAndSetters = new ArrayList<MethodUnit>();
		
		for (CodeItem item : codeItems) {
			
			if (item instanceof MethodUnit) {
				
				MethodUnit methodToVerify = (MethodUnit) item;
				
				for (StorableUnit storable : storableUnit) {
					
					if ( ( methodToVerify.getName().equalsIgnoreCase("get"+storable.getName()) ) || ( methodToVerify.getName().equalsIgnoreCase("set"+storable.getName()) ) ) {
						
						
						gettersAndSetters.add(methodToVerify);
						
					}
					
				}
				
				
			}
			
		}
		
		return gettersAndSetters;
		
	} 
	
	private void addGetterAndSetterToTheNewClass(ClassUnit classToPutTheGettersAndSetters, ArrayList<MethodUnit> gettersAndSetters) {
		
		
		for (MethodUnit methodUnit : gettersAndSetters) {
			classToPutTheGettersAndSetters.getCodeElement().add(methodUnit);
		}
		
		
	}
	
	private void addStorableUnitToTheNewClass(ClassUnit classToPutTheStorableUnits, ArrayList<StorableUnit> storableUnits) {
		
		for (StorableUnit storableUnit : storableUnits) {
			classToPutTheStorableUnits.getCodeElement().add(storableUnit);
		}
		
	}
	
	private ArrayList<StorableUnit> getSelectedStorableUnit() {
		
		Table table = this.page1.getTable();
		
		EList<CodeItem> storableUnits = this.classUnitToExtract.getCodeElement();
		
		ArrayList<StorableUnit> storableUnitToPut = new ArrayList<StorableUnit>();
		
		TableItem[] itens = table.getItems();
		
		for (TableItem tableItem : itens) {
			
			if (tableItem.getChecked()) {
				
				String nameAttributes = tableItem.getText(1);
				
				for (CodeItem attributes : storableUnits) {
					
					if (attributes instanceof StorableUnit) {
						
						StorableUnit storable = (StorableUnit) attributes;
						
						if (storable.getName().equals(nameAttributes)) {
							
							storableUnitToPut.add(storable);
							
						}
						
						
					}
					
				}
			
			}
		}
		return storableUnitToPut;
	}
	

}
