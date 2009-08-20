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
public class FHead extends Streamable {

	private FunctionSymbol functionSymbol;
	private List<Term> terms = new ArrayList<Term>();
	
	/**
	 * 
	 */
	public FHead(FunctionSymbol functionSymbol) {
		this.functionSymbol = functionSymbol;
	}
	
	/**
	 * 
	 */
	public FHead(FunctionSymbol functionSymbol, Term... terms) {
		this.functionSymbol = functionSymbol;
		for (Term t : terms) {
			this.terms.add(t);
		}
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		if (terms.size() > 0) {
			pw.print("(");
			writeInto(pw, functionSymbol);
			pw.print(" ");
			writeSpaceSeparatedList(terms);
			pw.print(")");
		} else {
			writeInto(pw, functionSymbol);
		}
	}

	public static FHead create(FunctionSymbol functionSymbol, Term... terms) {
		return new FHead(functionSymbol, terms);
	}
	
	public static FHead create(FunctionSymbol functionSymbol) {
		return new FHead(functionSymbol);
	}
}
