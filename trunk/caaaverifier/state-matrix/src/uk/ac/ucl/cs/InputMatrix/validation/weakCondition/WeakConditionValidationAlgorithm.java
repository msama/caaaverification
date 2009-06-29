/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix.validation.weakCondition;

import static uk.ac.ucl.cs.InputMatrix.AndPredicate.*;
import static uk.ac.ucl.cs.InputMatrix.NotPredicate.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import uk.ac.ucl.cs.InputMatrix.Fault;
import uk.ac.ucl.cs.InputMatrix.Predicate;
import uk.ac.ucl.cs.InputMatrix.PredicateVariable;
import uk.ac.ucl.cs.InputMatrix.State;
import uk.ac.ucl.cs.InputMatrix.validation.FaultDetectionAlgorithm;
import uk.ac.ucl.cs.InputMatrix.validation.InStateFault;

/**
 * @author rax
 *
 */
public class WeakConditionValidationAlgorithm extends FaultDetectionAlgorithm {

	protected long[] time;
	protected int[] variables;
	protected long[] exploredPaths;
	protected long[] foundFaults;
	
	/**
	 * 
	 */
	public WeakConditionValidationAlgorithm() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.InputMatrix.validation.FaultDetectionAlgorithm#applyRegression(uk.ac.ucl.cs.InputMatrix.Fault[])
	 */
	@Override
	public Fault[] applyRegression(Fault[] fault) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.InputMatrix.validation.FaultDetectionAlgorithm#detectFaults()
	 */
	@Override
	public Fault[] detectFaults() {
		List<Fault> faultList = new ArrayList<Fault>();
		time = new long[this.getAFSMUnderTest().stateSize()];
		variables = new int[this.getAFSMUnderTest().stateSize()];
		exploredPaths = new long[this.getAFSMUnderTest().stateSize()];
		foundFaults = new long[this.getAFSMUnderTest().stateSize()];
		
		for (int i = 0; i < getAFSMUnderTest().stateSize(); i++) {
			State state = getAFSMUnderTest().stateAt(i);
			time[i] = - System.currentTimeMillis();
			
			if (state.getHoldCondition() == null || state.getInStateAssumptions() == null) {
				time[i] += System.currentTimeMillis();
				continue;
			}
			
			Predicate empty = and(state.getHoldCondition(), not(state.getInStateAssumptions())); 
			PredicateVariable[] vars =  empty.getVariables();
			variables[i] = vars.length;
			
			long max = (long) Math.pow(2, vars.length);
			// does looping on a double reduces the speed in 64 bit machines?
			for (long l = 0; l < max; l ++) {
				char[] currentConfig = longToInputSequence(l, vars.length);
				long input = inputFromVariables(vars, currentConfig);
				
				if (empty.getValue((int) input)) {
					faultList.add(new InStateFault(new String(generalInputFromVariables(vars, currentConfig)), state));
					foundFaults[i] ++;
				}
			}
			exploredPaths[i] = max;
			time[i] += System.currentTimeMillis();
		}
		Fault[] faults = new Fault[faultList.size()];
		return faultList.toArray(faults);
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.InputMatrix.validation.FaultDetectionAlgorithm#printFaultsToStream(uk.ac.ucl.cs.InputMatrix.Fault[], java.io.PrintWriter)
	 */
	@Override
	public void printFaultsToStream(Fault[] fault, PrintWriter pw) {
		if (fault == null) {
			throw new RuntimeException("Fault array cannot be null");
		}
		if (pw == null) {
			throw new RuntimeException("PrintWriter array cannot be null");
		}
		
		pw.println(fault.length  +" instances of weak conditions.");
		pw.println("State\tInput");
		for(Fault f:fault)
		{
			InStateFault ff = (InStateFault) f;
			pw.println(ff.getState().getName()+"\t"+ff.getInput());
		}
		pw.flush();
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.InputMatrix.validation.FaultDetectionAlgorithm#printStatisticsToStream(java.io.PrintWriter)
	 */
	@Override
	public void printStatisticsToStream(PrintWriter pw) {
		if (pw == null) {
			throw new RuntimeException("PrintWriter array cannot be null");
		}
		
		long totalTime=0;
		for(long t : time)
		{
			totalTime += t;
		}
		
		pw.println("NondeterministicActivations detection: "+this.getAFSMUnderTest().stateSize()+" states explored in "+totalTime+" msec.");
		pw.println("State\tVariables\tPaths\tFaults\tTime");
		for (int i = 0; i < getAFSMUnderTest().stateSize(); i++) {
			pw.println(getAFSMUnderTest().stateAt(i)+"\t"+
					variables[i] + "\t" +
					exploredPaths[i] + "\t" +
					foundFaults[i] + "\t"+
					time[i]);
		}
	}

}
