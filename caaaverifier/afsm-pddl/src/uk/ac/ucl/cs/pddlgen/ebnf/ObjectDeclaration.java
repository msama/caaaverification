package uk.ac.ucl.cs.pddlgen.ebnf;

public class ObjectDeclaration extends Streamable {

	private TypedList<Name> names;
	
	/**
	 * @param names
	 */
	private ObjectDeclaration(TypedList<Name> names) {
		this.names = names;
	}

	public static ObjectDeclaration create(TypedList<Name> names) {
		if (names == null) {
			throw new IllegalArgumentException("The name should ne not null.");
		}
		return new ObjectDeclaration(names);
	}
	
	@Override
	protected void printInternal() {
		pw.print("(: objects ");
		writeInto(pw, names);
		pw.print(")");
	}

}
