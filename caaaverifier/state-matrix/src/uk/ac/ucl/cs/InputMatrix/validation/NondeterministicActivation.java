/**
 * For each state
 *   For each possible input
 *     the rule to trigger, if exist must be one
 */
package uk.ac.ucl.cs.InputMatrix.validation;


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


import uk.ac.ucl.cs.InputMatrix.Fault;
import uk.ac.ucl.cs.InputMatrix.InputSpace;
import uk.ac.ucl.cs.InputMatrix.Rule;
import uk.ac.ucl.cs.InputMatrix.State;
import uk.ac.ucl.cs.InputMatrix.PredicateVariable;

/**
 * @author rax
 *
 */
public class NondeterministicActivation extends FaultDetectionAlgorithm {
	
	//statistics
	protected long[][] _paths;
	protected long[] _foundFaults;
	protected long[] _time;
	
	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.InputMatrix.faultDetection.FaultDetectionAlgorithm#detectFaults()
	 */
	@Override
	public Fault[] detectFaults() {
		_detectedFaults = new ArrayList<Fault>();
		
		//initialize statistics
		this._paths=new long[this.getAFSMUnderTest().stateSize()][2];
		this._time=new long[this.getAFSMUnderTest().stateSize()];
		this._foundFaults=new long[this.getAFSMUnderTest().stateSize()];
		
		for(int i = 0; i < getAFSMUnderTest().stateSize(); i ++) {
			_time[i] = -System.currentTimeMillis();
			State s = getAFSMUnderTest().stateAt(i);
			
			//skip forbidden states
			if (_skipForbiddenStates && s.isForbiddenState()) {
				continue;
			}
			
			PredicateVariable[] vars = s.getVariables();
			int subspaceSize = vars.length;
			long maxSpaceSize = (long) Math.pow(2,subspaceSize);
			InputSpace iSpace = s.getInputSpace();
			
			for (long l = 0; l < maxSpaceSize; l ++) {
				//retrieve the rules from the iSpace
				char[] currentConfig = FaultDetectionAlgorithm.longToInputSequence(l, subspaceSize);
				long input = inputFromVariables(vars, currentConfig);
				
				if (iSpace.isForbiddenInput(input) == true) {
					continue;
				} else {
					_paths[i][0] ++;
				}
				
				Rule[] rules = iSpace.getSatisfiedRules(input);
				List<Rule> faultyRules = new ArrayList<Rule>();
				
				int higherPriority = Integer.MAX_VALUE; //note that higher priority is 0
				for(Rule r:rules)
				{
					if(r.getPriority()==higherPriority)
					{
						faultyRules.add(r);
					}else if(r.hasHigherPriority(higherPriority)) //lower is higher
					{
						higherPriority=r.getPriority();
						faultyRules.clear();
						faultyRules.add(r);
					}
				}
				if(faultyRules.size()>1)
				{
					InStateFault fault = new InStateFault(new String(this.generalInputFromVariables(vars, currentConfig)), s);
					fault.setNondeterministicRules(faultyRules.toArray(new Rule[faultyRules.size()]));
					_detectedFaults.add(fault);
					_foundFaults[i] ++;
				}				 
			}
			//this._paths[i][0]=maxSpaceSize;
			_paths[i][1] = (long) Math.pow(2, this.getAFSMUnderTest().getInputDimension()) - this._paths[i][0];
			_time[i] += System.currentTimeMillis();
		}
		return this._detectedFaults.toArray(new Fault[this._detectedFaults.size()]);
	}
	
	
	@Override
	public Fault[] applyRegression(Fault[] fault) 
	{
		//TODO regression
		return fault;
	}
	
	/*
	@Override
	public Fault[] applyRegression(Fault[] fault) {
		int states=this.getAFSMUnderTest().stateSize();
		Hashtable<String,Fault>[] reducedFaults=new Hashtable[states];
		for(int i=0;i<states;i++)
		{
			reducedFaults[i]=new Hashtable<String, Fault>();
		}
		for(int j=0;j<this.getAFSMUnderTest().stateSize();j++)
		{
			State s=this.getAFSMUnderTest().stateAt(j);
			
			for(Fault f:fault)
			{
				NondeterministicFault ff=(NondeterministicFault)f;
				if(ff.getState()!=s)
				{
					continue;
				}
				Vector<Variable> variables=new Vector<Variable>();
				for(Rule r:ff.getNondeterministicRules())
				{
					Variable[] varR=r.getPredicate().getVariables();
					for(Variable v:varR)
					{
						if(variables.contains(v)==false)
						{
							variables.addElement(v);
						}
					}
				}
				Variable[] var=variables.toArray(new Variable[variables.size()]);
				variables=null;
				
				char[] input=ff.getInput().toCharArray();
				char[] mask=new char[this.getAFSMUnderTest().getInputDimension()];
				for(int k=0;k<mask.length;k++)
				{
					mask[k]='*';
				}
				for(int k=0;k<var.length;k++)
				{
					mask[this.getAFSMUnderTest().getInputDimension()-1-var[k].getIndexInSpace()]='-';
				}
				int gap=mask.length-input.length;
				for(int k=0;k<input.length;k++)
				{
					if(mask[gap+k]=='-')
					{
						mask[gap+k]=input[k];
					}
				}
				for(int k=0;k<gap;k++)
				{
					if(mask[k]=='-')
					{
						mask[k]='0';
					}
				}
				ff.setInput(new String(mask));
				reducedFaults[j].put(ff.getInput(), ff);
			}
		}

		//create an array
		int size=0;
		for(Hashtable h:reducedFaults)
		{
			size+=h.size();
		}
		Fault[] returnF=new Fault[size];
		int i=0;
		for(Hashtable<String, Fault> h:reducedFaults)
		{
			Set<Entry<String, Fault>> s=h.entrySet();
			for(Entry<String, Fault> e:s)
			{
				returnF[i++]=e.getValue();
			}
		}
		return returnF;
	}
	*/

	@Override
	public void printFaultsToStream(Fault[] fault, PrintWriter pw) {
		if(fault==null){throw new RuntimeException("Fault array cannot be null");}
		if(pw==null){throw new RuntimeException("PrintWriter array cannot be null");}
		
		
		pw.println(fault.length+" instances of nondeterministic activation.");
		pw.println("State\tInput\tRules");
		for(Fault f:fault)
		{
			InStateFault ff=(InStateFault)f;
			pw.print(ff.getState().getName()+"\t"+ff.getInput()+"\t[");
			for(int i=0;i<ff.getNondeterministicRules().length;i++)
			{
				Rule r=ff.getNondeterministicRules()[i];
				pw.print(r.getName());
				if(i!=ff.getNondeterministicRules().length-1)
				{
					pw.print(",");
				}
			}
			pw.println("]");
		}
		pw.flush();
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
		
		pw.println("NondeterministicActivations detection: "+this.getAFSMUnderTest().stateSize()+" states explored in "+totalTime+" msec.");
		pw.println("State\tVariables\tExploredPaths\tSkippedPaths\tFaults\tTime");
		for(int i=0;i<this._paths.length;i++)
		{
			pw.println(this.getAFSMUnderTest().stateAt(i)+"\t"+
					this.getAFSMUnderTest().stateAt(i).getVariables().length+"\t"+
					this._paths[i][0]+"\t"+this._paths[i][1]+"\t"+
					this._foundFaults[i]+"\t"+
					this._time[i]);
		}
	}
	
	

}
