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

import java.util.Iterator;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.ipa.cha.IClassHierarchy;

public class util {
	private static boolean alreadyShown = false;
	
	public static IClass getIClass(String str, IClassHierarchy cha)
	{
		Iterator<IClass> classes = cha.iterator();
	     
		while (classes.hasNext()) {
			IClass aClass = (IClass) classes.next();
			if (aClass.getName().toString().equals(str))
				return aClass;		
		}
		
		return null;		
	}

	public static void error(String string) {
		System.err.append("Error:" + string + "\n");
		System.exit(1);		
	}

	public static void warn(String string) {
		System.err.append("Warning" + string + "\n");		
	}
	
	public static void warnException() {
		if (alreadyShown == false) {
			System.err.append("Warning: We currently do not check exceptions\n");
			alreadyShown = true; 
		}
	}

	public static void print_usage() {
		error("Usage: Two arguments should be given. \n" +
	      		"\t-primordial jop_scj.jar (if only -application is specified java and javax will be added as primordial).\n" +
	      		"\t-application application.jar jar file containing application.\n" +
	      		"\t-main class_name_containing_main_function(default: use Main)\n\n" +
	      		"TIP: if the jar files are placed in the root of the eclipse workspace dir. prepend 'filename.jar' with: ${workspace_loc}/\n" );
	}
	
	public static <E> void print_iterator(Iterator<E> itr)
	{
		while (itr.hasNext())
		{
			System.out.println(itr.next());
		}
	}
}
