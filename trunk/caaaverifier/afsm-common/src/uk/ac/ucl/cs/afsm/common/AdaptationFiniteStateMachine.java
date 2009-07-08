/**
 * 
 */
package uk.ac.ucl.cs.afsm.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import uk.ac.ucl.cs.afsm.common.predicate.Predicate;
import uk.ac.ucl.cs.afsm.common.predicate.Variable;

/**
 * Defines an Adaptation Finite State Machine (AFSM)
 * 
 * @author -RAX- (Michele Sama)
 *
 */
public class AdaptationFiniteStateMachine {

	public AdaptationFiniteStateMachine(String name) {
		super();
		this.name = name;
	}
	
	public final Set<Context> contexts = new HashSet<Context>();

	/**
	 * States defined in the {@link AdaptationFiniteStateMachine}.
	 */
	public final ArrayList<State> states = new ArrayList<State>();
	
	/**
	 * Collects all the {@link Variable}s defined in this
	 * {@link AdaptationFiniteStateMachine}.
	 * 
	 * <p>{@link State}s and {@link Rule}s use the same variable in their
	 * {@link Predicate}s. Having a collection of all the defined
	 * {@link Variable}s is useful to convert the state machine in other forms
	 * and to avoid duplicated definitions of the same {@link Variable}.
	 */
	public final Map<String, Variable> variables = new HashMap<String, Variable>();
	
	/**
	 * Collects all the {@link Rule}s defined in this
	 * {@link AdaptationFiniteStateMachine}.
	 */
	public final ArrayList<Rule> rules = new ArrayList<Rule>();
	
	private State initialState;

	private final String name;
	
	public Context context(String name) {
		Context c = new Context(name);
		contexts.add(c);
		return c;
	}
	
	public Variable variable(String name, Context context) {
		if (variables.containsKey(name)) {
			throw new IllegalStateException("A variable with id " + name +
					" has already been declared in this AFSM.");
		}
		Variable v = new Variable(name, context);
		variables.put(name, v);
		return v;
	}
	
	public Rule rule(String name, Predicate trigger, State destination, Assignment... actions) {
		for (Rule rl : rules) {
			if (rl.getName().equals(name)) {
				throw new IllegalStateException("A rule with id " + name +
						" has already been declared in this AFSM.");
			}
		}
		Rule r = new Rule(name, trigger, destination, actions);
		rules.add(r);
		return r;
	}
	
	public State state(String name, boolean initial, boolean end) {
		for (State st : states) {
			if (st.getName().equals(name)) {
				throw new IllegalStateException("A state with id " + name +
						" has already been declared in this AFSM.");
			}
		}
		if (initialState != null && initial) {
			throw new IllegalStateException("An intial state" +
			" has already been declared in this AFSM: " +
			initialState);
		}
		State s = new State(name);
		if (initial) {
			initialState = s;
		}
		s.setInitial(initial);
		s.setEnd(end);
		states.add(s);
		return s;
	}

	/**
	 * @return the initialState
	 */
	public State getInitialState() {
		return initialState;
	}

	public String getName() {
		return name;
	}
}
