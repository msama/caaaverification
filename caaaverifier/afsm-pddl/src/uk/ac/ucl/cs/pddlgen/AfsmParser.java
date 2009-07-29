package uk.ac.ucl.cs.pddlgen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import phoneAdapter.PhoneAdapterAfsm;

import uk.ac.ucl.cs.afsm.common.AdaptationFiniteStateMachine;
import uk.ac.ucl.cs.afsm.common.Assignment;
import uk.ac.ucl.cs.afsm.common.Rule;
import uk.ac.ucl.cs.afsm.common.State;
import uk.ac.ucl.cs.afsm.common.predicate.Constant;
import uk.ac.ucl.cs.afsm.common.predicate.Constrain;
import uk.ac.ucl.cs.afsm.common.predicate.Operator;
import uk.ac.ucl.cs.pddlgen.ebnf.ActionDefBody;
import uk.ac.ucl.cs.pddlgen.ebnf.AtomicFormula;
import uk.ac.ucl.cs.pddlgen.ebnf.AtomicFormulaSkeleton;
import uk.ac.ucl.cs.pddlgen.ebnf.ConstantsDef;
import uk.ac.ucl.cs.pddlgen.ebnf.Domain;
import uk.ac.ucl.cs.pddlgen.ebnf.DomainVarDeclaration;
import uk.ac.ucl.cs.pddlgen.ebnf.DomainVarsDef;
import uk.ac.ucl.cs.pddlgen.ebnf.Effect;
import uk.ac.ucl.cs.pddlgen.ebnf.ExtensionDef;
import uk.ac.ucl.cs.pddlgen.ebnf.GD;
import uk.ac.ucl.cs.pddlgen.ebnf.Literal;
import uk.ac.ucl.cs.pddlgen.ebnf.Name;
import uk.ac.ucl.cs.pddlgen.ebnf.Predicate;
import uk.ac.ucl.cs.pddlgen.ebnf.PredicatesDef;
import uk.ac.ucl.cs.pddlgen.ebnf.RequireDef;
import uk.ac.ucl.cs.pddlgen.ebnf.RequireKey;
import uk.ac.ucl.cs.pddlgen.ebnf.SafetyDef;
import uk.ac.ucl.cs.pddlgen.ebnf.Streamable;
import uk.ac.ucl.cs.pddlgen.ebnf.StructureDef;
import uk.ac.ucl.cs.pddlgen.ebnf.Term;
import uk.ac.ucl.cs.pddlgen.ebnf.TimelessDef;
import uk.ac.ucl.cs.pddlgen.ebnf.Type;
import uk.ac.ucl.cs.pddlgen.ebnf.TypedList;
import uk.ac.ucl.cs.pddlgen.ebnf.TypesDef;
import uk.ac.ucl.cs.pddlgen.ebnf.Variable;

public class AfsmParser {

	private AdaptationFiniteStateMachine afsm;
	
	public static final String STATE = "state";
	public static final String CONTEXT = "context";
	public static final String PRIORITY = "priority";
	
	public static final Name STATE_NAME = Name.create(STATE);
	public static final Name CONTEXT_NAME = Name.create(CONTEXT);
	public static final Name PRIORITY_NAME = Name.create(PRIORITY);
	
	public static final Name STATE_VARIABLE_NAME = Name.create("s");
	public static final Name CONTEXT_VARIABLE_NAME = Name.create("c");
	public static final Name PRIORITY_VARIABLE_NAME = Name.create("p");
	
	public static final Variable STATE_VARIABLE = Variable.create(STATE_VARIABLE_NAME);
	public static final Variable CONTEXT_VARIABLE = Variable.create(CONTEXT_VARIABLE_NAME);
	public static final Variable PRIORITY_VARIABLE = Variable.create(PRIORITY_VARIABLE_NAME);
	
	public static final Type STATE_TYPE = Type.create(STATE_NAME);
	public static final Type CONTEXT_TYPE = Type.create(CONTEXT_NAME);
	public static final Type PRIORITY_TYPE = Type.create(PRIORITY_NAME);

	public static final Term STATE_TERM = Term.create(STATE_VARIABLE);
	public static final Term CONTEXT_TERM = Term.create(CONTEXT_VARIABLE);
	public static final Term PRIORITY_TERM = Term.create(PRIORITY_VARIABLE);
	
	private Name domainName;
	
	private RequireDef requireDef;
	
	private Map<String, Predicate> predicates = new HashMap<String, Predicate>();
	
	private Map<Rule, GD> ruleTriggersGD = new HashMap<Rule, GD>();

	private Predicate existPredicate;
	
	private Domain domain;
	
	/**
	 * @param afsm
	 */
	public AfsmParser(AdaptationFiniteStateMachine afsm) {
		this.afsm = afsm;
		domainName = Name.create(afsm.getName());
		requireDef = createRequireDef();
		domain = parse();
	}
	
	public static void main(String[] args) {
		AdaptationFiniteStateMachine afsm = new PhoneAdapterAfsm().getAdaptationFiniteStateMachine();
		AfsmParser parser = new AfsmParser(afsm);
		parser.save("out");
		
		NondeterministicProblemGenerator nonDetGen = new NondeterministicProblemGenerator(afsm, parser);
		nonDetGen.saveAll("out/nondet");
		
		RuleLivenessProblemDefinition ruleLiveness = new RuleLivenessProblemDefinition(afsm, parser);
		ruleLiveness.saveAll("out/ruleLiv");
		
		ReachabilityProblemDefinition reachability = new ReachabilityProblemDefinition(afsm, parser);
		reachability.saveAll("out/reach");
		
		InStateViolationProblemDefinition inState = new InStateViolationProblemDefinition(afsm, parser);
		inState.saveAll("out/instate");
	}
	
	public void save(String foldername) {
		try {
			File folder = new File(foldername);
			folder.mkdirs();
			folder = null;
			PrintStream ps = new PrintStream(new File(foldername + "/" + domainName + ".pddl"));
			print(ps);
			ps.flush();
			ps.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void print(PrintStream ps) {
		domain.startWriting(ps);
	}
	
	public Domain parse() {
		Domain domain = Domain.create(
				domainName,
				null, //createExtensionDef(),
				requireDef,//createRequireDef(), 
				createTypesDef(), 
				null, //createConstantsDef(), 
				null, //createDomainVarsDef(), 
				createPredicatesDef(), 
				null, //createTimelessDef(), 
				null, //safetyDef, 
				createStructureDefs());
		return domain;
	}
	
	private ExtensionDef createExtensionDef() {
		List<Name> names = new ArrayList<Name>();
		// TODO(rax): add extensions
		return ExtensionDef.create(names);
	}
	
	/**
	 * Creates a list of the common requirements
	 * 
	 * @return
	 */
	private RequireDef createRequireDef() {
		List<RequireKey> keys = new ArrayList<RequireKey>();
		keys.add(RequireKey.STRIPS);
		keys.add(RequireKey.TYPING);
		keys.add(RequireKey.EQUALITY);
		keys.add(RequireKey.DISJUNCTIVE_PRECONDITIONS);
		Streamable.definedKeys.addAll(keys);
		return RequireDef.create(keys);
	}
	
	/**
	 * Creates variables to model this domain
	 * <ul>
	 * <li> a type ''state''
	 * <li> 1 type for each context type
	 * 
	 * @return
	 */
	private TypesDef createTypesDef() {
		List<Name> names = new ArrayList<Name>();
		names.add(CONTEXT_NAME);
		
		TypedList<Name> types = TypedList.create(names);
		return TypesDef.create(types);
	}
	
	private ConstantsDef createConstantsDef() {
		List<Name> names = new ArrayList<Name>();
		TypedList<Name> types = TypedList.create(names);
		// TODO(rax): add requirements
		return ConstantsDef.create(types);
	}
	
	private DomainVarsDef createDomainVarsDef() {
		List<DomainVarDeclaration> names = new ArrayList<DomainVarDeclaration>();
		names.add(DomainVarDeclaration.create(STATE_NAME));
		names.add(DomainVarDeclaration.create(PRIORITY_NAME));
		
		TypedList<DomainVarDeclaration> vars = TypedList.create(names);
		return DomainVarsDef.create(vars);
	}
	
	/**
	 * There are:
	 * <ul>
	 * <li> 1 predicate for state
	 * <li> 1 predicate for variable
	 * 
	 * @return
	 */
	private PredicatesDef createPredicatesDef() {
		List<AtomicFormulaSkeleton> formulas = new ArrayList<AtomicFormulaSkeleton>();
		
		//Type t = null;
		//Variable var = null;
		List<Variable> vars = new ArrayList<Variable>();

		//t= Type.create(this.names.get("state"));
		//var = Variable.create("s");
		vars.add(STATE_VARIABLE);
		
		for (State state : afsm.states) {
			String key = state.getName();
			Predicate predicate = Predicate.create("is_state_" + key);

			TypedList<Variable> typedList = TypedList.create(vars, STATE_TYPE, null);
			AtomicFormulaSkeleton skeleton = AtomicFormulaSkeleton.create(predicate, typedList);
			formulas.add(skeleton);
			predicates.put(key, predicate);
		}
		

		vars = new ArrayList<Variable>();
		vars.add(CONTEXT_VARIABLE);
		
		//existPredicate = Predicate.create("exist");
		//formulas.add(createContextAtomicFormulaSkeleton(vars, existPredicate));
		//predicates.put("exist", existPredicate);
		
		for (String name : afsm.variables.keySet()) {
			Predicate predicate = Predicate.create("is_true_" + name);
			AtomicFormulaSkeleton skeleton = createContextAtomicFormulaSkeleton(vars,
					predicate);
			formulas.add(skeleton);
			predicates.put(name, predicate);
		}
		
		return PredicatesDef.create(formulas);
	}

	private AtomicFormulaSkeleton createContextAtomicFormulaSkeleton(
			List<Variable> vars, Predicate predicate) {
		TypedList<Variable> typedList = TypedList.create(vars, CONTEXT_TYPE, null);
		AtomicFormulaSkeleton skeleton = AtomicFormulaSkeleton.create(predicate, typedList);
		return skeleton;
	}
	
	private TimelessDef createTimelessDef() {
		List<Literal<Name>> defs = new ArrayList<Literal<Name>>();
		return TimelessDef.create(defs);
	}
	
	private SafetyDef createSafetyDef() {
		//TODO
		return SafetyDef.create(null);
	}
	
	/**
	 * Creates:
	 * <ul>
	 * <li> 1 structure per adaptation rule
	 * <li> 2 for each variable which can be changed by an event  
	 * 
	 * @return
	 */
	private List<StructureDef> createStructureDefs() {
		
		List<StructureDef> defs = new ArrayList<StructureDef>();
		
		// Add an action for each Rule
		for (Rule rule : afsm.rules) {
			TypedList<Variable> typedList = createPriorityStateContextTypedList();
			
			GD trigger = parsePredicate(rule.getTrigger());
			ruleTriggersGD.put(rule, trigger);
			GD initialStates = parseInitialStates(rule);
			GD preconditions = GD.createAnd(initialStates, trigger); 
			
			Effect[] and = new Effect[rule.action.size() + afsm.states.size()];
			
			int i = 0;
			for (Assignment a : rule.action) {
				and[i++] = createEffectForAssignment(a);
			}
			
			//Add the future state
			for (State s : afsm.states) {
				and[i++] = createEffectForState(s, !(s.equals(rule.getDestination())));
			}
			Effect effect = Effect.createAnd(and);
			
			ActionDefBody body = ActionDefBody.create(preconditions, effect);
			StructureDef struct = StructureDef.create(Name.create("rule_" + rule.getName()), typedList, body);
			defs.add(struct);
		}
		
		// Add events to satisfy/unsatisfy each variables
		for (String key : afsm.variables.keySet()) {
			uk.ac.ucl.cs.afsm.common.predicate.Variable v = afsm.variables.get(key);
			
			TypedList<Variable> typedList = null;
			// Add context
			typedList = createTypedList(CONTEXT_VARIABLE, CONTEXT_TYPE, typedList);
			
			List<GD> satisfyPrec = new ArrayList<GD>();
			List<GD> unsatisfyPrec = new ArrayList<GD>();
			
			for (Constrain con : afsm.constrains) {
				if (v.equals(con.requiree)) {
					parseConstrainForPrecondition(con, satisfyPrec, unsatisfyPrec);
				}
			}
			
			GD satisfyPreconditions = null;
			GD unsatisfyPreconditions = null;
			if (satisfyPrec.size() > 0) {
				satisfyPreconditions = GD.createOr(satisfyPrec.toArray(new GD[satisfyPrec.size()]));
				satisfyPreconditions = GD.createAnd(satisfyPreconditions, GD.createNot(parsePredicate(v)));
			} else {
				satisfyPreconditions = GD.createNot(parsePredicate(v));
			}
			
			if (unsatisfyPrec.size() > 0) {
				unsatisfyPreconditions = GD.createOr(unsatisfyPrec.toArray(new GD[unsatisfyPrec.size()]));
				unsatisfyPreconditions = GD.createAnd(unsatisfyPreconditions, parsePredicate(v));
			} else {
				unsatisfyPreconditions =  parsePredicate(v);
			}
			
			// Add AND with the variable negated
			List<Effect> satisfyEff = new ArrayList<Effect>();
			List<Effect> unsatisfyEff = new ArrayList<Effect>();
			
			// Add constraints in the effect
			for (Constrain con : afsm.constrains) {
				if (v.equals(con.required)) {
					parseConstrainForEffect(con, satisfyEff, unsatisfyEff);
				}
			}
			
			satisfyEff.add(Effect.createFormula(AtomicFormula.create(predicates.get(v.getName()), CONTEXT_TERM)));
			unsatisfyEff.add(Effect.createNot(AtomicFormula.create(predicates.get(v.getName()), CONTEXT_TERM)));
			
			Effect satisfyEffect = Effect.createAnd(satisfyEff.toArray(new Effect[satisfyEff.size()]));
			Effect unsatisfyEffect = Effect.createAnd(unsatisfyEff.toArray(new Effect[unsatisfyEff.size()]));
			
			ActionDefBody body_satisfy = ActionDefBody.create(satisfyPreconditions, satisfyEffect);
			ActionDefBody body_unsatisfy = ActionDefBody.create(unsatisfyPreconditions, unsatisfyEffect);
			
			StructureDef struct = StructureDef.create(Name.create("satisfy_" + v.getName()), typedList, body_satisfy);
			defs.add(struct);
			
			struct = StructureDef.create(Name.create("unsatisfy_" + v.getName()), typedList, body_unsatisfy);
			defs.add(struct);
		}
		
		return defs;
	}

	private void parseConstrainForEffect(Constrain con,
			List<Effect> satisfyEff, List<Effect> unsatisfyEff) {
		if (con instanceof Constrain.AThenB) {
			parseConstrainForEffect((Constrain.AThenB)con, satisfyEff, unsatisfyEff);
		} else if (con instanceof Constrain.AThenNotB) {
			parseConstrainForEffect((Constrain.AThenNotB)con, satisfyEff, unsatisfyEff);
		}  else if (con instanceof Constrain.NotAThenB) {
			parseConstrainForEffect((Constrain.NotAThenB)con, satisfyEff, unsatisfyEff);
		} else if (con instanceof Constrain.NotAThenNotB) {
			parseConstrainForEffect((Constrain.NotAThenNotB)con, satisfyEff, unsatisfyEff);
		}
	}
	
	private void parseConstrainForEffect(Constrain.AThenB con,
			List<Effect> satisfyEff, List<Effect> unsatisfyEff) {
		satisfyEff.add(Effect.createFormula(AtomicFormula.create(predicates.get(con.requiree.getName()), CONTEXT_TERM)));
	}
	
	private void parseConstrainForEffect(Constrain.AThenNotB con,
			List<Effect> satisfyEff, List<Effect> unsatisfyEff) {
		satisfyEff.add(Effect.createNot(AtomicFormula.create(predicates.get(con.requiree.getName()), CONTEXT_TERM)));
	}
	
	private void parseConstrainForEffect(Constrain.NotAThenB con,
			List<Effect> satisfyEff, List<Effect> unsatisfyEff) {
		unsatisfyEff.add(Effect.createFormula(AtomicFormula.create(predicates.get(con.requiree.getName()), CONTEXT_TERM)));
	}
	
	private void parseConstrainForEffect(Constrain.NotAThenNotB con,
			List<Effect> satisfyEff, List<Effect> unsatisfyEff) {
		unsatisfyEff.add(Effect.createNot(AtomicFormula.create(predicates.get(con.requiree.getName()), CONTEXT_TERM)));
	}
	
	private void parseConstrainForPrecondition(Constrain con,
			List<GD> satisfyPrec, List<GD> unsatisfyPrec) {
		if (con instanceof Constrain.AThenB) {
			parseConstrainForPrecondition((Constrain.AThenB)con, satisfyPrec, unsatisfyPrec);
		} else if (con instanceof Constrain.AThenNotB) {
			parseConstrainForPrecondition((Constrain.AThenNotB)con, satisfyPrec, unsatisfyPrec);
		}  else if (con instanceof Constrain.NotAThenB) {
			parseConstrainForPrecondition((Constrain.NotAThenB)con, satisfyPrec, unsatisfyPrec);
		} else if (con instanceof Constrain.NotAThenNotB) {
			parseConstrainForPrecondition((Constrain.NotAThenNotB)con, satisfyPrec, unsatisfyPrec);
		}
	}

	private void parseConstrainForPrecondition(Constrain.AThenB con,
			List<GD> satisfyPrec, List<GD> unsatisfyPrec) {
		satisfyPrec.add(parsePredicate(con.required));
	}
	
	private void parseConstrainForPrecondition(Constrain.AThenNotB con,
			List<GD> satisfyPrec, List<GD> unsatisfyPrec) {
		satisfyPrec.add(GD.createNot(parsePredicate(con.required)));
	}

	private void parseConstrainForPrecondition(Constrain.NotAThenB con,
			List<GD> satisfyPrec, List<GD> unsatisfyPrec) {
		unsatisfyPrec.add(parsePredicate(con.required));
	}
	
	private void parseConstrainForPrecondition(Constrain.NotAThenNotB con,
			List<GD> satisfyPrec, List<GD> unsatisfyPrec) {
		unsatisfyPrec.add(GD.createNot(parsePredicate(con.required)));
	}
	
	public static TypedList<Variable> createPriorityStateContextTypedList() {
		TypedList<Variable> typedList = null;
		
		// Add vontext
		typedList = createTypedList(CONTEXT_VARIABLE, CONTEXT_TYPE, typedList);
		
		// Add state as a variable
		typedList = createTypedList(STATE_VARIABLE, STATE_TYPE, typedList);
		
		// Add priority
		typedList = createTypedList(PRIORITY_VARIABLE, PRIORITY_TYPE, typedList);
		
		return typedList;
	}
	
	private static TypedList<Variable> createTypedList(Variable v, Type t, TypedList<Variable>  tail) {
		List<Variable> vars = new ArrayList<Variable>();
		vars.add(v);
		return TypedList.create(vars, t, tail);
	}

	private Effect createEffectForAssignment(Assignment s) {
		Predicate predicate = predicates.get(s.getVariable().getName());
		AtomicFormula<Term> formula = AtomicFormula.create(predicate, CONTEXT_TERM);	
		return (s instanceof Assignment.Negate) ? Effect.createNot(formula) : Effect.createFormula(formula);
	}
	
	private Effect createEffectForState(State s, boolean negate) {
		Predicate predicate = predicates.get(s.getName());
		AtomicFormula<Term> formula = AtomicFormula.create(predicate, STATE_TERM);	
		return negate ? Effect.createNot(formula) : Effect.createFormula(formula);
	}
	
	private GD parsePredicate(uk.ac.ucl.cs.afsm.common.predicate.Predicate predicate) {
		return parsePredicate(predicate, CONTEXT_TERM, false);
	}
	
	/**
	 * Goal definitions do not support disjunctions. ORs are replaced with NOT(AND(NOT(a), NOT(b), ...))
	 * 
	 * @param predicate
	 * @param term
	 * @param deMorgan
	 * @return
	 */
	private GD parsePredicate(uk.ac.ucl.cs.afsm.common.predicate.Predicate predicate, Term term, boolean deMorgan) {
		if (predicate == Constant.TRUE) {
			throw new IllegalStateException("True not defined: " + predicate);
		} else if (predicate == Constant.FALSE) {
			throw new IllegalStateException("False not defined: " + predicate);
		} if (predicate instanceof uk.ac.ucl.cs.afsm.common.predicate.Variable) {
			uk.ac.ucl.cs.afsm.common.predicate.Variable v = (uk.ac.ucl.cs.afsm.common.predicate.Variable)predicate;
			AtomicFormula<Term> formula = AtomicFormula.create(predicates.get(v.getName()), term);
			return GD.createFormula(formula);
		} else if (predicate instanceof Operator.Not) {
			Operator.Not not = (Operator.Not) predicate;
			return GD.createNot(parsePredicate(not.getPredicate(), term, deMorgan));
		} else if (predicate instanceof Operator.And) {
			Operator.And and = (Operator.And) predicate;
			GD[] andGd = new GD[and.size()];
			for (int i = 0; i < and.size(); i++) {
				andGd[i] = parsePredicate(and.get(i), term, deMorgan);
			}
			return GD.createAnd(andGd);
		} else if (predicate instanceof Operator.Or) {
			Operator.Or or = (Operator.Or) predicate;
			GD[] orGd = new GD[or.size()];
			if (!deMorgan) {
				
				for (int i = 0; i < or.size(); i++) {
					orGd[i] = parsePredicate(or.get(i), term, deMorgan);
				}
				return GD.createOr(orGd);
			} else {
				for (int i = 0; i < or.size(); i++) {
					orGd[i] = GD.createNot(parsePredicate(or.get(i), term, deMorgan));
				}
				return GD.createNot(GD.createAnd(orGd));
			}
		}
		throw new IllegalStateException(
				"This method should have already returned a value: " + predicate);
	}
	
	private GD parseInitialStates(Rule r) {
		List<GD> states = new ArrayList<GD>();
		for (State s : afsm.states) {
			if (s.outGoingRules.contains(r)) {
				AtomicFormula<Term> formula = AtomicFormula.create(predicates.get(s.getName()), STATE_TERM);
				
				states.add(GD.createFormula(formula));
			}
		}
		// TODO(rax): if size is 0 the rule is dead...
		
		if (states.size() == 1) {
			return states.get(0);
		}
		
		GD[] orGD = new GD[states.size()];
		orGD = states.toArray(orGD);
		return GD.createOr(orGD);
	}

	public Name getDomainName() {
		return domainName;
	}

	public RequireDef getRequireDef() {
		return requireDef;
	}

	public GD createRuleGDForProblem(Rule rule) {
		//return ruleTriggersGD.get(rule);
		return parsePredicate(rule.getTrigger(), Term.create(CONTEXT_VARIABLE_NAME), true);
	}
	
	public AtomicFormula<Name> createStateFormulaForProblem(State s) {
		return AtomicFormula.create(predicates.get(s.getName()), STATE_VARIABLE_NAME);
	}
	
	public GD createStateGDForProblem(State s) {
		AtomicFormula<Term> formula = AtomicFormula.create(predicates.get(s.getName()), Term.create(STATE_VARIABLE_NAME));
		return GD.createFormula(formula);
	}
	
	public GD createInStateAssumptionGDForProblem(State s) {
		return parsePredicate(s.getInStateAssumption(), Term.create(STATE_VARIABLE_NAME), true);
	}
	
	public List<Literal<Name>> createInitiContextLiteralsForProblem() {
		List<Literal<Name>> list = new ArrayList<Literal<Name>>();
		list.add(Literal.createFormula(AtomicFormula.create(existPredicate, CONTEXT_VARIABLE_NAME)));
		for (String key : afsm.variables.keySet()) {
			Predicate p = predicates.get(key);
			AtomicFormula<Name> f = AtomicFormula.create(p, CONTEXT_VARIABLE_NAME);
			list.add(Literal.createNot(f));
		}
		return list;
	}
}
