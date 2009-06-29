/**
 * 
 */
package uk.ac.ucl.cs.afsm.common;

import uk.ac.ucl.cs.afsm.common.predicate.Variable;

/**
 * Represent the concept of assigning a value to a {@link Variable}.
 * 
 * @author -RAX- (Michele Sama)
 *
 */
public class Assignment {

	protected Variable variable;	

	protected Assignment() {
	}
	
	protected Assignment(Variable variable) {
		this();
		this.variable = variable;
	}

	/**
	 * @return the variable
	 */
	public Variable getVariable() {
		return variable;
	}

	/**
	 * @param variable the variable to set
	 */
	public void setVariable(Variable variable) {
		this.variable = variable;
	}

	public static Assignment negate(Variable variable) {
		return new Negate(variable);
	}
	
	public static Assignment satisfy(Variable variable) {
		return new Satisfy(variable);
	}
	
	public static final class Negate extends Assignment {
		public Negate() {
		}
		
		public Negate(Variable variable) {
			this.variable = variable;
		}
	}
	
	public static final class Satisfy extends Assignment {
		public Satisfy() {
		}
		
		public Satisfy(Variable variable) {
			this.variable = variable;
		}
	}

}


