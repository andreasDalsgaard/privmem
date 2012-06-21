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

	HashSet<ScjScopeStack> scopeStacks;
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
	    	this.scopeStacks = new HashSet<ScjScopeStack>();
	    	this.scopeStacks.add(new ScjScopeStack());	    	
	    }
	    else
	    {
	    	this.scopeStacks = parent.cloneScopeStacks();
	    }
	    
	    Iterator<ScjScopeStack> ssIter = this.scopeStacks.iterator();
	    
	    while (ssIter.hasNext())
	    {	    	
	    	ssIter.next().add(new ScjScope(scopeName, type));	    	
	    }	    
	}
	
	/*** Copy constructor ***/
	public ScjContext(ScjContext parent) 
	{
		this.scopeStacks = parent.cloneScopeStacks();
		this.setLastGetCurrentScope(parent.getLastGetCurrentScope());
	}
	
	private HashSet<ScjScopeStack> cloneScopeStacks()
	{
		HashSet<ScjScopeStack> clone = new HashSet<ScjScopeStack>();
		
		Iterator<ScjScopeStack> ssIter = this.scopeStacks.iterator();
		    
		while (ssIter.hasNext())
		{			
		  	clone.add(ssIter.next().deepCopy());	    	
		}	   
		
		return clone;
	}

	public boolean less(ScjContext c)
	{
		if (c == null)
			return false;
		
		Iterator<ScjScopeStack> ssIter = c.scopeStacks.iterator();
		Iterator<ScjScopeStack> ss2Iter = this.scopeStacks.iterator();
		boolean result = true; 
		
		ScjScopeStack ss;
		while (ssIter.hasNext())
		{
			ss = ssIter.next();
					
			while (ss2Iter.hasNext())
			{
				if (!(result & ss2Iter.next().less(ss)) )
					return false;					
			}	   
		}
		
		return true;		
	}
	
	@Override
	public boolean equals(Object o) {		
	    return (o instanceof ScjContext) && ((ScjContext) o).scopeStacks.toString().equals(this.scopeStacks.toString());
	}

	@Override
	public int hashCode() {
	    return scopeStacks.toString().hashCode();
	}

	@Override
	public String toString() {
	    return this.getLastGetCurrentScope()+" "+scopeStacks.toString();
	}
	
	public ContextItem get(ContextKey name) {
		if (!this.scopeStacks.isEmpty())
		{
			Iterator<ScjScopeStack> ssIter = this.scopeStacks.iterator();
			
			while (ssIter.hasNext())
			{						  	
			  	Iterator<ScjScope> ScjScopes = ssIter.next().iterator();
				
				while( ScjScopes.hasNext() )
				{
					ScjScope scope = ScjScopes.next();
					if (scope.getName().equals(name) )
						return scope; 
				}
			}	  
			
		}
		
	    return null;
	}
	
	public ScjScope getStackTop()
	{
		Iterator<ScjScopeStack> ssIter = this.scopeStacks.iterator();
		ScjScope top = null; 
		int i = 0;
		
		while (ssIter.hasNext())
		{
			i++;
			ScjScopeStack next = ssIter.next();
			top = next.getLast();			
		}
		
		if (i > 1)
			util.error("Currently only one stack is supported");		
		
		return top; 
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
