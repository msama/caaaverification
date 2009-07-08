/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public abstract class ActionConstraint extends Streamable {

	public static ActionConstraint createInContext(
			final ActionConstraint constraint, final ActionDefBody body) {
		
		if (constraint == null) {
			throw new IllegalArgumentException("Statement <action constraint>" +
					" must contain a <action constraint>.");
		}
		
		if (body == null) {
			throw new IllegalArgumentException("Statement <action constraint>" +
					" must contain a <action-def body>.");
		}
		
		return new ActionConstraint() {
			@Override
			protected void printInternal() {
				pw.print("(in-context ");
				writeInto(pw, constraint);
				++alignment;
				align();
				writeInto(pw, body);
				--alignment;
				align();
				pw.print(")");
			}
		};
	} 

	public static ActionConstraint createSeries(
			final ActionConstraint... constraint) {
		
		if (constraint == null || constraint.length < 1) {
			throw new IllegalArgumentException("Statement <action constraint>" +
					" must contain a <action constraint>.");
		}
		
		return new ActionConstraint() {
			@Override
			protected void printInternal() {
				pw.print("(series ");
				++alignment;
				align();
				writeInto(pw, constraint);
				--alignment;
				align();
				pw.print(")");
			}
		};
	} 
	
	public static ActionConstraint createParallel(
			final ActionConstraint... constraint) {
		
		if (constraint == null || constraint.length < 1) {
			throw new IllegalArgumentException("Statement <action constraint>" +
					" must contain a <action constraint>.");
		}
		
		return new ActionConstraint() {
			@Override
			protected void printInternal() {
				pw.print("(parallel ");
				++alignment;
				align();
				writeInto(pw, constraint);
				--alignment;
				align();
				pw.print(")");
			}
		};
	} 

}
