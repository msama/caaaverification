/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class Variable extends Streamable {

	protected Name name;
	
	private Variable() {
		
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		pw.print("?");
		writeInto(name);
	}

	public static Variable create(String name) {
		return create(Name.create(name));
	}
	
	public static Variable create(Name name) {
		if (name == null) {
			throw new IllegalArgumentException(
					"Statement <variable> must have a <name>.");
		}
		Variable var = new Variable();
		var.name = name;
		return var;
	}
}
