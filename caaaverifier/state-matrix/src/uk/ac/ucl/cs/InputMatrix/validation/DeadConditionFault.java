/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix.validation;

import uk.ac.ucl.cs.InputMatrix.Fault;
import uk.ac.ucl.cs.InputMatrix.Rule;
import uk.ac.ucl.cs.InputMatrix.State;

/**
 * @author michsama
 *
 */
public class DeadConditionFault extends Fault {

	public final static String DEAD_CONDITION_FAULT="DeadConditionFault";
	protected State _state=null;
	protected Rule _rule=null;
	
	public DeadConditionFault(String _input, State _state, Rule _rule) {
		super(_input, DeadConditionFault.DEAD_CONDITION_FAULT);
		this._state = _state;
		this._rule = _rule;
	}

	/**
	 * @return the _state
	 */
	public State getState() {
		return _state;
	}

	/**
	 * @return the _rule
	 */
	public Rule getRule() {
		return _rule;
	}
	


}
