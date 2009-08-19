package uk.ac.ucl.cs.pddlgen.ebnf;

public abstract class Goal extends Streamable {

	public static Goal create(final PreGD preGD) {
		return new Goal() {

			@Override
			protected void printInternal() {
				pw.print("(:goal ");
				++alignment;
				align();
				writeInto(pw, preGD);
				--alignment;
				align();
				pw.print(")");
			}
			
		};
	}

}
