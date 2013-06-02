package com.br.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;


import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;

import org.eclipse.jdt.core.IJavaProject;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;


import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.m2m.atl.common.ATLExecutionException;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.sqlmodel2kdmdata.files.SQLModel2KDMData;
import org.eclipse.modisco.infra.discovery.core.exception.DiscoveryException;
import org.eclipse.modisco.java.discoverer.DiscoverJavaModelFromJavaProject;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;



import org.eclipse.ui.dialogs.ResourceSelectionDialog;

import com.br.databaseDDL.DataBase;
import com.br.utils.CreateSQLModel;

import parser.Teste1;

public class IdentifySQL implements IObjectActionDelegate {

	private Shell shell;

	private IJavaProject file;

	public IdentifySQL() {
		// TODO Auto-generated constructor stub

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

	@Override
	public void run(IAction action) {
		
		boolean result = MessageDialog.openConfirm(this.shell, "Please Confirm", "Are you sure you would like to find all embedded SQL?");
		
		String local = null;
		
		if(result) {
		
			
			
			System.out.println("COMECOU a FUNCIONAR");

			String nameOfProject = this.file.getElementName();

			String locationURIoFTheProject = this.file.getResource().getLocationURI().toString();
			
			System.out.println("Name of Project " + nameOfProject);

			IFolder folderSRC = this.file.getProject().getFolder("src");

			System.out.println("Name of the FOLDER " + folderSRC.getName());

			try {
				IResource member[] = folderSRC.members();
				System.out.println(member.length);

//				IFile file = null;
//				IFolder folder = null;

				
				System.out.println(" Caminho completo " + this.file.getResource().getLocation() );
				
				
				System.out.println(" Caminho completo Dois " + this.file.getResource().getLocationURI() );
				
				ArrayList<String> javaFiles = new ArrayList<String>();

//				DiscoverJavaModelFromJavaProject discoverJava = new DiscoverJavaModelFromJavaProject();
//				try {
//					discoverJava.discoverElement(this.file, null);
//					
//					System.out.println("FUNCIONOU O java Discover");
//					
//				} catch (DiscoveryException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				
				
				System.out.println("--------------------------------------");
				System.out.println("Somente arquivos Java ");
				System.out.println(getAllJavaFile(member, javaFiles));
				System.out.println("--------------------------------------");
				System.out.println("O array que voltou "+ javaFiles.size());
				
				if (javaFiles.size()==0){
					
					MessageDialog.openInformation(this.shell, "No java file was found! Please verify the project", "Error");
					
				}else {
					
					Teste1 teste1 = new Teste1();
					
					teste1.callParser(javaFiles.get(0));
					
					DataBase dataBase = teste1.getDataBase();
					
					CreateSQLModel.createModel(dataBase, locationURIoFTheProject);
					
					
					
					ResourcesPlugin.getWorkspace().getRoot().getProject(nameOfProject).refreshLocal(IResource.DEPTH_INFINITE, null);
					

					ResourceSelectionDialog dialog = new ResourceSelectionDialog(this.shell, ResourcesPlugin.getWorkspace().getRoot(), "Select Resource");
				    dialog.setTitle("Resource Selection");
				    dialog.open();
				    
				    IFile arquivoObtido = (IFile) dialog.getResult()[0];
				    
				    
				    
				    
				    System.out.println(arquivoObtido.getName());
				    System.out.println("Location of the file " + arquivoObtido.getLocation());
				    System.out.println("Full path of the File "+ arquivoObtido.getFullPath());
				    
				    local = arquivoObtido.getParent().getLocation().toOSString();
				    
				    System.out.println("Location of the Parente "+arquivoObtido.getParent().getLocation());
				    
				    org.eclipse.m2m.atl.sqlmodel2kdmdata.files.SQLModel2KDMData runner = null;
				    try {
				    	
				    	runner =  new org.eclipse.m2m.atl.sqlmodel2kdmdata.files.SQLModel2KDMData();
				    	runner.loadModels(arquivoObtido.getFullPath().toString());
						runner.doSQLModel2KDMData(new NullProgressMonitor());
						runner.saveModels(URI.createURI("file:"+arquivoObtido.getParent().getLocation().toString()+""+"/My2TESTENOVO.kdm").toString());
						System.out.println("Model salvo");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ATLExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ATLCoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					
				}
				
				
				
				DiscoverJavaModelFromJavaProject discoverJava = new DiscoverJavaModelFromJavaProject();
				try {
					discoverJava.discoverElement(this.file, new NullProgressMonitor());
					
					System.out.println("FUNCIONOU O java Discover");
					
					Resource javaResource = discoverJava.getTargetModel();
					
					javaResource.setURI(URI.createURI(locationURIoFTheProject+"/MODELS_PSM_AS_IS/teste.javaxmi"));
					
					
					// Now save the content.
				    try {
				      
				    	javaResource.save(Collections.EMPTY_MAP);
				    	
				      
				    } catch (IOException e) {
				    	MessageDialog.openError(null, "It was not possible to create the sqlmodel.", "Please vefiry what happened.");
				    	e.printStackTrace();
				    }
					
					
//					FileOutputStream fout = new FileOutputStream(new File (local));
//					javaResource.save(fout, null);
//					fout.close();
					
				} catch (DiscoveryException e1) {
					// TODO Auto-generated catch block
					System.out.println("N‹o FUNCIONOU O java Discover");
					e1.printStackTrace();
				}
				
				// for (IResource iResource : member) {
				// if (iResource instanceof IFile) {
				// file = (IFile) iResource;
				// System.out.println("---------------------------------------");
				// System.out.println("Nome " + file.getName());
				// System.out.println("Path " + file.getFullPath());
				// System.out.println("Location " + file.getLocation());
				// System.out.println("Extension " + file.getFileExtension());
				// System.out.println("---------------------------------------");
				// }
				// else if (iResource instanceof IFolder) {
				//
				// folder = (IFolder) iResource;
				//
				// System.out.println("---------------------------------------");
				// System.out.println("FOLDER NAME "+ folder.getName());
				// System.out.println("FOLDER NAME "+ folder.members().length);
				//
				// }
				// }

			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
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
