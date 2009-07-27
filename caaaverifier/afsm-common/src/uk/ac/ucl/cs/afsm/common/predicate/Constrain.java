/**
 * 
 */
package uk.ac.ucl.cs.afsm.common.predicate;

import static uk.ac.ucl.cs.afsm.common.predicate.Operator.*;

/**
 * @author -RAX- (Michele Sama)
 *
 */
public abstract class Constrain implements Predicate {

	public final Variable required;
	public final Variable requiree;
	
	/**
	 * @param required
	 * @param requiree
	 */
	protected Constrain(Variable required, Variable requiree) {
		this.required = required;
		this.requiree = requiree;
	}
	
	public static Constrain createAThenB(Variable required, Variable requiree) {
		return new AThenB(required, requiree);
	}
	
	public static Constrain createAThenNotB(Variable required, Variable requiree) {
		return new AThenNotB(required, requiree);
	}
	
	public static Constrain createNotAThenB(Variable required, Variable requiree) {
		return new NotAThenB(required, requiree);
	}
	
	public static Constrain createNotAThenNotB(Variable required, Variable requiree) {
		return new NotAThenNotB(required, requiree);
	}
	
	public static class AThenB extends Constrain {
		protected AThenB(Variable required, Variable requiree) {
			super(required, requiree);
		}
	}
	
	public static class NotAThenNotB extends Constrain {
		protected NotAThenNotB(Variable required, Variable requiree) {
			super(required, requiree);
		}
	}
	
	public static class AThenNotB extends Constrain {
		protected AThenNotB(Variable required, Variable requiree) {
			super(required, requiree);
		}
	}
	
	public static class NotAThenB extends Constrain {
		protected NotAThenB(Variable required, Variable requiree) {
			super(required, requiree);
		}
	}
	

}
