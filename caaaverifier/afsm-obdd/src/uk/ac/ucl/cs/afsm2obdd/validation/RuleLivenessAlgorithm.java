/**
 * 
 */
package uk.ac.ucl.cs.afsm2obdd.validation;

import java.util.ArrayList;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import uk.ac.ucl.cs.afsm2obdd.AfsmParser;
import uk.ac.ucl.cs.afsm2obdd.RuleBDD;
import uk.ac.ucl.cs.afsm2obdd.StateBDD;

/**
 * @author -RAX- (Michele Sama)
 *
 */
public class RuleLivenessAlgorithm extends Algorithm {

	/**
	 * 
	 */
	public RuleLivenessAlgorithm() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * For each state checks if all the rule are satisfied for at least a configuration of inputs. 
	 * The complexity is S*R*R because while checking each single rule we must remove rules with higher priority.
	 * 
	 * @see uk.ac.ucl.cs.afsm2obdd.validation.Algorithm#computeLinear(uk.ac.ucl.cs.afsm2obdd.AfsmParser)
	 */
	@Override
	public Fault[] computeLinear(AfsmParser parser) {
		System.out.println("RuleLiveness computeLinear");
		long time = - System.currentTimeMillis();
		ArrayList<DeadRule> faults = new ArrayList<DeadRule>();
		for (StateBDD state : parser.states) {
			BDD stateActivation = state.getActivation();
			// Checks all the rules minus the rules with higher priority
			for (RuleBDD rule : state.outGoingRules) {
				//
				BDD faultInstance = stateActivation.apply(rule.getEncoding(), BDDFactory.and);
				//
				if (faultInstance.isZero()) {
					faults.add(new DeadRule(state.getName(), rule.getName()));
				}
			}
		}
		
		Fault[] faultsArray = new Fault[faults.size()]; 
		faultsArray = faults.toArray(faultsArray);
		
		time += System.currentTimeMillis();
		System.out.println("RuleLiveness computeLinear time to compute the faults: " + time + "ms.");
		return faultsArray;
	}

	/**
	 * For each state and each rule checks if the rule has at least a solution in the activation BDD
	 * the complexity is S*R.
	 * 
	 * @see uk.ac.ucl.cs.afsm2obdd.validation.Algorithm#computeSymbolic(uk.ac.ucl.cs.afsm2obdd.AfsmParser)
	 */
	@Override
	public Fault[] computeSymbolic(AfsmParser parser) {
		System.out.println("RuleLiveness computeSymbolic");
		long time = - System.currentTimeMillis();
		
		// The following exists could be removed.
		BDD faults = parser.getGlobalActivation()
				.exist(parser.getVarVarSet())
				.exist(parser.getFutureStateVarSet());
		
		ArrayList<DeadRule> solutions = new ArrayList<DeadRule>();
		for (StateBDD state : parser.states) {
			BDD faultsInState = faults.apply(state.getEncoding(), BDDFactory.and);
			for (RuleBDD rule : state.outGoingRules) {
				BDD faultInstance = faultsInState.apply(rule.getEncoding(), BDDFactory.and);
				if (faultInstance.isZero()) {
					solutions.add(new DeadRule(state.getName(), rule.getName()));
				}
			}
		}
		
		Fault[] faultsArray = new Fault[solutions.size()]; 
		faultsArray = solutions.toArray(faultsArray);
		
		time += System.currentTimeMillis();
		System.out.println("RuleLiveness computeSymbolic time to compute the faults: " + time + "ms.");
		
		return faultsArray;
	}
	
}
