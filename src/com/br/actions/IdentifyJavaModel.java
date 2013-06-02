package com.br.actions;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.modisco.infra.discovery.core.exception.DiscoveryException;
import org.eclipse.modisco.java.discoverer.DiscoverJavaModelFromJavaProject;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class IdentifyJavaModel implements IObjectActionDelegate {

	
	private Shell shell;

	private IJavaProject file;
	
	
	
	public IdentifyJavaModel() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(IAction action) {
		// TODO Auto-generated method stub

		
		
		
		System.out.println(" O arquivo selecionado Ž  " + this.file.getPath());
		
		
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
			
			javaResource.setURI(URI.createURI(locationURIoFTheProject+"/MODELS_PSM_AS_IS/teste.javaxmi"));
			
			
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
			System.out.println("N‹o FUNCIONOU O java Discover");
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
