/**
 * 
 */
package uk.ac.ucl.cs.afsm.common.predicate;

import java.util.List;
import java.util.Set;

import uk.ac.ucl.cs.afsm.common.Context;

/**
 * @author -RAX- (Michele Sama)
 *
 */
public class Variable implements Predicate {

	private String name;
	
	private Context context;
	
	public Variable() {
	}
	
	/**
	 * @param name
	 */
	public Variable(String name, Context context) {
		this();
		this.name = name;
		this.context = context;
	}

	/**
	 * State if two instances of {@link Variable} identifies the same variable
	 * by comparing their names.
	 * 
	 * @param obj the instance with which this instance should be compared.
	 * @return <code>true</code> if <code>obj</code> is a {@link Variable} and neither
	 * 	its name or the name of the current instance are <code>null</code>,
	 * 	<code>false</code> otherwise.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Variable)) {
			return false;
		}
		Variable v = (Variable) obj;
		if (name == null || v.name == null) {
			return false;
		}
		return name.equals(v.name);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(Context context) {
		this.context = context;
	}
	
	public static void getAllVariables(Predicate predicate, Set<Variable> set) {
		if (predicate == Constant.TRUE) {
			return;
		} else if (predicate == Constant.FALSE) {
			return;
		} if (predicate instanceof Variable) {
			set.add((Variable)predicate);
			return;
		} else if (predicate instanceof Operator.Not) {
			Operator.Not not = (Operator.Not) predicate;
			getAllVariables(not.getPredicate(), set);
		} else if (predicate instanceof Operator.And) {
			Operator.And and = (Operator.And) predicate;
			for (int i = 0; i < and.size(); i++) {
				getAllVariables(and.get(i), set);
			}
		} else if (predicate instanceof Operator.Or) {
			Operator.Or or = (Operator.Or) predicate;
			for (int i = 0; i < or.size(); i++) {
				getAllVariables(or.get(i), set);
			}
		}
	}
}
