package com.br.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.gmt.modisco.java.Comment;
import org.eclipse.gmt.modisco.java.CompilationUnit;
import org.eclipse.gmt.modisco.java.Expression;
import org.eclipse.gmt.modisco.java.StringLiteral;
import org.eclipse.gmt.modisco.java.TryStatement;
import org.eclipse.gmt.modisco.java.TypeAccess;
import org.eclipse.gmt.modisco.java.VariableDeclarationFragment;
import org.eclipse.gmt.modisco.java.VariableDeclarationStatement;

import parser.StatementDelete;
import parser.StatementSelect;
import parser.StatementUpdate;

public class DeepInVariableDeclarationStatement {

	private CompilationUnit compilationUnit;
	
	private VariableDeclarationStatement variableDeclarationStatement;
	
	private CreateCommentVariableDeclarationStatement ccVariableDeclaration = new CreateCommentVariableDeclarationStatement();

	public DeepInVariableDeclarationStatement(CompilationUnit compilationUnit,
			VariableDeclarationStatement variableDeclarationStatement) {
		super();
		this.compilationUnit = compilationUnit;
		this.variableDeclarationStatement = variableDeclarationStatement;
	}
	
	
	
	
	public void deep() {
		
		
		//get the type to validade if it is an instance of Connection, ResultSet, PreparementStatement or Statement;
		String type  = variableDeclarationStatement.getType().getType().getName();
		
		TypeAccess typeAcess = variableDeclarationStatement.getType();
		
		
		if (type.equalsIgnoreCase("Connection")) {
			
			ccVariableDeclaration.createCommentIfConnection(variableDeclarationStatement, typeAcess);
			
		} else if (type.equalsIgnoreCase("ResultSet")) {
			
			ccVariableDeclaration.createCommentIfResultSet(variableDeclarationStatement, typeAcess);
			
		} else if (type.equalsIgnoreCase("PreparedStatement")) {
			
			ccVariableDeclaration.createCommentIfPreparementStatement(variableDeclarationStatement, typeAcess);
			
		} else if (type.equalsIgnoreCase("Statement")) {
			
			ccVariableDeclaration.createCommentIfJDBCStatement(variableDeclarationStatement, typeAcess);
			
		} else if (type.equalsIgnoreCase("String")) {
			
			VariableDeclarationFragment fragment = variableDeclarationStatement.getFragments().get(0);
			
			Expression expression = fragment.getInitializer();

			if (expression instanceof StringLiteral) {

				//Class that contains all comment to be added...in the VariableDeclarationStatement
				CreateCommentVariableDeclarationStatement createCommentFieldDeclaration = new CreateCommentVariableDeclarationStatement();
				
				StringLiteral stringLiteral = (StringLiteral) expression;

				System.out.println("O valor do Field "
						+ stringLiteral.getEscapedValue());

				String lineSeparator = System.getProperty("line.separator")
						+ " ";

				// regex para obter somente update statement
				Pattern patternUpdate = Pattern.compile("^\\W\\s*update",
						Pattern.CASE_INSENSITIVE);

				Matcher matcherUpdate = patternUpdate.matcher(stringLiteral
						.getEscapedValue());

				if (matcherUpdate.find()) {

					this.addUpdateCommentInTheModel(stringLiteral, lineSeparator, createCommentFieldDeclaration, variableDeclarationStatement);

				}
				
				//regex para obter somente delele statement
				Pattern patternDelete = Pattern.compile("^\\W\\s*delete", Pattern.CASE_INSENSITIVE);
				
				Matcher matcherDelete = patternDelete.matcher(stringLiteral
						.getEscapedValue());
				
				if (matcherDelete.find()) {
					
					this.addDeleteCommentInTheModel(stringLiteral, lineSeparator, createCommentFieldDeclaration, variableDeclarationStatement);
					
				}
				
				//regex para obter somente select
		    	Pattern patternSelect = Pattern.compile("^\\W\\s*select", Pattern.CASE_INSENSITIVE);
		    	
				Matcher matcher = patternSelect.matcher(stringLiteral
						.getEscapedValue());
				
				if(matcher.find()) {
				
					this.addSelectCommentInTheModel(stringLiteral, lineSeparator, createCommentFieldDeclaration, variableDeclarationStatement);
				
				}

				// stringLiteral.setEscapedValue(stringLiteral.getEscapedValue()+"//Please remove here and use the generated DAO");

			}
		
		
	}
	
	
}
	
	private void addSelectCommentInTheModel(StringLiteral stringLiteral, String lineSeparator, CreateCommentVariableDeclarationStatement createComment, VariableDeclarationStatement variableDeclaration) {
		
		String newSelect = stringLiteral.getEscapedValue().trim().replaceFirst("\\s*select", "select").replace('"', ' ').replaceAll("\\+"+lineSeparator, "").replaceAll("\\s+", " ").replaceAll("\\+\\D*?\\+", " ")+";";
		
		StatementSelect statementSelect = new StatementSelect();
		
		String tableName = statementSelect.getTableName(newSelect);
		
		List<Comment> comments = createComment.createCommentDAOSelect(tableName, variableDeclaration);
		
		for (Comment comment : comments) {
			variableDeclaration.getComments().add(comment);
		}
		
		
	}
	
	private void addUpdateCommentInTheModel(StringLiteral stringLiteral, String lineSeparator, CreateCommentVariableDeclarationStatement createComment, VariableDeclarationStatement variableDeclaration) {
		
		String newUpdate = stringLiteral.getEscapedValue().trim().replaceFirst("\\s*update", "update").replace('"', ' ').replaceAll("\\+" + lineSeparator, "").replaceAll("\\s+", " ").replaceAll("\\+\\D*?\\+", " ")+ ";";

		System.out.println("The new UPDATE is " + newUpdate);

		StatementUpdate statementUpdate = new StatementUpdate();

		String tableName = statementUpdate.getTableName(newUpdate);
	
		List<Comment> comments = createComment.createCommentDAOUpdate(tableName,
				variableDeclaration);

		for (Comment comment : comments) {
			variableDeclaration.getComments().add(comment);
		}
		
	}
	
	private void addDeleteCommentInTheModel (StringLiteral stringLiteral, String lineSeparator, CreateCommentVariableDeclarationStatement createComment, VariableDeclarationStatement variableDeclaration) {
		
		String newDelete = stringLiteral.getEscapedValue().trim().replaceFirst("\\s*delete", "delete").replace('"', ' ').replaceAll("\\+"+lineSeparator, "").replaceAll("\\s+", " ").replaceAll("\\+\\D*?\\+", " ")+";";
		
		StatementDelete statementDelete = new StatementDelete();
		
		String tableName = statementDelete.getTableName(newDelete);
		
		List<Comment> comments = createComment.createCommentDAODelete(tableName,
				variableDeclaration);

		for (Comment comment : comments) {
			variableDeclaration.getComments().add(comment);
		}
		
		
	}
}
