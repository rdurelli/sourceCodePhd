package com.br.utils;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory.Registry;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;

import SQLMODEL.sqlmodel.Database;
import SQLMODEL.sqlmodel.SqlmodelFactory;
import SQLMODEL.sqlmodel.impl.SqlmodelPackageImpl;

import com.br.databaseDDL.Column;
import com.br.databaseDDL.DataBase;
import com.br.databaseDDL.Table;


/**
 * @author Rafael Durelli
 * @see SQLMODEL.sqlmodel.*
 * @version 1.0
 * */
public class CreateSQLModel {

	
	/**
	 * This Factory is used to create the SQLmodel with all metadatas related to the databases...
	 * */
	private static SqlmodelFactory factory ;
	
	
	/**
	 * @author rafaeldurelli
	 * In this block we init the SqlModel by calling the init()
	 * */
	static {
		
		SqlmodelPackageImpl.init();
		
		factory = SqlmodelFactory.eINSTANCE;
		
	}

	/**
	 * @author rafaeldurelli
	 *  @param SQLMODEL.sqlmodel.DataBase used to instantiate the DataBase metaClasse
	 * */
	public static void createModel(DataBase dataBase) {
		
		
		Database dataBaseModel = factory.createDatabase();
		
		//set the name of the DataBase
		dataBaseModel.setName(dataBase.getDataBaseName());
		
		addTables(dataBaseModel, dataBase.getDataBaseTables());
		
		Registry reg = Registry.INSTANCE;
		
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("sqlmodel", new XMIResourceFactoryImpl());
		
		ResourceSet resSet = new ResourceSetImpl();
		
		Resource resource = resSet.createResource(URI.createURI("file:/Users/rafaeldurelli/Documents/runtime-EclipseApplication/Teste/models/My2.sqlmodel"));
		
		// Get the first model element and cast it to the right type, in my
	    // example everything is hierarchical included in this first node
	    resource.getContents().add(dataBaseModel);

	    // Now save the content.
	    try {
	      
	    	resource.save(Collections.EMPTY_MAP);
	    	
	      
	    } catch (IOException e) {
	    	MessageDialog.openError(null, "It was not possible to create the sqlmodel.", "Please vefiry what happened.");
	    	e.printStackTrace();
	    }
	    
	    MessageDialog.openInformation(null, "The sqlmodel was created in the folder models.", "If the folder named models is not visible, please refresh your project in order to see the sqlmodel.");
	    
	    
	    
	    
	
	}
	
	/**
	 * Add tables to an instance of DataBase metaClasse
	 * @author rafaeldurelli
	 *  @param SQLMODEL.sqlmodel.DataBase DataBase used to put tables.
	 *  @param Set<Table> Tables obtained from a database.
	 * */
	private static void addTables(Database dataBaseModel, Set<Table> tables){
		
		Iterator<Table> iter = tables.iterator();
		
		while (iter.hasNext()) {
			
			Table table = iter.next();
			
			//create the SQLMODEL Table..
			SQLMODEL.sqlmodel.Table tableModel = factory.createTable();
			
			//set the name of the Table.
			tableModel.setName(table.getTableName());
			
			addColumn(tableModel, table.getColumnsTable());
			
			dataBaseModel.getTables().add(tableModel);
			
			
		}
		
		
		
		
	}
	
	/**
	 * Add columns to an instance of Table metaClasse
	 * @author rafaeldurelli
	 *  @param SQLMODEL.sqlmodel.Table table used to put columns.
	 *  @param Set<Column> Columns obtained from a database.
	 * */
	private static void addColumn(SQLMODEL.sqlmodel.Table table,  Set<Column> columns){
		
		Iterator<Column> iter = columns.iterator();
		
		while (iter.hasNext()) {
			
			Column column = iter.next();
			
			SQLMODEL.sqlmodel.Column columnModel = factory.createColumn();
			
			if( ! (column.getColumnName() == null) ) {
				
				columnModel.setName(column.getColumnName());
				
			} else {
				
				columnModel.setName("Name Default");
				
			}
			
			if ( ! ( column.getIsPrimaryKey() == null )) {
				
				columnModel.setPrimarykey(column.getIsPrimaryKey());
				
			} else {
				
				columnModel.setPrimarykey(false);
				
			} 
			
			if ( ! ( column.getColumnType() == null )) {
			
				columnModel.setType(column.getColumnType());
				
			} else {
				
				columnModel.setType("Type Default");
				
			}
			
			
			table.getColumns().add(columnModel);
			
		}
		
		
		
	}
}
