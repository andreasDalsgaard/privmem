package analyser;

import com.ibm.wala.classLoader.IClass;

public class MemoryAnnotationDefineScope extends MemoryAnnotation {

	ScjScope parent;

	

	public MemoryAnnotationDefineScope(ScjScopeStack ss) {
		super(ss.getLast());
		if (ss.size() > 1) {
			this.parent = ss.get(ss.size()-2);			
		} else {
			this.parent = null;
		}		
	}

	@Override
	public String toString() {
		if (this.parent != null)
			return "@DefineScope(name="+this.humaniseStr(scope.getName().toString())+", parent="+this.humaniseStr(this.parent.getName().toString())+")\n";
		else //ImmortalMemory does not need to be defined
			return ""; 
	}

	@Override
	public String getClassName() {
		//Does not really make sense for this type of annotation
		return "";
	}

}
