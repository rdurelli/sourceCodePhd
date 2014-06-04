package com.br.actions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.br.parser.refactoring.CreateModelRefactoringProgrammatically;
import com.br.refactoring.dsl.refactoring.Attribute;
import com.br.refactoring.dsl.refactoring.Class;
import com.br.refactoring.dsl.refactoring.ClassType;
import com.br.refactoring.dsl.refactoring.Method;
import com.br.refactoring.dsl.refactoring.Model;
import com.br.refactoring.dsl.refactoring.RefactoringFactory;
import com.br.refactoring.dsl.refactoring.Type;

public class InitializeFileRefactoring implements IObjectActionDelegate {

	private Shell shell;

	private IJavaProject file;
	
	public InitializeFileRefactoring() {

	}

	@Override
	public void run(IAction action) {
		
 		boolean result = MessageDialog.openConfirm(this.shell, "Please Confirm", "Are you sure you would like to create the file .refactoring?");

		if(result) {
		
			String nameOfProject = this.file.getElementName();

			String locationURIoFTheProject = this.file.getResource().getLocationURI().toString();
			
			System.out.println("Name of Project " + nameOfProject);

			IFolder folderSRC = this.file.getProject().getFolder("src");

			System.out.println("Name of the FOLDER " + folderSRC.getName());

			try {
				IResource member[] = folderSRC.members();
				System.out.println(member.length);
				
				ArrayList<String> javaFiles = new ArrayList<String>();
				
				getAllJavaFile(member, javaFiles);
				
				if (javaFiles.size()==0){
					
					MessageDialog.openInformation(this.shell, "No java file was found! Please verify the project", "Error");
					
				} else {
					
					CreateModelRefactoringProgrammatically modelProgrammatically = new CreateModelRefactoringProgrammatically();
					
					StringBuffer contents = new StringBuffer();
					contents.append("model");
					contents.append(" "+ this.file.getProject().getName());
				
					
					IFile fileToCreate = this.file.getProject().getFile("/src/testePrograma.refactoring"); // such as file.exists() == false
					
					for (String fileJavaClass : javaFiles) {
						

						modelProgrammatically.callParserIFile(fileToCreate, fileJavaClass, contents);
						
					}
					
					System.out.println(contents);
					
					InputStream source = new ByteArrayInputStream(contents.toString().getBytes());
					try {
						fileToCreate.create(source, false, null);
					} catch (CoreException e1) {
	
						e1.printStackTrace();
					}
					
				}
				
			}catch (Exception e) {
			}
			
		}
		
	}
	
	private List<String> getAllJavaFile(IResource allResource[],
			List<String> listOfAllFileJava) throws CoreException {

		IFile resourceFile = null;
		IFolder resourceFolder = null;

		if (allResource.length < 1) {

			return listOfAllFileJava;

		} else {

			for (IResource iResource : allResource) {

				if (iResource instanceof IFile) {
					resourceFile = (IFile) iResource;

					if (resourceFile.getFileExtension().equals("java")) {

						listOfAllFileJava.add(resourceFile.getLocation()
								.toString());

					}

				} else if (iResource instanceof IFolder) {

					resourceFolder = (IFolder) iResource;

					getAllJavaFile(resourceFolder.members(), listOfAllFileJava);

				}
			}

			return listOfAllFileJava;
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
