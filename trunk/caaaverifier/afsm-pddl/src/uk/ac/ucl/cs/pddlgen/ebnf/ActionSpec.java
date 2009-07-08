/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public abstract class ActionSpec extends Streamable {

	private ActionSpec() {}
	
	public static ActionSpec create(final ActionTerm actionTerm) {
		if (actionTerm == null) {
			throw new IllegalArgumentException("Statement <action spec> must define <action-term>.");
		}
		ActionSpec actionSpec = new ActionSpec() {
			protected void printInternal() {
				writeInto(pw, actionTerm);
			}
		};
		return actionSpec;
	}
	
	public static ActionSpec createInContext(final ActionSpec action, final ActionDefBody body) {
		if (action == null) {
			throw new IllegalArgumentException("Statement <action spec> must define <action spec>.");
		}
		if (body == null) {
			throw new IllegalArgumentException("Statement <action spec> must define <action-def-body>.");
		}
		
		ActionSpec actionSpec = new ActionSpec() {
			protected void printInternal() {
				pw.print("(in-context");
				++alignment;
				align();
				writeInto(pw, action);
				align();
				writeInto(pw, body);
				--alignment;
				align();
				pw.print(")");
			}
		};
		return actionSpec;
	}
	
	public static ActionSpec createChoice(final ActionSpec... actions) {
		if (actions == null || actions.length == 0) {
			throw new IllegalArgumentException("Statement <action spec> must define <action spec>.");
		}
		
		ActionSpec actionSpec = new ActionSpec() {
			protected void printInternal() {
				pw.print("(choice");
				++alignment;
				align();
				writeInto(pw, actions);
				--alignment;
				align();
				pw.print(")");
			}
		};
		return actionSpec;
	}
	
	public static ActionSpec createForsome(final ActionSpec action, final TypedList<Variable>... vars) {
		if (vars == null) {
			throw new IllegalArgumentException("Statement <action spec> must define <typed list (<variable>)>.");
		}
		if (action == null) {
			throw new IllegalArgumentException("Statement <action spec> must define <action spec>.");
		}
		
		ActionSpec actionSpec = new ActionSpec() {
			protected void printInternal() {
				pw.print("(forsome");
				++alignment;
				align();
				pw.print("(");
				writeInto(pw, vars);
				pw.print(")");
				align();
				writeInto(pw, action);
				--alignment;
				align();
				pw.print(")");
			}
		};
		return actionSpec;
	}
	
	public static ActionSpec createSeries(final ActionSpec... actions) {
		if (actions == null || actions.length == 0) {
			throw new IllegalArgumentException("Statement <action spec> must define <action spec>.");
		}
		
		ActionSpec actionSpec = new ActionSpec() {
			protected void printInternal() {
				pw.print("(series");
				++alignment;
				align();
				writeInto(pw, actions);
				--alignment;
				align();
				pw.print(")");
			}
		};
		return actionSpec;
	}
	
	public static ActionSpec createParallel(final ActionSpec... actions) {
		if (actions == null || actions.length == 0) {
			throw new IllegalArgumentException("Statement <action spec> must define <action spec>.");
		}
		
		ActionSpec actionSpec = new ActionSpec() {
			protected void printInternal() {
				pw.print("(parallel");
				++alignment;
				align();
				writeInto(pw, actions);
				--alignment;
				align();
				pw.print(")");
			}
		};
		return actionSpec;
	}
	
	public static ActionSpec createParallel(final ActionLabelTerm[] labels1, final ActionTerm term, final ActionLabelTerm[] labels2) {
		if (labels1 == null || labels1.length == 0) {
			throw new IllegalArgumentException("Statement <action spec> must define <action label>.");
		}
		if (term == null ) {
			throw new IllegalArgumentException("Statement <action spec> must define <action label>.");
		}
		if (labels2 == null || labels2.length == 0) {
			throw new IllegalArgumentException("Statement <action spec> must define <action label>.");
		}
		
		ActionSpec actionSpec = new ActionSpec() {
			protected void printInternal() {
				pw.print("(tag");
				++alignment;
				align();
				writeInto(pw, labels1);
				align();
				writeInto(pw, term);
				align();
				writeInto(pw, labels2);
				--alignment;
				align();
				pw.print(")");
			}
		};
		return actionSpec;
	}
	
	public static ActionSpec create(final TypedList<Variable> var, final GD gd, final ActionSpec spec) {
		if (!RequireKey.definedKeys.contains(RequireKey.FOREACH_EXPANSION)) {
			throw new IllegalArgumentException("FOREACH_EXPANSION required.");
		}
		
		if (var == null) {
			throw new IllegalArgumentException("Statement <action spec> must define <typed-list>.");
		}
		if (gd == null ) {
			throw new IllegalArgumentException("Statement <action spec> must define <action label>.");
		}
		if (spec == null) {
			throw new IllegalArgumentException("Statement <action spec> must define <action spec>.");
		}
		
		ActionSpec actionSpec = new ActionSpec() {
			protected void printInternal() {
				pw.print("(foreach");
				++alignment;
				align();
				writeInto(pw, var);
				align();
				writeInto(pw, gd);
				align();
				writeInto(pw, spec);
				--alignment;
				align();
				pw.print(")");
			}
		};
		return actionSpec;
	}
	
	public static ActionSpec createConstrained(final ActionSpec[] specs, final ActionConstraint[] constraints) {
		if (!RequireKey.definedKeys.contains(RequireKey.FOREACH_EXPANSION)) {
			throw new IllegalArgumentException("DAG_EXPANSION required.");
		}
		
		if (specs == null) {
			throw new IllegalArgumentException("Statement <action spec> must define <typed-list>.");
		}
		if (constraints == null) {
			throw new IllegalArgumentException("Statement <action spec> must define <action spec>.");
		}
		
		ActionSpec actionSpec = new ActionSpec() {
			protected void printInternal() {
				pw.print("(constrained");
				++alignment;
				align();
				writeInto(pw, specs);
				align();
				writeInto(pw, constraints);
				--alignment;
				align();
				pw.print(")");
			}
		};
		return actionSpec;
	}
}
