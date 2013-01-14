package analyser;

import com.ibm.wala.classLoader.IClass;

public abstract class MemoryAnnotation {
	
	ScjScope scope;
	
	public MemoryAnnotation(ScjScope scope)
	{		
		this.scope = scope;		
	}
	
	public int hashCode()
	{		
	    return this.toString().hashCode();
	}	
	
	public boolean equals(Object obj) {	
	    return this.hashCode() == ((MemoryAnnotation)obj).hashCode();
	}
	
	public abstract String toString();
	
	public abstract String getClassName();
	
	protected String humaniseStr(String str)
	{
		if (str.equals("Ljavax/realtime/ImmortalMemory"))
		{
			return "IMMORTAL";
		} else {
			return "\""+str.substring(str.lastIndexOf('/')+1, str.length())+"\"";
		}
	}
}
