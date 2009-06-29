/**
 * We define STABLE_CONFIGURATION for a state S inputs which don't activate any adaptation rule.
 */
package uk.ac.ucl.cs.InputMatrix.validation;

import java.io.PrintWriter;
import java.util.Vector;

import uk.ac.ucl.cs.InputMatrix.Fault;
import uk.ac.ucl.cs.InputMatrix.InputSpace;
import uk.ac.ucl.cs.InputMatrix.Rule;
import uk.ac.ucl.cs.InputMatrix.State;
import uk.ac.ucl.cs.InputMatrix.PredicateVariable;

/**
 * @author rax
 *
 */
public class ContextHazardDetectionOptimized extends FaultDetectionAlgorithm {

	protected boolean _debug=true;
	protected boolean _output=true;
	protected boolean _debugInfo=false;
	
	//statistics
	protected double[][] _paths;
	protected long[][] _foundFaults;
	protected long[] _time;
	
	
	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.InputMatrix.faultDetection.FaultDetectionAlgorithm#applyRegression(uk.ac.ucl.cs.InputMatrix.Fault[])
	 */
	@Override
	public Fault[] applyRegression(Fault[] fault) {
		throw new RuntimeException("Regression not implemented yet");
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.InputMatrix.faultDetection.FaultDetectionAlgorithm#detectFaults()
	 */
	@Override
	public Fault[] detectFaults() {	
		Vector<Fault> faults=new Vector<Fault>();
		
		//initialize statistics
		this._paths=new double[this.getAFSMUnderTest().stateSize()][2];
		this._time=new long[this.getAFSMUnderTest().stateSize()];
		this._foundFaults=new long[this.getAFSMUnderTest().stateSize()][3];
		for(int i=0;i<this.getAFSMUnderTest().stateSize();i++)
		{
			this._paths[i][0]=0;
			this._paths[i][1]=0;
			this._time[i]=0;
			this._foundFaults[i][0]=0;
			this._foundFaults[i][1]=0;
			this._foundFaults[i][2]=0;
		}
		
		//for each state
		//consider only the subset of variables 
		for(int s=0;s<this.getAFSMUnderTest().stateSize();s++)
		{
			this._time[s]=-System.currentTimeMillis();
			
			State state=this.getAFSMUnderTest().stateAt(s);
			InputSpace iSpace=state.getInputSpace();
			if(this._skipForbiddenStates==true&&state.isForbiddenState())
			{
				if(this._output)
				{
					System.out.println("State "+state.getName()+" skipped because forbidden.");
				}
				continue;
			}else
			{
				if(this._output)
				{
					System.out.println("Exploring state "+state.getName()+".");
				}
			}
			
			//variables in the state
			PredicateVariable[] variables=state.getVariables();
			int numVar=variables.length;
			long maxInputValue=(long) Math.pow(2,numVar);
			
			//for each possible combination of input
			for(long l=0;l<maxInputValue;l++)
			{
				//starting configuration
				char[] currentConfig=FaultDetectionAlgorithm.longToInputSequence(l, numVar);
				if(this._output){
					System.out.println("State "+state.getName()+" exploring "+new String(currentConfig));
				}
				
				//if it is not a stable configuration-> discard 
				long input=this.inputFromVariables(variables, currentConfig);
				
				//skipped constrained inputs
				if(iSpace.isForbiddenInput(input)==true)
				{							
					continue;
				}//else{this._paths[s][0]++;}
				
				
				if(state.getInputSpace().getSatisfiedRules(input)!=null&&state.getInputSpace().getSatisfiedRules(input).length>0)
				{
					if(this._output)
					{
						System.out.println("State "+state.getName()+" configuration "+new String(currentConfig)+" skipped because not stable.");
					}
					continue;
				}
			
				final long expectedIterations=ContextHazardDetectionOptimized.numberOfPermutations(numVar,2,numVar);
				long iterations=0;
				long skipped=0;

				//start the path
				//since the max number of variable will be numVar just initialize everything with that size
				int currentSize=1;
				int[] index=new int[numVar];//{0}; //incremental indices
				boolean[] contextPrioritizedHazard=new boolean[numVar];//false
				//int[] currentPriority=new int[numVar];//Integer.MAX_VALUE;
				//Rule[][] ruleOnPath=new Rule[numVar][];
				Rule[] maxPrioritizedRuleInThisConfig=new Rule[numVar];
				int[] maxPriorityInThisConfig=new int[numVar];//Integer.MAX_VALUE;
				PredicateVariable[] pathVariables=new PredicateVariable[numVar];

				//inizialize arrays
				for(int i=0;i<numVar;i++)
				{
					index[i]=0;
					contextPrioritizedHazard[i]=false;
					//currentPriority[i]=Integer.MAX_VALUE;
					//ruleOnPath[i]=null;
					maxPrioritizedRuleInThisConfig[i]=null;
					maxPriorityInThisConfig[i]=Integer.MAX_VALUE;
					pathVariables[i]=null;
				}
				
				do{
					assert(currentSize<=numVar);
					//++++++++++++++++++++++
					//init
					char[] newConfig=currentConfig.clone();
					for(int i=0;i<currentSize;i++)
					{
						//System.out.println("numVar: "+numVar+" newConfig:"+newConfig.length+" index[i]"+index[i]+" i:"+i);
						//commute the variable in the char[]
						newConfig[index[i]]=(newConfig[index[i]]=='0')?'1':'0';
					}
					
					//+++++++++++++
					
					
					//increasing size and exploring
					while(currentSize<=numVar){
						
						//output
						if(this._debugInfo==true)
						{
							System.out.print("State "+state.getName()+" from "+new String(currentConfig)+" evaluating commutations {");						
							for(int bf_i=0;bf_i<currentSize;bf_i++)
							{
								System.out.print(index[bf_i]);
								if(bf_i!=currentSize-1)
								{
									System.out.print(',');
								}
							}
							System.out.println("}");
						}
						
						if(currentSize>=2){
							iterations++;
						}
						
						//++++++++++++++++++++++++++++++++++++
						//cool stuff i am supposed to do
						
						
						//generating input from configuration
						pathVariables[currentSize-1]=variables[index[currentSize-1]];
						long currentInput=this.inputFromVariables(variables, newConfig);
						
						//skipped constrained inputs
						if(iSpace.isForbiddenInput(currentInput)==true)
						{
							if(currentSize<numVar){
								skipped+=ContextHazardDetectionOptimized.numberOfPermutations(numVar-currentSize, 1, numVar-currentSize);
							}							
							break;
						}//else{this._paths[s][0]++;}
						
						//ruleOnPath[currentSize-1]=state.getInputSpace().getSatisfiedRules(currentInput);
						Rule[] r=iSpace.getSatisfiedRules(currentInput);
						
						
						
						//we reach a stable configuration
						//if(ruleOnPath[currentSize-1]==null||ruleOnPath[currentSize-1].length==0)
						if(r==null||r.length==0)
						{
							//if there are satisfied rules in the path this is a fault
							if(currentSize>1)
							{
								assert(state.getInputSpace().getSatisfiedRules(this.inputFromVariables(variables,newConfig)).length==0);
								assert(maxPrioritizedRuleInThisConfig[currentSize-2]!=null);
								ContextHazardFault f=new ContextHazardFault(ContextHazardFault.CONTEXT_STABILITY_HAZARD, new String(this.generalInputFromVariables(variables,currentConfig)), state, ContextHazardDetectionOptimized.resizeArray(pathVariables, currentSize), ContextHazardDetectionOptimized.resizeArray(maxPrioritizedRuleInThisConfig,currentSize-1));
								faults.addElement(f);
								this._foundFaults[s][0]++;
								if(this._output)
								{
									System.out.println("State "+state.getName()+" from "+new String(currentConfig)+" path: "+new String(newConfig)+" found "+ContextHazardFault.CONTEXT_STABILITY_HAZARD+".");
								}
							}
							//any further configuration can be discarded
							if(currentSize<numVar){
								skipped+=ContextHazardDetectionOptimized.numberOfPermutations(numVar-currentSize, 1, numVar-currentSize);
							}
							break;
						}
						
						//now that the current is not stable
						//check for prioritized hazards
						//maxPrioritizedRuleInThisConfig[currentSize-1]=getMaxPriorityzedRule(ruleOnPath[currentSize-1]);
						//maxPriorityInThisConfig[currentSize-1]=getMaxPriority(ruleOnPath[currentSize-1]);
						maxPrioritizedRuleInThisConfig[currentSize-1]=getMaxPriorityzedRule(r);
						maxPriorityInThisConfig[currentSize-1]=getMaxPriority(r);
						
						contextPrioritizedHazard[currentSize-1]=false;
						for(int i=1;i<currentSize;i++)
						{
							if(maxPrioritizedRuleInThisConfig[currentSize-1].hasHigherPriority(maxPrioritizedRuleInThisConfig[i-1]))
							{
								contextPrioritizedHazard[currentSize-1]=true;
								break;
							}
						}
						
						//the actual configuration is not stable but contains a priority preemption
						if(contextPrioritizedHazard[currentSize-1]==true)
						{
							ContextHazardFault f=new ContextHazardFault(ContextHazardFault.CONTEXT_PRIORITIZED_HAZARD, new String(this.generalInputFromVariables(variables,currentConfig)), state, ContextHazardDetectionOptimized.resizeArray(pathVariables, currentSize), ContextHazardDetectionOptimized.resizeArray(maxPrioritizedRuleInThisConfig,currentSize));
							faults.addElement(f);
							this._foundFaults[s][2]++;
							if(this._output)
							{
								System.out.println("State "+state.getName()+" from "+new String(currentConfig)+" path: "+new String(newConfig)+" found "+ContextHazardFault.CONTEXT_PRIORITIZED_HAZARD+".");
							}
							//any further configuration can be discarded
							if(currentSize<numVar){
								skipped+=ContextHazardDetectionOptimized.numberOfPermutations(numVar-currentSize, 1, numVar-currentSize);
							}
							break;
						}else
						//the actual configuration does not contains a priority preemption but a flow of activation can activate more than one rule	
						{
							boolean raise=false;
							for(int i=0;i<currentSize-1;i++)
							{
								if (!maxPrioritizedRuleInThisConfig[currentSize-1].equals(maxPrioritizedRuleInThisConfig[i]) &&
									maxPrioritizedRuleInThisConfig[currentSize-1].hasLowerPriority(maxPrioritizedRuleInThisConfig[i])) {
									raise=true;
									break;
								}
							}
							if(raise==true)
							{
								//if there are satisfied rules in the path this is a fault
								assert(currentSize>1);
								ContextHazardFault f=new ContextHazardFault(ContextHazardFault.CONTEXT_ACTIVATION_HAZARD, new String(this.generalInputFromVariables(variables,currentConfig)), state, this.resizeArray(pathVariables, currentSize), this.resizeArray(maxPrioritizedRuleInThisConfig,currentSize));
								faults.addElement(f);
								this._foundFaults[s][1]++;
								if(this._output)
								{
									System.out.println("State "+state.getName()+" from "+new String(currentConfig)+" path: "+new String(newConfig)+" found "+ContextHazardFault.CONTEXT_ACTIVATION_HAZARD+".");
								}
								
								//any further configuration can be discarded
								if(currentSize<numVar){
									skipped+=ContextHazardDetectionOptimized.numberOfPermutations(numVar-currentSize, 1, numVar-currentSize);
								}
								break;
							}
						}
						
						
						//end of size now stop increasing
						if(currentSize>=numVar)
						{
							break;
						}
						
						//int[] newIndex=new int[subsetSize+1];
						currentSize++;
						index[currentSize-1]=ContextHazardDetectionOptimized.getSmallestAvilable(index, numVar, currentSize-1);
						assert(index[currentSize-1]<numVar);
						//for(int i=0;i<subsetSize;i++)
						//{
						//	newIndex[i]=index[i];
						//}
						//newIndex[subsetSize]=ContextHazardDetectionOptimized.getSmallestAvilable(index, numVar, subsetSize-1);
						//index=newIndex;
						
						//update the new config
						newConfig[index[currentSize-1]]=(newConfig[index[currentSize-1]]=='0')?'1':'0';
					}
					
					//++++++++++++++++++++++++++++++++++++++++++++++++
									
					if(ContextHazardDetectionOptimized.isEnd(index,numVar,currentSize)==true)
					{
						break;
					}
					
					//decreasing and rolling back
					int decreasedIndex=currentSize-1; //it was decreasedIndex=currentSize
					int value=-1;
					boolean stop=false;
					while(decreasedIndex>=0&&stop==false)
					{
						value=index[decreasedIndex];
						do{
							value++;
							if(value<numVar&&!ContextHazardDetectionOptimized.valueInUse(value, numVar, index, decreasedIndex))
							{
								stop=true;
								break;
							}
						}while(value<numVar);
						if(stop==true)
						{
							break;
						}
						decreasedIndex--;
					}
					
					//if we reach the last value end
					if(decreasedIndex<0)
					{
						assert(isEnd(index,numVar,1));
						break;
					}
					
				
					assert(value>-1);
					assert(value<numVar);
					assert(decreasedIndex>=0);
					//int[] newIndex=new int[decreasedIndex+1];
					currentSize=decreasedIndex+1;//+1
					index[currentSize-1]=value;
					
					//for(int i=0;i<newIndex.length-1;i++)
					//{
					//	newIndex[i]=index[i];
					//}
					//newIndex[newIndex.length-1]=value;//this.getSmallestAvilable(index, numVar, newIndex.length);
					//index=newIndex;
				}
				while(true);
				
				if(this._output){
					System.out.println("State "+state.getName()+" in "+new String(currentConfig)+" Performed:"+iterations+", Skipped: "+skipped+", Expected: "+expectedIterations+" Missing: "+(expectedIterations-(skipped+iterations)));
				}
				assert((skipped+iterations)==expectedIterations);
				this._paths[s][0]+=iterations;
				this._paths[s][1]+=skipped;
				assert(this._paths[s][0]>-1);
				assert(this._paths[s][1]>-1);
			}
			
			this._time[s]+=System.currentTimeMillis();
			
		}
		
		return faults.toArray(new Fault[faults.size()]);
	}


	/**
	 * @return the _debug
	 */
	public boolean isDebug() {
		return _debug;
	}

	/**
	 * @param _debug the _debug to set
	 */
	public void setDebug(boolean _debug) {
		this._debug = _debug;
	}

	/**
	 * @return the _output
	 */
	public boolean isOutput() {
		return _output;
	}

	/**
	 * @param _output the _output to set
	 */
	public void setOutput(boolean _output) {
		this._output = _output;
	}
	
	/**
	 * From a
	 * @param r
	 * @return
	 */
	public static Rule getMaxPriorityzedRule(Rule[] r)
	{
		if(r==null||r.length==0)
		{
			return null;
		}
		int max=Integer.MAX_VALUE;
		int index=-1;
		for(int i=0;i<r.length;i++)
		{
			if(r[i].getPriority()==max)
			{
				throw new RuntimeException("The model is nondeterministic and hazard detection cannot be applied!");
			}
			if(r[i].hasHigherPriority(max))
			{
				max=r[i].getPriority();
				index=i;
			}
		}
		assert(index>=0);
		return r[index];
	}
	
	public static int getMaxPriority(Rule[] r)
	{
		if(r==null||r.length==0)
		{
			return -1;
		}
		int max=Integer.MAX_VALUE;
		for(int i=0;i<r.length;i++)
		{
			if(r[i].hasHigherPriority(max))
			{
				max=r[i].getPriority();
			}
		}
		assert(max<=Rule.LOW_PRIORITY);
		assert(max>=Rule.MAX_PRIORITY);
		return max;
	}
	
	public static boolean isEnd(int[] index, int setSize, int maxSize)
	{
		assert(index!=null);
		assert(maxSize<=setSize);
		assert(index.length<=setSize);
		
		//if(index.length<setSize){return false;}
		for(int i=0;i<maxSize;i++)
		{
			if(index[i]!=setSize-1-i)
			{
				return false;
			}
		}
		return true;
	}
	

	//@Override
	public void printFaultsToStream(Fault[] fault, PrintWriter pw) {
		if(fault==null){throw new RuntimeException("Fault array cannot be null");}
		if(pw==null){throw new RuntimeException("PrintWriter array cannot be null");}
		
		pw.println(fault.length+" instances of context hazard.");
		pw.println("State\tType\tConfiguration\tCommutations\tRules");
		for(Fault f:fault)
		{
			ContextHazardFault ff=(ContextHazardFault)f;
			pw.print(ff.getState().getName()+"\t"+ff.getName()+"\t"+ff.getInput()+"\t");
			
			pw.print("{");
			for(int i=0;i<ff.getCriticalPath().length;i++)
			{
				pw.print(ff.getCriticalPath()[i].getName());
				if(i!=ff.getCriticalPath().length-1)
				{
					pw.print(",");
				}
			}
			pw.print("}\t");
			
			pw.print("{");
			for(int i=0;i<ff.getRules().length;i++)
			{
				pw.print(ff.getRules()[i].getName());
				if(i!=ff.getRules().length-1)
				{
					pw.print(",");
				}
			}
			pw.print("}");
			
			pw.println("");
		}
		pw.flush();
	}

	
	public static int getSmallestAvilable(int[] index, int setSize, int maxIndex)
	{
		assert(index.length<=setSize);
		assert(maxIndex<=index.length);
		int k=-1;
		for(int j=0;j<setSize;j++){
			boolean available=true;
			for(int i=0;i<maxIndex;i++)
			{
				if(index[i]==j)
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
		assert(k>=0);
		assert(k<setSize);
		return k;
	}
	
	public static boolean valueInUse(int value, int setSize,int[] index, int maxSize)
	{
		assert(value<setSize);
		assert(maxSize<=index.length);
		for(int i=0;i<maxSize;i++)
		{
			if(index[i]==value)
			{
				return true;
			}
		}
		return false;
	}
	
	
	public static long numberOfPermutations(int setSize, int minSubset, int maxSubset)
	{
		assert(minSubset>0);
		assert(maxSubset>0);
		assert(setSize>0);
		if(minSubset>setSize){return 0;}
		//assert(minSubset<=setSize);
		assert(maxSubset<=setSize);
		assert(minSubset<=maxSubset);
		long l=0;
		long factS=factorial(setSize);
		for(int k=minSubset;k<=maxSubset;k++)
		{
			l+=factS/factorial(setSize-k);
		}
		return l;
	}
	
	public static long factorial(int n)
	{
		long l=1;
		for(int i=2;i<=n;i++)
		{
			l*=i;
			assert(l>0);
		}
		return l;
	}
	
	
	public static PredicateVariable[] resizeArray(PredicateVariable[] var, int newSize)
	{
		assert(newSize<=var.length);
		PredicateVariable[] v=new PredicateVariable[newSize];
		for(int i=0;i<newSize;i++)
		{
			assert(var[i]!=null);
			v[i]=var[i];
		}
		return v;
	}
	
	public static Rule[] resizeArray(Rule[] rule, int newSize)
	{
		assert(newSize<=rule.length);
		Rule[] r=new Rule[newSize];
		for(int i=0;i<newSize;i++)
		{
			assert(rule[i]!=null);
			r[i]=rule[i];
		}
		return r;
	}

	@Override
	public void printStatisticsToStream(PrintWriter pw) {
		if(this._paths==null){throw new RuntimeException("Paths array cannot be null: invoke detectFaults() first.");}
		if(pw==null){throw new RuntimeException("PrintWriter array cannot be null");}
		
		long totalTime=0;
		for(int i=0;i<this._time.length;i++)
		{
			totalTime+=this._time[i];
		}
		
		pw.println("ContextHaard detection: "+this.getAFSMUnderTest().stateSize()+" states explored in "+totalTime+" msec.");
		pw.println("State\tVariables\tExploredPaths\tSkippedPaths\tStability\tActivation\tPrioritized\tTime");
		for(int i=0;i<this._paths.length;i++)
		{
			pw.println(this.getAFSMUnderTest().stateAt(i)+"\t"+
					this.getAFSMUnderTest().stateAt(i).getVariables().length+"\t"+
					this._paths[i][0]+"\t"+this._paths[i][1]+"\t"+
					this._foundFaults[i][0]+"\t"+this._foundFaults[i][1]+"\t"+this._foundFaults[i][2]+"\t"+
					this._time[i]);
		}
	}
	
	
	
	public static void main(String[] args)
	{
		int num=7;
		double d=0;
		d+=numberOfPermutations(num,2,num);
		assert(d>0);
		System.out.println(d);
		d*=Math.pow(2, num);
		System.out.println(d);
	}
	
}


