/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class ActionDefBody extends Streamable {

	private EmptyOr<PreGD> precondition;
	private EmptyOr<Effect> effect;

	/**
	 * @param precondition
	 * @param effect
	 */
	private ActionDefBody(EmptyOr<PreGD> precondition, EmptyOr<Effect> effect) {
		this.precondition = precondition;
		this.effect = effect;
	}

	@Override
	protected void printInternal() {
		pw.print(":precondition ");
		writeInto(pw, precondition);
		align();
		pw.print(":effect ");
		writeInto(pw, effect);
		align();
	}
	
	public static ActionDefBody create(PreGD precondition, Effect effect) {
		return create(EmptyOr.create(precondition), EmptyOr.create(effect));
	}
	
	public static ActionDefBody create(EmptyOr<PreGD> precondition, EmptyOr<Effect> effect) {
		if (precondition == null) {
			throw new IllegalArgumentException("Precondition cannot be null.");
		}
		if (effect == null) {
			throw new IllegalArgumentException("Effect cannot be null.");
		}
		return new ActionDefBody(precondition, effect);
	}
}
