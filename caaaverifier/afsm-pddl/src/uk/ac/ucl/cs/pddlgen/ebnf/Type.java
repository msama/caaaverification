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

	public static Type create(final List<Type> either) {
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
	
	public static Type create(final Type fluent) {
		if (fluent == null) {
			throw new IllegalArgumentException("Statement <type> must have a <type> fluent.");
		}
		if (!definedKeys.contains(RequireKey.FLUENTS)) {
			throw new IllegalArgumentException(
					"A fluent <type> can only be specified if <require-key> :fluents is specified.");
		}
		
		Type t = new Type() {
			protected void printInternal() {
				pw.print("(fluent ");
				writeInto(pw, fluent);
				pw.print(")");
			}
		};
		return t;
	}
	
}
