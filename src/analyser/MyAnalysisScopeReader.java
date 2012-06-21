/*******************************************************************************
 * Copyright (c) 2012 Andreas Engelbredt Dalsgaard <andreas.dalsgaard@gmail.com>.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Andreas Engelbredt Dalsgaard <andreas.dalsgaard@gmail.com>
 ******************************************************************************/
package analyser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.core.runtime.Plugin;
import com.ibm.wala.classLoader.Module;
import com.ibm.wala.classLoader.ModuleEntry;
import com.ibm.wala.core.plugin.CorePlugin;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.util.config.AnalysisScopeReader;
import com.ibm.wala.util.io.FileProvider;

public class MyAnalysisScopeReader extends AnalysisScopeReader {
	
	public static AnalysisScope makeJavaBinaryAnalysisScope(String classPath, String scjJar, File exclusionsFile) throws IOException {
	    return makeJavaBinaryAnalysisScope(classPath, scjJar, exclusionsFile, CorePlugin.getDefault());
	  }

	  /**
	   * @param classPath class path to analyze, delimited by File.pathSeparator
	   * @param exclusionsFile file holding class hierarchy exclusions. may be null
	   * @throws IOException 
	   * @throws IllegalStateException if there are problems reading wala properties
	   */
	  public static AnalysisScope makeJavaBinaryAnalysisScope(String application, String primordial, File exclusionsFile, Plugin plugIn) throws IOException {	    
	    AnalysisScope scope = AnalysisScope.createJavaAnalysisScope();
	    
	    
	    if (primordial != null)
	    {
	    	ClassLoaderReference walaLoader = scope.getLoader(AnalysisScope.PRIMORDIAL);
	    	Module M = FileProvider.getJarFileModule(primordial, AnalysisScopeReader.class.getClassLoader());
	    	scope.addToScope(walaLoader, M);
	    	 
	      
	    	if (new File(application).exists()) {
	    		addClassPathToScope(application, scope, scope.getLoader(AnalysisScope.APPLICATION));
	    	} else {
	    		util.error("File not found "+application);
	    	}
	    	
	    } else 
	    {    		   	   
		    Module appMixed = FileProvider.getJarFileModule(application, AnalysisScopeReader.class.getClassLoader());	    
		    Iterator<ModuleEntry> myit = appMixed.getEntries();
		   
		    class MyModule implements Module
		    {
		    	List<ModuleEntry> entries = new ArrayList<ModuleEntry>();
		    	
		    	void addEntry(ModuleEntry entry)
		    	{
		    		this.entries.add(entry);
		    	}
		    	
				@Override
				public Iterator<ModuleEntry> getEntries() {
					return entries.iterator();
				}
		    	
		    }
		    
		    MyModule app = new MyModule(), prim = new MyModule();
		    
		    while (myit.hasNext())
		    {
		    	ModuleEntry entry = myit.next();
		    	if (entry.getClassName().startsWith("java") || 
		    			entry.getClassName().startsWith("com") || 
		    			entry.getClassName().startsWith("joprt") || 
		    			entry.getClassName().startsWith("util/Dbg") || entry.getClassName().startsWith("util/Timer") ){
		      		prim.addEntry(entry);
		    	}		  
		    	else
		    	{
		    		if (entry.isClassFile())
		    			app.addEntry(entry);			    			    		
		    	}
		    	
		    }
		    		    
		    scope.addToScope(scope.getLoader(AnalysisScope.PRIMORDIAL), prim);
		    scope.addToScope(scope.getLoader(AnalysisScope.APPLICATION), app);		    
	    }
	   
	    
	    return scope;
	  }
}
