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

import java.util.Hashtable;
import com.ibm.wala.classLoader.CallSiteReference;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.Context;
import com.ibm.wala.ipa.callgraph.ContextSelector;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.util.intset.EmptyIntSet;
import com.ibm.wala.util.intset.IntSet;
import java.util.UUID;


public class ScjContextSelector implements ContextSelector {
		
	private ScjContext immortalMemory = new ScjContext(null, "ImmortalMemory", ScjScopeType.IMMORTAL);
	private IClass missionIClass = null;
	private IClass PEHIClass;
	int counter = 0;
	private Hashtable<IMethod, ScjContext> methodContextMap = new Hashtable<IMethod,ScjContext>();
	private ClassHierarchy cha;
	
	public ScjContextSelector(ClassHierarchy cha) 
	{
		this.cha = cha;
		this.missionIClass = util.getIClass("Ljavax/safetycritical/Mission", cha);
				
		if (this.missionIClass == null)
			throw new IllegalArgumentException("No mission class in ClassHierarchy");
		
		this.PEHIClass = util.getIClass("Ljavax/safetycritical/PeriodicEventHandler", cha);
	}
	
	public Context getCalleeTarget(CGNode caller, CallSiteReference site,
			IMethod callee, InstanceKey[] actualParameters) {	
				
		ScjContext calleeContext;		
	
		// Handles the first nodes that is called from the synthetic fakeRoot
		if (caller.getContext() instanceof ScjContext)
		{			
			Context tmp = caller.getContext();	
			calleeContext = (ScjContext) tmp;
						
		
			if ( this.cha.isSubclassOf(callee.getDeclaringClass(),this.PEHIClass) && 
					callee.getName().toString().equals("handleAsyncEvent")) 
			{											
				calleeContext = new ScjContext((ScjContext)tmp, callee.getDeclaringClass().getName().toString(), ScjScopeType.PEH);					
			} else if (callee.getName().toString().equals("enterPrivateMemory") && callee.getDeclaringClass().getName().toString().equals("Ljavax/safetycritical/ManagedMemory")) 
			{
				calleeContext = new ScjContext((ScjContext)tmp, UUID.randomUUID().toString(), ScjScopeType.PM);				
				
			} else if (callee.getName().toString().equals("executeInArea") && callee.getDeclaringClass().getName().toString().equals("Ljavax/realtime/MemoryArea")) 
			{	
				if (calleeContext.getLastGetCurrentScope() != null)
					calleeContext = new ScjContext((ScjContext)tmp, ((ScjContext)tmp).getLastGetCurrentScope().getName().toString(), ((ScjContext)tmp).getLastGetCurrentScope().getScopeType()); 
				else {
					calleeContext = new ScjContext((ScjContext)tmp, callee.getDeclaringClass().getName().toString(), ScjScopeType.UNKNOWN);
				}
			} else if (callee.getName().toString().equals("getCurrentManagedMemory") && callee.getDeclaringClass().getName().toString().equals("Ljavax/safetycritical/ManagedMemory")) 
			{
				((ScjContext)caller.getContext()).setLastGetCurrentScope(((ScjContext)tmp).getStackTop());				
			} else if (callee.getName().toString().equals("startMission") && callee.getDeclaringClass().getName().toString().equals("javax/safetycritical/JopSystem")) 
			{	
				//XXX:JOP HACK!
				calleeContext = new ScjContext(immortalMemory, 
						"Ljavax/safetycritical/JopSystem", ScjScopeType.MISSION);
			} else if (callee.getName().toString().equals("initialize")) 
			{	
				IClass declaringClass = callee.getDeclaringClass();
								
				if (callee.getClassHierarchy().isSubclassOf(declaringClass, this.missionIClass)) {
					calleeContext = new ScjContext(immortalMemory, 
							caller.getMethod().getDeclaringClass().getName().toString(), ScjScopeType.MISSION);			
				}				
			}
		} else 
		{
			calleeContext = this.immortalMemory;
		}	
		
		this.methodContextMap.put(callee, calleeContext);		
		return calleeContext;
	}

	public IntSet getRelevantParameters(CGNode caller, CallSiteReference site) {
		return EmptyIntSet.instance;
	}	
}
