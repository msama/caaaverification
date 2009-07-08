/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class ActionLabel extends Streamable {

	private Name name;
	
	/**
	 * @param name
	 */
	private ActionLabel(Name name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		writeInto(pw, name);
	}

	public static ActionLabel create(String name) {
		return new ActionLabel(Name.create(name));
	}
	
	public static ActionLabel create(Name name) {
		if (name == null) {
			throw new IllegalArgumentException("Statement <action label> " +
					"must contain a <name>.");
		}
		return new ActionLabel(name);
	}
}
