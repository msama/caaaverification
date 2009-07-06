/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class ActionFunctor extends Streamable {

	private Name name;
	
	/**
	 * @param name
	 */
	public ActionFunctor(Name name) {
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		writeInto(name);
	}

	
}
