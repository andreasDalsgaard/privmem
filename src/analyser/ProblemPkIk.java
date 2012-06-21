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

import com.ibm.wala.analysis.reflection.InstanceKeyWithNode;
import com.ibm.wala.ipa.callgraph.propagation.AbstractLocalPointerKey;

public class ProblemPkIk extends Problem {

	InstanceKeyWithNode ikn;
	AbstractLocalPointerKey pk;
	ScjContext iContext;
	ScjContext pContext;
	
	public ProblemPkIk(InstanceKeyWithNode ikn, AbstractLocalPointerKey pk) {//, ScjContext iContext, ScjContext pContext) {		
		this.pk = pk;
		this.ikn = ikn;
		//this.iContext = iContext;
		//this.pContext= pContext;
	}
	
	
	
	public String toString()
	{
		if ( !this.isPrimordial() ) {
			return "PointerKey and InstanceKey mismatch between scopes for variable of type: "+ikn.getConcreteType()+"\n   in class: "+				
				this.pk.getNode().getMethod().getDeclaringClass() + " method: " + this.pk.getNode().getMethod().getName()+ "\n"+
				"   in scope: "+this.pk.getNode().getContext() + "\n" +
				"   in class: "+this.ikn.getNode().getMethod().getDeclaringClass() + " in method: " + this.ikn.getNode().getMethod().getName()+ "\n"+
				"   in scope: "+this.ikn.getNode().getContext() + "\n";
		} else {
			return "";		
		}
	}
	
	@Override
	protected boolean isPrimordial() {
		
		
		if ( this.ikn.getNode().getMethod().getDeclaringClass().getClassLoader().toString().equals("Primordial") && !this.showPrimordial )
			return true;
		
		return false;
	}
}
