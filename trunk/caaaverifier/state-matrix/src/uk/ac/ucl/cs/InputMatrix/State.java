/**
 * This class represent a state in the A-FSM
 * 
 * 
 */
package uk.ac.ucl.cs.InputMatrix;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author rax
 *
 */
public class State {

	private String name;
	
	private InputSpace inputSpace;
	private boolean forbidden = false;
	private boolean start = false;
	
	/**
	 * Contains all the active rules in this state.
	 */
	private List<Rule> rules = new ArrayList<Rule>();
	
	/**
	 * Contains a {@link Predicate} of assumptions which should be satisfied while in the state.
	 * 
	 * <p>While in this state those assumption should not be violated.
	 */
	private Predicate inStateAssumptions;
	
	/**
	 * Defines a condition on which the state is forced to be stable and prevents rules to be evaluated.  
	 */
	private Predicate holdCondition;
	
	/**
	 * 
	 */
	public State(String name) {
		super();
		this.name = name;
	}
	
	public boolean isForbiddenState(){
		return forbidden;
	}
	
	public boolean isStartState(){
		return start;
	}
	
	public void setForbiddenState(boolean forbidden){
		this.forbidden = forbidden;
	}
	
	public void setStartState(boolean start){
		this.start = start;
	}
	
	public String getName(){
		return name;
	}
	
	/**
	 * Returns an array of all the variables evaluated in this state. This methods calls BooleanPredicate.getVariables().
	 * @return array containing all the variables used in this states
	 */
	public PredicateVariable[] getVariables()
	{
		Set<PredicateVariable> variables = new HashSet<PredicateVariable>();
		for(Rule r : rules)
		{
			PredicateVariable[] var = r.getCondition().getVariables();
			for(PredicateVariable v:var)
			{
				variables.add(v);
			}
		}
		return variables.toArray(new PredicateVariable[variables.size()]);
	}
	
	/**
	 * Method to test which rule are satisfied in a specific state according to a specific input.
	 * 
	 * @param input the input in which we need informations
	 * @return an array containing all the satisfied rules
	 */
	public Rule[] evaluateRuleForInput(int input)
	{
		List<Rule> satisfiedRules = new ArrayList<Rule>();
		for(Rule r : rules)
		{
			if(r.getCondition().getValue(input))
			{
				satisfiedRules.add(r);
			}
		}
		return satisfiedRules.toArray(new Rule[satisfiedRules.size()]);
	}
	
	/**
	 * Create the input space according to a specific dimension
	 * @param inputDimension the dimension of the input space
	 */
	public void createInputSpace(int inputDimension, Constraint[] constraints)
	{
		Predicate constrainPredicate = generateConstrainPredicate(constraints);
		
		long skippedInput=0;
		long createdInput=0;
		
		inputSpace = new InputSpace(inputDimension);
		double max = Math.pow(2, inputDimension);
		for (int i = 0; i < max; i++) {
			//apply constraints
			if (!constrainPredicate.getValue(i)) {
				skippedInput++;
				inputSpace.setForbiddenInput(i, true);
				continue;
			}

			createdInput++;
			
			if (holdCondition == null || !holdCondition.getValue(i)) {
				for(Rule r : rules)
				{
					if(r.getCondition().getValue(i)==true)
					{
						inputSpace.addSatisfiedRule(i, r);
					}
				}
			}
		}
		
		System.out.println("State " + name + " created=" + createdInput + " skipped by constraints=" + skippedInput);
	}

	/**
	 * Converts an array of {@link Constraint} to a predicate.
	 * 
	 * @param constraints
	 */
	private Predicate generateConstrainPredicate(Constraint[] constraints) {
		List<Predicate> constrainList = new ArrayList<Predicate>();
		for(Constraint c : constraints){
			if (c.isValidInState(this)) {
				constrainList.add(c);
			}
		}
		return new AndPredicate(constrainList.toArray(new Predicate[constrainList.size()]));
	}

	/**
	 * @return the _inputSpace
	 */
	public InputSpace getInputSpace() {
		return inputSpace;
	}

	/**
	 * @param obj
	 */
	public void addRule(Rule rule) {
		// TODO(rax): this was only added for testing purposes to avoid nondeterminisms
		//System.err.println("Rule priority has been reset! in State.addRule for testing purposes");
		//rule.setPriority(rules.size());
		rules.add(rule);
	}

	/**
	 * @param obj
	 * @return
	 */
	public boolean removeRule(Rule rule) {
		return rules.remove(rule);
	}

	/**
	 * @param index
	 */
	public void removeRuleAt(int index) {
		rules.remove(index);
	}

	/**
	 * @return
	 */
	public int ruleSize() {
		return rules.size();
	}

	public Rule ruleAt(int index) {
		return rules.get(index);
	}

	/**
	 * @param elem
	 * @return
	 * @see java.util.Vector#indexOf(java.lang.Object)
	 */
	public int indexOfRule(Object elem) {
		return rules.indexOf(elem);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof State)) {
			return false;
		}
		
		State s = (State)obj;
		return name.equals(s.name);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * @return the inStateAssumptions
	 */
	public Predicate getInStateAssumptions() {
		return inStateAssumptions;
	}

	/**
	 * @param inStateAssumptions the inStateAssumptions to set
	 */
	public void setInStateAssumptions(Predicate inStateAssumptions) {
		this.inStateAssumptions = inStateAssumptions;
	}

	/**
	 * @return the holdCondition
	 */
	public Predicate getHoldCondition() {
		return holdCondition;
	}

	/**
	 * @param holdCondition the holdCondition to set
	 */
	public void setHoldCondition(Predicate holdCondition) {
		this.holdCondition = holdCondition;
	}

}
