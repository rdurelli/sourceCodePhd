package com.br.util.models;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.gmt.modisco.java.Model;
import org.eclipse.gmt.modisco.java.emf.JavaPackage;
import org.eclipse.gmt.modisco.java.generation.files.GenerateJavaExtended;
import org.eclipse.gmt.modisco.omg.kdm.action.ActionElement;
import org.eclipse.gmt.modisco.omg.kdm.action.ActionFactory;
import org.eclipse.gmt.modisco.omg.kdm.action.Addresses;
import org.eclipse.gmt.modisco.omg.kdm.action.BlockUnit;
import org.eclipse.gmt.modisco.omg.kdm.action.Reads;
import org.eclipse.gmt.modisco.omg.kdm.code.AbstractCodeElement;
import org.eclipse.gmt.modisco.omg.kdm.code.BooleanType;
import org.eclipse.gmt.modisco.omg.kdm.code.CharType;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeFactory;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeItem;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeModel;
import org.eclipse.gmt.modisco.omg.kdm.code.ExportKind;
import org.eclipse.gmt.modisco.omg.kdm.code.Extends;
import org.eclipse.gmt.modisco.omg.kdm.code.FloatType;
import org.eclipse.gmt.modisco.omg.kdm.code.IntegerType;
import org.eclipse.gmt.modisco.omg.kdm.code.LanguageUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.MethodKind;
import org.eclipse.gmt.modisco.omg.kdm.code.MethodUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.OctetType;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.eclipse.gmt.modisco.omg.kdm.code.ParameterKind;
import org.eclipse.gmt.modisco.omg.kdm.code.ParameterUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.PrimitiveType;
import org.eclipse.gmt.modisco.omg.kdm.code.Signature;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableKind;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.StringType;
import org.eclipse.gmt.modisco.omg.kdm.code.VoidType;
import org.eclipse.gmt.modisco.omg.kdm.core.KDMEntity;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Attribute;
import org.eclipse.gmt.modisco.omg.kdm.kdm.KDMModel;
import org.eclipse.gmt.modisco.omg.kdm.kdm.KdmFactory;
import org.eclipse.gmt.modisco.omg.kdm.kdm.KdmPackage;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Segment;
import org.eclipse.gmt.modisco.omg.kdm.source.SourceFactory;
import org.eclipse.gmt.modisco.omg.kdm.source.SourceFile;
import org.eclipse.gmt.modisco.omg.kdm.source.SourceRef;
import org.eclipse.gmt.modisco.omg.kdm.source.SourceRegion;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.omg.IOP.CodecFactory;

import com.br.databaseDDL.Column;
import com.br.gui.refactoring.ExtractSuperClassInfo;
import com.br.utils.ProjectSelectedToModernize;

public class UtilKDMModel {

	
public Segment load(String KDMModelFullPath){

		
		KdmPackage.eINSTANCE.eClass();
	

		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("website", new XMIResourceFactoryImpl());

		// Obtain a new resource set
		ResourceSet resSet = new ResourceSetImpl();

		// Get the resource
		Resource resource = resSet.getResource(URI
				.createURI(KDMModelFullPath), true);
		// Get the first model element and cast it to the right type, in my
		// example everything is hierarchical included in this first node
		System.out.println(resource.getContents().get(0).toString());
		
		System.out.println("O Contents é " + resource.getContents());
		
		return (Segment) resource.getContents().get(0);
	}


	
	/**
	 * 
	 * 
	 * @author rafaeldurelli
	 * @param segment obtem o KDM do projeto para obter os tipos primitivos.
	 * @param name - você deve passar os nomes dos primitivos para recuperar: int, long, double, short, float, boolean, void, char, byte, string
	 * @return  PrimitiveType - deve-se fazer um cast de acordo com o type de primitive que vc deseja obter.
	 * */
	public PrimitiveType getPrimitiveType (Segment segment, String name) {
		
		EList<KDMModel> models =  segment.getModel();
		
		CodeModel codeModel = null;
		
		LanguageUnit languageUnit = null;
		
		PrimitiveType typeToReturn = null;
		
		if (models.get(0) instanceof CodeModel) {
			
			
			codeModel = (CodeModel) models.get(0);
		
			EList<AbstractCodeElement> codeElements = codeModel.getCodeElement();
			
			for (AbstractCodeElement abstractCodeElement : codeElements) {
				
				if (abstractCodeElement instanceof LanguageUnit) {
					
					languageUnit = (LanguageUnit) abstractCodeElement;
					
					EList<AbstractCodeElement> primitives = languageUnit.getCodeElement();
					
					
					for (AbstractCodeElement abstractCodeElement2 : primitives) {
						
						if (( abstractCodeElement2 instanceof IntegerType ) && ( (IntegerType)abstractCodeElement2).getName().equalsIgnoreCase(name) ) {
							
							typeToReturn = (IntegerType) abstractCodeElement2;
							
							
						}
						else if (( abstractCodeElement2 instanceof FloatType ) && ( (FloatType)abstractCodeElement2).getName().equalsIgnoreCase(name)) {
							
							typeToReturn = (FloatType) abstractCodeElement2;
							
						}
						else if (( abstractCodeElement2 instanceof BooleanType ) && ( (BooleanType)abstractCodeElement2).getName().equalsIgnoreCase(name)) {
							
							typeToReturn = (BooleanType) abstractCodeElement2;
							
						}
						else if (( abstractCodeElement2 instanceof VoidType ) && ( (VoidType)abstractCodeElement2).getName().equalsIgnoreCase(name)) {
							
							typeToReturn = (VoidType) abstractCodeElement2;
							
						}
						else if (( abstractCodeElement2 instanceof CharType ) && ( (CharType)abstractCodeElement2).getName().equalsIgnoreCase(name)) {
							
							typeToReturn = (CharType) abstractCodeElement2;
							
						}
						else if (( abstractCodeElement2 instanceof OctetType ) && ( (OctetType)abstractCodeElement2).getName().equalsIgnoreCase(name)) {
							
							typeToReturn = (OctetType) abstractCodeElement2;
							
						}
						else if (( abstractCodeElement2 instanceof StringType ) && ( (StringType)abstractCodeElement2).getName().equalsIgnoreCase(name)) {
							
							typeToReturn = (StringType) abstractCodeElement2;
							
						}
						
						
					}
					
				}
				
			}
			
		}
		
		return typeToReturn;
	}

	//TODO verificar como esse metodo esta implementado. Eu lembro que eu forço ele começar com o codeModelCorreto. Verificar isso antes de chamar ele.
	public ClassUnit getStringType (Segment segment) {
		
		
		
		List<KDMModel> models = segment.getModel();
		
		CodeModel codeModel = (CodeModel) models.get(1);
		
		
		ClassUnit stringToBeRetorned = null;
		
		EList<AbstractCodeElement> codeElements = codeModel.getCodeElement();
		
		for (AbstractCodeElement abstractCodeElement : codeElements) {
			
			if (abstractCodeElement instanceof Package) {
				
				EList<AbstractCodeElement> packages = ((Package) abstractCodeElement).getCodeElement();
				
				for (AbstractCodeElement abstractCodeElement2 : packages) {
					
					if ( ( abstractCodeElement2 instanceof Package ) && ((Package) abstractCodeElement2).getName().equalsIgnoreCase("lang") ) {
						
						
						EList<AbstractCodeElement> stuffs = ((Package) abstractCodeElement2).getCodeElement();
						
						for (AbstractCodeElement abstractCodeElement3 : stuffs) {
							
							if (( abstractCodeElement3 instanceof ClassUnit ) && ( ((ClassUnit) abstractCodeElement3).getName().equalsIgnoreCase("String") )) {
								
								stringToBeRetorned = (ClassUnit) abstractCodeElement3;
								
							}
							
						}
						
					}
					
					
				}
				
				
				
			}
			
		}
		
		return stringToBeRetorned;
		
	}
	
	private SourceFile criarSourceFile (String nameOFTheTableToBeClass) {
		
		SourceFile sourceFile = SourceFactory.eINSTANCE.createSourceFile();
		
		sourceFile.setName(nameOFTheTableToBeClass+".java");
		
		sourceFile.setPath("/Users/rafaeldurelli/Documents/runtime-EclipseApplication/Legacy_System_To_Test/src/"+nameOFTheTableToBeClass+".java");
		
		sourceFile.setLanguage("java");
		
		
		return sourceFile;
		
	}
	
	private SourceRegion criarSourceRegion (String nameOFTheTableToBeClass) {
		
		SourceRegion sourceRegion = SourceFactory.eINSTANCE.createSourceRegion();
		
		sourceRegion.setLanguage("java");
//		O File não esta sendo criado no modelo, tentar verificar o motivo...
		sourceRegion.setFile(this.criarSourceFile(nameOFTheTableToBeClass));
		
		
		return sourceRegion;
		
		
		
	}
	
	private SourceRef criarSource ( String nameOFTheTableToBeClass ) {
		
		SourceRef sourceRef = SourceFactory.eINSTANCE.createSourceRef();
		
		sourceRef.setLanguage("java");
		
		sourceRef.getRegion().add(this.criarSourceRegion(nameOFTheTableToBeClass));
		
		return sourceRef;
		
	}
	
	public ClassUnit createClassUnit (String name, Package packageObtained) {
		
		ClassUnit classUnitToBeCreated = CodeFactory.eINSTANCE.createClassUnit();
		
		classUnitToBeCreated.setName(name);
		classUnitToBeCreated.setIsAbstract(false);
		classUnitToBeCreated.getSource().add(this.criarSource(name));
		
		
		packageObtained.getCodeElement().add(classUnitToBeCreated);
		
		return classUnitToBeCreated;
		
	}
	
	public StorableUnit createStorableUnitInAClassUnit (ClassUnit classUnit, String name, ClassUnit type) {
		
		StorableUnit attributeLike = CodeFactory.eINSTANCE.createStorableUnit();
		attributeLike.setName(name);
		attributeLike.setKind(StorableKind.GLOBAL);
		attributeLike.getAttribute().add(this.criarAttibuteForStorableUnit());
		attributeLike.getSource().add(this.criarSource(classUnit.getName()));
		attributeLike.setType(type);
		
		
		classUnit.getCodeElement().add(attributeLike);
		
		return attributeLike;
		
	}
	
	public MethodUnit createMethodUnitGETInClassUnit (ClassUnit classUnit, String name, ClassUnit stringType, Segment segment) {
		
		MethodUnit methodUnit = CodeFactory.eINSTANCE.createMethodUnit();
		methodUnit.setName(name);
		methodUnit.setKind(MethodKind.METHOD);
		methodUnit.setExport(ExportKind.PUBLIC);
		methodUnit.getAttribute().add(this.criarAttributeForMethodUnit());
		methodUnit.getSource().add(this.criarSource(classUnit.getName()));
		
		//criar a Signature
		
		Signature signature = CodeFactory.eINSTANCE.createSignature();
		signature.setName(name);
		
		//criar o ParameterUnit
		
		ParameterUnit parameterUnit = CodeFactory.eINSTANCE.createParameterUnit();
		
		parameterUnit.getSource().add(this.criarSource(classUnit.getName()));
		
		parameterUnit.setKind(ParameterKind.RETURN);
		
		
		
		parameterUnit.setType(stringType);
		
		signature.getParameterUnit().add(parameterUnit);
		
		//criar o ParameterUnit
		
		methodUnit.setType(signature);
		
		methodUnit.getCodeElement().add(signature);
		
		BlockUnit blockUnit = ActionFactory.eINSTANCE.createBlockUnit();
		
		blockUnit.getSource().add(this.criarSource(classUnit.getName()));
		
		ActionElement actionElement = ActionFactory.eINSTANCE.createActionElement();
		
		actionElement.setName("return");
		actionElement.setKind("return");
		actionElement.getSource().add(this.criarSource(classUnit.getName()));
		
		blockUnit.getCodeElement().add(actionElement);
		
		//colocar o blockUnit
		methodUnit.getCodeElement().add(blockUnit);
		
		classUnit.getCodeElement().add(methodUnit);
		
		return methodUnit;
		
	}
	
	public MethodUnit createMethodUnitGETInClassUnit (ClassUnit classUnit, String name, PrimitiveType type, Segment segment) {
		
		MethodUnit methodUnit = CodeFactory.eINSTANCE.createMethodUnit();
		methodUnit.setName(name);
		methodUnit.setKind(MethodKind.METHOD);
		methodUnit.setExport(ExportKind.PUBLIC);
		methodUnit.getAttribute().add(this.criarAttributeForMethodUnit());
		methodUnit.getSource().add(this.criarSource(classUnit.getName()));
		
		//criar a Signature
		
		Signature signature = CodeFactory.eINSTANCE.createSignature();
		signature.setName(name);
		
		//criar o ParameterUnit
		
		ParameterUnit parameterUnit = CodeFactory.eINSTANCE.createParameterUnit();
		
		parameterUnit.getSource().add(this.criarSource(classUnit.getName()));
		
		parameterUnit.setKind(ParameterKind.RETURN);
		
		
		
		parameterUnit.setType(type);
		
		signature.getParameterUnit().add(parameterUnit);
		
		//criar o ParameterUnit
		
		methodUnit.setType(signature);
		
		methodUnit.getCodeElement().add(signature);
		
		BlockUnit blockUnit = ActionFactory.eINSTANCE.createBlockUnit();
		
		blockUnit.getSource().add(this.criarSource(classUnit.getName()));
		
		ActionElement actionElement = ActionFactory.eINSTANCE.createActionElement();
		
		actionElement.setName("return");
		actionElement.setKind("return");
		actionElement.getSource().add(this.criarSource(classUnit.getName()));
		
		blockUnit.getCodeElement().add(actionElement);
		
		//colocar o blockUnit
		methodUnit.getCodeElement().add(blockUnit);
		
		classUnit.getCodeElement().add(methodUnit);
		
		return methodUnit;
		
	}
	
	//não terminado ter que terminar ainda.......
	public MethodUnit createMethodUnitSETInClassUnit (ClassUnit classUnit, String name, PrimitiveType type, StorableUnit attribute, Segment segment) {
		
		MethodUnit methodUnit = CodeFactory.eINSTANCE.createMethodUnit();
		methodUnit.setName(name);
		methodUnit.setKind(MethodKind.METHOD);
		methodUnit.setExport(ExportKind.PUBLIC);
		methodUnit.getAttribute().add(this.criarAttributeForMethodUnit());
		methodUnit.getSource().add(this.criarSource(classUnit.getName()));
		
		//criar a Signature
		
		Signature signature = CodeFactory.eINSTANCE.createSignature();
		signature.setName(name);
		
		//criar o ParameterUnit
		
		ParameterUnit parameterUnit = CodeFactory.eINSTANCE.createParameterUnit();
		
		parameterUnit.getSource().add(this.criarSource(classUnit.getName()));
		
		parameterUnit.setKind(ParameterKind.RETURN);
		
		ParameterUnit secondParameterUnit = CodeFactory.eINSTANCE.createParameterUnit();
		secondParameterUnit.setName("name");//mudar aqui..
		
		secondParameterUnit.getSource().add(this.criarSource(classUnit.getName()));
		
		secondParameterUnit.setType(type);
		
		parameterUnit.setType(this.getPrimitiveType(segment, "void"));
		
		signature.getParameterUnit().add(parameterUnit);
		signature.getParameterUnit().add(secondParameterUnit);
		//criar o ParameterUnit
		
		methodUnit.setType(signature);
		
		methodUnit.getCodeElement().add(signature);
		
		BlockUnit blockUnit = ActionFactory.eINSTANCE.createBlockUnit();
		
		blockUnit.getSource().add(this.criarSource(classUnit.getName()));
		
		ActionElement actionElement = ActionFactory.eINSTANCE.createActionElement();
		
		actionElement.setName("expression statement");
		actionElement.setKind("expression statement");
		actionElement.getSource().add(this.criarSource(classUnit.getName()));
//		actionElement.getCo
		
		ActionElement actionElementAssign = ActionFactory.eINSTANCE.createActionElement();
		actionElementAssign.setName("ASSIGN");
		actionElementAssign.setKind("assignment");
		actionElementAssign.getSource().add(this.criarSource(classUnit.getName()));
		
		Reads readActionAssign = ActionFactory.eINSTANCE.createReads();
		
		readActionAssign.setFrom(actionElementAssign);
		
		actionElementAssign.getActionRelation().add(readActionAssign);
		
		ParameterUnit parameterUnitRead = CodeFactory.eINSTANCE.createParameterUnit();
		parameterUnitRead.setName("name");//mudar aqui;
		parameterUnitRead.setKind(ParameterKind.UNKNOWN);
		parameterUnitRead.getSource().add(this.criarSource(classUnit.getName()));
		parameterUnitRead.setType(type);
		
		readActionAssign.setTo(parameterUnitRead);
		
		actionElement.getCodeElement().add(actionElementAssign);
		
		ActionElement actionElementFieldAccess = ActionFactory.eINSTANCE.createActionElement();
		actionElementFieldAccess.setName("field access");
		actionElementFieldAccess.setKind("field access");
		actionElementFieldAccess.getSource().add(this.criarSource(classUnit.getName()));
		
		Addresses addressesFieldAccess = ActionFactory.eINSTANCE.createAddresses();
		
		actionElementFieldAccess.getActionRelation().add(addressesFieldAccess);
		
		addressesFieldAccess.setFrom(actionElementFieldAccess);
		addressesFieldAccess.setTo(attribute);
		
		actionElementAssign.getCodeElement().add(actionElementFieldAccess);
		
		ActionElement actionElementThis = ActionFactory.eINSTANCE.createActionElement();
		actionElementThis.setName("this");
		actionElementThis.setKind("this");
		actionElementThis.getSource().add(this.criarSource(classUnit.getName()));
		
		actionElementFieldAccess.getCodeElement().add(actionElementThis);
		
		blockUnit.getCodeElement().add(actionElement);
		
		//colocar o blockUnit
		methodUnit.getCodeElement().add(blockUnit);
		
		classUnit.getCodeElement().add(methodUnit);
		
		return methodUnit;
		
	}
	
	private Attribute criarAttributeForMethodUnit () {
		
		Attribute att = KdmFactory.eINSTANCE.createAttribute();
		
		att.setTag("export");
		att.setValue("private");
		
		return att;
		
	}
	
	public void moveStorableUnitToClassUnit (ClassUnit classUnit, StorableUnit storableUnit) {
		
		
		if ( classUnit!= null) {
			
			classUnit.getCodeElement().add(storableUnit);
			
		} else {
			Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			MessageDialog.openError(activeShell, "Error", "ClassUnit can not be null");
			
		}
		
		
	}
	
	public void moveMethodUnitToClassUnit (ClassUnit classUnit, MethodUnit methodUnit) {
		
		
		if ( classUnit!= null) {
			
			classUnit.getCodeElement().add(methodUnit);
			
		} else {
			Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			MessageDialog.openError(activeShell, "Error", "ClassUnit can not be null");
			
		}
		
		
	}
	
	public void removeStorableUnit (ClassUnit classUnit, StorableUnit storableUnit) {
		
		
		if ( classUnit!= null) {
			
			classUnit.getCodeElement().remove(storableUnit);
			
		} else {
			Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			MessageDialog.openError(activeShell, "Error", "ClassUnit can not be null");
			
		}
		
		
	}
	
	
	public void createSuperExtractClass (ClassUnit classUnit, LinkedHashSet<ExtractSuperClassInfo> extractSuperClassInfo) {
		
		
		Iterator<ExtractSuperClassInfo> ite = extractSuperClassInfo.iterator();
		
		while (ite.hasNext()) {
			
			ExtractSuperClassInfo classInfo = ite.next();
			
			moveStorableUnitToClassUnit(classUnit, classInfo.getStorableUnitTo());
			moveStorableUnitToClassUnit(classUnit, classInfo.getStorableUnitFROM());
			
		}
		
		EList<CodeItem> elements = classUnit.getCodeElement();
		
		for (int i = 0; i < elements.size(); i++) {
			
		
			for (int j = 0; j < elements.size(); j++) {
				
				if (elements.get(i) instanceof StorableUnit && elements.get(i).getName().equals(elements.get(j).getName())) {
					
					StorableUnit elementToRemove = (StorableUnit) elements.get(j);
					
					//não é a melhor ideia, pois tem lugares que tem referencia ao attributo removido..
					removeStorableUnit(classUnit, elementToRemove);
					
				}
				
			}
			
			
		}
		
		
		
		
	}
	
	public List<StorableUnit> getStorablesUnit (ClassUnit classUnit) {
		
		EList<CodeItem> codeElements = classUnit.getCodeElement();
		
		List<StorableUnit> attributes = new ArrayList<StorableUnit>();
		
		for (CodeItem element : codeElements) {
			
			if (element instanceof StorableUnit) {
				
				attributes.add((StorableUnit)element);
				
			}
			
		}
		return attributes;
		
		
	}
	
	private Attribute criarAttibuteForStorableUnit () {
		
		Attribute att = KdmFactory.eINSTANCE.createAttribute();
		
		att.setTag("export");
		att.setValue("private");
		
		return att;
		
	}
	
	
	
	
	
	//this method is used to generate source-code based on the Java model as input.
	public void generateNewSourceCode() throws IOException {


		XMIResourceFactoryImpl xmiResourceFactoryImpl = new XMIResourceFactoryImpl() {
			public Resource createResource(URI uri) {
				XMIResource xmiResource = new XMIResourceImpl(uri);
				return xmiResource;
			}
		};

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				"xmi", xmiResourceFactoryImpl);


		
		String projectToPutTheNewSourceCode = ProjectSelectedToModernize.projectSelected.getProject().getLocationURI().toString().split("file:")[1];
		
		String locationOfTheNewJAvaModelWithComment = ProjectSelectedToModernize.projectSelected.getProject().getLocationURI().toString();

		GenerateJavaExtended javaGenerator = new GenerateJavaExtended(URI.createURI(locationOfTheNewJAvaModelWithComment+"/MODELS_PSM_TO_BE/newJavaModel.javaxmi"),
				new File(projectToPutTheNewSourceCode+"/src-gen"), new ArrayList<Object>()); 


		System.out.println("Esta gerando...");		
		javaGenerator.doGenerate(null);
		System.out.println("Gerou");
	}


	
//	this method is used to save the JavaModel 
	public void save(Segment model)  {


		KdmPackage.eINSTANCE.eClass();

		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("website", new XMIResourceFactoryImpl());

		// Obtain a new resource set
		ResourceSet resSet = new ResourceSetImpl();

//		ProjectSelectedToModernize.projectSelected.getProject().getF
		
//		IResource resrouce = ProjectSelectedToModernize.projectSelected.getProject().findMember("/MODELS_PIM/KDMMODEL.xmi");
//		
//		IFile fileToBeRead = (IFile) resrouce;
		
		String locationOfTheNewJAvaModelWithComment = ProjectSelectedToModernize.projectSelected.getProject().getLocationURI().toString();
		
		Resource resource = resSet.createResource(URI.createURI(locationOfTheNewJAvaModelWithComment+"/MODELS_PIM/KDMMODEL_NEW.xmi"));

		resource.getContents().add(model);

		try {

			resource.save(Collections.EMPTY_MAP);

		} catch (IOException e) {
			// TODO: handle exception
		}

	}
	
//	this method is used to save the JavaModel 
	public Resource save(Segment model, String name, String projectURI)  {


		KdmPackage.eINSTANCE.eClass();

		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("website", new XMIResourceFactoryImpl());

		// Obtain a new resource set
		ResourceSet resSet = new ResourceSetImpl();

//		ProjectSelectedToModernize.projectSelected.getProject().getF
		
//		IResource resrouce = ProjectSelectedToModernize.projectSelected.getProject().findMember("/MODELS_PIM/KDMMODEL.xmi");
//		
//		IFile fileToBeRead = (IFile) resrouce;
		
//		String locationOfTheNewJAvaModelWithComment = ProjectSelectedToModernize.projectSelected.getProject().getLocationURI().toString();
		
		Resource resource = resSet.createResource(URI.createURI(projectURI+"/MODELS_PIM_modificado/KDMRefactoring.xmi"));

		resource.getContents().add(model);

		try {

			resource.save(Collections.EMPTY_MAP);
			
		} catch (IOException e) {
			// TODO: handle exception
		}

		return resource;
		
	}
	
	public String[] getCompletePackageName (ClassUnit classUnit) {
		
		CodeModel codeModel = null;
		
		String packageComplete = "";
		
		EObject eObject = classUnit.eContainer();
		
		while (codeModel == null) {
			
			if (eObject instanceof CodeModel) {
				
				codeModel = (CodeModel) eObject;
				
			} else if ( eObject instanceof Package)
			{
				
				Package packageKDM = (Package) eObject;
				
				eObject = packageKDM.eContainer();
				
				packageComplete += packageKDM.getName()+".";
				
				System.out.println(packageKDM.getName());
				
			}
			
			
		}
		
		String[] packages = packageComplete.split("\\.");
		Collections.reverse(Arrays.asList(packages));
		
		return packages;
	}
	
	public Segment getSegmentToPersiste(KDMEntity kdmEntity) {
		
		Segment segment = null;
		
		EObject eObject = kdmEntity.eContainer();
		
		while (segment == null) {
			
			if (eObject instanceof Segment) {
				
				segment = (Segment) eObject;
				
			} else if ( eObject instanceof Package)
			{
				
				Package packageKDM = (Package) eObject;
				
				eObject = packageKDM.eContainer();
				
				System.out.println(eObject);
				
			} else if (eObject instanceof CodeModel) {
				
				
				CodeModel codeModelKDM = (CodeModel) eObject;
				
				eObject = codeModelKDM.eContainer();
				
			} else if (eObject instanceof ClassUnit) {
				
				ClassUnit classUnitKDM = (ClassUnit) eObject;
				eObject = classUnitKDM.eContainer();
				
			} else if (eObject instanceof ActionElement) {
				
				ActionElement actionElement = (ActionElement) eObject;
				
				eObject = actionElement.eContainer();
				
				
			} else if (eObject instanceof BlockUnit) {
				
				
				BlockUnit blockUnit = (BlockUnit) eObject;
				
				eObject = blockUnit.eContainer();
				
				
			} else if (eObject instanceof MethodUnit) {
				
				
				MethodUnit methodUnit = (MethodUnit) eObject;
				
				eObject = methodUnit.eContainer();
				
				
			}
			
			
		}
		
		return segment;
	}
	
	
	public void createInheritanceExtends (ClassUnit superClassUnit, ClassUnit subClassUnit) {
		
		if (superClassUnit != null && subClassUnit != null) {
			
			Extends extendsKDM = CodeFactory.eINSTANCE.createExtends();
			extendsKDM.setTo(superClassUnit);
			extendsKDM.setFrom(subClassUnit);
			
			subClassUnit.getCodeRelation().add(extendsKDM);
			
		}
		
	}

	
}
