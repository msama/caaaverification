package uk.ac.ucl.cs.pddlgen;

import java.util.ArrayList;
import java.util.List;

import com.sun.org.apache.bcel.internal.generic.ATHROW;

import uk.ac.ucl.cs.afsm.common.AdaptationFiniteStateMachine;
import uk.ac.ucl.cs.pddlgen.ebnf.AtomicFormulaSkeleton;
import uk.ac.ucl.cs.pddlgen.ebnf.ConstantsDef;
import uk.ac.ucl.cs.pddlgen.ebnf.Domain;
import uk.ac.ucl.cs.pddlgen.ebnf.DomainVarDeclaration;
import uk.ac.ucl.cs.pddlgen.ebnf.DomainVarsDef;
import uk.ac.ucl.cs.pddlgen.ebnf.ExtensionDef;
import uk.ac.ucl.cs.pddlgen.ebnf.Literal;
import uk.ac.ucl.cs.pddlgen.ebnf.MethodDef;
import uk.ac.ucl.cs.pddlgen.ebnf.Name;
import uk.ac.ucl.cs.pddlgen.ebnf.PredicatesDef;
import uk.ac.ucl.cs.pddlgen.ebnf.RequireDef;
import uk.ac.ucl.cs.pddlgen.ebnf.RequireKey;
import uk.ac.ucl.cs.pddlgen.ebnf.SafetyDef;
import uk.ac.ucl.cs.pddlgen.ebnf.StructureDef;
import uk.ac.ucl.cs.pddlgen.ebnf.TimelessDef;
import uk.ac.ucl.cs.pddlgen.ebnf.TypedList;
import uk.ac.ucl.cs.pddlgen.ebnf.TypesDef;

public class AfsmParser {

	AdaptationFiniteStateMachine afsm;
	
	public void parse() {
		Domain domain = Domain.create(afsm.getName(),
				createExtensionDef(),
				createRequireDef(), 
				createTypesDef(), 
				createConstantsDef(), 
				createDomainVarsDef(), 
				createPredicatesDef(), 
				createTimelessDef(), 
				null,//safetyDef, 
				structureDefs);
	}
	
	private ExtensionDef createExtensionDef() {
		List<Name> names = new ArrayList<Name>();
		// TODO(rax): add extensions
		return ExtensionDef.create(names);
	}
	
	private RequireDef createRequireDef() {
		List<RequireKey> keys = new ArrayList<RequireKey>();
		// TODO(rax): add requirements
		return RequireDef.create(keys);
	}
	
	private TypesDef createTypesDef() {
		List<Name> names = new ArrayList<Name>();
		TypedList<Name> types = TypedList.create(names);
		// TODO(rax): add requirements
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
	
	private PredicatesDef createPredicatesDef() {
		List<AtomicFormulaSkeleton> formulas = new ArrayList<AtomicFormulaSkeleton>();
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
	
	private StructureDef createStructureDef() {
		MethodDef methodDef = MethodDef.create();
		return StructureDef.create(methodDef);
	}
}
