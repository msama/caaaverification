package uk.ac.ucl.cs.pddlgen.ebnf;

public class InitSitName extends Streamable {

	private Name name;
	
	/**
	 * @param name
	 */
	private InitSitName(Name name) {
		this.name = name;
	}

	public static InitSitName create(Name name) {
		if (name == null) {
			throw new IllegalArgumentException("Name cannot be null.");
		}
		return new InitSitName(name);
	}
	
	@Override
	protected void printInternal() {
		writeInto(pw, name);
	}

}
