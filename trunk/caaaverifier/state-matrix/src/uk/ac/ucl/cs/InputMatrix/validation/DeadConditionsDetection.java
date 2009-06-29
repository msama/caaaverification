/**
 * Algorithm to detect violations of forbidden states
 * 
 * foreach state which is not forbidden{
 *	 foreach adaptation rule 
 * 		foreach input 
 *      	if (the rule is triggered) then{ it is not dead}
 */
package uk.ac.ucl.cs.InputMatrix.validation;

import java.io.PrintWriter;
import java.util.ArrayList;

import uk.ac.ucl.cs.InputMatrix.Fault;
import uk.ac.ucl.cs.InputMatrix.InputSpace;
import uk.ac.ucl.cs.InputMatrix.Rule;
import uk.ac.ucl.cs.InputMatrix.State;
import uk.ac.ucl.cs.InputMatrix.PredicateVariable;


/**
 * @author rax
 *
 */
public class DeadConditionsDetection extends FaultDetectionAlgorithm {

	//statistics
	protected long[][] _paths;
	protected long[] _foundFaults;
	protected long[] _time;
	

	@Override
	public Fault[] detectFaults() {
		this._detectedFaults = new ArrayList<Fault>();
		
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
			
			//initialize an array of boolean as false then 
			boolean[] ruleFlag=new boolean[s.ruleSize()];
			for(int j=0;j<ruleFlag.length;j++)
			{
				ruleFlag[j]=false;
			}
			
			
			PredicateVariable[] vars=s.getVariables();
			int subspaceSize=vars.length;
			long maxSpaceSize=(long) Math.pow(2,subspaceSize);
			InputSpace iSpace=s.getInputSpace();
			
			for(long l=0;l<maxSpaceSize&&DeadConditionsDetection.checkRuleFlag(ruleFlag)==false;l++)
			{
				
				char[] currentConfig=FaultDetectionAlgorithm.longToInputSequence(l, subspaceSize);
				long input=this.inputFromVariables(vars, currentConfig);
				
				if(iSpace.isForbiddenInput(input)==true){continue;}else{this._paths[i][0]++;}
				
				Rule[] rules=iSpace.getSatisfiedRules(input);
				if(rules==null||rules.length==0){continue;}
				Rule highPriorityRule=null;
				
				for(Rule r:rules)
				{
					if(highPriorityRule==null||r.hasHigherPriority(highPriorityRule))
					{
						highPriorityRule=r;
					}
				}	
				if(highPriorityRule!=null)
				{
					ruleFlag[s.indexOfRule(highPriorityRule)]=true;
				}
			}
			this._paths[i][1]=(long)Math.pow(2, this.getAFSMUnderTest().getInputDimension())-this._paths[i][0];
			
			for(int j=0;j<ruleFlag.length;j++)
			{
				if(ruleFlag[j]==false)
				{
					DeadConditionFault f=new DeadConditionFault("",s,s.ruleAt(j));
					this._detectedFaults.add(f);
					this._foundFaults[i]++;
				}
			}
			
			this._time[i]+=System.currentTimeMillis();
		}
		
		return this._detectedFaults.toArray(new Fault[this._detectedFaults.size()]);
	}
	/*
	 	public Fault[] detectFaults() {
		this._detectedFaults=new Vector<Fault>();
		
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
			
			//initialize an array of boolean as false then 
			boolean[] ruleFlag=new boolean[s.ruleSize()];
			for(int j=0;j<ruleFlag.length;j++)
			{
				ruleFlag[j]=false;
			}
			
			
			InputSpace iSpace=s.getInputSpace();
			Set<Long> keys=iSpace.keySet();
			for(Long input:keys)
			{
				this._paths[i][0]++;
				Rule[] rules=iSpace.getSatisfiedRules(input.longValue());
				Rule highPriorityRule=null;
				
				for(Rule r:rules)
				{
					if(highPriorityRule==null||r.hasHigherPriority(highPriorityRule))
					{
						highPriorityRule=r;
					}
				}	
				if(highPriorityRule!=null)
				{
					ruleFlag[s.indexOfRule(highPriorityRule)]=true;
				}
			}
			this._paths[i][1]=(long)Math.pow(2, this.getAFSMUnderTest().getInputDimension())-this._paths[i][0];
			
			for(int j=0;j<ruleFlag.length;j++)
			{
				if(ruleFlag[j]==false)
				{
					DeadConditionFault f=new DeadConditionFault("",s,s.ruleAt(j));
					this._detectedFaults.addElement(f);
					this._foundFaults[i]++;
				}
			}
			
			this._time[i]+=System.currentTimeMillis();
		}
		
		return this._detectedFaults.toArray(new Fault[this._detectedFaults.size()]);
	}*/
	

	protected static boolean checkRuleFlag(boolean[] flag)
	{
		for(boolean b:flag)
		{
			if(b==false)
			{
				return false;
			}
		}
		return true;
	}
	
	

	@Override
	public Fault[] applyRegression(Fault[] fault) {
		return fault;
	}


	@Override
	public void printFaultsToStream(Fault[] fault, PrintWriter pw) {
		if(fault==null){throw new RuntimeException("Fault array cannot be null");}
		if(pw==null){throw new RuntimeException("PrintWriter array cannot be null");}
		
		pw.println(fault.length+" instances of dead conditions.");
		pw.println("State\tRule");
		for(Fault f:fault)
		{
			DeadConditionFault ff=(DeadConditionFault)f;
			pw.println(ff.getState().getName()+"\t"+ff.getRule().getName());
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
		
		pw.println("DeadConditions detection: "+this.getAFSMUnderTest().stateSize()+" states explored in "+totalTime+" msec.");
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
