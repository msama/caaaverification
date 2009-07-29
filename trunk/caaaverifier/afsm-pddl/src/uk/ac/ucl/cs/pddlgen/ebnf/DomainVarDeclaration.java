/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class DomainVarDeclaration extends Streamable {

	private Name name;
	
	/**
	 * @param name
	 */
	private DomainVarDeclaration(Name name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		writeInto(pw, name);
	}

	public static DomainVarDeclaration create(Name name) {
		if (name == null) {
			throw new IllegalArgumentException("Variable name cannot be null.");
		}
		return new DomainVarDeclaration(name);
	}
}
