/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class TypesDef extends Streamable {
	
	protected TypedList<Name> typedList;

	private TypesDef() {}

	@Override
	protected void printInternal() {
		pw.print("(:types ");
		writeInto(pw, typedList);
		pw.print(" )");
	}

	public static TypesDef create(TypedList<Name> typedList) {
		if (typedList == null) {
			throw new IllegalStateException("Statement <types-def> must have a <typed list (name)>.");
		}
		TypesDef types = new TypesDef();
		types.typedList = typedList;
		return types;
	}

}
