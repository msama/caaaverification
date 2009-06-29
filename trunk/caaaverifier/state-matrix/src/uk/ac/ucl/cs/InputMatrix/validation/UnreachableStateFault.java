/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix.validation;

import uk.ac.ucl.cs.InputMatrix.Fault;
import uk.ac.ucl.cs.InputMatrix.State;

/**
 * @author -RAX- (Michele Sama)
 *
 */
public class UnreachableStateFault extends Fault {

	private State state;
	
	/**
	 * @param _input
	 * @param _name
	 */
	public UnreachableStateFault(String input, State state) {
		super(input, "Unreachable state");
		this.state = state;
	}

	/**
	 * @return the state
	 */
	public State getState() {
		return state;
	}

}
