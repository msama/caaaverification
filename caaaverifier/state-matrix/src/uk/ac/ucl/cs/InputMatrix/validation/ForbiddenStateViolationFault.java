/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix.validation;

import uk.ac.ucl.cs.InputMatrix.Fault;
import uk.ac.ucl.cs.InputMatrix.Rule;
import uk.ac.ucl.cs.InputMatrix.State;

/**
 * @author rax
 *
 */
public class ForbiddenStateViolationFault extends Fault {

	private State _state;
	private State _forbiddenState;
	private Rule _rule;
	
	public ForbiddenStateViolationFault(String _input, State _state, State _forbidden, Rule _rule) {
		super(_input, "ForbiddenStateViolationFault");
		this._state=_state;
		this._forbiddenState=_forbidden;
		this._rule=_rule;
	}

	@Override
	public String toString() {
		return this.getName()+"of state "+this._forbiddenState+" from "+this._state+" by rule "+this._rule+" with input "+this.getInput();
	}

	/**
	 * @return the _state
	 */
	public State getState() {
		return _state;
	}

	/**
	 * @return the _forbiddenState
	 */
	public State getForbiddenState() {
		return _forbiddenState;
	}

	/**
	 * @return the _rule
	 */
	public Rule getRule() {
		return _rule;
	}




}
