/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class ExtraDef extends Streamable {

	private Streamable streamable;
	
	/**
	 * @param streamable
	 */
	private ExtraDef(Streamable streamable) {
		this.streamable = streamable;
	}

	@Override
	protected void printInternal() {
		writeInto(pw, streamable);
	}
	
	public static ExtraDef create(final ActionDef action) {
		if (action == null) {
			throw new IllegalArgumentException("Statement <extra-def> " +
					"must contain a <action-def>.");
		}
		return new ExtraDef(action);
	}
	
	public static ExtraDef create(final AxiomDef axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Statement <extra-def> " +
					"must contain a <axiom-def>.");
		}
		if (!definedKeys.contains(RequireKey.DOMAIN_AXIOMS)) {
			throw new IllegalStateException("<axiom-def> requires domain-axioms.");
		}
		return new ExtraDef(axiom);
	}

	public static ExtraDef create(final MethodDef method) {
		if (method == null) {
			throw new IllegalArgumentException("Statement <extra-def> " +
					"must contain a <method-def>.");
		}
		if (!definedKeys.contains(RequireKey.ACTION_EXPANSIONS)) {
			throw new IllegalStateException("<method-def> requires action-expansions.");
		}
		return new ExtraDef(method);
	}
	
	public static ExtraDef create(final SafetyDef safety) {
		if (safety == null) {
			throw new IllegalArgumentException("Statement <extra-def> " +
					"must contain a <safety-def>.");
		}
		if (!definedKeys.contains(RequireKey.SAFETY_CONSTRAINTS)) {
			throw new IllegalStateException("<method-def> requires safety-constraints.");
		}
		return new ExtraDef(safety);
	}
}
