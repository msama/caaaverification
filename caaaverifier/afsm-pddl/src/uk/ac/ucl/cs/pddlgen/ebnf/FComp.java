/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class FComp extends Streamable {

	private BinaryComp binaryComp;
	private FExp expLeft;
	private FExp expRight;
	
	/**
	 * @param binaryComp
	 * @param expLeft
	 * @param expRight
	 */
	public FComp(BinaryComp binaryComp, FExp expLeft, FExp expRight) {
		this.binaryComp = binaryComp;
		this.expLeft = expLeft;
		this.expRight = expRight;
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		pw.print("(");
		writeInto(pw, binaryComp);
		pw.print(" ");
		writeInto(pw, expLeft);
		pw.print(" ");
		writeInto(pw, expRight);
		pw.print(")");
	}

	public static FComp create(BinaryComp binaryComp, FExp expLeft, FExp expRight) {
		return new FComp(binaryComp, expLeft, expRight);
	}
}
