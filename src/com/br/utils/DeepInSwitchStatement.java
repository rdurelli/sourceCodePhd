package com.br.utils;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gmt.modisco.java.CompilationUnit;
import org.eclipse.gmt.modisco.java.ForStatement;
import org.eclipse.gmt.modisco.java.IfStatement;
import org.eclipse.gmt.modisco.java.Statement;
import org.eclipse.gmt.modisco.java.SwitchStatement;
import org.eclipse.gmt.modisco.java.TryStatement;
import org.eclipse.gmt.modisco.java.VariableDeclarationStatement;
import org.eclipse.gmt.modisco.java.WhileStatement;

public class DeepInSwitchStatement {

	
	private CompilationUnit compilationUnit;
	
	private SwitchStatement switchStatement;

	public DeepInSwitchStatement(CompilationUnit compilationUnit,
			SwitchStatement switchStatement) {
		super();
		this.compilationUnit = compilationUnit;
		this.switchStatement = switchStatement;
	}
	
	
	public void deep () {
		
		
		if (this.switchStatement.getStatements().size() > 0 || this.switchStatement.getStatements() != null ) {
			
			EList<Statement> statementSwi = this.switchStatement.getStatements();
			
			if (statementSwi.size() > 0 || statementSwi != null) {
				for (Statement statement : statementSwi) {

					if (statement instanceof VariableDeclarationStatement) {

						VariableDeclarationStatement variable = (VariableDeclarationStatement) statement;

						DeepInVariableDeclarationStatement variableDeclaration = new DeepInVariableDeclarationStatement(
								compilationUnit, variable);

						variableDeclaration.deep();
					}

					else if (statement instanceof TryStatement) {

						TryStatement tryStatement = (TryStatement) statement;

						DeepInTryStatement deepInTry = new DeepInTryStatement(
								compilationUnit, tryStatement);

						deepInTry.deep();

					}

					else if (statement instanceof IfStatement) {

						IfStatement ifStatement = (IfStatement) statement;

						 DeepInIfStatement deepInIF = new
						 DeepInIfStatement(compilationUnit, ifStatement);

						 deepInIF.deep();

					}

					else if (statement instanceof ForStatement) {

						ForStatement forStatement = (ForStatement) statement;

						 DeepInForStatement deepInFOR = new
						 DeepInForStatement(compilationUnit, forStatement);

						 deepInFOR.deep();

					}

					else if (statement instanceof WhileStatement) {

						WhileStatement whileStatement = (WhileStatement) statement;

						 DeepInWhileStatement deepInWHILE = new
						 DeepInWhileStatement(compilationUnit,
						 whileStatement);

						 deepInWHILE.deep();

					}

					else if (statement instanceof SwitchStatement) {

						SwitchStatement switchStatment = (SwitchStatement) statement;

						 DeepInSwitchStatement deepInSwitch = new
						 DeepInSwitchStatement(compilationUnit,
						 switchStatment);

						 deepInSwitch.deep();

					}

				}
			}
			
		}
		
	}
	
}
