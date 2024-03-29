/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class ActionTerm extends Streamable {

	private ActionSymbol functor;
	private Term[] terms;
	
	/**
	 * @param functor
	 * @param terms
	 */
	private ActionTerm(ActionSymbol functor, Term[] terms) {
		this.functor = functor;
		this.terms = terms;
	}	
	
	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		writeInto(pw, functor);
		writeSpaceSeparatedList(terms);
	}

	public static ActionTerm create(
			final ActionSymbol functor, final Term[] terms) {
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
