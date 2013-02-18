package parser;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Set;

import com.br.connection.factory.ConnectionFactory;
import com.br.databaseDDL.Column;
import com.br.databaseDDL.DataBase;
import com.br.databaseDDL.Table;

public abstract class StatementCRUD {
	
	public static Set<String> inserts;
	public static Set<String> delets;
	public static Set<String> updates;
	public static Set<String> selects;
	
	public abstract void createStatement(Set<String> statements, DataBase database);
	

	public  void getColumnType (Set<Table> tables){
		
		Connection connection = ConnectionFactory.getInstance();
		
		try {
			Statement stmt = connection.createStatement();
			DatabaseMetaData metaData = connection.getMetaData();
			
		    ResultSet rSeltMeta = metaData.getTables(null, null, "%", null);
		      
		      while (rSeltMeta.next()) {
				
		    	  if (tables.contains(new Table(rSeltMeta.getString(3)))){
		    		  
		    		  Table tablesObtained = tables.iterator().next();
		    		  
		    		  Set<Column> columnObtained = tablesObtained.getColumnsTable();
		    		  
//		    		  System.out.println("O nome da tabela OBTIDA Ž " + teste.getTableName());
		    		  
//		    		  System.err.println("Sim contem ");
		    		  
		    		  ResultSet resultSetMetaData = stmt.executeQuery("SELECT * FROM "+ tablesObtained.getTableName());
		    		  
		    		  ResultSetMetaData rsMeta = resultSetMetaData.getMetaData();
		    		  
		    		  Iterator<Column> iterator = columnObtained.iterator();
		    		  
//		    		  Column columnToAddType = columnObtained.iterator().next();
		    		  
		    		  for (int j = 0; j < rsMeta.getColumnCount(); j++) {
						
		    			  
		    			  iterator.hasNext();
		    			  if(columnObtained.contains(new Column(rsMeta.getColumnName(j + 1)))) {
		    				  
		    				  Column columnToAddType = iterator.next();
		    				  
		    				  System.err.println("O que tem no getColumnName " +rsMeta.getColumnName(j + 1));
		    				  
		    				  columnToAddType.setColumnType(rsMeta.getColumnTypeName(j + 1));
		    				  
		    				  System.err.println("O nome da COLUNA Ž " +columnToAddType.getColumnName());
		    				  
		    				  System.err.println("O type Ž COLUNA Ž " +columnToAddType.getColumnType());
		    				  
		    				  if(rsMeta.isAutoIncrement(j + 1)) {
		    					  
		    					  columnToAddType.setIsPrimaryKey(true);
		    					  
		    				  }else {
		    					  
		    					  columnToAddType.setIsPrimaryKey(false);
		    					  
		    				  }
		    				  
		    				  
		    			  } else {
		    				 
		    				  Column newColumn = new Column(rsMeta.getColumnName(j + 1));
		    				  System.err.println("O nome da COLUNA no else Ž " +newColumn.getColumnName());
		    				  
		    				  newColumn.setColumnType(rsMeta.getColumnTypeName(j + 1));
		    				  
		    				  System.err.println("O type da COLUNA no else Ž " +newColumn.getColumnType());
		    				  
		    				  if(rsMeta.isAutoIncrement(j + 1)) {
		    					  
		    					  newColumn.setIsPrimaryKey(true);
		    					  
		    				  } else {
		    					  
		    					  
		    					  newColumn.setIsPrimaryKey(false);
		    					  
		    				  }
		    				  columnObtained.add(newColumn);
		    				  columnObtained.iterator().hasNext();

		    			  }
		    			  
		    			  
//		    			  	System.out.println(rsMeta.getColumnTypeName(j + 1));
//		    	            System.out.println(rsMeta.getColumnType(j + 1));
//		    	            System.out.println(rsMeta.getColumnDisplaySize(j + 1));
//		    	            System.out.println(rsMeta.getColumnName(j + 1));
//		    	            System.out.println(rsMeta.isAutoIncrement(j + 1));
		    			  
					}
		    		  
		    	  }
		    	  
		    	  
		    	  
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
