/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix;

import uk.ac.ucl.cs.InputMatrix.ContextVariable;
import uk.ac.ucl.cs.InputMatrix.PredicateVariable;
import junit.framework.TestCase;

/**
 * @author rax
 *
 */
public class VariableTest extends TestCase {

	/**
	 * @param name
	 */
	public VariableTest(String name) {
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
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.PredicateVariable#getValue(int)}.
	 */
	public void testGetValue() {
		PredicateVariable v=new PredicateVariable("V0",0, new ContextVariable("cv0",1));
		this.assertTrue("Variable 0 with input 1",v.getValue(1));
		this.assertFalse("Variable 0 with input 0",v.getValue(0));
		this.assertFalse("Variable 0 with input 2",v.getValue(2));
		this.assertTrue("Variable 0 with input 3",v.getValue(3));
		
		v=new PredicateVariable("V1",1, new ContextVariable("cv1",1));
		this.assertTrue("Variable 1 with input 2",v.getValue(2));
		this.assertFalse("Variable 1 with input 1",v.getValue(1));
		this.assertFalse("Variable 1 with input 0",v.getValue(0));
		this.assertTrue("Variable 1 with input 3",v.getValue(3));
	}

}
