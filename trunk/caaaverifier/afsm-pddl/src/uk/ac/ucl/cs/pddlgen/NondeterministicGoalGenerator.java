/**
 * 
 */
package uk.ac.ucl.cs.pddlgen;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import uk.ac.ucl.cs.afsm.common.AdaptationFiniteStateMachine;
import uk.ac.ucl.cs.afsm.common.Rule;
import uk.ac.ucl.cs.afsm.common.State;
import uk.ac.ucl.cs.pddlgen.ebnf.GD;
import uk.ac.ucl.cs.pddlgen.ebnf.Goal;
import uk.ac.ucl.cs.pddlgen.ebnf.Init;
import uk.ac.ucl.cs.pddlgen.ebnf.Literal;
import uk.ac.ucl.cs.pddlgen.ebnf.Name;
import uk.ac.ucl.cs.pddlgen.ebnf.ObjectDeclaration;
import uk.ac.ucl.cs.pddlgen.ebnf.Problem;
import uk.ac.ucl.cs.pddlgen.ebnf.TypedList;

/**
 * @author -RAX- (Michele Sama)
 *
 */
public class NondeterministicGoalGenerator {

	AdaptationFiniteStateMachine afsm;
	AfsmParser parser;
	
	/**
	 * @param afsm
	 * @param parser
	 */
	public NondeterministicGoalGenerator(AdaptationFiniteStateMachine afsm,
			AfsmParser parser) {
		this.afsm = afsm;
		this.parser = parser;
	}

	
	public List<Problem> createGoals() {
		List<Problem> problems = new ArrayList<Problem>();
		
		for (int i = 0; i < afsm.rules.size() - 1; i++) {
			Rule rule1 = afsm.rules.get(i);
			for (int j = i + 1; j < afsm.rules.size(); j++) {
				Rule rule2 = afsm.rules.get(j);
				
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
						null,//objectDeclaration,
						createInit(),
						goals,
						null// length
						);
			}
		}
		return problems;
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

	private Init createInit() {
		List<Literal<Name>> list = new ArrayList<Literal<Name>>();
		// Initial state
		list.add(Literal.createFormula(parser.getStateFormulaName(afsm.getInitialState())));
		// Context
		list.add(Literal.createFormula(parser.getContextFormulaName()));
		return Init.create(list);
	}
}
