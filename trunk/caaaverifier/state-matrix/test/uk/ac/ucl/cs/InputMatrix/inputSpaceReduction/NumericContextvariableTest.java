/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix.inputSpaceReduction;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.ucl.cs.InputMatrix.AdaptationFSM;
import uk.ac.ucl.cs.InputMatrix.inputSpaceReduction.NumericContextVariable;

/**
 * @author rax
 *
 */
public class NumericContextvariableTest {

	protected AdaptationFSM _afsm;
	protected NumericContextVariable _cv;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this._afsm=new AdaptationFSM();
		this._cv=new NumericContextVariable("Dummy context",1);
		this._afsm.addContextVariable(this._cv);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this._afsm=null;
		this._cv=null;
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.inputSpaceReduction.NumericContextVariable#addConditionEquals(uk.ac.ucl.cs.InputMatrix.Variable, double)}.
	 * @throws Exception 
	 */
	@Test
	public void testAddConditionEquals() throws Exception {
		this._afsm.createVariable("A", this._cv);
		this._cv.addConditionEquals(this._afsm.getVariableByName("A"), 10);
		char c[][]=this._cv.generateInputSpace(this._afsm.getInputDimension());
		assertEquals(3,c.length);
		assertEquals(1,c[0].length);
		assertEquals('0',c[0][0]);
		assertEquals('1',c[1][0]);
		assertEquals('0',c[2][0]);
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.inputSpaceReduction.NumericContextVariable#addConditionDifferent(uk.ac.ucl.cs.InputMatrix.Variable, double)}.
	 * @throws Exception 
	 */
	@Test
	public void testAddConditionDifferent() throws Exception {
		this._afsm.createVariable("A", this._cv);
		this._cv.addConditionDifferent(this._afsm.getVariableByName("A"), 10);
		char c[][]=this._cv.generateInputSpace(this._afsm.getInputDimension());
		assertEquals(3,c.length);
		assertEquals(1,c[0].length);
		assertEquals('1',c[0][0]);
		assertEquals('0',c[1][0]);
		assertEquals('1',c[2][0]);
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.inputSpaceReduction.NumericContextVariable#addConditionGreater(uk.ac.ucl.cs.InputMatrix.Variable, double)}.
	 * @throws Exception 
	 */
	@Test
	public void testAddConditionGreater() throws Exception {
		this._afsm.createVariable("A", this._cv);
		this._cv.addConditionGreater(this._afsm.getVariableByName("A"), 10);
		char c[][]=this._cv.generateInputSpace(this._afsm.getInputDimension());
		assertEquals(3,c.length);
		assertEquals(1,c[0].length);
		assertEquals('0',c[0][0]);
		assertEquals('0',c[1][0]);
		assertEquals('1',c[2][0]);
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.inputSpaceReduction.NumericContextVariable#addConditionLesser(uk.ac.ucl.cs.InputMatrix.Variable, double)}.
	 * @throws Exception 
	 */
	@Test
	public void testAddConditionLesser() throws Exception {
		this._afsm.createVariable("A", this._cv);
		this._cv.addConditionLesser(this._afsm.getVariableByName("A"), 10);
		char c[][]=this._cv.generateInputSpace(this._afsm.getInputDimension());
		assertEquals(3,c.length);
		assertEquals(1,c[0].length);
		assertEquals('1',c[0][0]);
		assertEquals('0',c[1][0]);
		assertEquals('0',c[2][0]);
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.inputSpaceReduction.NumericContextVariable#addConditionGreaterEquals(uk.ac.ucl.cs.InputMatrix.Variable, double)}.
	 * @throws Exception 
	 */
	@Test
	public void testAddConditionGreaterEquals() throws Exception {
		this._afsm.createVariable("A", this._cv);
		this._cv.addConditionGreaterEquals(this._afsm.getVariableByName("A"), 10);
		char c[][]=this._cv.generateInputSpace(this._afsm.getInputDimension());
		assertEquals(3,c.length);
		assertEquals(1,c[0].length);
		assertEquals('0',c[0][0]);
		assertEquals('1',c[1][0]);
		assertEquals('1',c[2][0]);
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.inputSpaceReduction.NumericContextVariable#addConditionLesserEquals(uk.ac.ucl.cs.InputMatrix.Variable, double)}.
	 * @throws Exception 
	 */
	@Test
	public void testAddConditionLesserEquals() throws Exception {
		this._afsm.createVariable("A", this._cv);
		this._cv.addConditionLesserEquals(this._afsm.getVariableByName("A"), 10);
		char c[][]=this._cv.generateInputSpace(this._afsm.getInputDimension());
		assertEquals(3,c.length);
		assertEquals(1,c[0].length);
		assertEquals('1',c[0][0]);
		assertEquals('1',c[1][0]);
		assertEquals('0',c[2][0]);
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.inputSpaceReduction.NumericContextVariable#addConditionIncluded(uk.ac.ucl.cs.InputMatrix.Variable, double, double)}.
	 * @throws Exception 
	 */
	@Test
	public void testAddConditionIncluded() throws Exception {
		this._afsm.createVariable("A", this._cv);
		this._cv.addConditionIncluded(this._afsm.getVariableByName("A"), 5, 10);
		char c[][]=this._cv.generateInputSpace(this._afsm.getInputDimension());
		assertEquals(5,c.length);
		assertEquals(1,c[0].length);
		assertEquals('0',c[0][0]);
		assertEquals('1',c[1][0]);
		assertEquals('1',c[2][0]);
		assertEquals('1',c[3][0]);
		assertEquals('0',c[4][0]);
	}
	
	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.inputSpaceReduction.NumericContextVariable#addConditionIncluded(uk.ac.ucl.cs.InputMatrix.Variable, double, double)}.
	 * @throws Exception 
	 */
	@Test
	public void testAddConditionIncluded_2var() throws Exception {
		this._afsm.createVariable("A", this._cv);
		this._afsm.createVariable("B", this._cv);
		this._cv.addConditionIncluded(this._afsm.getVariableByName("A"), 5, 10);
		this._cv.addConditionLesser(this._afsm.getVariableByName("B"), 7);
		char c[][]=this._cv.generateInputSpace(this._afsm.getInputDimension());
		assertEquals(7,c.length);
		assertEquals(2,c[0].length);
		assertEquals('0',c[0][0]);
		assertEquals('1',c[1][0]);
		assertEquals('1',c[2][0]);
		assertEquals('1',c[3][0]);
		assertEquals('1',c[4][0]);
		assertEquals('1',c[5][0]);
		assertEquals('0',c[6][0]);
		
		assertEquals(2,c[0].length);
		assertEquals('1',c[0][1]);
		assertEquals('1',c[1][1]);
		assertEquals('1',c[2][1]);
		assertEquals('0',c[3][1]);
		assertEquals('0',c[4][1]);
		assertEquals('0',c[5][1]);
		assertEquals('0',c[6][1]);
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.inputSpaceReduction.NumericContextVariable#addConditionExcluded(uk.ac.ucl.cs.InputMatrix.Variable, double, double)}.
	 * @throws Exception 
	 */
	@Test
	public void testAddConditionExcluded() throws Exception {
		this._afsm.createVariable("A", this._cv);
		this._cv.addConditionExcluded(this._afsm.getVariableByName("A"), 5, 10);
		char c[][]=this._cv.generateInputSpace(this._afsm.getInputDimension());
		assertEquals(5,c.length);
		assertEquals(1,c[0].length);
		assertEquals('1',c[0][0]);
		assertEquals('0',c[1][0]);
		assertEquals('0',c[2][0]);
		assertEquals('0',c[3][0]);
		assertEquals('1',c[4][0]);
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.inputSpaceReduction.NumericContextVariable#generateInputSpace(int)}.
	 */
	@Test
	public void testGenerateInputSpace() {
		fail("Not yet implemented");
	}

}
