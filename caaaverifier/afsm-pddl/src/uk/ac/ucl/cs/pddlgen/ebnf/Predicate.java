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
		if (name == null) {
			throw new IllegalArgumentException("Statement <predicate> must have a <name>.");
		}
		return new Predicate(name);
	}
	
	public static Predicate createEquals() {
		if (!definedKeys.contains(RequireKey.EQUALITY)) {
			throw new IllegalStateException("'=' requires equalities.");
		}
		return new Predicate(Name.create("="));
	}
}
