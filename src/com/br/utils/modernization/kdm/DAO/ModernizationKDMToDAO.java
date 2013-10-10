package com.br.utils.modernization.kdm.DAO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gmt.modisco.omg.kdm.code.AbstractCodeElement;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeFactory;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeModel;
import org.eclipse.gmt.modisco.omg.kdm.code.ExportKind;
import org.eclipse.gmt.modisco.omg.kdm.code.Implements;
import org.eclipse.gmt.modisco.omg.kdm.code.InterfaceUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.MethodKind;
import org.eclipse.gmt.modisco.omg.kdm.code.MethodUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.eclipse.gmt.modisco.omg.kdm.code.ParameterKind;
import org.eclipse.gmt.modisco.omg.kdm.code.ParameterUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Signature;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableKind;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.TemplateType;
import org.eclipse.gmt.modisco.omg.kdm.code.VoidType;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Attribute;
import org.eclipse.gmt.modisco.omg.kdm.kdm.KdmFactory;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Segment;
import org.eclipse.gmt.modisco.omg.kdm.source.SourceFactory;
import org.eclipse.gmt.modisco.omg.kdm.source.SourceFile;
import org.eclipse.gmt.modisco.omg.kdm.source.SourceRef;
import org.eclipse.gmt.modisco.omg.kdm.source.SourceRegion;

import com.br.databaseDDL.Column;
import com.br.databaseDDL.DataBase;
import com.br.databaseDDL.Table;
import com.br.util.models.UtilKDMModel;


/**
 * @author rafaeldurelli
 * @since 20/09/2013
 * */
public class ModernizationKDMToDAO {

	
	
	private UtilKDMModel util = new UtilKDMModel();
	
	private Segment segment = null;
	
	public ModernizationKDMToDAO() {
		
		
		
	}
	
	
	/**
	 * This method must be called to start the modernization of the KDM MODEL.  
	 *@author rafaeldurelli
	 * @param  kdmPath is a String that contains the path to the KDM to be put the news DAO
	 * @param  dataBase contains the elements to put in the segment
	 * @return return a new Segment to be persist
	 * 
	 */
	public Segment start(String kdmPath, DataBase dataBase) {
		
		Segment segment = null;
		
		UtilKDMModel utilKDMModel = new UtilKDMModel();
		
		segment = utilKDMModel.load(kdmPath);
		
		this.segment = segment;//associar o segmento para usar durante essa classInteira.
			
		CodeModel codeModel = (CodeModel) this.segment.getModel().get(0);//pega o primeiro CodeModel para colocar as novas Entities que serão criadas....
		
		
		this.createEntities(codeModel, dataBase);
		
//		primeira refatoração FEITA devo arrumar isso aqui..
		
//		Package packageTesteRefactoring = (Package) codeModel.getCodeElement().get(0);
//		
//		ClassUnit classUnitTesteRefactorin = (ClassUnit)packageTesteRefactoring.getCodeElement().get(0);
//		
//		StorableUnit novo = (StorableUnit)classUnitTesteRefactorin.getCodeElement().get(0);
//		
//		Package dao = (Package) codeModel.getCodeElement().get(2);
//		
//		ClassUnit classTObePut = (ClassUnit) dao.getCodeElement().get(0);
//		
//		classTObePut.getCodeElement().add(novo);
		
		return this.segment;		
		
	} 
	
	
	private void createEntities (CodeModel codeModel, DataBase dataBase) {
		
		
		Package packageCreated = CodeFactory.eINSTANCE.createPackage();
		
		packageCreated.setName("DAO");
		
		codeModel.getCodeElement().add(packageCreated);//aqui estamos adicionando o pacote criado no KDM já existente.....
		
		//agora devemos começar a criar as Entities dentro do Package criado		
		
		
		EList<AbstractCodeElement> codeElements = codeModel.getCodeElement();
//			devemos chamar agora o method createClassUnit passando o nome do banco de dados e um Set contendo todas as colunas daquele banco de dados...
		
		Iterator<Table> tables = dataBase.getDataBaseTables().iterator();
		
		while (tables.hasNext()) {
			
			Table table = (Table) tables.next();
			
			ClassUnit classCreated =  this.createClassUnit(packageCreated, table.getTableName(), table.getColumnsTable());
			this.createInterfaceUnit(packageCreated, classCreated);
			
		}
		
	}
	
	private void createInterfaceUnit (Package packageCreated, ClassUnit classCreated) {
		
		InterfaceUnit interfaceUnitToBeCreated = CodeFactory.eINSTANCE.createInterfaceUnit();
		
		interfaceUnitToBeCreated.setName(classCreated.getName()+"DAO");
		
		interfaceUnitToBeCreated.getAttribute().add(this.createAttributeToPutInTheInterfaceUnit());
		
		interfaceUnitToBeCreated.getSource().add(this.criarSource(classCreated.getName()));
		
		interfaceUnitToBeCreated.getCodeElement().add(this.createMethodUnitSaveOrDeleteOrUpdateOrFindOrSelectToTheInterfaceUnit("save",interfaceUnitToBeCreated, classCreated));
		
		interfaceUnitToBeCreated.getCodeElement().add(this.createMethodUnitSaveOrDeleteOrUpdateOrFindOrSelectToTheInterfaceUnit("delete",interfaceUnitToBeCreated, classCreated));
		
		interfaceUnitToBeCreated.getCodeElement().add(this.createMethodUnitSaveOrDeleteOrUpdateOrFindOrSelectToTheInterfaceUnit("update",interfaceUnitToBeCreated, classCreated));
		
		interfaceUnitToBeCreated.getCodeElement().add(this.createMethodUnitSaveOrDeleteOrUpdateOrFindOrSelectToTheInterfaceUnit("find",interfaceUnitToBeCreated, classCreated));
	
		interfaceUnitToBeCreated.getCodeElement().add(this.createMethodUnitSaveOrDeleteOrUpdateOrFindOrSelectToTheInterfaceUnit("select",interfaceUnitToBeCreated, classCreated));
		
		packageCreated.getCodeElement().add(interfaceUnitToBeCreated);
		
		this.createClassUnitJDBCDAO(packageCreated, classCreated, interfaceUnitToBeCreated);
		
	}
	
	private void createClassUnitJDBCDAO (Package packageCreated, ClassUnit nameOfTheTableToBeClass, InterfaceUnit interfaceUnit) {
		
		ClassUnit classUnitToBeCreated = CodeFactory.eINSTANCE.createClassUnit();
		classUnitToBeCreated.setName("JDBC"+nameOfTheTableToBeClass.getName()+"DAO");
		classUnitToBeCreated.setIsAbstract(false);
		classUnitToBeCreated.getAttribute().add(this.createAttibuteToPutInTheClassUnit());
		classUnitToBeCreated.getSource().add(this.criarSource(nameOfTheTableToBeClass.getName()));
		
		classUnitToBeCreated.getCodeRelation().add(this.createCodeRelationImplements(classUnitToBeCreated, interfaceUnit));
		
		classUnitToBeCreated.getCodeElement().add(this.createMethodUnitSaveOrDeleteOrUpdateOrFindOrSelectToTheClassUnit("save",classUnitToBeCreated, nameOfTheTableToBeClass));
		
		classUnitToBeCreated.getCodeElement().add(this.createMethodUnitSaveOrDeleteOrUpdateOrFindOrSelectToTheClassUnit("delete",classUnitToBeCreated, nameOfTheTableToBeClass));
		
		classUnitToBeCreated.getCodeElement().add(this.createMethodUnitSaveOrDeleteOrUpdateOrFindOrSelectToTheClassUnit("update",classUnitToBeCreated, nameOfTheTableToBeClass));
		
		classUnitToBeCreated.getCodeElement().add(this.createMethodUnitSaveOrDeleteOrUpdateOrFindOrSelectToTheClassUnit("find",classUnitToBeCreated, nameOfTheTableToBeClass));
	
		classUnitToBeCreated.getCodeElement().add(this.createMethodUnitSaveOrDeleteOrUpdateOrFindOrSelectToTheClassUnit("select",classUnitToBeCreated, nameOfTheTableToBeClass));
		
		packageCreated.getCodeElement().add(classUnitToBeCreated);
		
	}
	
	private Implements createCodeRelationImplements (ClassUnit classUnit, InterfaceUnit interfaceUnit) {
		
		Implements implementsRelationShip = CodeFactory.eINSTANCE.createImplements();
		
		implementsRelationShip.setTo(interfaceUnit);
		implementsRelationShip.setFrom(classUnit);
		
		return implementsRelationShip;
	}
	
	private ClassUnit createClassUnit (Package packageCreated, String nameOFTheTableToBeClass, Set<Column> columnsToBeAttribute ) {
		
		ClassUnit classUnitToBeCreated = CodeFactory.eINSTANCE.createClassUnit();
		
		classUnitToBeCreated.setName(nameOFTheTableToBeClass);
		classUnitToBeCreated.setIsAbstract(false);
		classUnitToBeCreated.getAttribute().add(this.createAttibuteToPutInTheClassUnit());
		classUnitToBeCreated.getSource().add(this.criarSource(nameOFTheTableToBeClass));
		
		Iterator<Column> columns = columnsToBeAttribute.iterator();
		
		while (columns.hasNext()) {
			Column column = (Column) columns.next();
			this.criarStorableUnit(classUnitToBeCreated, column.getColumnName(), column.getColumnType());
		}
		
		packageCreated.getCodeElement().add(classUnitToBeCreated);
		
		return classUnitToBeCreated;
		
	}
	
	private Attribute createAttributeToPutInTheInterfaceUnit () {
		
		
		Attribute attibute = KdmFactory.eINSTANCE.createAttribute();
		
		attibute.setTag("export");
		
		attibute.setValue("public");
		
		return attibute;
	}
	
	private Attribute createAttibuteToPutInTheClassUnit () {
		
		Attribute attibute = KdmFactory.eINSTANCE.createAttribute();
		
		attibute.setTag("export");
		
		attibute.setValue("public");
		
		return attibute;
		
	}
	
	private SourceFile criarSourceFile (String nameOFTheTableToBeClass) {
		
		SourceFile sourceFile = SourceFactory.eINSTANCE.createSourceFile();
		
		sourceFile.setName(nameOFTheTableToBeClass+".java");
		
		sourceFile.setPath("/Users/rafaeldurelli/Documents/runtime-EclipseApplication/Legacy_System_To_Test/src/"+nameOFTheTableToBeClass+".java");
		
		sourceFile.setLanguage("java");
		
		
		return sourceFile;
		
	}
	
	private SourceRegion criarSourceRegion (String nameOFTheTableToBeClass) {
		
		SourceRegion sourceRegion = SourceFactory.eINSTANCE.createSourceRegion();
		
		sourceRegion.setLanguage("java");
//		O File não esta sendo criado no modelo, tentar verificar o motivo...
		sourceRegion.setFile(this.criarSourceFile(nameOFTheTableToBeClass));
		
		
		return sourceRegion;
		
		
		
	}
	
	private SourceRef criarSource ( String nameOFTheTableToBeClass ) {
		
		SourceRef sourceRef = SourceFactory.eINSTANCE.createSourceRef();
		
		sourceRef.setLanguage("java");
		
		sourceRef.getRegion().add(this.criarSourceRegion(nameOFTheTableToBeClass));
		
		return sourceRef;
		
	}
	
	private void criarStorableUnit(ClassUnit classUnit, String name, String columnType) {
		
		StorableUnit storableUnit = CodeFactory.eINSTANCE.createStorableUnit();
		
		storableUnit.setName(name);
		
		storableUnit.setKind(StorableKind.GLOBAL);
		
		storableUnit.getAttribute().add(this.criarAttibuteForStorableUnit());
		
//		storableUnit.getSource().add(classUnit.getSource().get(0));
		
		
		if (columnType.contains("INT")) {

			
			
		   storableUnit.setType(util.getPrimitiveType(this.segment, "int"));

			
		} else if (columnType.contains("VARCHAR")) {
			
			storableUnit.setType(util.getStringType(this.segment));
			
			
		} else if (columnType.contains("FLOAT")) {
			
			storableUnit.setType(util.getPrimitiveType(this.segment, "float"));
			
		}
		else if (columnType.contains("DOUBLE")) {
			
			storableUnit.setType(util.getPrimitiveType(this.segment, "double"));
			
		}
		else if (columnType.contains("bool")) {
			
			storableUnit.setType(util.getPrimitiveType(this.segment, "boolean"));
			
		}
		else if (columnType.contains("boolean")) {
			
			storableUnit.setType(util.getPrimitiveType(this.segment, "boolean"));
			
		}
		
		classUnit.getCodeElement().add(storableUnit);
		
		
	}
	
	private Attribute criarAttibuteForStorableUnit () {
		
		Attribute att = KdmFactory.eINSTANCE.createAttribute();
		
		att.setTag("export");
		att.setValue("private");
		
		return att;
		
	}
	
	private Attribute criarAttibuteForMethodUnit () {
		
		Attribute att = KdmFactory.eINSTANCE.createAttribute();
		
		att.setTag("export");
		att.setValue("private");
		
		return att;
		
	}
	
	private MethodUnit createMethodUnitSaveOrDeleteOrUpdateOrFindOrSelectToTheInterfaceUnit (String saveOrDelete, InterfaceUnit interfaceUnitToPutTheMethod, ClassUnit classUnitToBeSaved) {
		
		MethodUnit methodUnitSave = CodeFactory.eINSTANCE.createMethodUnit();
		methodUnitSave.setName(saveOrDelete);
		methodUnitSave.setKind(MethodKind.METHOD);
		methodUnitSave.setExport(ExportKind.PUBLIC);
		methodUnitSave.getAttribute().add(this.criarAttibuteForMethodUnit());
		methodUnitSave.getSource().add(this.criarSource(interfaceUnitToPutTheMethod.getName()));
		methodUnitSave.setType(this.createTypeSignature(saveOrDelete, classUnitToBeSaved));
		methodUnitSave.getCodeElement().add(this.createSignatureSave( saveOrDelete, classUnitToBeSaved));
		
		interfaceUnitToPutTheMethod.getCodeElement().add(methodUnitSave);
		
		
		//colocar dentro do InterfaceUnit:D
		return methodUnitSave;
	}
	
	private MethodUnit createMethodUnitSaveOrDeleteOrUpdateOrFindOrSelectToTheClassUnit (String saveOrDeleteOrUpdateOrFindOrSelect, ClassUnit classUnitJDBC, ClassUnit classUnitToBeSaved) {
		
		MethodUnit methodUnitSave = CodeFactory.eINSTANCE.createMethodUnit();
		methodUnitSave.setName(saveOrDeleteOrUpdateOrFindOrSelect);
		methodUnitSave.setKind(MethodKind.METHOD);
		methodUnitSave.setExport(ExportKind.PUBLIC);
		methodUnitSave.getAttribute().add(this.criarAttibuteForMethodUnit());
		methodUnitSave.getSource().add(this.criarSource(classUnitJDBC.getName()));
		methodUnitSave.setType(this.createTypeSignature(saveOrDeleteOrUpdateOrFindOrSelect, classUnitToBeSaved));
		methodUnitSave.getCodeElement().add(this.createSignatureSave( saveOrDeleteOrUpdateOrFindOrSelect, classUnitToBeSaved));
		
		classUnitJDBC.getCodeElement().add(methodUnitSave);
		
		
		//colocar dentro do InterfaceUnit:D
		return methodUnitSave;
	}
	
	
	private Signature createTypeSignature (String saveOrDelete, ClassUnit classToBeType) {
		
		Signature signature = CodeFactory.eINSTANCE.createSignature();
		signature.setName(saveOrDelete);
		
		signature.getParameterUnit().add(this.createFirstParameterUnitForSignatureSave(classToBeType));
		//colocar aqui o parameterUnit
		signature.getParameterUnit().add(this.createSecondParameterUnitForSignatureSave(classToBeType));

		return signature;
		
	}
	
	private Signature createSignatureSave (String saveOrDelete, ClassUnit classToBeTyped) {
		
		Signature signature = CodeFactory.eINSTANCE.createSignature();
		signature.setName(saveOrDelete);
		
		signature.getParameterUnit().add(this.createFirstParameterUnitForSignatureSave(classToBeTyped));
		//colocar aqui o parameterUnit
		signature.getParameterUnit().add(this.createSecondParameterUnitForSignatureSave(classToBeTyped));

		return signature;
	}
	
	private  ParameterUnit createFirstParameterUnitForSignatureSave (ClassUnit classToBeType) {
		
		ParameterUnit parameterUnit = CodeFactory.eINSTANCE.createParameterUnit();
		
		parameterUnit.getSource().add(this.criarSource(classToBeType.getName()));
		
		parameterUnit.setKind(ParameterKind.RETURN);
		
		VoidType voidType = (VoidType) util.getPrimitiveType(segment, "void");
		
		parameterUnit.setType(voidType);
		
		return parameterUnit;
		
	}
	
	private ParameterUnit createSecondParameterUnitForSignatureSave (ClassUnit classToBeTyped) {
		
		ParameterUnit parameterUnit = CodeFactory.eINSTANCE.createParameterUnit();
		
		parameterUnit.setName(classToBeTyped.getName().toLowerCase());
		
		parameterUnit.getSource().add(this.criarSource(classToBeTyped.getName()));
		
		parameterUnit.setKind(ParameterKind.UNKNOWN);
		
		parameterUnit.setType(classToBeTyped);
		
		return parameterUnit;
	}
	
	
}
