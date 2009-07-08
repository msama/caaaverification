/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class ActionLabelTerm extends Streamable {

	private ActionLabel label;
	private ActionLabel lesser;
	private ActionLabel greater;
	
	/**
	 * @param label
	 * @param lesser
	 * @param greater
	 */
	private ActionLabelTerm(ActionLabel label, ActionLabel lesser,
			ActionLabel greater) {
		this.label = label;
		this.lesser = lesser;
		this.greater = greater;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		writeInto(pw, label);
		++alignment;
		align();
		pw.print("| (< ");
		writeInto(pw, lesser);
		pw.print(")");
		pw.print("| (> ");
		writeInto(pw, greater);
		pw.print(")");
		--alignment;
		align();
	}

	public static ActionLabelTerm create(final ActionLabel label,
			final ActionLabel lesser, final ActionLabel greater) {
		if (label == null) {
			throw new IllegalArgumentException("Statement <action-label term>" +
					"must contain a valid label.");
		}
		if (lesser == null) {
			throw new IllegalArgumentException("Statement <action-label term>" +
					"must be greater than a valid label.");
		}
		if (label == null) {
			throw new IllegalArgumentException("Statement <action-label term>" +
					"must be lesser than a valid label.");
		}
		return new ActionLabelTerm(label, lesser, greater);
	}
}
