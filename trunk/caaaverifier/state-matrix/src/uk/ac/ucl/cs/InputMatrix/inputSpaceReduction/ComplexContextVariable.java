/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix.inputSpaceReduction;

import java.util.Vector;

import uk.ac.ucl.cs.InputMatrix.ContextVariable;
import uk.ac.ucl.cs.InputMatrix.PredicateVariable;

/**
 * @author rax
 *
 */
public class ComplexContextVariable<T> extends ContextVariable {

	protected Vector<T> _values=new Vector<T>();
	
	protected Vector<ActivationObject<T>> _equals=new Vector<ActivationObject<T>>();
	protected Vector<ActivationObject<T>> _different=new Vector<ActivationObject<T>>();
	
	public ComplexContextVariable(String _name, long refresh) {
		super(_name, refresh);
	}
	
	protected void addValue(T value)
	{
		if(this._values.contains(value))return;
		this._values.addElement(value);
	}
	
	
	public void addConditionEquals(PredicateVariable var, T value)
	{
		this._equals.addElement(new ActivationObject<T>(var, value));
		this.addValue(value);
		super.addVariable(var);
	}
	
	public void addConditionDifferent(PredicateVariable var, T value)
	{
		this._different.addElement(new ActivationObject<T>(var, value));
		this.addValue(value);
		super.addVariable(var);
	}
	

	public char[][] generateInputSpace(int spaceSize)
	{
		char c[][]=new char[this._values.size()+1][spaceSize];
		for(int i=0;i<c.length;i++)
		{
			for(int j=0;j<c[i].length;j++)
			{
				c[i][j]='*';
			}
		}
		for(int i=0;i<this._values.size();i++)
		{		
			//generating input for the current value
			//**********************************
				
			//different are only false when is equals to current
			for(ActivationObject ao:this._different)
			{
				if(c[i][ao.getVariable().getIndexInSpace()]=='1')continue;
				c[i][ao.getVariable().getIndexInSpace()]=(ao.getValue().equals(this._values.elementAt(i))?'0':'1');
			}
			//equals are true only when the value equals to current
			for(ActivationObject ao:this._equals)
			{
				if(c[i][ao.getVariable().getIndexInSpace()]=='1')continue;
				c[i][ao.getVariable().getIndexInSpace()]=(ao.getValue().equals(this._values.elementAt(i))?'1':'0');
			}	
		}
		//creates an input in which all the variables are false, eg values is different than any previous
		//different are true
		for(ActivationObject ao:this._different)
		{
			if(c[this._values.size()][ao.getVariable().getIndexInSpace()]=='1')continue;
			c[this._values.size()][ao.getVariable().getIndexInSpace()]='1';
		}
		//equals are false
		for(ActivationObject ao:this._equals)
		{
			if(c[this._values.size()][ao.getVariable().getIndexInSpace()]=='1')continue;
			c[this._values.size()][ao.getVariable().getIndexInSpace()]='0';
		}
		
		return c;
	}
	
	
}


class ActivationObject<T>
{
	PredicateVariable _variable;
	T _value;
	/**
	 * @param _variable
	 * @param _value
	 */
	public ActivationObject(PredicateVariable _variable, T _value) {
		super();
		this._variable = _variable;
		this._value = _value;
	}
	/**
	 * @return the _variable
	 */
	public PredicateVariable getVariable() {
		return _variable;
	}
	/**
	 * @return the _value
	 */
	public T getValue() {
		return _value;
	}

}