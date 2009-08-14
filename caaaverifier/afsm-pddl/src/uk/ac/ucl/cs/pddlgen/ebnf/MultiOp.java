/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public abstract class MultiOp extends Streamable {

	public static MultiOp createPlus() {
		return new MultiOp() {
			@Override
			protected void printInternal() {
				pw.print("+");
			}
		};
	}
	
	public static MultiOp createMultiply() {
		return new MultiOp() {
			@Override
			protected void printInternal() {
				pw.print("*");
			}
		};
	}

}
