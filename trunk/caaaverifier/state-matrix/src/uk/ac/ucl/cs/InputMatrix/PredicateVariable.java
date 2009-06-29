/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix;

/**
 * Define a single argument boolean equivalence such as <code>a == b</code> or 
 * <code>a == true</code>. Each {@link PredicateVariable} is added to the {@link AdaptationFSM} which 
 * assign it to an index in the {@link InputSpace}. The generated input will contain at least
 * one value satisfying and one value not satisfying the predicate and all of their combinations.
 * 
 * @author rax
 *
 */
public class PredicateVariable implements Predicate {

	private int indexInSpace = 0;
	private String name;
	private ContextVariable contextVariable;
	
	public PredicateVariable(String name, int indexInSpace, ContextVariable contextVariable) {
		super();
		this.name = name;
		this.indexInSpace = indexInSpace;
		this.contextVariable = contextVariable;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.InputMatrix.BooleanPredicate#getValue()
	 */
	public boolean getValue(int input) {
		int mask = 1 << indexInSpace;
		input = mask & input;
		return input != 0;
	}

	/**
	 * @param _name the _name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the _name
	 */
	public String getName() {
		return name;
	}

	public PredicateVariable[] getVariables() {
		return new PredicateVariable[]{this};
	}

	/**
	 * @return the _indexInSpace
	 */
	public int getIndexInSpace() {
		return indexInSpace;
	}

	public ContextVariable getContextVariable() {
		return contextVariable;
	}

	@Override
	public String toString() {
		return getName();
	}

	/**
	 * Returns the satisfaction for a Variable which is always 0.5
	 * because into a 0-1 space a variable has 50% probability to 
	 * be satisfied.
	 * 
	 * @see uk.ac.ucl.cs.InputMatrix.Predicate#getSatisfaction()
	 */
	@Override
	public double getSatisfaction() {
		return 0.5;
	}

	/**
	 * Factory method for {@link PredicateVariable}s
	 * 
	 * @param afsm
	 * @param name
	 * @param cv
	 * @return
	 */
	public static Predicate variable(AdaptationFSM afsm, String name, ContextVariable cv) {
		return afsm.createVariable(name, cv);
	}
}
