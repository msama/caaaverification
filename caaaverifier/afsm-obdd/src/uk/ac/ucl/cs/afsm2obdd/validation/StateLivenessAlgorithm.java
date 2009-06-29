/**
 * 
 */
package uk.ac.ucl.cs.afsm2obdd.validation;

import java.util.ArrayList;
import java.util.HashSet;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import uk.ac.ucl.cs.afsm2obdd.AfsmParser;
import uk.ac.ucl.cs.afsm2obdd.RuleBDD;
import uk.ac.ucl.cs.afsm2obdd.StateBDD;

/**
 * @author -RAX- (Michele Sama)
 *
 */
public class StateLivenessAlgorithm extends Algorithm {

	/**
	 * 
	 */
	public StateLivenessAlgorithm() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * For each rule in each state checks the destination state. In the worst case the complexity is S
	 * 
	 * @see uk.ac.ucl.cs.afsm2obdd.validation.Algorithm#computeLinear(uk.ac.ucl.cs.afsm2obdd.AfsmParser)
	 */
	@Override
	public Fault[] computeLinear(AfsmParser parser) {
		System.out.println("StateLiveness computeLinear");
		long time = - System.currentTimeMillis();
		
		HashSet<StateBDD> states = new HashSet<StateBDD>();
		for (StateBDD state : parser.states) {
			BDD activation = state.getActivation();
			// add  unreachable states
			if (activation.isZero()) {
				states.add(state);
			}
		}
		
		ArrayList<DeadState> solutions = new ArrayList<DeadState>();
		for (StateBDD state : states) {
			solutions.add(new DeadState(state.getName()));
		}
		
		Fault[] faultsArray = new Fault[solutions.size()]; 
		faultsArray = solutions.toArray(faultsArray);
		
		time += System.currentTimeMillis();
		System.out.println("StateLiveness computeLinear time to compute the faults: " + time + "ms.");
		
		return faultsArray;
	}

	/**
	 * Checks if all the states are reachable in the global activation.
	 * The complexity is S.
	 * 
	 * @see uk.ac.ucl.cs.afsm2obdd.validation.Algorithm#computeSymbolic(uk.ac.ucl.cs.afsm2obdd.AfsmParser)
	 */
	@Override
	public Fault[] computeSymbolic(AfsmParser parser) {
		System.out.println("StateLiveness computeSymbolic");
		long time = - System.currentTimeMillis();
		
		BDD states = parser.getGlobalActivation();
			
		// TODO(rax) this exists can be removed
		states = states.exist(parser.getRuleVarSet())
				.exist(parser.getFutureStateVarSet())
				.exist(parser.getVarVarSet());
		
		ArrayList<DeadState> solutions = new ArrayList<DeadState>();
		for (StateBDD state : parser.states) {
			BDD faultInstance = states.apply(state.getEncoding(), BDDFactory.and);
			// If a state do not exists the it is dead
			if (faultInstance.isZero()) {
				solutions.add(new DeadState(state.getName()));
			}
		}
		
		Fault[] faultsArray = new Fault[solutions.size()]; 
		faultsArray = solutions.toArray(faultsArray);
		
		time += System.currentTimeMillis();
		System.out.println("StateLiveness computeSymbolic time to compute the faults: " + time + "ms.");
		
		return faultsArray;
	}

}
