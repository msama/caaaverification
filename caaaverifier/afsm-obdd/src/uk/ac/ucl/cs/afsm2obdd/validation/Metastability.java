/**
 * 
 */
package uk.ac.ucl.cs.afsm2obdd.validation;

import net.sf.javabdd.BDD;

/**
 * @author -RAX- (Michele Sama)
 *
 */
public class Metastability implements Fault {

	private final BDD activation;
	private final String state;
	private final String rule;
	private final String futureState;
	
	/**
	 * @param activation
	 * @param state
	 * @param rule
	 * @param futureState
	 */
	public Metastability(BDD activation, String state, String rule,
			String futureState) {
		this.activation = activation;
		this.state = state;
		this.rule = rule;
		this.futureState = futureState;
	}

	/**
	 * @return the activation
	 */
	public BDD getActivation() {
		return activation;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @return the rule
	 */
	public String getRule() {
		return rule;
	}

	/**
	 * @return the futureState
	 */
	public String getFutureState() {
		return futureState;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Metastability[state='" + state + "' rule='" + rule + 
				"' futureState='" + futureState + "' activation='" + activation + "']";
	}

	


}
