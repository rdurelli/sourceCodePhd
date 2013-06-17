package com.br.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gmt.modisco.java.AbstractTypeDeclaration;
import org.eclipse.gmt.modisco.java.Block;
import org.eclipse.gmt.modisco.java.BodyDeclaration;
import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.Comment;
import org.eclipse.gmt.modisco.java.Expression;
import org.eclipse.gmt.modisco.java.FieldDeclaration;
import org.eclipse.gmt.modisco.java.MethodDeclaration;
import org.eclipse.gmt.modisco.java.Model;
import org.eclipse.gmt.modisco.java.Package;
import org.eclipse.gmt.modisco.java.Statement;
import org.eclipse.gmt.modisco.java.StringLiteral;
import org.eclipse.gmt.modisco.java.TryStatement;
import org.eclipse.gmt.modisco.java.TypeAccess;
import org.eclipse.gmt.modisco.java.VariableDeclarationFragment;
import org.eclipse.gmt.modisco.java.VariableDeclarationStatement;
import org.eclipse.swt.widgets.Shell;

import parser.StatementDelete;
import parser.StatementSelect;
import parser.StatementUpdate;

public class CreateCommentOnJavaModelBasedInSqlStatement {

	private Shell shell;

	private IJavaProject file;

	private void deepInPackage(EList<Package> packages) {

		for (Package pack : packages) {

			System.out.println(" Nome dos packages " + pack.getName());

			if ((pack.getOwnedPackages() == null)
					|| (pack.getOwnedPackages().size() == 0)) {

				deepInClasse(pack.getOwnedElements());

			} else {

				deepInPackage(pack.getOwnedPackages());

			}

		}

	}

	private void deepInClasse(EList<AbstractTypeDeclaration> classesModel) {

		for (AbstractTypeDeclaration abstractTypeDeclaration : classesModel) {

			if (abstractTypeDeclaration instanceof ClassDeclaration) {

				ClassDeclaration classOfTheModel = (ClassDeclaration) abstractTypeDeclaration;

				EList<BodyDeclaration> bodies = classOfTheModel
						.getBodyDeclarations();

				for (BodyDeclaration bodyDeclaration : bodies) {

					if (bodyDeclaration instanceof FieldDeclaration) {

						FieldDeclaration fieldDeclaration = (FieldDeclaration) bodyDeclaration;
						
						DeepInFieldDeclaration deepInField = new DeepInFieldDeclaration(fieldDeclaration.getOriginalCompilationUnit(), fieldDeclaration);
						
						deepInField.deep();
						
//						this.deepInFieldDeclaration(fieldDeclaration);

					} else if (bodyDeclaration instanceof MethodDeclaration) {

						MethodDeclaration methodDeclaration = (MethodDeclaration) bodyDeclaration;

						DeepInMethodDeclaration deepInMethod = new DeepInMethodDeclaration(methodDeclaration.getOriginalCompilationUnit(), methodDeclaration);
						
						deepInMethod.deep();
						
//						this.deepInMethodDeclaration(methodDeclaration);

//						System.out.println("Yeap i'am a Method");

					}

				}

			}

		}

	}

//	private void deepInFieldDeclaration(FieldDeclaration fieldDeclaration) {
//
//		TypeAccess typeOfTheFiedl = fieldDeclaration.getType();
//
//		if (typeOfTheFiedl.getType().getName().equalsIgnoreCase("String")) {
//
//			VariableDeclarationFragment fragment = fieldDeclaration
//					.getFragments().get(0);
//
//			Expression expression = fragment.getInitializer();
//
//			if (expression instanceof StringLiteral) {
//
//				//Class that contains all comment to be added...
//				CreateCommentFieldDeclaration createCommentFieldDeclaration = new CreateCommentFieldDeclaration();
//				
//				StringLiteral stringLiteral = (StringLiteral) expression;
//
//				System.out.println("O valor do Field "
//						+ stringLiteral.getEscapedValue());
//
//				String lineSeparator = System.getProperty("line.separator")
//						+ " ";
//
//				// regex para obter somente update statement
//				Pattern patternUpdate = Pattern.compile("^\\W\\s*update",
//						Pattern.CASE_INSENSITIVE);
//
//				Matcher matcherUpdate = patternUpdate.matcher(stringLiteral
//						.getEscapedValue());
//
//				if (matcherUpdate.find()) {
//
//					this.addUpdateCommentInTheModel(stringLiteral, lineSeparator, createCommentFieldDeclaration, fieldDeclaration);
//
//				}
//				
//				//regex para obter somente delele statement
//				Pattern patternDelete = Pattern.compile("^\\W\\s*delete", Pattern.CASE_INSENSITIVE);
//				
//				Matcher matcherDelete = patternDelete.matcher(stringLiteral
//						.getEscapedValue());
//				
//				if (matcherDelete.find()) {
//					
//					this.addDeleteCommentInTheModel(stringLiteral, lineSeparator, createCommentFieldDeclaration, fieldDeclaration);
//					
//				}
//				
//				//regex para obter somente select
//		    	Pattern patternSelect = Pattern.compile("^\\W\\s*select", Pattern.CASE_INSENSITIVE);
//		    	
//				Matcher matcher = patternSelect.matcher(stringLiteral
//						.getEscapedValue());
//				
//				if(matcher.find()) {
//				
//					this.addSelectCommentInTheModel(stringLiteral, lineSeparator, createCommentFieldDeclaration, fieldDeclaration);
//				
//				}
//
//				// stringLiteral.setEscapedValue(stringLiteral.getEscapedValue()+"//Please remove here and use the generated DAO");
//
//			}
//
//		}
//
//		System.out.println("The Type is.....");
//		System.out.println("The name is " + typeOfTheFiedl.getType().getName());
//
//	}
	
	


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
	
	private void addSelectCommentInTheModel(StringLiteral stringLiteral, String lineSeparator, CreateCommentFieldDeclaration createCommentFieldDeclaration, FieldDeclaration fieldDeclaration) {
		
		String newSelect = stringLiteral.getEscapedValue().trim().replaceFirst("\\s*select", "select").replace('"', ' ').replaceAll("\\+"+lineSeparator, "").replaceAll("\\s+", " ").replaceAll("\\+\\D*?\\+", " ")+";";
		
		StatementSelect statementSelect = new StatementSelect();
		
		String tableName = statementSelect.getTableName(newSelect);
		
		List<Comment> comments = createCommentFieldDeclaration.createCommentDAOSelect(tableName, fieldDeclaration);
		
		for (Comment comment : comments) {
			fieldDeclaration.getComments().add(comment);
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
	
	private void cleanComment(FieldDeclaration fieldDeclaration) {

		EList<Comment> comments = fieldDeclaration.getComments();

		for (int i = 0; i < comments.size(); i++) {
			comments.remove(i);
		}

	}


	private void deepInMethodDeclaration(MethodDeclaration methodDeclaration) {
		
		
		
		if (methodDeclaration.getBody() != null) {
		
		
			
			Block blockMethod = methodDeclaration.getBody();
			
			
			System.out.println("O block Ž " + blockMethod);
			
			System.out.println(" statements of this block " + blockMethod.getStatements());
			
			
			EList<Statement> statementsOfTheMethod = blockMethod.getStatements();
//			
			for (Statement statement : statementsOfTheMethod) {
				if (statement instanceof VariableDeclarationStatement) {
					
					
					
					VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) statement;
					
					DeepInVariableDeclarationStatement deepVariable = new DeepInVariableDeclarationStatement(variableDeclarationStatement.getOriginalCompilationUnit(), variableDeclarationStatement);
					deepVariable.deep();
					
					System.out.println("O variableDeclaration is " + variableDeclarationStatement.getFragments().get(0).getName());
					
					System.out.println(" O type is " + variableDeclarationStatement.getType().getType().getName());
					
					
//					//get the type to validade if it is an instance of Connection, ResultSet, PreparementStatement or Statement;
//					String type  = variableDeclarationStatement.getType().getType().getName();
//					
//					TypeAccess typeAcess = variableDeclarationStatement.getType();
//					
//					CreateCommentVariableDeclarationStatement ccVariableDeclaration = new CreateCommentVariableDeclarationStatement();
//					
//					if (type.equalsIgnoreCase("Connection")) {
//						
//						ccVariableDeclaration.createCommentIfConnection(variableDeclarationStatement, typeAcess);
//						
//					} else if (type.equalsIgnoreCase("ResultSet")) {
//						
//						ccVariableDeclaration.createCommentIfResultSet(variableDeclarationStatement, typeAcess);
//						
//					} else if (type.equalsIgnoreCase("PreparedStatement")) {
//						
//						ccVariableDeclaration.createCommentIfPreparementStatement(variableDeclarationStatement, typeAcess);
//						
//					} else if (type.equalsIgnoreCase("Statement")) {
//						
//						ccVariableDeclaration.createCommentIfJDBCStatement(variableDeclarationStatement, typeAcess);
//						
//					} else if (type.equalsIgnoreCase("String")) {
//						
//						VariableDeclarationFragment fragment = variableDeclarationStatement.getFragments().get(0);
//						
//						Expression expression = fragment.getInitializer();
//
//						if (expression instanceof StringLiteral) {
//
//							//Class that contains all comment to be added...in the VariableDeclarationStatement
//							CreateCommentVariableDeclarationStatement createCommentFieldDeclaration = new CreateCommentVariableDeclarationStatement();
//							
//							StringLiteral stringLiteral = (StringLiteral) expression;
//
//							System.out.println("O valor do Field "
//									+ stringLiteral.getEscapedValue());
//
//							String lineSeparator = System.getProperty("line.separator")
//									+ " ";
//
//							// regex para obter somente update statement
//							Pattern patternUpdate = Pattern.compile("^\\W\\s*update",
//									Pattern.CASE_INSENSITIVE);
//
//							Matcher matcherUpdate = patternUpdate.matcher(stringLiteral
//									.getEscapedValue());
//
//							if (matcherUpdate.find()) {
//
//								this.addUpdateCommentInTheModel(stringLiteral, lineSeparator, createCommentFieldDeclaration, variableDeclarationStatement);
//
//							}
//							
							//regex para obter somente delele statement
//							Pattern patternDelete = Pattern.compile("^\\W\\s*delete", Pattern.CASE_INSENSITIVE);
//							
//							Matcher matcherDelete = patternDelete.matcher(stringLiteral
//									.getEscapedValue());
//							
//							if (matcherDelete.find()) {
//								
//								this.addDeleteCommentInTheModel(stringLiteral, lineSeparator, createCommentFieldDeclaration, variableDeclarationStatement);
//								
//							}
//							
//							//regex para obter somente select
//					    	Pattern patternSelect = Pattern.compile("^\\W\\s*select", Pattern.CASE_INSENSITIVE);
//					    	
//							Matcher matcher = patternSelect.matcher(stringLiteral
//									.getEscapedValue());
//							
//							if(matcher.find()) {
//							
//								this.addSelectCommentInTheModel(stringLiteral, lineSeparator, createCommentFieldDeclaration, variableDeclarationStatement);
//							
//							}

							// stringLiteral.setEscapedValue(stringLiteral.getEscapedValue()+"//Please remove here and use the generated DAO");

//						}
						
						
//					}
					
					
				} else if (statement instanceof TryStatement) {
					
					TryStatement tryStatement = (TryStatement) statement;
					
					System.out.println(" Aqui Ž o TRYYYY " + tryStatement);
					
					DeepInTryStatement deepInTryStatement = new DeepInTryStatement(statement.getOriginalCompilationUnit(), tryStatement);
					
					
				}
			}
			
			
		}
		
		
		
		
//		

	}

	public void createCommentOnTheJavaModel(Model model) {

		// model.`

		EList<Package> pacotes = model.getOwnedElements();

		deepInPackage(pacotes);

		// pacotes.get(0).getOwnedElements();
		//
		// //
		// EList<AbstractTypeDeclaration> classessss =
		// pacotes.get(0).getOwnedElements();
		// //
		// ClassDeclaration eusei = (ClassDeclaration)classessss.get(0);
		// //
		//
		//
		// EList<BodyDeclaration> listBodyDeclaration =
		// eusei.getBodyDeclarations();
		//
		// if (listBodyDeclaration.get(0) instanceof FieldDeclaration) {
		//
		// FieldDeclaration field = (FieldDeclaration)
		// listBodyDeclaration.get(0);
		//
		// System.out.println("Aqui dentro " +
		// field.getFragments().get(0).getInitializer());
		//
		// StringLiteral stringLiteral =
		// (StringLiteral)field.getFragments().get(0).getInitializer();
		//
		// System.out.println("O que tem aqui dentro tem que ser um SQL Statement "
		// + stringLiteral.getEscapedValue());
		//
		//
		// Comment comment = JavaFactory.eINSTANCE.createLineComment();
		//
		// field.getFragments().get(0).getInitializer().getComments().add(comment);
		//
		//
		// }
		//
		//
		//
		// System.out.println("Eu sei o que tem aqui vamos ver? " +
		// eusei.getName());
		// //
		// Comment comment1 = JavaFactory.eINSTANCE.createLineComment();
		// comment1.setContent("//eu sei que eu estou tentando ...");
		// eusei.getComments().add(comment1);
		// //
		// //
		// System.out.println("A lista de pacote contem " + pacotes);
		// //
		// EList<ClassFile> listassss = model.getClassFiles();
		//
		// System.out.println("A lista Ž " + listassss);
		//
		// for (ClassFile classFile : listassss) {
		// System.out.println("O nome da classe Ž " + classFile.getName());
		// }

	}

}
