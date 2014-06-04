package com.br.test.stereotype;

import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeFactory;
import org.eclipse.gmt.modisco.omg.kdm.code.MethodUnit;
import org.eclipse.gmt.modisco.omg.kdm.kdm.ExtensionFamily;
import org.eclipse.gmt.modisco.omg.kdm.kdm.KdmFactory;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Stereotype;

public class TesteStereotype {

	
	
	public static void main(String[] args) {
		
		ClassUnit pessoa = CodeFactory.eINSTANCE.createClassUnit();
		pessoa.setName("Pessoa");
		pessoa.setIsAbstract(false);
		
		
		MethodUnit methodTeste = CodeFactory.eINSTANCE.createMethodUnit();
		
		
		ExtensionFamily extensionFamily = KdmFactory.eINSTANCE.createExtensionFamily();
		
		extensionFamily.setName("Aspect");
		
		Stereotype stereotype = KdmFactory.eINSTANCE.createStereotype();	
		stereotype.setName("Aspect");
		stereotype.setType("ClassUnit");
		
		
		
		pessoa.getStereotype().add(stereotype);
		
		methodTeste.getStereotype().add(stereotype);
		
		System.out.println(pessoa.getStereotype().get(0).toString());
		
//		stereotype.setType(value)(value)
		
		
	}
	
}
