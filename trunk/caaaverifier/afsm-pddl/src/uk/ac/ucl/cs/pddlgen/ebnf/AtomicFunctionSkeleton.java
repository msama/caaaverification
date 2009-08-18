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
public class AtomicFunctionSkeleton extends Streamable {

	private FunctionSymbol symbol;
	private TypedList<Variable> typedList;
	
	/**
	 * @param symbol
	 * @param typedList
	 */
	private AtomicFunctionSkeleton(FunctionSymbol symbol,
			TypedList<Variable> typedList) {
		this.symbol = symbol;
		this.typedList = typedList;
	}	

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		pw.print("(");
		writeInto(pw, symbol);
		pw.print(" ");
		writeInto(pw, typedList);
		pw.print(")");
	}

	public static AtomicFunctionSkeleton create(FunctionSymbol symbol,
			Variable... variables) {
		List<Variable> varList = new ArrayList<Variable>();
		for (Variable v : variables) {
			varList.add(v);
		}
		TypedList<Variable> typedList = TypedList.create(varList);
		return create(symbol, typedList);	
	}
	
	
	public static AtomicFunctionSkeleton create(FunctionSymbol symbol,
			TypedList<Variable> typedList) {
		if (symbol == null || symbol.name.equals("")) {
			throw new IllegalArgumentException("FunctionSymbol should not be null.");
		}
		if (typedList == null) {
			throw new IllegalArgumentException("TypedList should not be null.");
		}
		return new AtomicFunctionSkeleton(symbol, typedList);
	}
}
