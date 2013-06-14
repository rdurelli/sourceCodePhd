package com.br.utils;

import java.util.List;

import org.eclipse.gmt.modisco.java.Comment;
import org.eclipse.gmt.modisco.java.FieldDeclaration;
import org.eclipse.gmt.modisco.java.VariableDeclarationStatement;

public interface CommentsInTheModelFieldDeclaration {
	
	
	
	public List<Comment> createCommentDAODelete (String tableOfTheDeleteStatement, FieldDeclaration field) ;
	
	public List<Comment> createCommentDAOUpdate(String tableOfTheUpdataStatement, FieldDeclaration field) ;
	
	public List<Comment> createCommentDAOSelect(String tableOfTheUpdataStatement, FieldDeclaration field) ;
		
	

}
