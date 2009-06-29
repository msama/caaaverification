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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.ac.ucl.cs.InputMatrix.AdaptationFSM;
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
public class UnrechableStatesDetection extends FaultDetectionAlgorithm {

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
		
		AdaptationFSM afsm = getAFSMUnderTest();
		List<State> next = new ArrayList<State>();
		Set<State> toExplore = new HashSet<State>();
		next.add(afsm.getInitialState());
		Set<State> unreached = new HashSet<State>();
		for (int i = 0; i < afsm.stateSize(); i++) {
			unreached.add(afsm.stateAt(i));
		}
		
		while (!next.isEmpty()) {
			for (State s : next) {
				int stateIndex = afsm.indexOfState(s);
				_time[stateIndex] = -System.currentTimeMillis();
				unreached.remove(s);
				
				//--
				PredicateVariable[] vars = s.getVariables();
				int subspaceSize = vars.length;
				long maxSpaceSize = (long) Math.pow(2, subspaceSize);
				InputSpace iSpace = s.getInputSpace();
				
				for(long l = 0; l < maxSpaceSize; l++)
				{	
					char[] currentConfig = FaultDetectionAlgorithm.longToInputSequence(l, subspaceSize);
					long input=this.inputFromVariables(vars, currentConfig);
					
					if(iSpace.isForbiddenInput(input)==true){continue;}else{this._paths[stateIndex][0]++;}
					
					Rule[] rules=iSpace.getSatisfiedRules(input);
					if(rules==null||rules.length==0){continue;}
					Rule highPriorityRule = null;
					
					for(Rule r:rules)
					{
						if(highPriorityRule==null||r.hasHigherPriority(highPriorityRule))
						{
							highPriorityRule=r;
						}
					}	
					if(highPriorityRule!=null)
					{
						State nextState = ((AdaptationRule)highPriorityRule).getAdaptationNextState();
						if (unreached.contains(nextState)) {
							toExplore.add(nextState);
						}
					}
				}
				//--
				
				_time[stateIndex] += System.currentTimeMillis();
			}
			next.clear();
			next.addAll(toExplore);
			toExplore.clear();
		}
		for (State s : unreached) {
			_detectedFaults.add(new UnreachableStateFault("", s));
			int stateIndex = afsm.indexOfState(s);
			_foundFaults[stateIndex]++;
		}
		
		return this._detectedFaults.toArray(new Fault[this._detectedFaults.size()]);
	}

	@Override
	public Fault[] applyRegression(Fault[] fault) {
		return fault;
	}


	@Override
	public void printFaultsToStream(Fault[] fault, PrintWriter pw) {
		if(fault==null){throw new RuntimeException("Fault array cannot be null");}
		if(pw==null){throw new RuntimeException("PrintWriter array cannot be null");}
		
		pw.println(fault.length+" instances of unreachable states.");
		pw.println("State");
		for(Fault f:fault)
		{
			UnreachableStateFault ff=(UnreachableStateFault)f;
			pw.println(ff.getState().getName());
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
		
		pw.println("Unreachable states detection: "+this.getAFSMUnderTest().stateSize()+" states explored in "+totalTime+" msec.");
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
