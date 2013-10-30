package com.br.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.gmt.modisco.infra.browser.editors.EcoreBrowser;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
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
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import com.br.gui.refactoring.WizardExtract;

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
						
					
						ClassUnit classUnitToExtract = (ClassUnit) offset.getFirstElement();
						
						WizardDialog teste = new WizardDialog(shell,
								new WizardExtract(classUnitToExtract));

						teste.open();
						
						
					} else {
						
						
						MessageDialog.openError(shell, "Error", "Please be sure you have selected a ClassUnit to extract another ClassUnit.");
					}
					
					

				}
			}
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
