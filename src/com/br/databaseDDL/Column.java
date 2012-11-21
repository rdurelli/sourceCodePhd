package com.br.databaseDDL;

public class Column implements Comparable<Column>{
	
	
	private String columnName;
	
	private String columnType;
	
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
