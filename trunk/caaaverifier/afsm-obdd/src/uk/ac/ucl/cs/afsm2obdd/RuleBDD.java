/**
 * 
 */
package uk.ac.ucl.cs.afsm2obdd;

import java.util.ArrayList;
import java.util.List;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDVarSet;

import uk.ac.ucl.cs.afsm.common.Assignment;
import uk.ac.ucl.cs.afsm.common.Rule;

/**
 * @author -RAX- (Michele Sama)
 *
 */
public class RuleBDD {

	private final String name;
	private final BDD encoding;
	private BDDVarSet encodingVarSet;
	
	private BDD trigger;
	private BDDVarSet triggerVarSet;
	private BDD action;
	private StateBDD destination;
	private int priority = Rule.DEFAULT_PRIORITY;
	
	/**
	 * @param name
	 * @param encoding
	 */
	public RuleBDD(String name, BDD encoding) {
		if (name == null) {
			throw new IllegalArgumentException("Name cannot be null.");
		}
		if (encoding == null) {
			throw new IllegalArgumentException("Encoding cannot be null.");
		}
		this.name = name;
		this.encoding = encoding;
		encodingVarSet = this.encoding.toVarSet();
	}

	/**
	 * @return the trigger
	 */
	public BDD getTrigger() {
		return trigger;
	}
	/**
	 * @param trigger the trigger to set
	 */
	public void setTrigger(BDD trigger) {
		this.trigger = trigger;
		triggerVarSet = this.trigger.toVarSet();
	}
	/**
	 * @return the action
	 */
	public BDD getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(BDD action) {
		this.action = action;
	}
	/**
	 * @return the destination
	 */
	public StateBDD getDestination() {
		return destination;
	}
	/**
	 * @param destination the destination to set
	 */
	public void setDestination(StateBDD destination) {
		this.destination = destination;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the encoding
	 */
	public BDD getEncoding() {
		return encoding;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return the encodingVarSet
	 */
	public BDDVarSet getEncodingVarSet() {
		return encodingVarSet;
	}

	/**
	 * @return the triggerVarSet
	 */
	public BDDVarSet getTriggerVarSet() {
		return triggerVarSet;
	}
}
