/**
 * 
 */
package uk.ac.ucl.cs.afsm.pddl;

import java.util.*;
import static uk.ac.ucl.cs.afsm.pddl.Predicates.and;

/**
 * @author rax
 *
 */
public abstract class Action implements Visitable {
	
	protected Predicate precontion;
	protected Predicate effect;
	
	protected String name;
	
	/**
	 * 
	 */
	public Action(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 */
	public Action(Predicate precontion, Predicate effect) {
		this.precontion = precontion;
		this.effect = effect;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.afsm.pddl.Visitable#accept(uk.ac.ucl.cs.afsm.pddl.AfsmVisitor)
	 */
	@Override
	public void accept(AfsmVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * @return the precontion
	 */
	public Predicate getPrecontion() {
		return precontion;
	}

	/**
	 * @return the effect
	 */
	public Predicate getEffect() {
		return effect;
	}
	
	public Map<String, Variable> getVariables() {
		Map<String, Variable> variables = new HashMap<String, Variable>();
		// TODO(rax): do a type check on variables with the same name.
		variables.putAll(precontion.getVariables());
		variables.putAll(effect.getVariables());
		return variables;
	}
	
	public Set<String> getVariableTypes() {
		Set<String> types = new HashSet<String>();
		// TODO(rax): do a type check on variables with the same name.
		types.addAll(precontion.getVariableTypes());
		types.addAll(effect.getVariableTypes());
		return types;
	}

	/**
	 * @param precontion the precontion to set
	 */
	public void andPrecontion(Predicate pred) {
		this.precontion = and(pred, precontion);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
