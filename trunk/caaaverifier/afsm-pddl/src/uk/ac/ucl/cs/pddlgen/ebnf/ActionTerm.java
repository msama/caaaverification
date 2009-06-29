/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class ActionTerm extends Streamable {

	private ActionFunctor functor;
	private Term[] terms;
	
	/**
	 * @param functor
	 * @param terms
	 */
	private ActionTerm(ActionFunctor functor, Term[] terms) {
		this.functor = functor;
		this.terms = terms;
	}	
	
	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		writeInto(functor);
		writeInto(terms);
	}

	public static ActionTerm create(
			final ActionFunctor functor, final Term[] terms) {
		if (functor == null) {
			throw new IllegalArgumentException("Statement <action-term> " +
					"must contain a <action functor>.");
		}
		if (terms == null || terms.length < 1) {
			throw new IllegalArgumentException("Statement <action-term> " +
					"must contain a <term>.");
		}
		return new ActionTerm(functor, terms);
	}
}
