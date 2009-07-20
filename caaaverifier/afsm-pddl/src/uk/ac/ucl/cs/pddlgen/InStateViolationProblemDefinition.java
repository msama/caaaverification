/**
 * 
 */
package uk.ac.ucl.cs.pddlgen;

import java.util.ArrayList;
import java.util.List;

import uk.ac.ucl.cs.afsm.common.AdaptationFiniteStateMachine;
import uk.ac.ucl.cs.afsm.common.State;
import uk.ac.ucl.cs.pddlgen.ebnf.GD;
import uk.ac.ucl.cs.pddlgen.ebnf.Goal;
import uk.ac.ucl.cs.pddlgen.ebnf.Name;
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
			
			Goal goal = Goal.create(GD.createAnd(
					parser.getStateGD(state),
					GD.createNot(parser.getInStateAssumptionGD(state))));
			List<Goal> goals = new ArrayList<Goal>();
			goals.add(goal);
			
			Problem problem = new Problem(
					parser.getDomainName(), 
					Name.create("Reachability_" + state.getName()),
					parser.getRequireDef(),
					null, // Situation
					null,//objectDeclaration,
					createInit(),
					goals,
					null// length
					);
			problems.add(problem);
		}
		return problems;
	}

	
}
