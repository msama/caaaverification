/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

import javax.swing.plaf.basic.BasicTableUI.FocusHandler;

import uk.ac.ucl.cs.afsm.common.Assignment;

/**
 * @author rax
 *
 */
public abstract class PEffect extends Streamable {

	public static PEffect not(final AtomicFormula<Term> formula) {
		return new PEffect(){
			@Override
			protected void printInternal() {
				pw.print("(not ");
				writeInto(pw, formula);
				pw.print(")");
			}
		};
	}
	
	public static PEffect create(final AtomicFormula<Term> formula) {
		return new PEffect(){
			@Override
			protected void printInternal() {
				writeInto(pw, formula);
			}
		};
	}
	
	public static PEffect fluent(final AssignOp assignOp, final FHead fHead, final FExp fExp) {
		if (!definedKeys.contains(RequireKey.FLUENTS)) {
			throw new IllegalStateException("RequireKey.FLUENTS must be defined.");
		}
		return new PEffect(){
			@Override
			protected void printInternal() {
				pw.print("(");
				writeInto(pw, assignOp);
				pw.print(" ");
				writeInto(pw, fHead);
				pw.print(" ");
				writeInto(pw, fExp);
				pw.print(")");
			}
		};
	}

}
