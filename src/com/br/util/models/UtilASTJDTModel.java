package com.br.util.models;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.Document;

public class UtilASTJDTModel {

	private ArrayList<ICompilationUnit> javaClasses = new ArrayList<ICompilationUnit>();

	/**
	 * Metodo utilizado para retornar todas as classes de um determinado
	 * project.
	 * 
	 * @author rafaeldurelli
	 * @throws JavaModelException
	 *             , CoreException
	 * 
	 * */
	public ArrayList<ICompilationUnit> getAllClasses(IProject currentProjet)
			throws JavaModelException, CoreException {
		// check if we have a Java project
		if (currentProjet.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
			IJavaProject javaProject = JavaCore.create(currentProjet);
			printPackageInfos(javaProject);
		}

		System.out.println("O tamanho e " + javaClasses.size());

		return this.javaClasses;
	}
	
	
	/**
	 * Metodo utilizado para retornar um determinado ICompilationUnit tendo como entrada uma ClassUnit
	 * 
	 * @author rafaeldurelli
	 * @throws JavaModelException
	 *             , CoreException
	 * 
	 * */
	public ICompilationUnit getClassByClassUnit(ClassUnit classUnit,
			IProject currentProject, String[] packageString)
			throws JavaModelException, CoreException {

		String packageStringConcatenaed = "";

		ICompilationUnit classToBeRetorned = null;

		for (int i = 0; i < packageString.length; i++) {

			if (packageString.length - 1 == i) {

				packageStringConcatenaed += packageString[i];

			} else {
				packageStringConcatenaed += packageString[i] + ".";

			}

		}

		if (packageStringConcatenaed.equals("(default package)")) {

			packageStringConcatenaed = "";

		}

		System.out.println(packageStringConcatenaed);

		if (currentProject.isNatureEnabled("org.eclipse.jdt.core.javanature")) {

			IJavaProject javaProject = JavaCore.create(currentProject);

			IPackageFragment[] packages = javaProject.getPackageFragments();

			for (int i = 0; i < packages.length; i++) {

				if (packages[i].getElementName().equals(
						packageStringConcatenaed)) {

					ICompilationUnit[] allCompilatationUnit = packages[i]
							.getCompilationUnits();

					for (ICompilationUnit iCompilationUnit : allCompilatationUnit) {
						if (iCompilationUnit.getElementName().split(".java")[0]
								.equals(classUnit.getName())) {

							classToBeRetorned = iCompilationUnit;

							return classToBeRetorned;

						}
					}

				}

			}

		}
		return classToBeRetorned;
	}
	
	
	/***
	 * @author rafaeldurelli
	 * Esse metodo é utilizado para obter todos os attributos de uma determinada classe.
	 * 
	 * */
	public ArrayList<IField> getAllField (ICompilationUnit iCompilationUnit) throws JavaModelException {
		
		ArrayList<IField> allFields = new ArrayList<IField>();
		
		IType[] allTypes = iCompilationUnit.getAllTypes();
		
		for (int i = 0; i < allTypes.length; i++) {
			printIFieldDetails(allTypes[i], allFields);
		}
		
		
		return allFields;
	}
	
	
	/***
	 * @author rafaeldurelli
	 * Esse metodo é utilizado para obter todos os methods de uma determinada classe.
	 * 
	 * */
	public ArrayList<IMethod> getAllMethod (ICompilationUnit iCompilationUnit) throws JavaModelException {
		
		ArrayList<IMethod> allMethods = new ArrayList<IMethod>();
		
		IType[] allTypes = iCompilationUnit.getAllTypes();
		
		for (int i = 0; i < allTypes.length; i++) {
			printIMethodDetails(allTypes[i], allMethods);
		}
		
		
		return allMethods;
	}
	
	
	/***
	 * @author rafaeldurelli
	 * Esse metodo é utilizado para obter um determinado IMehod passando a classe e o nome desse método. 
	 * Por exemplo, se o método a ser recuparado é o getName(), apenas passe como string getName, sem o parenteses.
	 * 
	 * */
	public IMethod getIMethodByName (ICompilationUnit iCompilationUnit, String methodName) throws JavaModelException {
		
		ArrayList<IMethod> allMethods = this.getAllMethod(iCompilationUnit);
		
		for (IMethod iMethod : allMethods) {
			
			if (iMethod.getElementName().equals(methodName)) {
				
				return iMethod;
			}
			
		}
		
		return null;
		
	}
	
	/***
	 * @author rafaeldurelli
	 * Esse metodo é utilizado para obter o número que linhas que uma determinada classe tem.
	 * 
	 * */
	public Integer getNumberSourceLinesOfCodeOfAClass(ICompilationUnit iCompilationUnit)
			throws JavaModelException {

		Integer lineOfCode = 0;

		if (iCompilationUnit != null) {

			Document document = new Document(iCompilationUnit.getSource());

			lineOfCode = document.getNumberOfLines();

		}

		return lineOfCode;
	}
	
	
	/***
	 * @author rafaeldurelli
	 * Esse metodo é utilizado para obter o número que linhas que um determinado método tem.
	 * 
	 * */
	public Integer getNumberSourceLinesOfAMethod(IMethod method)
			throws JavaModelException {

		Integer lineOfCode = 0;

		if (method != null) {

			Document document = new Document(method.getSource());

			lineOfCode = document.getNumberOfLines();

		}

		return lineOfCode;

	}
	
	private void printIMethodDetails(IType type, ArrayList<IMethod> methodToGet) throws JavaModelException {
	    IMethod[] methods = type.getMethods();
	    for (IMethod method : methods) {
	    	
	    	methodToGet.add(method);
	    	
//	    Document doc = new Document(method.getSource());
//	    System.out.println("Numero de linha do metodo " + doc.getNumberOfLines());
//	      System.out.println("Method name " + method.getElementName());
//	      System.out.println("Signature " + method.getSignature());
//	      System.out.println("Return Type " + method.getReturnType());

	    }
	  }
	
	private void printIFieldDetails(IType type, ArrayList<IField> fieldToGet) throws JavaModelException {
	    IField[] fields = type.getFields();
	   
	    for (IField field : fields) {
	    	
	    	fieldToGet.add(field);
	    	
//	    Document doc = new Document(method.getSource());
//	    System.out.println("Numero de linha do metodo " + doc.getNumberOfLines());
//	      System.out.println("Method name " + method.getElementName());
//	      System.out.println("Signature " + method.getSignature());
//	      System.out.println("Return Type " + method.getReturnType());

	    }
	  }

	private void printPackageInfos(IJavaProject javaProject)
			throws JavaModelException {
		IPackageFragment[] packages = javaProject.getPackageFragments();
		for (IPackageFragment mypackage : packages) {
			// Package fragments include all packages in the
			// classpath
			// We will only look at the package from the source
			// folder
			// K_BINARY would include also included JARS, e.g.
			// rt.jar
			if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
				// System.out.println("Package " + mypackage.getElementName());
				printICompilationUnitInfo(mypackage);

			}

		}
	}

	private void printICompilationUnitInfo(IPackageFragment mypackage)
			throws JavaModelException {
		for (ICompilationUnit unit : mypackage.getCompilationUnits()) {

			printCompilationUnitDetails(unit);

		}
	}

	private void printCompilationUnitDetails(ICompilationUnit unit)
			throws JavaModelException {
		
//		IJavaElement elementeJava = (IJavaElement) unit;
//		
//		System.out.println(elementeJava.getElementType() == IJavaElement.METHOD);
		
		javaClasses.add(unit);
//		System.out.println("Source file " + unit.getElementName());
//		Document doc = new Document(unit.getSource());//para obter o numero de linha de codigo de uma determinada classe
		// System.out.println("Has number of lines: " + doc.getNumberOfLines());
		// printIMethods(unit);
	}

}
