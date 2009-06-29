/**
 * 
 */
package uk.ac.ucl.cs.afsm.pddl;

import static uk.ac.ucl.cs.afsm.pddl.Predicates.*;

import java.util.*;

/**
 * Defines an Adaptation Finite State Machine, or Adaptation Automaton.
 * 
 * <p>This class represent a state machine.
 * 
 * @author rax
 *
 */
public class Afsm implements Visitable{
	
	public Afsm(String name) {
		super();
		this.name = name;
	}

	protected String name;
	
	public List<State> states = new ArrayList<State>();
	
	public List<Rule> rules = new ArrayList<Rule>();
	
	public List<Event> events = new ArrayList<Event>();
	
	// Specifies the initial close world assumptions.
	public List<VariablePredicate> initialAssumptions = new ArrayList<VariablePredicate>();
	public List<Variable> initialObjects = new ArrayList<Variable>();
	
	@Override
	public void accept(AfsmVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public Set<String> getAllUsedVaribleTypes() {
		Set<String> types = new HashSet<String>();
		addVariableTypes(events, types);
		addVariableTypes(rules, types);
		return types;
	}
	
	private void addVariableTypes(List<? extends Action> actions, Set<String> to) {
		for (Action action : actions) {
			to.addAll(action.getVariableTypes());
		}
	}
	
	public List<VariablePredicate> getAllUsedVariablePredicates() {
		List<VariablePredicate> predicates = new ArrayList<VariablePredicate>();
		addPredicate(events, predicates);
		addPredicate(rules, predicates);
		return predicates;
	}
	
	private void addPredicate(List<? extends Action> actions, List<VariablePredicate> to) {
		for (Action action : actions) {
			addPredicate(action.getPrecontion(), to);
			addPredicate(action.getEffect(), to);
		}
	}
	
	private void addPredicate(Predicate predicate, List<VariablePredicate> to) {
		if (predicate instanceof VariablePredicate) {
			VariablePredicate variablePredicate = (VariablePredicate) predicate;
			if (!to.contains(variablePredicate)) {
				to.add(variablePredicate);
			}
		} else if (predicate instanceof CompositePredicate) {
			CompositePredicate composite = (CompositePredicate)predicate;
			List<Predicate> predicates = composite.getPredicates();
			for (Predicate pred: predicates) {
				// Recursive!
				addPredicate(pred, to);
			}
		} else {
			throw new IllegalStateException("No other type of Predicate has be defined.");
		}
	}
	
	/**
	 * Wraps rule's predicates with state definitions.
	 * 
	 * TODO(rax): find a way to roll back this change and perform it multiple times.
	 */
	public void applyStatesToRulePredicates() {		
		for(Rule r : rules) {
			if (!(r.getOriginalState() instanceof State.AnyState)) {
				r.precontion = and(r.getOriginalState().isState(State.S), not(r.getDestinationState().isState(State.S)), r.precontion);
				r.effect = and(r.getOriginalState().notState(State.S), r.getDestinationState().isState(State.S), r.effect);
			} else {
				// if the rule can start from any state
				r.precontion = and(not(r.getDestinationState().isState(State.S)), r.precontion);
				
				List<Predicate> statePredicates = new ArrayList<Predicate>();
				for (State s : states) {
					if (s.getName().equals(r.destinationState.getName())) {
						continue;
					}
					statePredicates.add(s.notState(State.S));
				}
				
				r.effect = and(and(statePredicates), r.getDestinationState().isState(State.S), r.effect);
			}
		}
	}
	
	/**
	 * Excludes events from being activated when the machine is in a certain state.
	 * Also add priority to rules rather than events.
	 * 
	 * TODO(rax): find a way to roll back this change and perform it multiple times.
	 */
	public void applyStatesToEventPredicates() {
		for(Event e : events) {
			if (e.exclusionList.isEmpty()) {
				//e.precontion = and(not(or(getRulesPrecondition())), e.precontion);
				continue;
			} else {
				List<Predicate> predicates = new ArrayList<Predicate>();
				for (State s: e.exclusionList) {
					predicates.add(s.isState(State.S));
				}
				e.precontion = and(not(or(predicates)), e.precontion);
				//e.precontion = and(not(or(predicates)), not(or(getRulesPrecondition())), e.precontion);
			}
		}
	}
	
	private Predicate[] getRulesPrecondition() {
		Predicate[] preds = new Predicate[rules.size()];
		for (int i = 0; i < rules.size(); i++) {
			preds[i] = rules.get(i).getPrecontion();
		} 
		return preds;
	}
 }
