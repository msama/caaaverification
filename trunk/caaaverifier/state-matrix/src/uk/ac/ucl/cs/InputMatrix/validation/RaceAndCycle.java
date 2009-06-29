/**
 * Races and Cycles detection. 
 * 
 * foreach state
 *   foreach input in the input space
 *     foreach adaptation rule
 *        if for the same input also the next state adapts
 *          RACE 
 */
package uk.ac.ucl.cs.InputMatrix.validation;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

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
public class RaceAndCycle extends FaultDetectionAlgorithm {
	
	Hashtable<String, List<RaceFault>> _reducedFaults = new Hashtable<String, List<RaceFault>>();

	//statistics
	protected long[][] _paths;
	protected long[] _foundFaults;
	protected long[] _time;
	
	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.InputMatrix.faultDetection.FaultDetectionAlgorithm#detectFaults()
	 */
	@Override
	public Fault[] detectFaults() {
		this._detectedFaults = new ArrayList<Fault>();
		
		//initialize statistics
		this._paths=new long[this.getAFSMUnderTest().stateSize()][2];
		this._time=new long[this.getAFSMUnderTest().stateSize()];
		this._foundFaults=new long[]{0,0};
		for(int i=0;i<this.getAFSMUnderTest().stateSize();i++)
		{
			this._paths[i][0]=0;
			this._paths[i][1]=0;
			this._time[i]=0;
		}
		
		for(int i=0;i<this.getAFSMUnderTest().stateSize();i++)
		{
			this._time[i]=-System.currentTimeMillis();
			State s=this.getAFSMUnderTest().stateAt(i);
			
			
			//skip forbidden states
			if(this._skipForbiddenStates&&s.isForbiddenState()){continue;}
			
			InputSpace iSpace=s.getInputSpace();
			Set<Long> keys=iSpace.keySet();
			for(Long input:keys)
			{
				//this._paths[i][0]++;
				List<State> v=new ArrayList<State>();
				v.add(s);
				if(iSpace.isForbiddenInput(input.longValue())==true){continue;}else{this._paths[i][0]++;}
				this.exploreState(s, input.longValue(), v, new ArrayList<Rule>());
			}
			this._paths[i][1]=(long)Math.pow(2, this.getAFSMUnderTest().getInputDimension())-this._paths[i][0];
			this._time[i]+=System.currentTimeMillis();
		}
		
		Enumeration<String> inputs=this._reducedFaults.keys();
		while(inputs.hasMoreElements())
		{
			String key=inputs.nextElement();
			List<RaceFault> ff = this._reducedFaults.get(key);
			for(RaceFault f:ff)
			{
				this._detectedFaults.add(f);
			}
		}
		return this._detectedFaults.toArray(new Fault[this._detectedFaults.size()]);
	}


	private void exploreState(State s, long input, List<State> states, List<Rule> rules) {
		Rule[] activeRules=s.getInputSpace().getSatisfiedRules(input);
		
		if(activeRules==null||activeRules.length==0)
		{
			//normal exit condition of the loop: no more activation
			//the method addFault will chose if it is a fault or not
			this.addFault(input, states,rules,false);
			return;
		}
		
		for(Rule r:activeRules)
		{
			if(r instanceof AdaptationRule)
			{
				AdaptationRule ar = (AdaptationRule)r;

				List<State> statesHotCopy = new ArrayList<State>(states);
				List<Rule> rulesHotCopy = new ArrayList<Rule>(rules);
				
				State nextS = ar.getAdaptationNextState();
				statesHotCopy.add(nextS);
				rulesHotCopy.add(ar);
				
				//check if it is a cycle
				if(states.contains(nextS))
				{
					//cycle
					this.addFault(input, statesHotCopy,rulesHotCopy, true);
					continue;
				}else if(nextS.isForbiddenState()||nextS.getInputSpace().isForbiddenInput(input))
				{
					//forbidden state reached
					this.addFault(input, statesHotCopy,rulesHotCopy, false);
					continue;
				}else
				{
					//next step
					this.exploreState(nextS, input, statesHotCopy, rulesHotCopy);
				} 
			}
		}
	}


	private void addFault(long input, List<State> states, List<Rule> rules, boolean cycle) {
		assert(states.size()==rules.size()+1);
		
		//at least 3 states
		if(states.size()<3){return;}
		
		//this._detectedFaults.addElement(new RaceFault(new String(FaultDetectionAlgorithm.longToInputSequence(input, this.getAFSMUnderTest().getInputDimension())),states.toArray(new State[states.size()]),rules.toArray(new Rule[rules.size()]),cycle));
		

		Set<PredicateVariable> varVector = new HashSet<PredicateVariable>();
		for(Rule r:rules)
		{
			PredicateVariable[] v=r.getCondition().getVariables();
			for(int i=0;i<v.length;i++)
			{
				varVector.add(v[i]);
			}
		}
		
		char[] inputSeq=FaultDetectionAlgorithm.longToInputSequence(input, this.getAFSMUnderTest().getInputDimension());
		char[] mask=new char[inputSeq.length];
		for(int k=0;k<mask.length;k++)
		{
			mask[k]='*';
		}
		for (PredicateVariable v : varVector) {
		//for(int i=0;i<varVector.size();i++){
			//int index=varVector.get(i).getIndexInSpace();
			int index = v.getIndexInSpace();
			mask[index] = inputSeq[index];
		}
		RaceFault fault=new RaceFault(new String(inputSeq),states.toArray(new State[states.size()]),rules.toArray(new Rule[rules.size()]),cycle);
		
		String key=new String(mask);
		if(this._reducedFaults.containsKey(key)==false)
		{
			this._reducedFaults.put(key, new ArrayList<RaceFault>());
		}
		List<RaceFault> v = this._reducedFaults.get(key);
		//only add new fault
		//rememeber that isEqual use also input so.... change it 
		fault.setInput(key);
		if(v.contains(fault)==false)
		{
			v.add(fault);
			//statistics
			if(cycle){this._foundFaults[1]++;}else{this._foundFaults[0]++;}
		}
	
	}


	@Override
	public Fault[] applyRegression(Fault[] faults) {
		return faults;		
	}
	


	@Override
	public void printFaultsToStream(Fault[] fault, PrintWriter pw) {
		if(fault==null){throw new RuntimeException("Fault array cannot be null");}
		if(pw==null){throw new RuntimeException("PrintWriter array cannot be null");}
		
		pw.println(fault.length+" instances of races and cycles.");
		pw.println("Input\tType\tActivation chain (state,rule)->");
		for(Fault f:fault)
		{
			RaceFault ff=(RaceFault)f;
			//pw.println(ff.getCurrentState().getName()+"\t"+ff.getNextState().getName()+"\t"+ff.getEndState().getName()+"\t"+ff.getInput());
			pw.print(ff.getInput()+"\t"+(ff.isCycle()?"Cycle":"Race")+"\t");
			for(int i=0;i<ff.getRules().length;i++)
			{
				pw.print("("+ff.getStates()[i].getName()+","+ff.getRules()[i].getName()+")->");
			}
			pw.println(ff.getStates()[ff.getStates().length-1].getName());
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
		
		pw.println("Race and cycle detection: "+this.getAFSMUnderTest().stateSize()+" states explored in "+totalTime+" msec.");
		pw.println("Races: "+this._foundFaults[0]+" Cycles: "+this._foundFaults[1]);
		pw.println("State\tVariables\tExploredPaths\tSkippedPaths\tTime");
		for(int i=0;i<this._paths.length;i++)
		{
			pw.println(this.getAFSMUnderTest().stateAt(i)+"\t"+
					this.getAFSMUnderTest().stateAt(i).getVariables().length+"\t"+
					this._paths[i][0]+"\t"+this._paths[i][1]+"\t"+
					this._time[i]);
		}
	}
}
