/**
 * 
 */
package uk.ac.ucl.cs.afsm.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.ac.ucl.cs.afsm.common.predicate.Constant;
import uk.ac.ucl.cs.afsm.common.predicate.Predicate;

/**
 * Defines an adaptation rule which from a set of current states in which
 * the {@link Rule} is active, if a triggering predicate is satisfied, adapts
 * the state machine to the destination state and apply a certain action.
 * 
 * @author -RAX- (Michele Sama)
 *
 */
public class Rule {

	public static final int DEFAULT_PRIORITY = 5;
	public static final int MAX_PRIORITY = 0;
	public static final int LOW_PRIORITY = 9;
	
	private String name;
	private Predicate trigger = Constant.FALSE;
	public final List<Assignment> action = new ArrayList<Assignment>();
	private State destination;
	private int priority = DEFAULT_PRIORITY;
	
	/**
	 * 
	 */
	public Rule() {
	}

	/**
	 * @param name
	 */
	public Rule(String name) {
		this();
		this.name = name;
	}
	
	/**
	 * @param trigger
	 * @param action
	 * @param destination
	 */
	public Rule(String name, Predicate trigger, State destination, Collection<Assignment> action) {
		this(name);
		this.trigger = trigger;
		this.destination = destination;
		this.action.addAll(action);
	}
	
	/**
	 * @param trigger
	 * @param action
	 * @param destination
	 */
	public Rule(String name, Predicate trigger, State destination, Assignment... actions) {
		this(name);
		this.trigger = trigger;
		this.destination = destination;
		for (Assignment a : actions) {
			action.add(a);
		}
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the trigger
	 */
	public Predicate getTrigger() {
		return trigger;
	}
	
	/**
	 * @param triggger the trigger to set
	 */
	public void setTrigger(Predicate trigger) {
		this.trigger = trigger;
	}

	/**
	 * @return the destination
	 */
	public State getDestination() {
		return destination;
	}
	
	/**
	 * @param destination the destination to set
	 */
	public void setDestination(State destination) {
		this.destination = destination;
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
	 * State if two instances of {@link Rule} identifies the same rule
	 * by comparing their names.
	 * 
	 * @param obj the instance with which this instance should be compared.
	 * @return <code>true</code> if <code>obj</code> is a {@link Rule} and neither
	 * 	its name or the name of the current instance are <code>null</code>,
	 * 	<code>false</code> otherwise.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Rule)) {
			return false;
		}
		Rule r = (Rule) obj;
		if (name == null || r.name == null) {
			return false;
		}
		return name.equals(r.name);
	}

	
}
