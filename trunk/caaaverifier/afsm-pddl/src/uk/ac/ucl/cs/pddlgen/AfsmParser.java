package uk.ac.ucl.cs.pddlgen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import phoneAdapter.PhoneAdapterAfsm;

import com.sun.org.apache.bcel.internal.generic.ATHROW;

import uk.ac.ucl.cs.afsm.common.AdaptationFiniteStateMachine;
import uk.ac.ucl.cs.afsm.common.State;
import uk.ac.ucl.cs.pddlgen.ebnf.AtomicFormulaSkeleton;
import uk.ac.ucl.cs.pddlgen.ebnf.ConstantsDef;
import uk.ac.ucl.cs.pddlgen.ebnf.Domain;
import uk.ac.ucl.cs.pddlgen.ebnf.DomainVarDeclaration;
import uk.ac.ucl.cs.pddlgen.ebnf.DomainVarsDef;
import uk.ac.ucl.cs.pddlgen.ebnf.ExtensionDef;
import uk.ac.ucl.cs.pddlgen.ebnf.Literal;
import uk.ac.ucl.cs.pddlgen.ebnf.MethodDef;
import uk.ac.ucl.cs.pddlgen.ebnf.Name;
import uk.ac.ucl.cs.pddlgen.ebnf.Predicate;
import uk.ac.ucl.cs.pddlgen.ebnf.PredicatesDef;
import uk.ac.ucl.cs.pddlgen.ebnf.RequireDef;
import uk.ac.ucl.cs.pddlgen.ebnf.RequireKey;
import uk.ac.ucl.cs.pddlgen.ebnf.SafetyDef;
import uk.ac.ucl.cs.pddlgen.ebnf.StructureDef;
import uk.ac.ucl.cs.pddlgen.ebnf.TimelessDef;
import uk.ac.ucl.cs.pddlgen.ebnf.TypedList;
import uk.ac.ucl.cs.pddlgen.ebnf.TypesDef;
import uk.ac.ucl.cs.pddlgen.ebnf.Variable;

public class AfsmParser {

	private AdaptationFiniteStateMachine afsm;
	
	private Map<String, Name> names = new HashMap<String, Name>();
	
	private Map<String, Variable> variables = new HashMap<String, Variable>();
	
	private Map<String, AtomicFormulaSkeleton> predicates = new HashMap<String, AtomicFormulaSkeleton>();
	
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
			predicates.put(key, skeleton);
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
			predicates.put(key, skeleton);
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
		//MethodDef methodDef = MethodDef.create();
		//StructureDef.create(methodDef);
		List<StructureDef> defs = new ArrayList<StructureDef>();
		return defs;
	}
}
