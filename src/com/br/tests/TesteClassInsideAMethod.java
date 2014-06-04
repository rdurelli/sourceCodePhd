package com.br.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeFactory;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeModel;
import org.eclipse.gmt.modisco.omg.kdm.code.CodePackage;
import org.eclipse.gmt.modisco.omg.kdm.code.MethodUnit;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Attribute;
import org.eclipse.gmt.modisco.omg.kdm.kdm.ExtensionFamily;
import org.eclipse.gmt.modisco.omg.kdm.kdm.KdmFactory;
import org.eclipse.gmt.modisco.omg.kdm.kdm.KdmPackage;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Segment;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Stereotype;
import org.eclipse.gmt.modisco.omg.kdm.kdm.TagDefinition;
import org.eclipse.gmt.modisco.omg.kdm.kdm.TaggedValue;


public class TesteClassInsideAMethod {







	public static void main(String[] args) throws IOException {


		ClassUnit classPersistence = CodeFactory.eINSTANCE.createClassUnit();

		classPersistence.setName("Persistence");
		classPersistence.setIsAbstract(false);
		
		ExtensionFamily extensionFamily = createExtensionFamily();
				
		
		createXMI(classPersistence, extensionFamily);

//		System.out.println(classPersistence.getStereotype().get(0).getTag().get(0));




	}


	public static void createXMI (ClassUnit classUnit, ExtensionFamily extensionFamily) throws IOException {

		// create a resource set to hold the resources
		ResourceSet resourceSet = new ResourceSetImpl();

		// register the appropriate resource factory to handle all file extensions
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
		
		// register the package to ensure it is available during loading
        resourceSet.getPackageRegistry().put(CodePackage.eNS_URI, CodePackage.eINSTANCE);
       
//        // register the package to ensure it is available during loading
        resourceSet.getPackageRegistry().put(KdmPackage.eNS_URI, KdmPackage.eINSTANCE);
//       
        
        Resource resource = resourceSet.createResource(URI.createURI("http://My.code"));

        Segment segment = KdmFactory.eINSTANCE.createSegment();
		
		segment.setName("mySegment");
		
		CodeModel codeModel = CodeFactory.eINSTANCE.createCodeModel();
		codeModel.setName("ProjectClass");
		
		segment.getModel().add(codeModel); // add the codemodel in the earlier created
		
		segment.getExtension().add(extensionFamily);
		
		// *** this chunk of code is responsible to create the packages to put the person class
		org.eclipse.gmt.modisco.omg.kdm.code.Package packageCom = CodeFactory.eINSTANCE.createPackage();
		packageCom.setName("com");
		
		org.eclipse.gmt.modisco.omg.kdm.code.Package packageBr = CodeFactory.eINSTANCE.createPackage();
		packageBr.setName("br");
		
		packageCom.getCodeElement().add(packageBr); // add the package "br" to the package "com"
		
		org.eclipse.gmt.modisco.omg.kdm.code.Package packageClassUfscar = CodeFactory.eINSTANCE.createPackage();
		packageClassUfscar.setName("classUfscar"); // add the package "classufscar" to the package
		
		packageBr.getCodeElement().add(packageClassUfscar);
		
		codeModel.getCodeElement().add(packageCom);
		
		// *** this chuck of code is responsible to create the packages to put the person class
		
		packageClassUfscar.getCodeElement().add(classUnit);

		
		Stereotype stereotypeAspectUnit = createAspectUnitStereotype(extensionFamily, true, null, "CrosscutFullConstructor", null, null, null, null);
		
//		stereotypeAspectUnit.ec
		
		classUnit.getStereotype().add(stereotypeAspectUnit);

	
	
//		System.out.println();
		

		resource.getContents().add(segment);
		resource.save(System.out,null);
	
		
		resource.getContents().add(stereotypeAspectUnit);
//		resource.getContents().add(segment);
		
		resource.save(System.out, null);



	}

	
	private static ExtensionFamily createExtensionFamily () {
		
		ExtensionFamily extensionFamily = KdmFactory.eINSTANCE.createExtensionFamily();
		
		extensionFamily.setName("Aspect");
		
		
		
		return extensionFamily;
	}

	public static Stereotype createAspectUnitStereotype( ExtensionFamily family, Boolean isPrivileged, List<String> aspectInstantiationType, String perPointCut, List<String> declaredParents, List<String> declaredImplements, String precededBy, String precedes) {

		
		
		Stereotype aspectUnit = KdmFactory.eINSTANCE.createStereotype();
		aspectUnit.setName("AspectUnit");
		aspectUnit.setType("ClassUnit");
		
		family.getStereotype().add(aspectUnit);//addciona para a famili
		
		//----------------------------criar isPrivileged------------------------------------------------//

		TagDefinition tagIsPrivileged = KdmFactory.eINSTANCE.createTagDefinition();//cria o primeiro tagDefinifion (isPrivileged)
		tagIsPrivileged.setTag("isPrivileged");
		tagIsPrivileged.setType("Boolean");

		TaggedValue taggedValueIsPrivileged = KdmFactory.eINSTANCE.createTaggedValue();

		if (isPrivileged == null) {


			taggedValueIsPrivileged.setTag(tagIsPrivileged);//coloca o tagIsPrivaleged dentro do TaggedValueIsPrivileged
			taggedValueIsPrivileged.setValue(null);


		}else {

			taggedValueIsPrivileged.setTag(tagIsPrivileged);//coloca o tagIsPrivaleged dentro do TaggedValueIsPrivileged
			taggedValueIsPrivileged.setValue(isPrivileged.toString());

		}

		aspectUnit.getTag().add(tagIsPrivileged);//coloca a tag dentro do stereotype...
		//-------------------------------------------------------------------------------------------------//


		//----------------------------criar perType------------------------------------------------//
		TagDefinition tagPerType = KdmFactory.eINSTANCE.createTagDefinition();//cria o segundo tagDefinifion (perType)
		tagPerType.setTag("perType");
		tagPerType.setType("AspectInstantiationType");

		TaggedValue taggedValuePerType = KdmFactory.eINSTANCE.createTaggedValue();

		if (aspectInstantiationType == null) {

			taggedValuePerType.setTag(tagPerType);//coloca o tagPerType dentro do TaggedValuePerType
			taggedValuePerType.setValue(null);

		} else {

			taggedValuePerType.setTag(tagPerType);//coloca o tagPerType dentro do TaggedValuePerType
			taggedValuePerType.setValue(aspectInstantiationType.toString());

		}

		aspectUnit.getTag().add(tagPerType);//coloca a tag dentro do stereotype...

		//--------------------------------------------------------------------------------------------//


		//----------------------------criar perPointCut------------------------------------------------//
		TagDefinition tagPerPointCut = KdmFactory.eINSTANCE.createTagDefinition();//cria o terceiro tagDefinifion (perPointCut)
		tagPerPointCut.setTag("perPointCut");
		tagPerPointCut.setType("PointCut");

		TaggedValue taggedValuePerPointCut = KdmFactory.eINSTANCE.createTaggedValue();

		if (perPointCut == null) {

			taggedValuePerPointCut.setTag(tagPerPointCut);//coloca o tagPerPointCut dentro do TaggedValuePerPointCut
			taggedValuePerPointCut.setValue(null);

		} else {

			taggedValuePerPointCut.setTag(tagPerPointCut);//coloca o tagPerPointCut dentro do TaggedValuePerPointCut
			taggedValuePerPointCut.setValue(perPointCut);

		}
		aspectUnit.getTag().add(tagPerPointCut);//coloca a tag dentro do stereotype...
		//--------------------------------------------------------------------------------------------//



		//----------------------------criar DeclaredParents------------------------------------------------//
		TagDefinition tagDeclaredParents = KdmFactory.eINSTANCE.createTagDefinition();//cria o quarto tagDefinifion (DeclaredParents)
		tagDeclaredParents.setTag("DeclaredParents");
		tagDeclaredParents.setType("Generalization");

		TaggedValue taggedValueDeclaredParents = KdmFactory.eINSTANCE.createTaggedValue();

		if (declaredParents == null) {

			taggedValueDeclaredParents.setTag(tagDeclaredParents);//coloca o DeclaredParents dentro do TaggedValueDeclaredParents
			taggedValueDeclaredParents.setValue(null);

		} else {

			taggedValueDeclaredParents.setTag(tagDeclaredParents);//coloca o DeclaredParents dentro do TaggedValueDeclaredParents
			taggedValueDeclaredParents.setValue(declaredParents.toString());

		}
		aspectUnit.getTag().add(tagDeclaredParents);//coloca a tag dentro do stereotype...
		//--------------------------------------------------------------------------------------------//



		//----------------------------criar DeclaredImplements------------------------------------------------//
		TagDefinition tagDeclaredImplements = KdmFactory.eINSTANCE.createTagDefinition();//cria o quinto tagDefinifion (DeclaredImplements)
		tagDeclaredImplements.setTag("DeclaredImplements");
		tagDeclaredImplements.setType("InterfaceRealization");

		TaggedValue taggedValueDeclaredImplements = KdmFactory.eINSTANCE.createTaggedValue();

		if (declaredImplements == null) {

			taggedValueDeclaredImplements.setTag(tagDeclaredImplements);//coloca o DeclaredImplements dentro do TaggedValueDeclaredImplements
			taggedValueDeclaredImplements.setValue(null);

		} else {

			taggedValueDeclaredImplements.setTag(tagDeclaredImplements);//coloca o DeclaredImplements dentro do TaggedValueDeclaredImplements
			taggedValueDeclaredImplements.setValue(declaredImplements.toString());

		}
		aspectUnit.getTag().add(tagDeclaredImplements);//coloca a tag dentro do stereotype...
		//--------------------------------------------------------------------------------------------//

		//----------------------------criar precededBy------------------------------------------------//
		TagDefinition tagPrecededBy = KdmFactory.eINSTANCE.createTagDefinition();//cria o quinto tagDefinifion (DeclaredImplements)
		tagPrecededBy.setTag("precededBy");
		tagPrecededBy.setType("AspectUnit");

		TaggedValue taggedValuePrecededBy = KdmFactory.eINSTANCE.createTaggedValue();

		if (precededBy == null) {

			taggedValuePrecededBy.setTag(tagPrecededBy);//coloca o DeclaredImplements dentro do TaggedValueDeclaredImplements
			taggedValuePrecededBy.setValue(null);

		} else {

			taggedValuePrecededBy.setTag(tagPrecededBy);//coloca o DeclaredImplements dentro do TaggedValueDeclaredImplements
			taggedValuePrecededBy.setValue(precededBy);

		}
		aspectUnit.getTag().add(tagPrecededBy);//coloca a tag dentro do stereotype...
		//--------------------------------------------------------------------------------------------//

		//----------------------------criar precededBy------------------------------------------------//
		TagDefinition tagPrecedes = KdmFactory.eINSTANCE.createTagDefinition();//cria o quinto tagDefinifion (DeclaredImplements)
		tagPrecedes.setTag("precedes");
		tagPrecedes.setType("AspectUnit");

		TaggedValue taggedValuePrecedes = KdmFactory.eINSTANCE.createTaggedValue();

		if (precedes == null) {

			taggedValuePrecedes.setTag(tagPrecedes);//coloca o DeclaredImplements dentro do TaggedValueDeclaredImplements
			taggedValuePrecedes.setValue(null);

		} else {

			taggedValuePrecedes.setTag(tagPrecedes);//coloca o DeclaredImplements dentro do TaggedValueDeclaredImplements
			taggedValuePrecedes.setValue(precedes);

		}
		aspectUnit.getTag().add(tagPrecedes);//coloca a tag dentro do stereotype...
		//--------------------------------------------------------------------------------------------//

		return aspectUnit;
	}


}
