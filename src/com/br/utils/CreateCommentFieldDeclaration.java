package com.br.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gmt.modisco.java.Comment;
import org.eclipse.gmt.modisco.java.FieldDeclaration;
import org.eclipse.gmt.modisco.java.TypeAccess;
import org.eclipse.gmt.modisco.java.VariableDeclarationStatement;
import org.eclipse.gmt.modisco.java.emf.JavaFactory;

public class CreateCommentFieldDeclaration extends CreateComment implements CommentsInTheModelFieldDeclaration{

	
	
	@Override
	public void createCommentIfConnection(
			FieldDeclaration  variable, TypeAccess type) {
		
		Comment newCommentVariableDeclarationStatement = this.createLineComment("// " +type.getType().getName() +" "+ variable.getFragments().get(0).getName() + " is a possible concern to be removed ");
		variable.getComments().add(newCommentVariableDeclarationStatement);
		
		
//		n‹o usar n‹o esta funcionando verificar o motivo
//		Comment newCommentType = this.createLineComment("// " + type.getType().getName() + " is a possible concern to be removed ", variable);
//		type.getComments().add(newCommentType);
		
		
	}

	@Override
	public void createCommentIfResultSet(
			FieldDeclaration variable, TypeAccess type) {
		
		Comment newCommentVariableDeclarationStatement = this.createLineComment("// " +type.getType().getName() +" "+ variable.getFragments().get(0).getName() + " is a possible concern to be removed ");
		variable.getComments().add(newCommentVariableDeclarationStatement);
//		
//		n‹o usar n‹o esta funcionando verificar o motivo
//		Comment newCommentType = this.createLineComment("// " + type.getType().getName() + " is a possible concern to be removed", variable);
//		type.getComments().add(newCommentType);
//		
		
	
	}

	@Override
	public void createCommentIfPreparementStatement(
			FieldDeclaration variable, TypeAccess type) {
		
		
		Comment newCommentVariableDeclarationStatement = this.createLineComment("// " +type.getType().getName() +" "+ variable.getFragments().get(0).getName() + " is a possible concern to be removed ");
		variable.getComments().add(newCommentVariableDeclarationStatement);
//		n‹o usar n‹o esta funcionando verificar o motivo
//		Comment newCommentType = this.createLineComment("// " + type.getType().getName() + " is a possible concern to be removed");
//		type.getComments().add(newCommentType);
//		

	}

	@Override
	public void createCommentIfJDBCStatement(
			FieldDeclaration variable, TypeAccess type) {
		
		Comment newCommentVariableDeclarationStatement = this.createLineComment("// " +type.getType().getName() +" "+ variable.getFragments().get(0).getName() + " is a possible concern to be removed ");
		variable.getComments().add(newCommentVariableDeclarationStatement);
		
		//n‹o usar n‹o esta funcionando verificar o motivo
//		Comment newCommentType = this.createLineComment("// " + type.getType().getName() + " is a possible concern to be removed", variable);
//		type.getComments().add(newCommentType);
		
	}
	
	
	@Override
	public List<Comment> createCommentDAODelete (String tableOfTheDeleteStatement, FieldDeclaration field) {
		
		List<Comment> comments = new ArrayList<Comment>();

		//eu removi o jeito que colocava os coment‡rios antes no c—digo-fonte agora eu coloco o apenas uma coment‡rio.. veja \\KDM DELETE e o nome da tabela..
		//eu removi o jeito que colocava os coment‡rios antes no c—digo-fonte agora eu coloco o apenas uma coment‡rio.. veja \\KDM DELETE e o nome da tabela..
		
		Comment commentDelete = this.createLineComment("//KDM DELETE "+ tableOfTheDeleteStatement.substring(0, 1).toUpperCase() + tableOfTheDeleteStatement.substring(1).toLowerCase(), field);
		
//		Comment newComment_1 = this.createLineComment("//*******************************************************************************",field);
//		Comment newComment_2 = this.createLineComment("// Please look at the above SQL DELETE statement and then see where it is been used.      ",field);
//		Comment newComment_3 = this.createLineComment("// Furthermore, remove it and use the generated code. For instance:				 ",field);
//		Comment newComment1 = this.createLineComment("//(i) Firstly create an instance of the "+ tableOfTheDeleteStatement.substring(0, 1).toUpperCase() + tableOfTheDeleteStatement.substring(1).toLowerCase()+ " object ", field);
//		Comment newComment2 = this.createLineComment("//(ii) Secondly, set all attributes of this object that you would like to delete",field);
//		Comment newComment3 = this.createLineComment("//(iii) Thirdly, create an instance of the "+ "JDBC"+ tableOfTheDeleteStatement.substring(0, 1).toUpperCase()+ tableOfTheDeleteStatement.substring(1).toLowerCase()+ "DAO object", field);
//		Comment newComment4 = this.createLineComment("//(iv) Fourthly, call the method delete", field);
//		Comment newComment5 = this.createLineComment("//Source-code example:",field);
//		Comment newComment6 = this.createLineComment("//	"+ tableOfTheDeleteStatement.substring(0, 1).toUpperCase()+ tableOfTheDeleteStatement.substring(1).toLowerCase()+ " arg = new "+ tableOfTheDeleteStatement.substring(0, 1).toUpperCase()+ tableOfTheDeleteStatement.substring(1).toLowerCase() + "();",field);
//		Comment newComment7 = this.createLineComment("//	arg.setSomething();",field);
//		Comment newComment8 = this.createLineComment("//	JDBC"+ tableOfTheDeleteStatement.substring(0, 1).toUpperCase()+ tableOfTheDeleteStatement.substring(1).toLowerCase()+ "DAO argDAO = new " + "JDBC"+ tableOfTheDeleteStatement.substring(0, 1).toUpperCase()+ tableOfTheDeleteStatement.substring(1).toLowerCase()+ "DAO();	", field);
//		Comment newComment9 = this.createLineComment("//	argDAO.delete(arg);",field);
//		Comment newComment10 = this.createLineComment("//********************************************************************************",field);
//
//		comments.add(newComment_1);
//		comments.add(newComment_2);
//		comments.add(newComment_3);
//		comments.add(newComment1);
//		comments.add(newComment2);
//		comments.add(newComment3);
//		comments.add(newComment4);
//		comments.add(newComment5);
//		comments.add(newComment6);
//		comments.add(newComment7);
//		comments.add(newComment8);
//		comments.add(newComment9);
//		comments.add(newComment10);
		
		comments.add(commentDelete);
		
		return comments;
	}
	
	@Override
	public List<Comment> createCommentDAOUpdate(String tableOfTheUpdataStatement, FieldDeclaration field) {

		List<Comment> comments = new ArrayList<Comment>();

		//eu removi o jeito que colocava os coment‡rios antes no c—digo-fonte agora eu coloco o apenas uma coment‡rio.. veja \\KDM UPDATE e o nome da tabela..
		//eu removi o jeito que colocava os coment‡rios antes no c—digo-fonte agora eu coloco o apenas uma coment‡rio.. veja \\KDM UPDATE e o nome da tabela..
				
		Comment commentUPDATE = this.createLineComment("//KDM UPDATE "+ tableOfTheUpdataStatement.substring(0, 1).toUpperCase() + tableOfTheUpdataStatement.substring(1).toLowerCase(), field);
		
//		Comment newComment_1 = this.createLineComment("//*******************************************************************************",field);
//		Comment newComment_2 = this.createLineComment("// Please look at the above SQL UPDATE statement and then see where it is been used.      ",field);
//		Comment newComment_3 = this.createLineComment("// Furthermore, remove it and use the generated code. For instance:				 ",field);
//		Comment newComment1 = this.createLineComment("//(i) Firstly create an instance of the "+ tableOfTheUpdataStatement.substring(0, 1).toUpperCase()+ tableOfTheUpdataStatement.substring(1).toLowerCase()+ " object ", field);
//		Comment newComment2 = this.createLineComment("//(ii) Secondly, set all attributes of this object that you would like to update",field);
//		Comment newComment3 = this.createLineComment("//(iii) Thirdly, create an instance of the "+ "JDBC"+ tableOfTheUpdataStatement.substring(0, 1).toUpperCase()+ tableOfTheUpdataStatement.substring(1).toLowerCase()+ "DAO object", field);
//		Comment newComment4 = this.createLineComment("//(iv) Fourthly, call the method update", field);
//		Comment newComment5 = this.createLineComment("//Source-code example:",field);
//		Comment newComment6 = this.createLineComment("//	"+ tableOfTheUpdataStatement.substring(0, 1).toUpperCase()+ tableOfTheUpdataStatement.substring(1).toLowerCase()+ " arg = new "+ tableOfTheUpdataStatement.substring(0, 1).toUpperCase()+ tableOfTheUpdataStatement.substring(1).toLowerCase() + "();",field);
//		Comment newComment7 = this.createLineComment("//	arg.setSomething();",field);
//		Comment newComment8 = this.createLineComment("//	JDBC"+ tableOfTheUpdataStatement.substring(0, 1).toUpperCase()+ tableOfTheUpdataStatement.substring(1).toLowerCase()+ "DAO argDAO = new " + "JDBC"+ tableOfTheUpdataStatement.substring(0, 1).toUpperCase()+ tableOfTheUpdataStatement.substring(1).toLowerCase()+ "DAO();	", field);
//		Comment newComment9 = this.createLineComment("//	argDAO.update(arg);",field);
//		Comment newComment10 = this.createLineComment("//********************************************************************************",field);
//
//		comments.add(newComment_1);
//		comments.add(newComment_2);
//		comments.add(newComment_3);
//		comments.add(newComment1);
//		comments.add(newComment2);
//		comments.add(newComment3);
//		comments.add(newComment4);
//		comments.add(newComment5);
//		comments.add(newComment6);
//		comments.add(newComment7);
//		comments.add(newComment8);
//		comments.add(newComment9);
//		comments.add(newComment10);
		
		comments.add(commentUPDATE);

		return comments;
	}
	
	
	@Override
	public List<Comment> createCommentDAOSelect (String tableOfTheDeleteStatement, FieldDeclaration field) {
		
		List<Comment> comments = new ArrayList<Comment>();

		//eu removi o jeito que colocava os coment‡rios antes no c—digo-fonte agora eu coloco o apenas uma coment‡rio.. veja \\KDM Select e o nome da tabela..
		//eu removi o jeito que colocava os coment‡rios antes no c—digo-fonte agora eu coloco o apenas uma coment‡rio.. veja \\KDM Select e o nome da tabela..
						
		Comment commentSelect = this.createLineComment("//KDM SELECT "+ tableOfTheDeleteStatement.substring(0, 1).toUpperCase() + tableOfTheDeleteStatement.substring(1).toLowerCase(), field);
		
		
//		Comment newComment_1 = this.createLineComment("//*******************************************************************************",field);
//		Comment newComment_2 = this.createLineComment("// Please look at the above SQL SELECT statement and then see where it is been used.      ",field);
//		Comment newComment_3 = this.createLineComment("// Furthermore, remove it and use the generated code. For instance:				 ",field);
//		Comment newComment1 = this.createLineComment("//(i) Firstly create an instance of the "+ tableOfTheDeleteStatement.substring(0, 1).toUpperCase() + tableOfTheDeleteStatement.substring(1).toLowerCase()+ " object ", field);
//		Comment newComment2 = this.createLineComment("//(ii) Secondly, set all attributes of this object that you would like to select",field);
//		Comment newComment3 = this.createLineComment("//(iii) Thirdly, create an instance of the "+ "JDBC"+ tableOfTheDeleteStatement.substring(0, 1).toUpperCase()+ tableOfTheDeleteStatement.substring(1).toLowerCase()+ "DAO object", field);
//		Comment newComment4 = this.createLineComment("//(iv) Fourthly, call the method select", field);
//		Comment newComment5 = this.createLineComment("//Source-code example:",field);
//		Comment newComment6 = this.createLineComment("//	"+ tableOfTheDeleteStatement.substring(0, 1).toUpperCase()+ tableOfTheDeleteStatement.substring(1).toLowerCase()+ " arg = new "+ tableOfTheDeleteStatement.substring(0, 1).toUpperCase()+ tableOfTheDeleteStatement.substring(1).toLowerCase() + "();",field);
//		Comment newComment7 = this.createLineComment("//	arg.setSomething();",field);
//		Comment newComment8 = this.createLineComment("//	JDBC"+ tableOfTheDeleteStatement.substring(0, 1).toUpperCase()+ tableOfTheDeleteStatement.substring(1).toLowerCase()+ "DAO argDAO = new " + "JDBC"+ tableOfTheDeleteStatement.substring(0, 1).toUpperCase()+ tableOfTheDeleteStatement.substring(1).toLowerCase()+ "DAO();	", field);
//		Comment newComment9 = this.createLineComment("//	argDAO.select(arg);",field);
//		Comment newComment10 = this.createLineComment("//********************************************************************************",field);
//
//		comments.add(newComment_1);
//		comments.add(newComment_2);
//		comments.add(newComment_3);
//		comments.add(newComment1);
//		comments.add(newComment2);
//		comments.add(newComment3);
//		comments.add(newComment4);
//		comments.add(newComment5);
//		comments.add(newComment6);
//		comments.add(newComment7);
//		comments.add(newComment8);
//		comments.add(newComment9);
//		comments.add(newComment10);

		comments.add(commentSelect);
		
		return comments;
	}
	
	
	
	
	private Comment createLineComment(String comment) {

		Comment newComment = JavaFactory.eINSTANCE.createLineComment();

		newComment.setContent(comment);

		return newComment;

	}
	
	private Comment createLineComment(String comment, FieldDeclaration field) {

		Comment newComment = JavaFactory.eINSTANCE.createLineComment();

		newComment.setContent(comment);
		newComment.setOriginalCompilationUnit(field
				.getOriginalCompilationUnit());

		return newComment;

	}
	
	

}
