/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class Predicate extends Streamable {
	Name name;

	private Predicate(Name name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		writeInto(pw, name);
	}

	public static Predicate create(String name) {
		return create(Name.create(name));
	}

	public static Predicate create(Name name) {
		return new Predicate(name);
	}
}
