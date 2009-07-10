/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

import java.util.ArrayList;
import java.util.List;

/**
 * Goal Description
 * 
 * @author rax
 *
 */
public class GD extends Streamable {
	
	private Expansion expansion;

	protected AtomicFormula<Term> formula;
	
	protected List<GD> and;
	
	protected Literal<Term> literal;
	
	// Requires RequireKey.DISJUNCTIVE_PRECONDITIONS
	protected List<GD> or;
	
	// Requires RequireKey.DISJUNCTIVE_PRECONDITIONS
	protected GD not;
	
	// Requires RequireKey.DISJUNCTIVE_PRECONDITIONS
	protected GD implyPrecondition;
	
	// Requires RequireKey.DISJUNCTIVE_PRECONDITIONS
	protected GD implyEffect;
	
	// Requires RequireKey.EXISTENTIAL_PRECONDITIONS
	protected List<TypedList<Variable>> existList;
	
	// Requires RequireKey.EXISTENTIAL_PRECONDITIONS
	protected GD existEffect;
	
	// Requires RequireKey.UNIVERSAL_PRECONDITIONS
	protected List<TypedList<Variable>> forallList;
	
	// Requires RequireKey.UNIVERSAL_PRECONDITIONS
	protected GD forallEffect;
	
	private GD() {
		
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		switch(expansion) {
			case FORMULA:
				writeInto(pw, formula);
				break;
			case AND:
				pw.print("(and ");
				writeAlignedList(and);
				pw.print(")");
				break;
			case LITERAL:
				writeInto(pw, literal);
				break;
			case OR:
				pw.print("(or ");
				writeAlignedList(or);
				pw.print(")");
				break;
			case NOT:
				pw.print("(not ");
				writeGD(not);
				pw.print(")");
				break;
			case IMPLY:
				pw.print("(imply ");
				++alignment;
				writeGD(implyPrecondition);
				writeGD(implyEffect);
				pw.print(")");
				break;
			case EXISTS:
				pw.print("(exist ");
				writeAlignedList(existList);
				writeInto(pw, existEffect);
				--alignment;
				align();
				pw.print(")");
				break;
			case FORALL:
				pw.print("(forall ");
				writeAlignedList(forallList);
				writeGD(forallEffect);
				pw.print(")");
				break;
		}
	}
	
	private void writeGD(GD gd) {
		++alignment;
		align();
		//pw.print("(");
		writeInto(pw, gd);
		//pw.print(")");
		--alignment;
		align();
	}
	
	private enum Expansion {
		FORMULA,
		AND,
		LITERAL,
		OR,
		NOT,
		IMPLY,
		EXISTS,
		FORALL;
	}
	
	public static GD createFormula(AtomicFormula<Term> formula) {
		GD gd = new GD();
		gd.formula = formula;
		gd.expansion = Expansion.FORMULA;
		return gd;
	}
	
	public static GD createAnd(GD... and) {
		GD gd = new GD();
		gd.and = new ArrayList<GD>();
		
		for (GD g : and) {
			gd.and.add(g);
		}
		
		gd.expansion = Expansion.AND;
		return gd;
	}
	
	public static GD createLiteral(Literal<Term> literal) {
		GD gd = new GD();
		gd.literal = literal;
		gd.expansion = Expansion.LITERAL;
		return gd;
	}
	
	public static GD createOr(GD... or) {
		if (!Streamable.definedKeys.contains(RequireKey.DISJUNCTIVE_PRECONDITIONS)) {
			throw new IllegalStateException("Statement not can only be used if " +
					"<required-key> defines :disjunctive-preconditions.");
		}
		
		GD gd = new GD();
		gd.or = new ArrayList<GD>();
		
		for (GD g : or) {
			gd.or.add(g);
		}
		
		gd.expansion = Expansion.OR;
		return gd;
	}
	
	public static GD createNot(GD not) {
		if (!Streamable.definedKeys.contains(RequireKey.DISJUNCTIVE_PRECONDITIONS)) {
			throw new IllegalStateException("Statement not can only be used if " +
					"<required-key> defines :disjunctive-preconditions.");
		}
		
		GD gd = new GD();
		gd.not = not;
		
		gd.expansion = Expansion.NOT;
		return gd;
	}
	
	public static GD createImply(GD precondition, GD effect) {
		if (!Streamable.definedKeys.contains(RequireKey.DISJUNCTIVE_PRECONDITIONS)) {
			throw new IllegalStateException("Statement imply can only be used if " +
					"<required-key> defines :disjunctive-preconditions.");
		}
		
		GD gd = new GD();
		gd.implyPrecondition = precondition;
		gd.implyEffect = effect;
		
		gd.expansion = Expansion.IMPLY;
		return gd;
	}
	
	public static GD createExist(GD effect, TypedList<Variable>... existList) {
		if (!Streamable.definedKeys.contains(RequireKey.EXISTENTIAL_PRECONDITIONS)) {
			throw new IllegalStateException("Statement exist can only be used if " +
					"<required-key> defines :existential-preconditions.");
		}
		
		GD gd = new GD();
		gd.existList = new ArrayList<TypedList<Variable>>();
		for (TypedList<Variable> l : existList) {
			gd.existList.add(l);
		}
		
		gd.existEffect = effect;
		
		gd.expansion = Expansion.EXISTS;
		return gd;
	}
	
	public static GD createForall(GD effect, TypedList<Variable>... forallList) {
		if (!Streamable.definedKeys.contains(RequireKey.UNIVERSAL_PRECONDITIONS)) {
			throw new IllegalStateException("Statement forall can only be used if " +
					"<required-key> defines :universal-preconditions.");
		}
		
		GD gd = new GD();
		gd.forallList = new ArrayList<TypedList<Variable>>();
		for (TypedList<Variable> l : forallList) {
			gd.forallList.add(l);
		}
		
		gd.forallEffect = effect;
		
		gd.expansion = Expansion.FORALL;
		return gd;
	}
}
