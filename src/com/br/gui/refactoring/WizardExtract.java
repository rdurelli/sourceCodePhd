package com.br.gui.refactoring;

import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gmt.modisco.java.BodyDeclaration;
import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.FieldDeclaration;
import org.eclipse.gmt.modisco.java.MethodDeclaration;
import org.eclipse.gmt.modisco.java.Model;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeItem;
import org.eclipse.gmt.modisco.omg.kdm.code.MethodUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableUnit;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Segment;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.br.util.models.UtilJavaModel;
import com.br.util.models.UtilKDMModel;

public class WizardExtract extends Wizard {

	private WizardExtractClass page1 = null; 
	
	private ClassUnit classUnitToExtract;
	
	private ClassDeclaration classDeclarationToExtract;
	
	private Package packageKDM;
	
	public WizardExtract(ClassUnit classUnitToExtract, ClassDeclaration classDeclarationToExtract) {
		setWindowTitle("Extract ClassUnit");
		this.classUnitToExtract = classUnitToExtract;
		this.classDeclarationToExtract = classDeclarationToExtract;
		this.page1 = new WizardExtractClass(this.classUnitToExtract, this.classDeclarationToExtract);
	}

	@Override
	public void addPages() {
		addPage(this.page1);
	}

	@Override
	public boolean performFinish() {
		
		
		boolean check = this.page1.getCreateGettersAndSetters().getSelection();
		
		//obtem o pacote para colocar as classes criadas.
		this.packageKDM = (Package)this.classUnitToExtract.eContainer();
		
		//obtem todos os attributes selecionados na interface...
		ArrayList<FieldDeclaration> fields = this.getSelectedFieldDeclaration();
		//obtem todos os attributes selecionados na interface...
		ArrayList<StorableUnit> storableUnit = this.getSelectedStorableUnit();
		
		
		
		ArrayList<MethodUnit> getAndSet = getMethodsGettersAndSetters(storableUnit);
		
		ArrayList<MethodDeclaration> getAndSetJavaModel =  getMethodsGettersAndSettersJavaMODEL(fields);
		
		
		
		
		
		//cria uma instância do UtilJavaModel para realizar criações de ClassDeclaration, MethodDeclaration, FieldDeclaraction, etc.
		UtilJavaModel utilJavaModel = new UtilJavaModel();
		
		
		//cria uma instânca do UtilKDMModel para realizar criação de ClassUnit, MethodUnit, storableUnit, etc..
		UtilKDMModel utilKDM = new UtilKDMModel();
		
		//obtem o Model para depois persistir as mudanças realizadas..
		Model model = utilJavaModel.getModelToPersiste(classDeclarationToExtract);
		
		String nameNewClass = this.page1.getNameNewClass().getText();
		
		//cria uma nova classeDeclaration (nivel de JavaModel)
		ClassDeclaration classDeclarationCreated = utilJavaModel.createClassDeclaration(nameNewClass, classDeclarationToExtract.getPackage(), model);
		
		//cria uma nova ClassUnit (nivel de KDMModel)
		ClassUnit newClassUnit = utilKDM.createClassUnit(nameNewClass, this.packageKDM);
		
		//obtem o pacote como um array..passando a classUnit selecionada na INTERFACE.
		String []  packageName = utilKDM.getCompletePackageName(classUnitToExtract);
		
		//obtem a semelhante classe selecionada na INTERFACE só que em nível de JavaModel.
		ClassDeclaration classDeclarationToRemoveTheAttributes = utilJavaModel.getClassDeclaration(classUnitToExtract, packageName, model);
		
		
		//obtem o nome do fieldName da INTERFACE. será o nome do LINK
		String fieldName = this.page1.getFieldName().getText();
	
		//cria um FieldDeclaration em uma determinada ClassDeclaration...
		FieldDeclaration link =  utilJavaModel.createFieldDeclaration(fieldName, classDeclarationToRemoveTheAttributes, classDeclarationCreated, model);
		
		//cria um StorableUnit em uma determinada ClassUnit...
		utilKDM.createStorableUnitInAClassUnit(classUnitToExtract, fieldName, newClassUnit);
		
		//cria um methodDeclaration GET e coloca em uma determinada ClassDeclaration
		utilJavaModel.createMethodDeclarationGET("get"+classDeclarationCreated.getName(), classDeclarationToRemoveTheAttributes, link, classDeclarationCreated.getName().toLowerCase(), classDeclarationCreated, model);
		//cria um methodDeclaration GET e coloca em uma determinada ClassDeclaration
		utilJavaModel.createMethodDeclarationSET("set"+classDeclarationCreated.getName(), classDeclarationToRemoveTheAttributes, link, classDeclarationCreated.getName().toLowerCase(), classDeclarationCreated, model);
		
		//obtem o segment para depois persistir.
		Segment segment = utilKDM.getSegmentToPersiste(newClassUnit);
		
		
		utilKDM.createMethodUnitGETInClassUnit(this.classUnitToExtract, "get"+newClassUnit.getName(), newClassUnit,  segment);
		
//		FieldDeclaration fieldCreated = utilJavaModel.createFieldDeclaration("attriCreated", classDeclarationToRemoveTheAttributes, utilJavaModel.getStringType(model), model);
//		//cria o méthod get..
//		utilJavaModel.createMethodDeclarationGET("getAttriCreated", classDeclarationToRemoveTheAttributes,fieldCreated, "attriCreated", utilJavaModel.getStringType(model),  model);
//		
//		utilJavaModel.createMethodDeclarationSET("setAttriCreated", classDeclarationToRemoveTheAttributes, fieldCreated, "attriCreated", model);
		
		//move todos os FieldDeclaration para a nova classe criada..
		for (FieldDeclaration field : fields) {
			
			utilJavaModel.moveFieldDeclarationToClassDeclaration(classDeclarationCreated, field);
			
		}
		
		for (MethodDeclaration method : getAndSetJavaModel) {
			
			utilJavaModel.moveMethodDeclarationToClassDeclaration(classDeclarationCreated, method);
		} 
		
		//move todos os StorableUnit para a nova classe Criada..
		addStorableUnitToTheNewClass(newClassUnit, storableUnit);
		
		
		
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
	
	private ArrayList<MethodDeclaration> getMethodsGettersAndSettersJavaMODEL(ArrayList<FieldDeclaration> fieldDeclaration) {
		
		EList<BodyDeclaration> items = this.classDeclarationToExtract.getBodyDeclarations();
		ArrayList<MethodDeclaration> gettersAndSetters = new ArrayList<MethodDeclaration>();
		
		for (BodyDeclaration item : items) {
			
			if (item instanceof MethodDeclaration) {
				
				MethodDeclaration methodToVerify = (MethodDeclaration) item;
				
				for (FieldDeclaration field : fieldDeclaration) {
					
					if ( ( methodToVerify.getName().equalsIgnoreCase("get"+field.getFragments().get(0).getName()) ) || ( methodToVerify.getName().equalsIgnoreCase("set"+field.getFragments().get(0).getName()) ) ) {
						
						
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
	
	private ArrayList<FieldDeclaration> getSelectedFieldDeclaration() {
		
		Table table = this.page1.getTable();
		
		ArrayList<FieldDeclaration> fieldDeclarationsToPut = new ArrayList<FieldDeclaration>();
		
		EList<BodyDeclaration> fieldDeclarations = this.classDeclarationToExtract.getBodyDeclarations();
		
		TableItem[] itens = table.getItems();
		
		for (TableItem tableItem : itens) {
			
			if (tableItem.getChecked()) {
				
				String nameAttributes = tableItem.getText(1);
				
				for (BodyDeclaration attributes : fieldDeclarations) {
					
					if (attributes instanceof FieldDeclaration) {
						
						FieldDeclaration fieldDeclaration = (FieldDeclaration) attributes;
						
						
						if (fieldDeclaration.getFragments().get(0).getName().equals(nameAttributes)) {
							
							fieldDeclarationsToPut.add(fieldDeclaration);
							
							
						}
						
						
					}
					
				} 
				
			}
			
		}
		
		return fieldDeclarationsToPut;
		
		
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
