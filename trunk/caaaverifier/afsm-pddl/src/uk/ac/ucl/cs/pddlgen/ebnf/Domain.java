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
	
	protected ExtensionDef extensionDef;
	
	protected RequireDef requireDef;
	
	// Requires RequireKey.TYPING
	protected TypesDef typesDef;
	
	protected ConstantsDef constantsDef;

	// Requires RequireKey.EXPRESSION_EVALUATION
	protected DomainVarsDef domainsVarDef;
	
	protected PredicatesDef predicatesDef;
	
	protected TimelessDef timelessDef;
	
	// Requires RequireKey.SAFETY_CONSTRAINTS
	protected SafetyDef safetyDef;
	
	// 0..n
	protected List<StructureDef> structureDefs = new ArrayList<StructureDef>();
	
	private Domain() {
		
	}
	
	@Override
	public void printInternal() {
		pw.print("(define (domain ");
		writeInto(name);
		pw.print(")");
		++ alignment;
		align();
		
		writeIntoIfDefined(extensionDef);
		
		writeIntoIfDefined(requireDef);
		
		if (requireDef.requireKeys.contains(RequireKey.TYPING)) {
			writeIntoIfDefined(typesDef);
		}
		
		writeIntoIfDefined(constantsDef);
		
		if (requireDef.requireKeys.contains(RequireKey.EXPRESSION_EVALUATION)) {
			writeIntoIfDefined(domainsVarDef);
		}
		
		writeIntoIfDefined(predicatesDef);
		
		writeIntoIfDefined(timelessDef);
		
		if (requireDef.requireKeys.contains(RequireKey.SAFETY_CONSTRAINTS)) {
			writeIntoIfDefined(safetyDef);
		}
		
		writeIntoIfDefined(structureDefs);
		
		-- alignment;
		align();
		pw.print(")");
	}

	public static Domain create(Name name, ExtensionDef extensionDef, RequireDef requireDef, TypesDef typesDef,
			ConstantsDef constantsDef, DomainVarsDef domainsVarDef, PredicatesDef predicatesDef,
			TimelessDef timelessDef, SafetyDef safetyDef, List<StructureDef> structureDefs) {
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
		
		if (domainsVarDef != null && !requireDef.requireKeys.contains(RequireKey.EXPRESSION_EVALUATION)) {
			throw new IllegalStateException(
					"Statement <domain-var-def> requires <require-key> :expression-evaluation.");
		}
		
		if (safetyDef != null && !requireDef.requireKeys.contains(RequireKey.SAFETY_CONSTRAINTS)) {
			throw new IllegalStateException(
					"Statement <safety-def> requires <require-key> :safety-constraints.");
		}
		
		Domain domain = new Domain();
		domain.name = name;
		domain.extensionDef = extensionDef;
		domain.requireDef = requireDef;
		domain.typesDef = typesDef;
		domain.constantsDef = constantsDef;
		domain.domainsVarDef = domainsVarDef;
		domain.predicatesDef = predicatesDef;
		domain.timelessDef = timelessDef;
		domain.safetyDef = safetyDef;
		domain.structureDefs.addAll(structureDefs);
		return domain;
	}
}
