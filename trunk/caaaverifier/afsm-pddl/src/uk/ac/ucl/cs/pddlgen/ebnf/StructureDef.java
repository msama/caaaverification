/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class StructureDef extends Streamable {

	ActionDef actionDef;
	
	// Requires RequireKey.DOMAIN_AXIOMS
	AxiomDef axiomDef;
	
	// Requires RequireKey.ACTION_EXPANSIONS
	MethodDef methodDef;

	private StructureDef() {
		
	}

	@Override
	protected void printInternal() {
		if (actionDef != null) {
			writeInto(pw, actionDef);
		} else if (axiomDef != null) {
			writeInto(pw, axiomDef);
		} else if (methodDef != null) {
			writeInto(pw, methodDef);
		}
		align();
	} 

	public static StructureDef create(ActionDef actionDef) {
		if (actionDef == null) {
			throw new IllegalStateException(
					"Statement <axiom-def> must contain only one statement between an " +
					"<action-def> or an <axiom-def> or a <method-def>.");
		}
		StructureDef structure = new StructureDef();
		structure.actionDef = actionDef;
		return structure;
	}
	
	public static StructureDef create(AxiomDef axiomDef) {
		if (axiomDef == null) {
			throw new IllegalStateException(
					"Statement <axiom-def> must contain only one statement between an " +
					"<action-def> or an <axiom-def> or a <method-def>.");
		}
		if (!definedKeys.contains(RequireKey.DOMAIN_AXIOMS)) {
			throw new IllegalStateException(
					"Statement <axiom-def> requires <require-key> :domain-axioms.");
		}
		StructureDef structure = new StructureDef();
		structure.axiomDef = axiomDef;
		return structure;
	}
	
	public static StructureDef create(MethodDef methodDef) {
		if (methodDef == null) {
			throw new IllegalStateException(
					"Statement <axiom-def> must contain only one statement between an " +
					"<action-def> or an <axiom-def> or a <method-def>.");
		}
		if (!definedKeys.contains(RequireKey.ACTION_EXPANSIONS)) {
			throw new IllegalStateException(
					"Statement <method-def> requires <require-key> :action-expansions.");
		}
		StructureDef structure = new StructureDef();
		structure.methodDef = methodDef;
		return structure;
	}
}
