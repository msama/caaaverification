/**
 * 
 */
package uk.ac.ucl.cs.afsm.pddl;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author rax
 *
 */
public abstract class VariablePredicate implements Predicate {

	@Override
	public void accept(AfsmVisitor visitor) {
		visitor.visit(this);		
	}

	@Override
	public Map<String, Variable> getVariables() {
		
		Map<String, Variable> variables = new HashMap<String, Variable>();
		Field[] fields = getClass().getDeclaredFields();
		try {
			for(Field f : fields) {
				if (Variable.class.isAssignableFrom(f.getType())) {
					if (!f.isAccessible()) {
						f.setAccessible(true);
					}
					Variable v = (Variable) f.get(this);
					variables.put(v.getName(), v);
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return variables;
	}
	
	public Set<String> getVariableTypes() {
		Set<String>  types = new HashSet<String>();
		Field[] fields = getClass().getDeclaredFields();
		try {
			for(Field f : fields) {
				if (Variable.class.isAssignableFrom(f.getType())) {
					if (!f.isAccessible()) {
						f.setAccessible(true);
					}
					Variable v = (Variable) f.get(this);
					types.add(v.getType());
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return types;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof VariablePredicate)) {
			return false;
		}
		VariablePredicate predicate = (VariablePredicate) obj;
		return getUniqueName().equals(predicate.getUniqueName()); 
	}
}
