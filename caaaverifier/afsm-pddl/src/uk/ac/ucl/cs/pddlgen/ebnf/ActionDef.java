/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class ActionDef extends Streamable {

	protected ActionSymbol actionSymbol;
	protected TypedList<Variable> variables;
	protected ActionDefBody actionDefBody;
	
	
	/**
	 * @param actionSymbol
	 * @param variables
	 * @param actionDefBody
	 */
	private ActionDef(ActionSymbol actionSymbol,
			TypedList<Variable> variables, ActionDefBody actionDefBody) {
		this.actionSymbol = actionSymbol;
		this.variables = variables;
		this.actionDefBody = actionDefBody;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		pw.print("(:action ");
		writeInto(pw, actionSymbol);
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

	public static ActionDef create(ActionSymbol actionSymbol, TypedList<Variable> variables,
			ActionDefBody actionDefBody) {
		if (actionSymbol == null) {
			throw new IllegalStateException("Statement <action> requires an <action-functor>.");
		}

		if (variables == null) {
			throw new IllegalStateException("Statement <action> requires a <typed-list (variable)>.");
		}
		
		if (actionDefBody == null) {
			throw new IllegalStateException("Statement <action> requires an <action-def body>.");
		}
		return new ActionDef(actionSymbol, variables, actionDefBody);
	}
}
