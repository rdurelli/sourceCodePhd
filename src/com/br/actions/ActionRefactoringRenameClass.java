package com.br.actions;

import java.awt.Window;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmt.modisco.infra.browser.MoDiscoBrowserPlugin;
import org.eclipse.gmt.modisco.infra.browser.editors.EcoreBrowser;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeModel;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Segment;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import com.br.gui.refactoring.Refactoring;
import com.br.gui.refactoring.RefactoringName;
import com.br.gui.refactoring.RefactoringNameWizard;
import com.br.util.models.UtilKDMModel;
import com.restphone.jrubyeclipse.Activator;

public class ActionRefactoringRenameClass implements IObjectActionDelegate {

	
	private Shell shell;
	
	public ActionRefactoringRenameClass() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(IAction action) {
	
	      System.out.println("FEITO");
		
		
			
			IEditorPart editorPart = org.eclipse.modisco.kdm.source.extension.Activator.getDefault().getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.getActiveEditor();
			
			
			System.out.println(editorPart.getTitle());
			
			System.out.println(editorPart);
			
			System.out.println(editorPart.getClass());
			
			if (editorPart instanceof AbstractTextEditor) {
				
				
				System.out.println("Sim....");
				
			} else if (editorPart instanceof EcoreBrowser) {
				
				System.out.println("Chegou aqui mesmo");
				
				StructuredSelection offset = null;
				IEditorSite iEditorSite = editorPart.getEditorSite();
				if (iEditorSite != null) {
					//get selection provider
					ISelectionProvider selectionProvider = iEditorSite
							.getSelectionProvider();
					if (selectionProvider != null) {
						
						
						System.out.println("Chamou aqui");
						
						
						 IFileEditorInput input = (IFileEditorInput)editorPart.getEditorInput() ;
						    IFile file = input.getFile();
						    IProject activeProject = file.getProject();
						    String activeProjectName = activeProject.getName();
						
						    String URIProject = activeProject.getLocationURI().toString();
						    
						    System.out.println("Location URI " + activeProject.getLocationURI().toString());
						    
						    System.out.println("O nome do Projeto � " + activeProjectName);
						
						ISelection iSelection = selectionProvider
								.getSelection();
					
						
					
						//offset
						offset = ((StructuredSelection) iSelection);
									
						if ( offset.getFirstElement() instanceof ClassUnit)  {
							
							ClassUnit classUnit = (ClassUnit)offset.getFirstElement();	
						
							Segment segment = getSegmentToPersiste(classUnit);
							
							WizardDialog wizardDialog = new WizardDialog(shell, new RefactoringNameWizard(classUnit.getName(), classUnit));
							
							if (wizardDialog.open() == org.eclipse.jface.window.Window.OK) {
								
								System.out.println("Ok pressed..");
								
							} else {
								
								System.out.println("Cancel pressed..");
								
							}
							
							UtilKDMModel utilKDM = new UtilKDMModel();
							
							
							
							Resource resource = utilKDM.save(segment, offset.toString(), URIProject);
							
							closeEditor(editorPart);
							
							
							IWorkspaceRoot workRoot = ResourcesPlugin.getWorkspace().getRoot();
						
							IPath path = new Path(resource.getURI().toFileString());
							
							IFile fileToOpen = workRoot.getFileForLocation(path);
								
							refreshLocal(activeProject);					
							
							
							openEditor(fileToOpen);
							
						} else {
						
							MessageDialog.openError(shell, "Error", "Please be sure you have selected a class to rename.");
							
							
						}
						
						
					}
				}
				
			}
		
		

	}
	
	private Segment getSegmentToPersiste(ClassUnit classUnit) {
		
		Segment segment = null;
		
		EObject eObject = classUnit.eContainer();
		
		while (segment == null) {
			
			
			
			
			if (eObject instanceof Segment) {
				
				segment = (Segment) eObject;
				
			} else if ( eObject instanceof Package)
			{
				
				Package packageKDM = (Package) eObject;
				
				eObject = packageKDM.eContainer();
				
				System.out.println(eObject);
				
			} else if (eObject instanceof CodeModel) {
				
				
				CodeModel codeModelKDM = (CodeModel) eObject;
				
				eObject = codeModelKDM.eContainer();
				
			}
			
			
		}
		
		return segment;
	}
	
	private void closeEditor (IEditorPart editorPart) {
		
		
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(editorPart, true);
		
	}
	
	private void openEditor (IFile fileToOpen) {
		
		 IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			
			try {
				IDE.openEditor(page, fileToOpen);
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
	}
	
	private void refreshLocal (IProject project) {
		
		try {
			ResourcesPlugin.getWorkspace().getRoot().getProject(project.getName()).refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}					
		
	}
	
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		
		shell = targetPart.getSite().getShell();


	}

}
