package uk.ac.ucl.cs.pddlgen.ebnf;

public class Situation extends Streamable {

	private InitSitName initName;
	
	/**
	 * @param initName
	 */
	private Situation(InitSitName initName) {
		this.initName = initName;
	}

	public static Situation create(String name) {
		return create(Name.create(name));
	}
	
	public static Situation create(Name name) {
		return create(InitSitName.create(name));
	}
	
	public static Situation create(InitSitName name) {
		if (name == null) {
			throw new IllegalArgumentException("Name cannot be null.");
		}
		return new Situation(name);
	}

	@Override
	protected void printInternal() {
		pw.print("(:situation ");
		writeInto(pw, initName);
		pw.print(")");
	}

}
