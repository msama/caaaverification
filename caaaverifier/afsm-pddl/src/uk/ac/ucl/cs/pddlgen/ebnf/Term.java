/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public abstract class Term extends Streamable {

	private Term() {}
	
	public static Term create(final String name) {
		return create(Name.create(name));
	}
	
	public static Term create(final Name name) {
		if (name == null) {
			throw new IllegalArgumentException("Statement <term> must have a name.");
		}
		Term term = new Term() {
			protected void printInternal() {
				writeInto(pw, name);
			}
		};
		return term;
	}

	public static Term create(final Variable variable) {
		if (variable == null) {
			throw new IllegalArgumentException("Statement <term> must have a variable.");
		}
		Term term = new Term() {
			protected void printInternal() {
				writeInto(pw, variable);
			}
		};
		return term;
	}
}
