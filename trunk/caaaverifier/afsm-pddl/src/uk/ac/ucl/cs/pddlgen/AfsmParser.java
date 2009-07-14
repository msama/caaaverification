package uk.ac.ucl.cs.pddlgen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import phoneAdapter.PhoneAdapterAfsm;

import uk.ac.ucl.cs.afsm.common.AdaptationFiniteStateMachine;
import uk.ac.ucl.cs.afsm.common.Assignment;
import uk.ac.ucl.cs.afsm.common.Rule;
import uk.ac.ucl.cs.afsm.common.State;
import uk.ac.ucl.cs.afsm.common.predicate.Constant;
import uk.ac.ucl.cs.afsm.common.predicate.Constrain;
import uk.ac.ucl.cs.afsm.common.predicate.Operator;
import uk.ac.ucl.cs.pddlgen.ebnf.ActionDef;
import uk.ac.ucl.cs.pddlgen.ebnf.ActionDefBody;
import uk.ac.ucl.cs.pddlgen.ebnf.ActionFunctor;
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
import uk.ac.ucl.cs.pddlgen.ebnf.MethodDef;
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
	
	//private Map<String, Name> names = new HashMap<String, Name>();
	
	//private Map<String, Variable> variables = new HashMap<String, Variable>();
	
	private Map<String, Predicate> predicates = new HashMap<String, Predicate>();
	
	private Map<String, StructureDef> actionRules = new HashMap<String, StructureDef>();
	
	private Map<String, StructureDef> actionEvent = new HashMap<String, StructureDef>();
	
	/**
	 * @param afsm
	 */
	public AfsmParser(AdaptationFiniteStateMachine afsm) {
		this.afsm = afsm;
	}
	
	public static void main(String[] args) {
		AfsmParser parser = new AfsmParser(new PhoneAdapterAfsm().getAdaptationFiniteStateMachine());
		Domain domain = parser.parse();
		domain.startWriting(System.out);
	}
	
	public Domain parse() {
		Domain domain = Domain.create(
				Name.create(afsm.getName()),
				null, //createExtensionDef(),
				createRequireDef(), 
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
		
		for (String name : afsm.variables.keySet()) {
			
			String key = name;
			Predicate predicate = Predicate.create("is-true-" + key);
			TypedList<Variable> typedList = TypedList.create(vars, CONTEXT_TYPE, null);
			AtomicFormulaSkeleton skeleton = AtomicFormulaSkeleton.create(predicate, typedList);
			formulas.add(skeleton);
			predicates.put(key, predicate);
		}
		
		return PredicatesDef.create(formulas);
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
			TypedList<Variable> typedList = null;
			ActionFunctor functor = new ActionFunctor(Name.create("rule_" + rule.getName()));
			
			// Add vontext
			typedList = createTypedList(CONTEXT_VARIABLE, CONTEXT_TYPE, typedList);
			
			// Add state as a variable
			typedList = createTypedList(STATE_VARIABLE, STATE_TYPE, typedList);
			
			GD trigger = parsePredicate(rule.getTrigger());
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
			ActionDef actionDef = ActionDef.create(functor, typedList, body);
			StructureDef struct = StructureDef.create(actionDef);
			defs.add(struct);
		}
		
		// Add events to satisfy/unsatisfy each variables
		for (String key : afsm.variables.keySet()) {
			uk.ac.ucl.cs.afsm.common.predicate.Variable v = afsm.variables.get(key);
			
			ActionFunctor satisfy = new ActionFunctor(Name.create("satisfy_" + v.getName()));
			ActionFunctor unsatisfy = new ActionFunctor(Name.create("unsatisfy_" + v.getName()));
			
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
							unsatisfyEff.add(Effect.createFormula(AtomicFormula.create(predicates.get(var.getName()), t)));
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
			
			ActionDef actionDef = ActionDef.create(satisfy, typedList, body_satisfy);
			StructureDef struct = StructureDef.create(actionDef);
			defs.add(struct);
			
			actionDef = ActionDef.create(unsatisfy, typedList, body_unsatisfy);
			struct = StructureDef.create(actionDef);
			defs.add(struct);
		}
		
		return defs;
	}
	
	private TypedList<Variable> createTypedList(Variable v, Type t, TypedList<Variable>  tail) {
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
}
