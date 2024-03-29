/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class ActionSymbol extends Streamable {

	private Name name;
	
	/**
	 * @param name
	 */
	public ActionSymbol(Name name) {
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		writeInto(pw, name);
	}

	public static ActionSymbol create(String name) {
		return create(Name.create(name));
	}
	
	public static ActionSymbol create(Name name) {
		return new ActionSymbol(name);
	}
}
