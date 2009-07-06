package uk.ac.ucl.cs.pddlgen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import phoneAdapter.PhoneAdapterAfsm;

import com.sun.org.apache.bcel.internal.generic.ATHROW;

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
import uk.ac.ucl.cs.pddlgen.ebnf.StructureDef;
import uk.ac.ucl.cs.pddlgen.ebnf.Term;
import uk.ac.ucl.cs.pddlgen.ebnf.TimelessDef;
import uk.ac.ucl.cs.pddlgen.ebnf.TypedList;
import uk.ac.ucl.cs.pddlgen.ebnf.TypesDef;
import uk.ac.ucl.cs.pddlgen.ebnf.Variable;

public class AfsmParser {

	private AdaptationFiniteStateMachine afsm;
	
	private Map<String, Name> names = new HashMap<String, Name>();
	
	private Map<String, Variable> variables = new HashMap<String, Variable>();
	
	private Map<String, Predicate> predicates = new HashMap<String, Predicate>();
	
	/**
	 * @param afsm
	 */
	public AfsmParser(AdaptationFiniteStateMachine afsm) {
		this.afsm = afsm;
	}
	
	public static void main(String[] args) {
		AfsmParser parser = new AfsmParser(new PhoneAdapterAfsm().getAdaptationFiniteStateMachine());
		parser.parse();
	}
	
	public void parse() {
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
		keys.add(RequireKey.PREFERENCIES);
		return RequireDef.create(keys);
	}
	
	/**
	 * Creates variables to model this domain
	 * <ul>
	 * <li> a type ''state''
	 * <li> 1 type for each variable
	 * 
	 * @return
	 */
	private TypesDef createTypesDef() {
		List<Name> names = new ArrayList<Name>();
		Name state = Name.create("state");
		names.add(state);
		this.names.put("state",state);
		for (String name : afsm.variables.keySet()) {
			Name var = Name.create(name);
			names.add(var);
			this.names.put(name, var);
		}
		
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
		
		Variable var = Variable.create(this.names.get("state"));
		variables.put("state", var);
		for (State state : afsm.states) {
			String key = "is-state-" + state.getName();
			Predicate predicate = Predicate.create(key);
			List<Variable> vars = new ArrayList<Variable>();
			vars.add(var);
			TypedList<Variable> typedList = TypedList.create(vars);
			AtomicFormulaSkeleton skeleton = AtomicFormulaSkeleton.create(predicate, typedList);
			formulas.add(skeleton);
			predicates.put(key, predicate);
		}
		
		for (String name : afsm.variables.keySet()) {
			String key = "is-true-" + name;
			Predicate predicate = Predicate.create(key);
			List<Variable> vars = new ArrayList<Variable>();
			Variable v = Variable.create(this.names.get(name));
			variables.put(name, v);
			vars.add(v);
			TypedList<Variable> typedList = TypedList.create(vars);
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
		//ActionDef actionDef = ActionDef.create();
		//StructureDef.create(methodDef);
		for (Rule rule : afsm.rules) {
			ActionFunctor functor = new ActionFunctor(Name.create("rule_" + rule.getName()));
			
			List<Variable> varName = new ArrayList<Variable>();
			for (uk.ac.ucl.cs.afsm.common.predicate.Variable v : rule.getAllVariables()) {
				varName.add(variables.get(v.getName()));
			}
			// Add state as a variable
			varName.add(variables.get("state"));
			TypedList<Variable> vars = TypedList.create(varName);
			
			GD preconditions = parsePredicate(rule.getTrigger());
			
			Effect[] and = new Effect[rule.action.size()];
			int i = 0;
			for (Assignment a : rule.action) {
				Effect eff;
				// predicate must be one satisfying the variable
				Predicate predicate = predicates.get(a.getVariable().getName());
				// the term is the name of the local variable
				Term t = Term.create(names.get(a.getVariable().getName()));
				AtomicFormula<Term> formula = AtomicFormula.create(predicate, t);
				if (a instanceof Assignment.Satisfy) {
					eff = Effect.createFormula(formula);
				} else {
					eff = Effect.createNot(formula);
				}
				and[i++] = eff;
			}
			Effect effect = Effect.createAnd(and);
			
			
			ActionDefBody body = ActionDefBody.create(preconditions, effect);
			ActionDef actionDef = ActionDef.create(functor, vars, body);
		}
		
		List<StructureDef> defs = new ArrayList<StructureDef>();
		return defs;
	}
	
	private GD parsePredicate(uk.ac.ucl.cs.afsm.common.predicate.Predicate predicate) {
		if (predicate == Constant.TRUE) {
			throw new IllegalStateException("True not defined: " + predicate);
		} else if (predicate == Constant.FALSE) {
			throw new IllegalStateException("False not defined: " + predicate);
		} if (predicate instanceof uk.ac.ucl.cs.afsm.common.predicate.Variable) {
			uk.ac.ucl.cs.afsm.common.predicate.Variable v = (uk.ac.ucl.cs.afsm.common.predicate.Variable)predicate;
			Term t = Term.create(names.get(v.getName()));
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
}
