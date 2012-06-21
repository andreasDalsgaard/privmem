/*******************************************************************************
 * Copyright (c) 2012 Andreas Engelbredt Dalsgaard <andreas.dalsgaard@gmail.com>.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Andreas Engelbredt Dalsgaard <andreas.dalsgaard@gmail.com>
 *     Martin Schoeberl <masca@imm.dtu.dk>
 ******************************************************************************/

package com.ibm.wala.ipa.callgraph.propagation;

import analyser.util;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.propagation.SSAPropagationCallGraphBuilder;
import com.ibm.wala.ipa.callgraph.propagation.SSAPropagationCallGraphBuilder.ConstraintVisitor;
import com.ibm.wala.ssa.SSAInvokeInstruction;

public class MyConstraintVisitor extends ConstraintVisitor {

	public MyConstraintVisitor(SSAPropagationCallGraphBuilder builder,
			CGNode node) {
		super(builder, node);				
	}
    
	 public void visitInvoke(SSAInvokeInstruction instruction) {
		 
		 	  if (instruction.getDeclaredTarget().getName().toString().contains("handleAsyncEvent") ||
		    	  instruction.getDeclaredTarget().getName().toString().equals("executeInArea") || 
		          instruction.getDeclaredTarget().getName().toString().equals("enterPrivateMemory") ||
		          (instruction.getDeclaredTarget().getName().toString().equals("getCurrentManagedMemory") &&
    			  instruction.getDeclaredTarget().getDeclaringClass().getName().toString().equals("Ljavax/safetycritical/ManagedMemory")))
		      {			 				 		  
		    	  IClass receiver = getBuilder().cha.lookupClass(instruction.getDeclaredTarget().getDeclaringClass());
		    	  
		    	  if (receiver != null ) {
		    		  if (!instruction.getCallSite().isStatic())
		    			  getTargetForCall(node, instruction.getCallSite(), receiver, null);			    		
		    	  } else {
		    		  util.error("Could not resolve call target for: "+instruction.getDeclaredTarget().getName().toString()+"\n");
		    	  }
		      }
		 	  
		 	  visitInvokeInternal(instruction,  new DefaultInvariantComputer());
	    }
}
