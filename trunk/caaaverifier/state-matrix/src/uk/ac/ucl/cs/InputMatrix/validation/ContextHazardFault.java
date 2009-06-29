/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix.validation;

import uk.ac.ucl.cs.InputMatrix.Fault;
import uk.ac.ucl.cs.InputMatrix.Rule;
import uk.ac.ucl.cs.InputMatrix.State;
import uk.ac.ucl.cs.InputMatrix.PredicateVariable;
import uk.ac.ucl.cs.InputMatrix.ContextVariable;

import java.util.Vector;

/**
 * @author rax
 *
 */
public class ContextHazardFault extends Fault {

	public static final String CONTEXT_STABILITY_HAZARD="ContextStabilityHazard";
	public static final String CONTEXT_ACTIVATION_HAZARD="ContextActivationHazard";
	public static final String CONTEXT_PRIORITIZED_HAZARD="ContextPrioritizedHazard";
	
	private State _state;
	protected PredicateVariable[] _criticalPath;
	private Rule[] _rules;
	
	
	/**
	 * @param _input
	 * @param _name
	 * @param _state
	 * @param path
	 * @param _rules
	 */
	public ContextHazardFault(String _name, String _input,  State _state,
			PredicateVariable[] path, Rule[] _rules) {
		super(_input, _name);
		this._state = _state;
		_criticalPath = path;
		this._rules = _rules;
	}

	/**
	 * @return the _state
	 */
	public State getState() {
		return _state;
	}


	/**
	 * @return the _criticalPath
	 */
	public PredicateVariable[] getCriticalPath() {
		return _criticalPath;
	}


	/**
	 * @return the _rules
	 */
	public Rule[] getRules() {
		return _rules;
	}






	


}
