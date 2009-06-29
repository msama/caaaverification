/**
 * 
 */
package uk.ac.ucl.cs.afsm2obdd;


import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import uk.ac.ucl.cs.afsm.common.AdaptationFiniteStateMachine;
import uk.ac.ucl.cs.afsm.common.State;
import uk.ac.ucl.cs.afsm.common.predicate.Constant;
import uk.ac.ucl.cs.afsm.common.predicate.Predicate;

/**
 * @author -RAX- (Michele Sama)
 *
 */
public class AfsmParserTest {

	private final static String STATE_ZERO = "S0";
	private final static String STATE_ONE = "S1";
	
	private final static String RULE_ZERO = "R0";
	private final static String RULE_ONE = "R1";
	
	private AfsmParser parser;
	private AdaptationFiniteStateMachine afsm;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		afsm = null;
		parser = null;
	}

	@Test
	public void testCreateBDDFromBooleanEncoding() {
		afsm = new AdaptationFiniteStateMachine();
		afsm.state(STATE_ZERO, true, false);
		afsm.state(STATE_ONE, false, false);
		parser = new AfsmParser(afsm, true, true);
		int indexOfStates = 0;
		
		BDD expectedS0 = parser.getFactory().nithVar(indexOfStates);
		BDD s0 = parser.getStateBdd(STATE_ZERO).getEncoding();
		assertTrue("Wrong encoding for state zero. Found: " + s0 + " expected: " + expectedS0,
				s0.equals(expectedS0));
		
		BDD expectedS1 = parser.getFactory().ithVar(indexOfStates);
		BDD s1 = parser.getStateBdd(STATE_ONE).getEncoding();
		assertTrue("Wrong encoding for state one. Found: " + s1 + " expected: " + expectedS1,
				s1.equals(expectedS1));
	}
	
	@Test
	public void testNotEncodeRule() {
		afsm = new AdaptationFiniteStateMachine();
		State s0 = afsm.state(STATE_ZERO, true, false);
		State s1 = afsm.state(STATE_ONE, false, false);
		afsm.rule(RULE_ZERO, Constant.TRUE, s1);
		afsm.rule(RULE_ONE, Constant.TRUE, s0);
		parser = new AfsmParser(afsm, false, false);
		
		BDD rBdd0 = parser.getRuleBdd(RULE_ZERO).getEncoding();
		
		assertTrue("Bed encoding for rule0: s0=" + rBdd0 + ".",
				rBdd0.apply(parser.getFactory().nithVar(0 + 4), BDDFactory.and).isZero());
		
		BDD rBdd1 = parser.getRuleBdd(RULE_ONE).getEncoding();
		
		assertTrue("Bed encoding for rule1: s1=" + rBdd1 + ".",
				rBdd1.apply(parser.getFactory().nithVar(1 + 4), BDDFactory.and).isZero());
		
		assertTrue("Rules encoding should be exclusive: " +
				"r0=" + rBdd0 + " r1=" + rBdd1 + ".",
				rBdd0.apply(rBdd1, BDDFactory.and).isZero());
	}
	
	@Test
	public void testNotEncodeStates() {
		afsm = new AdaptationFiniteStateMachine();
		afsm.state(STATE_ZERO, true, false);
		afsm.state(STATE_ONE, false, false);
		parser = new AfsmParser(afsm, false, false);
		
		BDD sBdd0 = parser.getStateBdd(STATE_ZERO).getEncoding();
		
		assertTrue("Bed encoding for state0: s0=" + sBdd0 + ".",
				sBdd0.apply(parser.getFactory().nithVar(0), BDDFactory.and).isZero());
		
		BDD sBdd1 = parser.getStateBdd(STATE_ONE).getEncoding();
		
		assertTrue("Bed encoding for state1: s1=" + sBdd1 + ".",
				sBdd1.apply(parser.getFactory().nithVar(1), BDDFactory.and).isZero());
		
		assertTrue("States encoding should be exclusive: " +
				"s0=" + sBdd0 + " s1=" + sBdd1 + ".",
				sBdd0.apply(sBdd1, BDDFactory.and).isZero());
	}
	
	@Test
	public void testStatePairing() {
		afsm = new AdaptationFiniteStateMachine();
		afsm.state(STATE_ZERO, true, false);
		afsm.state(STATE_ONE, false, false);
		parser = new AfsmParser(afsm, false, false);
		
		BDD s0now = parser.states.get(0).getEncoding();
		BDD s0future = parser.states.get(0).getFutureEncoding();
		
		BDD s1now = parser.states.get(1).getEncoding();
		BDD s1future = parser.states.get(1).getFutureEncoding();
		
		BDD s0futureToPast = s0future.replace(parser.getStatePairingFutureToPast());
		BDD s1futureToPast = s1future.replace(parser.getStatePairingFutureToPast());
		
		assertTrue("Variables have not swapped correctly: " +
				"s0now=" + s0now + " s0futureToPast=" + s0futureToPast, 
				s0now.equals(s0futureToPast));
		
		assertTrue("Variables have not swapped correctly: " +
				"s1now=" + s1now + " s1futureToPast=" + s1futureToPast, 
				s1now.equals(s1futureToPast));
		
		BDD s0pastToFuture = s0now.replace(parser.getStatePairingPastToFuture());
		BDD s1pastToFuture = s1now.replace(parser.getStatePairingPastToFuture());
		
		assertTrue("Variables have not swapped correctly: " +
				"s0future=" + s0future + " s0pastToFuture=" + s0pastToFuture, 
				s0future.equals(s0pastToFuture));
		
		assertTrue("Variables have not swapped correctly: " +
				"s1future=" + s1future + " s1pastToFuture=" + s1pastToFuture, 
				s1future.equals(s1pastToFuture));
	}
}
