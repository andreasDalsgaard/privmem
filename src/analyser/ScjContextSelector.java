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

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.UUID;


public class ScjContextSelector implements ContextSelector {
		
	private IClass missionIClass = null;
	private IClass AEHIClass = null;	
	int counter = 0;	
	private ClassHierarchy cha;
	private IClass ManagedMemoryIClass;
	private IClass MemoryAreaIClass;
	public HashMap<IClass, ScjScopeStack> classScopeMap = new HashMap<IClass, ScjScopeStack>();
	public HashSet<ScjScopeStack> scopeStacks = new HashSet<ScjScopeStack>();
	public HashMap<IMethod, ScjScopeStack> methodScopeMap = new HashMap<IMethod, ScjScopeStack>();
	public int count;		
	private IClass immortalIClass;
	private IClass safeletIClass;
	private IClass PEHIClass;
	private IClass APEHIClass;
	private IClass CyclicExecutiveIClass;
	private ScjContext immortal;
	private boolean CyclicExecutiveUsed;
	
	
	
	public ScjContextSelector(ClassHierarchy cha) 
	{
		this.cha = cha;
		this.missionIClass = util.getIClass("Ljavax/safetycritical/Mission", cha);
				
		if (this.missionIClass == null)
			throw new IllegalArgumentException("No mission class in ClassHierarchy");
		
		this.AEHIClass = util.getIClass("Ljavax/realtime/AbstractAsyncEventHandler", cha);
		this.ManagedMemoryIClass  = util.getIClass("Ljavax/safetycritical/ManagedMemory", cha);		
		this.MemoryAreaIClass  = util.getIClass("Ljavax/realtime/MemoryArea", cha);		
		this.immortalIClass = util.getIClass("Ljavax/realtime/ImmortalMemory", cha);
		this.safeletIClass = util.getIClass("Ljavax/safetycritical/Safelet", cha);		
		this.PEHIClass = util.getIClass("Ljavax/safetycritical/PeriodicEventHandler", cha);
		this.APEHIClass = util.getIClass("Ljavax/safetycritical/AperiodicEventHandler", cha);
		this.CyclicExecutiveIClass = util.getIClass("Ljavax/safetycritical/CyclicExecutive", cha);
		this.immortal = new ScjContext(null, "Ljavax/realtime/ImmortalMemory", ScjScopeType.IMMORTAL);
	}
	
	public Context getCalleeTarget(CGNode caller, CallSiteReference site,
			IMethod callee, InstanceKey[] actualParameters) 
	{	
		ScjContext calleeContext;		
	
		// Handles the first nodes that is called from the synthetic fakeRoot
		if (!(caller.getContext() instanceof ScjContext))		
		{			
			return immortal; 
		}		
		
		calleeContext = (ScjContext) caller.getContext();
				
		if (isSubclassOf(callee, this.AEHIClass) && 
				isFuncName(callee, "handleAsyncEvent")) 
		{
			calleeContext = new ScjContext(calleeContext, callee.getDeclaringClass().getName().toString(), ScjScopeType.PM);
		} else if (isSubclassOf(callee,this.ManagedMemoryIClass)) 
		{			
			if (isFuncName(callee, "enterPrivateMemory")) {
				calleeContext = new ScjContext(calleeContext, getUniquePMName(), ScjScopeType.PM);
			} else if (isFuncName(callee, "getCurrentManagedMemory")) {
				((ScjContext)caller.getContext()).setLastGetCurrentScope(calleeContext.getStackTop());
			}
		} else if(isSubclassOf(callee,this.MemoryAreaIClass)) 
		{					
			if (isFuncName(callee, "executeInArea"))
			{				
				calleeContext = new ScjContext(calleeContext, calleeContext.getLastGetCurrentScope().getName().toString(), calleeContext.getLastGetCurrentScope().getScopeType()); 
			} else if (isFuncName(callee, "getMemoryArea"))
			{
					((ScjContext)caller.getContext()).setLastGetCurrentScope(calleeContext.getStackTop());
			}
		} else if (isFuncName(callee, "startMission") && callee.getDeclaringClass().getName().toString().equals("Ljavax/safetycritical/JopSystem")) 
		{						
				calleeContext = new ScjContext(calleeContext, 
						caller.getMethod().getDeclaringClass().getName().toString(), ScjScopeType.MISSION);
		} 

		this.updateClassScopeMapping(callee,calleeContext.scopeStack);		
		this.updateMethodScope(callee, calleeContext.scopeStack);
		return calleeContext;
	}

	public IntSet getRelevantParameters(CGNode caller, CallSiteReference site) {
		return EmptyIntSet.instance;
	}
	
	public boolean isSubclassOf(IMethod callee, IClass parent)
	{
		if (parent != null)			
			return this.cha.isSubclassOf(callee.getDeclaringClass(), parent);
		return false;		
	}
	
	public boolean isFuncName(IMethod callee, String str)
	{
		return callee.getName().toString().equals(str);
	}
	
	public String getUniquePMName()
	{
		return UUID.randomUUID().toString();
	}
	
	private void updateClassScopeMapping(IMethod callee, ScjScopeStack scopeStack)
	{
		if (isSubclassOf(callee, this.CyclicExecutiveIClass))
		{
			updateClassScope(callee.getDeclaringClass(),immortal.scopeStack);			
			this.scopeStacks.add(immortal.scopeStack);
			this.CyclicExecutiveUsed = true;
		} else if (isSubclassOf(callee, this.immortalIClass)) {			
			updateClassScope(callee.getDeclaringClass(), scopeStack);
			this.scopeStacks.add(scopeStack);
		} else if(isSubclassOf(callee, this.safeletIClass)) {		
			updateClassScope(callee.getDeclaringClass(), scopeStack);
			this.scopeStacks.add(scopeStack);
		} else if(isSubclassOf(callee, this.missionIClass)) { 		//Mission 		
			updateClassScope(callee.getDeclaringClass(), scopeStack);
			this.scopeStacks.add(scopeStack);
		} else if(isSubclassOf(callee, this.PEHIClass) || isSubclassOf(callee, this.APEHIClass)) {		//EventHandlers
			if (this.CyclicExecutiveUsed) //Heuristic to human-like annotations if cyclicexcecutive is used put eventhandlers in mission
			{
				ScjScopeStack ss = new ScjScopeStack();
				ss.add(scopeStack.get(0));
				ss.add(scopeStack.get(1));
				updateClassScope(callee.getDeclaringClass(), ss);			
				this.scopeStacks.add(ss);
			} else {
				updateClassScope(callee.getDeclaringClass(), scopeStack);			
				this.scopeStacks.add(scopeStack);
			}
		}
	}
	
	private void updateClassScope(IClass type, ScjScopeStack ss1)
	{
		ScjScope scjScope = ss1.getLast();
		
		if (this.classScopeMap.containsKey(type))
		{
			ScjScopeStack ss2 = this.classScopeMap.get(type);			
			ss2.add(scjScope);
			this.classScopeMap.put(type, ss2);	
		} else {
			ScjScopeStack scopestack = new ScjScopeStack();	
			scopestack.add(ss1.getLast());
			this.classScopeMap.put(type, scopestack);
		}
	}
	
	private void updateMethodScope(IMethod method, ScjScopeStack ss1)
	{
		ScjScope scjScope = ss1.getLast();
				
		if (this.methodScopeMap.containsKey(method))
		{
			ScjScopeStack ss2 = this.methodScopeMap.get(method);			
			
			if (ss2.size() != 1 || ss2.getLast() == scjScope)
			{ 
				this.methodScopeMap.remove(method);
				ss2.add(scjScope);
				this.methodScopeMap.put(method, ss2);								
			}
			
		} else {
			ScjScopeStack scopestack = new ScjScopeStack();	
			scopestack.add(ss1.getLast());
			this.methodScopeMap.put(method, scopestack);
		}
	}
	
	
}
