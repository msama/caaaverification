/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

import java.util.List;

/**
 * @author rax
 *
 */
public abstract class Type extends Streamable {
	
	private Type() {}
	
	public static Type create(final Name name) {
		if (name == null) {
			throw new IllegalArgumentException("Statement <type> must have a <name>.");
		}
		Type t = new Type() {
			protected void printInternal() {
				writeInto(pw, name);
			}
		};
		return t;
	}

	public static Type create(final List<PrimitiveType> either) {
		if (either == null) {
			throw new IllegalArgumentException("Statement <type> must have some eithers.");
		}
		Type t = new Type() {
			protected void printInternal() {
				pw.print("(either ");
				super.writeSpaceSeparatedList(either);
				pw.print(")");
			}
		};
		return t;
	}
	
}
