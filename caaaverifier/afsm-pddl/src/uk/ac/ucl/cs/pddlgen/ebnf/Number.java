/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public abstract class Number extends Streamable {

	public static Number create(final int i) {
		return new Number() {
			@Override
			protected void printInternal() {
				pw.print(i);
			}
		};
	}
	
	public static Number create(final float f) {
		return new Number() {
			@Override
			protected void printInternal() {
				pw.print(f);
			}
		};
	}

}
