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
	
	protected Predicate predicate;
	
	private AtomicFormula() {
		
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		pw.print("(");
		writeInto(pw, predicate);
		pw.print(" ");
		writeInto(pw, t);
		pw.print(")");
	}

	public static <K extends Streamable> AtomicFormula<K> create(Predicate predicate, K t) {
		if (predicate == null) {
			throw new IllegalArgumentException("<atomic-formula> must contain a <predicate>");
		}
		if (t == null) {
			throw new IllegalArgumentException("<atomic-formula> must contain an element.");
		}
		AtomicFormula<K> formula = new AtomicFormula<K>();
		formula.predicate = predicate;
		formula.t = t;
		return formula;
	}
}
