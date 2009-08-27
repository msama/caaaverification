/**
 * 
 */
package falsepositivegps.obdd;

import falsePositive.GPSAFSM;
import phoneAdapter.PhoneAdapterAfsm;
import uk.ac.ucl.cs.afsm.common.AdaptationFiniteStateMachine;
import uk.ac.ucl.cs.afsm2obdd.AfsmParser;
import uk.ac.ucl.cs.afsm2obdd.validation.Fault;
import uk.ac.ucl.cs.afsm2obdd.validation.MetastabilityAlgorithm;
import uk.ac.ucl.cs.afsm2obdd.validation.NondeterministicActivationAlgorithm;
import uk.ac.ucl.cs.afsm2obdd.validation.ReachabilityDetactionAlgorithm;
import uk.ac.ucl.cs.afsm2obdd.validation.RuleLivenessAlgorithm;
import uk.ac.ucl.cs.afsm2obdd.validation.StateLivenessAlgorithm;

/**
 * @author -RAX- (Michele Sama)
 *
 */
public class ValidateFalsePositiveGPS {

	AdaptationFiniteStateMachine afsm;
	AfsmParser linearParser;
	AfsmParser symbolicParser;
	AfsmParser hybridParser;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ValidateFalsePositiveGPS();
	}

	public ValidateFalsePositiveGPS() {
		afsm = (new GPSAFSM()).getAdaptationFiniteStateMachine();
		long gen = -System.currentTimeMillis();
		linearParser = new AfsmParser(afsm, false);
		gen += System.currentTimeMillis();
		System.out.println("Linear model generated in :" +gen);
		gen = -System.currentTimeMillis();
		symbolicParser = new AfsmParser(afsm, true);
		gen += System.currentTimeMillis();
		System.out.println("Symbolic model generated in :" +gen);
		
		NondeterministicActivationAlgorithm nondet = new NondeterministicActivationAlgorithm();
		printArray(nondet.compute(symbolicParser));
		printArray(nondet.compute(linearParser));
		
		RuleLivenessAlgorithm ruleLiveness = new RuleLivenessAlgorithm();
		printArray(ruleLiveness.compute(symbolicParser));
		printArray(ruleLiveness.compute(linearParser));
		
		StateLivenessAlgorithm stateLiveness = new StateLivenessAlgorithm();
		printArray(stateLiveness.compute(symbolicParser));
		printArray(stateLiveness.compute(linearParser));
		
		MetastabilityAlgorithm metastability = new MetastabilityAlgorithm();
		printArray(metastability.compute(symbolicParser));
		printArray(metastability.compute(linearParser));
		
		ReachabilityDetactionAlgorithm reachability = new ReachabilityDetactionAlgorithm();
		printArray(reachability.compute(symbolicParser));
		printArray(reachability.compute(linearParser));
	}
	
	private static void printArray(Fault[] faults) {
		System.out.println("Found " + faults.length + " faults:");
		for (Fault f : faults) {
			System.out.println(f);
		}
	}
}
