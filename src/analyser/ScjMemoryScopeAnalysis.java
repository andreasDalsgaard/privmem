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

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import com.ibm.wala.analysis.pointers.BasicHeapGraph;
import com.ibm.wala.analysis.pointers.HeapGraph;
import com.ibm.wala.analysis.reflection.InstanceKeyWithNode;
import com.ibm.wala.analysis.reflection.ReflectionContextInterpreter;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.AnalysisOptions.ReflectionOptions;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ipa.callgraph.CallGraphBuilder;
import com.ibm.wala.ipa.callgraph.Context;
import com.ibm.wala.ipa.callgraph.ContextSelector;
import com.ibm.wala.ipa.callgraph.Entrypoint;
import com.ibm.wala.ipa.callgraph.impl.DefaultContextSelector;
import com.ibm.wala.ipa.callgraph.impl.DelegatingContextSelector;
import com.ibm.wala.ipa.callgraph.impl.Util;
import com.ibm.wala.ipa.callgraph.propagation.AbstractFieldPointerKey;
import com.ibm.wala.ipa.callgraph.propagation.AbstractLocalPointerKey;
import com.ibm.wala.ipa.callgraph.propagation.AbstractTypeInNode;
import com.ibm.wala.ipa.callgraph.propagation.AllocationSiteInNodeFactory;
import com.ibm.wala.ipa.callgraph.propagation.ConcreteTypeKey;
import com.ibm.wala.ipa.callgraph.propagation.InstanceFieldKey;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.callgraph.propagation.MyConstraintVisitor;
import com.ibm.wala.ipa.callgraph.propagation.NormalAllocationInNode;
import com.ibm.wala.ipa.callgraph.propagation.PointerAnalysis;
import com.ibm.wala.ipa.callgraph.propagation.SSAContextInterpreter;
import com.ibm.wala.ipa.callgraph.propagation.SSAPropagationCallGraphBuilder;
import com.ibm.wala.ipa.callgraph.propagation.StaticFieldKey;
import com.ibm.wala.ipa.callgraph.propagation.cfa.DefaultPointerKeyFactory;
import com.ibm.wala.ipa.callgraph.propagation.cfa.DefaultSSAInterpreter;
import com.ibm.wala.ipa.callgraph.propagation.cfa.DelegatingSSAContextInterpreter;
import com.ibm.wala.ipa.callgraph.propagation.cfa.ExceptionReturnValueKey;
import com.ibm.wala.ipa.callgraph.propagation.cfa.ZeroXInstanceKeys;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.WalaException;
import com.ibm.wala.util.io.CommandLine;


public class ScjMemoryScopeAnalysis {

	public static void main(String[] args) throws WalaException {
		String primordial, main;
		Properties p = CommandLine.parse(args);
		
		if (p.getProperty("application") == null ) 		
			util.print_usage();
 
		if (p.getProperty("main") != null)
			main = p.getProperty("main");
		else
			main = "Main";
		
		if (p.getProperty("primordial") != null)
			primordial = p.getProperty("primordial");
		else
			primordial = null;
			
		try {
			Set<Problem> problems = buildPointsTo(p.getProperty("application"), main, primordial);
			
			Iterator<Problem> pItr = problems.iterator();			
			System.out.print("Problems:\n");
			
			while ( pItr.hasNext() )
			{				
				System.out.print(pItr.next()+"\n");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Set<Problem> buildPointsTo(String application, String main_class, String primordial) throws WalaException, IllegalArgumentException, CancelException, IOException, InvalidClassFileException {	
		
	    AnalysisScope scope = MyAnalysisScopeReader.makeJavaBinaryAnalysisScope(application, primordial, null);
	    ClassHierarchy cha = ClassHierarchy.make(scope);
	    Iterable<Entrypoint> entrypoints = com.ibm.wala.ipa.callgraph.impl.Util.makeMainEntrypoints(scope, cha, "L"+main_class);	    
	    AnalysisOptions options = new AnalysisOptions(scope, entrypoints);   
	    options.setReflectionOptions(ReflectionOptions.NONE);
	    
	    ContextSelector scjContextSelector = new ScjContextSelector(cha);	    
		com.ibm.wala.ipa.callgraph.CallGraphBuilder builder = MyCFABuilder(options, new AnalysisCache(), cha, scope, scjContextSelector);	    
	    CallGraph cg = builder.makeCallGraph(options,null);
	    PointerAnalysis pointerAnalysis = builder.getPointerAnalysis(); 	   
	    BasicHeapGraph bhg = new BasicHeapGraph(pointerAnalysis, cg); 
	    HashSet<Problem> problems = new HashSet<Problem>();    
	    
	    runAnalysis(bhg.getPointerAnalysis(), (HeapGraph)bhg, problems);	
	    
		return problems; 
  }	
	
	
	public static void runAnalysis(PointerAnalysis pa, HeapGraph hg, HashSet<Problem> problems)
	{
		
	    Iterator<InstanceKey> ikItr = pa.getInstanceKeys().iterator();
	    IClassHierarchy cha = pa.getClassHierarchy();
	    IClass throwableIClass = util.getIClass("Ljava/lang/Throwable", pa.getClassHierarchy());    	
		
	    while( ikItr.hasNext() )
	    {
	    	InstanceKey ik = ikItr.next();
	    	
	    	Iterator<Object> pkIter = hg.getPredNodes(ik);
	    	while (pkIter.hasNext()){
	    		Object pred = pkIter.next(); 
	    		if (getScjContext(ik) == null) {	
	    			; 
	    			if (ik instanceof NormalAllocationInNode){ //Remove errors from fakeRootMethod	    				
	    				if (!((NormalAllocationInNode)ik).getNode().getMethod().getName().toString().equals("fakeRootMethod"))
	    					util.error("Unexpected instancekey, expected fakeRootMethod");
	    			} else if(ik instanceof ConcreteTypeKey && 
	    					ik.getConcreteType().getName().toString().equals("Ljava/lang/String")){  
	    				;	//TODO: We believe this is from strings in exceptions.
	    			} else if (cha.isSubclassOf(ik.getConcreteType(), throwableIClass)) { //XXX: We currently do not handle exceptions
	    				util.warnException();
	    			} else { 
	    				System.out.print(ik.getClass()+"\n");
	    				util.warn(": Cannot resolve context on InstanceKey: "+ik.getClass()+ " "+ik.getConcreteType()+" "+pred);	    				
	    			}
	    			
	    		} else {
	    			if (!getScjContext(ik).less(getScjContext(pred)))
	    			{
	    				if (ik instanceof InstanceKeyWithNode) {
	    					InstanceKeyWithNode ikn = (InstanceKeyWithNode) ik;
	    					if (pred instanceof InstanceFieldKey){	    						
	    						problems.add(new ProblemField(ikn, (InstanceFieldKey)pred));
	    					} else if (pred instanceof StaticFieldKey) {
	    						problems.add(new ProblemStaticField(ikn, (StaticFieldKey)pred));
		    				} else if (pred instanceof ExceptionReturnValueKey) {	//XXX: We currently do not handle exceptions
		    					util.warnException();
	    					} else if (pred instanceof AbstractLocalPointerKey) {
	    						problems.add(new ProblemPkIk(ikn, (AbstractLocalPointerKey)pred));
		    				} else{
		    					problems.add(new ProblemUnknown("Unknown mismatch is: "+ pred.getClass() + "scopes: "+getScjContext(pred)+ " result: " + getScjContext(ik)+
		    							"\n   instance of class: "+ikn.getNode().getMethod().getDeclaringClass() + " in method: " + ikn.getNode().getMethod().getName()+ "\n"));
		    				}
	    				} else {
	    					problems.add(new ProblemUnknown("Unknown mismatch is: "+ pred.getClass() + "scopes: "+getScjContext(pred)+ " result: " + getScjContext(ik)+"\n"));	    				
	    				}
	    			}
	    		}
	    	}
	    }
	}

	
	private static ScjContext getScjContext(Object o) 
	{
		  Context context = null;
		  
		  if(o instanceof AbstractTypeInNode) { // In case o is an instancekey this is  
			  context = ((AbstractTypeInNode) o).getNode().getContext();			  
		  } else if(o instanceof AbstractLocalPointerKey) {
			  context = ((AbstractLocalPointerKey) o).getNode().getContext();			  
		  } else if (o instanceof AbstractFieldPointerKey) {
			  context = getScjContext(((AbstractFieldPointerKey) o).getInstanceKey());	//XXX: Make sure this is right!		  		  
		  } else if (o instanceof StaticFieldKey) {             
			  context = new ScjContext(null, "ImmortalMemory", ScjScopeType.IMMORTAL); 
		  }
		  
		  if (context instanceof ScjContext)
			  return (ScjContext) context;
		  
		  return null;		  
	}
	
/*	
 * Does not seem to be working yet. Should be tested with a recent version of WALA.
 * private static CallGraphBuilder MyRTABuilder(AnalysisOptions options, AnalysisCache cache, IClassHierarchy cha,
		      AnalysisScope scope, ContextSelector customSelector) 
	{
		    Util.addDefaultSelectors(options, cha);
		    Util.addDefaultBypassLogic(options, scope, Util.class.getClassLoader(), cha);

		    return new BasicRTABuilder(cha, options, cache, customSelector, null);
	}
*/

	private static CallGraphBuilder MyCFABuilder(AnalysisOptions options,
			AnalysisCache cache, ClassHierarchy cha, AnalysisScope scope,
			ContextSelector customSelector) {

		if (options == null) {
			throw new IllegalArgumentException("options is null");
		}

		Util.addDefaultSelectors(options, cha);
		Util.addDefaultBypassLogic(options, scope, Util.class.getClassLoader(),
				cha);

		
		return MyZeroXCFABuilder.make(cha, options, cache, customSelector,
			 ZeroXInstanceKeys.ALLOCATIONS | ZeroXInstanceKeys.SMUSH_MANY						
						);

	}

}

class MyZeroXCFABuilder extends SSAPropagationCallGraphBuilder {

    protected MyZeroXCFABuilder(IClassHierarchy cha, AnalysisOptions options,
                    AnalysisCache cache, ContextSelector appContextSelector,
                    int instancePolicy) {
            super(cha, options, cache, new DefaultPointerKeyFactory());

            ContextSelector def = new DefaultContextSelector(options, cha);
            ContextSelector contextSelector = appContextSelector == null ? def
                            : new DelegatingContextSelector(appContextSelector, def);
            setContextSelector(contextSelector);

            SSAContextInterpreter appContextInterpreter = null;

            SSAContextInterpreter c = new DefaultSSAInterpreter(options, cache);
            c = new DelegatingSSAContextInterpreter(ReflectionContextInterpreter
                            .createReflectionContextInterpreter(cha, options,
                                            getAnalysisCache()), c);
            SSAContextInterpreter contextInterpreter = appContextInterpreter == null ? c
                            : new DelegatingSSAContextInterpreter(appContextInterpreter, c);
            setContextInterpreter(contextInterpreter);

            //Create InstanceKeys based on allocation sites  
            setInstanceKeys(new AllocationSiteInNodeFactory(options, cha));
    }

    public static SSAPropagationCallGraphBuilder make(IClassHierarchy cha,
                    AnalysisOptions options, AnalysisCache cache,
                    ContextSelector contextSelector, int instancePolicy) {

            return new MyZeroXCFABuilder(cha, options, cache, contextSelector,
                            instancePolicy);

    }         

    protected ConstraintVisitor makeVisitor(CGNode node) {
            return new MyConstraintVisitor(this, node);
    }

}


