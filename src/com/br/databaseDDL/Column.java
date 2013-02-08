package com.br.databaseDDL;


/**
 * @author rafaeldurelli
 * @version 1.0
 * This class is used to represent all information related to a {@link Column}
 * @see DataBase and {@link Table}
 * 
 * */

public class Column implements Comparable<Column>{
	
	/**
	 * Representing the name of the column
	 * */
	private String columnName;
	
	
	/**
	 * Representing the type of the column
	 * */
	private String columnType;
	
	
	/**
	 * This attribute represents if this column is primary key or not 
	 * */
	private Boolean isPrimaryKey;
	
	public Column(String columnName) {
		
		this.columnName = columnName;
		
	}
	
	public String getColumnName() {
		return columnName;
	}
	
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public String getColumnType() {
		return columnType;
	}
	
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	
	public Boolean getIsPrimaryKey() {
		return isPrimaryKey;
	}
	
	public void setIsPrimaryKey(Boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	@Override
	public int compareTo(Column o) {
		// TODO Auto-generated method stub
		return columnName.compareTo(o.columnName);
	}

	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		return columnName.compareToIgnoreCase(((Column) arg0).columnName) == 0;
	}
}
