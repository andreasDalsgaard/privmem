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
package analyserTest;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import analyser.Problem;
import analyser.ScjMemoryScopeAnalysis;

import com.ibm.wala.util.io.CommandLine;
 

public class ScjMemoryScopeAnalysisTest {

	ScjMemoryScopeAnalysis memAnalysis;
	String workingDir = System.getProperty("user.dir")+"/../../artikler/scjmemana/experiments/";
	
	@Test
	public void testMinepump() {		
		try {
			long t1, t2;
			t1 = System.currentTimeMillis();
			Set<Problem> problems = ScjMemoryScopeAnalysis.buildPointsTo(workingDir+"minepump.jar","privmem/minepump/Minepump", null);
			t2 = System.currentTimeMillis();
			System.out.print("Analysis time: "+(t2-t1)+"\n"); 
			printProblems(problems);
		} catch (Exception e) {
			System.out.print("Error in unit test\n");
			e.printStackTrace();
		}

		fail("Not yet implemented");
	}

	@Test
	public void testMinepumpLog() {
		try {
			long t1, t2;
			t1 = System.currentTimeMillis();			
			Set<Problem> problems = ScjMemoryScopeAnalysis.buildPointsTo(workingDir+"minepumplog.jar","privmem/minepumplog/Minepump", null);
			t2 = System.currentTimeMillis();
			System.out.print("Analysis time: "+(t2-t1)+"\n");
			printProblems(problems);
		} catch (Exception e) {
			System.out.print("Error in unit test\n");
			e.printStackTrace();
		}

		fail("Not yet implemented");
	}
	
	@Test
	public void testpmFFTcpResult() {
		try {			
			long t1, t2;
			t1 = System.currentTimeMillis();
			Set<Problem> problems = ScjMemoryScopeAnalysis.buildPointsTo(workingDir+"pmFFTcpResult.jar","pmFFTcpResult/pmFFTcpResult", null);
			t2 = System.currentTimeMillis();
			System.out.print("Analysis time: "+(t2-t1)+"\n");
			printProblems(problems);
		} catch (Exception e) {
			System.out.print("Error in unit test\n");
			e.printStackTrace();
		}

		fail("Not yet implemented");
	}
	
	@Test
	public void testRepRap() {
		try {			
			long t1, t2;
			t1 = System.currentTimeMillis();
			Set<Problem> problems = ScjMemoryScopeAnalysis.buildPointsTo(workingDir+"reprap.jar","org/reprap/Main", null);
			t2 = System.currentTimeMillis();
			System.out.print("Analysis time: "+(t2-t1)+"\n");
			printProblems(problems);
		} catch (Exception e) {
			System.out.print("Error in unit test\n");
			e.printStackTrace();
		}

		fail("Not yet implemented");
	}
	
	@Test
	public void testInOutParameter() {
		try {			
			long t1, t2;
			t1 = System.currentTimeMillis();
			Set<Problem> problems = ScjMemoryScopeAnalysis.buildPointsTo(workingDir+"InOutParameter.jar","privmem/InOutParameter/InOutParameter", null);
			t2 = System.currentTimeMillis();
			System.out.print("Analysis time: "+(t2-t1)+"\n");
			printProblems(problems);
		} catch (Exception e) {
			System.out.print("Error in unit test\n");
			e.printStackTrace();
		}

		fail("Not yet implemented");
	}
	
	@Test
	public void testSorter() {
		try {			
			long t1, t2;
			t1 = System.currentTimeMillis();
			Set<Problem> problems = ScjMemoryScopeAnalysis.buildPointsTo(workingDir+"Sorter.jar","privmem/sorter/SorterApp", null);
			t2 = System.currentTimeMillis();
			System.out.print("Analysis time: "+(t2-t1)+"\n");
			printProblems(problems);
		} catch (Exception e) {
			System.out.print("Error in unit test\n");
			e.printStackTrace();
		}

		fail("Not yet implemented");
	}

	@Test
	public void testmd5scj() {
		try {			
			long t1, t2;
			t1 = System.currentTimeMillis();
			Set<Problem> problems = ScjMemoryScopeAnalysis.buildPointsTo(workingDir+"md5scj.jar","privmem/md5scj/Level0App", null);
			t2 = System.currentTimeMillis();
			System.out.print("Analysis time: "+(t2-t1)+"\n");
			printProblems(problems);
		} catch (Exception e) {
			System.out.print("Error in unit test\n");
			e.printStackTrace();
		}

		fail("Not yet implemented");
	}

	@Test
	public void testthruster() {
		try {			
			long t1, t2;
			t1 = System.currentTimeMillis();
			Set<Problem> problems = ScjMemoryScopeAnalysis.buildPointsTo(workingDir+"Thruster.jar","privmem/thruster/engine/ThrusterControlSystem", null);
			t2 = System.currentTimeMillis();
			System.out.print("Analysis time: "+(t2-t1)+"\n");
			printProblems(problems);
		} catch (Exception e) {
			System.out.print("Error in unit test\n");
			e.printStackTrace();
		}

		fail("Not yet implemented");
	}
	@Test
	public void testPacemakerScj() {
		try {			
			long t1, t2;
			t1 = System.currentTimeMillis();
			Set<Problem> problems = ScjMemoryScopeAnalysis.buildPointsTo(workingDir+"pacemaker-scj.jar","privmem/pacemakerScj/main", null);
			t2 = System.currentTimeMillis();
			System.out.print("Analysis time: "+(t2-t1)+"\n");
			printProblems(problems);
		} catch (Exception e) {
			System.out.print("Error in unit test\n");
			e.printStackTrace();
		}

		fail("Not yet implemented");
	}
	
	@Test
	public void testMiniCDj() {
		try {			
			long t1, t2;
			t1 = System.currentTimeMillis();
			Set<Problem> problems = ScjMemoryScopeAnalysis.buildPointsTo(workingDir+"miniCDj.jar","cdx/Launcher", null);
			t2 = System.currentTimeMillis();
			System.out.print("Analysis time: "+(t2-t1)+"\n");
			printProblems(problems);
		} catch (Exception e) {
			System.out.print("Error in unit test\n");
			e.printStackTrace();
		}

		fail("Not yet implemented");
	}

	
	
	
	
	@Test
	public void testAppOnlyMinepump() {		
		try {
			long t1, t2;
			t1 = System.currentTimeMillis();
			Set<Problem> problems = ScjMemoryScopeAnalysis.buildPointsTo(workingDir+"minepump.jar","privmem/minepump/Minepump", workingDir+"privmemScj.jar", true);
			t2 = System.currentTimeMillis();
			System.out.print("Analysis time: "+(t2-t1)+"\n"); 
			printProblems(problems);
		} catch (Exception e) {
			System.out.print("Error in unit test\n");
			e.printStackTrace();
		}

		fail("Not yet implemented");
	}

	@Test
	public void testAppOnlyMinepumpLog() {
		try {
			long t1, t2;
			t1 = System.currentTimeMillis();			
			Set<Problem> problems = ScjMemoryScopeAnalysis.buildPointsTo(workingDir+"minepumplog.jar","privmem/minepumplog/Minepump", workingDir+"privmemScj.jar", true);
			t2 = System.currentTimeMillis();
			System.out.print("Analysis time: "+(t2-t1)+"\n");
			printProblems(problems);
		} catch (Exception e) {
			System.out.print("Error in unit test\n");
			e.printStackTrace();
		}

		fail("Not yet implemented");
	}
	
	@Test
	public void testAppOnlypmFFTcpResult() {
		try {			
			long t1, t2;
			t1 = System.currentTimeMillis();
			Set<Problem> problems = ScjMemoryScopeAnalysis.buildPointsTo(workingDir+"pmFFTcpResult.jar","privmem/pmFFTcpResult/pmFFTcpResult", workingDir+"privmemScj.jar", true);
			t2 = System.currentTimeMillis();
			System.out.print("Analysis time: "+(t2-t1)+"\n");
			printProblems(problems);
		} catch (Exception e) {
			System.out.print("Error in unit test\n");
			e.printStackTrace();
		}

		fail("Not yet implemented");
	}
	
	@Test
	public void testAppOnlyRepRap() {
		try {			
			long t1, t2;
			t1 = System.currentTimeMillis();
			Set<Problem> problems = ScjMemoryScopeAnalysis.buildPointsTo(workingDir+"reprap.jar","org/reprap/Main", workingDir+"privmemScj.jar", true);
			t2 = System.currentTimeMillis();
			System.out.print("Analysis time: "+(t2-t1)+"\n");
			printProblems(problems);
		} catch (Exception e) {
			System.out.print("Error in unit test\n");
			e.printStackTrace();
		}

		fail("Not yet implemented");
	}
	
	@Test
	public void testAppOnlyInOutParameter() {
		try {			
			long t1, t2;
			t1 = System.currentTimeMillis();
			Set<Problem> problems = ScjMemoryScopeAnalysis.buildPointsTo(workingDir+"InOutParameter.jar","privmem/InOutParameter/InOutParameter", workingDir+"privmemScj.jar", true);
			t2 = System.currentTimeMillis();
			System.out.print("Analysis time: "+(t2-t1)+"\n");
			printProblems(problems);
		} catch (Exception e) {
			System.out.print("Error in unit test\n");
			e.printStackTrace();
		}

		fail("Not yet implemented");
	}
	
	@Test
	public void testAppOnlySorter() {
		try {			
			long t1, t2;
			t1 = System.currentTimeMillis();
			Set<Problem> problems = ScjMemoryScopeAnalysis.buildPointsTo(workingDir+"Sorter.jar","privmem/sorter/SorterApp", workingDir+"privmemScj.jar", true);
			t2 = System.currentTimeMillis();
			System.out.print("Analysis time: "+(t2-t1)+"\n");
			printProblems(problems);
		} catch (Exception e) {
			System.out.print("Error in unit test\n");
			e.printStackTrace();
		}

		fail("Not yet implemented");
	}

	@Test
	public void testAppOnlymd5scj() {
		try {			
			long t1, t2;
			t1 = System.currentTimeMillis();
			Set<Problem> problems = ScjMemoryScopeAnalysis.buildPointsTo(workingDir+"md5scj.jar","privmem/md5scj/Level0App", workingDir+"privmemScj.jar", true);
			t2 = System.currentTimeMillis();
			System.out.print("Analysis time: "+(t2-t1)+"\n");
			printProblems(problems);
		} catch (Exception e) {
			System.out.print("Error in unit test\n");
			e.printStackTrace();
		}

		fail("Not yet implemented");
	}

	@Test
	public void testAppOnlythruster() {
		try {			
			long t1, t2;
			t1 = System.currentTimeMillis();
			Set<Problem> problems = ScjMemoryScopeAnalysis.buildPointsTo(workingDir+"Thruster.jar","privmem/thruster/engine/ThrusterControlSystem", workingDir+"privmemScj.jar", true);
			t2 = System.currentTimeMillis();
			System.out.print("Analysis time: "+(t2-t1)+"\n");
			printProblems(problems);
		} catch (Exception e) {
			System.out.print("Error in unit test\n");
			e.printStackTrace();
		}

		fail("Not yet implemented");
	}
	@Test
	public void testAppOnlyPacemakerScj() {
		try {			
			long t1, t2;
			t1 = System.currentTimeMillis();
			Set<Problem> problems = ScjMemoryScopeAnalysis.buildPointsTo(workingDir+"pacemaker-scj.jar","privmem/pacemakerScj/main", workingDir+"privmemScj.jar", true);
			t2 = System.currentTimeMillis();
			System.out.print("Analysis time: "+(t2-t1)+"\n");
			printProblems(problems);
		} catch (Exception e) {
			System.out.print("Error in unit test\n");
			e.printStackTrace();
		}

		fail("Not yet implemented");
	}
	
	@Test
	public void testAppOnlyMiniCDj() {
		try {			
			long t1, t2;
			t1 = System.currentTimeMillis();
			Set<Problem> problems = ScjMemoryScopeAnalysis.buildPointsTo(workingDir+"miniCDj.jar","cdx/Launcher", workingDir+"privmemScj.jar", true);
			t2 = System.currentTimeMillis();
			System.out.print("Analysis time: "+(t2-t1)+"\n");
			printProblems(problems);
		} catch (Exception e) {
			System.out.print("Error in unit test\n");
			e.printStackTrace();
		}

		fail("Not yet implemented");
	}

	
	
	
	
	
	
	
	
	private void printProblems(Set<Problem> problems)
	{
		int problemCounter = 0;
		Iterator<Problem> pItr = problems.iterator();			
		System.out.print("Problems:\n");
		
		while ( pItr.hasNext() )
		{	
			String strNext = pItr.next().toString();								
			if (!strNext.isEmpty()) {
				System.out.print(strNext+"\n");				
				problemCounter++;
			}
		}
		
		System.out.print("Nr. of problems: "+problemCounter+"\n");		
	}

}
