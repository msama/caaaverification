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
import uk.ac.ucl.cs.pddlgen.ebnf.ObjectDeclaration;
import uk.ac.ucl.cs.pddlgen.ebnf.Problem;
import uk.ac.ucl.cs.pddlgen.ebnf.TypedList;

public abstract class ProblemGenerator {

	protected AdaptationFiniteStateMachine afsm;
	protected AfsmParser parser;

	/**
	 * @param folderName
	 * @param afsm
	 * @param parser
	 */
	public ProblemGenerator(AdaptationFiniteStateMachine afsm, AfsmParser parser) {
		this.afsm = afsm;
		this.parser = parser;
	}	
	
	public abstract List<Problem> createProblems();
	
	protected ObjectDeclaration createObject() {
		TypedList<Name> typedList = null;
		typedList = TypedList.create(AfsmParser.STATE_VARIABLE_NAME, AfsmParser.STATE_TYPE, typedList);
		typedList = TypedList.create(AfsmParser.CONTEXT_VARIABLE_NAME, AfsmParser.CONTEXT_TYPE, typedList);
		return ObjectDeclaration.create(typedList);
	}
	
	public void save(Problem problem, String folderName) {
		String filename = folderName + "/" + problem.getProblemName() + ".pddl";
		try {
			PrintStream ps = new PrintStream(new File(filename));
			problem.startWriting(ps);
			ps.flush();
			ps.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void saveAll(String folderName) {
		File folder = new File(folderName);
		folder.mkdirs();
		folder = null;
		
		List<Problem> problems = createProblems();
		for (Problem p : problems) {
			save(p, folderName);
		}
	}
	
	protected Init createInit() {
		List<Literal<Name>> list = new ArrayList<Literal<Name>>();
		// Initial state
		list.add(Literal.createFormula(parser.createStateFormulaForProblem(afsm.getInitialState())));
		// Context
		//list.addAll(parser.createInitiContextLiteralsForProblem());
		return Init.create(list);
	}
}
