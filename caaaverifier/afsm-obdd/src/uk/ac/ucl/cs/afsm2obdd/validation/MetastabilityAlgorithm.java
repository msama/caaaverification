package uk.ac.ucl.cs.afsm2obdd.validation;

import java.util.ArrayList;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import uk.ac.ucl.cs.afsm2obdd.AfsmParser;
import uk.ac.ucl.cs.afsm2obdd.RuleBDD;
import uk.ac.ucl.cs.afsm2obdd.StateBDD;

public class MetastabilityAlgorithm extends Algorithm {

	@Override
	public Fault[] computeLinear(AfsmParser parser) {
		System.out.println("MetastabilityAlgorithm computeLinear");
		long time = - System.currentTimeMillis();
		
		// Decode faults
		ArrayList<Metastability> faultList = new ArrayList<Metastability>();
		for (StateBDD state : parser.states) {
			//--
			BDD activation = state.getActivation();
			for (RuleBDD rule : state.outGoingRules) {
				//
				BDD ruleActivation = activation.apply(rule.getEncoding(), BDDFactory.and)
						.exist(parser.getFutureStateVarSet())
						.exist(parser.getRuleVarSet())
						.exist(parser.getStateVarSet());
				
				BDD ruleEffect = rule.getAction().replace(parser.getActionPairingToVariables());
				
				ruleActivation = ruleActivation.apply(ruleEffect, BDDFactory.and);

				if (ruleActivation.isZero()) {
					continue; // Dead rule
				}
				
				// compares with the future activation
				BDD faultInstance = rule.getDestination().getActivation().apply(ruleActivation, BDDFactory.and);
				if (!faultInstance.isZero()) {
					faultList.add(
							new Metastability(faultInstance, 
									state.getName(), rule.getName(), rule.getDestination().getName()));
				}
			}
			//--
		}
		
		Metastability[] faultsArray = new Metastability[faultList.size()];
		faultsArray = faultList.toArray(faultsArray);
		
		time += System.currentTimeMillis();
		System.out.println("MetastabilityAlgorithm computeLinear " + time + "ms.");
		return faultsArray;
	}

	@Override
	public Fault[] computeSymbolic(AfsmParser parser) {
		System.out.println("MetastabilityAlgorithm computeSymbolic");
		long time = - System.currentTimeMillis();
		
		BDD activation = parser.getGlobalActivation();
		
		/*// predicates&action&-&future&rules
		BDD present = activation
			// Remove the future state in order to swap the current state in it.
			.exist(parser.getFutureStateVarSet());
		
		BDD effects = present
				.exist(parser.getVarVarSet())
				.replace(parser.getActionPairingToVariables());
		
		present = present.apply(effects, BDDFactory.and);*/
		
		// predicates&-&-&future&-
		BDD present = activation
				// action have been swapped and are not supposed to exist.
				// Remove the future state in order to swap the current state in it.
				.exist(parser.getFutureStateVarSet())
				// Remove the rules to match the future state as well. Otherwise it will only match two states loops
				.exist(parser.getRuleVarSet());
		
		BDD effects = present
			.exist(parser.getVarVarSet())
			.replace(parser.getActionPairingToVariables());

		present = present.apply(effects, BDDFactory.and);
		
		// this contains predicates&-&state&-
		BDD future = present.replace(parser.getStatePairingPastToFuture());
		
		// Match where predicates&states with predicates&futurestates 
		// because current states and states have been swapped
		// This returns a BDD with all
		BDD faults = activation.apply(future, BDDFactory.and);
		
		// Decode faults
		ArrayList<Metastability> faultList = new ArrayList<Metastability>();
		for (StateBDD state : parser.states) {
			BDD statefilter = state.getEncoding();
			for (RuleBDD rule : state.outGoingRules) {
				BDD stateRuleFilter = statefilter.apply(rule.getEncoding(), BDDFactory.and);
				BDD stateRuleFutureFilter = stateRuleFilter.apply(rule.getDestination().getFutureEncoding(), BDDFactory.and);
				BDD faultInstance = faults.apply(stateRuleFutureFilter, BDDFactory.and);
				if (!faultInstance.isZero()) {
					faultList.add(
							new Metastability(faultInstance, 
									state.getName(), rule.getName(), rule.getDestination().getName()));
				}
			}
		}
		
		Metastability[] faultsArray = new Metastability[faultList.size()];
		faultsArray = faultList.toArray(faultsArray);
		
		time += System.currentTimeMillis();
		System.out.println("MetastabilityAlgorithm computeSymbolic " + time + "ms.");
		
		return faultsArray;
	}
}
