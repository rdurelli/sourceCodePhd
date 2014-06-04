package com.br.actions;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmt.modisco.infra.browser.editors.EcoreBrowser;
import org.eclipse.gmt.modisco.java.AbstractTypeDeclaration;
import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.Model;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Segment;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.ui.texteditor.AbstractTextEditor;

import com.br.gui.refactoring.WizardExtract;
import com.br.trace.refactoring.PersisteTraceLogRefactoring;
import com.br.util.models.UtilASTJDTModel;
import com.br.util.models.UtilJavaModel;
import com.br.util.models.UtilKDMModel;

public class ExtractClass implements IObjectActionDelegate {

	private Shell shell;

	public ExtractClass() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(IAction action) {

		IEditorPart editorPart = org.eclipse.modisco.kdm.source.extension.Activator
				.getDefault().getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getActiveEditor();

		System.out.println(editorPart.getTitle());

		System.out.println(editorPart);

		System.out.println(editorPart.getClass());

		if (editorPart instanceof AbstractTextEditor) {

			System.out.println("Sim....");

		} else if (editorPart instanceof EcoreBrowser) {

			StructuredSelection offset = null;
			IEditorSite iEditorSite = editorPart.getEditorSite();
			if (iEditorSite != null) {
				// get selection provider
				ISelectionProvider selectionProvider = iEditorSite
						.getSelectionProvider();
				if (selectionProvider != null) {


					IFileEditorInput input = (IFileEditorInput) editorPart
							.getEditorInput();
					IFile file = input.getFile();
					IProject activeProject = file.getProject();
					String activeProjectName = activeProject.getName();

					
					
					
					String URIProject = activeProject.getLocationURI()
							.toString();

					System.out.println("Location URI "
							+ activeProject.getLocationURI().toString());

					System.out.println("O nome do Projeto Ž "
							+ activeProjectName);

					ISelection iSelection = selectionProvider.getSelection();

					// offset
					offset = ((StructuredSelection) iSelection);

					
					if (offset.getFirstElement() instanceof ClassUnit) {
						
						UtilJavaModel utilJavaModel =  new UtilJavaModel(); //utilizado para ajudar a lidar com o JavaModel
						
						Model modelJava = utilJavaModel.load(activeProject.getLocationURI().toString()+"/MODELS_PIM_modificado/JavaModelRefactoring.javaxmi");
						
						ClassUnit classUnitToExtract = (ClassUnit) offset.getFirstElement();//obtem o elemento no View do Java e deve fazer um cast para transforma-lo para um ClassUnit
						
						
						UtilKDMModel utilKDMMODEL = new UtilKDMModel(); //class criada para ajudar a ligar com o KDMModel
						
						String [] packageKDM = utilKDMMODEL.getCompletePackageName(classUnitToExtract); //method utilizado para obter o nome do Pacote, deve-se passar umq classUnit.
						
						
						ClassDeclaration classDeclaration = utilJavaModel.getClassDeclaration(classUnitToExtract, packageKDM, modelJava);
						
						WizardDialog wizardExtractClass = new WizardDialog(shell,
								new WizardExtract(classUnitToExtract, classDeclaration));

						wizardExtractClass.open();
						
						
						Segment segment = utilKDMMODEL.getSegmentToPersiste(classUnitToExtract);
						
						Resource resource = utilKDMMODEL.save(segment, offset.toString(), URIProject);
						
						closeEditor(editorPart);
						
						
						IWorkspaceRoot workRoot = ResourcesPlugin.getWorkspace().getRoot();
					
						IPath path = new Path(resource.getURI().toFileString());
						
						IFile fileToOpen = workRoot.getFileForLocation(path);
							
						refreshLocal(activeProject);					
						
						
						openEditor(fileToOpen);
						
						
						Model model = utilJavaModel.getModelToPersiste(classDeclaration);
						
						utilJavaModel.save(model, URIProject);
						
						PersisteTraceLogRefactoring.saveTrace(activeProjectName, "ExtractClass", "Rafael Durelli");
						
					} else {
						
						
						MessageDialog.openError(shell, "Error", "Please be sure you have selected a ClassUnit to extract another ClassUnit.");
					}
					
					

				}
			}
		}

	}
	
	private void closeEditor(IEditorPart editorPart) {

		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.closeEditor(editorPart, true);

	}

	private void openEditor(IFile fileToOpen) {

		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();

		try {
			IDE.openEditor(page, fileToOpen);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void refreshLocal(IProject project) {

		try {
			ResourcesPlugin.getWorkspace().getRoot()
					.getProject(project.getName())
					.refreshLocal(IResource.DEPTH_INFINITE, null);
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
