/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rax
 *
 */
public class RequireDef extends Streamable {

	protected List<RequireKey> requireKeys = new ArrayList<RequireKey>();
	
	private RequireDef() {
		
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#writeInto(java.io.PrintWriter, int)
	 */
	@Override
	protected void printInternal() {
		pw.print("(:requirements");
		for (RequireKey k : requireKeys) {
			pw.print(" ");
			k.printToStream();
		}
		pw.print(")");
	}

	public static RequireDef create(List<RequireKey> requireKeys) {
		if (requireKeys.size() < 1) {
			throw new IllegalStateException(
					"Statement <require-def> must contain at least one <require-key>.");
		}
		RequireDef requireDef = new RequireDef();
		requireDef.requireKeys = requireKeys;
		return requireDef;
	}
}
