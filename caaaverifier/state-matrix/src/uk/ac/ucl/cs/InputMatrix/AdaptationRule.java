/**
 * This object contains a Rule which leads to an adaptation. It also contains references to the new state.
 * 
 */
package uk.ac.ucl.cs.InputMatrix;

/**
 * @author rax
 *
 */
public class AdaptationRule extends Rule {

	private State _adaptationNextState;
	
	public AdaptationRule(String _name, State nextState) {
		super(_name);
		this._adaptationNextState=nextState;
	}

	/**
	 * @return the _adaptationNextState
	 */
	public State getAdaptationNextState() {
		return _adaptationNextState;
	}

}
