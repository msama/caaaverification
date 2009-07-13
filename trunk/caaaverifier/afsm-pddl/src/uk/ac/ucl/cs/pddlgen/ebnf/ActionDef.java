/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class ActionDef extends Streamable {

	protected ActionFunctor actionFunctor;
	protected TypedList<Variable> variables;
	protected ActionDefBody actionDefBody;
	
	
	/**
	 * @param actionFunctor
	 * @param variables
	 * @param actionDefBody
	 */
	private ActionDef(ActionFunctor actionFunctor,
			TypedList<Variable> variables, ActionDefBody actionDefBody) {
		this.actionFunctor = actionFunctor;
		this.variables = variables;
		this.actionDefBody = actionDefBody;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		pw.print("(:action ");
		writeInto(pw, actionFunctor);
		++ alignment;
		align();
		
		pw.print(":parameters (");
		writeInto(pw, variables);
		pw.print(")");
		
		writeInto(pw, actionDefBody);
		
		-- alignment;
		align();
		pw.print(")");
	}

	public static ActionDef create(ActionFunctor actionFunctor, TypedList<Variable> variables,
			ActionDefBody actionDefBody) {
		if (actionFunctor == null) {
			throw new IllegalStateException("Statement <action> requires an <action-functor>.");
		}

		if (variables == null) {
			throw new IllegalStateException("Statement <action> requires a <typed-list (variable)>.");
		}
		
		if (actionDefBody == null) {
			throw new IllegalStateException("Statement <action> requires an <action-def body>.");
		}
		return new ActionDef(actionFunctor, variables, actionDefBody);
	}
}
