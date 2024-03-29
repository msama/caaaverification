/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class AtomicFormulaSkeleton extends Streamable {

	private Predicate predicate;
	
	private TypedList<Variable> typedList;
	
	private AtomicFormulaSkeleton() {
		
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		pw.print("(");
		writeInto(pw, predicate);
		pw.print(" ");
		writeInto(pw, typedList);
		pw.print(")");
	}

	public static AtomicFormulaSkeleton create(Predicate predicate,
			TypedList<Variable> typedList) {
		if (predicate == null) {
			throw new IllegalStateException(
					"Statement <atomic-formula-skeleton> must have a <predicate>.");
		}
		if (typedList == null) {
			throw new IllegalStateException(
					"Statement <atomic-formula-skeleton> must have a <typed list (variable)>.");
		}
		AtomicFormulaSkeleton skeleton = new AtomicFormulaSkeleton();
		skeleton.predicate = predicate;
		skeleton.typedList = typedList;
		return skeleton;
	}
}
