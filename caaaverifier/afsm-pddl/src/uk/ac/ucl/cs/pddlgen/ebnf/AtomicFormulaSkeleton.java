/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class AtomicFormulaSkeleton extends Streamable {

	FunctionSymbol functionSymbol;
	
	TypedList<Variable> typedList;
	
	private AtomicFormulaSkeleton() {
		
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		pw.print("(");
		writeInto(pw, functionSymbol);
		pw.print(" ");
		writeInto(pw, typedList);
		pw.print(")");
	}

	public static AtomicFormulaSkeleton create(FunctionSymbol functionSymbol,	TypedList<Variable> typedList) {
		if (functionSymbol == null) {
			throw new IllegalStateException(
					"Statement <atomic-formula-skeleton> must have a <predicate>.");
		}
		if (typedList == null) {
			throw new IllegalStateException(
					"Statement <atomic-formula-skeleton> must have a <typed list (variable)>.");
		}
		AtomicFormulaSkeleton skeleton = new AtomicFormulaSkeleton();
		skeleton.functionSymbol = functionSymbol;
		skeleton.typedList = typedList;
		return skeleton;
	}
}
