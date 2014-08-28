package com.br.refactoring.dsl.readingdata.action;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.xtext.parsetree.reconstr.Serializer;
import org.eclipse.xtext.resource.XtextResource;

import com.br.refactoring.dsl.refactoring.Attribute;
import com.br.refactoring.dsl.refactoring.BasicType;
import com.br.refactoring.dsl.refactoring.Class;
import com.br.refactoring.dsl.refactoring.ClassType;
import com.br.refactoring.dsl.refactoring.ElementType;
import com.br.refactoring.dsl.refactoring.Method;
import com.br.refactoring.dsl.refactoring.Model;
import com.br.refactoring.dsl.refactoring.Refactoring;
import com.br.refactoring.dsl.refactoring.RefactoringFactory;
import com.br.refactoring.dsl.util.ReadingDSL;
import com.br.refactoring.xtext.DslRuntimeModule;
import com.br.refactoring.xtext.DslStandaloneSetup;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class ReadDSL implements IObjectActionDelegate {

	private IFile fileRightClicked;
	
	private Shell shell;


	@Override
	public void run(IAction action) {
		
		
		//aqui Ž utilizado para ler o xtext e tbm para criar programaticamente o Xtext....
		System.out.println("O valor Ž " + this.fileRightClicked.getRawLocationURI());
		
		System.out.println(this.fileRightClicked.getProject().getLocationURI());
		
		String catalogueRefactoringDslFileToBeRead = this.fileRightClicked.getRawLocationURI().toString();
		
		String applicationDslFileToBeRead = this.fileRightClicked.getProject().getLocationURI().toString()+"/src/application.refactoring";
		
		String pathKDMModel = this.fileRightClicked.getProject().getLocationURI().toString()+"/MODELS_PIM_modificado/KDMRefactoring.xmi";
		
		ReadingDSL.readXTextToApplyTheRefactoring(applicationDslFileToBeRead, catalogueRefactoringDslFileToBeRead, pathKDMModel);
		
//		ReadingDSL.read(this.fileRightClicked.getLocationURI().toString());
		
//		DslStandaloneSetup.doSetup();
//		ResourceSet resourceSet = new ResourceSetImpl();
////		URI uri = URI.createFileURI("myapplication.refactoring");
//		Resource resource = resourceSet.createResource(URI.createURI("platform:/resource/TesteLabes/src/CriadoProgrammatically.refactoring"));
////		Model model = (Model) resource.getContents().get(0);
//
////		model.setName("model");
//		
//		Model model = RefactoringFactory.eINSTANCE.createModel();
//		model.setName("myModel");
//		
//		Class classRefactoring = RefactoringFactory.eINSTANCE.createClass();
//		classRefactoring.setName("ClasseCriadaProgramaticamente");
//		Class classRefactoring2 = RefactoringFactory.eINSTANCE.createClass();
//		classRefactoring2.setName("ClasseCriadaProgramaticamente2");
//		model.getContains().add(classRefactoring);
//		model.getContains().add(classRefactoring2);
////		
//		
//		Method method1 = RefactoringFactory.eINSTANCE.createMethod();
//		method1.setName("getName");
//		
//		Method method2 = RefactoringFactory.eINSTANCE.createMethod();
//		method2.setName("setName");
//		
//		Attribute primeiro = RefactoringFactory.eINSTANCE.createAttribute();
//		primeiro.setName("attrProgrammatically");
//		BasicType type = RefactoringFactory.eINSTANCE.createBasicType();
//		type.setTypeName("string");
//		primeiro.setElementType(type);
//		classRefactoring.getAttributes().add(primeiro);
//		classRefactoring2.getMethods().add(method1);
//		classRefactoring2.getMethods().add(method2);
//		
//		Injector injector = Guice.createInjector(new DslRuntimeModule());
//		Serializer serializer = injector.getInstance(Serializer.class);  
//		
//		String s = serializer.serialize(model);
//		
//		System.out.println(s);
//		
//		
//		resource.getContents().add(model);
//		try {
//			resource.save(Collections.EMPTY_MAP);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		HashMap saveOptions = new HashMap();
////	    saveOptions.put(XtextResource.OPTION_FORMAT, Boolean.TRUE);
////	    try {
////			resource.save(new FileOutputStream("myapplication.refactoring"), saveOptions);
////		} catch (FileNotFoundException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
		
		
		
 	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {

		if (selection instanceof IStructuredSelection) {
			action.setEnabled(updateSelection((IStructuredSelection) selection));
		} else {
			action.setEnabled(false);
		}

	}

	public boolean updateSelection(IStructuredSelection selection) {
		for (Iterator<?> objects = selection.iterator(); objects.hasNext();) {
			Object object = AdapterFactoryEditingDomain.unwrap(objects.next());
			if (object instanceof IFile) {
				this.fileRightClicked = (IFile) object;
				return true;
			}
		}
		return false;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();

	}

}
