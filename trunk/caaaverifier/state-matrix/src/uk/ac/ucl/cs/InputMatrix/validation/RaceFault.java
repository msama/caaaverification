/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix.validation;

import java.util.Vector;

import uk.ac.ucl.cs.InputMatrix.Fault;
import uk.ac.ucl.cs.InputMatrix.Rule;
import uk.ac.ucl.cs.InputMatrix.State;

/**
 * @author rax
 *
 */
public class RaceFault extends Fault {

	protected State[] _states;
	protected Rule[] _rules;
	private boolean _cycle=false;
	
	
	/**
	 * @param _input
	 * @param _name
	 * @param _states
	 * @param _rules
	 */
	public RaceFault(String _input, State[] _states, Rule[] _rules, boolean cycle) {
		super(_input, "RaceAndCycle");
		this._states = _states;
		this._rules = _rules;
		this._cycle=cycle;
	}


	/**
	 * @return the _states
	 */
	public State[] getStates() {
		return _states;
	}


	/**
	 * @return the _rules
	 */
	public Rule[] getRules() {
		return _rules;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if((obj instanceof RaceFault)==false){return false;}
		RaceFault f=(RaceFault)obj;

		if(this.getInput().equals(f.getInput())==false){return false;}
		
		if(this._rules.length!=f._rules.length){return false;}
		
		if(this._states.length!=f._states.length){return false;}
		
		for(int i=0;i<this._rules.length;i++)
		{
			if(this._rules[i].getName().equals(f._rules[i].getName())==false){return false;}
		}
		
		for(int i=0;i<this._states.length;i++)
		{
			if(this._states[i].getName().equals(f._states[i].getName())==false){return false;}
		}
		
		return true;
	}


	/**
	 * @param _cycle the _cycle to set
	 */
	public void setCycle(boolean _cycle) {
		this._cycle = _cycle;
	}


	/**
	 * @return the _cycle
	 */
	public boolean isCycle() {
		return _cycle;
	}
	
	
	
}
