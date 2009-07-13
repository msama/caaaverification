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
	
	//private Map<String, Name> names = new HashMap<String, Name>();
	
	//private Map<String, Variable> variables = new HashMap<String, Variable>();
	
	private Map<String, Predicate> predicates = new HashMap<String, Predicate>();
	
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
		
		//Type t = null;
		//Variable var = null;
		//List<Variable> vars = new ArrayList<Variable>();
		//TypedList<Variable> typedList = null;
		
		// Add an action for each Rule
		for (Rule rule : afsm.rules) {
			TypedList<Variable> typedList = null;
			ActionFunctor functor = new ActionFunctor(Name.create("rule_" + rule.getName()));
			
			// Add vontext
			List<Variable> vars = new ArrayList<Variable>();
			vars.add(CONTEXT_VARIABLE);
			typedList = TypedList.create(vars, CONTEXT_TYPE, typedList);
			
			// Add state as a variable
			vars = new ArrayList<Variable>();
			vars.add(STATE_VARIABLE);
			typedList = TypedList.create(vars, STATE_TYPE, typedList);
			
			GD trigger = parsePredicate(rule.getTrigger());
			GD initialStates = parseInitialStates(rule);
			GD preconditions = GD.createAnd(initialStates, trigger); 
			
			// +1 because it also contains the future state
			Effect[] and = new Effect[rule.action.size() + 1];
			
			int i = 0;
			for (Assignment a : rule.action) {
				Effect eff;
				// predicate must be one satisfying the variable
				Predicate predicate = predicates.get(a.getVariable().getName());
				// the term is the name of the local variable
				Term term = Term.create(CONTEXT_VARIABLE);
				AtomicFormula<Term> formula = AtomicFormula.create(predicate, term);
				if (a instanceof Assignment.Satisfy) {
					eff = Effect.createFormula(formula);
				} else {
					eff = Effect.createNot(formula);
				}
				and[i++] = eff;
			}
			
			//Add the future state
			// predicate must be one satisfying the variable
			Predicate predicate = predicates.get(rule.getDestination().getName());
			// the term is the name of the local variable
			Term t = Term.create(STATE_VARIABLE);
			AtomicFormula<Term> formula = AtomicFormula.create(predicate, t);	
			and[i] = Effect.createFormula(formula);
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
			// Add vontext
			List<Variable> vars = new ArrayList<Variable>();
			vars.add(CONTEXT_VARIABLE);
			typedList = TypedList.create(vars, CONTEXT_TYPE, typedList);
			
			Term t = Term.create(CONTEXT_VARIABLE);
			AtomicFormula<Term> formula = AtomicFormula.create(predicates.get(v.getName()), t);
			
			GD preconditions_positive = GD.createFormula(formula);
			GD preconditions_negative = GD.createNot(GD.createFormula(formula));
			//TODO(rax): add constraints here
			
			Effect effect_positive = Effect.createFormula(formula);
			Effect effect_negative = Effect.createNot(formula);
			
			ActionDefBody body_satisfy = ActionDefBody.create(preconditions_negative, effect_positive);
			ActionDefBody body_unsatisfy = ActionDefBody.create(preconditions_positive, effect_negative);
			
			ActionDef actionDef = ActionDef.create(satisfy, typedList, body_satisfy);
			StructureDef struct = StructureDef.create(actionDef);
			defs.add(struct);
			
			actionDef = ActionDef.create(unsatisfy, typedList, body_unsatisfy);
			struct = StructureDef.create(actionDef);
			defs.add(struct);
		}
		
		return defs;
	}
	
	private GD parsePredicate(uk.ac.ucl.cs.afsm.common.predicate.Predicate predicate) {
		if (predicate == Constant.TRUE) {
			throw new IllegalStateException("True not defined: " + predicate);
		} else if (predicate == Constant.FALSE) {
			throw new IllegalStateException("False not defined: " + predicate);
		} if (predicate instanceof uk.ac.ucl.cs.afsm.common.predicate.Variable) {
			uk.ac.ucl.cs.afsm.common.predicate.Variable v = (uk.ac.ucl.cs.afsm.common.predicate.Variable)predicate;
			Term t = Term.create(CONTEXT_VARIABLE);
			AtomicFormula<Term> formula = AtomicFormula.create(predicates.get(v.getName()), t);
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
		Term t = Term.create(STATE_VARIABLE);
		List<GD> states = new ArrayList<GD>();
		for (State s : afsm.states) {
			if (s.outGoingRules.contains(r)) {
				AtomicFormula<Term> formula = AtomicFormula.create(predicates.get(s.getName()), t);
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
