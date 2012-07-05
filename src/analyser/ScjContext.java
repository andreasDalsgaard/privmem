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

import java.util.HashSet;
import java.util.Iterator;

import com.ibm.wala.ipa.callgraph.Context;
import com.ibm.wala.ipa.callgraph.ContextItem;
import com.ibm.wala.ipa.callgraph.ContextKey;
import analyser.ScjScope;


public class ScjContext implements Context {

	ScjScopeStack scopeStack;
	private ScjScope lastGetCurrentMemoryScope = null;
	
	public ScjContext(ScjContext parent, String scopeName, ScjScopeType type) {

		if (scopeName == null)
	    	throw new IllegalArgumentException("null scope");
  		    	
	    if (parent == null && type != ScjScopeType.IMMORTAL)
	    	throw new IllegalArgumentException("null parent");
	    
	    if (parent != null)
	    	this.setLastGetCurrentScope(parent.getLastGetCurrentScope());
	    
	    if (type == ScjScopeType.IMMORTAL)
	    {
	    	this.scopeStack = new ScjScopeStack();	    	
	    }
	    else
	    {
	    	this.scopeStack = parent.getScopeStack();
	    }
	    
	    this.scopeStack.add(new ScjScope(scopeName, type));	    
	}
	
	/*** Copy constructor ***/
	public ScjContext(ScjContext parent) 
	{
		this.scopeStack = parent.getScopeStack();
		this.setLastGetCurrentScope(parent.getLastGetCurrentScope());
	}
	
	private ScjScopeStack getScopeStack()
	{		
		return this.scopeStack.deepCopy();
	}

	public boolean less(ScjContext c)
	{
		if (c == null)
			return false;
		
		return this.scopeStack.less(c.scopeStack);
	}
	
	@Override
	public boolean equals(Object o) {		
	    return (o instanceof ScjContext) && ((ScjContext) o).scopeStack.toString().equals(this.scopeStack.toString());
	}

	@Override
	public int hashCode() {
	    return scopeStack.toString().hashCode();
	}

	@Override
	public String toString() {
	    return this.getLastGetCurrentScope()+" "+scopeStack.toString();
	}
	
	public ContextItem get(ContextKey name) {
							  	
	  	Iterator<ScjScope> ScjScopes = this.scopeStack.iterator();
		
		while( ScjScopes.hasNext() )
		{
			ScjScope scope = ScjScopes.next();
			if (scope.getName().equals(name) )
				return scope; 
		}
		
	    return null;
	}
	
	public ScjScope getStackTop()
	{		
		return this.scopeStack.getLast(); 
	}

	public ScjScope getLastGetCurrentScope() {
		return lastGetCurrentMemoryScope;
	}

	public void setLastGetCurrentScope(ScjScope lastgetscope) {
		if (this.lastGetCurrentMemoryScope == null || lastgetscope == null)
			this.lastGetCurrentMemoryScope = lastgetscope;
		else
			this.lastGetCurrentMemoryScope = new ScjScope(this.lastGetCurrentMemoryScope.getName()+" "+lastgetscope.getName().toString(), ScjScopeType.UNKNOWN);
	}
	
}
