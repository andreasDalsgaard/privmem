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

public class ProblemUnknown extends Problem {

	String description;	
	ScjContext iContext;
	ScjContext pContext;
	
	public ProblemUnknown(String description) {		
		this.description = description;
	}
	
	
	public String toString()
	{
		if ( !this.isPrimordial() )
			return description;		
		else
			return "";
	}
	
	protected boolean isPrimordial() 
	{
		if ( this.description.contains("Primordial") && !this.showPrimordial )
			return true;
			
		return false;
	}
}
