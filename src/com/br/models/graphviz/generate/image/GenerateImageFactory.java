package com.br.models.graphviz.generate.image;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.br.models.graphviz.AttributeModel;
import com.br.models.graphviz.Elements;
import com.br.models.graphviz.MethodModel;





public class GenerateImageFactory {

	private static GenerateImageFactory instance;
	
	private GenerateImageFactory() {
		
	}
	
	public static GenerateImageFactory getInstance() {
	      if (instance == null)
	         instance = new GenerateImageFactory();
	      return instance;
	}
	
	
	public void createClassGraphviz (List<? extends Elements> classes) {
		
		  GraphViz gv = new GraphViz();
	      gv.addln(gv.start_graph());
	      gv.addln("node[shape=record,style=filled,fillcolor=gray93]");
	      gv.addln("edge[dir=back, arrowtail=empty]");
	      
	      String node = "";
	      for (Elements elementsModel : classes) {
	       if (elementsModel.isClass()) {
	    	   Elements classModel = (Elements) elementsModel;
			node =""+classModel.getNumberClass()+"[label = \"{"+classModel.getName()+"|";
			
			List<AttributeModel> attributes = classModel.getAttributes();
			
			if (attributes != null) {
			
			for (int i = 1; i <= attributes.size(); i++) {
				
				if (i < attributes.size()){
					node+=attributes.get(i-1).getAccesibility()+attributes.get(i-1).getName()+":"+attributes.get(i-1).getType()+"\n";
				}
				else if (i == attributes.size()) {
					
					node+=attributes.get(i-1).getAccesibility()+attributes.get(i-1).getName()+":"+attributes.get(i-1).getType()+"|";
					
				}
				
			}
			} else {
				
				node += "|";
			}
			
			List<MethodModel> methods = classModel.getMethods();
			
			if (methods != null) {
			
			for (int i = 1; i <= methods.size(); i++) {
				
				if (i < methods.size()){
					node+=methods.get(i-1).getAccesibility()+methods.get(i-1).getName()+"\\l";
				}
				else if (i == methods.size()) {
					
					node+=methods.get(i-1).getAccesibility()+methods.get(i-1).getName();
					
				}
				
			}
			} else {
				node += "";
			}
			node+="}\"];";
			gv.addln(node);
//			System.out.println("O NODE Ž " + node);
		} else if (elementsModel.isInterface()) {
			
			Elements interfaceModel = (Elements) elementsModel;
			
			System.out.println("Nome da interface Ž " + interfaceModel);
			
			node =""+interfaceModel.getNumberClass()+"[label = \"{\\<\\<interface\\>\\>\\n"+interfaceModel.getName()+"|";
			
			List<AttributeModel> attributes = interfaceModel.getAttributes();
			
			if (attributes != null) {
			
			for (int i = 1; i <= attributes.size(); i++) {
				
				if (i < attributes.size()){
					node+=attributes.get(i-1).getAccesibility()+attributes.get(i-1).getName()+":"+attributes.get(i-1).getType()+"\n";
				}
				else if (i == attributes.size()) {
					
					node+=attributes.get(i-1).getAccesibility()+attributes.get(i-1).getName()+":"+attributes.get(i-1).getType()+"|";
					
				}
				
			}
			} else {
				
				node += "|";
			}
			
			List<MethodModel> methods = interfaceModel.getMethods();
			
			if (methods != null) {
			
			for (int i = 1; i <= methods.size(); i++) {
				
				if (i < methods.size()){
					node+=methods.get(i-1).getAccesibility()+methods.get(i-1).getName()+"\\l";
				}
				else if (i == methods.size()) {
					
					node+=methods.get(i-1).getAccesibility()+methods.get(i-1).getName();
					
				}
				
			}
			} else {
				node += "";
			}
			node+="}\"];";
			gv.addln(node);
			
			
		}
	      }
	      createInherit(classes, gv);
	      createAggregation(classes, gv);
	      gv.addln(gv.end_graph());
	      System.out.println(gv.getDotSource());
//	      String type = "gif";
//	      String type = "dot";
//	      String type = "fig";    // open with xfig
//	      String type = "pdf";
//	      String type = "ps";
//	      String type = "svg";    // open with inkscape
	      String type = "png";
//	      String type = "plain";
	      File out = new File("/Users/rafaeldurelli/Desktop/durelli." + type);   // Linux
//	      File out = new File("c:/eclipse.ws/graphviz-java-api/out." + type);    // Windows
	      gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
		
	}
	
	
	private void createInherit(List<? extends Elements> classes, GraphViz gv) {
		
		for (Elements elementsModel : classes) {
		 if (elementsModel.isClass()) {	
			 Elements classModel = (Elements) elementsModel;
			if (classModel.getParent() != null) {
				gv.addln(classModel.getParent().getNumberClass()+"->"+classModel.getNumberClass()+";");
				
			}
			if (classModel.getInterfaceParents() != null && classModel.getInterfaceParents().size() > 0) {
				
			ArrayList<Elements> interfacesParent = classModel.getInterfaceParents();
			
			for (Elements interfaceModel : interfacesParent) {
				gv.addln(interfaceModel.getNumberClass()+"->"+classModel.getNumberClass()+";");
			}
				
			}
			
		} 
		}
		
	}
	
	private void createAggregation(List<? extends Elements> classes, GraphViz gv) {
		
		for (Elements elementsModel : classes) {
		if (elementsModel.isClass()){	
			Elements classModel = (Elements) elementsModel;	
			if (classModel.getAggregation() != null) {
				
				List<Elements> aggregation = classModel.getAggregation();
				
				for (Elements classAgregation : aggregation) {
					gv.addln(classAgregation.getNumberClass()+"->"+classModel.getNumberClass()+"[arrowtail=vee style=dashed label=\"<has>\"];");
				}
				
			}
		}
			
		}
		
		
	}
	
}
