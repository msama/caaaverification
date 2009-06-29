/**
 * 
 */
package uk.ac.ucl.cs.afsm.pddl;

import java.util.ArrayList;
import java.util.List;

/**
 * Static factory for the creation of common predicates.
 * 
 * @author rax
 *
 */
public class Predicates {

	/**
	 * Does not allows the creation of object of this 
	 * <code>class</code> because it is just a static factory.
	 */
	private Predicates() {
		// Uninstantiable.
	}

	public static Predicate and(Predicate... predicates) {
		List<Predicate> predicatesToAdd = new ArrayList<Predicate>();
		for (Predicate pred : predicates) {
			if (!(pred instanceof And)) {
				predicatesToAdd.add(pred);
			} else {
				And and = (And) pred;
				predicatesToAdd.addAll(and.predicates);
			}
		}
		return new And(predicatesToAdd);
	}
	
	public static Predicate or(Predicate... predicates) {
		List<Predicate> predicatesToAdd = new ArrayList<Predicate>();
		for (Predicate pred : predicates) {
			if (!(pred instanceof Or)) {
				predicatesToAdd.add(pred);
			} else {
				Or and = (Or) pred;
				predicatesToAdd.addAll(and.predicates);
			}
		}
		return new Or(predicatesToAdd);
	}
	
	public static Predicate not(Predicate predicate) {
		if (predicate instanceof Not) {
			return ((Not) predicate).predicates.get(0);
		}
		return new Not(predicate);
	}
	
	public static Predicate and(List<Predicate> predicates) {
		List<Predicate> predicatesToAdd = new ArrayList<Predicate>();
		for (Predicate pred : predicates) {
			if (!(pred instanceof And)) {
				predicatesToAdd.add(pred);
			} else {
				And and = (And) pred;
				predicatesToAdd.addAll(and.predicates);
			}
		}
		return new And(predicatesToAdd);
	}
	
	public static Predicate or(List<Predicate> predicates) {
		List<Predicate> predicatesToAdd = new ArrayList<Predicate>();
		for (Predicate pred : predicates) {
			if (!(pred instanceof Or)) {
				predicatesToAdd.add(pred);
			} else {
				Or and = (Or) pred;
				predicatesToAdd.addAll(and.predicates);
			}
		}
		return new Or(predicatesToAdd);
	}

}

class And extends CompositePredicate {

	public And(Predicate... predicates) {
		super(predicates);
	}
	
	public And(List<Predicate> predicates) {
		super(predicates);
	}

	@Override
	public String getUniqueName() {
		return "and";
	}
	
}

class Or extends CompositePredicate {

	public Or(Predicate... predicates) {
		super(predicates);
	}
	
	public Or(List<Predicate> predicates) {
		super(predicates);
	}

	@Override
	public String getUniqueName() {
		return "or";
	}
	
}

class Not extends CompositePredicate {

	public Not(Predicate predicate) {
		super(predicate);
	}
	
	@Override
	public String getUniqueName() {
		return "not";
	}
	
}
