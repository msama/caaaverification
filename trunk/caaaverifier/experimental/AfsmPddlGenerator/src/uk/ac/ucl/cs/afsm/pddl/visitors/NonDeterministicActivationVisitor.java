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

import static uk.ac.ucl.cs.afsm.pddl.Predicates.*;

/**
 * @author rax
 *
 */
public class NonDeterministicActivationVisitor implements AfsmVisitor {

	private File folder;
	
	private PrintStream ps;
	
	private int alignment = 0;
	
	/**
	 * 
	 */
	public NonDeterministicActivationVisitor(String folderName) {
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
		
		
		for (int i = 0; i < afsm.rules.size(); i++) {
			
			Rule r0 = afsm.rules.get(i);
			if (r0.getPrecontion() == null ) {
				// No in precondition
				continue;
			}

			for (int j = i + 1; j < afsm.rules.size(); j++) {
				
				Rule r1 = afsm.rules.get(j);
			
				if (r1.getPrecontion() == null ) {
					// No in precondition
					continue;
				}
				
				// this is not needed but ok....
				if (!r0.getOriginalState().equals(r1.getOriginalState())) {
					return;
				}
			
				File stateFile = new File(
						folder.getAbsolutePath() + "/NonDeterministicActivation-[" +
						r0.getName() + "-"+ r1.getName() + "].pddl");
				if (stateFile.exists()) {
					stateFile.delete();
				}
				try {
					stateFile.createNewFile();
					ps = new PrintStream(stateFile);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				ps.print("; Non deterministic avtivation violation goal for state ");
				writeAlignment();
				ps.print("; If a solution is found then the two rules ");
				writeAlignment();
				ps.print("; ");
				ps.print(r0.getName());
				ps.print(" and ");
				ps.print(r1.getName());
				ps.print(" are not deterministic. ");
				writeAlignment();
				
				ps.print("(define (problem nondeterministic-activation-");
				ps.print(r0.getName());
				ps.print("-");
				ps.print(r1.getName());
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
				and(r0.getPrecontion(), r1.getPrecontion()).accept(this);
				alignment --;
				writeAlignment();
				ps.print(")");
				
				alignment --;
				writeAlignment();
				ps.print(")");
				visitEnd();
			}
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
