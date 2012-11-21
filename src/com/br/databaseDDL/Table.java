package com.br.databaseDDL;

import java.util.Set;

public class Table implements Comparable<Table> {
	
	private String tableName;
	
	private Set<Column> columnsTable;
	
	
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
