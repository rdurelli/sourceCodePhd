package com.br.actions;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.br.utils.CreateKDMDataModel;

public class IdentifyKDMData implements IObjectActionDelegate {

	private IFile fileRightClicked;
	
	private Shell shell;
	
	
	public IdentifyKDMData() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(IAction action) {
	
		
		CreateKDMDataModel.createModel(fileRightClicked);
		

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
