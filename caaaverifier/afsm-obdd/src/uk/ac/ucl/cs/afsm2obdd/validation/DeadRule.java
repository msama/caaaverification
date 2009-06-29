/**
 * 
 */
package uk.ac.ucl.cs.afsm2obdd.validation;

/**
 * @author -RAX- (Michele Sama)
 *
 */
public class DeadRule implements Fault {

	private String state;
	private String rule;
	
	/**
	 * @param state
	 * @param rule
	 */
	public DeadRule(String state, String rule) {
		this.state = state;
		this.rule = rule;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DeadRule[state='" + state + "' rule='" + rule + "']";
	}
}
