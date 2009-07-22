/**
 * 
 */
package uk.ac.ucl.cs.pddlgen;

import java.util.ArrayList;
import java.util.List;

import uk.ac.ucl.cs.afsm.common.AdaptationFiniteStateMachine;
import uk.ac.ucl.cs.afsm.common.Rule;
import uk.ac.ucl.cs.afsm.common.State;
import uk.ac.ucl.cs.pddlgen.ebnf.GD;
import uk.ac.ucl.cs.pddlgen.ebnf.Goal;
import uk.ac.ucl.cs.pddlgen.ebnf.Name;
import uk.ac.ucl.cs.pddlgen.ebnf.Problem;

/**
 * @author rax
 *
 */
public class RuleLivenessProblemDefinition extends ProblemGenerator {

	/**
	 * @param afsm
	 * @param parser
	 */
	public RuleLivenessProblemDefinition(AdaptationFiniteStateMachine afsm,
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
		
		for (Rule rule : afsm.rules) {
			if (!hasInitialStates(rule)) {
				continue;
			}
			
			Goal goal = Goal.create(
					GD.createAnd(getStatesGD(rule),
					parser.getRuleGD(rule)));
			List<Goal> goals = new ArrayList<Goal>();
			goals.add(goal);
			
			Problem problem = new Problem(
					parser.getDomainName(), 
					Name.create("RuleLiveness_"+rule.getName()),
					parser.getRequireDef(),
					null, // Situation
					createObject(),
					createInit(),
					goals,
					null// length
					);
			problems.add(problem);
		}
		return problems;
	}

	private boolean hasInitialStates(Rule rule) {
		for (State s : afsm.states) {
			if (s.outGoingRules.contains(rule)) {
				return true;
			}
		}
		return false;
	}
	
	private GD getStatesGD(Rule rule) {
		List<GD> gds = new ArrayList<GD>();
		for (State s : afsm.states) {
			if (s.outGoingRules.contains(rule)) {
				gds.add(parser.getStateGD(s));
			}
		}
		return GD.createAnd(gds.toArray(new GD[gds.size()]));
	}
}
