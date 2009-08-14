/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

import java.util.Arrays;

/**
 * @author rax
 *
 */
public abstract class CEffect extends Streamable {

	public static CEffect forAll(final TypedList<Variable> typedList, final Effect effect) {
		if (!definedKeys.contains(RequireKey.CONDITIONAL_EFFECTS)) {
			throw new IllegalStateException("");
		}
		return new CEffect(){

			@Override
			protected void printInternal() {
				pw.print("(forall (");
				writeInto(pw, typedList);
				pw.print(")");
				writeInto(pw, effect);
				pw.print(")");
			}

		};
	}
	
	public static CEffect when(final GD gd, final CondEffect condEffect) {
		if (!definedKeys.contains(RequireKey.CONDITIONAL_EFFECTS)) {
			throw new IllegalStateException("");
		}
		return new CEffect(){
			@Override
			protected void printInternal() {
				pw.print("(when ");
				writeInto(pw, gd);
				pw.print(" ");
				writeInto(pw, condEffect);
				pw.print(")");
			}
		};
	}

	public static CEffect create(final PEffect pEffect) {
		return new CEffect(){
			@Override
			protected void printInternal() {
				writeInto(pw, pEffect);
			}
		};
	}
}
