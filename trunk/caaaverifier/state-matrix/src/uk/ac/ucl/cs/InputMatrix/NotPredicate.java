/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix;

/**
 * @author rax
 *
 */
public class NotPredicate implements Predicate {

	private Predicate innerPredicate;
	
	public NotPredicate(Predicate predicate) {
		innerPredicate = predicate;
	}

	public boolean getValue(int input) {
		return !innerPredicate.getValue(input);
	}

	public PredicateVariable[] getVariables() {
		return innerPredicate.getVariables();
	}

	@Override
	public String toString() {
		return "Not(" + innerPredicate.toString() + ")";
	}

	@Override
	public double getSatisfaction() {
		return 1 - innerPredicate.getSatisfaction();
	}
	
	/**
	 * Factory method for this {@link Predicate}
	 * 
	 * @param p the {@link Predicate} to be negated.
	 * @return a predicate negating the original one.
	 */
	public static Predicate not(Predicate p) {
		if (p == null) {
			throw new IllegalArgumentException("The predicate cannot be null!");
		}
		return new NotPredicate(p);
	}
}
