package uk.ac.ucl.cs.InputMatrix.validation.actions;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import uk.ac.ucl.cs.InputMatrix.AdaptationFSM;
import uk.ac.ucl.cs.InputMatrix.AndPredicate;
import uk.ac.ucl.cs.InputMatrix.Constraint;
import uk.ac.ucl.cs.InputMatrix.Fault;
import uk.ac.ucl.cs.InputMatrix.NotPredicate;
import uk.ac.ucl.cs.InputMatrix.Predicate;
import uk.ac.ucl.cs.InputMatrix.Rule;
import uk.ac.ucl.cs.InputMatrix.State;
import uk.ac.ucl.cs.InputMatrix.validation.FaultDetectionAlgorithm;

/**
 * Explores the {@link AdaptationFSM} looking for a sequence of transitions whose actions
 * violates one or more in-state-assumption.
 * 
 * <p>Given a {@link State} under test the algorithm tries to activate rules which violate 
 * the in state predicate and tries to go back in the {@link State} under test.
 * 
 * <p>In a first phase (1) the algorithm should try to activate conditions violating the assumptions 
 * of the state under tests. In a second phase (2) the algorithm should try to go to the state under
 * test without changing those conditions. 
 * 
 * @author rax
 *
 */
public class InStateViolationDetection extends FaultDetectionAlgorithm {
	
	private int executionTime = 60000;
	
	private GraphTraversalDelegate graphTraversal;
	
	
	/**
	 * 
	 */
	public InStateViolationDetection() {
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.InputMatrix.faultDetection.FaultDetectionAlgorithm#applyRegression(uk.ac.ucl.cs.InputMatrix.Fault[])
	 */
	@Override
	public Fault[] applyRegression(Fault[] fault) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.InputMatrix.faultDetection.FaultDetectionAlgorithm#detectFaults()
	 */
	@Override
	public Fault[] detectFaults() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.InputMatrix.faultDetection.FaultDetectionAlgorithm#printFaultsToStream(uk.ac.ucl.cs.InputMatrix.Fault[], java.io.PrintWriter)
	 */
	@Override
	public void printFaultsToStream(Fault[] fault, PrintWriter pw) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.InputMatrix.faultDetection.FaultDetectionAlgorithm#printStatisticsToStream(java.io.PrintWriter)
	 */
	@Override
	public void printStatisticsToStream(PrintWriter pw) {
		// TODO Auto-generated method stub

	}

	public Map<Rule, Double> computeRuleValue(State stateUnderTest) {
		Map<Rule,Double> valueMap = new HashMap<Rule, Double>();
		AdaptationFSM afsm = getAFSMUnderTest();
		Predicate assumption = stateUnderTest.getInStateAssumptions();
		
		for (int i = 0; i < afsm.stateSize(); i++) {
			State s = afsm.stateAt(i);
			if (s.equals(stateUnderTest)) {
				continue;
			}
			for (int j = 0; j < s.ruleSize(); j++ ) {
				Rule r =  s.ruleAt(j);
				if (valueMap.containsKey(r)) {
					continue;
				}
				Predicate action = r.getAppliedAction();
				double value = computeValue(assumption, action);
				valueMap.put(r, value);
			}
		}
		
		return valueMap;
	}

	private double computeValue(Predicate assumption, Predicate action) {
		// Create a action & !assumption
		Predicate p = new AndPredicate(action, new NotPredicate(assumption));
		return p.getSatisfaction();
	}
}
