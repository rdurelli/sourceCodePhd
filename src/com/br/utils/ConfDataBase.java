package com.br.utils;


/**
 * This class represents the necessaries configurations to use the data base 
 * 
 * @author rafaeldurelli
 * @version v1.0
 * */
public class ConfDataBase {

	
	/**
	 * This attribute represents the driver
	 **/
	private String driver;
	
	
	/**
	 * This attribute represents the URL
	 **/
	private String url;
	
	/**
	 * This attribute represents the user to connect to the database
	 **/
	private String user;
	
	
	/**
	 * This attribute represents the password that someone has to provide in order to connect to the database
	 **/
	private String password;
	
	
	public ConfDataBase(){
		
		
	}
	
	/**
	 * Constructor that represents the instantiate of the object
	 * 
	 * */
	public ConfDataBase(String driver, String url, String user, String password) {
		super();
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.password = password;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	
	
}
