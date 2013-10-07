package com.br.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.gmt.modisco.java.Comment;
import org.eclipse.gmt.modisco.java.CompilationUnit;
import org.eclipse.gmt.modisco.java.Expression;
import org.eclipse.gmt.modisco.java.FieldDeclaration;
import org.eclipse.gmt.modisco.java.StringLiteral;
import org.eclipse.gmt.modisco.java.TypeAccess;
import org.eclipse.gmt.modisco.java.VariableDeclarationFragment;

import parser.StatementDelete;
import parser.StatementSelect;
import parser.StatementUpdate;

public class DeepInFieldDeclaration {

	private CompilationUnit compilationUnit;
	
	private FieldDeclaration fieldDeclaration;
	
	//Class that contains all comment to be added...
	private CreateCommentFieldDeclaration createCommentFieldDeclaration = new CreateCommentFieldDeclaration();

	public DeepInFieldDeclaration(CompilationUnit compilationUnit,
			FieldDeclaration fieldDeclaration) {
		super();
		this.compilationUnit = compilationUnit;
		this.fieldDeclaration = fieldDeclaration;
	}
	

	public void deep () {
		
		//get the type to validade if it is an instance of Connection, ResultSet, PreparementStatement or Statement;
				String type  = fieldDeclaration.getType().getType().getName();
				
				TypeAccess typeAcess = fieldDeclaration.getType();
		
		TypeAccess typeOfTheFiedl = this.fieldDeclaration.getType();
		
		
		if (type.equalsIgnoreCase("Connection")) {
			
			createCommentFieldDeclaration.createCommentIfConnection(fieldDeclaration, typeOfTheFiedl);
			
		} else if (type.equalsIgnoreCase("ResultSet")) {
			
			createCommentFieldDeclaration.createCommentIfResultSet(fieldDeclaration, typeOfTheFiedl);
			
		} else if (type.equalsIgnoreCase("PreparedStatement")) {
			
			createCommentFieldDeclaration.createCommentIfPreparementStatement(fieldDeclaration, typeOfTheFiedl);
			
		} else if (type.equalsIgnoreCase("Statement")) {
			
			createCommentFieldDeclaration.createCommentIfJDBCStatement(fieldDeclaration, typeOfTheFiedl);
			
		} else if (typeOfTheFiedl.getType().getName().equalsIgnoreCase("String")) {

			VariableDeclarationFragment fragment = fieldDeclaration
					.getFragments().get(0);

			Expression expression = fragment.getInitializer();

			if (expression instanceof StringLiteral) {
				
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

					this.addUpdateCommentInTheModel(stringLiteral, lineSeparator, createCommentFieldDeclaration, fieldDeclaration);

				}
				
				//regex para obter somente delele statement
				Pattern patternDelete = Pattern.compile("^\\W\\s*delete", Pattern.CASE_INSENSITIVE);
				
				Matcher matcherDelete = patternDelete.matcher(stringLiteral
						.getEscapedValue());
				
				if (matcherDelete.find()) {
					
					this.addDeleteCommentInTheModel(stringLiteral, lineSeparator, createCommentFieldDeclaration, fieldDeclaration);
					
				}
				
				//regex para obter somente select
		    	Pattern patternSelect = Pattern.compile("^\\W\\s*select", Pattern.CASE_INSENSITIVE);
		    	
				Matcher matcher = patternSelect.matcher(stringLiteral
						.getEscapedValue());
				
				if(matcher.find()) {
				
					this.addSelectCommentInTheModel(stringLiteral, lineSeparator, createCommentFieldDeclaration, fieldDeclaration);
				
				}

				// stringLiteral.setEscapedValue(stringLiteral.getEscapedValue()+"//Please remove here and use the generated DAO");

			}

		}

		System.out.println("The Type is.....");
		System.out.println("The name is " + typeOfTheFiedl.getType().getName());
		
	}
	
	private void addDeleteCommentInTheModel (StringLiteral stringLiteral, String lineSeparator, CreateCommentFieldDeclaration createCommentFieldDeclaration, FieldDeclaration fieldDeclaration) {
		
		String newDelete = stringLiteral.getEscapedValue().trim().replaceFirst("\\s*delete", "delete").replace('"', ' ').replaceAll("\\+"+lineSeparator, "").replaceAll("\\s+", " ").replaceAll("\\+\\D*?\\+", " ")+";";
		
		StatementDelete statementDelete = new StatementDelete();
		
		String tableName = statementDelete.getTableName(newDelete);
		
		List<Comment> comments = createCommentFieldDeclaration.createCommentDAODelete(tableName,
				fieldDeclaration);

		for (Comment comment : comments) {
			fieldDeclaration.getComments().add(comment);
		}
		
		
	}
	
	private void addUpdateCommentInTheModel(StringLiteral stringLiteral, String lineSeparator, CreateCommentFieldDeclaration createCommentFieldDeclaration, FieldDeclaration fieldDeclaration) {
		
		String newUpdate = stringLiteral.getEscapedValue().trim().replaceFirst("\\s*update", "update").replace('"', ' ').replaceAll("\\+" + lineSeparator, "").replaceAll("\\s+", " ").replaceAll("\\+\\D*?\\+", " ")+ ";";

		System.out.println("The new UPDATE is " + newUpdate);

		StatementUpdate statementUpdate = new StatementUpdate();

		String tableName = statementUpdate.getTableName(newUpdate);
	
		List<Comment> comments = createCommentFieldDeclaration.createCommentDAOUpdate(tableName,
				fieldDeclaration);

		for (Comment comment : comments) {
			fieldDeclaration.getComments().add(comment);
		}
		
	}
	
	private void addSelectCommentInTheModel(StringLiteral stringLiteral, String lineSeparator, CreateCommentFieldDeclaration createCommentFieldDeclaration, FieldDeclaration fieldDeclaration) {
		
		String newSelect = stringLiteral.getEscapedValue().trim().replaceFirst("\\s*select", "select").replace('"', ' ').replaceAll("\\+"+lineSeparator, "").replaceAll("\\s+", " ").replaceAll("\\+\\D*?\\+", " ")+";";
		
		StatementSelect statementSelect = new StatementSelect();
		
		String tableName = statementSelect.getTableName(newSelect);
		
		List<Comment> comments = createCommentFieldDeclaration.createCommentDAOSelect(tableName, fieldDeclaration);
		
		for (Comment comment : comments) {
			fieldDeclaration.getComments().add(comment);
		}
		
		
	}
	
}
