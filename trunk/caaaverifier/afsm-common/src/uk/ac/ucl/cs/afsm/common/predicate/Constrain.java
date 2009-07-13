/**
 * 
 */
package uk.ac.ucl.cs.afsm.common.predicate;

import static uk.ac.ucl.cs.afsm.common.predicate.Operator.*;

/**
 * @author -RAX- (Michele Sama)
 *
 */
public class Constrain implements Predicate {

	public final Predicate required;
	public final Predicate requiree;
	
	/**
	 * @param required
	 * @param requiree
	 */
	public Constrain(Predicate required, Predicate requiree) {
		this.required = required;
		this.requiree = requiree;
	}
	
	public static Constrain createAThenB(Variable required, Variable requiree) {
		return new Constrain(required, requiree);
	}
	
	public static Constrain createAThenNotB(Variable required, Variable requiree) {
		return new Constrain(required, not(requiree));
	}
	
	public static Constrain createNotAThenB(Variable required, Variable requiree) {
		return new Constrain(not(required), requiree);
	}
	
	public static Constrain createNotAThenNotB(Variable required, Variable requiree) {
		return new Constrain(not(required), not(requiree));
	}
}
