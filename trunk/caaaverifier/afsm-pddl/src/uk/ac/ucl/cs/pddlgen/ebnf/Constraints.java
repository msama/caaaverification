/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class Constraints extends Streamable {

	private ConGD conGD;
	
	/**
	 * 
	 */
	private Constraints(ConGD conGD) {
		this.conGD = conGD;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		pw.print("(:functions ");
		writeInto(pw, conGD);
		pw.print(")");
	}

	public static Constraints create(ConGD conGD) {
		if (conGD == null) {
			throw new IllegalArgumentException("con-GD cannot be null.");
		}
		return new Constraints(conGD);
	}
}
