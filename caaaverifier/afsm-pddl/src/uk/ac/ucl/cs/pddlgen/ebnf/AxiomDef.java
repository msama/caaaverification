/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class AxiomDef extends Streamable {

	private GD gd;
	private TypedList<Variable> vars;
	private GD context;
	private Literal<Term> implies;
	
	/**
	 * @param gd
	 * @param vars
	 * @param context
	 * @param implies
	 */
	private AxiomDef(GD gd, TypedList<Variable> vars, GD context,
			Literal<Term> implies) {
		this.gd = gd;
		this.vars = vars;
		this.context = context;
		this.implies = implies;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		pw.print("(:axiom ");
		writeInto(gd);
		++alignment;
		align();
		pw.print(":vars (");
		writeInto(vars);
		pw.print(")");
		align();
		pw.print(":context ");
		writeInto(context);
		align();
		pw.print(":implies ");
		writeInto(implies);
		pw.print(")");
		--alignment;
		align();
	}

	public static AxiomDef create(final GD gd, final TypedList<Variable> vars,
			final GD context, final Literal<Term> implies) {
		if (gd == null) {
			throw new IllegalArgumentException("Statement <axiom-def> " +
					"must have a <GD>");
		}
		if (vars == null) {
			throw new IllegalArgumentException("Statement <axiom-def> " +
					"must have a <typed list (variable)>");
		}
		if (context == null) {
			throw new IllegalArgumentException("Statement <axiom-def> " +
					"must have a context <GD>");
		}
		if (implies == null) {
			throw new IllegalArgumentException("Statement <axiom-def> " +
					"must have a <literal(term)>");
		}
		
		return new AxiomDef(gd, vars, context, implies);
	}
}
