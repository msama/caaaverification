/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author rax
 *
 */
public abstract class Effect extends Streamable {

	private Effect() {}
	
	private void writeEffect(Effect[] effects) {
		++alignment;
		for (Effect eff : effects) {
			align();
			pw.print("(");
			writeInto(pw, eff);
			pw.print(")");
		}
		--alignment;
		//align();
	}
	
	public static Effect createAnd(final Effect... and) {
		if (and.length == 0) {
			throw new IllegalArgumentException(
					"Statement <effect> must contain a list of effects.");
		}
		
		Effect effect = new Effect(){ 
			protected void printInternal() {
				super.writeEffect(and);
			}
		};
		return effect;
	}

	public static Effect createNot(final AtomicFormula<Term> formula) {
		if (formula == null) {
			throw new IllegalArgumentException(
					"Statement <effect> must contain a formula.");
		}
		
		Effect effect = new Effect(){ 
			protected void printInternal() {
				pw.print("(not ");
				++alignment;
				align();
				writeInto(pw, formula);
				--alignment;
				align();
				pw.print(")");
			}
		};
		return effect;
	}
	
	public static Effect createFormula(final AtomicFormula<Term> formula) {
		if (formula == null) {
			throw new IllegalArgumentException(
					"Statement <effect> must contain a formula.");
		}
		
		Effect effect = new Effect(){ 
			protected void printInternal() {
				writeInto(pw, formula);
			}
		};
		return effect;
	}
	
	public static Effect createForall(final Effect effect, final Variable... vars) {
		if (!RequireKey.definedKeys.contains(RequireKey.CONDITIONAL_EFFECTS)) {
			throw new IllegalArgumentException(
					"Statement <effect> must contain a formula.");
		}

		if (vars.length == 0) {
			throw new IllegalArgumentException(
					"Statement <effect> must contain a list of variables.");
		}

		if (effect == null) {
			throw new IllegalArgumentException(
					"Statement <effect> must contain a formula.");
		}
		
		Effect result = new Effect(){ 
			protected void printInternal() {
				pw.print("(not ");
				++alignment;
				align();
				writeSpaceSeparatedList(vars);
				align();
				writeInto(pw, effect);
				--alignment;
				align();
				pw.print(")");
			}
		};
		return result;
	}

	public static Effect createWhen(final GD gd, final Effect effect) {
		if (!RequireKey.definedKeys.contains(RequireKey.CONDITIONAL_EFFECTS)) {
			throw new IllegalArgumentException(
					"Statement <effect> must contain a formula.");
		}

		if (gd == null) {
			throw new IllegalArgumentException(
					"Statement <effect> must contain a goal description.");
		}

		if (effect == null) {
			throw new IllegalArgumentException(
					"Statement <effect> must contain a formula.");
		}
		
		Effect result = new Effect(){ 
			protected void printInternal() {
				pw.print("(not ");
				++alignment;
				align();
				writeInto(pw, gd);
				align();
				writeInto(pw, effect);
				--alignment;
				align();
				pw.print(")");
			}
		};
		return result;
	}

	public static Effect createChange(final Fluent fluent, final Expression expression) {
		if (!RequireKey.definedKeys.contains(RequireKey.FLUENTS)) {
			throw new IllegalArgumentException(
					"Statement <effect> must contain a formula.");
		}

		if (fluent == null) {
			throw new IllegalArgumentException(
					"Statement <effect> must contain a fluent.");
		}

		if (expression == null) {
			throw new IllegalArgumentException(
					"Statement <effect> must contain a formula.");
		}
		
		Effect result = new Effect(){ 
			protected void printInternal() {
				pw.print("(not ");
				++alignment;
				align();
				writeInto(pw, fluent);
				align();
				writeInto(pw, expression);
				--alignment;
				align();
				pw.print(")");
			}
		};
		return result;
	}
}
