/**
 * 
 */
package uk.ac.ucl.cs.afsm.pddl.visitors;

import java.io.File;
import java.io.PrintStream;
import java.util.*;

import uk.ac.ucl.cs.afsm.pddl.Action;
import uk.ac.ucl.cs.afsm.pddl.Afsm;
import uk.ac.ucl.cs.afsm.pddl.AfsmVisitor;
import uk.ac.ucl.cs.afsm.pddl.CompositePredicate;
import uk.ac.ucl.cs.afsm.pddl.Event;
import uk.ac.ucl.cs.afsm.pddl.Predicate;
import uk.ac.ucl.cs.afsm.pddl.Rule;
import uk.ac.ucl.cs.afsm.pddl.Variable;
import uk.ac.ucl.cs.afsm.pddl.VariablePredicate;

/**
 * @author rax
 *
 */
public class PddlGenerator implements AfsmVisitor {
	
	private PrintStream ps;
	
	private int alignment = 0;
	
	/**
	 * @param ps
	 */
	public PddlGenerator(PrintStream ps) {
		this.ps = ps;
	}
	
	public PddlGenerator(String folderName) {
		File folder = new File(folderName);
		if (!folder.exists()) {
			folder.mkdirs();
		} else if (!folder.isDirectory()) {
			throw new IllegalArgumentException(folder + " must be a directory");
		}
		
		File file = new File(folderName + "/domain.pddl");
		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
			ps = new PrintStream(file);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void writeAlignment() {
		ps.print("\n");
		for (int i = 0; i < alignment; i++) {
			ps.print("\t");
		}
	}
	
	private void printActionParameters(Map<String, Variable> variables) {
		ps.print(":parameters (");
		boolean skipSpace = false;
		for (String key : variables.keySet()) {
			if (!skipSpace) {
				skipSpace = true;
			} else {
				ps.print(" ");
			}
			ps.print("?");
			ps.print(key);
			ps.print(" - ");
			ps.print(variables.get(key).getType());
		}
		ps.print(")");
	}
	
	private void writeEffect(Predicate effect) {
		ps.print(":effect");
		alignment ++;
		writeAlignment();
		effect.accept(this);
		alignment --;
	}

	private void writePrecondition(Predicate precontion) {
		ps.print(":precondition");
		alignment ++;
		writeAlignment();
		precontion.accept(this);
		alignment --;
	}
	
	private void printVariableTypes(Set<String> types) {
		ps.print("(:types");
		for (String type : types) {
			ps.print(" ");
			ps.print(type);
		}
		ps.print(")");
	}
	
	private void printPredicatesDeclaration(List<VariablePredicate> predicates) {
		ps.print("(:predicates");
		alignment ++;
		for (VariablePredicate predicate : predicates) {
			writeAlignment();
			printPredicateDeclaration(predicate);
		}
		alignment --;
		writeAlignment();
		ps.print(")");
	}
	
	private void printPredicateDeclaration(VariablePredicate predicate) {
		ps.print("(");
		ps.print(predicate.getUniqueName());
		Map<String, Variable> variables = predicate.getVariables();
		for (String name : variables.keySet()) {
			ps.print(" ?");
			ps.print(name);
			ps.print(" - ");
			ps.print(variables.get(name).getType());
		}
		ps.print(")");
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.afsm.pddl.AfsmVisitor#visit(uk.ac.ucl.cs.afsm.pddl.Afsm)
	 */
	@Override
	public void visit(Afsm afsm) {
		ps.println("; Self generated PDDL code. Do not edit manually ");
		
		ps.print("(define (domain ");
		ps.print(afsm.getName());
		ps.print(")");
		alignment ++;
		
		// Requirements
		// TODO(rax): include them dynamically
		writeAlignment();
		ps.print("(:requirements :typing :constraints :preferences)");
		
		// Types
		writeAlignment();
		printVariableTypes(afsm.getAllUsedVaribleTypes());
		
		// Predicates
		writeAlignment();
		printPredicatesDeclaration(afsm.getAllUsedVariablePredicates());
		
		// functions
		ps.print("(:functions (fired-events))");
		
		writeAlignment();
		
		// Events;
		writeAlignment();
		ps.print("; **********************************************");
		writeAlignment();
		ps.print("; Events");
		writeAlignment();
		ps.print("; **********************************************");
		writeAlignment();
		ps.print("; Events represent external events or direct user interaction or");
		writeAlignment();
		ps.print("; simply changes in the environment. Examples can be: GPS enabled");
		writeAlignment();
		ps.print("; by the user or GPS connection lost because of a change in the");
		writeAlignment();
		ps.print("; environment or a certain time has been reach.");
		for (Event evt : afsm.events) {
			writeAlignment();
			evt.accept(this);
			writeAlignment();
		}	
		
		//Rules
		writeAlignment();
		ps.print("; **********************************************");
		writeAlignment();
		ps.print("; Rules");
		writeAlignment();
		ps.print("; **********************************************");
		writeAlignment();
		ps.print("; Rules represent a state change on the A-FSM. From the application");
		writeAlignment();
		ps.print("; point of view it means that the application has performed an action");
		writeAlignment();
		ps.print("; changing its own configuration of applying a new behaviour.");
		writeAlignment();
		for (Rule rule : afsm.rules) {
			writeAlignment();
			rule.accept(this);
			writeAlignment();
		}
		
		alignment --;
		writeAlignment();
		ps.print(")");
		
		visitEnd();
	}

	/** 
	 * Writes <pre>variable-name<pre>
	 * 
	 * @see uk.ac.ucl.cs.afsm.pddl.AfsmVisitor#visit(uk.ac.ucl.cs.afsm.pddl.Variable)
	 */
	@Override
	public void visit(Variable variable) {
		ps.print(variable.getName());
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.afsm.pddl.AfsmVisitor#visit(uk.ac.ucl.cs.afsm.pddl.Rule)
	 */
	@Override
	public void visit(Action action) {
		ps.print("(:action ");
		ps.print(action.getName());
		alignment ++;
		writeAlignment();
		printActionParameters(action.getVariables());
		writeAlignment();
		writePrecondition(action.getPrecontion());
		writeAlignment();
		writeEffect(action.getEffect());

		alignment --;
		writeAlignment();
		ps.print(")");
	}

	/**
	 * Writes <pre>(predicate ?var1 ?var2)</pre>
	 * 
	 * @see uk.ac.ucl.cs.afsm.pddl.AfsmVisitor#visit(uk.ac.ucl.cs.afsm.pddl.VariablePredicate)
	 */
	@Override
	public void visit(VariablePredicate predicate) {
		ps.print("(");
		ps.print(predicate.getUniqueName());
		Map<String, Variable> variables = predicate.getVariables();
		for (String name : variables.keySet()) {
			ps.print(" ?");
			variables.get(name).accept(this);
		}
		ps.print(")");
	}

	/**
	 * Writes 
	 * <pre>
	 * (predicate
	 * {@value #ALIGMENT}[sub-pred]{@value #NEW_LINE}{@value #ALIGMENT}[sub-pred]{@value #NEW_LINE}
	 * )
	 * </pre>
	 * 
	 * @see uk.ac.ucl.cs.afsm.pddl.AfsmVisitor#visit(uk.ac.ucl.cs.afsm.pddl.CompositePredicate)
	 */
	@Override
	public void visit(CompositePredicate predicate) {
		ps.print("(");
		ps.print(predicate.getUniqueName());

		alignment ++;
		List<Predicate> predicates = predicate.getPredicates();
		for (Predicate pred : predicates) {
			writeAlignment();
			pred.accept(this);
		}
		alignment --;
		writeAlignment();
		ps.print(")");
	}

	@Override
	public void visitEnd() {
		if (ps == null) {
			return;
		}
		ps.flush();
		ps.close();
		ps = null;
	}

}
