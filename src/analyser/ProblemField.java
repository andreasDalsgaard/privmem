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
import com.ibm.wala.ipa.callgraph.propagation.InstanceFieldKey;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.callgraph.propagation.NormalAllocationInNode;

public class ProblemField extends Problem {

	InstanceKeyWithNode ikn;
	InstanceFieldKey pk;
	ScjContext iContext;
	ScjContext pContext;
	private String pointerStr;
	
	public ProblemField(InstanceKeyWithNode ikn, InstanceFieldKey pk) {		
		this.pk = pk;
		
		if (pk.getInstanceKey() instanceof NormalAllocationInNode) {
			pointerStr = "   pointer in: "+((NormalAllocationInNode)pk.getInstanceKey()).getNode().getContext().toString()+"\n";
		}
		
		
		this.ikn = ikn;		
	}

	public String toString()
	{
		if ( !this.isPrimordial() ) {
			return "Field mismatch between scope of pointer and instance, type: "+this.pk.getField().getDeclaringClass() + " field: " + this.pk.getField().getName()+ "\n"+	    					   					
					"   in class: "+this.ikn.getNode().getMethod().getDeclaringClass() + " in method: " + this.ikn.getNode().getMethod().getName()+ "\n" +
					"   in scope: "+this.ikn.getNode().getContext() + "\n"+
					pointerStr;			
		} else {
			return "";
		}
	}

	@Override
	protected boolean isPrimordial() {
		
		if ( this.ikn.getNode().getMethod().getDeclaringClass().getClassLoader().toString().equals("Primordial") &&
				!this.ikn.getNode().getMethod().getDeclaringClass().getName().toString().startsWith("Ljava/") && 
				!this.showPrimordial) 
			return true;
			
		return false;
	}
}
