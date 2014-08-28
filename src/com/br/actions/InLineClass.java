package com.br.actions;

import java.util.ArrayList;
import java.util.LinkedHashSet;
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
import org.eclipse.gmt.modisco.java.Model;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableUnit;
import org.eclipse.gmt.modisco.omg.kdm.core.KDMEntity;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Segment;
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

import com.br.gui.refactoring.ExtractSuperClassInfo;
import com.br.gui.refactoring.ExtractSuperClassInfoJavaModel;
import com.br.gui.refactoring.PullUpFieldInfo;
import com.br.gui.refactoring.PullUpFieldInfoJavaModel;
import com.br.gui.refactoring.WizardExtractSuperClass;
import com.br.gui.refactoring.WizardInLineClass;
import com.br.gui.refactoring.WizardPullUpField;
import com.br.trace.refactoring.PersisteTraceLogRefactoring;
import com.br.util.models.UtilJavaModel;
import com.br.util.models.UtilKDMModel;

public class InLineClass implements IObjectActionDelegate {

	private Shell shell;
	

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

					System.out.println("Location URI "
							+ activeProject.getLocationURI().toString());

					System.out.println("O nome do Projeto Ž "
							+ activeProjectName);

					ISelection iSelection = selectionProvider.getSelection();

					// offset
					offset = ((StructuredSelection) iSelection);
					
					List<?> classesSelectedToInLine = offset.toList();
					
					if (classesSelectedToInLine.size() == 1) {
						
						MessageDialog.openError(shell, "Error", "Please be sure you have selected at least two ClassUnits to realize the InLine Class.");
						
					} else {
					
					Segment segment = utilKDMMODEL.getSegmentToPersiste((KDMEntity)classesSelectedToInLine.get(0));
						
					ClassUnit classUnitSelectedToInline = (ClassUnit) classesSelectedToInLine.get(0);	
					List<StorableUnit> storablesOfClassUnitSelectedToInLine1 = utilKDMMODEL.getStorablesUnit(classUnitSelectedToInline);
					
					boolean hasALink = false;
					
					ClassUnit classUnitSelectedToInline2 = (ClassUnit) classesSelectedToInLine.get(1);
					
					Package packageToRemoveTheClass = (Package)classUnitSelectedToInline2.eContainer();
					
					StorableUnit storableUnitToRemoveLink = null;
					
					FieldDeclaration fieldDeclarationLink = null;
					
					
					
					for (StorableUnit storableUnit : storablesOfClassUnitSelectedToInLine1) {
						
						if (storableUnit.getType().getName().equals(classUnitSelectedToInline2.getName())) {
							
							storableUnitToRemoveLink = storableUnit;
							hasALink = true;
							break;
							
						}
						
					}
					
					if (!hasALink) {
						
						MessageDialog.openError(shell, "Error", "Not possible to apply the refactoring InLine Class once the two selected classes does not have none association.");
					} 
						else {
						
						
						
					} 
					
					utilKDMMODEL.verifyConstraintForClassUnit(segment, activeProjectName, classUnitSelectedToInline2, activeProject);
					
					Model modelJava = utilJavaModel.load(activeProject.getLocationURI().toString()+"/MODELS_PIM_modificado/JavaModelRefactoring.javaxmi");
					
					List<ClassDeclaration> classesSelectedJavaModel = this.getAllClassDeclaration(classesSelectedToInLine, utilKDMMODEL, utilJavaModel, modelJava);
					
					if (classesSelectedJavaModel.size() == 1) {
						
						MessageDialog.openError(shell, "Error", "Please be sure you have selected at least two ClassUnits to realize the InLine Class.");
						
					} else  {
					
					ClassDeclaration classDeclarationSelectedToInline = null;
					ClassDeclaration classDeclarationSelectedToInline2 = null;
				
					
					
					for (ClassDeclaration classDeclaration : classesSelectedJavaModel) {
						if (classDeclaration.getName().equals(classUnitSelectedToInline.getName())) {
							
							classDeclarationSelectedToInline = classDeclaration;
							
						}
						else if (classDeclaration.getName().equals(classUnitSelectedToInline2.getName())) {
							
							classDeclarationSelectedToInline2 = classDeclaration;
							
						}
						
					}
					
					List<FieldDeclaration> fieldDeclarationOfClassDeclarationSelectedToInLine1 = utilJavaModel.getFieldDeclarations(classDeclarationSelectedToInline);
					
					for (FieldDeclaration fieldDeclaration : fieldDeclarationOfClassDeclarationSelectedToInLine1) {
						
						if (fieldDeclaration.getType().getType().getName().equals(classDeclarationSelectedToInline2.getName())) {
							
							fieldDeclarationLink = fieldDeclaration;
							break;
							
						}
						
					}
					
					
					System.out.println(classDeclarationSelectedToInline.getName());
					System.out.println(classDeclarationSelectedToInline2.getName());
					
					 
					
					
					
					org.eclipse.gmt.modisco.java.Package packageToPutTheNewClassJavaModel = (org.eclipse.gmt.modisco.java.Package)classesSelectedJavaModel.get(0).eContainer();
					
//					ClassUnit superClassExtractedCreated = utilKDMMODEL.createClassUnit("SuperClassExtracted", ((Package)((ClassUnit)classesSelectedToSuperExtract.get(0)).eContainer()));
					
					org.eclipse.gmt.modisco.java.Package packageToRemoveTheClassJava = (org.eclipse.gmt.modisco.java.Package) classDeclarationSelectedToInline2.eContainer();

					System.out.println(packageToRemoveTheClassJava.getName());
					
					
					
					WizardDialog wizard = new WizardDialog(shell, new WizardInLineClass(classUnitSelectedToInline, classUnitSelectedToInline2, classDeclarationSelectedToInline, classDeclarationSelectedToInline2, modelJava, URIProject, packageToRemoveTheClass, packageToRemoveTheClassJava, storableUnitToRemoveLink, fieldDeclarationLink, segment));
//					
					wizard.open();
//					WizardDialog wizard = new WizardDialog(shell, new WizardInLineClass(extractSuperClassInfo, extractSuperClassInfoJAVAMODEL, packageToPuTTheNewClass, packageToPutTheNewClassJavaModel, modelJava, URIProject ));
//
//					wizard.open();
					
					UtilKDMModel utilKDM = new UtilKDMModel();
					
					
					
					Resource resource = utilKDM.save(segment, URIProject);
					
					closeEditor(editorPart);
					
					
					IWorkspaceRoot workRoot = ResourcesPlugin.getWorkspace().getRoot();
				
					IPath path = new Path(resource.getURI().toFileString());
					
					IFile fileToOpen = workRoot.getFileForLocation(path);
						
					refreshLocal(activeProject);					
					
					
					openEditor(fileToOpen);
					
					
					Model model = utilJavaModel.getModelToPersiste(classesSelectedJavaModel.get(0));
					
					utilJavaModel.save(model, URIProject);
					
					PersisteTraceLogRefactoring.saveTrace(activeProjectName, "InLine Class", "Rafael Durelli");
					
					
					

					}
					
				}
			}
		}
		}
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
	
	private List<ClassDeclaration> getAllClassDeclaration (List<?> classesSelectedToSuperExtract, UtilKDMModel utilKDMMODEL, UtilJavaModel utilJavaModel, Model modelJava ) {
		
		List<ClassDeclaration> classesSelectedJavaModel = new ArrayList<ClassDeclaration>();
		
		for (Object object : classesSelectedToSuperExtract) {
			
			
			String [] packageKDM = utilKDMMODEL.getCompletePackageName((ClassUnit)object);
			
			ClassDeclaration classDeclaration = utilJavaModel.getClassDeclaration((ClassUnit)object, packageKDM, modelJava);
			
			classesSelectedJavaModel.add(classDeclaration);
			
		}
		
		return classesSelectedJavaModel;
		
		
	}

}
