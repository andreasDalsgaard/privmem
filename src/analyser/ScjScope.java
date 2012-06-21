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

import com.ibm.wala.ipa.callgraph.ContextItem;
import com.ibm.wala.ipa.callgraph.ContextKey;


public class ScjScope implements ContextItem {
		
	private ScopeName name;	
	private ScjScopeType type;
	
	public ScjScope(String name, ScjScopeType type) {		
		this.name = new ScopeName(name);
		this.type = type;
	}

	public ScjScope(ScopeName name, ScjScopeType type) {		
		this.name = name;
		this.type = type;
	}
	
	@Override
	public boolean equals(Object o) {
	    return (o instanceof ScjScope) && ((ScjScope) o).name.equals(this.name);
	}
	
	@Override
	public String toString() {	    
	    return name.toString() + " " + type.toString();
	}

	@Override
	public int hashCode() {
		return name.hashCode();	//TODO add type
   }

	public ContextKey getName() {
		return this.name;		
	}
	
	public ScjScopeType getScopeType()
	{
		return this.type;
	}

	public ScjScope deepCopy() {		
		return new ScjScope(this.name,this.type);
	}	
}

