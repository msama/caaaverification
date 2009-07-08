/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class RequireKey extends Streamable {
	
	/**
	 * @param label
	 */
	public RequireKey(String label) {
		this.label = label;
	}

	public static final RequireKey TYPING = new RequireKey("typing");
	public static final RequireKey CONSTRAINTS = new RequireKey("constaints"); //????????????????
	public static final RequireKey PREFERENCIES = new RequireKey("preferencies"); //????????????????
	public static final RequireKey SAFETY_CONSTRAINTS = new RequireKey("safety-constraints");
	public static final RequireKey EXPRESSION_EVALUATION = new RequireKey("expression-evaluation");
	public static final RequireKey DOMAIN_AXIOMS = new RequireKey("domain-axioms");
	public static final RequireKey ACTION_EXPANSIONS = new RequireKey("action-expasions");
	public static final RequireKey FLUENTS = new RequireKey("fluents");
	public static final RequireKey EXISTENTIAL_PRECONDITIONS = new RequireKey("existential-preconditions");
	public static final RequireKey CONDITIONAL_EFFECTS = new RequireKey("conditional-effects");
	public static final RequireKey UNIVERSAL_PRECONDITIONS = new RequireKey("universal-preconditions");
	public static final RequireKey DISJUNCTIVE_PRECONDITIONS = new RequireKey("disjunctive-preconditions");
	public static final RequireKey FOREACH_EXPANSION = new RequireKey("foreach-expansion");

	private final String label;
	
	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		definedKeys.add(this); // TODO(rax): this is unnecessary
		pw.print(":" + label);
	}

}
