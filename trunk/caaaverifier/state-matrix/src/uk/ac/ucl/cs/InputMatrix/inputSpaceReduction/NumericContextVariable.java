/**
 * This class reduce the number of possible combination of input for N conditions from 2^n to 2*n+1
 */
package uk.ac.ucl.cs.InputMatrix.inputSpaceReduction;

import java.util.Vector;

import uk.ac.ucl.cs.InputMatrix.ContextVariable;
import uk.ac.ucl.cs.InputMatrix.PredicateVariable;

/**
 * @author rax
 *
 */
public class NumericContextVariable<T> extends ContextVariable {

	protected Vector<Double> _values=new Vector<Double>();
	
	protected Vector<ActivationValue> _equals=new Vector<ActivationValue>();
	protected Vector<ActivationValue> _different=new Vector<ActivationValue>();
	protected Vector<ActivationValue> _lesser=new Vector<ActivationValue>();
	protected Vector<ActivationValue> _greater=new Vector<ActivationValue>();
	protected Vector<ActivationRange> _included=new Vector<ActivationRange>();
	protected Vector<ActivationRange> _excluded=new Vector<ActivationRange>();
	
	
	public NumericContextVariable(String _name, long refresh) {
		super(_name, refresh);
	}

	protected void addValue(double d)
	{
		Double db=new Double(d);
		if(this._values.contains(db)==false)
		{
			boolean flag=false;
			for(int i=0;i<this._values.size();i++)
			{
				if(d<this._values.elementAt(i).doubleValue())
				{
					this._values.add(i, db);
					flag=true;
					break;
				}
			}
			if(flag==false)
			{
				this._values.addElement(db);
			}
		}
	}
	
	public void addConditionEquals(PredicateVariable v, double d)
	{
		this._equals.addElement(new ActivationValue(v,d));
		this.addValue(d);
		super.addVariable(v);
	}
	
	public void addConditionDifferent(PredicateVariable v, double d)
	{
		this._different.addElement(new ActivationValue(v,d));
		this.addValue(d);
		super.addVariable(v);
	}
	
	public void addConditionGreater(PredicateVariable v, double d)
	{
		this._greater.addElement(new ActivationValue(v,d));
		this.addValue(d);
		super.addVariable(v);
	}
	
	public void addConditionLesser(PredicateVariable v, double d)
	{
		this._lesser.addElement(new ActivationValue(v,d));
		this.addValue(d);
		super.addVariable(v);
	}
	
	public void addConditionGreaterEquals(PredicateVariable v, double d)
	{
		ActivationValue av=new ActivationValue(v,d);
		this._equals.addElement(av);
		this._greater.addElement(av);
		this.addValue(d);
		super.addVariable(v);
	}
	
	public void addConditionLesserEquals(PredicateVariable v, double d)
	{
		ActivationValue av=new ActivationValue(v,d);
		this._equals.addElement(av);
		this._lesser.addElement(av);
		this.addValue(d);
		super.addVariable(v);
	}
	
	public void addConditionIncluded(PredicateVariable v, double min, double max)
	{
		this._included.addElement(new ActivationRange(v,min,max));
		this.addValue(min);
		this.addValue(max);
		super.addVariable(v);
	}
	
	public void addConditionExcluded(PredicateVariable v, double min, double max)
	{
		this._excluded.addElement(new ActivationRange(v,min,max));
		this.addValue(min);
		this.addValue(max);
		super.addVariable(v);
	}

	
	/**
	 * Create all the possible input spaces according to the variables.
	 * the space is subdivided according to this._values
	 * An input is generated for each value and for each gap between values.
	 * The total number of values is reduced from 2^n to 2*n+1.
	 *
	 * The current implementation utilise a sliding window on the _values array; in each cycle it evaluates the gap between the previous value and the current one, and then only the current value.
	 */
	public char[][] generateInputSpace(int spaceSize)
	{
		char c[][]=new char[2*this._values.size()+1][spaceSize];
		for(int i=0;i<c.length;i++)
		{
			for(int j=0;j<c[i].length;j++)
			{
				c[i][j]='*';
			}
		}
		double previous=Double.MIN_VALUE;
		double current=Double.MIN_VALUE;
		for(int i=0;i<this._values.size();i++)
		{
			//fill two arrays for cycle
			previous=current;
			current=this._values.elementAt(i).doubleValue();
			//generating input for the previous gap
			//*************************************
			
			//different are always true in gap
			for(ActivationValue av:this._different)
			{
				if(c[2*i][av.getVariable().getIndexInSpace()]=='1')continue;
				c[2*i][av.getVariable().getIndexInSpace()]='1';
			}
			//equals are always false in gap
			for(ActivationValue av:this._equals)
			{
				if(c[2*i][av.getVariable().getIndexInSpace()]=='1')continue;
				c[2*i][av.getVariable().getIndexInSpace()]='0';
			}
			//lesser are true when > than current
			for(ActivationValue av:this._lesser)
			{
				if(c[2*i][av.getVariable().getIndexInSpace()]=='1')continue;
				c[2*i][av.getVariable().getIndexInSpace()]=(current<=av.getValue()?'1':'0');
			}
			//greater are true when < than previous
			for(ActivationValue av:this._greater)
			{
				if(c[2*i][av.getVariable().getIndexInSpace()]=='1')continue;
				c[2*i][av.getVariable().getIndexInSpace()]=(av.getValue()<=previous?'1':'0');
			}
			//included are true when min is >= than previous and max is <= than current
			for(ActivationRange ar:this._included)
			{
				if(c[2*i][ar.getVariable().getIndexInSpace()]=='1')continue;
				//the minimum has been passed ->  current>ar.getValueMin()&&previous>=ar.getValueMin()
				//the maximum has not been passed -> current<=ar.getValueMax()
				c[2*i][ar.getVariable().getIndexInSpace()]=(current>ar.getValueMin()&&previous>=ar.getValueMin()&&current<=ar.getValueMax()?'1':'0');
			}
			//excluded are true when min is >= than current or max is <= than previous
			for(ActivationRange ar:this._excluded)
			{
				if(c[2*i][ar.getVariable().getIndexInSpace()]=='1')continue;
				c[2*i][ar.getVariable().getIndexInSpace()]=(ar.getValueMin()>current||ar.getValueMax()<=previous?'1':'0');
			}
			
			
			
			//generating input for the current value
			//**********************************
				
			//different are only false when is equals to current
			for(ActivationValue av:this._different)
			{
				if(c[2*i+1][av.getVariable().getIndexInSpace()]=='1')continue;
				c[2*i+1][av.getVariable().getIndexInSpace()]=(av.getValue()==current?'0':'1');
			}
			//equals are true only when the value equals to current
			for(ActivationValue av:this._equals)
			{
				if(c[2*i+1][av.getVariable().getIndexInSpace()]=='1')continue;
				c[2*i+1][av.getVariable().getIndexInSpace()]=(av.getValue()==current?'1':'0');
			}
			//lesser are true when lesser than current
			for(ActivationValue av:this._lesser)
			{
				if(c[2*i+1][av.getVariable().getIndexInSpace()]=='1')continue;
				c[2*i+1][av.getVariable().getIndexInSpace()]=(current<av.getValue()?'1':'0');
			}
			//greater are true when greater than previous
			for(ActivationValue av:this._greater)
			{
				if(c[2*i+1][av.getVariable().getIndexInSpace()]=='1')continue;
				c[2*i+1][av.getVariable().getIndexInSpace()]=(av.getValue()<previous?'1':'0');
			}
			//included are true when min is >= than previous and max is <= than current
			for(ActivationRange ar:this._included)
			{
				if(c[2*i+1][ar.getVariable().getIndexInSpace()]=='1')continue;
				//the first value -> ar.getValueMin()==current
				//the last value -> ar.getValueMax()==current
				//values in the middle -> current>ar.getValueMin()&&current<ar.getValueMax()
				c[2*i+1][ar.getVariable().getIndexInSpace()]=((ar.getValueMin()==current)||(ar.getValueMax()==current)||(current>ar.getValueMin()&&current<ar.getValueMax())?'1':'0');
			}
			//excluded are true when min is >= than current or max is <= than previous
			for(ActivationRange ar:this._excluded)
			{
				if(c[2*i+1][ar.getVariable().getIndexInSpace()]=='1')continue;
				c[2*i+1][ar.getVariable().getIndexInSpace()]=(ar.getValueMin()>=current||ar.getValueMax()<=previous?'1':'0');
			}
				
		}
		//generating input for the last gap 
		previous=current;
		current=Double.MAX_VALUE;
		//different are always true in gap
		for(ActivationValue av:this._different)
		{
			if(c[2*this._values.size()][av.getVariable().getIndexInSpace()]=='1')continue;
			c[2*this._values.size()][av.getVariable().getIndexInSpace()]='1';
		}
		//equals are always false in gap
		for(ActivationValue av:this._equals)
		{
			if(c[2*this._values.size()][av.getVariable().getIndexInSpace()]=='1')continue;
			c[2*this._values.size()][av.getVariable().getIndexInSpace()]='0';
		}
		//lesser is always false
		for(ActivationValue av:this._lesser)
		{
			if(c[2*this._values.size()][av.getVariable().getIndexInSpace()]=='1')continue;
			c[2*this._values.size()][av.getVariable().getIndexInSpace()]='0';
		}
		//greater are true 
		for(ActivationValue av:this._greater)
		{
			if(c[2*this._values.size()][av.getVariable().getIndexInSpace()]=='1')continue;
			c[2*this._values.size()][av.getVariable().getIndexInSpace()]='1';
		}
		//included are false
		for(ActivationRange ar:this._included)
		{
			if(c[2*this._values.size()][ar.getVariable().getIndexInSpace()]=='1')continue;
			c[2*this._values.size()][ar.getVariable().getIndexInSpace()]='0';
		}
		//excluded are true 
		for(ActivationRange ar:this._excluded)
		{
			if(c[2*this._values.size()][ar.getVariable().getIndexInSpace()]=='1')continue;
			c[2*this._values.size()][ar.getVariable().getIndexInSpace()]='1';
		}
		
		return c;
	}
	
	
	
}


class ActivationValue
{
	
	PredicateVariable _variable;
	double _value;
	/**
	 * @param _variable
	 * @param _value
	 */
	public ActivationValue(PredicateVariable _variable, double _value) {
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
	public double getValue() {
		return _value;
	}

}

class ActivationRange
{
	
	PredicateVariable _variable;
	double _valueMin;
	double _valueMax;
	/**
	 * @param _variable
	 * @param min
	 * @param max
	 */
	public ActivationRange(PredicateVariable _variable, double min, double max) {
		super();
		this._variable = _variable;
		_valueMin = min;
		_valueMax = max;
	}
	/**
	 * @return the _variable
	 */
	public PredicateVariable getVariable() {
		return _variable;
	}
	/**
	 * @return the _valueMin
	 */
	public double getValueMin() {
		return _valueMin;
	}
	/**
	 * @return the _valueMax
	 */
	public double getValueMax() {
		return _valueMax;
	}
	
}