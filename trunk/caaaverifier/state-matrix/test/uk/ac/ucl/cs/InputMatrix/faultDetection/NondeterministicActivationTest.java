/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix.faultDetection;

import uk.ac.ucl.cs.InputMatrix.AdaptationFSM;
import uk.ac.ucl.cs.InputMatrix.ContextVariable;
import uk.ac.ucl.cs.InputMatrix.ExternalRule;
import uk.ac.ucl.cs.InputMatrix.Fault;
import uk.ac.ucl.cs.InputMatrix.OrPredicate;
import uk.ac.ucl.cs.InputMatrix.Rule;
import uk.ac.ucl.cs.InputMatrix.State;
import uk.ac.ucl.cs.InputMatrix.validation.NondeterministicActivation;
import junit.framework.TestCase;

/**
 * @author rax
 *
 */
public class NondeterministicActivationTest extends TestCase {

	
	/**
	 * @param name
	 */
	public NondeterministicActivationTest(String name) {
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
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.validation.NondeterministicActivation#detectFaults()}.
	 */
	public void testDetectFaults_nofaults() throws Exception{
		NondeterministicActivation nondeterministicActivation=new NondeterministicActivation();
		AdaptationFSM AFSMUnderTest=new AdaptationFSM();
		nondeterministicActivation.setAFSMUnderTest(AFSMUnderTest);
		
		//variable
		ContextVariable cv=new ContextVariable("DummyContext",1);
		AFSMUnderTest.addContextVariable(cv);
		AFSMUnderTest.createVariable("V0",cv);
		Rule r=new Rule("R0");
		r.setCondition(AFSMUnderTest.getVariableByName("V0"));
		
		//state
		State s=new State("S0");
		s.addRule(r);
		AFSMUnderTest.addState(s);
		AFSMUnderTest.loadInputSpaces();
		
		Fault[] faults=nondeterministicActivation.detectFaults();
		this.assertEquals(0, faults.length);
	}
	
	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.validation.NondeterministicActivation#detectFaults()}.
	 */
	public void testDetectFaults_oneFault() throws Exception{
		NondeterministicActivation nondeterministicActivation=new NondeterministicActivation();
		AdaptationFSM AFSMUnderTest=new AdaptationFSM();
		nondeterministicActivation.setAFSMUnderTest(AFSMUnderTest);
		
		//variable
		ContextVariable cv=new ContextVariable("DummyContext",1);
		AFSMUnderTest.addContextVariable(cv);
		AFSMUnderTest.createVariable("V0",cv);
		Rule r0=new Rule("R0");
		r0.setCondition(AFSMUnderTest.getVariableByName("V0"));
		Rule r1=new Rule("R1");
		r1.setCondition(AFSMUnderTest.getVariableByName("V0"));
		
		//state
		State s=new State("S0");
		s.addRule(r0);
		s.addRule(r1);
		AFSMUnderTest.addState(s);
		AFSMUnderTest.loadInputSpaces();
		
		Fault[] faults=nondeterministicActivation.detectFaults();
		
		this.assertEquals(1, faults.length);
		this.assertEquals("1", faults[0].getInput());
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.validation.NondeterministicActivation#detectFaults()}.
	 */
	public void testDetectFaults_priority() throws Exception{
		NondeterministicActivation nondeterministicActivation=new NondeterministicActivation();
		AdaptationFSM AFSMUnderTest=new AdaptationFSM();
		nondeterministicActivation.setAFSMUnderTest(AFSMUnderTest);
		
		//variable
		ContextVariable cv=new ContextVariable("DummyContext",1);
		AFSMUnderTest.addContextVariable(cv);
		AFSMUnderTest.createVariable("V0",cv);
		Rule r0=new Rule("R0");
		r0.setCondition(AFSMUnderTest.getVariableByName("V0"));
		Rule r1=new Rule("R1");
		r1.setPriority(Rule.LOW_PRIORITY);
		r1.setCondition(AFSMUnderTest.getVariableByName("V0"));
		
		//state
		State s=new State("S0");
		s.addRule(r0);
		s.addRule(r1);
		AFSMUnderTest.addState(s);
		AFSMUnderTest.loadInputSpaces();
		
		Fault[] faults=nondeterministicActivation.detectFaults();
		
		this.assertEquals(0, faults.length);
	}
	
	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.validation.NondeterministicActivation#detectFaults()}.
	 */
	public void testDetectFaults_multipleVariables() throws Exception{
		NondeterministicActivation nondeterministicActivation=new NondeterministicActivation();
		AdaptationFSM AFSMUnderTest=new AdaptationFSM();
		nondeterministicActivation.setAFSMUnderTest(AFSMUnderTest);
		
		//variable
		ContextVariable cv=new ContextVariable("DummyContext",1);
		AFSMUnderTest.addContextVariable(cv);
		AFSMUnderTest.createVariable("V0",cv);
		AFSMUnderTest.createVariable("V1",cv);
		Rule r0=new Rule("R0");
		r0.setCondition(AFSMUnderTest.getVariableByName("V0"));
		Rule r1=new Rule("R1");
		r1.setCondition(AFSMUnderTest.getVariableByName("V1"));
		Rule r2=new Rule("R1");
		r2.setCondition(new OrPredicate(AFSMUnderTest.getVariableByName("V0"),AFSMUnderTest.getVariableByName("V1")));
		
		//state
		State s=new State("S0");
		s.addRule(r0);
		s.addRule(r1);
		s.addRule(r2);
		AFSMUnderTest.addState(s);
		AFSMUnderTest.loadInputSpaces();
		
		Fault[] faults=nondeterministicActivation.detectFaults();
		this.assertEquals(3, faults.length);
		//lower ending
		this.assertEquals("10", faults[0].getInput());
		this.assertEquals("01", faults[1].getInput());
		this.assertEquals("11", faults[2].getInput());
		//this.assertEquals(2, faults[0].getRules());
	}
}
