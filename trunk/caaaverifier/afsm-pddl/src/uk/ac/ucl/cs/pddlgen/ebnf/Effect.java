/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

import java.util.Arrays;

/**
 * @author rax
 *
 */
public class Effect extends Streamable {
	
	private CEffect[] cEffects;

	/**
	 * @param cEffects
	 */
	public Effect(CEffect[] cEffects) {
		this.cEffects = cEffects;
	}

	@Override
	protected void printInternal() {
		if (cEffects.length == 1) {
			writeInto(pw, cEffects[0]);
		} else {
			pw.print("(and ");
			writeAlignedList(Arrays.asList(cEffects));
			pw.print(")");
		}
	}
	
	

}
