/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class EmptyOr<T extends Streamable> extends Streamable {

	private T element;
	
	/**
	 * 
	 */
	public EmptyOr() {
	}

	/**
	 * @param element
	 */
	public EmptyOr(T element) {
		this.element = element;
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		if (element != null) {
			writeInto(pw, element);
		} else {
			pw.print("()");
		}
	}

	public static <K extends Streamable> EmptyOr<K> create(K k){
		return new EmptyOr<K>(k);
	}
}
