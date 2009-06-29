/**
 * 
 */
package uk.ac.ucl.cs.afsm2obdd.validation;

import java.util.Arrays;

import uk.ac.ucl.cs.afsm2obdd.RuleBDD;
import uk.ac.ucl.cs.afsm2obdd.StateBDD;
import net.sf.javabdd.BDD;

/**
 * @author -RAX- (Michele Sama)
 *
 */
public class NondeterministicActivation implements Fault {
	
	protected String state;
	protected String[] rules;
	protected BDD overlap;
	
	/**
	 * @param state
	 * @param rules
	 * @param overlap
	 */
	public NondeterministicActivation(String state, String[] rules, BDD overlap) {
		this.state = state;
		this.rules = rules;
		this.overlap = overlap;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @return the rules
	 */
	public String[] getRules() {
		return rules;
	}
	/**
	 * @return the overlap
	 */
	public BDD getOverlap() {
		return overlap;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NondeterministicActivation[state='" + state +
				"' rules='" + Arrays.toString(rules) + "' BDD='" + overlap + "']";
	}
}
