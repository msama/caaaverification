/**
 * 
 */
package uk.ac.ucl.cs.afsm.pddl;

import static uk.ac.ucl.cs.afsm.pddl.Predicates.not;

/**
 * @author rax
 *
 */
public abstract class BooleanVariable extends Variable {

	private Predicate isTrue;
	private Predicate isFalse;
	
	private Event setTrue;
	private Event setFalse;
	
	/**
	 * @param name
	 */
	public BooleanVariable(String type, String name) {
		super(type, name);
	}

	public Predicate isTrue() {
		// Lazy initializer
		if (isTrue == null) {
			isTrue = new IsTrue(this);
		}
		return isTrue;
	}
	
	public Predicate isFalse() {
		// Lazy initializer
		if (isFalse == null) {
			isFalse = not(isTrue());
		}
		return isFalse;
	}
	
	public Event setTrue() {
		// Lazy initializer
		if (setTrue == null) {
			setTrue = new EventSetTrue(this);
		}
		return setTrue;
	}
	
	public Event setFalse() {
		// Lazy initializer
		if (setFalse == null) {
			setFalse = new EventSetFalse(this);
		}
		return setFalse;
	}
	
	static class IsTrue extends VariablePredicate {
		
		BooleanVariable b;
		
		public IsTrue(BooleanVariable b) {
			this.b = b;
		}

		@Override
		public String getUniqueName() {
			return "is-true-" + b.getType();
		}
	}
	
	static class EventSetTrue extends Event {
		public EventSetTrue(BooleanVariable v) {
			super("Event_enable_" + v.getName());
			precontion = v.isFalse();
			effect = v.isTrue();
		}	
	}
	
	static class EventSetFalse extends Event {
		public EventSetFalse(BooleanVariable v) {
			super("Event_disable_" + v.getName());
			precontion = v.isTrue();
			effect = v.isFalse();
		}	
	}
}
