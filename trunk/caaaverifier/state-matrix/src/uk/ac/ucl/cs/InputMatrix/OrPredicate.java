/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix;

/**
 * @author rax
 *
 */
public class OrPredicate extends CompositePredicate {

	public OrPredicate(Predicate... predicates) {
		super(predicates);
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.InputMatrix.BooleanPredicate#getValue(int)
	 */
	public boolean getValue(int input) {
		for (Predicate p : predicates) {
			if (p.getValue(input)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected String getOperatorName() {
		return "Or";
	}
	
	/**
	 * Factory method for this {@link Predicate}
	 * 
	 * @param predicates the {@link Predicate} to be inserted.
	 * @return a predicate doing the congiunction of the original ones.
	 */
	public static Predicate or(Predicate... predicates) {
		return new OrPredicate(predicates);
	}
}
