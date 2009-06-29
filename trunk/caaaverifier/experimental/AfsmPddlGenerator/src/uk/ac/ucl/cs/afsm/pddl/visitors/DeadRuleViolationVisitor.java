/**
 * 
 */
package uk.ac.ucl.cs.afsm.pddl.visitors;

import java.io.File;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import uk.ac.ucl.cs.afsm.pddl.Action;
import uk.ac.ucl.cs.afsm.pddl.Afsm;
import uk.ac.ucl.cs.afsm.pddl.AfsmVisitor;
import uk.ac.ucl.cs.afsm.pddl.CompositePredicate;
import uk.ac.ucl.cs.afsm.pddl.Predicate;
import uk.ac.ucl.cs.afsm.pddl.Rule;
import uk.ac.ucl.cs.afsm.pddl.Variable;
import uk.ac.ucl.cs.afsm.pddl.VariablePredicate;

/**
 * @author rax
 *
 */
public class DeadRuleViolationVisitor implements AfsmVisitor {

	private File folder;
	
	private PrintStream ps;
	
	private int alignment = 0;
	
	/**
	 * 
	 */
	public DeadRuleViolationVisitor(String folderName) {
		folder = new File(folderName);
		if (!folder.exists()) {
			folder.mkdir();
		} else if (!folder.isDirectory()) {
			throw new IllegalArgumentException(folder + " must be a directory");
		}
	}
	
	private void writeAlignment() {
		ps.print("\n");
		for (int i = 0; i < alignment; i++) {
			ps.print("\t");
		}
	}
	
	private void printObjects(List<Variable> variables) {
		ps.print("(:objects");
		for (Variable var : variables) {
			ps.print(" ");
			var.accept(this);
			ps.print(" - ");
			ps.print(var.getType());
		}
		ps.print(")");
	}

	@Override
	public void visit(Afsm afsm) {
		Predicate[] init = afsm.initialAssumptions.toArray(new Predicate[afsm.initialAssumptions.size()]);		
		
		
		for (Rule r : afsm.rules) {
			
			if (r.getPrecontion() == null) {
				// No in precondition
				continue;
			}
			
			File stateFile = new File(folder.getAbsolutePath() + "/DeadRule-" + r.getName() + ".pddl");
			if (stateFile.exists()) {
				stateFile.delete();
			}
			try {
				stateFile.createNewFile();
				ps = new PrintStream(stateFile);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			ps.print("; Dead rule violation goal for rule ");
			ps.print(r.getName());
			writeAlignment();
			ps.print("; If no solution is found then the rule is unreachable ");
			writeAlignment();
			
			ps.print("(define (problem dead-rule-");
			ps.print(r.getName());
			ps.print(")");
			
			alignment ++;
			writeAlignment();
			
			// Domain
			ps.print("(:domain ");
			ps.print(afsm.getName());
			ps.print(")");
			writeAlignment();
			
			// Objects
			printObjects(afsm.initialObjects);
			writeAlignment();
			
			// Init
			ps.print("(:init ");
			alignment ++;
			for (Predicate p : init) {
				writeAlignment();
				p.accept(this);
			}
			alignment --;
			writeAlignment();
			ps.print(")");
			writeAlignment();
			
			// Goal
			ps.print("(:goal ");
			alignment ++;
			writeAlignment();
			r.getPrecontion().accept(this);
			alignment --;
			writeAlignment();
			ps.print(")");
			
			alignment --;
			writeAlignment();
			ps.print(")");
			visitEnd();
		}
	}

	@Override
	public void visit(Variable variable) {
		ps.print(variable.getName());	
	}

	@Override
	public void visit(Action action) {
		throw new IllegalStateException("This method should not be invoked.");
	}

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
	public void visit(VariablePredicate predicate) {
		ps.print("(");
		ps.print(predicate.getUniqueName());
		Map<String, Variable> variables = predicate.getVariables();
		for (String name : variables.keySet()) {
			ps.print(" ");
			variables.get(name).accept(this);
		}
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
