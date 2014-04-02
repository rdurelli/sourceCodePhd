package com.br.actions;

import java.awt.Window;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmt.modisco.infra.browser.MoDiscoBrowserPlugin;
import org.eclipse.gmt.modisco.infra.browser.editors.EcoreBrowser;
import org.eclipse.gmt.modisco.java.AbstractTypeDeclaration;
import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.Model;
import org.eclipse.gmt.modisco.java.NamedElement;
import org.eclipse.gmt.modisco.java.emf.JavaFactory;
import org.eclipse.gmt.modisco.omg.kdm.action.ActionElement;
import org.eclipse.gmt.modisco.omg.kdm.action.BlockUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeModel;
import org.eclipse.gmt.modisco.omg.kdm.code.InterfaceUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.MethodUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableUnit;
import org.eclipse.gmt.modisco.omg.kdm.core.KDMEntity;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Segment;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
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

import com.br.catalogue.refactorings.util.PopulateKDMIntoMemory;
import com.br.gui.refactoring.Refactoring;
import com.br.gui.refactoring.RefactoringName;
import com.br.gui.refactoring.RefactoringNameWizard;
import com.br.gui.refactoring.WizardExtract;
import com.br.gui.refactoring.WizardExtractClass;
import com.br.models.graphviz.Elements;
import com.br.models.graphviz.generate.image.GenerateImageFactory;
import com.br.util.models.UtilASTJDTModel;
import com.br.util.models.UtilJavaModel;
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
						    
						    System.out.println("O nome do Projeto é " + activeProjectName);
						
						ISelection iSelection = selectionProvider
								.getSelection();
					
						
						UtilJavaModel utilJavaModel = new UtilJavaModel();
						
						Model modelJava = utilJavaModel.load(activeProject.getLocationURI().toString()+"/MODELS_PIM_modificado/JavaModelRefactoring.javaxmi");
					
						//offset
						offset = ((StructuredSelection) iSelection);
						
						
						if (offset.getFirstElement() instanceof KDMEntity) {
							
							KDMEntity kdmEntity = (KDMEntity)offset.getFirstElement();
							
							System.out.println("Ok você pode renomear o que vc quiser....");
							
							System.out.println("A classe é " + kdmEntity.toString());
							
							System.out.println("A classe é " + kdmEntity.getClass());
							
						}
						
									
						if ( offset.getFirstElement() instanceof ClassUnit)  {
							
							
							
							
							
							ClassUnit classUnit = (ClassUnit)offset.getFirstElement();	
						
							
							
							String[] packageComplete  = getCompletePackageName(classUnit);
							
							/**
							 * -------------------------------------------------------------------------------------
							 * */
							UtilASTJDTModel astJDTModel = new UtilASTJDTModel();
							
							try {
								ICompilationUnit iCompilation = astJDTModel.getClassByClassUnit(classUnit, activeProject, packageComplete);
								ArrayList<IMethod> allMethods = astJDTModel.getAllMethod(iCompilation);
								ArrayList<IField> allFields = astJDTModel.getAllField(iCompilation);
								
								
								System.out.println(astJDTModel.getNumberSourceLinesOfCodeOfAClass(iCompilation));
								
								UtilKDMModel utilKDM = new UtilKDMModel();
								
								MethodUnit methodUnits = utilKDM.getMethodsUnitByName(classUnit, "getName");
								
								IMethod methodReturned = astJDTModel.getIMethodByName(iCompilation, methodUnits.getName());
								
								System.out.println(methodReturned);
								
								System.out.println(astJDTModel.getNumberSourceLinesOfAMethod(methodReturned));
								
								System.out.println(allMethods.size());
								
								System.out.println(allFields.size());
								
								
								System.out.println(iCompilation);
							} catch (JavaModelException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (CoreException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							/**
							 * -------------------------------------------------------------------------------------
							 * */
							
							NamedElement classDeclaration = getClassDeclaration(classUnit, packageComplete, modelJava);
							
							
//							NamedElement teste = JavaFactory.eINSTANCE.createMethodDeclaration();
//							
//							NamedElement teste1 = JavaFactory.eINSTANCE.createFieldDeclaration();
//							
							
							Segment segment = getSegmentToPersiste(classUnit);
							
							WizardDialog wizardDialog = new WizardDialog(shell, new RefactoringNameWizard(classUnit.getName(), classUnit, classDeclaration, true));
							
							if (wizardDialog.open() == org.eclipse.jface.window.Window.OK) {
								
								System.out.println("Ok pressed..");
								
							} else {
								
								System.out.println("Cancel pressed..");
								
							}
							
							Model modelObtained  = getModelToPersiste(classDeclaration);
							
							System.out.println(modelObtained);
							
							UtilKDMModel utilKDM = new UtilKDMModel();
							
							utilJavaModel.save(modelObtained, URIProject);
							
							
							Resource resource = utilKDM.save(segment, offset.toString(), URIProject);
							
							closeEditor(editorPart);
							
							
							IWorkspaceRoot workRoot = ResourcesPlugin.getWorkspace().getRoot();
						
							IPath path = new Path(resource.getURI().toFileString());
							
							IFile fileToOpen = workRoot.getFileForLocation(path);
								
							refreshLocal(activeProject);					
							
							
							openEditor(fileToOpen);
							
							
							Segment segmentToShow = utilKDM.load(fileToOpen.getFullPath().toOSString());
							
							System.out.println(segmentToShow);
							
							PopulateKDMIntoMemory populateKDMIntoMemory = new PopulateKDMIntoMemory(segmentToShow);
							
							ArrayList<Elements> elements = populateKDMIntoMemory.getClasses();
							
							System.out.println(elements.size());
							
							GenerateImageFactory generate = GenerateImageFactory.getInstance();
							generate.createClassGraphviz(elements);
							
							
						} else if (offset.getFirstElement() instanceof InterfaceUnit) {
							
							
							InterfaceUnit interfaceUnit = (InterfaceUnit)offset.getFirstElement();	
							
							Segment segment = getSegmentToPersiste(interfaceUnit);
							
							WizardDialog wizardDialog = new WizardDialog(shell, new RefactoringNameWizard(interfaceUnit.getName(), interfaceUnit, null, false));
							
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
							
							
							
						} else if (offset.getFirstElement() instanceof MethodUnit) {
							
							MethodUnit methodUnit = (MethodUnit)offset.getFirstElement();
							
							Segment segment = getSegmentToPersiste(methodUnit);
							
							WizardDialog wizardDialog = new WizardDialog(shell, new RefactoringNameWizard(methodUnit.getName(), methodUnit, null, false));
							
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
							
							
						} else if (offset.getFirstElement() instanceof StorableUnit) {
							
							StorableUnit storableUnit = (StorableUnit) offset.getFirstElement();
							
							Segment segment = getSegmentToPersiste(storableUnit);
							
							WizardDialog wizardDialog = new WizardDialog(shell, new RefactoringNameWizard(storableUnit.getName(), storableUnit, null, false));
							
							
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
							
							
						}
							
							
							
							
							
							
							else {
						
							MessageDialog.openError(shell, "Error", "Please be sure you have selected a KDMEntity to rename.");
							
							
						}
						
						
					}
				}
				
			}
		
		

	}
	
	
	private ClassDeclaration getClassDeclaration (ClassUnit classUnit, String[] packageComplete, Model model) {
		
		ClassDeclaration classToReturn = null;
		
		org.eclipse.gmt.modisco.java.Package packageJava = null;
		
		EList<AbstractTypeDeclaration> classes = null; 
		
		
		
		EList<org.eclipse.gmt.modisco.java.Package> packages = model.getOwnedElements();
		
		for (int i = 0; i < packageComplete.length; i++) {
			
			for (org.eclipse.gmt.modisco.java.Package package1 : packages) {
				
				if (package1.getName().equals(packageComplete[i])) {
					
					packageJava = package1;
					if (packageJava.getOwnedPackages() != null || packageJava.getOwnedPackages().size() > 0) {
						packages = packageJava.getOwnedPackages();
					}else {
						
						classes = packageJava.getOwnedElements();
						break;
						
					}
					
				}
				
			}
			
		}
		classes = packageJava.getOwnedElements();
		if (classes != null) {
			for (AbstractTypeDeclaration abstractTypeDeclaraction : classes) {
				
				if (abstractTypeDeclaraction instanceof ClassDeclaration) {
					
					ClassDeclaration classToVerifyTheName = (ClassDeclaration) abstractTypeDeclaraction;
					
					if(classToVerifyTheName.getName().equals(classUnit.getName())) {
						
						classToReturn = classToVerifyTheName;
						break;
						
					}
					
					
				}
				
			}
		}
	
		
		
		return classToReturn;
	}
	
	
	private String[] getCompletePackageName (ClassUnit classUnit) {
		
		CodeModel codeModel = null;
		
		String packageComplete = "";
		
		EObject eObject = classUnit.eContainer();
		
		while (codeModel == null) {
			
			if (eObject instanceof CodeModel) {
				
				codeModel = (CodeModel) eObject;
				
			} else if ( eObject instanceof Package)
			{
				
				Package packageKDM = (Package) eObject;
				
				eObject = packageKDM.eContainer();
				
				packageComplete += packageKDM.getName()+".";
				
				System.out.println(packageKDM.getName());
				
			}
			
			
		}
		
		String[] packages = packageComplete.split("\\.");
		Collections.reverse(Arrays.asList(packages));
		
		return packages;
	}
	
	
	private Model getModelToPersiste(NamedElement namedElement) {
		
		Model model = null;
		
		EObject eObject = namedElement.eContainer();
		
		while (model == null) {
			
			if (eObject instanceof Model) {
				
				model = (Model) eObject;
				
			} else if ( eObject instanceof org.eclipse.gmt.modisco.java.Package)
			{
				
				org.eclipse.gmt.modisco.java.Package packageJAVA = (org.eclipse.gmt.modisco.java.Package) eObject;
				
				eObject = packageJAVA.eContainer();
				
			}
			
		}
		return model;
		
	}
	
	private Segment getSegmentToPersiste(KDMEntity kdmEntity) {
		
		Segment segment = null;
		
		EObject eObject = kdmEntity.eContainer();
		
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
				
			} else if (eObject instanceof ClassUnit) {
				
				ClassUnit classUnitKDM = (ClassUnit) eObject;
				eObject = classUnitKDM.eContainer();
				
			} else if (eObject instanceof ActionElement) {
				
				ActionElement actionElement = (ActionElement) eObject;
				
				eObject = actionElement.eContainer();
				
				
			} else if (eObject instanceof BlockUnit) {
				
				
				BlockUnit blockUnit = (BlockUnit) eObject;
				
				eObject = blockUnit.eContainer();
				
				
			} else if (eObject instanceof MethodUnit) {
				
				
				MethodUnit methodUnit = (MethodUnit) eObject;
				
				eObject = methodUnit.eContainer();
				
				
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
