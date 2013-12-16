package com.br.actions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
import org.eclipse.gmt.modisco.omg.kdm.code.CodeItem;
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
import com.br.gui.refactoring.WizardExtract;
import com.br.gui.refactoring.WizardExtractSuperClass;
import com.br.util.models.UtilJavaModel;
import com.br.util.models.UtilKDMModel;

public class ExtractSuperClass implements IObjectActionDelegate {

	private Shell shell;
	
	

	public ExtractSuperClass() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(IAction action) {

		LinkedHashSet<ExtractSuperClassInfo> extractSuperClassInfo = new LinkedHashSet<ExtractSuperClassInfo>();
		
		UtilKDMModel utilKDMMODEL = new UtilKDMModel();
		UtilJavaModel utilJavaModel =  new UtilJavaModel();
		
		
		
		
		
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
					
					List<?> classesSelectedToSuperExtract = offset.toList();
					
					if (classesSelectedToSuperExtract.size() == 1) {
						
						MessageDialog.openError(shell, "Error", "Please be sure you have selected at least two ClassUnits to realize the Super Extract Class.");
						
					} else {
					
					for (int i = 0; i < classesSelectedToSuperExtract.size(); i++) {
						
						ClassUnit classUnitSelected = (ClassUnit) classesSelectedToSuperExtract.get(i);
						
						List<StorableUnit>  storables = utilKDMMODEL.getStorablesUnit(classUnitSelected);
						
						if (storables.size() > 0) {
							
							for (int j = 0; j < classesSelectedToSuperExtract.size(); j++) {
								
								ClassUnit classUnitSelected2 = (ClassUnit) classesSelectedToSuperExtract.get(j);
								
								if (classUnitSelected != classUnitSelected2) {
								
									List<StorableUnit> storables2 = utilKDMMODEL.getStorablesUnit(classUnitSelected2);
								
									
									for (StorableUnit storableUnit : storables) {
										
										
										for (StorableUnit storableUnit2 : storables2) {
											
											if (storableUnit.getName().equals(storableUnit2.getName()) && (storableUnit.getType().getName().equals(storableUnit2.getType().getName()))) {
												//verificar ainda o TYPE n‹o apenas o Nome...
//												extractSuperClassInfo
												
												ExtractSuperClassInfo superClassInfo = new ExtractSuperClassInfo();
												
												superClassInfo.setTo(classUnitSelected);
												superClassInfo.setFrom(classUnitSelected2);
												superClassInfo.setAttributeToExtract(storableUnit.getName());
												superClassInfo.setStorableUnitTo(storableUnit);
												superClassInfo.setStorableUnitFROM(storableUnit2);
												extractSuperClassInfo.add(superClassInfo);
												
//												System.out.println("Sim somos iguais :" +classUnitSelected.getName() +" tem "+storableUnit.getName());
//												System.out.println("Sim somos iguais :" +classUnitSelected2.getName() +" tem "+storableUnit2.getName());
//												
											}
											
										}
										
									}
									
//									System.out.println(storables);
//									System.out.println(storables2);
//								
								}
								
							}
							
						}
						
						
						
						
					}
					
					Model modelJava = utilJavaModel.load(activeProject.getLocationURI().toString()+"/MODELS_PIM_modificado/JavaModelRefactoring.javaxmi");
					
					List<ClassDeclaration> classesSelectedJavaModel = this.getAllClassDeclaration(classesSelectedToSuperExtract, utilKDMMODEL, utilJavaModel, modelJava);
					
					
//					ClassUnit superClassExtractedCreated = utilKDMMODEL.createClassUnit("SuperClassExtracted", ((Package)((ClassUnit)classesSelectedToSuperExtract.get(0)).eContainer()));
					
					for (ExtractSuperClassInfo info : extractSuperClassInfo) {
						
						System.out.println(info.getTo().getName() + " has similar feature with " + info.getFrom().getName() + " ( "+ info.getAttributeToExtract()+" )");
						
					}
					
					Package packageToPuTTheNewClass = ((Package)((ClassUnit)classesSelectedToSuperExtract.get(0)).eContainer());
					
					WizardDialog teste = new WizardDialog(shell, new WizardExtractSuperClass(extractSuperClassInfo, packageToPuTTheNewClass));

					teste.open();
					
					UtilKDMModel utilKDM = new UtilKDMModel();
					
					Segment segment = utilKDM.getSegmentToPersiste((KDMEntity)classesSelectedToSuperExtract.get(0));
					
					Resource resource = utilKDM.save(segment, offset.toString(), URIProject);
					
					closeEditor(editorPart);
					
					
					IWorkspaceRoot workRoot = ResourcesPlugin.getWorkspace().getRoot();
				
					IPath path = new Path(resource.getURI().toFileString());
					
					IFile fileToOpen = workRoot.getFileForLocation(path);
						
					refreshLocal(activeProject);					
					
					
					openEditor(fileToOpen);
					
					
//					Model model = utilJavaModel.getModelToPersiste(classDeclaration);
//					
//					utilJavaModel.save(model, URIProject);
					
					
					

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

