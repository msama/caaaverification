package uk.ac.ucl.cs.pddlgen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import uk.ac.ucl.cs.afsm.common.AdaptationFiniteStateMachine;
import uk.ac.ucl.cs.pddlgen.ebnf.Init;
import uk.ac.ucl.cs.pddlgen.ebnf.Literal;
import uk.ac.ucl.cs.pddlgen.ebnf.Name;
import uk.ac.ucl.cs.pddlgen.ebnf.Problem;

public abstract class GoalGenerator {

	protected AdaptationFiniteStateMachine afsm;
	protected AfsmParser parser;

	/**
	 * @param folderName
	 * @param afsm
	 * @param parser
	 */
	public GoalGenerator(AdaptationFiniteStateMachine afsm, AfsmParser parser) {
		this.afsm = afsm;
		this.parser = parser;
	}	
	
	public abstract List<Problem> createProblems();
	
	public void save(Problem problem, String folderName) {
		String filename = folderName + "/" + problem.getProblemName();
		try {
			PrintStream ps = new PrintStream(new File(filename));
			problem.startWriting(ps);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void saveAll(String folderName) {
		List<Problem> problems = createProblems();
		for (Problem p : problems) {
			save(p, folderName);
		}
	}
	
	protected Init createInit() {
		List<Literal<Name>> list = new ArrayList<Literal<Name>>();
		// Initial state
		list.add(Literal.createFormula(parser.getStateFormulaName(afsm.getInitialState())));
		// Context
		list.add(Literal.createFormula(parser.getContextFormulaName()));
		return Init.create(list);
	}
}
