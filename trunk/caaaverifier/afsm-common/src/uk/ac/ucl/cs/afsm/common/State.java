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
 * Defines a behaviour of the {@link AdaptationFiniteStateMachine} as a
 * {@link State} in which the automaton can operate.
 * 
 * @author -RAX- (Michele Sama)
 *
 */
public class State {

	private String name = "";
	private boolean initial;
	private boolean end;
	
	private Predicate inStateCondition = Constant.FALSE;
	private Predicate inStateAssumption = Constant.TRUE;
	public final List<Rule> outGoingRules = new ArrayList<Rule>();
	
	/**
	 * 
	 */
	public State() {
	}
	
	/**
	 * @param name
	 * @param inStateCondition
	 * @param outGoingRules
	 */
	public State(String name) {
		this();
		this.name = name;
	}
	
	/**
	 * @param name
	 * @param inStateCondition
	 * @param outGoingRules
	 */
	public State(String name, Predicate inStateCondition,
			Collection<Rule> outGoingRules) {
		this(name);
		this.inStateCondition = inStateCondition;
		this.outGoingRules.addAll(outGoingRules);
	}
	
	/**
	 * @param name
	 * @param initial
	 * @param end
	 * @param inStateCondition
	 * @param outGoingRules
	 */
	public State(String name, boolean initial, boolean end,
			Predicate inStateCondition, List<Rule> outGoingRules) {
		this(name, inStateCondition, outGoingRules);
		this.initial = initial;
		this.end = end;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return
	 */
	public boolean isInitial() {
		return initial;
	}
	
	/**
	 * @param initial
	 */
	public void setInitial(boolean initial) {
		this.initial = initial;
	}
	
	/**
	 * @return
	 */
	public boolean isEnd() {
		return end;
	}
	
	/**
	 * @param end
	 */
	public void setEnd(boolean end) {
		this.end = end;
	}

	/**
	 * State if two instances of {@link State} identifies the same state
	 * in the {@link AdaptationFiniteStateMachine}. {@link State}s are
	 * identified by name.
	 * 
	 * @param obj the instance with which this instance should be compared.
	 * @return <code>true</code> if <code>obj</code> is a state and neither
	 * 	its name or the name of the current instance are <code>null</code>,
	 * 	<code>false</code> otherwise.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof State)) {
			return false;
		}
		State s = (State) obj;
		if (name == null || s.name == null) {
			return false;
		}
		return name.equals(s.name);
	}

	/**
	 * @return the inStateCondition
	 */
	public Predicate getInStateCondition() {
		return inStateCondition;
	}

	/**
	 * @param inStateCondition the inStateCondition to set
	 */
	public void setInStateCondition(Predicate inStateCondition) {
		this.inStateCondition = inStateCondition;
	}

	/**
	 * @return the inStateAssumption
	 */
	public Predicate getInStateAssumption() {
		return inStateAssumption;
	}

	/**
	 * @param inStateAssumption the inStateAssumption to set
	 */
	public void setInStateAssumption(Predicate inStateAssumption) {
		this.inStateAssumption = inStateAssumption;
	}

}
