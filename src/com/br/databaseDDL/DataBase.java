package com.br.databaseDDL;

import java.util.Set;
/**
 * @author rafaeldurelli
 * @version 1.0
 * This class is used to represent all information related to the DataBase
 * 
 * */

public class DataBase {

	/**
	 * Representing the name of the database
	 * */
	private String dataBaseName;
	
	/**
	 * Representing the tables available in the database
	 * @see Table
	 * */
	private Set<Table> dataBaseTables;
	
	
	private static DataBase instance;
	
	
	
	//quando vc passar null ao inves do nome do dataBase significa que vc s— quer a instancia....e vc j‡ sabe que ele j‡ foi criado em algum lugar...
	public static DataBase getInstance(String name) {
		
		
		if (( instance == null ) && ( name != null )) {
			
			instance = new DataBase(name);
			
			return instance;	
		}
		
		return instance;
		
		
	}
	
	/**
	 * Construct
	 * */
	private  DataBase(String dataBaseName){
		
		this.dataBaseName = dataBaseName;
		
	}
	
	public void setDataBaseName(String dataBaseName) {
		this.dataBaseName = dataBaseName;
	}
	
	public String getDataBaseName() {
		return dataBaseName;
	}
	
	public void setDataBaseTables(Set<Table> dataBaseTables) {
		this.dataBaseTables = dataBaseTables;
	}
	
	public Set<Table> getDataBaseTables() {
		return dataBaseTables;
	}
	
	
}
