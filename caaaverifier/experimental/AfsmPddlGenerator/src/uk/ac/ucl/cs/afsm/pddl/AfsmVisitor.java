/**
 * 
 */
package uk.ac.ucl.cs.afsm.pddl;

/**
 * @author rax
 *
 */
public interface AfsmVisitor {

	void visit(Afsm afsm);
	
	void visit(Variable variable);

	void visit(Action action);

	void visit(CompositePredicate predicate);

	void visit(VariablePredicate predicate);

	void visitEnd();
}
