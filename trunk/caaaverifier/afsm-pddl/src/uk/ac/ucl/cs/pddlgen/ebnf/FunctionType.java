/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class FunctionType extends Streamable {

	private Number number;
	
	/**
	 * 
	 */
	private FunctionType(Number number) {
		this.number = number;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		writeInto(pw, number);
	}

	public static FunctionType create(Number number) {
		if (number == null) {
			throw new IllegalArgumentException("Number cannot be null.");
		}
		return new FunctionType(number);
	}
}
