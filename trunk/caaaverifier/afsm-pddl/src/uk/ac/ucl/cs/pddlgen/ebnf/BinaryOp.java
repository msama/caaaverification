/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public abstract class BinaryOp extends Streamable {

	public static BinaryOp create(final MultiOp multyOp) {
		return new BinaryOp() {
			@Override
			protected void printInternal() {
				writeInto(pw, multyOp);
			}
		};
	}

	public static BinaryOp createMinus() {
		return new BinaryOp() {
			@Override
			protected void printInternal() {
				pw.print("-");
			}
		};
	}
	
	public static BinaryOp createDivide() {
		return new BinaryOp() {
			@Override
			protected void printInternal() {
				pw.print("/");
			}
		};
	}
}
