package com.br.databaseDDL;

import java.util.Set;

public class DataBase {

	private String dataBaseName;
	
	private Set<Table> dataBaseTables;
	
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
