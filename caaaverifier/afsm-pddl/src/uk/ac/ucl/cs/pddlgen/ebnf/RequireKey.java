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

	public static final RequireKey STRIPS = new RequireKey("strips");
	public static final RequireKey TYPING = new RequireKey("typing");
	public static final RequireKey DISJUNCTIVE_PRECONDITIONS = new RequireKey("disjunctive-preconditions");
	public static final RequireKey EQUALITY = new RequireKey("equality");
	public static final RequireKey EXISTENTIAL_PRECONDITIONS = new RequireKey("existential-preconditions");
	public static final RequireKey UNIVERSAL_PRECONDITIONS = new RequireKey("universal-preconditions");
	public static final RequireKey QUANTIFIED_PRECONDITIONS = new RequireKey("quantified-preconditions");
	public static final RequireKey CONDITIONAL_EFFECTS = new RequireKey("conditional-effects");
	public static final RequireKey ACTION_EXPANSIONS = new RequireKey("action-expasions");
	public static final RequireKey FOREACH_EXPANSIONS = new RequireKey("foreach-expansions");
	public static final RequireKey DAG_EXPANSIONS = new RequireKey("dag-expansion");
	public static final RequireKey DOMAIN_AXIOMS = new RequireKey("domain-axioms");
	public static final RequireKey SUBGOAL_THROUGH_AXIOMS = new RequireKey("subgoal-through-axioms");
	public static final RequireKey SAFETY_CONSTRAINTS = new RequireKey("safety-constraints");
	public static final RequireKey EXPRESSION_EVALUATION = new RequireKey("expression-evaluation");
	public static final RequireKey FLUENTS = new RequireKey("fluents");
	public static final RequireKey OPEN_WORLD = new RequireKey("open-world");
	public static final RequireKey TRUE_NEGATION = new RequireKey("true-negation");
	public static final RequireKey ADL = new RequireKey("adl");
	public static final RequireKey UCPOP = new RequireKey("ucpop");
	public static final RequireKey DERIVED_PREDICATES = new RequireKey("derived-predicates");
	public static final RequireKey DURATIVE_ACTIONS = new RequireKey("durative-actions");
	public static final RequireKey PREFERENCES = new RequireKey("preferences");


		
	
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
