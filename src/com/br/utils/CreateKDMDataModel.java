package com.br.utils;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.m2m.atl.common.ATLExecutionException;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.sqlmodel2kdmdata.files.SQLModel2KDMData;


/**
 * This class is used to create a instance of KDM based on a SQLMODEL. Here we made the M2M transformation between SQLModel2KDMData
 * 
 * 
 * */

public class CreateKDMDataModel {
	
	
	/**
	 * This method must be used in order to create the kdmdata.kdm. This file represents all entities identified in the legacy source code. 
	 * @param modelIN This argument represents the input model, eg., it represents the sqlmodel that will be transformed to kdmdata.kdm
	 */
	public static void createModel(IFile modelIN) {
		
		SQLModel2KDMData runner;
		try {
			runner = new SQLModel2KDMData();
			String fullPathOfModelIn = modelIN.getFullPath().toString();
			
			URI placeToSaveModelOUT = URI.createURI("file:"+modelIN.getParent().getLocation().toString()+""+"/kdmdata.kdm");
			
			String nameOfProject = modelIN.getProject().getName();
			
			runner.loadModels(fullPathOfModelIn);
			runner.doSQLModel2KDMData(new NullProgressMonitor());
			runner.saveModels(placeToSaveModelOUT.toString());
			
			
			CreateKDMDataModel.refreshProject(nameOfProject);
			
			MessageDialog.openInformation(null, "KDM Data Model was created", "KDM Data Model was created properly. See the folder models.");
		
		} catch (IOException e) {
			MessageDialog.openError(null, "Error", "Could not find the file SQLModel2KDMData.properties.");
			e.printStackTrace();
			
		} catch (ATLExecutionException e) {
			MessageDialog.openError(null, "Error", "Error during the ATL Execution.");
			e.printStackTrace();
			
		} catch (ATLCoreException e) {
			MessageDialog.openError(null, "Error", "Error during the ATL Execution.");
			e.printStackTrace();
			
		} catch (CoreException e) {
			MessageDialog.openError(null, "Error", "Coudl not refresh the current project.");
			e.printStackTrace();
		}
			
	}
	
	
	
	private static void refreshProject (String nameOfProject) throws CoreException {
		
		ResourcesPlugin.getWorkspace().getRoot().getProject(nameOfProject).refreshLocal(IResource.DEPTH_INFINITE, null);
		
	} 

}
