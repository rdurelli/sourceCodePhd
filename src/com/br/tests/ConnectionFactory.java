package com.br.tests;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
	
	
public static void teste (){
		
		String sql = "INSERT INTO ALUNO (name, lastName, RA, id) values (?, ?, ?, ?)";
		
		PreparedStatement stmt = null;
		
//		private  String name;
//
//		private  String lastName;
//
//		private  String RA;
//
//		private  Integer id;
		
		try {
			stmt = getInstance().prepareStatement(sql);
			stmt.setString(1, "Rafael");
			stmt.setString(2, "Durelli");
			stmt.setString(3, "383488");
			stmt.setInt(4, 1);
			
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void main (String...args) {
		
		
		if (getInstance()!= null) {
			
			System.out.println("ok");
		}
		
		teste();
		
		
	}
	

}