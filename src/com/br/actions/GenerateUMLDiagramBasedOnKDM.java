package com.br.actions;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.gmt.modisco.infra.browser.editors.EcoreBrowser;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Segment;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import com.br.catalogue.refactorings.util.PopulateKDMIntoMemory;
import com.br.models.graphviz.Elements;
import com.br.models.graphviz.generate.image.GenerateImageFactory;

public class GenerateUMLDiagramBasedOnKDM implements IObjectActionDelegate {

	private Shell shell;

	public GenerateUMLDiagramBasedOnKDM() {
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void run(IAction action) {
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

					Segment segmentToApplyTheUMLDIAGRAM = null;

					if (!(objectSelected instanceof Segment)) {

						MessageDialog
								.openError(shell, "Error",
										"Please be sure you have selected at the Segment to generate the Diagrama UML.");

					} else {
						
						System.out.println(activeProject.getFullPath());
						System.out.println(activeProject.getLocation());
						
						segmentToApplyTheUMLDIAGRAM = (Segment) objectSelected;

						PopulateKDMIntoMemory populateKDM = new PopulateKDMIntoMemory(
								segmentToApplyTheUMLDIAGRAM);

						ArrayList<Elements> classesPopulated = populateKDM
								.getClasses();

						GenerateImageFactory generate = GenerateImageFactory
								.getInstance();
						
						
						IFolder folderTOPutTHeDiagram = createFolderToPutTheUMLDiagram(activeProject);
						
						String[] pathToGenerateTheUMLModel = {folderTOPutTHeDiagram.getLocation().toString()};
						
						generate.createClassGraphviz(classesPopulated, pathToGenerateTheUMLModel);

						try {
							activeProject.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor() );
						} catch (CoreException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						MessageDialog.openInformation(shell, "Information",
								"UML Diagram generated.");

					}
				}
			}
		}
	}

	private IFolder createFolderToPutTheUMLDiagram (IProject project) {
		
		IProgressMonitor progressMonitor = new NullProgressMonitor();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IFolder firstFolder = project.getFolder("UML_DIAGRAM");
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
	
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub

	}

}
