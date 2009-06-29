/**
 * 
 */
package uk.ac.ucl.cs.afsm.pddl;

import static uk.ac.ucl.cs.afsm.pddl.Predicates.*;

/**
 * @author rax
 *
 */
public class State extends Variable {
	
	public static State S = new State("s_");
	
	private Predicate inStateCondition;
	
	/**
	 * 
	 */
	public State(String name) {
		super("state", name);
	}
	
	public Predicate isState(State variable) {
		return new IsState(getName(), variable);
	}
	
	public Predicate notState(State variable) {
		return not(isState(variable));
	}

	public static State any() {
		// TODO(rax): store this into a ghost reference
		return new AnyState();
	}
	
	static class IsState extends VariablePredicate {
		
		String name;
		State s;
		
		public IsState(String name, State variable) {
			this.name = name;
			this.s = variable;
		}

		@Override
		public String getUniqueName() {
			return "is-state-" + name;
		}
	}
	
	static class AnyState extends State {
		public AnyState() {
			super("*");
		}
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
		this.inStateCondition = and(isState(State.S), not(inStateCondition));
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.afsm.pddl.Variable#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof State)) {
			return false;
		}
		State s = (State) obj;
		if (this instanceof AnyState || s instanceof AnyState) {
			return true;
		}
		return getName().equals(s.getName());
	}

}
