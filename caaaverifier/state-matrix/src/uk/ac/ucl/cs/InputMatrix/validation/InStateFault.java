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
public class InStateFault extends Fault {

	private State _state;
	private Rule[] _nondeterministicRules;
	
	/**
	 * @param _input
	 * @param _name
	 */
	public InStateFault(String _input, State _state) {
		super(_input, "NondeterministicActivation");
		this._state=_state;
	}

	/**
	 * @param _nondeterministicRules the _nondeterministicRules to set
	 */
	public void setNondeterministicRules(Rule[] _nondeterministicRules) {
		this._nondeterministicRules = _nondeterministicRules;
	}

	/**
	 * @return the _nondeterministicRules
	 */
	public Rule[] getNondeterministicRules() {
		return _nondeterministicRules;
	}

	/**
	 * @return the _state
	 */
	public State getState() {
		return _state;
	}

	@Override
	public String toString() {
		return this.getName()+" detected in state "+this._state.getName();
	}
}
