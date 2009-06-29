/**
 * This class contains information on which rule are active in a specific state.
 * Any instance must be initialized with a dimension and rules must be added separately.
 * 
 * Contains an hashtable of unstable inputs, e.g. inputs which satisfy at least one rule
 */
package uk.ac.ucl.cs.InputMatrix;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;



/**
 * @author rax
 *
 */
public class InputSpace {

	private int _spaceDimension=0;
	private double _maxValue=-1;
	
	private Map<Long,Set<Rule>> _unstableInputs=new HashMap<Long, Set<Rule>>();
	private Map<Long,Boolean> _forbiddenInputs=new HashMap<Long, Boolean>();

	public InputSpace(int dimension) {
		super();
		_spaceDimension = dimension;
		this._maxValue=Math.pow(2, this._spaceDimension);
	}

	/**
	 * This method is supposed to be invoked by the container state to initialize the space
	 * @param input a specific input
	 * @param rule the rule to add to that input
	 */
	public void addSatisfiedRule(long input, Rule rule)
	{
		if(input>this._maxValue){return;}
		Long l=new Long(input);
		
		Set<Rule> set = this._unstableInputs.get(l);
		if(set!=null)
		{
			set.add(rule);
		}else
		{
			set=new HashSet<Rule>();
			set.add(rule);
			this._unstableInputs.put(l, set);
		}
	}
	
	
	/**
	 * Method to query if a specific input stisfy rules in a specific state.
	 * @param input the input to query
	 * @return an Array of satisfied Rules
	 */
	public Rule[] getSatisfiedRules(long input)
	{
		assert(this.isForbiddenInput(input)==false);
		Set<Rule> set=this._unstableInputs.get(new Long(input));
		if(set!=null)
		{
			return set.toArray(new Rule[set.size()]);
		}else
		{
			return new Rule[0];
		}
	}

	/**
	 * @return an Enumeration of keys of unstable inputs
	 */
	public Set<Long> keySet() {
		return _unstableInputs.keySet();
	}

	public void setForbiddenInput(long l, boolean b) {
		this._forbiddenInputs.put(new Long(l), new Boolean(b));	
	}
	
	public boolean isForbiddenInput(long l)
	{
		Boolean b=this._forbiddenInputs.get(new Long(l));
		if(b==null){return false;}
		return b.booleanValue();
	}
	
}
