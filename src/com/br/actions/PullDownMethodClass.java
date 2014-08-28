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
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmt.modisco.infra.browser.editors.EcoreBrowser;
import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.MethodDeclaration;
import org.eclipse.gmt.modisco.java.Model;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.MethodUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.eclipse.gmt.modisco.omg.kdm.code.ParameterUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Signature;
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

import com.br.gui.refactoring.ExtractSuperClassInfoJavaModel;
import com.br.gui.refactoring.PullUpMethodInfo;
import com.br.gui.refactoring.WizardPullUpMethod;
import com.br.gui.refactoring.WizardPushDownField;
import com.br.gui.refactoring.WizardPushDownMethod;
import com.br.trace.refactoring.PersisteTraceLogRefactoring;
import com.br.util.models.UtilJavaModel;
import com.br.util.models.UtilKDMModel;

public class PullDownMethodClass implements IObjectActionDelegate {

	
	private Shell shell;
	
	public PullDownMethodClass() {
		// TODO Auto-generated constructor stub
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
					
					ClassUnit classesSelectedToApplyThePullDownMethod = null;
					
					
					if (! (objectSelected instanceof ClassUnit)) {
						
						MessageDialog.openError(shell, "Error", "Please be sure you have selected at a ClassUnit to realize the Pull Down Method.");
						
					} else {
					
						classesSelectedToApplyThePullDownMethod = (ClassUnit)objectSelected;
						
						String [] packageKDM = utilKDMMODEL.getCompletePackageName(classesSelectedToApplyThePullDownMethod);
						
						Model modelJava = utilJavaModel.load(activeProject.getLocationURI().toString()+"/MODELS_PIM_modificado/JavaModelRefactoring.javaxmi");
						Segment segment = utilKDMMODEL.getSegmentToPersiste(classesSelectedToApplyThePullDownMethod);
						
						
						ClassDeclaration classesSelectedToApplyThePullDownMethodJavaModel = utilJavaModel.getClassDeclaration(classesSelectedToApplyThePullDownMethod, packageKDM, modelJava);
						
						ArrayList<ClassDeclaration> classDeclarations = utilJavaModel.getAllClasses(modelJava);
						ArrayList<ClassUnit> classUnitsTodas = utilKDMMODEL.getAllClasses(segment);			
						
						ArrayList<ClassUnit> inheritance = utilKDMMODEL.getRelationShipInheritancePassingTheSuper(classesSelectedToApplyThePullDownMethod, classUnitsTodas);
						ArrayList<ClassDeclaration> inheritanceJavaModel = utilJavaModel.getRelationShipInheritancePassingTheSuper(classesSelectedToApplyThePullDownMethodJavaModel, classDeclarations);
						
						if (inheritance.size() == 0 && inheritanceJavaModel.size() == 0) {
							
							MessageDialog.openInformation(shell, "Error", "Push Down is not allowed on type " + classesSelectedToApplyThePullDownMethod.getName() + ", since it does not have subclasses to which members could be pushed down.");
							
						} else {
							
							List<MethodUnit> methodsUnits = utilKDMMODEL.getMethodsUnit(classesSelectedToApplyThePullDownMethod);
							List<MethodDeclaration> methodDeclarations = utilJavaModel.getMethodDeclarations(classesSelectedToApplyThePullDownMethodJavaModel);
							
							if (methodsUnits.size() == 0 && methodDeclarations.size() == 0) {
								
								MessageDialog.openInformation(shell, "Error", "There is none MethodUnit (methods) to apply the Pull Down Method.");
								
							} else {
															
//								List<ClassDeclaration> classesSelectedJavaModel = this.getAllClassDeclaration(classesSelectedToSuperExtract, utilKDMMODEL, utilJavaModel, modelJava);
//								org.eclipse.gmt.modisco.java.Package packageToPutTheNewClassJavaModel = (org.eclipse.gmt.modisco.java.Package)classesSelectedJavaModel.get(0).eContainer();
								
								
								WizardDialog wizard = new WizardDialog(shell, new WizardPushDownMethod(classesSelectedToApplyThePullDownMethod, inheritance, classesSelectedToApplyThePullDownMethodJavaModel, inheritanceJavaModel));
								
								wizard.open();
								
								Resource resource = utilKDMMODEL.save(segment, URIProject);
								
								closeEditor(editorPart);
								
								IWorkspaceRoot workRoot = ResourcesPlugin.getWorkspace().getRoot();
								
								IPath path = new Path(resource.getURI().toFileString());
								
								IFile fileToOpen = workRoot.getFileForLocation(path);
									
								refreshLocal(activeProject);					
								
								
								openEditor(fileToOpen);
								
								PersisteTraceLogRefactoring.saveTrace(activeProjectName, "PullDownMethod", "Rafael Durelli");
								
							}
							
							
							
						}
												
					}
					
					
					
//					
					
					
					//colocar aqui depois em um método, na verdade passar arrumando tudo e melhorar essa classe...
					
					
					
					
					
//					
					
//					ClassUnit superClassExtractedCreated = utilKDMMODEL.createClassUnit("SuperClassExtracted", ((Package)((ClassUnit)classesSelectedToSuperExtract.get(0)).eContainer()));
					
					
//					Package packageToPuTTheNewClass = ((Package)((ClassUnit)classesSelectedToSuperExtract.get(0)).eContainer());
					
					
					
//					WizardDialog wizard = new WizardDialog(shell, new WizardPullUpMethod(extractSuperClassInfo, extractSuperClassInfoJAVAMODEL, packageToPuTTheNewClass, packageToPutTheNewClassJavaModel, modelJava, URIProject));
					
//					wizard.open();
					
				
					
					
					
					
					
					
					
					
//					Model model = utilJavaModel.getModelToPersiste(classesSelectedJavaModel.get(0));
					
//					utilJavaModel.save(model, URIProject);
					
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
