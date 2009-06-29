/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix;

/**
 * @author rax
 *
 */
public interface Predicate {

	public boolean getValue(int input);
	
	public PredicateVariable[] getVariables();
	
	public double getSatisfaction();
}
