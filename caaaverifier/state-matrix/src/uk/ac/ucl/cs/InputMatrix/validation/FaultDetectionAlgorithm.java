/**
 * Abstract class to define the concept of algorithm
 */
package uk.ac.ucl.cs.InputMatrix.validation;

import java.io.PrintWriter;
import java.util.List;

import uk.ac.ucl.cs.InputMatrix.AdaptationFSM;
import uk.ac.ucl.cs.InputMatrix.Fault;
import uk.ac.ucl.cs.InputMatrix.PredicateVariable;

/**
 * @author rax
 *
 */
public abstract class FaultDetectionAlgorithm {
	
	protected List<Fault> _detectedFaults=null;
	private AdaptationFSM _AFSMUnderTest;

	
	public abstract Fault[] detectFaults();
	
	public abstract Fault[] applyRegression(Fault[] fault);
	
	public abstract void printFaultsToStream(Fault[] fault, PrintWriter pw);
	
	public abstract void printStatisticsToStream(PrintWriter pw);
	
	protected boolean _skipForbiddenStates=true;
	
	/**
	 * @param _AFSMUnderTest the _AFSMUnderTest to set
	 */
	public void setAFSMUnderTest(AdaptationFSM _AFSMUnderTest) {
		if(this._AFSMUnderTest!=_AFSMUnderTest)
		{
			this._detectedFaults=null;
		}
		this._AFSMUnderTest = _AFSMUnderTest;
	}


	/**
	 * @return the _AFSMUnderTest
	 */
	public AdaptationFSM getAFSMUnderTest() {
		return _AFSMUnderTest;
	}

	/**
	 * Given an array of variables and a string of inputs generates a long input value. Wildchars '*' are substituted with 0
	 * @param var an array containing all the variables taken in account
	 * @param localInput an input of  variable values
	 * @return the input according to the state machine
	 */
	public long inputFromVariables(PredicateVariable[] var, char[] localInput)
	{
		char[] c = generalInputFromVariables(var, localInput);
		long l=0;
		for(int i=0;i<c.length;i++)
		{
			if(c[i]=='1'){
				//sum the number
				l+=Math.pow(2, i);
			}
		}
		return l;
	}

	/**
	 * Given an array of variables and a string of inputs generates a char[] with size specified in the A-FSM with variable values according to the input array 
	 * @param var an array containing all the variables taken in account
	 * @param localInput an input of  variable values
	 * @return the input according to the state machine
	 */
	public char[] generalInputFromVariables(PredicateVariable[] var, char[] localInput)
	{
		if(var.length!=localInput.length)throw new RuntimeException("Local input must have the same size as the number of variable in current state.");
		if(var.length>this.getAFSMUnderTest().getInputDimension())throw new RuntimeException("The current state contains more variables than the hole A-FSM: sth must be wrong.");
		char[] c=new char[this.getAFSMUnderTest().getInputDimension()];
		for(int i=0;i<c.length;i++)
		{
			c[i]='*';
		}
		for(int i=0;i<localInput.length;i++)
		{
			c[var[i].getIndexInSpace()]=localInput[i];
		}
		
		return c;
	}
	
	
	public static char[] longToInputSequence(long l, int numVar)
	{
		char c[]=new char[numVar];
		for(int i=0;i<numVar;i++)
		{
			c[i]=(l%2==1)?'1':'0';
			l/=2;
		}
		return c;
		
		//we need a char[] with '1', '0' from l.
		//we also need a fixed number of char so we must fill it
		/*String stringCurrentConfig=Long.toBinaryString(l);
		char[] filler=new char[numVar-stringCurrentConfig.length()];
		for(int c=0;c<filler.length;c++)
		{
			filler[c]='0';
		}
		char[] currentConfig=(stringCurrentConfig+new String(filler)).toCharArray();
		assert(currentConfig.length==numVar);
		return currentConfig;*/
	}
}
