package analyser;

import java.util.HashSet;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;

public class MemoryAnnotationRunsIn extends MemoryAnnotation {

	IMethod method;
	ScjScopeStack scopeStack;
	
	public MemoryAnnotationRunsIn(ScjScope scope, IMethod method) {
		super(scope);
		this.method = method;
		this.scopeStack = null;
	}
	
	public MemoryAnnotationRunsIn(ScjScope scope, IMethod method, ScjScopeStack scjScopeStack) {
		super(scope);
		this.method = method;
		this.scopeStack = scjScopeStack;
	}

	@Override
	public String toString() {	
		String str;
		
		if (this.scopeStack == null || this.getScopeSet().size() <= 1) {
			if (this.scope == null)
				this.scope = (ScjScope) this.getScopeSet().toArray()[0];
			
			str = "@RunsIn("+ this.humaniseStr(this.scope.getName().toString())+") Method: "+this.method.getName()+"() from Class: "+this.getClassName()+"\n";
			
		}else {
			/*StringBuilder builder = new StringBuilder();
			
			for (java.util.Iterator<ScjScope> i = this.scopeStack.iterator(); i.hasNext(); ) {
				this.scope = i.next();
				builder.append("Method: "+this.method.getName()+" in class: "+this.getClassName()+" RunsIn("+ this.scope.getName()+")\n");
			}
			
			builder.append("Warning - Method: "+this.method.getName()+" has conflicting @RunsIn annotations\n");
			return builder.toString();*/
			str = "@RunsIn(CALLER) Method: "+this.method.getName()+" from Class: "+this.getClassName()+"\n";
		}
		
		if (!this.method.getName().toString().equals("<init>"))
			return str;
		else
			return "";			
	}

	@Override
	public String getClassName() {
		return this.method.getDeclaringClass().getName().toString();	
	}
	
	public Set<ScjScope> getScopeSet()
	{
		Set<ScjScope> scopeSet = new HashSet<ScjScope>();

		if (this.scopeStack == null)
			return scopeSet;
				
		for(java.util.Iterator<ScjScope> iter = this.scopeStack.iterator();iter.hasNext(); ) {
			scopeSet.add(iter.next());			
		}
			
		
		return scopeSet;
	}
}
