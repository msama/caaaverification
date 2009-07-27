/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class Literal<T extends Streamable> extends Streamable {

	protected AtomicFormula<T> formula;
	
	private Expansion expansion;

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		switch(expansion) {
			case FORMULA:
				writeInto(pw, formula);
				break;
			case NOT:
				pw.print("(not ");
				//++alignment;
				//align();
				writeInto(pw, formula);
				//--alignment;
				//align();
				pw.print(")");
		}
	}
	
	private enum Expansion {
		FORMULA,
		NOT;
	}

	public static<K extends Streamable> Literal<K> createFormula(AtomicFormula<K> formula) {
		Literal<K> literal = new Literal<K>();
		literal.formula = formula;
		literal.expansion = Expansion.FORMULA;
		return literal;
	}
	
	public static<K extends Streamable> Literal<K> createNot(AtomicFormula<K> formula) {
		Literal<K> literal = new Literal<K>();
		literal.formula = formula;
		literal.expansion = Expansion.NOT;
		return literal;
	}
}
