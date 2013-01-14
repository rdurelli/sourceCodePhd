package com.br.utils

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;


/**
 * This class is used to read the all configuration files of this project
 * @author rafaeldurelli
 * @version 1.0
 * 
 * 
 * */
class ParserXMLDataBaseConf {

	
	/**
	 * This method is used to read all configurations related to the database 
	 * @author rafaeldurelli
	 * @return confDataBase, which represents the object ConfDataBase
	 * @see ConfDataBase
	 * 
	 * */
	def parserDataBaseConf() {

		def confDataBase
		def conf_dataBase
		URL url;
		try {
				url = new URL("platform:/plugin/de.vogella.rcp.plugin.filereader/conf/conf_dataBase.xml");
			InputStream inputStream = url.openConnection().getInputStream();
		
			
				//Herein is where the parser really works.
			        conf_dataBase = new XmlParser().parse(inputStream)
			
					confDataBase = new ConfDataBase()
			
					confDataBase.driver = conf_dataBase.driver.text()
			
					confDataBase.url = conf_dataBase.url.text()
			
					confDataBase.user = conf_dataBase.user.text()
			
					confDataBase.password = conf_dataBase.password.text()
			
//					println(conf_dataBase.driver.text())
					 
			inputStream.close();
		 
		} catch (IOException e) {
			e.printStackTrace();
		}

		confDataBase


	}
}
