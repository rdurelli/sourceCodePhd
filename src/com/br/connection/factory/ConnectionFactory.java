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
	
	

}
