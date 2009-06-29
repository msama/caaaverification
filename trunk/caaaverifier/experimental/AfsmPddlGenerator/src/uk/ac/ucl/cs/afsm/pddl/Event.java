/**
 * 
 */
package uk.ac.ucl.cs.afsm.pddl;

import java.util.*;

/**
 * Represent an Event which is an action occurring because of a change in the
 * environment or because of some direct user interaction.
 * 
 * @author rax
 *
 */
public abstract class Event extends Action {

	public List<State> exclusionList = new ArrayList<State>();
	
	/**
	 * 
	 */
	public Event(String name) {
		super(name);
	}

	/**
	 * @param precontion
	 * @param effect
	 */
	public Event(Predicate precontion, Predicate effect) {
		super(precontion, effect);
	}

}
