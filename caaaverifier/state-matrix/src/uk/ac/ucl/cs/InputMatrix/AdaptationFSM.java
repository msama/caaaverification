/**
 * This class represent the whole A-FSM
 * It contains a vector of states, a pointer to the initial state and it is able to evaluate the total number of variables
 * 
 */
package uk.ac.ucl.cs.InputMatrix;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rax
 *
 */

/********************************************************\
Example of FSM description:

Name = "Adaptation FSM"
States = {s0-s3}
Alphabet = {A3,D3,A4,D4}
Start = s0
Forbidden = {s3}
Transition = (s0,A3)->s1 (s0,A4)->s2 (s1,A4)->s3 (s1,D3)->s0 (s2,A3)->s3 (s2,D4)->s0           
Trigger = s0-(T1,T2) s1-(T1,T2,T3) s2-(T1,T2,T4)
\*********************************************************/
// TODO: 1. write a simple grammar for FSM; 2. use SableCC to generate parser
// may have to relate to with concrete predicates with A3,D3,...
 
public class AdaptationFSM {
	
	private List<State> states = new ArrayList<State>();
	private List<ContextVariable> _contextVariables = new ArrayList<ContextVariable>();
	private List<PredicateVariable> _variables = new ArrayList<PredicateVariable>();
	private List<Constraint> _constraints = new ArrayList<Constraint>();
	private State _initialState;
	
	
	/**
	 * 
	 */
	public AdaptationFSM() {
		super();
	}


	public void setInitialState(State _initialState) {
		this._initialState = _initialState;
	}


	public State getInitialState() {
		return _initialState;
	}


	public void addState(State obj){
		if(this.states.contains(obj)){
			throw new RuntimeException("State "+obj+" already inserted");
		}//item already in set
		states.add(obj);
	}


	public State stateAt(int index) {
		return states.get(index);
	}


	public int indexOfState(Object o) {
		return states.indexOf(o);
	}


	public boolean removeState(Object obj) {
		boolean flag=states.remove(obj);
		//after removing a state the global size of input must be reloaded
		if(flag==true)
		{
			//TODO check variables
		}
		//initial state can be reassigned.
		if(this.getInitialState()!=null&&this.getInitialState().equals(obj))
		{
			this.setInitialState(null);
		}
		return flag;
	}



	public int stateSize() {
		return states.size();
	}



	/**
	 * @return the _inputDimension
	 */
	public int getInputDimension() {
		return this._variables.size();
	}


	/**
	 * Creates a new variable and updates the dimension of the input space
	 * @param name
	 * @param cv COntextVariable on which the Variable is making queries.
	 * @throws Exception if variable name is not unique and != null
	 */
	public PredicateVariable createVariable(String name, ContextVariable cv)
	{
		if(name==null)
		{
			throw new RuntimeException("Variable name cannot be null.");
		}
		if(this._contextVariables.contains(cv)==false)
		{
			throw new RuntimeException("ContextVariable is not recognized, add it first.");
		}
		for(PredicateVariable v:this._variables)
		{
			if(v.getName().equals(name))
			{
				throw new RuntimeException("Duplicated variable name: "+name+".");
			}
		}
		PredicateVariable v=new PredicateVariable(name,this._variables.size(),cv);
		this._variables.add(v);
		return v;
	}
	
	public PredicateVariable getVariableByName(String name)
	{
		if(name==null)
		{
			return null;
		}
		for(PredicateVariable v:this._variables)
		{
			if(v.getName().equals(name))
			{
				return v;
			}
		}
		return null;
	}
	
	
	public void loadInputSpaces()
	{
		Constraint[] c = this._constraints.toArray(
				new Constraint[this._constraints.size()]);
		for(State s : this.states)
		{
			s.createInputSpace(this._variables.size(),c);
		}
	}


	/**
	 * @param cv
	 */
	public void addContextVariable(ContextVariable cv) {
		_contextVariables.add(cv);
	}
	
	/**
	 * @return
	 */
	public int getContextVariablesCount()
	{
		return this._contextVariables.size();
	}
	
	/**
	 * @param i
	 * @return
	 */
	public ContextVariable getContextVariableAt(int i)
	{
		return this._contextVariables.get(i);
	}
	
	public void addConstrain(Constraint c)
	{
		this._constraints.add(c);
	}
	
}
