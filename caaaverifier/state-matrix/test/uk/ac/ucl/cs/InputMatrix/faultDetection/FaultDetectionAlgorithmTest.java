/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix.faultDetection;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.ucl.cs.InputMatrix.AdaptationFSM;
import uk.ac.ucl.cs.InputMatrix.ContextVariable;
import uk.ac.ucl.cs.InputMatrix.ExternalRule;
import uk.ac.ucl.cs.InputMatrix.OrPredicate;
import uk.ac.ucl.cs.InputMatrix.State;
import uk.ac.ucl.cs.InputMatrix.PredicateVariable;
import uk.ac.ucl.cs.InputMatrix.validation.ContextHazardDetectionOptimized;
import uk.ac.ucl.cs.InputMatrix.validation.FaultDetectionAlgorithm;

/**
 * @author michsama
 *
 */
public class FaultDetectionAlgorithmTest {

	protected AdaptationFSM _afsm;
	protected ContextVariable _cv;
	protected ContextHazardDetectionOptimized _chDet;
	protected State _state;
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		this._afsm=new AdaptationFSM();
		
		this._cv=new ContextVariable("Dummy context",1);
		this._afsm.addContextVariable(this._cv);
		
		this._chDet=new ContextHazardDetectionOptimized();
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
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.validation.FaultDetectionAlgorithm#inputFromVariables(uk.ac.ucl.cs.InputMatrix.PredicateVariable[], char[])}.
	 */
	@Test
	public void testInputFromVariables() {
		this._afsm.createVariable("A", this._cv);
		this._afsm.createVariable("B", this._cv);
		this._afsm.createVariable("C", this._cv);
		this._afsm.createVariable("D", this._cv);
		
		FaultDetectionAlgorithm alg= new ContextHazardDetectionOptimized();
		alg.setAFSMUnderTest(this._afsm);
		
		long l;
		char input[];
		PredicateVariable[] var;
		
		var=new PredicateVariable[]{this._afsm.getVariableByName("A")};
		input=new char[]{'0'};
		l=alg.inputFromVariables(var, input);
		assertEquals(0, l);
		
		var=new PredicateVariable[]{this._afsm.getVariableByName("A")};
		input=new char[]{'1'};
		l=alg.inputFromVariables(var, input);
		assertEquals(1, l);
		
		var=new PredicateVariable[]{this._afsm.getVariableByName("D")};
		input=new char[]{'1'};
		l=alg.inputFromVariables(var, input);
		assertEquals(8, l);
		
		var=new PredicateVariable[]{this._afsm.getVariableByName("C"),this._afsm.getVariableByName("D")};
		input=new char[]{'1','0'};
		l=alg.inputFromVariables(var, input);
		assertEquals(4, l);
		
		var=new PredicateVariable[]{this._afsm.getVariableByName("C"),this._afsm.getVariableByName("D")};
		input=new char[]{'1','1'};
		l=alg.inputFromVariables(var, input);
		assertEquals(12, l);

		
		var=new PredicateVariable[]{this._afsm.getVariableByName("C"),this._afsm.getVariableByName("D")};
		l=alg.inputFromVariables(var, FaultDetectionAlgorithm.longToInputSequence(2,2));
		assertEquals(8, l);	
		}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.validation.FaultDetectionAlgorithm#generalInputFromVariables(uk.ac.ucl.cs.InputMatrix.PredicateVariable[], char[])}.
	 */
	@Test
	public void testGeneralInputFromVariables() {
		this._afsm.createVariable("A", this._cv);
		this._afsm.createVariable("B", this._cv);
		this._afsm.createVariable("C", this._cv);
		this._afsm.createVariable("D", this._cv);
		
		FaultDetectionAlgorithm alg= new ContextHazardDetectionOptimized();
		alg.setAFSMUnderTest(this._afsm);
		
		char[] c;
		char input[];
		PredicateVariable[] var;
		
		var=new PredicateVariable[]{this._afsm.getVariableByName("A")};
		input=new char[]{'1'};
		c=alg.generalInputFromVariables(var, input);
		assertEquals(4, c.length);
		assertEquals('1', c[0]);
		assertEquals('*', c[1]);
		assertEquals('*', c[2]);
		assertEquals('*', c[3]);

		
		var=new PredicateVariable[]{this._afsm.getVariableByName("D")};
		input=new char[]{'1'};
		c=alg.generalInputFromVariables(var, input);
		assertEquals(4, c.length);
		assertEquals('*', c[0]);
		assertEquals('*', c[1]);
		assertEquals('*', c[2]);
		assertEquals('1', c[3]);
		
		var=new PredicateVariable[]{this._afsm.getVariableByName("C"),this._afsm.getVariableByName("D")};
		input=new char[]{'1','0'};
		c=alg.generalInputFromVariables(var, input);
		assertEquals(4, c.length);
		assertEquals('*', c[0]);
		assertEquals('*', c[1]);
		assertEquals('1', c[2]);
		assertEquals('0', c[3]);
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.validation.FaultDetectionAlgorithm#longToInputSequence(long, int)}.
	 */
	@Test
	public void testLongToInputSequence() {
		char[] c;
		
		c=FaultDetectionAlgorithm.longToInputSequence(0,5);
		assertEquals(5, c.length);
		assertEquals('0', c[0]);
		assertEquals('0', c[1]);
		assertEquals('0', c[2]);
		assertEquals('0', c[3]);
		assertEquals('0', c[4]);
		
		c=FaultDetectionAlgorithm.longToInputSequence(1,5);
		assertEquals(5, c.length);
		assertEquals('1', c[0]);
		assertEquals('0', c[1]);
		assertEquals('0', c[2]);
		assertEquals('0', c[3]);
		assertEquals('0', c[4]);
		
		c=FaultDetectionAlgorithm.longToInputSequence(2,5);
		assertEquals(5, c.length);
		assertEquals('0', c[0]);
		assertEquals('1', c[1]);
		assertEquals('0', c[2]);
		assertEquals('0', c[3]);
		assertEquals('0', c[4]);
		
		c=FaultDetectionAlgorithm.longToInputSequence(3,5);
		assertEquals(5, c.length);
		assertEquals('1', c[0]);
		assertEquals('1', c[1]);
		assertEquals('0', c[2]);
		assertEquals('0', c[3]);
		assertEquals('0', c[4]);
		
		c=FaultDetectionAlgorithm.longToInputSequence(12,5);
		assertEquals(5, c.length);
		assertEquals('0', c[0]);
		assertEquals('0', c[1]);
		assertEquals('1', c[2]);
		assertEquals('1', c[3]);
		assertEquals('0', c[4]);
		
		c=FaultDetectionAlgorithm.longToInputSequence(31,5);
		assertEquals(5, c.length);
		assertEquals('1', c[0]);
		assertEquals('1', c[1]);
		assertEquals('1', c[2]);
		assertEquals('1', c[3]);
		assertEquals('1', c[4]);
	}

}
