package com.br.utils;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gmt.modisco.java.AbstractTypeDeclaration;
import org.eclipse.gmt.modisco.java.BodyDeclaration;
import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.ClassFile;
import org.eclipse.gmt.modisco.java.Comment;
import org.eclipse.gmt.modisco.java.FieldDeclaration;
import org.eclipse.gmt.modisco.java.MethodDeclaration;
import org.eclipse.gmt.modisco.java.Model;
import org.eclipse.gmt.modisco.java.Package;
import org.eclipse.gmt.modisco.java.StringLiteral;
import org.eclipse.gmt.modisco.java.emf.JavaFactory;
import org.eclipse.swt.widgets.Shell;

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
				
				EList<BodyDeclaration> bodies = classOfTheModel.getBodyDeclarations();
				
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
	
	private void deepInFieldDeclaration (FieldDeclaration fieldDeclaration) {
		
		
	}
	
	private void deepInMethodDeclaration (MethodDeclaration methodDeclaration) {
		
		
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
