/**
 * 
 */
package timerrific.obdd;

import timerrific.TimerrificAfsm;
import uk.ac.ucl.cs.afsm.common.AdaptationFiniteStateMachine;
import uk.ac.ucl.cs.afsm2obdd.AfsmParser;
import uk.ac.ucl.cs.afsm2obdd.validation.NondeterministicActivationAlgorithm;

/**
 * @author -RAX- (Michele Sama)
 *
 */
public class ValidateTimerrific {


	AdaptationFiniteStateMachine afsm;
	AfsmParser linearParser;
	AfsmParser symbolicParser;
	AfsmParser hybridParser;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ValidateTimerrific();
	}
	
	/**
	 * 
	 */
	public ValidateTimerrific() {
		afsm = (new TimerrificAfsm()).getAdaptationFiniteStateMachine();
		//linearParser = new AfsmParser(afsm, true, true);
		symbolicParser = new AfsmParser(afsm, true);
		//hybridParser = new AfsmParser(afsm, false, true);
		
		NondeterministicActivationAlgorithm nondet = new NondeterministicActivationAlgorithm();
		nondet.compute(symbolicParser);
	}
}
