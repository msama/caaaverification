/**
 * 
 */
package uk.ac.ucl.cs.afsm.pddl;

/**
 * @author rax
 *
 */
public abstract class Variable implements Visitable {

	private String name;
	
	private String type;
	
	/**
	 * 
	 */
	public Variable(String type, String name) {
		if ("".equals(name)) {
			throw new IllegalArgumentException("Name cannot be null or empty.");
		}
		if ("".equals(type)) {
			throw new IllegalArgumentException("Type cannot be null or empty.");
		}
		this.type = type;
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.afsm.pddl.Visitable#accept(uk.ac.ucl.cs.afsm.pddl.AfsmVisitor)
	 */
	@Override
	public void accept(AfsmVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Variable)) {
			return false;
		}
		Variable var = (Variable) obj;
		return type.equals(var.type) && name.equals(var.name);
	}

}
