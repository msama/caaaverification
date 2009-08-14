/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class PrefName extends Streamable {
	Name name;

	private PrefName(Name name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		writeInto(pw, name);
	}

	public static PrefName create(String name) {
		return create(Name.create(name));
	}

	public static PrefName create(Name name) {
		return new PrefName(name);
	}
}
