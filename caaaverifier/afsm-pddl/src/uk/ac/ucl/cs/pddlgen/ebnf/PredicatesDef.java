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
public class PredicatesDef extends Streamable {

	protected List<AtomicFormulaSkeleton> atomicFormulaSkeletons = new ArrayList<AtomicFormulaSkeleton>();

	private PredicatesDef() {
		
	}

	@Override
	protected void printInternal() {
		pw.print("(:predicates");
		++alignment;
		for (AtomicFormulaSkeleton skeleton : atomicFormulaSkeletons) {
			align();
			skeleton.printToStream(pw);
		}
		--alignment;
		align();
		pw.print(")");
	}

	public static PredicatesDef create(List<AtomicFormulaSkeleton> atomicFormulaSkeletons) {
		if (atomicFormulaSkeletons == null) {
			throw new IllegalStateException(
					"Statement <predicates-def> must contain a list of <atomic-formula-skeleton>s.");
		}
		if (atomicFormulaSkeletons.size() < 1) {
			throw new IllegalStateException(
					"Statement <predicates-def> must contain at least an <atomic-formula-skeleton>.");
		}
		PredicatesDef predicates = new PredicatesDef();
		predicates.atomicFormulaSkeletons = atomicFormulaSkeletons;
		return predicates;
	}
	
	
}
