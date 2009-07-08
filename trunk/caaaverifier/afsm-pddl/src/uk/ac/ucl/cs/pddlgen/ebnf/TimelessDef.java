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
public class TimelessDef extends Streamable {

	protected final List<Literal<Name>> literals = new ArrayList<Literal<Name>>();

	/**
	 * Creates a new instance of this class. Only the factory method can invoke it.
	 */
	private TimelessDef() {
		// nothing to be done.
	}

	@Override
	protected void printInternal() {
		pw.print("(:timeless ");
		for (Literal<Name> l : literals) {
			pw.print(" ");
			l.printToStream(pw);
		}
		pw.print(")");
	}
	
	public static TimelessDef create(List<Literal<Name>> literals) {
		if (literals == null) {
			throw new IllegalStateException("Statement <timeless-def> must contain a list of <literal (name)>s.");
		}
		if (literals.size() < 1) {
			throw new IllegalStateException("Statement <timeless-def> must contain at least a <literal (name)>.");
		}
		TimelessDef timeless = new TimelessDef();
		timeless.literals.addAll(literals);
		return timeless;
	}
}
