/**
 * 
 */
package uk.ac.ucl.cs.afsm.pddl;

/**
 * Defines that classes implementing this interface are capable of
 * accepting an {@link AfsmVisitor}.
 * 
 * @author rax
 *
 */
public interface Visitable {

	public void accept(AfsmVisitor visitor);
	
}
