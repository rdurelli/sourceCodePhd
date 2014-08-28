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
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.gmt.modisco.java.FieldDeclaration;
import org.eclipse.gmt.modisco.java.Model;
import org.eclipse.gmt.modisco.java.emf.JavaPackage;
import org.eclipse.gmt.modisco.java.generation.files.GenerateJavaExtended;
import org.eclipse.gmt.modisco.omg.kdm.action.ActionElement;
import org.eclipse.gmt.modisco.omg.kdm.action.ActionFactory;
import org.eclipse.gmt.modisco.omg.kdm.action.Addresses;
import org.eclipse.gmt.modisco.omg.kdm.action.BlockUnit;
import org.eclipse.gmt.modisco.omg.kdm.action.Reads;
import org.eclipse.gmt.modisco.omg.kdm.code.AbstractCodeElement;
import org.eclipse.gmt.modisco.omg.kdm.code.AbstractCodeRelationship;
import org.eclipse.gmt.modisco.omg.kdm.code.BooleanType;
import org.eclipse.gmt.modisco.omg.kdm.code.CharType;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeFactory;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeItem;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeModel;
import org.eclipse.gmt.modisco.omg.kdm.code.ControlElement;
import org.eclipse.gmt.modisco.omg.kdm.code.Datatype;
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
import org.eclipse.gmt.modisco.omg.kdm.source.AbstractInventoryElement;
import org.eclipse.gmt.modisco.omg.kdm.source.InventoryModel;
import org.eclipse.gmt.modisco.omg.kdm.source.SourceFactory;
import org.eclipse.gmt.modisco.omg.kdm.source.SourceFile;
import org.eclipse.gmt.modisco.omg.kdm.source.SourceRef;
import org.eclipse.gmt.modisco.omg.kdm.source.SourceRegion;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.Document;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.modelgoon.classes.model.Method;
import org.omg.IOP.CodecFactory;

import com.br.constraint.ConstraintAfterToRemoveAClassUnitOnMethodUnit;
import com.br.constraint.ConstraintAfterToRemoveAClassUnitOnStorableUnit;
import com.br.constraint.GenericConstraint;
import com.br.databaseDDL.Column;
import com.br.gui.refactoring.ExtractSuperClassInfo;
import com.br.gui.refactoring.PullUpFieldInfo;
import com.br.gui.refactoring.PullUpMethodInfo;
import com.br.utils.ProjectSelectedToModernize;

public class UtilKDMModel {

	
	private Integer numberOfTheLine;
	
	private UtilASTJDTModel utilASTJDTModel = new UtilASTJDTModel();
	
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
	
	public StorableUnit createStorableUnitInAClassUnit (ClassUnit classUnit, String name, Datatype type) {
		
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
	
	
	/**
	 * Metodo utilizado para obter criar um MethodUnit que representa um GET
	 *
	 * @author rafaeldurelli
	 * @param ClassUnit classUnit
	 * @param String name
	 * @param Datatype type
	 * @param Segment segment
	 * @return MethodUnit retorna um determinado MethodUnit
	 * */ 
	public MethodUnit createMethodUnitGETInClassUnit (ClassUnit classUnit, String name, Datatype type, Segment segment) {
		
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
	/**
	 * Metodo utilizado para obter criar um MethodUnit que representa um SET. PS: Esse metodo tem algumas inconsistências.
	 *
	 * @author rafaeldurelli
	 * @param ClassUnit classUnit
	 * @param String name
	 * @param PrimitiveType type
	 * @param Segment segment
	 * @return MethodUnit retorna um determinado MethodUnit
	 * */ 
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
	
	
	
		/**
		 * Metodo utilizado para obter criar um MethodUnit que representa um SET. PS: Esse metodo tem algumas inconsistências.
		 *
		 * @author rafaeldurelli
		 * @param ClassUnit classUnit
		 * @param String name
		 * @param PrimitiveType type
		 * @param Segment segment
		 * @return MethodUnit retorna um determinado MethodUnit
		 * */ 
	public MethodUnit createMethodUnitSETInClassUnit (ClassUnit classUnit, String name, Datatype type, StorableUnit attribute, Segment segment) {
		
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
	
	public void removeMethodUnit (ClassUnit classUnit, MethodUnit methodUnit) {
		
		
		if ( classUnit!= null) {
			
			classUnit.getCodeElement().remove(methodUnit);
			
		} else {
			Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			MessageDialog.openError(activeShell, "Error", "ClassUnit can not be null");
			
		}
		
		
	}
	
	
	public void actionSuperExtractClass (ClassUnit classUnit, LinkedHashSet<ExtractSuperClassInfo> extractSuperClassInfo) {
		
		
		Iterator<ExtractSuperClassInfo> ite = extractSuperClassInfo.iterator();
		
		Iterator<ExtractSuperClassInfo> ite2 = extractSuperClassInfo.iterator();
		
	
		ArrayList<String> classAlreadyWithInheritance = new ArrayList<String>();
		
		
		
		while (ite.hasNext()) {
			
			boolean alreadyWithInheritance = false;
			
			
			ExtractSuperClassInfo classInfo = ite.next();
			
			ClassUnit classUnitSuper = classInfo.getFrom();
			
			
			for (String className : classAlreadyWithInheritance) {
				
				if (className.equals(classUnitSuper.getName())) {
					
					alreadyWithInheritance = true;
					
				}
				
			}
			
			if (!alreadyWithInheritance) {
				
				this.createInheritanceExtends(classUnit, classUnitSuper);
				classAlreadyWithInheritance.add(classUnitSuper.getName());
				alreadyWithInheritance = false;
			}
				
			
			
		}
		
		while (ite2.hasNext()) {
			
			ExtractSuperClassInfo classInfo = ite2.next();
			
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
	
	public List<MethodUnit> getMethodsUnit (ClassUnit classUnit) {
		
		
		EList<CodeItem> codeElements = classUnit.getCodeElement();
		
		List<MethodUnit> methods = new ArrayList<MethodUnit>();
		
		for (CodeItem element : codeElements) {
			
			if (element instanceof MethodUnit) {
				
				methods.add((MethodUnit)element);
				
			}
			
		}
		return methods;
		
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
	
//	public List<ControlElement> getControlElements (ClassUnit classUnit) {
//		
//		EList<CodeItem> codeElements = classUnit.getCodeElement();
//		
//		List<ControlElement> controlElements = new ArrayList<ControlElement>();
//		
//		for (CodeItem element : codeElements) {
//			
//			if (element instanceof ControlElement ) {
//				
//				ControlElement controlElement = (ControlElement) element;
//				
//				if (controlElement.getAttribute().size()!=0) {
//					
//					controlElements.add((ControlElement)element);
//					
//				}
//				
//				
//				
//			}
//			
//		}
//		return controlElements;
//		
//		
//	}
	
	public StorableUnit getStorablesUnitByName (ClassUnit classUnit, String name) {
		
		EList<CodeItem> codeElements = classUnit.getCodeElement();
		
		StorableUnit storableToReturn = null;
		
		for (CodeItem element : codeElements) {
			
			if (element instanceof StorableUnit && element.getName().equals(name)) {
				
				storableToReturn = (StorableUnit)element;
				
				break;
				
			}
			
		}
		
		return storableToReturn;
		
		
	}
	
	
	/**
	 * Metodo utilizado para obter um objeto do tipo MethodUnit passando o nome e a ClassUnit. 
	 * Para utilizar esse metodo nao precisa passar o nome do metodo completo, ou seja, sem os parametos e parenteses. Por exemplo: 
	 * Para obter um metodo getName() apenas escreva getName
	 *
	 * @author rafaeldurelli
	 * @param ClassUnit classUnit
	 * @param String name representa o nome do method
	 * @return MethodUnit retorna um determinado MethodUnit
	 * */ 
	public MethodUnit getMethodsUnitByName (ClassUnit classUnit, String name) {
		
		
		EList<CodeItem> codeElements = classUnit.getCodeElement();
		
		MethodUnit method = null;
		
		for (CodeItem element : codeElements) {
			
			
			System.out.println(name);
			System.out.println(element.getName());
			
			if (element instanceof MethodUnit && element.getName().startsWith(name)) {
				
				method = (MethodUnit) element;
				break;
			
				
			}
			
		}
		return method;
		
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


	
//	this method is used to save the KDMModel 
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
	/**
	 * @author rafaeldurelli
	 * @param Segment Segment para salvar as alterações realizadas no KDMModel.
	 * @param String name representa o nome 
	 * @param String projectURI representa o caminho onde esta armazenado o KDMMOdel.
	 * @return Resource - retorna um Objeto Resource 
	 * */ 
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
	
	/**
	 * 
	 * 
	 * @author rafaeldurelli
	 * @param ClassUnit classUnit para obter uma representação do Package do KDM 
	 * @return  String[] - essa string contem todos os pacotes de uma determina ClassUnit.
	 * */ 
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
	
	
	/**
	 * @author rafaeldurelli
	 * @param KDMEntity passa uma KDMEntity (ClassUnit, MethodUnit, StorableUnit) e ele ira fazer uma recursão para buscar o objeto Segmento para fazer a persistencia
	 * @return Segment - retorna o Segmento para realizar as mudanças e depois persistir.
	 * */ 
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
	
	
	public boolean verifyInheritanceExtends (ClassUnit classOne, ClassUnit classTwo) {
		
		EList<AbstractCodeRelationship> relationShips = classOne.getCodeRelation();
		
		EList<AbstractCodeRelationship> relationShipsClassTwo = classTwo.getCodeRelation();
		
		for (AbstractCodeRelationship abstractCodeRelationship : relationShips) {
			
			if (abstractCodeRelationship instanceof Extends) {
				
				Extends extends1 = (Extends) abstractCodeRelationship;
				
				for (AbstractCodeRelationship abstractCodeRelationship2 : relationShipsClassTwo) {
					
					if (abstractCodeRelationship2 instanceof Extends) {
						
						Extends extends2 = (Extends) abstractCodeRelationship2;
						
						if (extends1.getTo().getName().equals(extends2.getTo().getName())) {
							
							return true;
							
						}
						
					}
					
				}
				
			}
			
		}
		return false;
		
	}
	
	
	public void actionPullDownField (ClassUnit classToRemoveTheStorableUnit, List<ClassUnit> classesToPullDownTheStorableUnit, List<StorableUnit> storableUnitsToPullDown) {
		
		if (classesToPullDownTheStorableUnit.size() == 1) {
			
			for (StorableUnit storableUnit : storableUnitsToPullDown) {
				
				ClassUnit classToMoveTheStorableUnit = classesToPullDownTheStorableUnit.get(0);
				
				this.moveStorableUnitToClassUnit(classToMoveTheStorableUnit, storableUnit);
			}
			
		}else {
			
			for (ClassUnit classesUnits : classesToPullDownTheStorableUnit) {
				
				for (StorableUnit storableUnit : storableUnitsToPullDown) {
				
					this.createStorableUnitInAClassUnit(classesUnits, storableUnit.getName(), storableUnit.getType());
					
				}
				
			}
			
			for (StorableUnit storableUnit : storableUnitsToPullDown) {
				
				this.removeStorableUnit(classToRemoveTheStorableUnit, storableUnit);
			}
			
			
		}
		
		
		
	}
	
	public void actionPullDownMethod (ClassUnit classToRemoveTheMethodUnit, List<ClassUnit> classesToPullDownTheMethodUnit, List<MethodUnit> methodUnitToPullDown) {
		
		if (classesToPullDownTheMethodUnit.size() == 1) {
			
			for (MethodUnit methodUnit : methodUnitToPullDown) {
				
				ClassUnit classToMoveTheStorableUnit = classesToPullDownTheMethodUnit.get(0);
				
				this.moveMethodUnitToClassUnit(classToMoveTheStorableUnit, methodUnit);
				
			}
			
		}else {
			
			for (ClassUnit classesUnits : classesToPullDownTheMethodUnit) {
				
				for (MethodUnit methodUnit : methodUnitToPullDown) {
				

					MethodUnit methodNew = methodUnit;
					
					
					EList<CodeItem> codeItem = classesUnits.getCodeElement();
					
					codeItem.add(methodNew);
					
					this.moveMethodUnitToClassUnit(classToRemoveTheMethodUnit, methodNew);
//					this.createStorableUnitInAClassUnit(classesUnits, methodUnit.getName(), methodUnit.getType());
					
				}
				
			}
			
			for (MethodUnit methodUnit : methodUnitToPullDown) {
				
				this.removeMethodUnit(classToRemoveTheMethodUnit, methodUnit);
				
//				this.removeStorableUnit(classToRemoveTheMethodUnit, storableUnit);
			}
			
			
		}
		
		
		
	}
	
	public void actionInLineClass (ClassUnit classUnitSelectedToInline1, ClassUnit classUnitSelectedToInline2, Package packageToRemoveTheClass, StorableUnit storableUnitToRemove, Segment segment) {
		
		List<StorableUnit> storableUnits = this.getStorablesUnit(classUnitSelectedToInline2);
		
		String nameOFTheClasseToDelete = classUnitSelectedToInline2.getName();
		
		for (StorableUnit storableUnit : storableUnits) {
			
			this.moveStorableUnitToClassUnit(classUnitSelectedToInline1, storableUnit);//move all storableUnit
			
		}
		
		List<MethodUnit> methodUnits = this.getMethodsUnit(classUnitSelectedToInline2);
		
		for (MethodUnit methodUnit : methodUnits) {
			this.moveMethodUnitToClassUnit(classUnitSelectedToInline1, methodUnit);
		}
		
		classUnitSelectedToInline1.getCodeElement().remove(storableUnitToRemove);
		
		EList<AbstractCodeElement> allClasses = packageToRemoveTheClass.getCodeElement();
		allClasses.remove(classUnitSelectedToInline2);
		
		//remove the SourceFile of the deleted ClassUnit
		
		EList<KDMModel> kdmMOdels = segment.getModel();
		InventoryModel inventory = null;
		for (KDMModel kdmModel : kdmMOdels) {
			if (kdmModel instanceof InventoryModel) {
				
				inventory = (InventoryModel) kdmModel;
				break;
			}
		}
		
		if (inventory != null) {
			
			EList<AbstractInventoryElement> allInvetory = inventory.getInventoryElement();
			
			for (AbstractInventoryElement abstractInventoryElement : allInvetory) {
				if (abstractInventoryElement.getName().equals(nameOFTheClasseToDelete+".java")) {
					
					allInvetory.remove(abstractInventoryElement);
					break;
				}
			}
			
		}
		
	}
	
	public void actionPullUpField(LinkedHashSet<PullUpFieldInfo> pullUpFieldInfo) {

		ClassUnit superClass = null;

		ArrayList<String> alreadyRemoveStorableUnit = new ArrayList<String>();

		Iterator<PullUpFieldInfo> ite2 = pullUpFieldInfo.iterator();

		Iterator<PullUpFieldInfo> ite = pullUpFieldInfo.iterator();

		while (ite2.hasNext()) {

			PullUpFieldInfo classInfo = ite2.next();

			moveStorableUnitToClassUnit(
					(ClassUnit) classInfo.getSuperElement(),
					classInfo.getStorableUnitTo());
			moveStorableUnitToClassUnit(
					(ClassUnit) classInfo.getSuperElement(),
					classInfo.getStorableUnitFROM());

		}

		while (ite.hasNext()) {

			PullUpFieldInfo pullUpFieldInfo2 = (PullUpFieldInfo) ite.next();

			EList<CodeItem> elements = ((ClassUnit) pullUpFieldInfo2
					.getSuperElement()).getCodeElement();

			superClass = (ClassUnit) pullUpFieldInfo2.getSuperElement();

			System.out.println(elements.size());

			if (!alreadyRemoveStorableUnit.contains(superClass.getName())) {

				for (int i = 0; i < elements.size(); i++) {

					for (int j = 0; j < elements.size(); j++) {

						if (elements.get(i) instanceof StorableUnit
								&& elements.get(i).getName()
										.equals(elements.get(j).getName())) {

							StorableUnit elementToRemove = (StorableUnit) elements
									.get(j);

							// não é a melhor ideia, pois tem lugares que tem
							// referencia ao attributo removido..
							removeStorableUnit(
									(ClassUnit) pullUpFieldInfo2
											.getSuperElement(),
									elementToRemove);

						}

					}

					alreadyRemoveStorableUnit.add(superClass.getName());
					System.out.println(superClass.getCodeElement());
				}
			}
		}

	}
	
	public void actionPullUpMethod(
			LinkedHashSet<PullUpMethodInfo> pullUpMethodInfo) {

		ClassUnit superClass = null;

		ArrayList<String> alreadyRemoveMethodUnit = new ArrayList<String>();

		Iterator<PullUpMethodInfo> ite2 = pullUpMethodInfo.iterator();

		Iterator<PullUpMethodInfo> ite = pullUpMethodInfo.iterator();

		while (ite2.hasNext()) {

			PullUpMethodInfo classInfo = ite2.next();

			moveMethodUnitToClassUnit((ClassUnit) classInfo.getSuperElement(),
					classInfo.getMethodUnitTo());
			moveMethodUnitToClassUnit((ClassUnit) classInfo.getSuperElement(),
					classInfo.getMethodUnitFROM());

		}

		while (ite.hasNext()) {

			PullUpMethodInfo pullUpFieldInfo2 = (PullUpMethodInfo) ite.next();

			EList<CodeItem> elements = ((ClassUnit) pullUpFieldInfo2
					.getSuperElement()).getCodeElement();

			// List<MethodUnit> elements =
			// this.getMethodsUnit(((ClassUnit)pullUpFieldInfo2.getSuperElement()));

			superClass = (ClassUnit) pullUpFieldInfo2.getSuperElement();

			System.out.println(elements.size());

			if (!alreadyRemoveMethodUnit.contains(superClass.getName())) {

				for (int i = 0; i < elements.size(); i++) {

					for (int j = 0; j < elements.size(); j++) {

						if ((elements.get(i) instanceof MethodUnit)
								&& (elements.get(i).getName().equals(elements
										.get(j).getName()))) {

							MethodUnit elementToRemove = (MethodUnit) elements
									.get(j);

							// não é a melhor ideia, pois tem lugares que tem
							// referencia ao attributo removido..
							removeMethodUnit(
									(ClassUnit) pullUpFieldInfo2
											.getSuperElement(),
									elementToRemove);

						}

					}

				}

				alreadyRemoveMethodUnit.add(superClass.getName());
				System.out.println(superClass.getCodeElement());
			}
		}
	}		
	
	public void createInheritanceExtends (ClassUnit superClassUnit, ClassUnit subClassUnit) {
		
		if (superClassUnit != null && subClassUnit != null) {
			
			Extends extendsKDM = CodeFactory.eINSTANCE.createExtends();
			extendsKDM.setTo(superClassUnit);
			extendsKDM.setFrom(subClassUnit);
			
			subClassUnit.getCodeRelation().add(extendsKDM);
			
		}
		
	}
	
	
	public ClassUnit getClassUnit (Segment segment, String name) {
		
		ArrayList<ClassUnit> allClasses = this.getAllClasses(segment);
		
		ClassUnit classToReturn = null;
		
		for (ClassUnit classUnit : allClasses) {
			if (classUnit.getName().equals(name)) {
				
				classToReturn = classUnit;
				break;
				
			}
		}
		
		
		return classToReturn;
		
	}
	
	public ArrayList<ClassUnit> getAllClasses (Segment segment) {
		
		ArrayList<ClassUnit> allClasses = new ArrayList<ClassUnit>();
		
		CodeModel codeModel = (CodeModel)segment.getModel().get(0);
		
		EList<AbstractCodeElement> elements = codeModel.getCodeElement();
		
		for (int i = 0; i < elements.size()-1; i++) {
			
			System.out.println(elements.get(i));
			
			if (elements.get(i) instanceof Package) {
			
				Package packageKDM = (Package) elements.get(i);
				
				this.getClasses(packageKDM.getCodeElement(), allClasses);
				
			}
			
			
			
			
		}
		
		
		return allClasses;
		
		
	}
	
	private void getClasses(EList<AbstractCodeElement> elements, ArrayList<ClassUnit> allClasses) {
		
		for (AbstractCodeElement abstractCodeElement : elements) {
			
			if (abstractCodeElement instanceof ClassUnit) {
				
				allClasses.add((ClassUnit)abstractCodeElement);
				
			} else if (abstractCodeElement instanceof Package) {
				
				Package packageToPass = (Package) abstractCodeElement;
				
				getClasses(packageToPass.getCodeElement(), allClasses);
				
			}
			
		}
				
	}
	
	public ArrayList<ClassUnit> getRelationShipInheritancePassingTheSuper(ClassUnit classUnit, ArrayList<ClassUnit> allClasses) {
		
		ArrayList<ClassUnit> relationShipInheritancePassingTheSuper = new ArrayList<ClassUnit>();
		
		for (ClassUnit classes : allClasses) {
			
			EList<AbstractCodeRelationship> codeRelationShip = classes.getCodeRelation();
			
			for (AbstractCodeRelationship abstractCodeRelationship : codeRelationShip) {
				
				if (abstractCodeRelationship instanceof Extends) {
					
					Extends extendsKDM = (Extends) abstractCodeRelationship;
					
					if (extendsKDM.getTo() instanceof ClassUnit) {
					
						Datatype dataType = extendsKDM.getTo();
						
						if (dataType.getName().equals(classUnit.getName())) {
							
							relationShipInheritancePassingTheSuper.add(classes);
							
						}
						
					}
					
					
					
					
				}
				
			}
			
		}
		return relationShipInheritancePassingTheSuper ;
				
	}
	
	public void verifyConstraintForClassUnit(Segment segment, String projectName, ClassUnit classUnitToVerifyTheConstraint, IProject currentProject) {
		
		
		ArrayList<ClassUnit> allClassOfTheSystem = null;
		
		EList<KDMModel> kdmModels = segment.getModel();
		
		List<GenericConstraint> allConstraintRelatedToRemoveAClassUnit = new ArrayList<GenericConstraint>();
		
		CodeModel codeModelThatRepresentTheSystem = null;
		
		for (KDMModel kdmModel : kdmModels) {
			if (kdmModel instanceof CodeModel) {
				
				CodeModel temp = (CodeModel) kdmModel;
				
				if (temp.getName().equals(projectName)) {
					
					codeModelThatRepresentTheSystem = temp;
					break;
				}
				
			}
		}
		
		allClassOfTheSystem = this.getAllClasses(segment);
		
		for (ClassUnit classesToVerify : allClassOfTheSystem) {
			
			List<StorableUnit> storablesUnits = this.getStorablesUnit(classesToVerify);
			
			List<MethodUnit> methodUnits = this.getMethodsUnit(classesToVerify);
			
//			List<ControlElement> controlElements = this.getControlElements(classesToVerify);
			
			for (MethodUnit methodUnit : methodUnits) {
				this.verifyMethodUnitBeforeRemoveAClassUnit(methodUnit, classUnitToVerifyTheConstraint, currentProject, allConstraintRelatedToRemoveAClassUnit);
			}
			
			for (StorableUnit storableUnit : storablesUnits) {
				this.verifyStorableUnitBeforeRemoveAClassUnit(storableUnit, classUnitToVerifyTheConstraint, currentProject, allConstraintRelatedToRemoveAClassUnit);
			}
			
//			for (ControlElement controlElement : controlElements) {
//				this.verifyControlElementBeforeRemoveAClassUnit(controlElement, classUnitToVerifyTheConstraint, currentProject, allConstraintRelatedToRemoveAClassUnit);
//			}
			
		}
		
		System.out.println(allConstraintRelatedToRemoveAClassUnit.size());
	}
	
	
//	private void verifyControlElementBeforeRemoveAClassUnit(ControlElement controlElementToVerify, ClassUnit classUnitThatWillBeRemoved, IProject currentProject, List<GenericConstraint> allConstraintRelatedToRemoveAClassUnit) {
//		
//		ClassUnit classUnitThatContainsTheControlElement = (ClassUnit) controlElementToVerify.eContainer();
//		
//		String[] packageString = this.getCompletePackageName(classUnitThatContainsTheControlElement);
//		
//		String[] packageOfTheClassThatWillBeRemoved = this.getCompletePackageName(classUnitThatWillBeRemoved);
//		
//		EList<AbstractCodeElement> allElements = controlElementToVerify.getCodeElement();
//		
//		for (AbstractCodeElement abstractCodeElement : allElements) {
//			if (abstractCodeElement instanceof ActionElement) {
//				
//				ActionElement actionElement  = (ActionElement) abstractCodeElement;
//				
//				EList<AbstractCodeElement> allElementsOfActionElement  = actionElement.getCodeElement();
//				
//				for (AbstractCodeElement abstractCodeElement2 : allElementsOfActionElement) {
//					
//					if (abstractCodeElement2 instanceof StorableUnit) {
//						
//						StorableUnit storableUnitToVerify = (StorableUnit) abstractCodeElement2;
//						
//						this.verifyStorableUnitBeforeRemoveAClassUnit(storableUnitToVerify, classUnitThatWillBeRemoved, currentProject, allConstraintRelatedToRemoveAClassUnit);
//						
//					}
//					
//				}
//				
//			}
//		}
//		
//		
//	}



	private void verifyMethodUnitBeforeRemoveAClassUnit (MethodUnit methodUnitToVerify, ClassUnit classUnitThatWillBeRemoved, IProject currentProject, List<GenericConstraint> allConstraintRelatedToRemoveAClassUnit) {
		
		ClassUnit classUnitThatContainsTheStorableUnit = (ClassUnit) methodUnitToVerify.eContainer();
		
		if (classUnitThatContainsTheStorableUnit.getName().equals("A1")) {
			
			System.out.println("OK");
			
		}
		
		String[] packageString = this.getCompletePackageName(classUnitThatContainsTheStorableUnit);
		
		String[] packageOfTheClassThatWillBeRemoved = this.getCompletePackageName(classUnitThatWillBeRemoved);
		
		if (methodUnitToVerify.getType() instanceof Signature && methodUnitToVerify.getKind().equals(MethodKind.CONSTRUCTOR)) {
			
			
			
			Signature signatureOfTheMethod = (Signature) methodUnitToVerify.getType();
			
			EList<ParameterUnit> parameters = signatureOfTheMethod.getParameterUnit();
			
			String[] parameterNames = new String[parameters.size()];
			
			if (parameters.size() != 0) {
			
				for (int i = 0; i < parameters.size(); i++) {
				
					parameterNames[i] = parameters.get(i).getName();//pega todos os nomes do parametro
					
				}
				
				for (ParameterUnit parameter : parameters) {
					
					if (parameter.getType() instanceof ClassUnit) {
						
						ClassUnit toVerify = (ClassUnit) parameter.getType();
						
						String[] packageOfTheClassUnitThatContainsTheMethodUnit = this.getCompletePackageName(toVerify);
						
						boolean thePackagesAreEquals = Arrays.deepEquals(packageOfTheClassUnitThatContainsTheMethodUnit, packageOfTheClassThatWillBeRemoved);
						
						if ( ( thePackagesAreEquals) && ( toVerify.getName().equals(classUnitThatWillBeRemoved.getName() ) ) ) {
							
							ConstraintAfterToRemoveAClassUnitOnMethodUnit constraint = new ConstraintAfterToRemoveAClassUnitOnMethodUnit();
							
//							ConstraintAfterToRemoveAClassUnitOnStorableUnit constraint = new ConstraintAfterToRemoveAClassUnitOnStorableUnit(); criar uma aqui para METHOD
							
							try {
								ICompilationUnit  iCompilationUnitThatRepresentTheClassUnit = utilASTJDTModel.getClassByClassUnit(classUnitThatContainsTheStorableUnit, currentProject, packageString);
								
								final IMethod method = utilASTJDTModel.getIMethodByNameAndPararameter(iCompilationUnitThatRepresentTheClassUnit, methodUnitToVerify.getName(), parameterNames, parameterNames.length);
								
								this.getTheLineNumberOfAIMethodConstructor(iCompilationUnitThatRepresentTheClassUnit, method, parameterNames, parameterNames.length);
								
//								final IField field = utilASTJDTModel.getIFieldByName(iCompilationUnitThatRepresentTheClassUnit, storableUnitToVerify.getName());
								
//								List<IMethod> allMethods = utilASTJDTModel.getAllMethod(iCompilationUnitThatRepresentTheClassUnit);
								
								
//								this.getTheLineNumberOfAIField(iCompilationUnitThatRepresentTheClassUnit, field);//atribui o valor para o atributo numberOfLine
								
							} catch (JavaModelException e) {
								
								e.printStackTrace();
							} catch (CoreException e) {
								
								e.printStackTrace();
							}
							
							
							constraint.setClassUnitThatWasRemoved(classUnitThatWillBeRemoved.getName());
							constraint.setClassThatOwnsTheIrregularMethodUnit(classUnitThatContainsTheStorableUnit);
							constraint.setNumberOfTheLine(this.numberOfTheLine);
							constraint.setMethodUnit(methodUnitToVerify);
							constraint.setIsConstructor(true);
							allConstraintRelatedToRemoveAClassUnit.add(constraint);
							
						}
						
					}
					
				}
							
			}
			
		} else if (methodUnitToVerify.getType() instanceof Signature && methodUnitToVerify.getKind().equals(MethodKind.METHOD)) {
			
			Signature signatureOfTheMethod = (Signature) methodUnitToVerify.getType();
			
			if (methodUnitToVerify.getName().equals("teste")) {
				
				System.out.println("AQUI");
			}
			
			EList<ParameterUnit> parameters = signatureOfTheMethod.getParameterUnit();
			
			String[] parameterNames = new String[parameters.size()];
			
			if (parameters.size() != 0) {
			
				for (int i = 0; i < parameters.size(); i++) {
				
					if (!(parameters.get(i).getKind().equals(ParameterKind.RETURN))) {
					
						parameterNames[i] = parameters.get(i).getName();//pega todos os nomes do parametro
						
					}
					
				}
				
				parameterNames = clean(parameterNames);
				
				for (ParameterUnit parameter : parameters) {
					
					if (parameter.getType() instanceof ClassUnit) {
						
						ClassUnit toVerify = (ClassUnit) parameter.getType();
						
						String[] packageOfTheClassUnitThatContainsTheMethodUnit = this.getCompletePackageName(toVerify);
						
						boolean thePackagesAreEquals = Arrays.deepEquals(packageOfTheClassUnitThatContainsTheMethodUnit, packageOfTheClassThatWillBeRemoved);
						
						if ( ( thePackagesAreEquals) && ( toVerify.getName().equals(classUnitThatWillBeRemoved.getName() ) ) ) {
							
							ConstraintAfterToRemoveAClassUnitOnMethodUnit constraint = new ConstraintAfterToRemoveAClassUnitOnMethodUnit();
							
//							ConstraintAfterToRemoveAClassUnitOnStorableUnit constraint = new ConstraintAfterToRemoveAClassUnitOnStorableUnit(); criar uma aqui para METHOD
							
							try {
								ICompilationUnit  iCompilationUnitThatRepresentTheClassUnit = utilASTJDTModel.getClassByClassUnit(classUnitThatContainsTheStorableUnit, currentProject, packageString);
								
								final IMethod method = utilASTJDTModel.getIMethodByNameAndPararameter(iCompilationUnitThatRepresentTheClassUnit, methodUnitToVerify.getName(), parameterNames, parameterNames.length);
								
								this.getTheLineNumberOfAIMethod(iCompilationUnitThatRepresentTheClassUnit, method, parameterNames, parameterNames.length);
								
//								final IField field = utilASTJDTModel.getIFieldByName(iCompilationUnitThatRepresentTheClassUnit, storableUnitToVerify.getName());
								
//								List<IMethod> allMethods = utilASTJDTModel.getAllMethod(iCompilationUnitThatRepresentTheClassUnit);
								
								
//								this.getTheLineNumberOfAIField(iCompilationUnitThatRepresentTheClassUnit, field);//atribui o valor para o atributo numberOfLine
								
							} catch (JavaModelException e) {
								
								e.printStackTrace();
							} catch (CoreException e) {
								
								e.printStackTrace();
							}
							
							
							constraint.setClassUnitThatWasRemoved(classUnitThatWillBeRemoved.getName());
							constraint.setClassThatOwnsTheIrregularMethodUnit(classUnitThatContainsTheStorableUnit);
							constraint.setNumberOfTheLine(this.numberOfTheLine);
							constraint.setMethodUnit(methodUnitToVerify);
							constraint.setIsConstructor(false);
							allConstraintRelatedToRemoveAClassUnit.add(constraint);
							
						}
						
					}
					
				}
							
			}
			
		}
		
		
	}
	
	private void verifyStorableUnitBeforeRemoveAClassUnit (StorableUnit storableUnitToVerify, ClassUnit classUnitThatWillBeRemoved, IProject currentProject, List<GenericConstraint> allConstraintRelatedToRemoveAClassUnit) {
		
		ClassUnit classUnitThatContainsTheStorableUnit = (ClassUnit) storableUnitToVerify.eContainer();
		
		String[] packageString = this.getCompletePackageName(classUnitThatContainsTheStorableUnit);
		
		String[] packageOfTheClassThatWillBeRemoved = this.getCompletePackageName(classUnitThatWillBeRemoved);
		
		if (storableUnitToVerify.getType() instanceof ClassUnit) {
			
			ClassUnit toVerify = (ClassUnit) storableUnitToVerify.getType();
			
			String[] packageOfTheClassUnitThatContainsTheStorableUnit = this.getCompletePackageName(toVerify);
			
			boolean thePackagesAreEquals = Arrays.deepEquals(packageOfTheClassUnitThatContainsTheStorableUnit, packageOfTheClassThatWillBeRemoved);
			
			if ( ( thePackagesAreEquals) && ( toVerify.getName().equals(classUnitThatWillBeRemoved.getName() ) ) ) {
				
				ConstraintAfterToRemoveAClassUnitOnStorableUnit constraint = new ConstraintAfterToRemoveAClassUnitOnStorableUnit();
	
				try {
					ICompilationUnit  iCompilationUnitThatRepresentTheClassUnit = utilASTJDTModel.getClassByClassUnit(classUnitThatContainsTheStorableUnit, currentProject, packageString);
					
					final IField field = utilASTJDTModel.getIFieldByName(iCompilationUnitThatRepresentTheClassUnit, storableUnitToVerify.getName());
					
//					List<IMethod> allMethods = utilASTJDTModel.getAllMethod(iCompilationUnitThatRepresentTheClassUnit);
					
					
					this.getTheLineNumberOfAIField(iCompilationUnitThatRepresentTheClassUnit, field);//atribui o valor para o atributo numberOfLine
					
				} catch (JavaModelException e) {
					
					e.printStackTrace();
				} catch (CoreException e) {
					
					e.printStackTrace();
				}
				
				constraint.setClassUnitThatWasRemoved(classUnitThatWillBeRemoved.getName());
				constraint.setClassThatOwnsTheIrregularStorableUnit(classUnitThatContainsTheStorableUnit);
				constraint.setNumberOfTheLine(this.numberOfTheLine);
				constraint.setStorableUnit(storableUnitToVerify);
				
				allConstraintRelatedToRemoveAClassUnit.add(constraint);
				
				System.out.println("The Class "+ classUnitThatContainsTheStorableUnit.getName() + " contains an attribute of the type " + classUnitThatWillBeRemoved.getName());
				
			}
			
		}
	}



	private Integer getTheLineNumberOfAIField(
			ICompilationUnit iCompilationUnitThatRepresentTheClassUnit,
			final IField field) {
		 
		
		final ASTParser p = ASTParser.newParser(AST.JLS4);
		p.setKind(ASTParser.K_COMPILATION_UNIT);
		p.setResolveBindings(true); 
		p.setSource(field.getTypeRoot());				
		
//		ASTNode unitNode = p.createAST(new NullProgressMonitor());
		final org.eclipse.jdt.core.dom.CompilationUnit comp = (CompilationUnit) p.createAST(null);
		
		comp.accept(new ASTVisitor() {
	        @Override
	        public boolean visit(VariableDeclarationFragment node) {
	            IJavaElement element = node.resolveBinding().getJavaElement();
	            if (field.equals(element)) {
	                org.eclipse.jdt.core.dom.FieldDeclaration fieldDeclaration = (org.eclipse.jdt.core.dom.FieldDeclaration)node.getParent();
	                
	                numberOfTheLine = comp.getLineNumber(fieldDeclaration.getStartPosition() - 1);
	                 
	                
	            }
	            return false;
	        }
	    });
		
		
		
		return this.numberOfTheLine;
		
		
	}
	
	private Integer getTheLineNumberOfAIMethodConstructor(
			ICompilationUnit iCompilationUnitThatRepresentTheClassUnit,
			final IMethod method, final String[] parameterNames, final int numberOfParameter) throws JavaModelException {
		 
		
		
		final ASTParser p = ASTParser.newParser(AST.JLS4);
		p.setKind(ASTParser.K_COMPILATION_UNIT);
		p.setResolveBindings(true); 
		
		p.setSource(method.getTypeRoot());
				
		
//		ASTNode unitNode = p.createAST(new NullProgressMonitor());
		final org.eclipse.jdt.core.dom.CompilationUnit comp = (CompilationUnit) p.createAST(null);
		
		comp.accept(new ASTVisitor() {
	        @Override
	        public boolean visit(MethodDeclaration node) {
	            
	        	System.out.println(node.getName());
	        	
	        	System.out.println(method.getElementName());
	        	
	        	List<Object> parameter = node.parameters();
	        	
	        	final String[] parameterNamesToVerify = new String[numberOfParameter];
	        	
	        	for (int i = 0; i < node.parameters().size(); i++) {
	        		
	        		VariableDeclaration variableDeclaration = (VariableDeclaration) parameter.get(i);
	        		String parameterName = variableDeclaration.getStructuralProperty(SingleVariableDeclaration.TYPE_PROPERTY).toString();
					
	        		parameterNamesToVerify[i] = variableDeclaration.getName().toString();
	        		
				}
	        	
	        	final boolean theyAreEquals = Arrays.deepEquals(parameterNamesToVerify, parameterNames);
	        	
	        	if (theyAreEquals && node.isConstructor()) {
	        		
	        		numberOfTheLine = comp.getLineNumber(node.getStartPosition());
	        		System.out.println(numberOfTheLine);
	        		
	        	}     	
	            return true;
	        }
	    });
		
		
		
		return this.numberOfTheLine;
		
		
	}
	
	private Integer getTheLineNumberOfAIMethod(
			ICompilationUnit iCompilationUnitThatRepresentTheClassUnit,
			final IMethod method, final String[] parameterNames, final int numberOfParameter) throws JavaModelException {
		 
		
		
		final ASTParser p = ASTParser.newParser(AST.JLS4);
		p.setKind(ASTParser.K_COMPILATION_UNIT);
		p.setResolveBindings(true); 
		
		p.setSource(method.getTypeRoot());
				
		
//		ASTNode unitNode = p.createAST(new NullProgressMonitor());
		final org.eclipse.jdt.core.dom.CompilationUnit comp = (CompilationUnit) p.createAST(null);
		
		comp.accept(new ASTVisitor() {
	        @Override
	        public boolean visit(MethodDeclaration node) {
	            
	        	System.out.println(node.getName());
	        	
	        	System.out.println(method.getElementName());
	        	
	        	List<Object> parameter = node.parameters();
	        	
	        	final String[] parameterNamesToVerify = new String[numberOfParameter];
	        	
	        	for (int i = 0; i < node.parameters().size(); i++) {
	        		
	        		VariableDeclaration variableDeclaration = (VariableDeclaration) parameter.get(i);
	        		String parameterName = variableDeclaration.getStructuralProperty(SingleVariableDeclaration.TYPE_PROPERTY).toString();
					
	        		parameterNamesToVerify[i] = variableDeclaration.getName().toString();
	        		
				}
	        	
	        	final boolean theyAreEquals = Arrays.deepEquals(parameterNamesToVerify, parameterNames);
	        	
	        	if (theyAreEquals) {
	        		
	        		numberOfTheLine = comp.getLineNumber(node.getStartPosition());
	        		System.out.println(numberOfTheLine);
	        		
	        	}     	
	            return true;
	        }
	    });
		
		
		
		return this.numberOfTheLine;
		
		
	}
	
	
	private static String[] clean(final String[] v) {
	    List<String> list = new ArrayList<String>(Arrays.asList(v));
	    list.removeAll(Collections.singleton(null));
	    return list.toArray(new String[list.size()]);
	}
	
	
}
