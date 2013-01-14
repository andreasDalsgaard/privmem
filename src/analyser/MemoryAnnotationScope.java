package analyser;

import com.ibm.wala.classLoader.IClass;

public class MemoryAnnotationScope extends MemoryAnnotation {


	IClass objectType;
	String identifier;

	public MemoryAnnotationScope(ScjScope scope, IClass objectType, String identifier) {
		super(scope);
		this.objectType = objectType;
		this.identifier = identifier;		
	} 
	
	
	public String getClassName()
	{
		return this.objectType.getName().toString();
	}

	@Override
	public String toString() {
		if (this.identifier == null) { //Class scope annotation			
			return "@Scope("+ this.humaniseStr(this.scope.getName().toString())+") Class: "+this.objectType+"\n";
		}else {	//Field scope annotation			
			if (this.identifier.equals("seq") && this.scope.getScopeType() == ScjScopeType.IMMORTAL) //Filter out seq field inherited from cyclic excecutive in JOP SCJ impl
				return "";
			return "@Scope(" + this.humaniseStr(this.scope.getName().toString())+") Field: "+this.identifier+" in class: " + this.objectType + "\n";
		}
	}
}
