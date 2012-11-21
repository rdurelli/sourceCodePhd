package com.restphone.jrubyeclipse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.gibello.zql.ZStatement;
import org.gibello.zql.ZUpdate;
import org.gibello.zql.ZqlParser;
import org.jruby.RubyArray;
import org.osgi.framework.BundleContext;

import parser.Teste1;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

  // The plug-in ID
  public static final String PLUGIN_ID = "JrubyEclipsePlugin"; //$NON-NLS-1$

  // The shared instance
  private static Activator   plugin;

  /**
   * The constructor
   */
  public Activator() {
	  
	 
	  
  }

  @Override
  public void start(BundleContext context) throws Exception {
    super.start(context);
    
    String update = "UPDATE PERSONS SET usuario = '   ', usuario2 = '   ' WHERE usuario3 = '   ' AND usuario2 = ' '  ;";
    
    System.out.println("vai vai vai va");
    
    ZqlParser parserSQL = new ZqlParser();
    
    parserSQL.initParser(new ByteArrayInputStream(update
			.getBytes()));
    
    ZStatement st = parserSQL.readStatement();
    
	if (st instanceof ZUpdate) {
		ZUpdate query = (ZUpdate) st;
		
		System.out.println("O nome da tabela Ž " + query.getTable());
	}
	
	Teste1 testCaller = new Teste1();
	
	testCaller.callParser();
    
    TesteGroovyAndJRuby te = new TesteGroovyAndJRuby();
    System.out.println(te.testeImprimi());
    
    
    ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    ScriptEngine scriptEngine = scriptEngineManager.getEngineByExtension("rb");
    
    InputStream is = getBundle().getEntry("/jruby/sayHello.rb").openStream();
    Reader reader = new InputStreamReader(is);
    
    scriptEngine.eval(reader);
    Invocable invocableEngine = (Invocable)scriptEngine;
 if (invocableEngine != null) {
//	 tesate t = new tesate();
//	 invocableEngine.invokeFunction("somar", 1, 2);
	 RubyArray array = (RubyArray)invocableEngine.invokeFunction("somar", 1, 2, "Durelli");
	 for (Object object : array) {
		System.out.println(object);
		
	}
	 
	 te.apreenderGroovy();
// System.out.println(invocableEngine.invokeFunction("somar", 1, 2).getClass());
 }
    
  }

  @Override
  public void stop(BundleContext context) throws Exception {
    plugin = null;
    super.stop(context);
  }

  public static Activator getDefault() {
    return plugin;
  }

  public static ImageDescriptor getImageDescriptor(String path) {
    return imageDescriptorFromPlugin(PLUGIN_ID, path);
  }
}
