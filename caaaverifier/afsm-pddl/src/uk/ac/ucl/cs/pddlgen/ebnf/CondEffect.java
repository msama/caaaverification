package uk.ac.ucl.cs.pddlgen.ebnf;

import java.util.Arrays;

public class CondEffect extends Streamable {

	private PEffect[] pEffects;

	/**
	 * @param cEffects
	 */
	public CondEffect(PEffect[] pEffects) {
		this.pEffects = pEffects;
	}

	@Override
	protected void printInternal() {
		if (pEffects.length == 1) {
			writeInto(pw, pEffects[0]);
		} else {
			pw.print("(and ");
			writeAlignedList(Arrays.asList(pEffects));
			pw.print(")");
		}
	}
}
