/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class SafetyDef extends Streamable {

	private GD gd;
	
	/**
	 * @param gd
	 */
	private SafetyDef(GD gd) {
		this.gd = gd;
	}
	
	@Override
	protected void printInternal() {
		pw.print("(:safety ");
		writeInto(pw, gd);
		pw.print(")");
		align();
	}

	public static SafetyDef create(final GD gd) {
		if (gd == null) {
			throw new IllegalArgumentException("Statement <axiom-def> " +
					"must have a <GD>");
		}
		return new SafetyDef(gd);
	}

}
