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

import com.ibm.wala.ipa.callgraph.ContextKey;

public class ScopeName implements ContextKey {

	private String name;
	
	public ScopeName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	@Override
	public String toString() {	    
	    return name.toString();
	}

	
	public boolean equals(Object o) {
		return (o instanceof ScopeName) && ((ScopeName) o).getName().equals(name);
	}
}
