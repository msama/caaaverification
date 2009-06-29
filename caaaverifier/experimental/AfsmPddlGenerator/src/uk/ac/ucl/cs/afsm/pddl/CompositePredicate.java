/**
 * 
 */
package uk.ac.ucl.cs.afsm.pddl;

import java.util.*;

/**
 * @author rax
 *
 */
public abstract class CompositePredicate implements Predicate {

	protected List<Predicate> predicates = new ArrayList<Predicate>();
	
	/**
	 * 
	 */
	public CompositePredicate(Predicate... predicates) {
		for (Predicate pred : predicates) {
			this.predicates.add(pred);
		}
	}
	
	/**
	 * 
	 */
	public CompositePredicate(List<Predicate> predicates) {
		this.predicates.addAll(predicates);
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.afsm.pddl.Predicate#accept(uk.ac.ucl.cs.afsm.pddl.AfsmVisitor)
	 */
	@Override
	public void accept(AfsmVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Map<String, Variable> getVariables() {
		Map<String, Variable> variables = new HashMap<String, Variable>();
		for (Predicate pred : predicates) {
			Map<String, Variable> vars = pred.getVariables();
			for (String name : vars.keySet()) {
				if (variables.containsKey(name) && 
						!variables.get(name).getType().equals(vars.get(name).getType())) {
					throw new IllegalArgumentException(
							"Variables with the same name should be of the same type: " +
							variables.get(name) + " " + vars.get(name));
				}
				variables.put(name, vars.get(name));
			}
		}
		return variables;
	}

	/**
	 * @return the predicates
	 */
	public List<Predicate> getPredicates() {
		return predicates;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CompositePredicate)) {
			return false;
		} else {
			CompositePredicate p = (CompositePredicate) obj;
			return getClass().equals(p.getClass()) && predicates.equals(p.predicates);
		}
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.afsm.pddl.Predicate#getVariableTypes()
	 */
	@Override
	public Set<String> getVariableTypes() {
		Set<String> types = new HashSet<String>();
		for (Predicate p : predicates) {
			// TODO(rax): do a type check for singularity
			types.addAll(p.getVariableTypes());
		}
		return types;
	}
}
