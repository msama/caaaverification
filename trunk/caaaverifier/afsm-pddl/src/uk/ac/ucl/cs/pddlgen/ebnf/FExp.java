/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public abstract class FExp extends Streamable {

	public static FExp create(final Number number) {
		return new FExp(){
			@Override
			protected void printInternal() {
				writeInto(pw, number);
			}
		};
	}
	
	public static FExp createMinus(final BinaryOp binaryOp, final FExp expLeft, final FExp expRight) {
		return new FExp(){
			@Override
			protected void printInternal() {
				pw.print("(");
				writeInto(pw, binaryOp);
				pw.print(" ");
				writeInto(pw, expLeft);
				pw.print(" ");
				writeInto(pw, expRight);
				pw.print(")");
			}
		};
	}
	
	public static FExp createMinus(final FExp fExp) {
		return new FExp(){
			@Override
			protected void printInternal() {
				pw.print("(- ");
				writeInto(pw, fExp);
				pw.print(")");
			}
		};
	}
	
	public static FExp create(final FHead fhead) {
		return new FExp(){
			@Override
			protected void printInternal() {
				writeInto(pw, fhead);
			}
		};
	}
	


}
