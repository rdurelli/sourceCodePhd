package com.br.utils;


import java.io.IOException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.kdmdata2uml.files.KDMDATA2UML;


public class CreateUMLModelBasedOnKDMDATA {
	
	
	
	
	
	/**
	 * This method must be used in order to create the kdmdata.kdm. This file represents all entities identified in the legacy source code. 
	 * @param modelIN This argument represents the input model, eg., it represents the sqlmodel that will be transformed to kdmdata.kdm
	 */
	public static void createModel() {
		

		String kdmDataLocation = ProjectSelectedToModernize.projectSelected.getProject().getLocationURI().toString();
		
		
		
		KDMDATA2UML runner;
		try {
			
			runner = new KDMDATA2UML();
			
			runner.loadModels(kdmDataLocation+"/MODELS_PIM/KDMMDATA.kdm", kdmDataLocation+"/MODELS_PIM/KDMMDATA.kdm");
			runner.doKDMDATA2UML(new NullProgressMonitor());
			runner.saveModels(kdmDataLocation+"/MODELS_PSM_TO_BE/entities.uml");
		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ATLCoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
		
		
			
	}
	
	
	private static void refreshProject (String nameOfProject) throws CoreException {
		
		ResourcesPlugin.getWorkspace().getRoot().getProject(nameOfProject).refreshLocal(IResource.DEPTH_INFINITE, null);
		
	} 

}
