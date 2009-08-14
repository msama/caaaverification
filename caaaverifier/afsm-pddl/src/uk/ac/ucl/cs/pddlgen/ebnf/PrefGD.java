/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public abstract class PrefGD extends Streamable {

	public static PrefGD create(final PrefName prefName, final PreGD preGD) {
		if (!definedKeys.contains(RequireKey.PREFERENCES)) {
			throw new IllegalStateException("RequireKey.PREFERENCES required.");
		}
		return new PrefGD() {
			@Override
			protected void printInternal() {
				pw.print("(preference ");
				writeInto(pw, prefName);
				pw.print(" ");
				writeInto(pw, preGD);
				pw.print(")");
			}
		};
	}

	public static PrefGD create(final GD gd) {
		return new PrefGD() {
			@Override
			protected void printInternal() {
				writeInto(pw, gd);
			}
		};
	}
	
}
