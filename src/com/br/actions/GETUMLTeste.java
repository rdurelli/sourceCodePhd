package com.br.actions;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;

import org.eclipse.core.resources.IProject;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gmt.modisco.infra.browser.editors.EcoreBrowser;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.modelgoon.classes.model.Method;
import org.modelgoon.jdt.editparts.UMLClassEditPart;
import org.modelgoon.jdt.model.AssociationRelationShip;
import org.modelgoon.jdt.model.Field;
import org.modelgoon.jdt.model.UMLClass;


public class GETUMLTeste implements IObjectActionDelegate {

	
	private Shell shell;
	


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
			
			
			
		} else if (editorPart instanceof GraphicalEditorWithFlyoutPalette) {
			
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

					System.out.println("O nome do Projeto é "
							+ activeProjectName);

					ISelection iSelection = selectionProvider.getSelection();

		
					
					// offset
					offset = ((StructuredSelection) iSelection);
					UMLClassEditPart testesss = (UMLClassEditPart) offset.getFirstElement();
					
					 System.out.println(offset.toArray()[0]);
					
					 org.modelgoon.jdt.editparts.UMLClassEditPart umlClassTeste = (org.modelgoon.jdt.editparts.UMLClassEditPart)offset.toArray()[0];
					
					 UMLClass umlClassAgorasim = (UMLClass) umlClassTeste.getModel();
					 
					 System.out.println(umlClassAgorasim.getQualifiedName());
					
					 
					 Collection<Field> allFields = umlClassAgorasim.getFields();
					 
					 for (Field field : allFields) {
						System.out.println(field.getVisibilityString() + " " + "" + field.getName());
						
						System.out.println(field.getDeclaringClass().getName());
					}
					 
					org.modelgoon.jdt.model.Method newMethod = new org.modelgoon.jdt.model.Method();
					newMethod.setName("testeMethodAdd");
					
//					getModelChildren
					UMLClass testeVai = (UMLClass)testesss.getModel();
					
					System.out.println("O nome do pacote é " + testeVai.getPackageName());
					
					
					System.out.println(testeVai.getQualifiedName());
					
					System.out.println(testeVai.getFields().size());
					Collection<org.modelgoon.jdt.model.Method> methods = testeVai.getMethods();
					
									
//					testeVai.getMethods().add(newMethod);//add um novo
					
					testesss.update(null, null);
					
					Collection<AssociationRelationShip> aRelationShip = testeVai.getAssociationRelationships();
					
					for (AssociationRelationShip associationRelationShip : aRelationShip) {
						System.out.println("Package name "+ associationRelationShip.getDestination().getPackageName());
					}
					
					
					for (org.modelgoon.jdt.model.Method method : methods) {
						System.out.println(method.getJdtMethod().getElementName());
						System.out.println("O nome do método é " + method.getName());
					}
					
					System.out.println(methods);
					
					
					System.out.println(testesss.getJavaElement().getElementName());
					
//					IJavaElement testeMethod = testesss.getJavaElement().getAncestor(IJavaElement.FIELD);
//					
//					System.out.println(testeMethod.getElementName());
					
					
					try {
						System.out.println(testesss.getJavaElement().getJavaModel().getChildren().length);
						
						IJavaElement[] all = testesss.getJavaElement().getJavaModel().getChildren();
						
						for (IJavaElement iJavaElement : all) {
							
							System.out.println(iJavaElement.getElementName());
							
							if (iJavaElement.getElementType() == IJavaElement.METHOD) {
								
								System.out.println(iJavaElement.getElementName());
								
							} else if (iJavaElement.getElementType() == IJavaElement.FIELD) {
								
								System.out.println(iJavaElement.getElementName());
								
							}
						}
						
					} catch (JavaModelException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					System.out.println(offset);
					
		
		}
			}
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
