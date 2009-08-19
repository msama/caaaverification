/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

import java.util.List;

/**
 * @author -RAX- (Michele Sama)
 *
 */
public abstract class PrefConGD extends Streamable {

	public static PrefConGD and(final List<PrefConGD> conGDs) {
		return new PrefConGD() {
			@Override
			protected void printInternal() {
				pw.print("(and ");
				writeAlignedList(conGDs);
				pw.print(")");
			}
		};
	}
	
	public static PrefConGD forall(final TypedList<Variable> vars, final PrefConGD conGD) {
		if (!definedKeys.contains(RequireKey.UNIVERSAL_PRECONDITIONS)) {
			throw new IllegalStateException(":universal-preconditions is required.");
		}
		return new PrefConGD() {
			@Override
			protected void printInternal() {
				pw.print("(forall (");
				writeInto(pw, vars);
				pw.print(")");
				writeInto(pw, conGD);
				pw.print(")");
			}
		};
	}
	
	public static PrefConGD preference(final PrefName prefName, final PrefConGD conGD) {
		if (!definedKeys.contains(RequireKey.PREFERENCES)) {
			throw new IllegalStateException(":preferences is required.");
		}
		return new PrefConGD() {
			@Override
			protected void printInternal() {
				pw.print("(preference (");
				writeInto(pw, prefName);
				pw.print(")");
				writeInto(pw, conGD);
				pw.print(")");
			}
		};
	}

	public static PrefConGD create(final ConGD conGD) {
		return new PrefConGD() {
			@Override
			protected void printInternal() {
				writeInto(pw, conGD);
			}
		};
	}
}
