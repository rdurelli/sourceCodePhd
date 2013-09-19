package com.br.actions;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.gmt.modisco.java.AbstractTypeDeclaration;
import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.ClassFile;
import org.eclipse.gmt.modisco.java.Comment;
import org.eclipse.gmt.modisco.java.Model;
import org.eclipse.gmt.modisco.java.Package;
import org.eclipse.gmt.modisco.java.emf.JavaFactory;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.modisco.infra.discovery.core.exception.DiscoveryException;
import org.eclipse.modisco.java.discoverer.DiscoverJavaModelFromJavaProject;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.br.tests.Generation;
import com.br.utils.CreateCommentOnJavaModelBasedInSqlStatement;

public class IdentifyJavaModel implements IObjectActionDelegate {

	
	private Shell shell;

	private IJavaProject file;
	
	
	
	public IdentifyJavaModel() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(IAction action) {
		// TODO Auto-generated method stub

		
		String locationURIoFTheProject = this.file.getResource().getLocationURI().toString();
		
		
		try {
			IResource resource = this.file.getUnderlyingResource();
			
			IMarker marker = resource.createMarker(IMarker.TASK);
			marker.setAttribute(IMarker.MESSAGE, "This a a task");
			marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
			marker.setAttribute(IMarker.LOCATION, this.file.getPath().toOSString());
//			marker.setAttribute(IMarker., value)
			
			IMarker marker2 = resource.createMarker(IMarker.PROBLEM);
			marker2.setAttribute(IMarker.MESSAGE, "This a Problem");
			marker2.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
			marker2.setAttribute(IMarker.LOCATION, this.file.getPath().toOSString());

//			marker.setAttribute(IMarker., value)
			
			
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		System.out.println("Location is " + locationURIoFTheProject);
		
		System.out.println(" O arquivo selecionado Ž  " + this.file.getPath());
		
		Generation generation = new Generation();
//		
		Model model = generation.loadTeste();
		
		EList<Package> pacotes = model.getOwnedElements();
		
//		
		EList<AbstractTypeDeclaration> classessss = pacotes.get(0).getOwnedElements();
//		
		ClassDeclaration eusei = (ClassDeclaration)classessss.get(0);
//		
		System.out.println("Eu sei o que tem aqui vamos ver? " + eusei.getName());
//		
		Comment comment1  = JavaFactory.eINSTANCE.createLineComment();
		comment1.setContent("//eu sei que eu estou tentando ...");
		eusei.getComments().add(comment1);
//		
//		
		System.out.println("A lista de pacote contem " + pacotes);
//		
		EList<ClassFile> listassss = model.getClassFiles();
		
		System.out.println("A lista Ž " + listassss);
		
		for (ClassFile classFile : listassss) {
			System.out.println("O nome da classe Ž " + classFile.getName());
		}
		
		
		
		
		
		CreateCommentOnJavaModelBasedInSqlStatement createComment = new CreateCommentOnJavaModelBasedInSqlStatement();
		
		createComment.createCommentOnTheJavaModel(model);
		
		
		
		System.out.println("Esta agora criando um novo modelo jAva");
		generation.save(model);
		
		
		try {
			generation.generate();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("DEU CERTO VAI...");
		
		//call createJavaModel method to create the Java model
		
		createJavaModel();
		
		
		
	}
	
	
	
	private void createJavaModel (){
		
		String locationURIoFTheProject = this.file.getResource().getLocationURI().toString();
		
		
		DiscoverJavaModelFromJavaProject discoverJava = new DiscoverJavaModelFromJavaProject();
		try {
			discoverJava.discoverElement(this.file, new NullProgressMonitor());
			
			System.out.println("FUNCIONOU O java Discover");
			
			Resource javaResource = discoverJava.getTargetModel();
			
			javaResource.setURI(URI.createURI(locationURIoFTheProject+"/MODELS_PSM_AS_IS/javaModel.javaxmi"));
			
			
			// Now save the content.
		    try {
		      
		    	javaResource.save(Collections.EMPTY_MAP);
		    	
		    	MessageDialog.openInformation(null, "Sucess.", "The JavaModel was created.");
		      
		    } catch (IOException e) {
		    	MessageDialog.openError(null, "It was not possible to create the JavaModel.", "Please vefiry what happened.");
		    	e.printStackTrace();
		    }

			
		} catch (DiscoveryException e1) {
			// TODO Auto-generated catch block
			MessageDialog.openError(null, "It was not possible to create the JavaModel.", "Please vefiry what happened.");
			e1.printStackTrace();
		}
		
		
		
	}
	

	
	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {

		if (selection instanceof IStructuredSelection) {
			action.setEnabled(updateSelection((IStructuredSelection) selection));
		} else {
			action.setEnabled(false);
		}

	}

	public boolean updateSelection(IStructuredSelection selection) {
		for (Iterator<?> objects = selection.iterator(); objects.hasNext();) {
			Object object = AdapterFactoryEditingDomain.unwrap(objects.next());
			if (object instanceof IJavaProject) {
				this.file = (IJavaProject) object;
				return true;
			}
		}
		return false;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();

	}

}
