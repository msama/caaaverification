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
public class ExtensionDef extends Streamable {

	// 1..n
	List<Name> names = new ArrayList<Name>();

	private ExtensionDef() {}
	
	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#writeInto(java.io.PrintWriter, int)
	 */
	@Override
	protected void printInternal() {
		pw.print("(:extends");
		for (Name n : names) {
			pw.print(" ");
			n.printToStream(pw);
		}
		pw.print(")");
	}
	
	public static ExtensionDef create(List<Name> names) {
		if (names.size() < 1) {
			throw new IllegalStateException(
					"Statement <extension-def> must contain at least one <domain-name>.");
		}
		ExtensionDef extension = new ExtensionDef();
		extension.names = names;
		return extension;
	}

}
