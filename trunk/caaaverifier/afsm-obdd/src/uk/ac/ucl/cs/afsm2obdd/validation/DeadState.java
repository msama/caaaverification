/**
 * 
 */
package uk.ac.ucl.cs.afsm2obdd.validation;

/**
 * @author -RAX- (Michele Sama)
 *
 */
public class DeadState implements Fault {

	private String state;

	/**
	 * @param state
	 */
	public DeadState(String state) {
		this.state = state;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DeadState[state='" + state + "']";
	}

}
