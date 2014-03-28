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
						
						Attribute attributeStorableUnitToApplyTheEncapsulateField = storableUnitToApplyTheEncapsulateField.getAttribute().get(0);//utiliza esse Attribute para verificar se Ž private
						
						ClassUnit classThatContainTheStorableUnit = (ClassUnit) storableUnitToApplyTheEncapsulateField.eContainer(); //Obtem a ClassUnit que possui um determinado StorableUnit
						
						String [] packageKDM = utilKDMMODEL.getCompletePackageName(classThatContainTheStorableUnit); //obtem o nome completo dos pacotes.
						
						ClassDeclaration classDeclaration = utilJavaModel.getClassDeclaration(classThatContainTheStorableUnit, packageKDM, modelJava); //obtem uma instancia do ClassDeclaration equivalente ao ClassUnit.
						
						FieldDeclaration fieldDeclarationToApplyTheEncapsulateField =  utilJavaModel.getFieldDeclarationByName(classDeclaration, storableUnitToApplyTheEncapsulateField.getName());
						
						Segment segment = utilKDMMODEL.getSegmentToPersiste(classThatContainTheStorableUnit);
						
						if (attributeStorableUnitToApplyTheEncapsulateField.getValue().equals("private") && fieldDeclarationToApplyTheEncapsulateField.getModifier().getVisibility().equals(VisibilityKind.PRIVATE)) {
							
							boolean result = MessageDialog.openConfirm(shell, "Error", "The StorableUnit that you selected already is private. Would you like to check if it owns accessors?");
							
							if (result) {
								
								//verificar se tem get e set.
								
								System.out.println(storableUnitToApplyTheEncapsulateField.getName().substring(0, 1).toUpperCase() + storableUnitToApplyTheEncapsulateField.getName().substring(1).toLowerCase());
								
								MethodUnit methodGet = utilKDMMODEL.getMethodsUnitByName(classThatContainTheStorableUnit, "get"+storableUnitToApplyTheEncapsulateField.getName().substring(0, 1).toUpperCase() + storableUnitToApplyTheEncapsulateField.getName().substring(1).toLowerCase());
								
								MethodUnit methodSet = utilKDMMODEL.getMethodsUnitByName(classThatContainTheStorableUnit, "set"+storableUnitToApplyTheEncapsulateField.getName().substring(0, 1).toUpperCase() + storableUnitToApplyTheEncapsulateField.getName().substring(1).toLowerCase());
								
								MethodDeclaration methodDeclarationGET = utilJavaModel.getMethodDeclarationByName(classDeclaration, "get"+storableUnitToApplyTheEncapsulateField.getName().substring(0, 1).toUpperCase() + storableUnitToApplyTheEncapsulateField.getName().substring(1).toLowerCase());
								
								MethodDeclaration methodDeclarationSET = utilJavaModel.getMethodDeclarationByName(classDeclaration, "set"+storableUnitToApplyTheEncapsulateField.getName().substring(0, 1).toUpperCase() + storableUnitToApplyTheEncapsulateField.getName().substring(1).toLowerCase());

								
								if (methodGet != null && methodSet != null) {
									
									MessageDialog.openInformation(shell, "Error", "The StorableUnit that you selected already owns accessors");
									
								} else if (methodGet == null && methodSet != null) {
									
									boolean resultGET = MessageDialog.openConfirm(shell, "Error", "The StorableUnit that you selected already owns the accessor SET. Would you like to create the accerssor GET?");
									
									if (resultGET) {
										
										utilKDMMODEL.createMethodUnitGETInClassUnit(classThatContainTheStorableUnit, "get"+storableUnitToApplyTheEncapsulateField.getName().substring(0, 1).toUpperCase() + storableUnitToApplyTheEncapsulateField.getName().substring(1).toLowerCase(), storableUnitToApplyTheEncapsulateField.getType(), segment);
										utilJavaModel.createMethodDeclarationGET("get"+storableUnitToApplyTheEncapsulateField.getName().substring(0, 1).toUpperCase() + storableUnitToApplyTheEncapsulateField.getName().substring(1).toLowerCase(), classDeclaration, fieldDeclarationToApplyTheEncapsulateField, storableUnitToApplyTheEncapsulateField.getName().substring(0, 1).toUpperCase() + storableUnitToApplyTheEncapsulateField.getName().substring(1).toLowerCase(), fieldDeclarationToApplyTheEncapsulateField.getType().getType(), modelJava);
									}
									
								} else if (methodGet != null && methodSet == null) {
									
									boolean resultSET = MessageDialog.openConfirm(shell, "Error", "The StorableUnit that you selected already owns the accessor GET. Would you like to create the accerssor SET?");
									
									if (resultSET) {
										
										utilKDMMODEL.createMethodUnitSETInClassUnit(classThatContainTheStorableUnit, "set"+storableUnitToApplyTheEncapsulateField.getName().substring(0, 1).toUpperCase() + storableUnitToApplyTheEncapsulateField.getName().substring(1).toLowerCase(), storableUnitToApplyTheEncapsulateField.getType(), storableUnitToApplyTheEncapsulateField, segment);
										utilJavaModel.createMethodDeclarationSET("set"+storableUnitToApplyTheEncapsulateField.getName().substring(0, 1).toUpperCase() + storableUnitToApplyTheEncapsulateField.getName().substring(1).toLowerCase(), classDeclaration, fieldDeclarationToApplyTheEncapsulateField, storableUnitToApplyTheEncapsulateField.getName().substring(0, 1).toUpperCase() + storableUnitToApplyTheEncapsulateField.getName().substring(1).toLowerCase(), fieldDeclarationToApplyTheEncapsulateField.getType().getType(), modelJava);
									}
									
								} else if (methodGet == null && methodSet == null) {
									
									boolean resultSETandGET = MessageDialog.openConfirm(shell, "Error", "The StorableUnit that you selected does not owns the accessors. Would you like to create them?");
									
									if (resultSETandGET) {
										
										utilKDMMODEL.createMethodUnitGETInClassUnit(classThatContainTheStorableUnit, "get"+storableUnitToApplyTheEncapsulateField.getName().substring(0, 1).toUpperCase() + storableUnitToApplyTheEncapsulateField.getName().substring(1).toLowerCase(), storableUnitToApplyTheEncapsulateField.getType(), segment);
										utilKDMMODEL.createMethodUnitSETInClassUnit(classThatContainTheStorableUnit, "set"+storableUnitToApplyTheEncapsulateField.getName().substring(0, 1).toUpperCase() + storableUnitToApplyTheEncapsulateField.getName().substring(1).toLowerCase(), storableUnitToApplyTheEncapsulateField.getType(), storableUnitToApplyTheEncapsulateField, segment);
										
										utilJavaModel.createMethodDeclarationGET("get"+storableUnitToApplyTheEncapsulateField.getName().substring(0, 1).toUpperCase() + storableUnitToApplyTheEncapsulateField.getName().substring(1).toLowerCase(), classDeclaration, fieldDeclarationToApplyTheEncapsulateField, storableUnitToApplyTheEncapsulateField.getName().substring(0, 1).toUpperCase() + storableUnitToApplyTheEncapsulateField.getName().substring(1).toLowerCase(), fieldDeclarationToApplyTheEncapsulateField.getType().getType(), modelJava);
										utilJavaModel.createMethodDeclarationSET("set"+storableUnitToApplyTheEncapsulateField.getName().substring(0, 1).toUpperCase() + storableUnitToApplyTheEncapsulateField.getName().substring(1).toLowerCase(), classDeclaration, fieldDeclarationToApplyTheEncapsulateField, storableUnitToApplyTheEncapsulateField.getName().substring(0, 1).toUpperCase() + storableUnitToApplyTheEncapsulateField.getName().substring(1).toLowerCase(), fieldDeclarationToApplyTheEncapsulateField.getType().getType(), modelJava);
									}
									
								}
								System.out.println(methodGet);
								
							}
							
						} else if (attributeStorableUnitToApplyTheEncapsulateField.getValue().equals("public")) {
							
							utilKDMMODEL.createMethodUnitGETInClassUnit(classThatContainTheStorableUnit, "get"+storableUnitToApplyTheEncapsulateField.getName().substring(0, 1).toUpperCase() + storableUnitToApplyTheEncapsulateField.getName().substring(1).toLowerCase(), storableUnitToApplyTheEncapsulateField.getType(), segment);
							utilKDMMODEL.createMethodUnitSETInClassUnit(classThatContainTheStorableUnit, "set"+storableUnitToApplyTheEncapsulateField.getName().substring(0, 1).toUpperCase() + storableUnitToApplyTheEncapsulateField.getName().substring(1).toLowerCase(), storableUnitToApplyTheEncapsulateField.getType(), storableUnitToApplyTheEncapsulateField, segment);
							
							utilJavaModel.createMethodDeclarationGET("get"+storableUnitToApplyTheEncapsulateField.getName().substring(0, 1).toUpperCase() + storableUnitToApplyTheEncapsulateField.getName().substring(1).toLowerCase(), classDeclaration, fieldDeclarationToApplyTheEncapsulateField, storableUnitToApplyTheEncapsulateField.getName().substring(0, 1).toUpperCase() + storableUnitToApplyTheEncapsulateField.getName().substring(1).toLowerCase(), fieldDeclarationToApplyTheEncapsulateField.getType().getType(), modelJava);
							utilJavaModel.createMethodDeclarationSET("set"+storableUnitToApplyTheEncapsulateField.getName().substring(0, 1).toUpperCase() + storableUnitToApplyTheEncapsulateField.getName().substring(1).toLowerCase(), classDeclaration, fieldDeclarationToApplyTheEncapsulateField, storableUnitToApplyTheEncapsulateField.getName().substring(0, 1).toUpperCase() + storableUnitToApplyTheEncapsulateField.getName().substring(1).toLowerCase(), fieldDeclarationToApplyTheEncapsulateField.getType().getType(), modelJava);
						}
						
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
