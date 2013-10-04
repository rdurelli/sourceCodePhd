package com.br.utils.modernization.kdm.DAO;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gmt.modisco.omg.kdm.code.AbstractCodeElement;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeFactory;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeModel;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableKind;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableUnit;
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
 * @since 4/10/2013
 * */
public class ModernizationKDMToJPA {

	private UtilKDMModel util = new UtilKDMModel();
	
	private Segment segment = null;
	
	
	
	public Segment start(String kdmPath, DataBase dataBase) {
		
		Segment segment = null;
		
		UtilKDMModel utilKDMModel = new UtilKDMModel();
		
		segment = utilKDMModel.load(kdmPath);
		
		this.segment = segment;//associar o segmento para usar durante essa classInteira.
		
		CodeModel codeModel = (CodeModel) this.segment.getModel().get(0);//pega o primeiro CodeModel para colocar as novas Entities que serão criadas....
		
		this.createEntities(codeModel, dataBase);
		
		return this.segment;
	} 
	
	
	private void createEntities (CodeModel codeModel, DataBase dataBase) {
		
		Package packageCreated = CodeFactory.eINSTANCE.createPackage();
		
		packageCreated.setName("JPA");
		
		codeModel.getCodeElement().add(packageCreated);//aqui estamos adicionando o pacote criado no KDM já existente.....
		
		//agora devemos começar a criar as Entities dentro do Package criado		
		
		
		EList<AbstractCodeElement> codeElements = codeModel.getCodeElement();
//			devemos chamar agora o method createClassUnit passando o nome do banco de dados e um Set contendo todas as colunas daquele banco de dados...
		
		Iterator<Table> tables = dataBase.getDataBaseTables().iterator();
		
		while (tables.hasNext()) {
			
			Table table = (Table) tables.next();
			
			ClassUnit classCreated =  this.createClassUnit(packageCreated, table.getTableName(), table.getColumnsTable());
			
			
		}
		
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
	
	
}
