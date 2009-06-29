/**
 * We define STABLE_CONFIGURATION for a state S inputs which don't activate any adaptation rule.
 */
package uk.ac.ucl.cs.InputMatrix.validation;

import java.io.PrintWriter;
import java.util.Vector;

import uk.ac.ucl.cs.InputMatrix.Fault;
import uk.ac.ucl.cs.InputMatrix.Rule;
import uk.ac.ucl.cs.InputMatrix.State;
import uk.ac.ucl.cs.InputMatrix.PredicateVariable;

/**
 * @author rax
 *
 */
public class ContextHazardDetection extends FaultDetectionAlgorithm {

	protected boolean _debug=true;
	protected boolean _output=true;
	protected boolean _debugInfo=false;
	
	
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
		
		//for each state
		//consider only the subset of variables 
		for(int s=0;s<this.getAFSMUnderTest().stateSize();s++)
		{
			State state=this.getAFSMUnderTest().stateAt(s);
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
				if(state.getInputSpace().getSatisfiedRules(input)!=null&&state.getInputSpace().getSatisfiedRules(input).length>0)
				{
					if(this._output)
					{
						System.out.println("State "+state.getName()+" configuration "+new String(currentConfig)+" skipped because not stable.");
					}
					continue;
				}
			
				PermutationGenerator permutation=new PermutationGenerator(numVar);
				boolean prune=false;
				int prunableIndex=-1;
				//int[] explored=null;
				int[] index=null;
				     
				while((prune==false&&permutation.hasNext())||(prune==true&&permutation.hasNext(prunableIndex)))
				{
					//explored=index;
					if(prune==true){
						index=permutation.getNext(prunableIndex);
						prune=false;
					}else
					{
						index=permutation.getNext();
					}
					
					
					
					//+++++++++++++++++++
					//if all variables have been assigned (e.g. this is the last step of the path)
					//then evaluate the path.

					//output
					if(this._debugInfo==true)
					{
						StringBuffer bf=new StringBuffer();
						bf.append("{");
						
						for(int bf_i=0;bf_i<index.length;bf_i++)
						{
							bf.append(index[bf_i]);
							if(bf_i!=index.length-1)
							{
								bf.append(',');
							}
						}
						bf.append("}");
						System.out.println("State "+state.getName()+" from "+new String(currentConfig)+" evaluating commutations "+bf.toString());
					}
					
					//creates an array with the commuting variables starting from index[]
					PredicateVariable[] pathVariables=new PredicateVariable[index.length];
					for(int k=0;k<index.length;k++){
						pathVariables[k]=variables[index[k]];
					}
					
					//initialise a char[] from the current configuration 
					char[] newConfig=currentConfig.clone();
					boolean contextHazard=false;
					boolean contextPrioritizedHazard=false;
					int currentPriority=Integer.MAX_VALUE;
					Vector<Rule> ruleOnPath=new Vector<Rule>();
					Rule[] r=null;
					Rule maxPrioritizedRuleInThisConfig=null;
					int maxPriorityInThisConfig=Integer.MAX_VALUE;
					
					//explore path
					for(int h=0;h<index.length;h++)
					{
						//commute the variable in the char[]
						newConfig[index[h]]=(newConfig[index[h]]=='0')?'1':'0';
						/*if(explored!=null&&newConfig[h]==explored[h])
						{
							if(this._debugInfo==true)
							{
								System.out.println("State "+state.getName()+" from "+new String(currentConfig)+" to: "+new String(newConfig)+" already explored.");
							}
							continue;
						}*/
						
						if(this._debugInfo==true)
						{
							System.out.println("State "+state.getName()+" from "+new String(currentConfig)+" moving to: "+new String(newConfig));
						}
						
						//generating input from configuration
						long currentInput=this.inputFromVariables(variables, newConfig);
						
						
						r=state.getInputSpace().getSatisfiedRules(currentInput);
						maxPrioritizedRuleInThisConfig=null;
						maxPriorityInThisConfig=Integer.MAX_VALUE;
					
						//check is the current configuration is stable
						//if in the middle of the path there is a stable one: discard because a shorter path will consider it
						if(h!=index.length-1&&(r==null||r.length==0))
						{
							contextHazard=false;
							contextPrioritizedHazard=false;
							if(this._debugInfo==true)
							{
								System.out.println("State "+state.getName()+" from "+new String(currentConfig)+" path: "+new String(newConfig)+" discarded beause contains stable configurations.");
							}
							prunableIndex=h;
							prune=true;
							break;
						}
						
						//browse the array of rules and choose for the highest prioritized one
						for(Rule rule:r)
						{
							if(rule.hasHigherPriority(maxPriorityInThisConfig)){
								maxPriorityInThisConfig=rule.getPriority();
								maxPrioritizedRuleInThisConfig=rule;
							}
						}
						
						
						
						//0-low*-high is the only case of prioritized hazard
						if(maxPrioritizedRuleInThisConfig!=null&&ruleOnPath.contains(maxPrioritizedRuleInThisConfig)==false)
						{
							if(maxPriorityInThisConfig<currentPriority)
							{
								//if there was already a rule with lower priority then we have a context prioritized fault
								if(ruleOnPath.size()!=0)
								{
									contextPrioritizedHazard=true;
								}
								currentPriority=maxPriorityInThisConfig;
							}
							ruleOnPath.addElement(maxPrioritizedRuleInThisConfig);
						}
						
						
						
					}

					//if the last step is stable and we have rule on path the it is a context hazard
					if((r==null||r.length==0)&&ruleOnPath.size()!=0)
					{
						contextHazard=true;
						//0-low-high-0 is a normal context hazard
						contextPrioritizedHazard=false;
					}
					//if there are multiple possible rules on the path then it is an hazard
					//but only if it is not prioritized
					else if(ruleOnPath.size()>1&&contextPrioritizedHazard==false)
					{
						contextHazard=true;
					}
					
					
					//we create the fault
					//precedence to the normal context hazard because in case of 0-low-high-0 the reported fault must be a normal ContextHazard
					if(contextHazard==true)
					{
						ContextHazardFault f=new ContextHazardFault(ContextHazardFault.CONTEXT_STABILITY_HAZARD, new String(this.generalInputFromVariables(variables,currentConfig)), state, pathVariables, ruleOnPath.toArray(new Rule[ruleOnPath.size()]));
						faults.addElement(f);
						if(this._output)
						{
							System.out.println("State "+state.getName()+" from "+new String(currentConfig)+" path: "+new String(newConfig)+" found "+ContextHazardFault.CONTEXT_STABILITY_HAZARD+" .");
						}
					}else if(contextPrioritizedHazard==true)
					{
						ContextHazardFault f=new ContextHazardFault(ContextHazardFault.CONTEXT_PRIORITIZED_HAZARD, new String(this.generalInputFromVariables(variables,currentConfig)), state, pathVariables, ruleOnPath.toArray(new Rule[ruleOnPath.size()]));
						faults.addElement(f);
						if(this._output)
						{
							System.out.println("State "+state.getName()+" from "+new String(currentConfig)+" path: "+new String(newConfig)+" found "+ContextHazardFault.CONTEXT_PRIORITIZED_HAZARD+".");
						}
					} 
					//+++++++++++++++++++
					
				}
				
				/*
				//*********************
				//for each possible subset from 2 to numVar
				for(int subsetSize=2;subsetSize<=numVar;subsetSize++){
					//each variables
					for(int i=0;i<numVar;i++)
					{
						//each gap. that's a trick for having all the possible combination
						for(int gap=1;gap<numVar;gap++)
						{
							//the critical path we are searching, if exists have length maxPathLength
							//so we define an array of index in which we will store our variables
							int[] index=new int[subsetSize];
							
							//fill the array with different variables
							for(int size=0;size<subsetSize;size++){	
								index[size]=(i+(size*gap))%numVar;
								System.out.print(index[size]+", ");
							}
							System.out.println();
							
							//each variable can commute only once
							if(this._debug){
								for(int vr=0;vr<index.length-1;vr++)
								{
									for(int lvr=vr+1;lvr<index.length;lvr++)
									{
										assert(index[vr]!=index[lvr]);
									}
								}
							}
	
							//+++++++++++++++++++
							//if all variables have been assigned (e.g. this is the last step of the path)
							//then evaluate the path.

							//output
							if(this._output==true)
							{
								StringBuffer bf=new StringBuffer();
								bf.append("{");
								
								for(int bf_i=0;bf_i<index.length;bf_i++)
								{
									bf.append(index[bf_i]);
									if(bf_i!=index.length-1)
									{
										bf.append(',');
									}
								}
								bf.append("}");
								System.out.println("State "+state.getName()+" from "+new String(currentConfig)+" evaluating commutations "+bf.toString());
							}
							
							//creates an array with the commuting variables starting from index[]
							Variable[] pathVariables=new Variable[subsetSize];
							for(int k=0;k<subsetSize;k++){
								pathVariables[k]=variables[index[k]];
							}
							
							//initialise a char[] from the current configuration 
							char[] newConfig=currentConfig.clone();
							boolean contextHazard=false;
							boolean contextPrioritizedHazard=false;
							int currentPriority=Integer.MAX_VALUE;
							Vector<Rule> ruleOnPath=new Vector<Rule>();
							
							//explore path
							for(int h=0;h<index.length;h++)
							{
								//commute the variable in the char[]
								newConfig[index[h]]=(newConfig[index[h]]=='0')?'1':'0';
								
								
								if(this._output==true)
								{
									System.out.println("State "+state.getName()+" from "+new String(currentConfig)+" moving to: "+new String(newConfig));
								}
								
								//generating input from configuration
								long currentInput=this.inputFromVariables(variables, newConfig);
								
								
								Rule[] r=state.getInputSpace().getSatisfiedRules(currentInput);
								Rule maxPrioritizedRuleInThisConfig=null;
								int maxPriorityInThisConfig=Integer.MAX_VALUE;
							
								//check is the current configuration is stable
								//if in the middle of the path there is a stable one: discard because a shorter path will consider it
								if(h!=index.length-1&&(r==null||r.length==0))
								{
									contextHazard=false;
									contextPrioritizedHazard=false;
									if(this._output==true)
									{
										System.out.println("State "+state.getName()+" from "+new String(currentConfig)+" path: "+new String(newConfig)+" discarded beause contains stable configurations.");
									}
									break;
								}
								
								//browse the array of rules and choose for the highest prioritized one
								for(Rule rule:r)
								{
									if(rule.hasHigherPriority(maxPriorityInThisConfig)){
										maxPriorityInThisConfig=rule.getPriority();
										maxPrioritizedRuleInThisConfig=rule;
									}
								}
								
								
								
								//0-low*-high is the only case of prioritized hazard
								if(maxPrioritizedRuleInThisConfig!=null&&ruleOnPath.contains(maxPrioritizedRuleInThisConfig)==false)
								{
									if(maxPriorityInThisConfig<currentPriority)
									{
										//if there was already a rule with lower priority then we have a context prioritized fault
										if(ruleOnPath.size()!=0)
										{
											contextPrioritizedHazard=true;
										}
										currentPriority=maxPriorityInThisConfig;
									}
									ruleOnPath.addElement(maxPrioritizedRuleInThisConfig);
								}
								
								//last step we define hazards
								if(h==index.length-1)
								{
									//if the last step is stable and we have rule on path the it is a context hazard
									if((r==null||r.length==0)&&ruleOnPath.size()!=0)
									{
										contextHazard=true;
										//0-low-high-0 is a normal context hazard
										contextPrioritizedHazard=false;
									}
									//if there are multiple possible rules on the path then it is an hazard
									//but only if it is not prioritized
									else if(ruleOnPath.size()>1&&contextPrioritizedHazard==false)
									{
										contextHazard=true;
									}
								}
								
							}
							
							//we create the fault
							//precedence to the normal context hazard because in case of 0-low-high-0 the reported fault must be a normal ContextHazard
							if(contextHazard==true)
							{
								ContextHazardFault f=new ContextHazardFault(ContextHazardFault.CONTEXT_HAZARD, new String(this.generalInputFromVariables(variables,currentConfig)), state, pathVariables, ruleOnPath.toArray(new Rule[ruleOnPath.size()]));
								faults.addElement(f);
								if(this._output)
								{
									System.out.println("State "+state.getName()+" from "+new String(currentConfig)+" path: "+new String(newConfig)+" found "+ContextHazardFault.CONTEXT_HAZARD+" .");
								}
							}else if(contextPrioritizedHazard==true)
							{
								ContextHazardFault f=new ContextHazardFault(ContextHazardFault.CONTEXT_PRIORITIZED_HAZARD, new String(this.generalInputFromVariables(variables,currentConfig)), state, pathVariables, ruleOnPath.toArray(new Rule[ruleOnPath.size()]));
								faults.addElement(f);
								if(this._output)
								{
									System.out.println("State "+state.getName()+" from "+new String(currentConfig)+" path: "+new String(newConfig)+" found "+ContextHazardFault.CONTEXT_PRIORITIZED_HAZARD+".");
								}
							} 
							//+++++++++++++++++++
							
						}
					}
				}*/
				//*********************
				
				
			
			}

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

	@Override
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

	@Override
	public void printStatisticsToStream(PrintWriter pw) {
		// TODO Auto-generated method stub
		
	}

}


