/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix;

/**
 * @author rax
 *
 */
public class Rule {

	public static final int DEFAULT_PRIORITY=5;
	public static final int MAX_PRIORITY=0;
	public static final int LOW_PRIORITY=50;
	
	private String name;
	
	private int priority = Rule.DEFAULT_PRIORITY;
	
	/**
	 * Describes the condition on which this rule is applied.
	 */
	private Predicate condition;
	
	/**
	 * Describes the effect of this rule in the form of a predicate. 
	 */
	private Predicate appliedAction;

	public Rule(String name) {
		super();
		this.setName(name);
	}

	/**
	 * @param _name the _name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the _name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the predicate that is evaluated to trigger the rule
	 * @param condition the condition to set
	 */
	public void setCondition(Predicate condition) {
		this.condition = condition;
	}

	/**
	 * Get the predicate that is evaluated to trigger the rule
	 * @return the _predicate
	 */
	public Predicate getCondition() {
		return condition;
	}

	/**
	 * @param _priority the _priority to set
	 * @throws RuntimeException if priority is not between [Rule.LOW_PRIORITY, Rule.MAX_PRIORITY]
	 */
	public void setPriority(int priority) {
		if (priority < Rule.MAX_PRIORITY || priority > Rule.LOW_PRIORITY) { 
			throw new IllegalArgumentException("Wrong priority: " + priority + ". " +
				"Priority must be between ["+Rule.LOW_PRIORITY+","+Rule.MAX_PRIORITY+"]");
		}
		this.priority = priority;
	}

	/**
	 * @return the _priority
	 */
	public int getPriority() {
		return priority;
	}
	
	public boolean hasHigherPriority(Rule r)
	{
		return priority < r.priority;
	}
	
	public boolean hasLowerPriority(Rule r)
	{
		return priority > r.priority;
	}
	
	/**
	 * @param priority
	 * @return
	 */
	public boolean hasHigherPriority(int priority)
	{
		return this.priority<priority;
	}
	
	/**
	 * @param priority
	 * @return
	 */
	public boolean hasLowerPriority(int priority)
	{
		return this.priority>priority;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if ((obj instanceof Rule) == false) {
			return false;
		}
		Rule r=(Rule)obj;
		return name.equals(r.name);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * @return the appliedAction
	 */
	public Predicate getAppliedAction() {
		return appliedAction;
	}

	/**
	 * @param appliedAction the appliedAction to set
	 */
	public void setAppliedAction(Predicate appliedAction) {
		this.appliedAction = appliedAction;
	}
	
}
