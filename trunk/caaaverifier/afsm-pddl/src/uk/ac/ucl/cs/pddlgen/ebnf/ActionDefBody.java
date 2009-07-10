/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class ActionDefBody extends Streamable {
	
	// Requires RequireKey.EXISTENTIAL_PRECONDITIONS 
	// Requires RequireKey.CONDITIONAL_EFFECTS
	// 0..1
	protected TypedList<Variable> variables;
	
	protected GD preconditions;
	
	// Requires RequireKey.ACTION_EXPANSIONS
	protected ActionSpec actionSpec;
	
	// Requires RequireKey.ACTION_EXPANSIONS
	protected boolean expansionMethod = false;

	// Requires RequireKey.ACTION_EXPANSIONS
	protected GD maintain;
	
	protected Effect effect;
	
	// Requires RequireKey.ACTION_EXPANSIONS
	protected BooleanFlag onlyInExpansion;
	
	private ActionDefBody() {
		
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		if (variables != null) {
			align();
			pw.print(":vars (");
			writeInto(pw, variables);
			pw.print(")");
		}
		
		if (preconditions != null) {
			align();
			pw.print(":preconditions ");
			++alignment;
			align();
			writeInto(pw, preconditions);
			--alignment;
		}
		
		if (expansionMethod) {
			align();
			pw.print(":expansion :methods");
		}
		
		if (maintain != null) {
			align();
			pw.print(":maintain ");
			writeInto(pw, maintain);
		}
		
		if (effect != null) {
			align();
			pw.print(":effect ");
			writeInto(pw, effect);
		}
		
		if (onlyInExpansion != null) {
			align();
			pw.print(":only-in-expansion ");
			writeInto(pw, onlyInExpansion);
		}
	}

	public static ActionDefBody create(GD preconditions, Effect effect) {
		ActionDefBody body = new ActionDefBody();
		body.preconditions = preconditions;
		body.effect = effect;
		return body;
	}
	
	public static ActionDefBody create(TypedList<Variable> variables, GD preconditions,
			ActionSpec actionSpec, boolean expandMethod, GD maintain, Effect effect,
			BooleanFlag onlyInExpansion) {
		
		if (variables != null &&
				!definedKeys.contains(RequireKey.EXISTENTIAL_PRECONDITIONS) && 
				!definedKeys.contains(RequireKey.CONDITIONAL_EFFECTS)) {
			throw new IllegalStateException("Statement <typed-list (variables)> " +
					"requires <require-key> :existential-preconditions and :conditional-effects");
		}
		
		if ((actionSpec != null || expandMethod || maintain != null || onlyInExpansion != null) &&
				!definedKeys.contains(RequireKey.ACTION_EXPANSIONS)) {
			throw new IllegalStateException(
					":expansion requires <require-key> :action-expansions.");
		}
		
		ActionDefBody body = new ActionDefBody();
		body.variables = variables;
		body.preconditions = preconditions;
		body.actionSpec = actionSpec;
		body.expansionMethod = expandMethod;
		body.maintain = maintain;
		body.effect = effect;
		body.onlyInExpansion = onlyInExpansion;
		return body;
	}
}
