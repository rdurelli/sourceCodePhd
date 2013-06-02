package com.br.connection.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.br.utils.ConfDataBase;
import com.br.utils.ParserXMLDataBaseConf;
/**
 * @author rafaeldurelli
 * @version 1.0
 * This class is used to get a connection to a relational database. In this project we have used Mysql as the Data Base Management System (DBMS)
 * */
public class ConnectionFactory {
	
	private static Connection conn = null;
	
	private ParserXMLDataBaseConf parserXMLConf = new ParserXMLDataBaseConf();
	
	/**
	 * This constructor is based on the Singleton pattern.
	 * 
	 * */
	private ConnectionFactory(){
		
		ConfDataBase conf = (ConfDataBase) parserXMLConf.parserDataBaseConf();
		
		String URL = conf.getUrl();
		
		String driver = conf.getDriver();
		
		String password = conf.getPassword();
		
		String user = conf.getUser();
		
		// This will load the MySQL driver, each DB has its own driver
	      try {
			Class.forName(driver);
			// Setup the connection with the DB
		       this.conn = DriverManager.getConnection(URL, user, password );
		
	      } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
		
	}
	/**
	 * Method used to get an instance of {@link Connection}
	 * */
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
	
	public static void main(String[] args) {
		teste();
	}
	

}
