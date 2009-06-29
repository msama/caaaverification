/**
 * 
 */
package uk.ac.ucl.cs.afsm2obdd.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.BDD.BDDIterator;
import uk.ac.ucl.cs.afsm2obdd.AfsmParser;
import uk.ac.ucl.cs.afsm2obdd.RuleBDD;
import uk.ac.ucl.cs.afsm2obdd.StateBDD;

/**
 * @author rax
 *
 */
public class NondeterministicActivationAlgorithm extends Algorithm {

	/**
	 * For each rule in each state checks if aother rules are overlapping.
	 * The complexity is S*(R*(R-1)/2)
	 * 
	 * <p>Note that this algorithm does not detect groups of 3 or more overlapping rules but only the single couples.
	 * 
	 * @see uk.ac.ucl.cs.afsm2obdd.validation.Algorithm#computeLinear(uk.ac.ucl.cs.afsm2obdd.AfsmParser)
	 */
	@Override
	public Fault[] computeLinear(AfsmParser parser) {
		System.out.println("NondeterministicActivation computeLinear");
		long time = - System.currentTimeMillis();
		
		ArrayList<Fault> faults = new ArrayList<Fault>();
		for (StateBDD state : parser.states) {
			// Combine each couple of rules
			for (int i = 0; i < state.outGoingRules.size() - 1; i++) {
				RuleBDD rule1 = state.outGoingRules.get(i);
				for (int j = i + 1; j < state.outGoingRules.size(); j++) {
					RuleBDD rule2 = state.outGoingRules.get(j);
					// Skips different priorities
					if (rule1.getPriority() != rule2.getPriority()) {
						continue;
					}
					
					BDD faultInstance = rule1.getTrigger().apply(rule2.getTrigger(), BDDFactory.and);
					if (state.getPriority() <= rule1.getPriority()) {
						faultInstance = faultInstance.apply(state.getInStateCondition().not(), BDDFactory.and);
					}
					if (!faultInstance.isZero()) {
						Fault f = new NondeterministicActivation(
								state.getName(), new String[]{rule1.getName(), rule2.getName()}, faultInstance);
						faults.add(f);
					}
					
				}
			}
		}
		
		Fault[] faultsArray = new Fault[faults.size()]; 
		faultsArray = faults.toArray(faultsArray);
		
		time += System.currentTimeMillis();
		System.out.println("NondeterministicActivation computeLinear " + time + "ms.");
		return faultsArray;
	}

	/**
	 * @see uk.ac.ucl.cs.afsm2obdd.validation.Algorithm#computeSymbolic(uk.ac.ucl.cs.afsm2obdd.AfsmParser)
	 * 
	 * For each rule in each state checks if other rules are active at the same time. The complexity is S*R to
	 * find faults *R in the worst case to decode them.
	 * 
	 * <p>Note that each violation is detected once per overlapping rules. For instance a group of 3
	 * overlapping rules will be detected 3 times.
	 */
	@Override
	public Fault[] computeSymbolic(AfsmParser parser) {
		System.out.println("NondeterministicActivation computeSymbolic");
		long time = - System.currentTimeMillis();
		
		ArrayList<Fault> faults = new ArrayList<Fault>();
		
		BDD activations = parser.getGlobalActivation()
			.exist(parser.getFutureStateVarSet())
			.exist(parser.getActionVarSet());
		
//		BDD inputs = activations
//			.exist(parser.getRuleVarSet());
		
		for (StateBDD state : parser.states) {
//			BDD inputsInState = inputs.apply(state.getEncoding(), BDDFactory.and);
			BDD activationInState = activations.apply(state.getEncoding(), BDDFactory.and).exist(parser.getStateVarSet());
			for (RuleBDD rule : state.outGoingRules) {
				BDD ruleActivation = activationInState.apply(rule.getEncoding(), BDDFactory.and)
						.exist(parser.getRuleVarSet());
				BDD faultInstance = activationInState.apply(ruleActivation, BDDFactory.and);
				ArrayList<String> ruleArray = new ArrayList<String>();
				ruleArray.add(rule.getName());
				BDD faultResult = parser.getFactory().zero();
				for (RuleBDD r : state.outGoingRules) {
					if (r.equals(rule)) { continue; }
					BDD f = faultInstance.apply(r.getEncoding(), BDDFactory.and);
					if (!f.isZero()) {
						faultResult = faultResult.apply(f, BDDFactory.or);
						ruleArray.add(r.getName());
					}
				}
				if (ruleArray.size() > 1) {
					String[] rules = new String[ruleArray.size()];
					rules = ruleArray.toArray(rules);
					Fault f = new NondeterministicActivation(
							state.getName(), rules, faultResult);	
					faults.add(f);
				}
			}
		}
        
        /*ArrayList<Fault> solutions = new ArrayList<Fault>();
        for (StateBDD state : parser.states) {
        	BDD activationInState = activations.apply(state.getEncoding(), BDDFactory.and);
        	for (int i = 0; i < state.outGoingRules.size() - 1; i++) {
				RuleBDD rule = state.outGoingRules.get(i);
				BDD inputs = activationInState.apply(rule.getEncoding(), BDDFactory.and)
						.exist(parser.getFutureStateVarSet())
						.exist(parser.getRuleVarSet());
				
				inputs = activationInState.apply(inputs, BDDFactory.and);
				
				BDD faultInstance = inputs
						.exist(parser.getStateVarSet())
						.exist(parser.getFutureStateVarSet())
						.exist(parser.getVarVarSet());
				

				ArrayList<String> ruleArray = new ArrayList<String>();
				for (RuleBDD r : state.outGoingRules) {
					if (!faultInstance.apply(r.getEncoding(), BDDFactory.and).isZero()) {
						ruleArray.add(r.getName());
					}
				}
				if (ruleArray.size() > 1) {
					String[] rules = new String[ruleArray.size()];
					rules = ruleArray.toArray(rules);
					Fault f = new NondeterministicActivation(
							state.getName(), rules, inputs);	
					solutions.add(f);
				}
        	}
    	}
        */

        Fault[] faultsArray = new Fault[faults.size()]; 
		faultsArray = faults.toArray(faultsArray);
		
		time += System.currentTimeMillis();
		System.out.println("NondeterministicActivation computeSymbolic " + time + "ms.");
		return faultsArray;
	}

	private void printComplexity(ArrayList<NondeterministicActivation> faults) {
		double numNodes = 0;
		double numPaths = 0;
		for (NondeterministicActivation act : faults) {
			numNodes += act.overlap.nodeCount();
			numPaths =+ act.overlap.pathCount();
		}
		numNodes /= faults.size();
		numPaths /= faults.size();
		System.out.println("Nondet Linear average nodes: " + numNodes);
		System.out.println("Nondet Linear average paths: " + numPaths);
	}
}
