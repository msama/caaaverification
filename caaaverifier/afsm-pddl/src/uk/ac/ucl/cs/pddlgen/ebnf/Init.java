package uk.ac.ucl.cs.pddlgen.ebnf;

import java.util.ArrayList;
import java.util.List;

public class Init extends Streamable {

	private List<InitEl> elements = new ArrayList<InitEl>();
	
	/**
	 * @param names
	 */
	private Init(List<InitEl> elements) {
		this.elements = elements;
	}
	
	public static Init create(List<InitEl> elements) {
		if (elements == null || elements.size() == 0) {
			throw new IllegalArgumentException("Init should contain at least 1 literal.");
		}
		return new Init(elements);
	}
	
	@Override
	protected void printInternal() {
		boolean align = elements.size() > 1;
		
		pw.print("(:init ");
		int i = 0;
		if (align) {
			++alignment;
		}
		for (InitEl el : elements) {
			writeInto(pw, el);
			if (++i < elements.size()) {
				if (align) {
					align();
				} else {
					pw.print(" ");
				}
			}
		}
		if (align) {
			--alignment;
			align();
		}
		pw.print(")");
	}

}
