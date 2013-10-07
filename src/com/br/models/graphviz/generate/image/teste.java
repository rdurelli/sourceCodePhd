package com.br.models.graphviz.generate.image;

import java.util.ArrayList;
import java.util.List;

import com.br.models.graphviz.AttributeModel;
import com.br.models.graphviz.ClassModel;
import com.br.models.graphviz.MethodModel;



public class teste {

	
	public static void main(String[] args) {
		
		ArrayList<AttributeModel> att = new ArrayList<AttributeModel>();
		
		AttributeModel at1 = new AttributeModel();
		
		at1.setAccesibility("-");
		at1.setName("name");
		at1.setType("String");
		
		att.add(at1);
		
		AttributeModel at2 = new AttributeModel();
		
		at2.setAccesibility("-");
		at2.setName("age");
		at2.setType("Date");

		att.add(at2);
		
		ArrayList<MethodModel> meth = new ArrayList<MethodModel>();
		
		
		MethodModel method = new MethodModel();
		
		method.setAccesibility("+");
		method.setName("getName()");
		
		meth.add(method);
		
		ClassModel c1 = new ClassModel();
		
		c1.setName("Animal");
		
		c1.setAttributes(att);
		
		c1.setMethods(meth);
		
		ClassModel c2 = new ClassModel();
		
		c2.setName("Dog");
		
		c2.setAttributes(att);
		
		c2.setMethods(meth);
		
		
		c2.setParent(c1);
		
		ArrayList<ClassModel> agreegation = new ArrayList<ClassModel>();
		
		
		ClassModel c3 = new ClassModel();
		
		c3.setName("Habitat");
		
		c3.setAttributes(null);
		
		c3.setMethods(null);
		
		
		ClassModel c4 = new ClassModel();
		
		c4.setName("OUTRO");
		
		c4.setAttributes(att);
		
		c4.setMethods(null);
		
		agreegation.add(c3);
		agreegation.add(c4);
		
		
		ClassModel c5 = new ClassModel();
		
		ArrayList<ClassModel> agg = new ArrayList<ClassModel>();
		
		agg.add(c2);
		
		c5.setName("Cat");
		
//		c5.setAggregation(agg);
		
		c5.setAttributes(null);
		
		c5.setMethods(meth);
		
		c5.setParent(c1);
		
		c1.setAggregation(agreegation);
		
		c2.setAggregation(agreegation);
		
		List<ClassModel> classes = new ArrayList<ClassModel>();
		
		
		
		for (int i = 0; i < 20; i++) {
			ClassModel classModeli = new ClassModel();
			classModeli.setName("Class"+i);
			classModeli.setParent(c2);
			classes.add(classModeli);
			
		}
		
		classes.add(c1);
		classes.add(c2);
		classes.add(c3);
		classes.add(c4);
		classes.add(c5);
		
		GenerateImageFactory generate = GenerateImageFactory.getInstance();
		
		generate.createClassGraphviz(classes);
		
	}
	
}
