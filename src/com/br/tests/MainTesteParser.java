package com.br.tests;

import com.br.utils.ConfDataBase;
import com.br.utils.ParserXMLDataBaseConf;

public class MainTesteParser {

	
	public static void main(String[] args) {
		
		
		ParserXMLDataBaseConf teste = new ParserXMLDataBaseConf();
		
		ConfDataBase conf = (ConfDataBase)teste.parserDataBaseConf();
		
		
		System.out.println(conf.getDriver());
		System.out.println(conf.getPassword());
		System.out.println(conf.getUrl());
		System.out.println(conf.getUser());
	}
	
}
