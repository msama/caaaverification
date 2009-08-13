/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class AtomicFormula<T extends Streamable> extends Streamable {

	protected T t;
	
	protected FunctionSymbol functionSymbol;
	
	private AtomicFormula() {
		
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		pw.print("(");
		writeInto(pw, functionSymbol);
		pw.print(" ");
		writeInto(pw, t);
		pw.print(")");
	}

	public static <K extends Streamable> AtomicFormula<K> create(FunctionSymbol functionSymbol, K t) {
		if (functionSymbol == null) {
			throw new IllegalArgumentException("<atomic-formula> must contain a <predicate>");
		}
		if (t == null) {
			throw new IllegalArgumentException("<atomic-formula> must contain an element.");
		}
		AtomicFormula<K> formula = new AtomicFormula<K>();
		formula.functionSymbol = functionSymbol;
		formula.t = t;
		return formula;
	}
}
