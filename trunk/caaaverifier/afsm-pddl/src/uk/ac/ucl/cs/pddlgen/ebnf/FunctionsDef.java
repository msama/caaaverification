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
	private FunctionsDef(TypedList<AtomicFunctionSkeleton> typedList) {
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
		if (!definedKeys.contains(RequireKey.FLUENTS)) {
			throw new IllegalStateException(":fluents must be defined.");
		}
		if (typedList == null) {
			throw new IllegalArgumentException("TypedList should not be null.");
		}
		return new FunctionsDef(typedList);
	}
}
