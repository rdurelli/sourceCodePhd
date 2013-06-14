package com.br.utils;

import java.util.List;

import org.eclipse.gmt.modisco.java.Comment;
import org.eclipse.gmt.modisco.java.TypeAccess;
import org.eclipse.gmt.modisco.java.VariableDeclarationStatement;

public interface CommentsInTheModelVariableDeclarationStatement {

	
	public void createCommentIfConnection (VariableDeclarationStatement variable, TypeAccess type) ;
	
	public void createCommentIfResultSet (VariableDeclarationStatement variable, TypeAccess type) ;
	
	public void createCommentIfPreparementStatement (VariableDeclarationStatement variable, TypeAccess type) ;
	
	public void createCommentIfJDBCStatement (VariableDeclarationStatement variable, TypeAccess type) ;
	
	public List<Comment> createCommentDAODelete (String tableOfTheDeleteStatement, VariableDeclarationStatement field) ;
	
	public List<Comment> createCommentDAOUpdate(String tableOfTheUpdataStatement, VariableDeclarationStatement field) ;
	
	public List<Comment> createCommentDAOSelect(String tableOfTheUpdataStatement, VariableDeclarationStatement field) ;
	
	
	
}
