package com.br.actions;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.br.tests.Generation;

public class ApplyAllRefactoringToTheSourceCode implements IObjectActionDelegate {

	
	private Shell shell;

	private IJavaProject file;
	
	public ApplyAllRefactoringToTheSourceCode() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(IAction action) {
		
		Generation generation = new Generation();
		
		IFolder folderToPutTheNewSourceCodeRefactored = createFolderToPutTheUMLDiagram(this.file.getProject());
		
		IFile javaModelRefactored = getJavaModelToTransform();
		
		
			try {
				
				generation.generate(folderToPutTheNewSourceCodeRefactored.getLocation().toString(), javaModelRefactored.getLocation().toString());
				
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				this.file.getProject().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor() );
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//InputDialog inputDialog = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
			//inputDialog.open();
			
		
		

	}
	
	
	private IFolder createFolderToPutTheUMLDiagram (IProject project) {
		
		IProgressMonitor progressMonitor = new NullProgressMonitor();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IFolder firstFolder = project.getFolder("src-gen-refactored");
		if (!firstFolder.exists()){
			try {
				firstFolder.create(true, true, progressMonitor);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return firstFolder;
	}
	
	private IFile getJavaModelToTransform () {
		
		 IFile javaModelRefactored = file.getProject().getFolder("MODELS_PIM_modificado").getFile("JavaModelRefactoring.javaxmi");
		 return javaModelRefactored;
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
