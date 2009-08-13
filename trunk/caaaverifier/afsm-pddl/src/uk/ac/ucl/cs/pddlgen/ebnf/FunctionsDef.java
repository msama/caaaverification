/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class FunctionsDef extends Streamable {

	private TypedList<AtomicFunctionSkeleton> typedList;
	
	/**
	 * 
	 */
	public FunctionsDef(TypedList<AtomicFunctionSkeleton> typedList) {
		this.typedList = typedList;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		pw.print("(:functions ");
		writeInto(pw, typedList);
		pw.print(")");
	}

	public static FunctionsDef create(TypedList<AtomicFunctionSkeleton> typedList) {
		if (typedList == null) {
			throw new IllegalArgumentException("TypedList should not be null.");
		}
		return new FunctionsDef(typedList);
	}
}
