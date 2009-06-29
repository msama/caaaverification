/**
 * 
 */
package uk.ac.ucl.cs.caaa.classifier;

/**
 * Specify when a {@link ConditionedClockedExecutor} should compute or not.
 * 
 * @author rax
 *
 */
public interface Condition {

	public void setOwner(ConditionedClockedExecutor executor);
	public boolean compute();
	
}
