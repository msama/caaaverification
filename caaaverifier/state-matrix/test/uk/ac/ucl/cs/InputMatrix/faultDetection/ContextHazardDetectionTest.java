/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix.faultDetection;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.ucl.cs.InputMatrix.AdaptationFSM;
import uk.ac.ucl.cs.InputMatrix.AndPredicate;
import uk.ac.ucl.cs.InputMatrix.ContextVariable;
import uk.ac.ucl.cs.InputMatrix.ExternalRule;
import uk.ac.ucl.cs.InputMatrix.Fault;
import uk.ac.ucl.cs.InputMatrix.NotPredicate;
import uk.ac.ucl.cs.InputMatrix.OrPredicate;
import uk.ac.ucl.cs.InputMatrix.Rule;
import uk.ac.ucl.cs.InputMatrix.State;
import uk.ac.ucl.cs.InputMatrix.PredicateVariable;
import uk.ac.ucl.cs.InputMatrix.inputSpaceReduction.ComplexContextVariable;
import uk.ac.ucl.cs.InputMatrix.validation.ContextHazardDetection;

/**
 * @author rax
 *
 */
public class ContextHazardDetectionTest {

	protected AdaptationFSM _afsm;
	protected ContextVariable _cv;
	protected ContextHazardDetection _chDet;
	protected State _state;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this._afsm=new AdaptationFSM();
		
		this._cv=new ContextVariable("Dummy context",1);
		this._afsm.addContextVariable(this._cv);
		
		this._chDet=new ContextHazardDetection();
		this._chDet.setAFSMUnderTest(this._afsm);
		
		this._state=new State("S");
		this._afsm.addState(this._state);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this._afsm=null;
		this._cv=null;
		this._chDet=null;
		this._state=null;
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.validation.ContextHazardDetection#detectFaults()}.
	 * This method is testing A or B which is supposed to be safe
	 */
	@Test
	public void testDetectFaults_or() {
		this._afsm.createVariable("A", this._cv);
		this._afsm.createVariable("B", this._cv);
		this._afsm.createVariable("C", this._cv);
		this._afsm.createVariable("D", this._cv);
		
		OrPredicate or=new OrPredicate(this._afsm.getVariableByName("A"),this._afsm.getVariableByName("B"));
		ExternalRule rule=new ExternalRule("E");
		rule.setCondition(or);
		this._state.addRule(rule);
		this._afsm.loadInputSpaces();
		
		Fault[] f=this._chDet.detectFaults();
		assertEquals(0, f.length);
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.validation.ContextHazardDetection#detectFaults()}.
	 * This method is testing A and B which is supposed to have two failures
	 */
	@Test
	public void testDetectFaults_and() {
		this._afsm.createVariable("A", this._cv);
		this._afsm.createVariable("B", this._cv);
		this._afsm.createVariable("C", this._cv);
		this._afsm.createVariable("D", this._cv);
		
		AndPredicate and=new AndPredicate(this._afsm.getVariableByName("A"),this._afsm.getVariableByName("B"));
		ExternalRule rule=new ExternalRule("E");
		rule.setCondition(and);
		this._state.addRule(rule);
		this._afsm.loadInputSpaces();
		
		Fault[] f=this._chDet.detectFaults();
		assertEquals(2, f.length);
	}
	
	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.validation.ContextHazardDetection#detectFaults()}.
	 * This test case is testing the equals fnction A==B which is supposed to have 4 faults
	 */
	@Test
	public void testDetectFaults_equals() {
		this._afsm.createVariable("A", this._cv);
		this._afsm.createVariable("B", this._cv);
		this._afsm.createVariable("C", this._cv);
		this._afsm.createVariable("D", this._cv);
		
		AndPredicate and1=new AndPredicate(this._afsm.getVariableByName("A"),new NotPredicate(this._afsm.getVariableByName("B")));
		AndPredicate and2=new AndPredicate(new NotPredicate(this._afsm.getVariableByName("A")),this._afsm.getVariableByName("B"));
		OrPredicate or=new OrPredicate(and1,and2);
		ExternalRule rule=new ExternalRule("E");
		rule.setCondition(or);
		this._state.addRule(rule);
		this._afsm.loadInputSpaces();
		
		Fault[] f=this._chDet.detectFaults();
		assertEquals(4, f.length);
	}
	
	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.validation.ContextHazardDetection#applyRegression(uk.ac.ucl.cs.InputMatrix.Fault[])}.
	 */
	@Test
	public void testApplyRegression_twoStates() {
		this._afsm.createVariable("A", this._cv);
		this._afsm.createVariable("B", this._cv);
		this._afsm.createVariable("C", this._cv);
		this._afsm.createVariable("D", this._cv);
		
		AndPredicate and1=new AndPredicate(this._afsm.getVariableByName("A"),new NotPredicate(this._afsm.getVariableByName("B")));
		AndPredicate and2=new AndPredicate(new NotPredicate(this._afsm.getVariableByName("A")),this._afsm.getVariableByName("B"));
		OrPredicate or=new OrPredicate(and1,and2);
		ExternalRule rule1=new ExternalRule("E1");
		rule1.setCondition(or);
		this._state.addRule(rule1);
		
		
		AndPredicate and=new AndPredicate(this._afsm.getVariableByName("A"),this._afsm.getVariableByName("B"));
		ExternalRule rule2=new ExternalRule("E2");
		rule2.setCondition(and);
		State s=new State("Additional State");
		s.addRule(rule2);
		this._afsm.addState(s);
		
		this._afsm.loadInputSpaces();
		
		Fault[] f=this._chDet.detectFaults();
		assertEquals(6, f.length);
	}
	
	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.validation.ContextHazardDetection#applyRegression(uk.ac.ucl.cs.InputMatrix.Fault[])}.
	 * Two rules in one state: 
	 * R1 high priority = A and B
	 * R2 normal priority = not(A) and B
	 * 
	 * Expected faults are:
	 * PrioritizedContextHazard 00->01->11
	 * ContextHazard 10->11->01
	 */
	@Test
	public void testApplyRegression_priority() {
		this._afsm.createVariable("A", this._cv);
		this._afsm.createVariable("B", this._cv);
		this._afsm.createVariable("C", this._cv);
		this._afsm.createVariable("D", this._cv);
		
		AndPredicate and1=new AndPredicate(this._afsm.getVariableByName("A"),this._afsm.getVariableByName("B"));
		ExternalRule rule1=new ExternalRule("E1");
		rule1.setCondition(and1);
		rule1.setPriority(Rule.MAX_PRIORITY);
		this._state.addRule(rule1);
		
		AndPredicate and2=new AndPredicate(new NotPredicate(this._afsm.getVariableByName("A")),this._afsm.getVariableByName("B"));
		ExternalRule rule2=new ExternalRule("E2");
		rule2.setCondition(and2);
		this._state.addRule(rule2);

		this._afsm.loadInputSpaces();
		
		Fault[] f=this._chDet.detectFaults();
		assertEquals(2, f.length);
	}
	
	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.validation.ContextHazardDetection#applyRegression(uk.ac.ucl.cs.InputMatrix.Fault[])}.
	 */
	@Test
	public void testApplyRegression() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.validation.ContextHazardDetection#inputFromVariables(uk.ac.ucl.cs.InputMatrix.PredicateVariable[], char[])}.
	 */
	@Test
	public void testInputFromVariables() {
		this._afsm.createVariable("A", this._cv);
		this._afsm.createVariable("B", this._cv);
		this._afsm.createVariable("C", this._cv);
		this._afsm.createVariable("D", this._cv);
		
		//AndPredicate and=new AndPredicate(this._afsm.getVariableByName("A"),this._afsm.getVariableByName("B"));
		ExternalRule rule=new ExternalRule("E");
		this._state.addRule(rule);
		
		
		PredicateVariable[] v;
		char[] in;
		long out;
		
		//test first
		rule.setCondition(this._afsm.getVariableByName("A"));
		this._afsm.loadInputSpaces();
		v=new PredicateVariable[]{this._afsm.getVariableByName("A")};
		in=new char[]{'0'};
		out=this._chDet.inputFromVariables(v, in);
		assertEquals(0,out);
		in=new char[]{'1'};
		out=this._chDet.inputFromVariables(v, in);
		assertEquals(1,out);
		
		//test last
		rule.setCondition(this._afsm.getVariableByName("D"));
		this._afsm.loadInputSpaces();
		v=new PredicateVariable[]{this._afsm.getVariableByName("D")};
		in=new char[]{'0'};
		out=this._chDet.inputFromVariables(v, in);
		assertEquals(0,out);
		in=new char[]{'1'};
		out=this._chDet.inputFromVariables(v, in);
		assertEquals(8,out);
		
		//test multiple
		rule.setCondition(new AndPredicate(this._afsm.getVariableByName("A"),this._afsm.getVariableByName("B")));
		this._afsm.loadInputSpaces();
		v=new PredicateVariable[]{this._afsm.getVariableByName("A"),this._afsm.getVariableByName("B")};
		in=new char[]{'0','1'};
		out=this._chDet.inputFromVariables(v, in);
		assertEquals(2, out);

		//full order
		rule.setCondition(new AndPredicate(new AndPredicate(this._afsm.getVariableByName("A"),this._afsm.getVariableByName("B")),new AndPredicate(this._afsm.getVariableByName("C"),this._afsm.getVariableByName("D"))));
		this._afsm.loadInputSpaces();
		v=new PredicateVariable[]{this._afsm.getVariableByName("D"),this._afsm.getVariableByName("C"),this._afsm.getVariableByName("A"),this._afsm.getVariableByName("B")};
		in=new char[]{'0','1','1','1'};
		out=this._chDet.inputFromVariables(v, in);
		assertEquals(7,out);
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.validation.ContextHazardDetection#generalInputFromVariables(uk.ac.ucl.cs.InputMatrix.PredicateVariable[], char[])}.
	 * @throws Exception 
	 */
	@Test
	public void testGeneralInputFromVariables(){
		this._afsm.createVariable("A", this._cv);
		this._afsm.createVariable("B", this._cv);
		this._afsm.createVariable("C", this._cv);
		this._afsm.createVariable("D", this._cv);
		
		//AndPredicate and=new AndPredicate(this._afsm.getVariableByName("A"),this._afsm.getVariableByName("B"));
		ExternalRule rule=new ExternalRule("E");
		this._state.addRule(rule);
		
		
		PredicateVariable[] v;
		char[] in;
		char[] out;
		
		//test first
		rule.setCondition(this._afsm.getVariableByName("A"));
		this._afsm.loadInputSpaces();
		v=new PredicateVariable[]{this._afsm.getVariableByName("A")};
		in=new char[]{'0'};
		out=this._chDet.generalInputFromVariables(v, in);
		assertEquals("0***",new String(out));
		in=new char[]{'1'};
		out=this._chDet.generalInputFromVariables(v, in);
		assertEquals("1***",new String(out));
		
		//test last
		rule.setCondition(this._afsm.getVariableByName("D"));
		this._afsm.loadInputSpaces();
		v=new PredicateVariable[]{this._afsm.getVariableByName("D")};
		in=new char[]{'0'};
		out=this._chDet.generalInputFromVariables(v, in);
		assertEquals("***0",new String(out));
		in=new char[]{'1'};
		out=this._chDet.generalInputFromVariables(v, in);
		assertEquals("***1",new String(out));
		
		//test multiple
		rule.setCondition(new AndPredicate(this._afsm.getVariableByName("A"),this._afsm.getVariableByName("B")));
		this._afsm.loadInputSpaces();
		v=new PredicateVariable[]{this._afsm.getVariableByName("A"),this._afsm.getVariableByName("B")};
		in=new char[]{'0','1'};
		out=this._chDet.generalInputFromVariables(v, in);
		assertEquals("01**",new String(out));

		//full order
		rule.setCondition(new AndPredicate(new AndPredicate(this._afsm.getVariableByName("A"),this._afsm.getVariableByName("B")),new AndPredicate(this._afsm.getVariableByName("C"),this._afsm.getVariableByName("D"))));
		this._afsm.loadInputSpaces();
		v=new PredicateVariable[]{this._afsm.getVariableByName("D"),this._afsm.getVariableByName("C"),this._afsm.getVariableByName("A"),this._afsm.getVariableByName("B")};
		in=new char[]{'0','1','1','1'};
		out=this._chDet.generalInputFromVariables(v, in);
		assertEquals("1110",new String(out));
	}

}
