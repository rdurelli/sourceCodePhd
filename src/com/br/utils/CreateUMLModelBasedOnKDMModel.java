package com.br.utils;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.modisco.infra.discovery.core.exception.DiscoveryException;
import org.eclipse.modisco.kdm.uml2converter.DiscoverUmlModelFromKdmModel;

public class CreateUMLModelBasedOnKDMModel {
	
	
	
	public static void createModel () {
		
		String projectName = ProjectSelectedToModernize.projectSelected.getProject().getName();
		
		String kdmModelLocation = ProjectSelectedToModernize.projectSelected.getProject().getLocationURI().toString();
		
		DiscoverUmlModelFromKdmModel discover = new DiscoverUmlModelFromKdmModel();
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		
		IWorkspaceRoot workspaceRoot = workspace.getRoot();
		
		
		IResource resrouce = ProjectSelectedToModernize.projectSelected.getProject().findMember("/MODELS_PIM/KDMMODEL_NEW.xmi");
		
		IFile fileToBeRead = (IFile) resrouce;
		
		System.out.println(" O caminho relativo � o seguinte " + fileToBeRead.getProjectRelativePath());
		
		System.out.println("O resource transformado em File � " + fileToBeRead.getName());
		
		System.out.println("O Resource identificado foi " + resrouce);
		
		
//		IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI(URIUtil.toURI(kdmModelLocation+"/MODELS_PIM/KDMMODEL.kdm"));
		
		
//		System.out.println("O tamanho do array � " + files.length);
		
//		IFile file = (IFile) workspaceRoot.findMember(kdmModelLocation+"/MODELS_PIM/KDMMODEL.kdm");
		
		System.out.println("Localization of KDM Model" + kdmModelLocation);
		
//		System.out.println("O nome do arquivo � " + file.getName());
		
		try {
			discover.discoverElement(fileToBeRead, new NullProgressMonitor());
			
			
			Resource UML = discover.getTargetModel();
			UML.setURI(URI.createURI(kdmModelLocation+"/MODELS_PSM_TO_BE/"+projectName+".uml"));
			UML.save(Collections.EMPTY_MAP);
			
		} catch (DiscoveryException e) {
			
			e.printStackTrace();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}	
	}
	
	public static void createModel (String projectName, String kdmModelLocation) {
		
		DiscoverUmlModelFromKdmModel discover = new DiscoverUmlModelFromKdmModel();
		
		IResource resrouce = ProjectSelectedToModernize.projectSelected.getProject().findMember("/MODELS_PIM/KDMMODEL_NEW.xmi");
		
		IFile fileToBeRead = (IFile) resrouce;
		
		try {
			
			discover.discoverElement(fileToBeRead, new NullProgressMonitor());
			
			Resource UML = discover.getTargetModel();
			
			UML.setURI(URI.createURI(kdmModelLocation+"/MODELS_PSM_TO_BE/"+projectName+".uml"));
			
			UML.save(Collections.EMPTY_MAP);
			
		} catch (DiscoveryException e) {
			
			e.printStackTrace();
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
		
	
		
	}
	
	

}
