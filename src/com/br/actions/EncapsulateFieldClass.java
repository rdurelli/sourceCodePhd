package com.br.actions;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmt.modisco.infra.browser.editors.EcoreBrowser;
import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.FieldDeclaration;
import org.eclipse.gmt.modisco.java.MethodDeclaration;
import org.eclipse.gmt.modisco.java.Model;
import org.eclipse.gmt.modisco.java.VisibilityKind;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.MethodUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableUnit;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Attribute;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Segment;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
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

import com.br.trace.refactoring.PersisteTraceLogRefactoring;
import com.br.util.models.UtilJavaModel;
import com.br.util.models.UtilKDMModel;

public class EncapsulateFieldClass implements IObjectActionDelegate {

	private Shell shell;
	
	public EncapsulateFieldClass() {
		
	}

	@Override
	public void run(IAction action) {
		
		UtilKDMModel utilKDMMODEL = new UtilKDMModel();
		UtilJavaModel utilJavaModel =  new UtilJavaModel();
		
		
		IEditorPart editorPart = org.eclipse.modisco.kdm.source.extension.Activator
				.getDefault().getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getActiveEditor();

		if (editorPart instanceof AbstractTextEditor) {

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


					ISelection iSelection = selectionProvider.getSelection();

					// offset
					offset = ((StructuredSelection) iSelection);
					
					Object objectSelected = offset.getFirstElement();
					
					StorableUnit storableUnitToApplyTheEncapsulateField = null;
					
					if (! (objectSelected instanceof StorableUnit)) { //verifica se o ELEMENTE selecionado Ž um StorableUnit
						
						MessageDialog.openError(shell, "Error", "Please be sure you have selected at a StorableUnit to realize the Encapsulate Field.");
						
					} else {
						
						
						
						Model modelJava = utilJavaModel.load(activeProject.getLocationURI().toString()+"/MODELS_PIM_modificado/JavaModelRefactoring.javaxmi");
						
						storableUnitToApplyTheEncapsulateField = (StorableUnit) objectSelected;//caso o Objeto selecionado Ž um Object do tipo StorableUnit ent‹o faz o CAST
						
						ClassUnit classThatContainTheStorableUnit = (ClassUnit) storableUnitToApplyTheEncapsulateField.eContainer(); //Obtem a ClassUnit que possui um determinado StorableUnit
						
						utilKDMMODEL.actionEncapsulateField(classThatContainTheStorableUnit, storableUnitToApplyTheEncapsulateField, modelJava);
						
						Segment segment = utilKDMMODEL.getSegmentToPersiste(classThatContainTheStorableUnit);
						
						
						
						Resource resource = utilKDMMODEL.save(segment,
								offset.toString(), URIProject);

						closeEditor(editorPart);

						IWorkspaceRoot workRoot = ResourcesPlugin
								.getWorkspace().getRoot();

						IPath path = new Path(resource.getURI().toFileString());

						IFile fileToOpen = workRoot.getFileForLocation(path);

						refreshLocal(activeProject);

						openEditor(fileToOpen);

						utilJavaModel.save(modelJava, URIProject);
						
						PersisteTraceLogRefactoring.saveTrace(activeProjectName, "EncapsulateField", "Rafael Durelli");

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

	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();

	}

}
