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

	private Predicate() {
		
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
		Predicate predicate = new Predicate();
		predicate.name = name;
		return predicate;
	}
}
