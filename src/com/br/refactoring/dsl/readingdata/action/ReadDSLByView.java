package com.br.refactoring.dsl.readingdata.action;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.gmt.modisco.infra.browser.editors.EcoreBrowser;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Segment;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.xtext.parsetree.reconstr.Serializer;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;

import com.br.catalogue.refactorings.util.PopulateKDMIntoMemory;
import com.br.models.graphviz.Elements;
import com.br.models.graphviz.generate.image.GenerateImageFactory;
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

public class ReadDSLByView implements  IObjectActionDelegate {

	private Shell shell;

	private IFile file;
	
	
	@Override
	public void run(IAction action) {
		IEditorPart editorPart = org.eclipse.modisco.kdm.source.extension.Activator
				.getDefault().getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getActiveEditor();
		
		
		if (editorPart instanceof XtextEditor) {

		}
		
			IEditorSite iEditorSite = editorPart.getEditorSite();
			if (iEditorSite != null) {
				// get selection provider
				ISelectionProvider selectionProvider = iEditorSite
						.getSelectionProvider();

				if (selectionProvider != null) {

					IFileEditorInput input = (IFileEditorInput) editorPart
							.getEditorInput();
					this.file = input.getFile();
						
					String catalogueRefactoringDslFileToBeRead = this.file.getRawLocationURI().toString();
					
					String applicationDslFileToBeRead = this.file.getProject().getLocationURI().toString()+"/src/application.refactoring";
					
					String pathKDMModel = this.file.getProject().getLocationURI().toString()+"/MODELS_PIM_modificado/KDMRefactoring.xmi";
					
					ReadingDSL.readXTextToApplyTheRefactoring(applicationDslFileToBeRead, catalogueRefactoringDslFileToBeRead, pathKDMModel);
					
					
				}
			}
		}

	
	@Override
	public void selectionChanged(IAction action, ISelection selection) {

	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {


	}

}
