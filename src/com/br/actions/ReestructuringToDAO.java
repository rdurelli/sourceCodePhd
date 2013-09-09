package com.br.actions;

import java.awt.Window;
import java.util.Iterator;

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.br.utils.ProjectSelectedToModernize;
import com.br.wizards.ReestructuringPageWizard;
import com.br.wizards.ReestructuringWizard;

public class ReestructuringToDAO implements IObjectActionDelegate {

	
	private Shell shell;

	private IJavaProject file;
	
	public ReestructuringToDAO() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(IAction action) {
		// TODO Auto-generated method stub
		
		
		ProjectSelectedToModernize.projectSelected = file;
		
		ReestructuringWizard reestructuringWizard = new ReestructuringWizard();
		
		WizardDialog wizard = new WizardDialog(shell, reestructuringWizard);
		
		wizard.open();
		
//		wizard.open();
		

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
