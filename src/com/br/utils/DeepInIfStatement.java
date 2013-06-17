package com.br.utils;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gmt.modisco.java.Block;
import org.eclipse.gmt.modisco.java.CompilationUnit;
import org.eclipse.gmt.modisco.java.ForStatement;
import org.eclipse.gmt.modisco.java.IfStatement;
import org.eclipse.gmt.modisco.java.Statement;
import org.eclipse.gmt.modisco.java.SwitchStatement;
import org.eclipse.gmt.modisco.java.TryStatement;
import org.eclipse.gmt.modisco.java.VariableDeclarationStatement;
import org.eclipse.gmt.modisco.java.WhileStatement;

public class DeepInIfStatement {

	private CompilationUnit compilationUnit;

	private IfStatement ifStatement;

	public DeepInIfStatement(CompilationUnit compilationUnit,
			IfStatement ifStatement) {
		super();
		this.compilationUnit = compilationUnit;
		this.ifStatement = ifStatement;
	}

	public void deep() {

		Block blockThenStatement = null;

		Block blockElseStatement = null;

		if (this.ifStatement.getThenStatement() != null) {

			if (this.ifStatement.getThenStatement() instanceof Block) {
			
				blockThenStatement = (Block) this.ifStatement.getThenStatement();
			
			}

		}

		if (this.ifStatement.getElseStatement() != null) {

			if (this.ifStatement.getThenStatement() instanceof Block) {
				
				blockElseStatement = (Block) this.ifStatement.getElseStatement();
				
			}

		}

		if (blockThenStatement != null) {

			EList<Statement> statementThen = blockThenStatement.getStatements();
			if (statementThen.size() > 0 || statementThen != null) {
				for (Statement statement : statementThen) {

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

		if (blockElseStatement != null) {

			EList<Statement> statementsElse = blockElseStatement
					.getStatements();

			if (statementsElse.size() > 0 || statementsElse != null) {

				for (Statement statement : statementsElse) {

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
