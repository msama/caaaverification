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
public class MultivaluedContextVariable <T> extends ContextVariable {

	protected Vector<T> _values=new Vector<T>();
	protected Vector<Integer> _sizes=new Vector<Integer>();
	
	protected Vector<ActivationObject<T>> _valueEquals=new Vector<ActivationObject<T>>();
	protected Vector<ActivationObject<T>> _valueDifferent=new Vector<ActivationObject<T>>();
	
	
	protected Vector<ActivationValue> _sizeEquals=new Vector<ActivationValue>();
	protected Vector<ActivationValue> _sizeDifferent=new Vector<ActivationValue>();
	protected Vector<ActivationValue> _sizeLesser=new Vector<ActivationValue>();
	protected Vector<ActivationValue> _sizeGreater=new Vector<ActivationValue>();
	
	
	
	public MultivaluedContextVariable(String _name, long refresh) {
		super(_name, refresh);
	}

	protected void addValue(T value)
	{
		if(this._values.contains(value))return;
		this._values.addElement(value);
	}
	
	protected void addSize(int n)
	{
		if(n<0){throw new RuntimeException("Size must be >= 0.");}
		
		Integer it=new Integer(n);
		if(this._sizes.contains(it)==false)
		{
			boolean flag=false;
			for(int i=0;i<this._sizes.size();i++)
			{
				if(n<this._sizes.elementAt(i).intValue())
				{
					this._sizes.add(i, it);
					flag=true;
					break;
				}
			}
			if(flag==false)
			{
				this._sizes.addElement(it);
			}
		}
	}
	
	public void addConditionEquals(PredicateVariable var, T value)
	{
		this._valueEquals.addElement(new ActivationObject<T>(var, value));
		this.addValue(value);
		super.addVariable(var);
	}
	
	public void addConditionDifferent(PredicateVariable var, T value)
	{
		this._valueDifferent.addElement(new ActivationObject<T>(var, value));
		this.addValue(value);
		super.addVariable(var);
	}
	
	
	public void addConditionSizeEquals(PredicateVariable v, int n)
	{
		if(n<0){throw new RuntimeException("Size must be >= 0.");}
		this._sizeEquals.addElement(new ActivationValue(v,n));
		this.addSize(n);
		super.addVariable(v);
	}
	
	public void addConditionSizeDifferent(PredicateVariable v, int n)
	{
		if(n<0){throw new RuntimeException("Size must be >= 0.");}
		this._sizeDifferent.addElement(new ActivationValue(v,n));
		this.addSize(n);
		super.addVariable(v);
	}
	
	public void addConditionSizeGreater(PredicateVariable v, int n)
	{
		if(n<0){throw new RuntimeException("Size must be >= 0.");}
		this._sizeGreater.addElement(new ActivationValue(v,n));
		this.addSize(n);
		super.addVariable(v);
	}
	
	public void addConditionSizeLesser(PredicateVariable v, int n)
	{
		if(n<0){throw new RuntimeException("Size must be >= 0.");}
		this._sizeLesser.addElement(new ActivationValue(v,n));
		this.addSize(n);
		super.addVariable(v);
	}
	
	public void addConditionSizeGreaterEquals(PredicateVariable v, int n)
	{
		if(n<0){throw new RuntimeException("Size must be >= 0.");}
		ActivationValue av=new ActivationValue(v,n);
		this._sizeEquals.addElement(av);
		this._sizeGreater.addElement(av);
		this.addSize(n);
		super.addVariable(v);
	}
	
	public void addConditionSizeLesserEquals(PredicateVariable v, int n)
	{
		if(n<0){throw new RuntimeException("Size must be >= 0.");}
		ActivationValue av=new ActivationValue(v,n);
		this._sizeEquals.addElement(av);
		this._sizeLesser.addElement(av);
		this.addSize(n);
		super.addVariable(v);
	}

	public char[][] generateInputSpace(int spaceSize)
	{
		//create a clone of the sizes vector which will be filled with additional values which needs to be considered
		//this is required since the number of element is decimal
		Vector<Integer> sizeClone=(Vector<Integer>) this._sizes.clone();

		//add all the integers between o and the number of variable
		//values are inserted in the right order
		for(int i=0;i<this._values.size();i++)
		{
			Integer it=new Integer(i);
			if(sizeClone.contains(it)==false)
			{
				boolean flag=false;
				for(int j=0;j<sizeClone.size();j++)
				{
					if(i<sizeClone.elementAt(j).intValue())
					{
						sizeClone.add(j, it);
						flag=true;
						break;
					}
				}
				if(flag==false)
				{
					sizeClone.addElement(it);
				}
			}
		}
		
		
		int counter=0;
		int[] entryForValues=new int[sizeClone.size()];
		//count the possible combinations of variables
		for(int i=0;i<sizeClone.size();i++)
		{
			int value=sizeClone.elementAt(i);
			//if the number of supposed elements is grater of the number of declared element we need to consider all the possibilities
			if(value>this._values.size())
			{
				value=this._values.size();
			}
			//add all the possible combination of known values
			long l = binomialSeries(value, this._values.size());
			//add a combination for empty cells
			l+=1;
			if(l>Integer.MAX_VALUE)throw new RuntimeException("Combination numbers exceed integer");
			counter+=(int)l;
			entryForValues[i]=(int)l;
		}
		
		//initialise the array with '*'
		char c[][]=new char[counter][spaceSize];
		for(int i=0;i<c.length;i++)
		{
			for(int j=0;j<c[i].length;j++)
			{
				c[i][j]='*';
			}
		}
		
		//for each line in c[][]
		for(int i=0;i<sizeClone.size();i++)
		{
			int value=sizeClone.elementAt(i).intValue();
			for(int j=0;j<entryForValues[i];j++)
			{
				//first set size variables
				//++++++++++++++++++++++++++
				
				//different are only false when is equals to current
				for(ActivationValue av:this._sizeDifferent)
				{
					if(c[i+j][av.getVariable().getIndexInSpace()]=='1')continue;
					c[i+j][av.getVariable().getIndexInSpace()]=(av.getValue()==value?'0':'1');
				}
				//equals are true only when the value equals to current
				for(ActivationValue av:this._sizeEquals)
				{
					if(c[i+j][av.getVariable().getIndexInSpace()]=='1')continue;
					c[i+j][av.getVariable().getIndexInSpace()]=(av.getValue()==value?'1':'0');
				}
				//lesser are true when lesser than current
				for(ActivationValue av:this._sizeLesser)
				{
					if(c[i+j][av.getVariable().getIndexInSpace()]=='1')continue;
					c[i+j][av.getVariable().getIndexInSpace()]=(value<av.getValue()?'1':'0');
				}
				//greater are true when greater than previous
				for(ActivationValue av:this._sizeGreater)
				{
					if(c[i+j][av.getVariable().getIndexInSpace()]=='1')continue;
					c[i+j][av.getVariable().getIndexInSpace()]=(av.getValue()<value?'1':'0');
				}
				
				//TODO set possible combination of values
				throw new RuntimeException("Not implemented yet");
			}
		}
		
		return c;
	}


	
    /** 
    * @return n!
    * precondition: n >= 0 and n <= 20
    */
    public static long factorial(int n) {
        if      (n <  0) throw new RuntimeException("Underflow error in factorial");
        else if (n > 20) throw new RuntimeException("Overflow error in factorial");
        else if (n == 0) return 1;
        long l=1;
    	for(int i=2;i<n;i++)
    	{
    		l*=i;
    	}
    	return l;
    }
    
    
    public static long binomial(int size,int poolSize)
    {
    	if(size<0)throw new RuntimeException("size must be >= 0");
    	if(poolSize<0)throw new RuntimeException("poolSize must be >= 0");
    	if(size>poolSize)throw new RuntimeException("size must be < than poolSize");
    	return factorial(poolSize)/(factorial(size)*factorial(poolSize-size));
    }
    
    public static long binomialSeries(int maxSize, int poolSize)
    {
    	if(maxSize<0)throw new RuntimeException("maxSize must be >= 0");
    	if(poolSize<0)throw new RuntimeException("poolSize must be >= 0");
    	if(maxSize>poolSize)throw new RuntimeException("maxSize must be < than poolSize");
    	long l=0;
    	for(int i=0;i<maxSize;i++)
    	{
    		l+=binomial(i,poolSize);
    	}
    	return l;
    }
    
}
