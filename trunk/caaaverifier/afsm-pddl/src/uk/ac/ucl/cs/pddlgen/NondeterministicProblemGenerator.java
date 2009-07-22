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
 * @author -RAX- (Michele Sama)
 *
 */
public class NondeterministicProblemGenerator extends ProblemGenerator {

	/**
	 * @param afsm
	 * @param parser
	 */
	public NondeterministicProblemGenerator(AdaptationFiniteStateMachine afsm,
			AfsmParser parser) {
		super(afsm, parser);
	}

	@Override
	public List<Problem> createProblems() {
		List<Problem> problems = new ArrayList<Problem>();
		
		for (int i = 0; i < afsm.rules.size() - 1; i++) {
			Rule rule1 = afsm.rules.get(i);
			for (int j = i + 1; j < afsm.rules.size(); j++) {
				Rule rule2 = afsm.rules.get(j);
				
				if (!haveCommonStates(rule1,rule2)) {
					continue;
				}
				
				Goal goal = Goal.create(
						GD.createAnd(getCommonStatesGD(rule1,rule2),
								parser.getRuleGD(rule1), 
								parser.getRuleGD(rule2)));
				List<Goal> goals = new ArrayList<Goal>();
				goals.add(goal);
				
				Problem problem = new Problem(
						parser.getDomainName(), 
						Name.create("Nondeterministic_"+rule1.getName() + "_" + rule2.getName()),
						parser.getRequireDef(),
						null, // Situation
						createObject(),
						createInit(),
						goals,
						null// length
						);
				problems.add(problem);
			}
		}
		return problems;
	}

	private boolean haveCommonStates(Rule rule1, Rule rule2) {
		for (State s : afsm.states) {
			if (s.outGoingRules.contains(rule1) && s.outGoingRules.contains(rule2)) {
				return true;
			}
		}
		return false;
	}
	
	private GD getCommonStatesGD(Rule rule1, Rule rule2) {
		List<GD> gds = new ArrayList<GD>();
		for (State s : afsm.states) {
			if (s.outGoingRules.contains(rule1) && s.outGoingRules.contains(rule2)) {
				gds.add(parser.getStateGD(s));
			}
		}
		return GD.createAnd(gds.toArray(new GD[gds.size()]));
	}

}
