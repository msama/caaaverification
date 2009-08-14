/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

import java.util.List;

/**
 * @author rax
 *
 */
public abstract class PreGD extends Streamable {

	public static PreGD create(final PrefGD prefGD) {
		return new PreGD() {
			@Override
			protected void printInternal() {
				writeInto(pw, prefGD);
			}
		};
	}
	
	public static PreGD create(final List<PrefGD> list) {
		return new PreGD() {
			@Override
			protected void printInternal() {
				pw.print("(and ");
				writeAlignedList(list);
				pw.print(")");
			}
		};
	}

	public static PreGD create(final TypedList<Variable> typedList, final PrefGD prefGD) {
		if (!definedKeys.contains(RequireKey.UNIVERSAL_PRECONDITIONS)) {
			throw new IllegalStateException("RequireKey.UNIVERSAL_PRECONDITIONS required.");
		}
		return new PreGD() {
			@Override
			protected void printInternal() {
				pw.print("(forall (");
				writeInto(pw, typedList);
				pw.print(") ");
				writeInto(pw, prefGD);
				pw.print(")");
			}
		};
	}
}
