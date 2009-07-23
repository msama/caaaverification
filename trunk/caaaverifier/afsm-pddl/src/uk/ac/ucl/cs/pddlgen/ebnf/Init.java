package uk.ac.ucl.cs.pddlgen.ebnf;

import java.util.ArrayList;
import java.util.List;

public class Init extends Streamable {

	private List<Literal<Name>> literals = new ArrayList<Literal<Name>>();
	
	/**
	 * @param names
	 */
	private Init(List<Literal<Name>> names) {
		this.literals = names;
	}
	
	public static Init create(List<Literal<Name>> names) {
		if (names == null || names.size() == 0) {
			throw new IllegalArgumentException("Init should contain at least 1 literal.");
		}
		return new Init(names);
	}
	
	@Override
	protected void printInternal() {
		boolean align = literals.size() > 1;
		
		pw.print("(:init ");
		int i = 0;
		if (align) {
			++alignment;
		}
		for (Literal<Name> literal : literals) {
			writeInto(pw, literal);
			if (++i < literals.size()) {
				if (align) {
					align();
				} else {
					pw.print(" ");
				}
			}
		}
		if (align) {
			--alignment;
		}
		pw.print(")");
	}

}
