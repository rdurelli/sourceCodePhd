package com.br.actions;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.modisco.infra.discovery.core.exception.DiscoveryException;
import org.eclipse.modisco.kdm.source.discoverer.DiscoverSourceModelFromJavaElement;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class IdentifyKDMSource implements IObjectActionDelegate {

	private Shell shell;

	private IJavaProject file;
	
	
	public IdentifyKDMSource() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(IAction action) {
		// TODO Auto-generated method stub
		
		
		try {
			this.createKDMSource();
			MessageDialog.openConfirm(shell, "KDM Source", "The KDM SOURCE was created properly.");
			ResourcesPlugin.getWorkspace().getRoot().getProject(this.file.getElementName()).refreshLocal(IResource.DEPTH_INFINITE, null);
	
		} catch (DiscoveryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void createKDMSource() throws DiscoveryException, IOException {
		
		String locationURIoFTheProject = this.file.getResource().getLocationURI().toString();
		
		DiscoverSourceModelFromJavaElement discoverKDM  = new DiscoverSourceModelFromJavaElement();
		
		discoverKDM.discoverElement(this.file, new NullProgressMonitor());
		
		Resource kdmResource = discoverKDM.getTargetModel();
		kdmResource .setURI(URI.createURI(locationURIoFTheProject+"/MODELS_PIM/KDMSource.kdm"));
		kdmResource.save(Collections.EMPTY_MAP);
		
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
			if (object instanceof IJavaProject) {
				this.file = (IJavaProject) object;
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
