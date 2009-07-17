package uk.ac.ucl.cs.pddlgen.ebnf;

public abstract class Goal extends Streamable {

	public static Goal create(final GD gd) {
		return new Goal() {

			@Override
			protected void printInternal() {
				pw.print("(:goal ");
				writeInto(pw, gd);
				pw.print(")");
			}
			
		};
	}
	
	// TODO(rax): this should only accept ActionDef<ActionTerm>
	public static Goal create(final ActionSpec action) {
		if (!definedKeys.contains(RequireKey.ACTION_EXPANSIONS)) {
			throw new IllegalArgumentException("RequireKey.ACTION_EXPANSIONS is required.");
		}
		return new Goal() {

			@Override
			protected void printInternal() {
				pw.print("(:expansion ");
				writeInto(pw, action);
				pw.print(")");
			}
			
		};		
	}
}
