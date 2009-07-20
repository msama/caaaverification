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
	
	private static final String STATE = "state";
	private static final String CONTEXT = "context";
	
	private static final Name STATE_NAME = Name.create(STATE);
	private static final Name CONTEXT_NAME = Name.create(CONTEXT);
	
	private static final Variable STATE_VARIABLE = Variable.create("s");
	private static final Variable CONTEXT_VARIABLE = Variable.create("c");
	
	private static final Type STATE_TYPE = Type.create(STATE_NAME);
	private static final Type CONTEXT_TYPE = Type.create(CONTEXT_NAME);

	private static final Term STATE_TERM = Term.create(STATE_VARIABLE);
	private static final Term CONTEXT_TERM = Term.create(CONTEXT_VARIABLE);
	
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
		keys.add(RequireKey.TYPING);
		keys.add(RequireKey.CONSTRAINTS);
		keys.add(RequireKey.PREFERENCES);
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
		names.add(STATE_NAME);
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
		TypedList<DomainVarDeclaration> vars = TypedList.create(names);
		// TODO(rax): add requirements
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
			Predicate predicate = Predicate.create("is-state-" + key);

			TypedList<Variable> typedList = TypedList.create(vars, STATE_TYPE, null);
			AtomicFormulaSkeleton skeleton = AtomicFormulaSkeleton.create(predicate, typedList);
			formulas.add(skeleton);
			predicates.put(key, predicate);
		}
		

		vars = new ArrayList<Variable>();
		vars.add(CONTEXT_VARIABLE);
		
		existPredicate = Predicate.create("exist");
		formulas.add(createContextAtomicFormulaSkeleton(vars, existPredicate));
		predicates.put("exist", existPredicate);
		
		for (String name : afsm.variables.keySet()) {
			Predicate predicate = Predicate.create("is-true-" + name);
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
			TypedList<Variable> typedList = createStateContextTypedList();
			
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
				uk.ac.ucl.cs.afsm.common.predicate.Predicate requiree = con.requiree;
				if (requiree.equals(v)) {
					satisfyPrec.add(parsePredicate(con.required));
				} else if (requiree instanceof Operator.Not) {
					requiree = ((Operator.Not)requiree).getPredicate();
					if (requiree.equals(v)) {
						unsatisfyPrec.add(parsePredicate(con.required));
					}
				}
			}
			
			Term t = Term.create(CONTEXT_VARIABLE);
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
			
			// TODO(rax): this is awful fix it
			for (Constrain con : afsm.constrains) {
				uk.ac.ucl.cs.afsm.common.predicate.Predicate required = con.required;
				if (required.equals(v)) {
					if (con.requiree instanceof uk.ac.ucl.cs.afsm.common.predicate.Variable) {
						uk.ac.ucl.cs.afsm.common.predicate.Variable var = (uk.ac.ucl.cs.afsm.common.predicate.Variable)con.requiree;
						satisfyEff.add(Effect.createFormula(AtomicFormula.create(predicates.get(var.getName()), t)));
					} else {
						uk.ac.ucl.cs.afsm.common.predicate.Variable var = (uk.ac.ucl.cs.afsm.common.predicate.Variable)((Operator.Not)con.requiree).getPredicate();
						satisfyEff.add(Effect.createFormula(AtomicFormula.create(predicates.get(var.getName()), t)));
					}
				} else if (required instanceof Operator.Not) {
					required = ((Operator.Not)required).getPredicate();
					if (required.equals(v)) {
						if (con.requiree instanceof uk.ac.ucl.cs.afsm.common.predicate.Variable) {
							uk.ac.ucl.cs.afsm.common.predicate.Variable var = (uk.ac.ucl.cs.afsm.common.predicate.Variable)con.requiree;
							unsatisfyEff.add(Effect.createFormula(AtomicFormula.create(predicates.get(var.getName()), t)));
						} else {
							uk.ac.ucl.cs.afsm.common.predicate.Variable var = (uk.ac.ucl.cs.afsm.common.predicate.Variable)((Operator.Not)con.requiree).getPredicate();
							unsatisfyEff.add(Effect.createNot(AtomicFormula.create(predicates.get(var.getName()), t)));
						}
					}
				}
			}
			satisfyEff.add(Effect.createFormula(AtomicFormula.create(predicates.get(v.getName()), t)));
			unsatisfyEff.add(Effect.createNot(AtomicFormula.create(predicates.get(v.getName()), t)));
			
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

	public static TypedList<Variable> createStateContextTypedList() {
		TypedList<Variable> typedList = null;
		
		// Add vontext
		typedList = createTypedList(CONTEXT_VARIABLE, CONTEXT_TYPE, typedList);
		
		// Add state as a variable
		typedList = createTypedList(STATE_VARIABLE, STATE_TYPE, typedList);
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
		if (predicate == Constant.TRUE) {
			throw new IllegalStateException("True not defined: " + predicate);
		} else if (predicate == Constant.FALSE) {
			throw new IllegalStateException("False not defined: " + predicate);
		} if (predicate instanceof uk.ac.ucl.cs.afsm.common.predicate.Variable) {
			uk.ac.ucl.cs.afsm.common.predicate.Variable v = (uk.ac.ucl.cs.afsm.common.predicate.Variable)predicate;
			AtomicFormula<Term> formula = AtomicFormula.create(predicates.get(v.getName()), CONTEXT_TERM);
			return GD.createFormula(formula);
		} else if (predicate instanceof Operator.Not) {
			Operator.Not not = (Operator.Not) predicate;
			return GD.createNot(parsePredicate(not.getPredicate()));
		} else if (predicate instanceof Operator.And) {
			Operator.And and = (Operator.And) predicate;
			GD[] andGd = new GD[and.size()];
			for (int i = 0; i < and.size(); i++) {
				andGd[i] = parsePredicate(and.get(i));
			}
			return GD.createAnd(andGd);
		} else if (predicate instanceof Operator.Or) {
			Operator.Or or = (Operator.Or) predicate;
			GD[] orGd = new GD[or.size()];
			for (int i = 0; i < or.size(); i++) {
				orGd[i] = parsePredicate(or.get(i));
			}
			return GD.createOr(orGd);
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

	public GD getRuleGD(Rule rule) {
		return ruleTriggersGD.get(rule);
	}
	
	public AtomicFormula<Term> getStateFormulaTerm(State s) {
		return AtomicFormula.create(predicates.get(s.getName()), STATE_TERM);
	}
	
	public AtomicFormula<Name> getStateFormulaName(State s) {
		return AtomicFormula.create(predicates.get(s.getName()), STATE_NAME);
	}
	
	public GD getStateGD(State s) {
		return GD.createFormula(getStateFormulaTerm(s));
	}
	
	public GD getInStateAssumptionGD(State s) {
		return parsePredicate(s.getInStateAssumption());
	}
	
	public AtomicFormula<Name> getContextFormulaName() {
		return AtomicFormula.create(existPredicate, CONTEXT_NAME);
	}
}
