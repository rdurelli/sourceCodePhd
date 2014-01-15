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
import org.eclipse.gmt.modisco.java.SingleVariableAccess;
import org.eclipse.gmt.modisco.java.SingleVariableDeclaration;
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
import com.br.gui.refactoring.PullUpFieldInfo;
import com.br.gui.refactoring.PullUpMethodInfo;
import com.br.gui.refactoring.PullUpMethodInfoJavaModel;
import com.br.gui.refactoring.WizardPullUpField;
import com.br.gui.refactoring.WizardPullUpMethod;
import com.br.util.models.UtilJavaModel;
import com.br.util.models.UtilKDMModel;

public class PullUpMethodClass implements IObjectActionDelegate {

	private Shell shell;
	
	
	public PullUpMethodClass() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(IAction action) {

		LinkedHashSet<PullUpMethodInfo> extractSuperClassInfo = new LinkedHashSet<PullUpMethodInfo>();
		LinkedHashSet<PullUpMethodInfoJavaModel> extractSuperClassInfoJAVAMODEL = new LinkedHashSet<PullUpMethodInfoJavaModel>();
		//arrumar aqui no JavaModel...
		
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
					
					List<?> classesSelectedToSuperExtract = offset.toList();
					
					if (classesSelectedToSuperExtract.size() == 1) {
						
						MessageDialog.openError(shell, "Error", "Please be sure you have selected at least two ClassUnits to realize the Super Extract Class.");
						
					} else {
					
					for (int i = 0; i < classesSelectedToSuperExtract.size(); i++) {
						
						ClassUnit classUnitSelected = (ClassUnit) classesSelectedToSuperExtract.get(i);
						
						List<MethodUnit>  methodUnits = utilKDMMODEL.getMethodsUnit(classUnitSelected);
						
						ArrayList<Boolean> equalsMethods = new ArrayList<Boolean>();
						
						if (methodUnits.size() > 0) {
							
							for (int j = 0; j < classesSelectedToSuperExtract.size(); j++) {
								
								ClassUnit classUnitSelected2 = (ClassUnit) classesSelectedToSuperExtract.get(j);
								
								if (classUnitSelected != classUnitSelected2) {
								
									List<MethodUnit> methodUnits2 = utilKDMMODEL.getMethodsUnit(classUnitSelected2);
								
									
									for (MethodUnit methodUnit : methodUnits) {
										
										
										for (MethodUnit methodUnit2 : methodUnits2) {
											
											if (methodUnit.getName().equals(methodUnit2.getName()) && (((Signature)methodUnit.getType()).getParameterUnit().get(0).getType().getName().equals(((Signature)methodUnit2.getType()).getParameterUnit().get(0).getType().getName())) && utilKDMMODEL.verifyInheritanceExtends(classUnitSelected, classUnitSelected2)) {
												
												if (((Signature)methodUnit.getCodeElement().get(0)).getParameterUnit().size() == ((Signature)methodUnit2.getCodeElement().get(0)).getParameterUnit().size()) {
													
													boolean equals = true;
													EList<ParameterUnit> paramentsMethodUnit1 = ((Signature)methodUnit.getCodeElement().get(0)).getParameterUnit();
													
													EList<ParameterUnit> paramentsMethodUnit2 = ((Signature)methodUnit2.getCodeElement().get(0)).getParameterUnit();
													
													for (int k = 1; k < paramentsMethodUnit1.size(); k++) {
														
														if (!((paramentsMethodUnit1.get(k).getName().equals(paramentsMethodUnit2.get(k).getName())) && (paramentsMethodUnit1.get(k).getType().getName().equals(paramentsMethodUnit2.get(k).getType().getName())))){
															
															equalsMethods.add(false);
															
														}else {
															
															equalsMethods.add(true);
															
														}
														
													}
													
													
													
												}
												
												if (equalsMethods.contains(false)) {
													
													System.out
															.println("Os metodos s‹o diferentes..");
													
												} else {
													
												
													PullUpMethodInfo pullUpMethodInfo = new PullUpMethodInfo();
													
													pullUpMethodInfo.setTo(classUnitSelected);
													pullUpMethodInfo.setFrom(classUnitSelected2);
													pullUpMethodInfo.setMethodToExtract(methodUnit.getName());
													pullUpMethodInfo.setMethodUnitTo(methodUnit);
													pullUpMethodInfo.setMethodUnitFROM(methodUnit2);
													pullUpMethodInfo.setSuperElement(classUnitSelected.getCodeRelation().get(0).getTo());
													extractSuperClassInfo.add(pullUpMethodInfo);
													
												}
												

//												
											}
											
										}
										
									}
									
								}
								
							}
							
						}
						
						
						
						
					}
					
					Model modelJava = utilJavaModel.load(activeProject.getLocationURI().toString()+"/MODELS_PIM_modificado/JavaModelRefactoring.javaxmi");
					
					List<ClassDeclaration> classesSelectedJavaModel = this.getAllClassDeclaration(classesSelectedToSuperExtract, utilKDMMODEL, utilJavaModel, modelJava);
					
					if (classesSelectedJavaModel.size() == 1) {
						
						MessageDialog.openError(shell, "Error", "Please be sure you have selected at least two ClassUnits to realize the Super Extract Class.");
						
					}else {
						
						for (int i = 0; i < classesSelectedJavaModel.size(); i++) {
							
							ClassDeclaration classDeclationSelected = (ClassDeclaration) classesSelectedJavaModel.get(i);
							
							
							
							List<MethodDeclaration>  methodDeclations = utilJavaModel.getMethodDeclarations(classDeclationSelected);
							
							ArrayList<Boolean> equalsMethods = new ArrayList<Boolean>();
							
							if (methodDeclations.size() > 0) {
								
								for (int j = 0; j < classesSelectedJavaModel.size(); j++) {
									
									ClassDeclaration classDeclarationSelected2 = (ClassDeclaration) classesSelectedJavaModel.get(j);
									
									if (classDeclationSelected != classDeclarationSelected2) {
									
										List<MethodDeclaration> methodUnits2 = utilJavaModel.getMethodDeclarations(classDeclarationSelected2);
									
										
										for (MethodDeclaration methodUnit : methodDeclations) {
											
											
											for (MethodDeclaration methodUnit2 : methodUnits2) {
												
												if (methodUnit.getName().equals(methodUnit2.getName()) && utilJavaModel.verifyInheritanceExtends(classDeclationSelected, classDeclarationSelected2)) {
													
													EList<SingleVariableDeclaration> method1Parameters = methodUnit.getParameters();
													
													EList<SingleVariableDeclaration> method2Parameters = methodUnit.getParameters();
													
													for (int k = 0; k < method1Parameters.size(); k++) {
														
														if ( (method1Parameters.get(k).getName().equals(method2Parameters.get(k).getName())) && method1Parameters.get(k).getType().getType().getName().equals(method2Parameters.get(k).getType().getType().getName())) {
															
															equalsMethods.add(true);
															
														}else {
															
															equalsMethods.add(false);
															
														}
														
														
													}
													
													
													if (equalsMethods.contains(false)) {
														
														System.out
																.println("Os metodos s‹o diferentes..");
														
													} else {
														
													
														PullUpMethodInfoJavaModel pullUpMethodInfo = new PullUpMethodInfoJavaModel();
														
														pullUpMethodInfo.setTo(classDeclationSelected);
														pullUpMethodInfo.setFrom(classDeclarationSelected2);
														pullUpMethodInfo.setMethodToExtract(methodUnit.getName());
														pullUpMethodInfo.setMethodDeclarationTo(methodUnit);
														pullUpMethodInfo.setMethodDeclarationFROM(methodUnit2);
														pullUpMethodInfo.setSuperElement((ClassDeclaration)classDeclationSelected.getSuperClass().getType());
														extractSuperClassInfoJAVAMODEL.add(pullUpMethodInfo);
														
													}
													

//													
												}
												
											}
											
										}
										
									}
									
								}
								
							}
							
							
							
							
						}
						
						
					}
					
					
					//colocar aqui depois em um mŽtodo, na verdade passar arrumando tudo e melhorar essa classe...
					
					
					
					
					
					org.eclipse.gmt.modisco.java.Package packageToPutTheNewClassJavaModel = (org.eclipse.gmt.modisco.java.Package)classesSelectedJavaModel.get(0).eContainer();
					
//					ClassUnit superClassExtractedCreated = utilKDMMODEL.createClassUnit("SuperClassExtracted", ((Package)((ClassUnit)classesSelectedToSuperExtract.get(0)).eContainer()));
					
					for (PullUpMethodInfo info : extractSuperClassInfo) {
						
						System.out.println(info.getTo().getName() + " has similar feature with " + info.getFrom().getName() + " ( "+ info.getMethodToExtract()+" )");
						
					}
					
					Package packageToPuTTheNewClass = ((Package)((ClassUnit)classesSelectedToSuperExtract.get(0)).eContainer());
					
					if (extractSuperClassInfo.size() == 0 ) {
						
						MessageDialog.openError(shell, "Error", "It was not possible to find any bad smell related to Pull Up Field.");
						
					}else {
					
					
					WizardDialog wizard = new WizardDialog(shell, new WizardPullUpMethod(extractSuperClassInfo, extractSuperClassInfoJAVAMODEL, packageToPuTTheNewClass, packageToPutTheNewClassJavaModel, modelJava, URIProject));
					
					wizard.open();
					
				
					UtilKDMModel utilKDM = new UtilKDMModel();
					
					Segment segment = utilKDM.getSegmentToPersiste((KDMEntity)classesSelectedToSuperExtract.get(0));
					
					Resource resource = utilKDM.save(segment, offset.toString(), URIProject);
					
					closeEditor(editorPart);
					
					
					IWorkspaceRoot workRoot = ResourcesPlugin.getWorkspace().getRoot();
				
					IPath path = new Path(resource.getURI().toFileString());
					
					IFile fileToOpen = workRoot.getFileForLocation(path);
						
					refreshLocal(activeProject);					
					
					
					openEditor(fileToOpen);
					
					
					Model model = utilJavaModel.getModelToPersiste(classesSelectedJavaModel.get(0));
					
					utilJavaModel.save(model, URIProject);
					
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
