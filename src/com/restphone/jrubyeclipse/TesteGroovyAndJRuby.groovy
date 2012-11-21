package com.restphone.jrubyeclipse

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.List;

import groovy.xml.MarkupBuilder

class TesteGroovyAndJRuby {

	
	
	
	def apreenderGroovy(){
		
		def list = ["rafa", 1, "durelli"]
		
		list << 12
		
		list.each {
				
		
			println "$it"
			
			}
		
		
		}
	
	
	def testeImprimi(){
		
		
		10.times {
			println "$it"
		}
		
	
		def writer = new StringWriter()
		def xml = new MarkupBuilder(writer)	
		
		xml.records(){
			
			for (i in 0..9) {
				car(name:'HSV Mallo', make:'Holden', year:2006){
					country('Australia')
					
			   }
			}
			
			
		}
		
		
		
		println writer.toString()
		
		"rafael e nois que esta no jruby and groovy"	
	}
	
	
}
