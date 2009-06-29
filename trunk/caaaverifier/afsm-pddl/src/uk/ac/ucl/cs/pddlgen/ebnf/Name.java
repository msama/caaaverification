/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class Name extends Streamable {

	protected String name;

	private Name() {
	}

	@Override
	protected void printInternal() {
		pw.print(name);
	}

	public static Name create(String name) {
		if (name == null) {
			throw new IllegalStateException("Name cannot be null.");
		}
		if ("".equals(name)) {
			throw new IllegalStateException("Name cannot be an empty string.");
		}
		Name n = new Name();
		n.name = name;
		return n;
	}
}
