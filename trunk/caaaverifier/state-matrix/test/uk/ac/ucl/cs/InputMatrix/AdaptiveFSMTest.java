/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix;

import java.util.Vector;

import uk.ac.ucl.cs.InputMatrix.AdaptationFSM;
import uk.ac.ucl.cs.InputMatrix.State;
import junit.framework.TestCase;

/**
 * @author rax
 *
 */
public class AdaptiveFSMTest extends TestCase {
	
	protected AdaptationFSM _AFSMUnderTest;
	protected Vector<State> _states;

	public AdaptiveFSMTest(String name) {
		super(name);
	}

	/**
	 * @throws java.lang.Exception
	 */
	protected void setUp() throws Exception {
		super.setUp();
		this._AFSMUnderTest=new AdaptationFSM();
		this._states=new Vector<State>();
		this._states.addElement(new State("State 1"));
		this._states.addElement(new State("State 2"));
		this._states.addElement(new State("State 3"));
		this._states.addElement(new State("State 4"));
	}

	/**
	 * @throws java.lang.Exception
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		this._AFSMUnderTest=null;

	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.AdaptationFSM#setInitialState(uk.ac.ucl.cs.InputMatrix.State)}.
	 */
	public void testSetInitialState() throws Exception {
		this._AFSMUnderTest.addState(this._states.elementAt(0));
		this._AFSMUnderTest.setInitialState(this._states.elementAt(0));
		this.assertEquals(this._states.elementAt(0), this._AFSMUnderTest.getInitialState());
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.AdaptationFSM#getInitialState()}.
	 */
	public void testGetInitialState() throws Exception  {
		this._AFSMUnderTest.addState(this._states.elementAt(0));
		this._AFSMUnderTest.setInitialState(this._states.elementAt(0));
		this._AFSMUnderTest.removeState(this._states.elementAt(0));
		this.assertNull(this._AFSMUnderTest.getInitialState());
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.AdaptationFSM#addState(uk.ac.ucl.cs.InputMatrix.State)}.
	 */
	public void testAddState() throws Exception {
		this.assertEquals(0, this._AFSMUnderTest.stateSize());
		this._AFSMUnderTest.addState(this._states.elementAt(0));
		this.assertEquals(1, this._AFSMUnderTest.stateSize());
		this._AFSMUnderTest.addState(this._states.elementAt(1));
		this.assertEquals(2, this._AFSMUnderTest.stateSize());
		this._AFSMUnderTest.addState(this._states.elementAt(2));
		this.assertEquals(3, this._AFSMUnderTest.stateSize());
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.AdaptationFSM#removeState(java.lang.Object)}.
	 */
	public void testRemoveState() throws Exception {
		this.assertEquals(0, this._AFSMUnderTest.stateSize());
		for(State s:this._states)
		{
			this._AFSMUnderTest.addState(s);
		}
		this.assertEquals(this._states.size(), this._AFSMUnderTest.stateSize());
		if(this._states.size()>1){
			boolean flag= this._AFSMUnderTest.removeState(this._states.elementAt(0));
			this.assertTrue("Removed first item", flag);
			flag= this._AFSMUnderTest.removeState(this._states.elementAt(0));
			this.assertFalse("Removed first item", flag);
		}
		for(State s:this._states)
		{
			this._AFSMUnderTest.removeState(s);
		}
		this.assertEquals(0, this._AFSMUnderTest.stateSize());
	}


}
