/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix.validation;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import uk.ac.ucl.cs.InputMatrix.ContextVariable;
import uk.ac.ucl.cs.InputMatrix.Fault;
import uk.ac.ucl.cs.InputMatrix.State;
import uk.ac.ucl.cs.InputMatrix.PredicateVariable;

/**
 * Generates the minimum safe delays for a set of given hazards.
 * 
 * <p>The algorithm can be executed in assertion mode to detect possible 
 * inconsistencies.
 * 
 * @author Michele Sama aka -RAX-
 *
 */
public class MinimumSafeDelayGenerator {
	
	/**
	 * Contains for each state an hash table of delays addressed by the 
	 * corresponding sequence of activation.  
	 */
	protected Hashtable<State,Hashtable<String,Hashtable<String,Long>>> _indexedFaults = 
		new Hashtable<State,Hashtable<String,Hashtable<String,Long>>>();
	
	private long _numberOfComputedDelays;
	
	//statistics
	/**
	 * 
	 */
	private double[] _time=new double[]{0,0};
	

	/**
	 * Reset the internal collection of generated delays.
	 */
	public void reset() {
		// Clean the internal state 
		this._indexedFaults.clear();
		this._numberOfComputedDelays = 0;
	}

	/**
	 * Computes the minimum safe delay for the given faults and stores them in the internal 
	 * indexed collection sorting them by state, initial configuration and sequence.
	 * 
	 * <p>Sequential invocations of this method add new values to the inner collection. 
	 * If the same instance is used for different state machines then it must be reseted.
	 * 
	 * @param faults the array of faults for which the minimum safe delay has to be computed.
	 */
	public void generateDelays(Fault[] faults) {
		for (Fault fl : faults) {
			ContextHazardFault f = (ContextHazardFault) fl;
			
			// Retrieves the correct state or creates a new one if missing
			Hashtable<String,Hashtable<String,Long>> initialConfigurationInState = 
				this._indexedFaults.get(f.getState());
			if (initialConfigurationInState == null) {
				initialConfigurationInState = new Hashtable<String,Hashtable<String,Long>>();
				this._indexedFaults.put(f.getState(), initialConfigurationInState);
			}
			
			// Retrieve the collection of commutations of the given initial 
			// configuration in the given state or creates a new one if missing
			Hashtable<String,Long> commutationsInConfigurationInState = initialConfigurationInState.get(f.getInput());
			if(commutationsInConfigurationInState==null)
			{
				commutationsInConfigurationInState=new Hashtable<String,Long>();
				initialConfigurationInState.put(f.getInput(), commutationsInConfigurationInState);
			}
			
			PredicateVariable[] incrementalSequenceOfCommutations; 
			// Given a faulty sequence we incrementally generate the minimum safe delay for 
			// each step of the commutation. 
			for (int i = 0; i < f.getCriticalPath().length; i++) {
				// Regenerate the array with the commuting variables.
				incrementalSequenceOfCommutations = new PredicateVariable[i];
				for (int j = 0; j < i; j++) {
					incrementalSequenceOfCommutations[j]=f.getCriticalPath()[j];
				}
				
				long time = MinimumSafeDelayGenerator.getMinimumSafeDelay(f, 0, incrementalSequenceOfCommutations);
				this._time[0] += time;
				assert(this._time[0] > 0);
				this._time[1] += maxRefreshInput(incrementalSequenceOfCommutations);
				
				// If time == 0 then the sequence is now safe and the following combination can be pruned.
				// this is possible when two variable depends on the same ContextVariable and the delay becomes 0.
				if (time == 0) {
					break;
				}

				String sequence = generateStringRepresentationForSequence(incrementalSequenceOfCommutations);
				long previous = -1;
				if (commutationsInConfigurationInState.get(sequence) != null) {
					previous = commutationsInConfigurationInState.get(sequence).longValue();
				}
				
				if (time > previous) {
					commutationsInConfigurationInState.put(sequence, new Long(time));
				}
				this._numberOfComputedDelays++;
			}
		}
	}

	
	/**
	 * Generates a string representation of the given array of variables 
	 * to be used as a key to retrieve their delay.
	 * 
	 * @param incrementalSequenceOfCommutations
	 * @return 
	 */
	private String generateStringRepresentationForSequence(
			PredicateVariable[] incrementalSequenceOfCommutations) {
		StringBuffer sb=new StringBuffer();
		sb.append("{");
		for(int p=0;p<incrementalSequenceOfCommutations.length;p++)
		{
			sb.append(incrementalSequenceOfCommutations[p].getName());
			if(p!=incrementalSequenceOfCommutations.length-1)
			{
				sb.append(",");
			}
		}
		sb.append("}");
		return sb.toString();
	}
	
	
	/**
	 * Given a set of commuting variables returns
	 * 
	 * @param path
	 * @return
	 */
	public static long maxRefreshInput(PredicateVariable[] path) {
		long max=0;
		for(PredicateVariable v:path)
		{
			if(v.getContextVariable().getRefreshRate()>max)
			{
				max=v.getContextVariable().getRefreshRate();
			}
		}
		return max;
	}



	public static long getMinimumSafeDelay(ContextHazardFault fault, long elapsedTime, PredicateVariable[] actualCommutations)
	{
		if(actualCommutations.length>fault.getCriticalPath().length){throw new RuntimeException("Commutation list longer than critical path!");}
		else if(actualCommutations.length==fault.getCriticalPath().length){return 0;}
		
		
		long l=0;
		Vector<ContextVariable> commutedContext=new Vector<ContextVariable>();
		
		//add all the commuted context into a vector
		//check the context of the passed commutations
		int i=0;
		for(;i<actualCommutations.length;i++)
		{
			PredicateVariable v=actualCommutations[i];
			assert(v.equals(fault.getCriticalPath()[i]));
			if(commutedContext.contains(v.getContextVariable())==false)
			{
				commutedContext.addElement(v.getContextVariable());
			}
		}
		//explore expected commutations
		for(;i<fault.getCriticalPath().length;i++)
		{
			ContextVariable cv=fault.getCriticalPath()[i].getContextVariable();
			if(commutedContext.contains(cv)==false)
			{
				commutedContext.addElement(cv);
				if(elapsedTime<cv.getRefreshRate())
				{
					long t=cv.getRefreshRate()-elapsedTime;
					t=(t>0)?t:0;
					l=(l<t)?t:l;
				}
			}
		}
		
		return l;
	}
	
	
	
	public void printToStream(PrintWriter pw)
	{
		pw.println(this._numberOfComputedDelays+" MinimumSafeDelays suggested.");
		pw.println("State\tConfiguation\tCommutation\tMinimumSafeDelay");
		
		Enumeration<State> statesE=this._indexedFaults.keys();
		while(statesE.hasMoreElements())
		{
			State s=statesE.nextElement();
			Hashtable<String,Hashtable<String,Long>> confs=this._indexedFaults.get(s);
			Enumeration<String> confsE=confs.keys();
			while(confsE.hasMoreElements())
			{
				String c=confsE.nextElement();
				Hashtable<String,Long> delays=confs.get(c);
				Enumeration<String> delaysE=delays.keys();
				while(delaysE.hasMoreElements())
				{
					String vars=delaysE.nextElement();
					Long time=delays.get(vars);
					
					
					pw.println(s.getName()+"\t"+c+"\t"+vars+"\t"+time.longValue()+"-t");
				}
			}
		}
		pw.flush();
		System.out.println("Delay ["+this._time[0]+" - "+this._time[1]+"] "+(100*this._time[0]/this._time[1])+"%" );
	}
}
