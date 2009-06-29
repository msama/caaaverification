/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix.validation.actions;

import uk.ac.ucl.cs.InputMatrix.AdaptationFSM;

/**
 * Defines a strategy to explore the graph. The goal is to 
 * 
 * @author rax
 *
 */
public interface GraphTraversalDelegate {
	
	/**
	 * Generate the next input which should be used to
	 * explore the {@link AdaptationFSM}.
	 * 
	 * @return
	 */
	public int getNextInput();
}
