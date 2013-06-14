package com.br.utils;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gmt.modisco.java.Block;
import org.eclipse.gmt.modisco.java.CompilationUnit;
import org.eclipse.gmt.modisco.java.Statement;
import org.eclipse.gmt.modisco.java.TryStatement;

public class DeepInTryStatement {

	
	private CompilationUnit compilationUnit;
	
	private TryStatement tryStatement;

	public DeepInTryStatement(CompilationUnit compilationUnit,
			TryStatement tryStatement) {
		super();
		this.compilationUnit = compilationUnit;
		this.tryStatement = tryStatement;
	}
	

	public void deep() {
		
		Block block = this.tryStatement.getBody();
		
		if (block != null) {
			
			EList<Statement> statements =  block.getStatements();
			//terminar aqui..
		}
		
	}
	
	
	
}
