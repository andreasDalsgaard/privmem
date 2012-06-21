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

public abstract class Problem {
	
	boolean showPrimordial = false;
	
	public int hashCode()
	{		
	    return this.toString().hashCode();
	}
	
	public boolean equals(Object obj) {
		
	    if (obj instanceof Problem) {
	      if (obj.getClass().equals(getClass())) {
	    	  Problem other = (Problem) obj;	    	  
	    	  return other.hashCode() == this.hashCode();
	      } else {
	        return false;
	      }
	    } else {
	      return false;
	    }
	}
		
	abstract protected boolean isPrimordial();
	
}
