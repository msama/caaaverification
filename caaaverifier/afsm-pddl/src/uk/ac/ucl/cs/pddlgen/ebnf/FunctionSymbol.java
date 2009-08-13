/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class FunctionSymbol extends Streamable {

	Name name;

	private FunctionSymbol(Name name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		writeInto(pw, name);
	}

	public static FunctionSymbol create(String name) {
		return create(Name.create(name));
	}
	
	public static FunctionSymbol create(Name name) {
		if (name == null) {
			throw new IllegalArgumentException("Statement <function-symbol> must have a <name>.");
		}
		return new FunctionSymbol(name);
	}
	
}
