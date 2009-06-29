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
import uk.ac.ucl.cs.InputMatrix.Rule;
import uk.ac.ucl.cs.InputMatrix.State;
import uk.ac.ucl.cs.InputMatrix.PredicateVariable;
import uk.ac.ucl.cs.InputMatrix.validation.ContextHazardFault;
import uk.ac.ucl.cs.InputMatrix.validation.MinimumSafeDelayGenerator;

/**
 * @author michsama
 *
 */
public class MinimumSafeDelayGeneratorTest {

	AdaptationFSM _afsm;
	ContextVariable _cv1;
	ContextVariable _cv2;
	ContextVariable _cv3;
	ContextVariable _cv4;
	ContextVariable _cv5;
	State _s1;
	State _s2;
	Rule _r1;
	Rule _r2;
	Rule _r3;
	Rule _r4;
	Rule _r5;
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this._afsm=new AdaptationFSM();
		
		//states
		this._s1=new State("s1");
		this._afsm.addState(this._s1);
		this._s2=new State("s2");
		this._afsm.addState(this._s2);
		
		//context
		this._cv1=new ContextVariable("cv1",1);
		this._afsm.addContextVariable(this._cv1);
		this._cv2=new ContextVariable("cv2",2);
		this._afsm.addContextVariable(this._cv2);
		this._cv3=new ContextVariable("cv3",3);
		this._afsm.addContextVariable(this._cv3);
		this._cv4=new ContextVariable("cv4",4);
		this._afsm.addContextVariable(this._cv4);
		this._cv5=new ContextVariable("cv5",5);
		this._afsm.addContextVariable(this._cv5);
		
		//variables
		this._afsm.createVariable("v1", this._cv1);
		this._afsm.createVariable("v2", this._cv2);
		this._afsm.createVariable("v3", this._cv3);
		this._afsm.createVariable("v4", this._cv4);
		this._afsm.createVariable("v5", this._cv5);

		//rules
		this._r1=new Rule("r1");
		this._r1.setCondition(this._afsm.getVariableByName("v1"));
		this._s1.addRule(this._r1);
		this._s2.addRule(this._r1);
		this._r2=new Rule("r2");
		this._r2.setCondition(this._afsm.getVariableByName("v2"));
		this._s1.addRule(this._r2);
		this._s2.addRule(this._r2);
		this._r3=new Rule("r3");
		this._r3.setCondition(this._afsm.getVariableByName("v3"));
		this._s1.addRule(this._r3);
		this._s2.addRule(this._r3);
		this._r4=new Rule("r4");
		this._r4.setCondition(this._afsm.getVariableByName("v4"));
		this._s1.addRule(this._r4);
		this._s2.addRule(this._r4);
		this._r5=new Rule("r5");
		this._r5.setCondition(this._afsm.getVariableByName("v5"));
		this._s1.addRule(this._r5);
		this._s2.addRule(this._r5);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this._afsm=null;
		this._cv1=null;
		this._cv2=null;
		this._cv3=null;
		this._cv4=null;
		this._cv5=null;
		this._s1=null;
		this._s2=null;
		this._r1=null;
		this._r2=null;
		this._r3=null;
		this._r4=null;
		this._r5=null;
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.validation.MinimumSafeDelayGenerator#generateDelays(uk.ac.ucl.cs.InputMatrix.Fault[])}.
	 */
	@Test
	public void testGenerateDelays() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.validation.MinimumSafeDelayGenerator#getMinimumSafeDelay(uk.ac.ucl.cs.InputMatrix.validation.ContextHazardFault, long, uk.ac.ucl.cs.InputMatrix.PredicateVariable[])}.
	 */
	@Test
	public void testGetMinimumSafeDelay() {		
		ContextHazardFault f;
		PredicateVariable[] vars;
		long time;
		
		//test time
		vars=new PredicateVariable[]{this._afsm.getVariableByName("v2"), this._afsm.getVariableByName("v1")};
		f=new ContextHazardFault(ContextHazardFault.CONTEXT_STABILITY_HAZARD, "00000", this._s1, vars, new Rule[]{this._r1});
		time=MinimumSafeDelayGenerator.getMinimumSafeDelay(f, 0, new PredicateVariable[]{});
		assertEquals(2, time);
		time=MinimumSafeDelayGenerator.getMinimumSafeDelay(f, 0, new PredicateVariable[]{this._afsm.getVariableByName("v2")});
		assertEquals(1, time);
		time=MinimumSafeDelayGenerator.getMinimumSafeDelay(f, 0, new PredicateVariable[]{this._afsm.getVariableByName("v2"), this._afsm.getVariableByName("v1")});
		assertEquals(0, time);
		time=MinimumSafeDelayGenerator.getMinimumSafeDelay(f, 1, new PredicateVariable[]{this._afsm.getVariableByName("v2")});
		assertEquals(0, time);
		
		//test context order
		vars=new PredicateVariable[]{this._afsm.getVariableByName("v5"), this._afsm.getVariableByName("v1"), this._afsm.getVariableByName("v5")};
		f=new ContextHazardFault(ContextHazardFault.CONTEXT_STABILITY_HAZARD, "00000", this._s1, vars, new Rule[]{this._r1});
		time=MinimumSafeDelayGenerator.getMinimumSafeDelay(f, 0, new PredicateVariable[]{});
		assertEquals(5, time);
		time=MinimumSafeDelayGenerator.getMinimumSafeDelay(f, 0, new PredicateVariable[]{this._afsm.getVariableByName("v5")});
		assertEquals(1, time);
		time=MinimumSafeDelayGenerator.getMinimumSafeDelay(f, 3, new PredicateVariable[]{this._afsm.getVariableByName("v5")});
		assertEquals(0, time);
	}

}
