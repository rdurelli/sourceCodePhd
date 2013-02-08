package com.br.databaseDDL;

import java.util.Set;


/**
 * @author rafaeldurelli
 * @version 1.0
 * This class is used to represent all information related to the Tables available in an specific database
 * @see DataBase
 * 
 * */

public class Table implements Comparable<Table> {
	
	
	/**
	 * Representing the name of the table
	 * */
	private String tableName;
	
	
	/**
	 * Representing all column that this table owns
	 * */
	private Set<Column> columnsTable;
	
	
	/**
	 * Constructor
	 * */
	public Table(String tableName){
		
		this.tableName = tableName;
		
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public Set<Column> getColumnsTable() {
		return columnsTable;
	}
	
	public void setColumnsTable(Set<Column> columnsTable) {
		this.columnsTable = columnsTable;
	}

	@Override
	public int compareTo(Table t) {
		
		return tableName.compareTo(t.tableName);
	}
	
	@Override
	public boolean equals(Object p2) {
	    return tableName.compareToIgnoreCase(((Table) p2).tableName) == 0;
	  }

}
