/**
 * 
 */
package uk.ac.ucl.cs.afsm2obdd.validation;

/**
 * @author -RAX- (Michele Sama)
 *
 */
public class UnreachableState implements Fault {

	private String state;

	/**
	 * @param state
	 */
	public UnreachableState(String state) {
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
		return "UnreachableState[state='" + state + "']";
	}

}
