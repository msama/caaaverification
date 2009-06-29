/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix;

import uk.ac.ucl.cs.InputMatrix.AdaptationFSM;
import uk.ac.ucl.cs.InputMatrix.AdaptationRule;
import uk.ac.ucl.cs.InputMatrix.AndPredicate;
import uk.ac.ucl.cs.InputMatrix.ContextVariable;
import uk.ac.ucl.cs.InputMatrix.ExternalRule;
import uk.ac.ucl.cs.InputMatrix.InputSpace;
import uk.ac.ucl.cs.InputMatrix.NotPredicate;
import uk.ac.ucl.cs.InputMatrix.OrPredicate;
import uk.ac.ucl.cs.InputMatrix.Rule;
import uk.ac.ucl.cs.InputMatrix.State;
import junit.framework.TestCase;

/**
 * @author rax
 *
 */
public class StateTest extends TestCase {

	AdaptationFSM _afsm;
	State _stateUT1;
	State _stateUT2;
	State _stateUT3;
	protected ContextVariable _cv;
	
	/**
	 * @param name
	 */
	public StateTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		this._afsm=new AdaptationFSM();
		this._stateUT1=new State("state under test 1");
		this._stateUT2=new State("state under test 2");
		this._stateUT3=new State("state under test 3");
		this._afsm.addState(this._stateUT1);
		this._afsm.addState(this._stateUT2);
		this._afsm.addState(this._stateUT3);
		ContextVariable cv=new ContextVariable("DummyContext",1);
		this._afsm.addContextVariable(cv);
		this._afsm.addContextVariable(this._cv);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		this._afsm=null;
		this._stateUT1=null;
		this._stateUT2=null;
		this._stateUT3=null;
		this._cv=null;
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.State#evaluateRuleForInput(int)}.
	 * @throws Exception 
	 */
	public void testEvaluateRuleForInput_1ExternalRule() throws Exception {

		this._afsm.createVariable("A",this._afsm.getContextVariableAt(0));
		ExternalRule ext_r1=new ExternalRule("ER1");
		ext_r1.setCondition(this._afsm.getVariableByName("A"));
		this._stateUT1.addRule(ext_r1);
		this._afsm.loadInputSpaces();
		Rule[] rules=this._stateUT1.evaluateRuleForInput(0);
		this.assertEquals(0, rules.length);
		rules=this._stateUT1.evaluateRuleForInput(1);
		this.assertEquals(1, rules.length);
		this.assertEquals(ext_r1, rules[0]);
	}
	
	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.State#evaluateRuleForInput(int)}.
	 * @throws Exception 
	 */
	public void testEvaluateRuleForInput_1AdaptationRule() throws Exception {
		this._afsm.createVariable("A",this._afsm.getContextVariableAt(0));
		AdaptationRule ad_r1=new AdaptationRule("AR1",this._stateUT2);
		ad_r1.setCondition(this._afsm.getVariableByName("A"));
		this._stateUT1.addRule(ad_r1);
		this._afsm.loadInputSpaces();
		Rule[] rules=this._stateUT1.evaluateRuleForInput(0);
		this.assertEquals(0, rules.length);
		rules=this._stateUT1.evaluateRuleForInput(1);
		this.assertEquals(1, rules.length);
		this.assertEquals(ad_r1, rules[0]);
	}
	
	
	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.State#evaluateRuleForInput(int)}.
	 * @throws Exception 
	 */
	public void testEvaluateRuleForInput_2AdaptationRule() throws Exception {
		this._afsm.createVariable("A",this._afsm.getContextVariableAt(0));
		AdaptationRule ad_r1=new AdaptationRule("AR1",this._stateUT2);
		AdaptationRule ad_r2=new AdaptationRule("AR2",this._stateUT3);
		ad_r1.setCondition(this._afsm.getVariableByName("A"));
		ad_r2.setCondition(this._afsm.getVariableByName("A"));
		this._stateUT1.addRule(ad_r1);
		this._stateUT1.addRule(ad_r2);
		this._afsm.loadInputSpaces();
		Rule[] rules=this._stateUT1.evaluateRuleForInput(0);
		this.assertEquals(0, rules.length);
		rules=this._stateUT1.evaluateRuleForInput(1);
		this.assertEquals(2, rules.length);
		this.assertEquals(ad_r2, rules[1]);
		this.assertEquals(ad_r1, rules[0]);
	}
	
	
	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.State#evaluateRuleForInput(int)}.
	 * @throws Exception 
	 */
	public void testEvaluateRuleForInput_AdaptationRule() throws Exception {
		this._afsm.createVariable("E_t",this._afsm.getContextVariableAt(0));
		this._afsm.createVariable("F_bt",this._afsm.getContextVariableAt(0));
		this._afsm.createVariable("G_gps",this._afsm.getContextVariableAt(0));
		
		AdaptationRule rule_a3_s2=new AdaptationRule("A3",this._stateUT2);
		OrPredicate or_a3=new OrPredicate(this._afsm.getVariableByName("F_bt"),this._afsm.getVariableByName("G_gps"));
		AndPredicate and_a3=new AndPredicate(this._afsm.getVariableByName("E_t"),new NotPredicate(or_a3));
		rule_a3_s2.setCondition(and_a3);
		this._stateUT1.addRule(rule_a3_s2);
		this._afsm.loadInputSpaces();
		Rule[] rules;
		
		
		rules=this._stateUT1.evaluateRuleForInput(0);
		this.assertEquals(0, rules.length);
		
		rules=this._stateUT1.evaluateRuleForInput(1);
		this.assertEquals(1, rules.length);
		
		rules=this._stateUT1.evaluateRuleForInput(2);
		this.assertEquals(0, rules.length);
		
		rules=this._stateUT1.evaluateRuleForInput(3);
		this.assertEquals(0, rules.length);
		
		rules=this._stateUT1.evaluateRuleForInput(4);
		this.assertEquals(0, rules.length);
		
		rules=this._stateUT1.evaluateRuleForInput(5);
		this.assertEquals(0, rules.length);
		
		rules=this._stateUT1.evaluateRuleForInput(6);
		this.assertEquals(0, rules.length);
		
		rules=this._stateUT1.evaluateRuleForInput(7);
		this.assertEquals(0, rules.length);
		
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.State#createInputSpace(int)}.
	 */
	public void testCreateInputSpace() {
		this._afsm.createVariable("A", this._cv);
		this._afsm.createVariable("B", this._cv);
		this._afsm.createVariable("C", this._cv);
		this._afsm.createVariable("D", this._cv);
		
		AndPredicate and1=new AndPredicate(this._afsm.getVariableByName("A"),this._afsm.getVariableByName("B"));
		AndPredicate and2=new AndPredicate(this._afsm.getVariableByName("C"),this._afsm.getVariableByName("D"));
		NotPredicate not=new NotPredicate(new OrPredicate(this._afsm.getVariableByName("D"),this._afsm.getVariableByName("B")));
		OrPredicate or=new OrPredicate(and1,new OrPredicate(and2,not));
		
		ExternalRule rule1=new ExternalRule("E1");
		rule1.setCondition(or);
		rule1.setPriority(Rule.MAX_PRIORITY);
		this._stateUT1.addRule(rule1);
		
		this._afsm.loadInputSpaces();
		InputSpace ispace=this._stateUT1.getInputSpace();
		
		for(int i=0;i<Math.pow(2, 4);i++)
		{
			boolean A=i%2!=0;
			boolean B=(i/2)%2!=0;
			boolean C=(i/4)%2!=0;
			boolean D=(i/8)%2!=0;
			if(((A & B) | (C & D) | !(D|B)) != ( ispace.getSatisfiedRules(i).length==1) )
			{
				System.err.println("Input: "+i+" ((A & B) | (C & D) | !(D|B)):"+((A & B) | (C & D) | !(D|B))+" iSpace:"+ispace.getSatisfiedRules(i).length);
			}
			assertEquals( ((A & B) | (C & D) | !(D|B)), ispace.getSatisfiedRules(i).length==1);
		}
	}
	
	

}
