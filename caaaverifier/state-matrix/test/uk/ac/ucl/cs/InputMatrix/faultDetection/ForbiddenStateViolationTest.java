/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix.faultDetection;

import uk.ac.ucl.cs.InputMatrix.AdaptationFSM;
import uk.ac.ucl.cs.InputMatrix.AdaptationRule;
import uk.ac.ucl.cs.InputMatrix.ContextVariable;
import uk.ac.ucl.cs.InputMatrix.Fault;
import uk.ac.ucl.cs.InputMatrix.Rule;
import uk.ac.ucl.cs.InputMatrix.State;
import uk.ac.ucl.cs.InputMatrix.validation.ForbiddenStateViolationFault;
import uk.ac.ucl.cs.InputMatrix.validation.ForbiddenStatesViolation;
import uk.ac.ucl.cs.InputMatrix.validation.NondeterministicActivation;
import junit.framework.TestCase;

/**
 * @author rax
 *
 */
public class ForbiddenStateViolationTest extends TestCase {

	/**
	 * @param name
	 */
	public ForbiddenStateViolationTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.validation.ForbiddenStatesViolation#detectFaults()}.
	 * @throws Exception 
	 */
	public void testDetectFaults_1var() throws Exception {
		ForbiddenStatesViolation forbiddenStatesViolation=new ForbiddenStatesViolation();
		AdaptationFSM AFSMUnderTest=new AdaptationFSM();
		forbiddenStatesViolation.setAFSMUnderTest(AFSMUnderTest);
		
		
		//state
		State s0=new State("S0");
		State s1=new State("S1");
		s1.setForbiddenState(true);
		AFSMUnderTest.addState(s0);
		AFSMUnderTest.addState(s1);
		
		//variable
		ContextVariable cv=new ContextVariable("DummyContext",1);
		AFSMUnderTest.addContextVariable(cv);
		AFSMUnderTest.createVariable("V0",cv);
		AdaptationRule r=new AdaptationRule("R0",s1);
		r.setCondition(AFSMUnderTest.getVariableByName("V0"));
		s0.addRule(r);
		
		AFSMUnderTest.loadInputSpaces();
		
		Fault[] faults=forbiddenStatesViolation.detectFaults();
		ForbiddenStateViolationFault f=(ForbiddenStateViolationFault)faults[0];
		this.assertEquals(1, faults.length);
		this.assertEquals(s0, f.getState());
		this.assertEquals(s1, f.getForbiddenState());
		this.assertEquals(r, f.getRule());
	}

	
	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.validation.ForbiddenStatesViolation#detectFaults()}.
	 * @throws Exception 
	 */
	public void testDetectFaults_2var() throws Exception {
		ForbiddenStatesViolation forbiddenStatesViolation=new ForbiddenStatesViolation();
		AdaptationFSM AFSMUnderTest=new AdaptationFSM();
		forbiddenStatesViolation.setAFSMUnderTest(AFSMUnderTest);
		
		//state
		State s0=new State("S0");
		State s1=new State("S1");
		s1.setForbiddenState(true);
		AFSMUnderTest.addState(s0);
		AFSMUnderTest.addState(s1);
		
		//variable
		ContextVariable cv=new ContextVariable("DummyContext",1);
		AFSMUnderTest.addContextVariable(cv);
		AFSMUnderTest.createVariable("V0",cv);
		AFSMUnderTest.createVariable("V1",cv);
		AdaptationRule r=new AdaptationRule("R0",s1);
		r.setCondition(AFSMUnderTest.getVariableByName("V0"));
		s0.addRule(r);
		
		AFSMUnderTest.loadInputSpaces();
		
		Fault[] faults=forbiddenStatesViolation.detectFaults();
		ForbiddenStateViolationFault f=(ForbiddenStateViolationFault)faults[0];
		this.assertEquals(1, faults.length);
		this.assertEquals(s0, f.getState());
		this.assertEquals(s1, f.getForbiddenState());
		this.assertEquals(r, f.getRule());
	}
	
	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.validation.ForbiddenStatesViolation#applyRegression()}.
	 * @throws Exception 
	 */
	public void testapplyRegression() throws Exception 
	{
		ForbiddenStatesViolation forbiddenStatesViolation=new ForbiddenStatesViolation();
		AdaptationFSM AFSMUnderTest=new AdaptationFSM();
		forbiddenStatesViolation.setAFSMUnderTest(AFSMUnderTest);
		
		//state
		State s0=new State("S0");
		State s1=new State("S1");
		s1.setForbiddenState(true);
		AFSMUnderTest.addState(s0);
		AFSMUnderTest.addState(s1);
		
		//variable
		ContextVariable cv=new ContextVariable("DummyContext",1);
		AFSMUnderTest.addContextVariable(cv);
		AFSMUnderTest.createVariable("V0",cv);
		AFSMUnderTest.createVariable("V1",cv);
		AFSMUnderTest.createVariable("V2",cv);
		AFSMUnderTest.createVariable("V3",cv);
		AdaptationRule r=new AdaptationRule("R0",s1);
		r.setCondition(AFSMUnderTest.getVariableByName("V1"));
		s0.addRule(r);

		AFSMUnderTest.loadInputSpaces();
		Fault[] faults=forbiddenStatesViolation.detectFaults();
		faults=forbiddenStatesViolation.applyRegression(faults);
		ForbiddenStateViolationFault f=(ForbiddenStateViolationFault)faults[0];
		this.assertEquals(1, faults.length);
		this.assertEquals(s0, f.getState());
		this.assertEquals(s1, f.getForbiddenState());
		this.assertEquals(r, f.getRule());
		this.assertEquals("**1*", f.getInput());
	}
	
	
}
