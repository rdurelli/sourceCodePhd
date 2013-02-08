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
	
	
	/**
	 * Construct
	 * */
	public DataBase(String dataBaseName){
		
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
