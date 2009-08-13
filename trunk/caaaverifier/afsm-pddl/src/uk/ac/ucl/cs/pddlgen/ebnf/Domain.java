/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rax
 *
 */
public class Domain extends Streamable {

	// Required
	protected Name name;
	
	protected RequireDef requireDef;
	
	// Requires RequireKey.TYPING
	protected TypesDef typesDef;
	
	protected ConstantsDef constantsDef;
	
	protected PredicatesDef predicatesDef;
	
	// Requires RequireKey.FLUENTS
	protected FunctionsDef functionsDef;
	
	protected Constraints constraints;
	
	// 0..n
	protected List<StructureDef> structureDefs = new ArrayList<StructureDef>();
	
	private Domain() {
		
	}
	
	@Override
	public void printInternal() {
		pw.print("(define (domain ");
		writeInto(pw, name);
		pw.print(")");
		++ alignment;
		align();
		
		writeIntoIfDefined(requireDef, pw);
		align();
		
		if (requireDef.requireKeys.contains(RequireKey.TYPING)) {
			writeIntoIfDefined(typesDef, pw);
			align();
		}
		
		writeIntoIfDefined(constantsDef, pw);
		
		
		writeIntoIfDefined(predicatesDef, pw);
		align();
		
		writeIntoIfDefined(structureDefs, pw);
		align();
		
		-- alignment;
		align();
		pw.print(")");
	}

	public static Domain create(Name name, RequireDef requireDef, TypesDef typesDef,
			ConstantsDef constantsDef, PredicatesDef predicatesDef,
			FunctionsDef functionsDef, Constraints constraints, List<StructureDef> structureDefs) {
		if (name == null) {
			throw new IllegalStateException("Statement <domain> must have a <name>.");
		}
		
		if (structureDefs.size() < 1) {
			throw new IllegalStateException(
					"Statement <domain> must contain at least one <structure-def>.");
		}
		
		if (typesDef != null && !requireDef.requireKeys.contains(RequireKey.TYPING)) {
			throw new IllegalStateException(
					"Statement <types-def> requires <require-key> :types.");
		}
		
		
		if (functionsDef != null && !requireDef.requireKeys.contains(RequireKey.FLUENTS)) {
			throw new IllegalStateException(
					"Statement <functions-def> requires <require-key> :fluents.");
		}
		
		Domain domain = new Domain();
		domain.name = name;
		domain.requireDef = requireDef;
		domain.typesDef = typesDef;
		domain.constantsDef = constantsDef;
		domain.predicatesDef = predicatesDef;
		domain.functionsDef = functionsDef;
		domain.constraints = constraints;
		domain.structureDefs.addAll(structureDefs);
		return domain;
	}
}
