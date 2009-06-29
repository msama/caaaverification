package uk.ac.ucl.cs.afsm2obdd.validation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import uk.ac.ucl.cs.afsm2obdd.AfsmParser;
import uk.ac.ucl.cs.afsm2obdd.StateBDD;

public class ReachabilityDetactionAlgorithm extends Algorithm {

	@Override
	public Fault[] computeLinear(AfsmParser parser) {
		System.out.println("UnreachableStates computeLinear");
		long time = - System.currentTimeMillis();
		
		Set<StateBDD> toExplore = new HashSet<StateBDD>();
		Set<StateBDD> toExploreNext = new HashSet<StateBDD>();
		Set<StateBDD> unexplored = new HashSet<StateBDD>();
		
		toExplore.add(parser.getInitialState());
		unexplored.addAll(parser.states); // all the states
		
		while (!toExplore.isEmpty()) {
			unexplored.removeAll(toExplore);
			for (StateBDD state : toExplore) {
				BDD next = state.getActivation();
					//.exist(parser.getStateVarSet())
					//.exist(parser.getRuleVarSet())
					//.exist(parser.getVarVarSet());
				for (StateBDD s : unexplored) {
					BDD reached = next.apply(s.getFutureEncoding(), BDDFactory.and);
					if (!reached.isZero()) {
						toExploreNext.add(s);
					}
				}
			}
			toExplore = toExploreNext;
			toExploreNext = new HashSet<StateBDD>();
		}
		
		Fault[] faultsArray = new Fault[unexplored.size()]; 
		int i = 0;
		for (StateBDD state : unexplored) {
			faultsArray[i++] = new UnreachableState(state.getName());
		}
		
		time += System.currentTimeMillis();
		System.out.println("UnreachableStates computeLinear time to compute the faults: " + time + "ms.");
		
		return faultsArray;
	}

	@Override
	public Fault[] computeSymbolic(AfsmParser parser) {
		System.out.println("UnreachableStates computeSymbolic");
		long time = - System.currentTimeMillis();
		
		BDD toExplore = parser.getInitialState().getEncoding();
		BDD unexplored = parser.getFactory().one(); // all the states
		BDD activations = parser.getGlobalActivation();
		
		while (!toExplore.isZero()) {
			unexplored = unexplored.apply(toExplore.not(), BDDFactory.and);	
			toExplore = activations.apply(toExplore, BDDFactory.and)
					.exist(parser.getStateVarSet())
					.exist(parser.getRuleVarSet())
					.exist(parser.getVarVarSet())
					.replace(parser.getStatePairingFutureToPast())
					.apply(unexplored, BDDFactory.and);	
		}
		
		ArrayList<UnreachableState> solutions = new ArrayList<UnreachableState>();
		for (StateBDD state : parser.states) {
			BDD faultInstance = unexplored.apply(state.getEncoding(), BDDFactory.and);
			// If a state exists the it has been unreached
			if (!faultInstance.isZero()) {
				solutions.add(new UnreachableState(state.getName()));
			}
		}
		
		Fault[] faultsArray = new Fault[solutions.size()]; 
		faultsArray = solutions.toArray(faultsArray);
		
		time += System.currentTimeMillis();
		System.out.println("UnreachableStates computeSymbolic time to compute the faults: " + time + "ms.");
		
		return faultsArray;
	}

}
