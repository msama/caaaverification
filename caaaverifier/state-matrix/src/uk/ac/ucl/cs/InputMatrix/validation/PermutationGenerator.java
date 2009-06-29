/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix.validation;

/**
 * @author michsama
 *
 */
public class PermutationGenerator
{
	protected int _setSize=0;
	protected int _subsetSize=0;
	protected int[] _currentArray=null;
	protected int[] _targetArray=null;

	
	/**
	 * @param size
	 */
	public PermutationGenerator(int size) {
		super();
		_setSize = size;
		_subsetSize = 2;
		//this.initialize();
	}

	protected void initialize()
	{
		assert(this._setSize>=this._subsetSize);
		this._currentArray=new int[this._subsetSize];
		this._targetArray=new int[this._subsetSize];
		for(int i=0;i<this._subsetSize;i++)
		{
			this._currentArray[i]=i;
			this._targetArray[i]=this._subsetSize-1-i;
		}
	}
	
	protected int getSmallestAvilable(int index)
	{
		assert(index<this._subsetSize);
		int k=-1;
		for(int j=0;j<this._setSize;j++){
			boolean available=true;
			for(int i=0;i<index;i++)
			{
				if(this._currentArray[i]==j)
				{
					available=false;
					break;
				}
			}
			if(available==true)
			{
				k=j;
				break;
			}
		}
		assert(k>=0&&k<this._subsetSize);
		return k;
	}
	
	protected boolean valueInUse(int value,int index)
	{
		assert(index<this._subsetSize);
		assert(value<this._setSize);
		for(int i=0;i<index;i++)
		{
			if(this._currentArray[i]==value)
			{
				return true;
			}
		}
		return false;
	}
	
	protected boolean areEquals(int[] v1, int[] v2, int index)
	{
		assert(index<=v1.length);
		assert(v1.length==v2.length);
		assert(index>=0);
		assert(index<=v1.length);
		for(int i=0;i<index;i++)
		{
			if(v1[i]!=v2[i])
			{
				return false;
			}
		}
		return true;
	}
	
	public int[] getNext()
	{
		return this.getNext(this._subsetSize-1);
	}
	
	public int[] getNext(int index)
	{
		assert(index>=0);
		if(index>=this._subsetSize){index=this._subsetSize-1;}
		/*if(index==-1)
		{
			changeSubset=true;
		}*/
		
		if(this._currentArray==null){
			this.initialize();
			return this._currentArray.clone();
		}
		
		boolean changeSubset=false;
		
		
		assert(this.hasNext()==true);
		//boolean changeIndex=false;
		
		//int index=this._subsetSize-1;
		int value=-1;
		
		do
		{
			//changeIndex=false;
			value=this._currentArray[index];
			do
			{
				value++;
			}while(value<this._setSize&&this.valueInUse(value, index));
			if(value>=this._setSize)
			{
				//changeIndex=true;
				index--;
				if(index==-1){changeSubset=true;}
				continue;
			}else
			{
				this._currentArray[index]=value;
				for(int i=index+1;i<this._subsetSize;i++)
				{
					this._currentArray[i]=this.getSmallestAvilable(i);
				}
				return this._currentArray.clone();
			}
		}while(changeSubset==false);
		
		this._subsetSize++;
		assert(this._subsetSize<=this._setSize);
		this.initialize();
		
		return this._currentArray.clone();
	}
	
	
	public boolean hasNext()
	{
		return this.hasNext(0);
	}
	
	public boolean hasNext(int index)
	{
		assert(index>=0);
		if(this._currentArray==null){return true;}
		if(this._subsetSize<this._setSize){return true;}
		if(this.areEquals(this._currentArray,this._targetArray,index))
		{
			return false;
		}else
		{
			return true;
		}
	}
	
	
	
	public long numberOfPermutations()
	{
		long l=0;
		long factS=factorial(this._setSize);
		for(int k=2;k<=this._setSize;k++)
		{
			l+=factS/factorial(this._setSize-k);
		}
		return l;
	}
	
	public long factorial(int n)
	{
		long l=1;
		for(int i=2;i<=n;i++)
		{
			l*=i;
		}
		return l;
	}
	
}
