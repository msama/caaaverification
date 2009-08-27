/**
 * 
 */
package uk.ac.ucl.cs.pddlgen;

import java.util.ArrayList;
import java.util.List;

import uk.ac.ucl.cs.afsm.common.AdaptationFiniteStateMachine;
import uk.ac.ucl.cs.afsm.common.Rule;
import uk.ac.ucl.cs.afsm.common.State;
import uk.ac.ucl.cs.afsm.common.predicate.Operator;
import uk.ac.ucl.cs.afsm.common.predicate.Predicate;
import uk.ac.ucl.cs.pddlgen.ebnf.GD;
import uk.ac.ucl.cs.pddlgen.ebnf.Goal;
import uk.ac.ucl.cs.pddlgen.ebnf.Name;
import uk.ac.ucl.cs.pddlgen.ebnf.PreGD;
import uk.ac.ucl.cs.pddlgen.ebnf.PrefGD;
import uk.ac.ucl.cs.pddlgen.ebnf.Problem;

/**
 * @author rax
 *
 */
public class InStateViolationProblemDefinition extends ProblemGenerator {

	/**
	 * @param afsm
	 * @param parser
	 */
	public InStateViolationProblemDefinition(AdaptationFiniteStateMachine afsm,
			AfsmParser parser) {
		super(afsm, parser);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ProblemGenerator#createProblems()
	 */
	@Override
	public List<Problem> createProblems() {
		List<Problem> problems = new ArrayList<Problem>();
		
		for (State state : afsm.states) {
			
			GD assumption = null;
			try {
				// Skip states without any assumptions.
				assumption = parser.createInStateAssumptionGDForProblem(state);
			} catch (IllegalStateException ex) {
				continue;
			}
			/*
			List<Predicate> predicates = new ArrayList<Predicate>();
			for (Rule r : state.outGoingRules) {
				predicates.add(r.getTrigger());
			}
			Predicate and = Operator.and(predicates);
			GD outgoing = parser.parsePredicate(and);
			*/
			Goal goal = Goal.create(
					PreGD.create(
							PrefGD.create(
									GD.createAnd(
											parser.createStateGDForProblem(state),
											GD.createNot(assumption)//,
											//GD.createNot(outgoing)
									)
							)
					)
			);
			List<Goal> goals = new ArrayList<Goal>();
			goals.add(goal);
			
			Problem problem = new Problem(
					parser.getDomainName(), 
					Name.create("Reachability_" + state.getName()),
					parser.getRequireDef(),
					null, // Situation
					createObject(),
					createInit(),
					goals,
					null, // Constraints
					null// length
					);
			problems.add(problem);
		}
		return problems;
	}

	
}
