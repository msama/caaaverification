/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class Constraints extends Streamable {

	private PrefConGD prefConGD;
	
	/**
	 * 
	 */
	private Constraints(PrefConGD prefConGD) {
		this.prefConGD = prefConGD;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		pw.print("(:functions ");
		writeInto(pw, prefConGD);
		pw.print(")");
	}

	public static Constraints create(PrefConGD prefConGD) {
		if (prefConGD == null) {
			throw new IllegalArgumentException("prefConGD cannot be null.");
		}
		return new Constraints(prefConGD);
	}
}
