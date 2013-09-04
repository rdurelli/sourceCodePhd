package com.br.tests;

import java.util.HashSet;
import java.util.Set;

public class TesteSET {
	
	
	public static void main(String[] args) {
		
		Set<String> teste = new HashSet<String>();
		
		
		teste.add("RAFAEL");
		
		if (teste.add("RAFAEL")) {
			
			System.out.println("Nao funcionou pq s‹o iguals");
		} else {
			
			System.out.println("Esta no else esta certo :D");
			
		}
		
	}

}
