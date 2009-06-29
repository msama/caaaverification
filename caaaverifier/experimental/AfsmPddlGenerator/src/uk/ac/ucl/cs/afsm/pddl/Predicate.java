/**
 * 
 */
package uk.ac.ucl.cs.afsm.pddl;

import java.util.*;

/**
 * @author rax
 *
 */
public interface Predicate extends Visitable {
	
	public abstract Map<String, Variable> getVariables();

	public abstract Set<String> getVariableTypes();
	
	public String getUniqueName();
}
