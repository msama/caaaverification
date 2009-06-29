/**
 * 
 */
package uk.ac.ucl.cs.afsm.pddl;

/**
 * @author rax
 *
 */
public abstract class Rule extends Action {

	protected State originalState;
	
	protected State destinationState;

	/**
	 * @param originalState
	 * @param destinationState
	 */
	public Rule(String name, State originalState, State destinationState) {
		super(name);
		this.originalState = originalState;
		this.destinationState = destinationState;
	}

	/**
	 * @param precontion
	 * @param effect
	 */
	public Rule(State origin, State destination, Predicate precontion, Predicate effect) {
		super(precontion, effect);
	}

	/**
	 * @return the originalState
	 */
	public State getOriginalState() {
		return originalState;
	}

	/**
	 * @return the destinationState
	 */
	public State getDestinationState() {
		return destinationState;
	}
	
}
