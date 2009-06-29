/**
 * Algorithm to detect violations of forbidden states
 * 
 * foreach state which is not forbidden
 *	 foreach adaptation rule to a forbidden state
 * 		foreach input able to trigger the rule	
 *      	if the rule adapt then there is a fault
 */
package uk.ac.ucl.cs.InputMatrix.validation;

import java.io.PrintWriter;
import java.util.ArrayList;

import uk.ac.ucl.cs.InputMatrix.AdaptationRule;
import uk.ac.ucl.cs.InputMatrix.Fault;
import uk.ac.ucl.cs.InputMatrix.InputSpace;
import uk.ac.ucl.cs.InputMatrix.Rule;
import uk.ac.ucl.cs.InputMatrix.State;
import uk.ac.ucl.cs.InputMatrix.PredicateVariable;

/**
 * @author rax
 *
 */
public class ForbiddenStatesViolation extends FaultDetectionAlgorithm {

	//statistics
	protected long[][] _paths;
	protected long[] _foundFaults;
	protected long[] _time;
	
	@Override
	public Fault[] detectFaults() {
		this._detectedFaults=new ArrayList<Fault>();
		
		//initialize statistics
		this._paths=new long[this.getAFSMUnderTest().stateSize()][2];
		this._time=new long[this.getAFSMUnderTest().stateSize()];
		this._foundFaults=new long[this.getAFSMUnderTest().stateSize()];
		for(int i=0;i<this.getAFSMUnderTest().stateSize();i++)
		{
			this._paths[i][0]=0;
			this._paths[i][1]=0;
			this._time[i]=0;
			this._foundFaults[i]=0;
		}
		
		for(int i=0;i<this.getAFSMUnderTest().stateSize();i++)
		{
			this._time[i]=-System.currentTimeMillis();
			State s=this.getAFSMUnderTest().stateAt(i);
			
			//skip forbidden states
			if(this._skipForbiddenStates&&s.isForbiddenState()){continue;}
			for(int r=0;r<s.ruleSize();r++)
			{
				Rule rr=s.ruleAt(r);
				if(rr instanceof AdaptationRule)
				{
					AdaptationRule rule=(AdaptationRule)rr;
					if(rule.getAdaptationNextState().isForbiddenState()==false){continue;}
					
					PredicateVariable[] vars=rule.getCondition().getVariables();
					int subspaceSize=vars.length;
					long maxSpaceSize=(long) Math.pow(2,subspaceSize);
					InputSpace iSpace=s.getInputSpace();
					
					for(long l=0;l<maxSpaceSize;l++)
					{
						this._paths[i][0]++;
						char[] currentConfig=FaultDetectionAlgorithm.longToInputSequence(l, subspaceSize);
						Rule[] rules=iSpace.getSatisfiedRules(this.inputFromVariables(vars, currentConfig));
						//check if there are rules with higher priority
						boolean flag=false;
						boolean skip=false;
						for(Rule rs:rules)
						{
							if(rs.hasHigherPriority(rule))
							{
								skip=true;
								//break;
							}
							if(rs==rule)
							{
								flag=true;
							}
						}	
						//assert(flag==true);
						if(skip==false&&flag==true)
						{
							ForbiddenStateViolationFault f=new ForbiddenStateViolationFault(new String(this.generalInputFromVariables(vars, currentConfig)),s,rule.getAdaptationNextState(),rule);
							this._detectedFaults.add(f);
						}
					}
				}
			}

			this._paths[i][1]=(long)Math.pow(2, this.getAFSMUnderTest().getInputDimension())-this._paths[i][0]; //not sure bout that........
			this._time[i]+=System.currentTimeMillis();
		}
		
		return this._detectedFaults.toArray(new Fault[this._detectedFaults.size()]);
	}
	
	
	/*
	@Override
	public Fault[] applyRegression(Fault[] fault) {
		int states=this.getAFSMUnderTest().stateSize();
		
		//a different hashtable for each state
		Hashtable<String,Fault>[] reducedFaults=new Hashtable[states];
		for(int i=0;i<states;i++)
		{
			reducedFaults[i]=new Hashtable<String, Fault>();
		}
		
		//for each state
		for(int j=0;j<this.getAFSMUnderTest().stateSize();j++)
		{
			State s=this.getAFSMUnderTest().stateAt(j);
			
			//for each fault
			for(Fault f:fault)
			{
				ForbiddenStateViolationFault ff=(ForbiddenStateViolationFault)f;
				if(ff.getState()!=s)
				{
					continue;
				}
				Variable[] var=ff.getRule().getPredicate().getVariables();
				char[] input=ff.getInput().toCharArray();
				char[] mask=new char[this.getAFSMUnderTest().getInputDimension()];
				//create a mask of '*'
				for(int k=0;k<mask.length;k++)
				{
					mask[k]='*';
				}
				//put '-' if the variable is used in this state
				for(int k=0;k<var.length;k++)
				{
					if(input[k]!='*'){
						mask[this.getAFSMUnderTest().getInputDimension()-1-var[k].getIndexInSpace()]='-';
					}
				}
				int gap=mask.length-input.length;
				//assert(gap==0);
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
	public Fault[] applyRegression(Fault[] fault) {
		return fault;
	}


	@Override
	public void printFaultsToStream(Fault[] fault, PrintWriter pw) {
		if(fault==null){throw new RuntimeException("Fault array cannot be null");}
		if(pw==null){throw new RuntimeException("PrintWriter array cannot be null");}
		
		pw.println(fault.length+" instances of forbidden states violation.");
		pw.println("State\tForbidden\tRule\tInput");
		for(Fault f:fault)
		{
			ForbiddenStateViolationFault ff=(ForbiddenStateViolationFault)f;
			pw.println(ff.getState().getName()+"\t"+ff.getForbiddenState().getName()+"\t"+ff.getRule().getName()+"\t"+ff.getInput());
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
		
		pw.println("ForbiddenStateViolation detection: "+this.getAFSMUnderTest().stateSize()+" states explored in "+totalTime+" msec.");
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
