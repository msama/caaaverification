/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix;

/**
 * @author rax
 *
 */
public class AndPredicate extends CompositePredicate {

	public AndPredicate(Predicate... predicates) {
		super(predicates);
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.InputMatrix.BooleanPredicate#getValue(int)
	 */
	public boolean getValue(int input) {
		for (Predicate p : predicates) {
			if (!p.getValue(input)) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected String getOperatorName() {
		return "And";
	}
	
	/**
	 * Factory method for this {@link Predicate}
	 * 
	 * @param predicates the {@link Predicate} to be inserted.
	 * @return a predicate doing the disgiunction of the original ones.
	 */
	public static Predicate and(Predicate... predicates) {
		return new AndPredicate(predicates);
	}
}
