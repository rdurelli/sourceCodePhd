package com.br.catalogue.refactorings.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeFactory;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeModel;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.eclipse.gmt.modisco.omg.kdm.kdm.KDMModel;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Segment;

import com.br.models.graphviz.Elements;
import com.br.models.graphviz.PackageModel;

public class PopulateKDMFromJSON {

	
	private ArrayList<Elements> elements;
	
	private Segment segment;
	
	private String pacoteUniversal = "";
	
	private ArrayList<PackageModel> pacotes = new ArrayList<PackageModel>();
	
	private PackageModel pacoteToUse = new PackageModel();
	
	public PopulateKDMFromJSON(ArrayList<Elements> elements, Segment segment) {
		this.elements = elements;
		this.segment = segment;
	}
	
	public PopulateKDMFromJSON(ArrayList<Elements> elements) {
	
		this.elements = elements;
		
	}
	
	
	
	public void run() {
		
//		EList<KDMModel> kdmModels = this.segment.getModel();
		
		for (Elements elem : elements) {
		
			
			if (elem.isClass()) {
				
//				ClassModel classTEsteMudan = (ClassModel) elem;
				
				
				
				
				
				System.out.println("� class e seu nome � " + elem.getName());
				
				if (elem.getMethods() != null) {
					
					System.out.println(elem.getName() +" Tem um total de tantos m�todos " + elem.getMethods().size());
				}
				
				
				System.out.println("O pacote da classe � " + elem.getPackageModel().getCompleteName());
				
				
				
				if (!elem.getPackageModel().getCompleteName().equals(pacoteUniversal)) {
					
					pacoteUniversal = elem.getPackageModel().getCompleteName();
					
					System.out.println("O pacote Universal � " + pacoteUniversal);
					
				}
				
				
			} else if (elem.isInterface()) {
				
				
				System.out.println("� interface e seu nome � " + elem.getName());
				
				System.out.println("O pacote da classe � " + elem.getPackageModel().getCompleteName());
				
			}
			
			
//			System.out.println("O nome � " +elem.getName());
			PackageModel pegando = createKDMPackage(elem.getPackageModel());
//			
			if (pegando.getCompleteName().equals(pacoteToUse.getCompleteName())) {
				
				pacoteToUse = pegando;
				
			}
//			
//			if (elem instanceof ClassModel) {
//				
//				System.out.println(" O pacote para usar � esse " + pacoteToUse.getCompleteName());
//				
//			}
//			
//			System.out.println("Pegando "+ pegando.getName());
//			
//			if (pegando.getPack() != null ) {
//				System.out.println("Pegando "+ pegando.getPack().getName());
//				
//				if (pegando.getPack().getPack() != null){
//					
//					System.out.println("Pegando "+ pegando.getPack().getPack().getName());
//					
//				}
//			
//			}
			

			
//			Package packageKDM = createKDMPackage(elem.getPackageModel());
//			
//			System.out.println("TESTE1 " + packageKDM.getName());
//			
//			if (packageKDM.getCodeElement().size() > 0 ) {
//			
//			System.out.println("TESTE1 " + packageKDM.getCodeElement().get(0).getName());
//			
//			if (packageKDM.getCodeElement().get(0) instanceof Package) {
//				
//				Package testeste = (Package) packageKDM.getCodeElement().get(0);
//				
//				System.out.println("Teste nome " + testeste.getCodeElement().get(0).getName());
//				
//			}
			
			}
			
		}
		
	
	
	
	private void createPackage(String completeNamePackage) {
		
		
		String[] nameSplit = completeNamePackage.split("\\.");
		
		for (int i = 0; i < nameSplit.length; i++) {
			
			PackageModel packToBeCreated = new PackageModel();
			packToBeCreated.setName(nameSplit[i]);
			
			pacotes.add(packToBeCreated);
			
			
		}
		
		
	}
	
		
//		return this.segment;
	
	private PackageModel createKDMPackage (PackageModel packageModel) {
		
		PackageModel toCreate;
		
		if (packageModel == null) {
			
			return null;
			
		} else {
			

				if (packageModel.getPack() != null) {
				
//				System.out.println("O pacote antes "+ packageModel.getName());

				}
				
				
				toCreate = new PackageModel();
				toCreate.setName(packageModel.getName());
				this.createRelationShipPackage(toCreate, packageModel.getPack());
				System.out.println("O pacote Depois "+ packageModel.getName());
				
				createKDMPackage(packageModel.getPack());
				
					
				
				
				
//				packageKDM = createPackage(packageModel, packageModel.getPack(), packageKDM);
				
			
			 
			
//			Package packageKDM = CodeFactory.eINSTANCE.createPackage();
//			
//			kdmModels.getCodeElement().add(packageKDM);
//			
//			packageKDM.setName(packageModel.getName());
//			
			
		}
		
		return toCreate;
		
		
		
		
	}
	
	
	private void createRelationShipPackage(PackageModel model1, PackageModel model2) {
		
		model1.setPack(model2);
		
	}
	
	
	
}
