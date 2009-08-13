/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public abstract class StructureDef extends Streamable {
	
	public static StructureDef create(final ActionDef actionDef) {
		if (actionDef == null) {
			throw new IllegalStateException(
					"Statement <DurativeActionDef> cannot be null.");
		}
		return new StructureDef() {
			@Override
			protected void printInternal() {
				writeInto(pw, actionDef);
			}
		};
	}
	
	public static StructureDef create(final DurativeActionDef actionDef) {
		if (definedKeys.contains(RequireKey.DURATIVE_ACTIONS)) {
			throw new IllegalStateException(":durative-actions not defined.");
		}
		
		if (actionDef == null) {
			throw new IllegalStateException(
					"Statement <DurativeActionDef> cannot be null.");
		}
		return new StructureDef() {
			@Override
			protected void printInternal() {
				writeInto(pw, actionDef);
			}
		};
	}
	
	public static StructureDef create(final DerivedDef derivedDef) {
		if (definedKeys.contains(RequireKey.DERIVED_PREDICATES)) {
			throw new IllegalStateException(":durative-actions not defined.");
		}
		
		if (derivedDef == null) {
			throw new IllegalStateException(
					"Statement <DurativeActionDef> cannot be null.");
		}
		return new StructureDef() {
			@Override
			protected void printInternal() {
				writeInto(pw, derivedDef);
			}
		};
	}
}
