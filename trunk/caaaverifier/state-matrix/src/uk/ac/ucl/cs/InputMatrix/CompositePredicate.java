/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author rax
 *
 */
public abstract class CompositePredicate implements Predicate {

	protected List<Predicate> predicates = new ArrayList<Predicate>();

	public CompositePredicate(Predicate... predicates) {
		for (Predicate p : predicates) {
			this.predicates.add(p);
		}
	}

	protected abstract String getOperatorName();
	
	public PredicateVariable[] getVariables() {
		Set<PredicateVariable> variables = new HashSet<PredicateVariable> ();
		for (Predicate p : predicates) {
			PredicateVariable v[] = p.getVariables();
			for(PredicateVariable var : v)
			{
				variables.add(var);
			}
		}
		return variables.toArray(new PredicateVariable[variables.size()]);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getOperatorName());
		builder.append("(");
		for (int i = 0; i < predicates.size(); i++) {
			builder.append(predicates.get(i));
			if (i < predicates.size() - 1) {
				builder.append(',');
			}
		}
		builder.append(")");
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.InputMatrix.Predicate#getSatisfaction()
	 */
	@Override
	public double getSatisfaction() {
		int size = getVariables().length;
		double max = Math.pow(2, size);
		double value = 0;
		for (int i = 0; i < max; i++) {
			if (getValue(i)) {
				value ++;
			}
		}
		return value / max;
	}
}
