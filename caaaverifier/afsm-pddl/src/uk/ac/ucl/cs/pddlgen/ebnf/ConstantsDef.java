/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class ConstantsDef extends Streamable {

	TypedList<Name> typedList;

	private ConstantsDef() {
		
	}

	@Override
	protected void printInternal() {
		pw.print("(:constants ");
		writeInto(pw, typedList);
		pw.print(")");
	}

	public static ConstantsDef create(TypedList<Name> typedList) {
		if (typedList == null) {
			throw new IllegalStateException("Statement <constants-def> must have a <typed list (name)>.");
		}
		ConstantsDef constant = new ConstantsDef();
		constant.typedList = typedList;
		return constant;
	}
}
