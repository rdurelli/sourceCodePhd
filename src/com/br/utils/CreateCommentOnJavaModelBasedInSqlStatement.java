package com.br.utils;

import groovy.text.SimpleTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gmt.modisco.java.AbstractTypeDeclaration;
import org.eclipse.gmt.modisco.java.Annotation;
import org.eclipse.gmt.modisco.java.AnnotationTypeDeclaration;
import org.eclipse.gmt.modisco.java.BodyDeclaration;
import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.ClassFile;
import org.eclipse.gmt.modisco.java.Comment;
import org.eclipse.gmt.modisco.java.Expression;
import org.eclipse.gmt.modisco.java.FieldDeclaration;
import org.eclipse.gmt.modisco.java.MethodDeclaration;
import org.eclipse.gmt.modisco.java.Model;
import org.eclipse.gmt.modisco.java.Package;
import org.eclipse.gmt.modisco.java.StringLiteral;
import org.eclipse.gmt.modisco.java.Type;
import org.eclipse.gmt.modisco.java.TypeAccess;
import org.eclipse.gmt.modisco.java.VariableDeclarationFragment;
import org.eclipse.gmt.modisco.java.emf.JavaFactory;
import org.eclipse.swt.widgets.Shell;
import org.jruby.javasupport.Java;

import parser.StatementDelete;
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

						this.deepInFieldDeclaration(fieldDeclaration);

					} else if (bodyDeclaration instanceof MethodDeclaration) {

						MethodDeclaration methodDeclaration = (MethodDeclaration) bodyDeclaration;

						this.deepInMethodDeclaration(methodDeclaration);

						System.out.println("Yeap i'am a Method");

					}

				}

			}

		}

	}

	private void deepInFieldDeclaration(FieldDeclaration fieldDeclaration) {

		TypeAccess typeOfTheFiedl = fieldDeclaration.getType();

		if (typeOfTheFiedl.getType().getName().equalsIgnoreCase("String")) {

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

					String newUpdate = stringLiteral.getEscapedValue().trim()
							.replaceFirst("\\s*update", "update")
							.replace('"', ' ')
							.replaceAll("\\+" + lineSeparator, "")
							.replaceAll("\\s+", " ")
							.replaceAll("\\+\\D*?\\+", " ")
							+ ";";

					System.out.println("The new UPDATE is " + newUpdate);

					StatementUpdate statementUpdate = new StatementUpdate();

					String tableName = statementUpdate.getTableName(newUpdate);

					// this.cleanComment(fieldDeclaration);//limpa todos os
					// comentarios antes de adicionar o meu.

					List<Comment> comments = createCommentDAOUpdate(tableName,
							fieldDeclaration);

					for (Comment comment : comments) {
						fieldDeclaration.getComments().add(comment);
					}

				}
				
				//regex para obter somente delele statement
				Pattern patternDelete = Pattern.compile("^\\W\\s*delete", Pattern.CASE_INSENSITIVE);
				
				Matcher matcherDelete = patternDelete.matcher(stringLiteral
						.getEscapedValue());
				
				if (matcherDelete.find()) {
					
					String newDelete = stringLiteral.getEscapedValue().trim().replaceFirst("\\s*delete", "delete").replace('"', ' ').replaceAll("\\+"+lineSeparator, "").replaceAll("\\s+", " ").replaceAll("\\+\\D*?\\+", " ")+";";
					
					StatementDelete statementDelete = new StatementDelete();
					
					String tableName = statementDelete.getTableName(newDelete);
					
					List<Comment> comments = createCommentDAODelete(tableName,
							fieldDeclaration);

					for (Comment comment : comments) {
						fieldDeclaration.getComments().add(comment);
					}
					
				}

				// stringLiteral.setEscapedValue(stringLiteral.getEscapedValue()+"//Please remove here and use the generated DAO");

			}

		}

		System.out.println("The Type is.....");
		System.out.println("The name is " + typeOfTheFiedl.getType().getName());

	}

	private void cleanComment(FieldDeclaration fieldDeclaration) {

		EList<Comment> comments = fieldDeclaration.getComments();

		for (int i = 0; i < comments.size(); i++) {
			comments.remove(i);
		}

	}

	
	private List<Comment> createCommentDAODelete (String tableOfTheDeleteStatement, FieldDeclaration field) {
		
		List<Comment> comments = new ArrayList<Comment>();

		Comment newComment_1 = this.createLineComment("//*******************************************************************************",field);
		Comment newComment_2 = this.createLineComment("// Please look at the above SQL DELETE statement and then see where it is been used.      ",field);
		Comment newComment_3 = this.createLineComment("// Furthermore, remove it and use the generated code. For instance:				 ",field);
		Comment newComment1 = this.createLineComment("//(i) Firstly create an instance of the "+ tableOfTheDeleteStatement.substring(0, 1).toUpperCase() + tableOfTheDeleteStatement.substring(1).toLowerCase()+ " object ", field);
		Comment newComment2 = this.createLineComment("//(ii) Secondly, set all attributes of this object that you would like to delete",field);
		Comment newComment3 = this.createLineComment("//(iii) Thirdly, create an instance of the "+ "JDBC"+ tableOfTheDeleteStatement.substring(0, 1).toUpperCase()+ tableOfTheDeleteStatement.substring(1).toLowerCase()+ "DAO object", field);
		Comment newComment4 = this.createLineComment("//(iv) Fourthly, call the method delete", field);
		Comment newComment5 = this.createLineComment("//Source-code example:",field);
		Comment newComment6 = this.createLineComment("//	"+ tableOfTheDeleteStatement.substring(0, 1).toUpperCase()+ tableOfTheDeleteStatement.substring(1).toLowerCase()+ " arg = new "+ tableOfTheDeleteStatement.substring(0, 1).toUpperCase()+ tableOfTheDeleteStatement.substring(1).toLowerCase() + "();",field);
		Comment newComment7 = this.createLineComment("//	arg.setSomething();",field);
		Comment newComment8 = this.createLineComment("//	JDBC"+ tableOfTheDeleteStatement.substring(0, 1).toUpperCase()+ tableOfTheDeleteStatement.substring(1).toLowerCase()+ "DAO argDAO = new " + "JDBC"+ tableOfTheDeleteStatement.substring(0, 1).toUpperCase()+ tableOfTheDeleteStatement.substring(1).toLowerCase()+ "DAO();	", field);
		Comment newComment9 = this.createLineComment("//	argDAO.delete(arg);",field);
		Comment newComment10 = this.createLineComment("//********************************************************************************",field);

		comments.add(newComment_1);
		comments.add(newComment_2);
		comments.add(newComment_3);
		comments.add(newComment1);
		comments.add(newComment2);
		comments.add(newComment3);
		comments.add(newComment4);
		comments.add(newComment5);
		comments.add(newComment6);
		comments.add(newComment7);
		comments.add(newComment8);
		comments.add(newComment9);
		comments.add(newComment10);

		return comments;
	}
	
	private List<Comment> createCommentDAOUpdate(
			String tableOfTheUpdataStatement, FieldDeclaration field) {

		List<Comment> comments = new ArrayList<Comment>();

		Comment newComment_1 = this
				.createLineComment(
						"//*******************************************************************************",
						field);
		Comment newComment_2 = this
				.createLineComment(
						"// Please look at the above SQL UPDATE statement and then see where it is been used.      ",
						field);
		Comment newComment_3 = this
				.createLineComment(
						"// Furthermore, remove it and use the generated code. For instance:				 ",
						field);
		Comment newComment1 = this.createLineComment(
				"//(i) Firstly create an instance of the "
						+ tableOfTheUpdataStatement.substring(0, 1)
								.toUpperCase()
						+ tableOfTheUpdataStatement.substring(1).toLowerCase()
						+ " object ", field);
		Comment newComment2 = this
				.createLineComment(
						"//(ii) Secondly, set all attributes of this object that you would like to update",
						field);
		Comment newComment3 = this.createLineComment(
				"//(iii) Thirdly, create an instance of the "
						+ "JDBC"
						+ tableOfTheUpdataStatement.substring(0, 1)
								.toUpperCase()
						+ tableOfTheUpdataStatement.substring(1).toLowerCase()
						+ "DAO object", field);
		Comment newComment4 = this.createLineComment(
				"//(iv) Fourthly, call the method update", field);
		Comment newComment5 = this.createLineComment("//Source-code example:",
				field);
		Comment newComment6 = this.createLineComment("//	"
				+ tableOfTheUpdataStatement.substring(0, 1).toUpperCase()
				+ tableOfTheUpdataStatement.substring(1).toLowerCase()
				+ " arg = new "
				+ tableOfTheUpdataStatement.substring(0, 1).toUpperCase()
				+ tableOfTheUpdataStatement.substring(1).toLowerCase() + "();",
				field);
		Comment newComment7 = this.createLineComment("//	arg.setSomething();",
				field);
		Comment newComment8 = this.createLineComment("//	JDBC"
				+ tableOfTheUpdataStatement.substring(0, 1).toUpperCase()
				+ tableOfTheUpdataStatement.substring(1).toLowerCase()
				+ "DAO argDAO = new " + "JDBC"
				+ tableOfTheUpdataStatement.substring(0, 1).toUpperCase()
				+ tableOfTheUpdataStatement.substring(1).toLowerCase()
				+ "DAO();	", field);
		Comment newComment9 = this.createLineComment("//	argDAO.update(arg);",
				field);
		Comment newComment10 = this
				.createLineComment(
						"//********************************************************************************",
						field);

		comments.add(newComment_1);
		comments.add(newComment_2);
		comments.add(newComment_3);
		comments.add(newComment1);
		comments.add(newComment2);
		comments.add(newComment3);
		comments.add(newComment4);
		comments.add(newComment5);
		comments.add(newComment6);
		comments.add(newComment7);
		comments.add(newComment8);
		comments.add(newComment9);
		comments.add(newComment10);

		return comments;
	}

	private Comment createLineComment(String comment, FieldDeclaration field) {

		Comment newComment = JavaFactory.eINSTANCE.createLineComment();

		newComment.setContent(comment);
		newComment.setOriginalCompilationUnit(field
				.getOriginalCompilationUnit());

		return newComment;

	}

	private void deepInMethodDeclaration(MethodDeclaration methodDeclaration) {

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
