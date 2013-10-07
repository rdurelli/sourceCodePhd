package com.br.models.graphviz.generate.image;
import java.io.File;

import org.stringtemplate.v4.ST;


public class Proba
{
   public static void main(String[] args)
   {
      Proba p = new Proba();
      p.start();
//      p.start2();
   }

   /**
    * Construct a DOT graph in memory, convert it
    * to image and store the image in the file system.
    */
   private void start()
   {
	   
	   
	   String testete = "3[label = \"{<className>| <atr1>\n <atr2>| <meth1>\\l <meth2>}\"];";
	   
	   ST query = new ST(testete);
	   query.add("className", "Product");
	   query.add("atr1", "-name");
	   query.add("atr2", "-value");
	   query.add("meth1", "+getName()");
	   query.add("meth2", "+getValue()");
	   System.out.println("O TEMPLATE " + query.render());
	   
	   String valor = "\"conteudo\"";
      GraphViz gv = new GraphViz();
      gv.addln(gv.start_graph());
      gv.addln("node[shape=record,style=filled,fillcolor=gray93]");
      gv.addln("edge[dir=back, arrowtail=empty]");
      gv.addln("2[label = \"{Rafael |- name\n- age|+getName()\\l +getAge() }\"];");
      gv.addln(query.render());
      gv.addln("2 -> 3[constraint=false, arrowtail=odiamond];");
      gv.addln("A -> B;");
      gv.addln("A -> C;");
      gv.addln("A -> 2;");
      gv.addln("D -> 2;");
      gv.addln("E -> 2;");
      gv.addln("F -> 2;");
      gv.addln("4[label = \"{Patricia|- name\n- age|+getName()\\l +getAge() }\"];");
      gv.addln("5[label = \"{Isabela|- name\n- age|+getName()\\l +getAge() }\"];");
      gv.addln("6[label = \"{Vinicius|- name\n- age|+getName()\\l +getAge() }\"];");
      gv.addln("7[label = \"{Luciana | - name \n- age|+getName()\\l +getAge() }\"];");
      gv.addln("4 -> 6;");
      gv.addln("6 -> 7[dir=none headlabel = \"0..*\" ];");
      gv.addln(gv.end_graph());
      System.out.println(gv.getDotSource());
      
//      String type = "gif";
//      String type = "dot";
//      String type = "fig";    // open with xfig
//      String type = "pdf";
//      String type = "ps";
//      String type = "svg";    // open with inkscape
      String type = "png";
//      String type = "plain";
      File out = new File("/Users/rafaeldurelli/Documents/workspace_mining/testeVai/tmp/rafael." + type);   // Linux
//      File out = new File("c:/eclipse.ws/graphviz-java-api/out." + type);    // Windows
      gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
   }
   
   /**
    * Read the DOT source from a file,
    * convert to image and store the image in the file system.
    */
   private void start2()
   {
      String dir = "/Users/rafaeldurelli/Documents/workspace_kepler/testeVai";     // Linux
      String input = dir + "/sample/simple.dot";
//	   String input = "c:/eclipse.ws/graphviz-java-api/sample/simple.dot";    // Windows
	   
	   GraphViz gv = new GraphViz();
	   gv.readSource(input);
	   System.out.println(gv.getDotSource());
   		
//      String type = "gif";
//    String type = "dot";
//    String type = "fig";    // open with xfig
//    String type = "pdf";
//    String type = "ps";
//    String type = "svg";    // open with inkscape
    String type = "png";
//      String type = "plain";
	   File out = new File("/Users/rafaeldurelli/Desktop/rafael." + type);   // Linux
//	   File out = new File("c:/eclipse.ws/graphviz-java-api/tmp/simple." + type);   // Windows
	   gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
   }
}
