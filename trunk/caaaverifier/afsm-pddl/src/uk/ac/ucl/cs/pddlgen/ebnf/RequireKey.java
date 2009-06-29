/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class RequireKey extends Streamable {
	
	public static final RequireKey TYPING = null;
	public static final RequireKey SAFETY_CONSTRAINTS = null;
	public static final RequireKey EXPRESSION_EVALUATION = null;
	public static final RequireKey DOMAIN_AXIOMS = null;
	public static final RequireKey ACTION_EXPANSIONS = null;
	public static final RequireKey FLUENTS = null;
	public static final RequireKey EXISTENTIAL_PRECONDITIONS = null;
	public static final RequireKey CONDITIONAL_EFFECTS = null;
	public static final RequireKey UNIVERSAL_PRECONDITIONS = null;
	public static final RequireKey DISJUNCTIVE_PRECONDITIONS = null;
	public static final RequireKey FOREACH_EXPANSION = null;

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		definedKeys.add(this);

	}

}
