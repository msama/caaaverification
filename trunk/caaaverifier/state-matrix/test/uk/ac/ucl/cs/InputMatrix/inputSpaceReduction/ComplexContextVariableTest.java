/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix.inputSpaceReduction;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.ucl.cs.InputMatrix.AdaptationFSM;
import uk.ac.ucl.cs.InputMatrix.inputSpaceReduction.ComplexContextVariable;

/**
 * @author rax
 *
 */
public class ComplexContextVariableTest {

	protected AdaptationFSM _afsm;
	protected ComplexContextVariable<String> _cv;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this._afsm=new AdaptationFSM();
		this._cv=new ComplexContextVariable<String>("Dummy context",1);
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
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.inputSpaceReduction.ComplexContextVariable#addConditionEquals(uk.ac.ucl.cs.InputMatrix.Variable, java.lang.Object)}.
	 * @throws Exception 
	 */
	@Test
	public void testAddConditionEquals() throws Exception {
		this._afsm.createVariable("A", this._cv);
		this._cv.addConditionEquals(this._afsm.getVariableByName("A"), "dummy value");
		char c[][]=this._cv.generateInputSpace(this._afsm.getInputDimension());
		assertEquals(2,c.length);
		assertEquals(1,c[0].length);
		assertEquals('1',c[0][0]);
		assertEquals('0',c[1][0]);
	}
	
	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.inputSpaceReduction.ComplexContextVariable#addConditionEquals(uk.ac.ucl.cs.InputMatrix.Variable, java.lang.Object)}.
	 * @throws Exception 
	 */
	@Test
	public void testAddConditionEquals_2var_samevalue() throws Exception {
		this._afsm.createVariable("A0", this._cv);
		this._afsm.createVariable("A1", this._cv);
		this._cv.addConditionEquals(this._afsm.getVariableByName("A0"), "value");
		this._cv.addConditionEquals(this._afsm.getVariableByName("A1"), "value");
		char c[][]=this._cv.generateInputSpace(this._afsm.getInputDimension());
		assertEquals(2,c.length);
		assertEquals(2,c[0].length);
		assertEquals('1',c[0][0]);
		assertEquals('0',c[1][0]);
		assertEquals('1',c[0][1]);
		assertEquals('0',c[1][1]);
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.inputSpaceReduction.ComplexContextVariable#addConditionDifferent(uk.ac.ucl.cs.InputMatrix.Variable, java.lang.Object)}.
	 * @throws Exception 
	 */
	@Test
	public void testAddConditionDifferent() throws Exception {
		this._afsm.createVariable("A", this._cv);
		this._cv.addConditionDifferent(this._afsm.getVariableByName("A"), "dummy value");
		char c[][]=this._cv.generateInputSpace(this._afsm.getInputDimension());
		assertEquals(2,c.length);
		assertEquals(1,c[0].length);
		assertEquals('0',c[0][0]);
		assertEquals('1',c[1][0]);
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.inputSpaceReduction.ComplexContextVariable#generateInputSpace(int)}.
	 * @throws Exception 
	 */
	@Test
	public void testGenerateInputSpace() throws Exception {
		this._afsm.createVariable("A", this._cv);
		this._afsm.createVariable("B0", this._cv);
		this._afsm.createVariable("B1", this._cv);
		this._afsm.createVariable("C", this._cv);
		
		this._cv.addConditionEquals(this._afsm.getVariableByName("A"), "valueA");
		this._cv.addConditionEquals(this._afsm.getVariableByName("B0"), "valueB");
		this._cv.addConditionDifferent(this._afsm.getVariableByName("B1"), "valueB");
		this._cv.addConditionDifferent(this._afsm.getVariableByName("C"), "valueC");
		
		char c[][]=this._cv.generateInputSpace(this._afsm.getInputDimension());
		
		assertEquals(4,c.length);
		assertEquals(4,c[0].length);
		
		//A
		assertEquals('1',c[0][0]);
		assertEquals('0',c[1][0]);
		assertEquals('0',c[2][0]);
		assertEquals('0',c[3][0]);
		
		//B0
		assertEquals('0',c[0][1]);
		assertEquals('1',c[1][1]);
		assertEquals('0',c[2][1]);
		assertEquals('0',c[3][1]);
		
		//B1
		assertEquals('1',c[0][2]);
		assertEquals('0',c[1][2]);
		assertEquals('1',c[2][2]);
		assertEquals('1',c[3][2]);
		
		//A
		assertEquals('1',c[0][3]);
		assertEquals('1',c[1][3]);
		assertEquals('0',c[2][3]);
		assertEquals('1',c[3][3]);
	}

}
