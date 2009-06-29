/**
 *
 */
package uk.ac.ucl.cs.InputMatrix;

/**
 * Define an implication IF-CAUSE-THEN-EFFECT where "cause" and "effect" are 
 * {@link PredicateVariable}s in the boolean predicate space defined for this {@link AdaptationFSM}. 
 * 
 * <p>Given a certain {@link State} of the {@link AdaptationFSM}, if at least a {@link PredicateVariable}s 
 * in this {@link Constraint} exit in the {@link State} then the constraint is applied.
 * 
 * @author rax 
 *
 */
public class Constraint extends CompositePredicate {
	
	/**
	 * Creates a logic implication in the form (!{@code condition} | {@code effect}), which means that the constraint
	 * is false only if the cause is true and the effect is false
	 * 
	 * @param condition
	 * @param effect
	 */
	public Constraint(Predicate condition, Predicate effect) {
		super(new OrPredicate(new NotPredicate(condition), effect));
	}

	/**
	 * Tells if at least one predicate of the current {@link Constraint} 
	 * exists in the given {@link State} {@code s}.
	 * 
	 * @param s the given {@link State}
	 * @return <code>true</code> is at least a {@link PredicateVariable} used to 
	 * 	define this {@link Constraint} exist in {@code s}, <code>false</code> 
	 * 	otherwise.
	 */
	public boolean isValidInState(State s) {
		PredicateVariable[] stateVar = s.getVariables();
		
		for(PredicateVariable v : getVariables())
		{
			for(PredicateVariable vs : stateVar)
			{
				if(v.equals(vs))
				{
					return true;
				}
			}
		}
	
		return false;
	}

	@Override
	public boolean getValue(int input) {
		return predicates.get(0).getValue(input);
	}

	@Override
	protected String getOperatorName() {
		return "Constraint";
	}
}
