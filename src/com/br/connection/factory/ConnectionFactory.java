package com.br.connection.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	
	private static Connection conn = null;
	
	
	private ConnectionFactory(){
		
		
		// This will load the MySQL driver, each DB has its own driver
	      try {
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
		       this.conn = DriverManager.getConnection("jdbc:mysql://localhost/project", "root", "root" );
		
	      } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
		
	}
	
	public static Connection getInstance(){
		
		if (conn == null) {
			
			new ConnectionFactory();
			
		}
		
		return conn;
		
		
	}
	

}
