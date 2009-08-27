package uk.ac.ucl.cs.pddlgen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import falsePositive.GPSAFSM;

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
import uk.ac.ucl.cs.pddlgen.ebnf.ActionSymbol;
import uk.ac.ucl.cs.pddlgen.ebnf.AssignOp;
import uk.ac.ucl.cs.pddlgen.ebnf.AtomicFormula;
import uk.ac.ucl.cs.pddlgen.ebnf.AtomicFormulaSkeleton;
import uk.ac.ucl.cs.pddlgen.ebnf.AtomicFunctionSkeleton;
import uk.ac.ucl.cs.pddlgen.ebnf.BinaryComp;
import uk.ac.ucl.cs.pddlgen.ebnf.CEffect;
import uk.ac.ucl.cs.pddlgen.ebnf.ConstantsDef;
import uk.ac.ucl.cs.pddlgen.ebnf.Domain;
import uk.ac.ucl.cs.pddlgen.ebnf.Effect;
import uk.ac.ucl.cs.pddlgen.ebnf.EmptyOr;
import uk.ac.ucl.cs.pddlgen.ebnf.FComp;
import uk.ac.ucl.cs.pddlgen.ebnf.FExp;
import uk.ac.ucl.cs.pddlgen.ebnf.FHead;
import uk.ac.ucl.cs.pddlgen.ebnf.FunctionSymbol;
import uk.ac.ucl.cs.pddlgen.ebnf.FunctionsDef;
import uk.ac.ucl.cs.pddlgen.ebnf.GD;
import uk.ac.ucl.cs.pddlgen.ebnf.InitEl;
import uk.ac.ucl.cs.pddlgen.ebnf.Literal;
import uk.ac.ucl.cs.pddlgen.ebnf.Name;
import uk.ac.ucl.cs.pddlgen.ebnf.Number;
import uk.ac.ucl.cs.pddlgen.ebnf.PEffect;
import uk.ac.ucl.cs.pddlgen.ebnf.PreGD;
import uk.ac.ucl.cs.pddlgen.ebnf.Predicate;
import uk.ac.ucl.cs.pddlgen.ebnf.PredicatesDef;
import uk.ac.ucl.cs.pddlgen.ebnf.PrefGD;
import uk.ac.ucl.cs.pddlgen.ebnf.RequireDef;
import uk.ac.ucl.cs.pddlgen.ebnf.RequireKey;
import uk.ac.ucl.cs.pddlgen.ebnf.Streamable;
import uk.ac.ucl.cs.pddlgen.ebnf.StructureDef;
import uk.ac.ucl.cs.pddlgen.ebnf.Term;
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
	
	public static final FunctionSymbol STATE_FUNCTION_SYMBOL = FunctionSymbol.create(STATE_NAME);
	public static final FunctionSymbol PRIORITY_FUNCTION_SYMBOL = FunctionSymbol.create(PRIORITY_NAME);
	
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
	private Map<State, Number> stateNumber = new HashMap<State, Number>();
	
	private Map<Rule, GD> ruleTriggersGD = new HashMap<Rule, GD>();
	
	private Domain domain;
	
	/**
	 * @param afsm
	 */
	public AfsmParser(AdaptationFiniteStateMachine afsm) {
		this.afsm = afsm;
	
		int id = 0;
		for (State s : this.afsm.states) {
			stateNumber.put(s, Number.create(id++));
		}
		
		domainName = Name.create(afsm.getName());
		requireDef = createRequireDef();
		domain = parse();
	}
	
	public static void main(String[] args) {
		AdaptationFiniteStateMachine afsm = new GPSAFSM().getAdaptationFiniteStateMachine();
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
				requireDef,//createRequireDef(), 
				createTypesDef(), 
				null, //createConstantsDef(), 
				createPredicatesDef(), 
				createFunctionsDef(),
				null, // createContraints()
				createStructureDefs()
				);
		return domain;
	}
	
	private FunctionsDef createFunctionsDef() {
		List<AtomicFunctionSkeleton> list = new ArrayList<AtomicFunctionSkeleton>();
		
		list.add(AtomicFunctionSkeleton.create(STATE_FUNCTION_SYMBOL));
		list.add(AtomicFunctionSkeleton.create(PRIORITY_FUNCTION_SYMBOL));
		
		TypedList<AtomicFunctionSkeleton> functionsList = TypedList.create(list);
		return FunctionsDef.create(functionsList);
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
		keys.add(RequireKey.FLUENTS);
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
		// Nothing has to be done.
		TypedList<Name> types = TypedList.create(names);
		return ConstantsDef.create(types);
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
		
		List<Variable> vars = new ArrayList<Variable>();
		vars.add(CONTEXT_VARIABLE);

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
			TypedList<Variable> variables = createPriorityStateContextTypedList();
			
			GD trigger = parsePredicate(rule.getTrigger());
			ruleTriggersGD.put(rule, trigger);
			GD initialStates = parseInitialStates(rule);
			
			GD priority = createPriorityGD(rule.getPriority());
			
			PreGD preconditions = PreGD.create(
					PrefGD.create(
							GD.createAnd(initialStates, priority, trigger)
							)
					); 
			
			// One effect per rule plus one per the destination state and one for priority
			CEffect[] and = new CEffect[rule.action.size() + 2];
			
			int i = 0;
			for (Assignment a : rule.action) {
				and[i++] = createEffectForAssignment(a);
			}
			
			//Add the future state
			and[i++] = createEffectForState(rule.getDestination());
			
			PEffect resetPriority = createPEffectResetPriority();
			and[i++] = CEffect.create(resetPriority);
			
			Effect effect = Effect.and(and);
			
			
			ActionSymbol actionSymbol = ActionSymbol.create("rule_" + rule.getName());
			ActionDefBody actionDefBody = ActionDefBody.create(preconditions, effect);			
			
			ActionDef actionDef = ActionDef.create(actionSymbol, variables, actionDefBody);
			StructureDef struct = StructureDef.create(actionDef);
			
			defs.add(struct);
		}
		
		// Add events to satisfy/unsatisfy each variables
		for (String key : afsm.variables.keySet()) {
			uk.ac.ucl.cs.afsm.common.predicate.Variable v = afsm.variables.get(key);
			
			TypedList<Variable> variables = null;
			// Add context
			variables = createTypedList(CONTEXT_VARIABLE, CONTEXT_TYPE, variables);
			
			List<GD> satisfyPrec = new ArrayList<GD>();
			List<GD> unsatisfyPrec = new ArrayList<GD>();
			
			for (Constrain con : afsm.constrains) {
				if (v.equals(con.requiree)) {
					parseConstrainForPrecondition(con, satisfyPrec, unsatisfyPrec);
				}
			}
			
			GD priority = createPriorityGD(Rule.LOW_PRIORITY + 1);
			GD satisfyPreconditions = null;
			GD unsatisfyPreconditions = null;
			if (satisfyPrec.size() > 0) {
				satisfyPreconditions = GD.createOr(satisfyPrec.toArray(new GD[satisfyPrec.size()]));
				satisfyPreconditions = GD.createAnd(priority, satisfyPreconditions, GD.createNot(parsePredicate(v)));
			} else {
				satisfyPreconditions = GD.createAnd(priority, GD.createNot(parsePredicate(v)));
			}
			
			if (unsatisfyPrec.size() > 0) {
				unsatisfyPreconditions = GD.createOr(unsatisfyPrec.toArray(new GD[unsatisfyPrec.size()]));
				unsatisfyPreconditions = GD.createAnd(priority, unsatisfyPreconditions, parsePredicate(v));
			} else {
				unsatisfyPreconditions =  GD.createAnd(priority, parsePredicate(v));
			}
			
			// Add AND with the variable negated
			List<CEffect> satisfyEff = new ArrayList<CEffect>();
			List<CEffect> unsatisfyEff = new ArrayList<CEffect>();
			
			// Add constraints in the effect
			for (Constrain con : afsm.constrains) {
				if (v.equals(con.required)) {
					parseConstrainForEffect(con, satisfyEff, unsatisfyEff);
				}
			}
			
			satisfyEff.add(
					CEffect.create(
							PEffect.create(
									AtomicFormula.create(predicates.get(v.getName()),
											CONTEXT_TERM)
								)
							)
					);
			unsatisfyEff.add(
					CEffect.create(
							PEffect.not(
									AtomicFormula.create(predicates.get(v.getName()),
											CONTEXT_TERM)
								)
							)
					);
			
			// Reset Priority
			satisfyEff.add(CEffect.create(createPEffectResetPriority()));
			unsatisfyEff.add(CEffect.create(createPEffectResetPriority()));
			
			
			Effect satisfyEffect = Effect.and(
					satisfyEff.toArray(new CEffect[satisfyEff.size()]));
			Effect unsatisfyEffect = Effect.and(
					unsatisfyEff.toArray(new CEffect[unsatisfyEff.size()]));
			
			ActionSymbol actionSymbolSat = ActionSymbol.create("satisfy_" + v.getName());
			ActionSymbol actionSymbolUnsat = ActionSymbol.create("unsatisfy_" + v.getName());
			
			ActionDefBody actionDefBodySat = ActionDefBody.create(
					PreGD.create(PrefGD.create(satisfyPreconditions)), satisfyEffect);
			ActionDefBody actionDefBodyUnsat = ActionDefBody.create(
					PreGD.create(PrefGD.create(unsatisfyPreconditions)), unsatisfyEffect);
			
			
			ActionDef actionDefSatisfy = ActionDef.create(actionSymbolSat, variables, actionDefBodySat);
			ActionDef actionDefUnsatisfy = ActionDef.create(actionSymbolUnsat, variables, actionDefBodyUnsat);
			
			defs.add(StructureDef.create(actionDefSatisfy));
			defs.add(StructureDef.create(actionDefUnsatisfy));
		}
		
		defs.addAll(createIncreasePriority());
		
		return defs;
	}

	private PEffect createPEffectResetPriority() {
		PEffect resetPriority = PEffect.fluent(
				AssignOp.assign(), 
				FHead.create(PRIORITY_FUNCTION_SYMBOL),
				// Reset the priority
				FExp.create(Number.create(Rule.MAX_PRIORITY)));
		return resetPriority;
	}

	private List<StructureDef> createIncreasePriority() {
		List<StructureDef> defs = new ArrayList<StructureDef>();
		
		// COntext changes act into a level of priority lower than rules (Rule.LOW_PRIORITY + 1)
		for (int i = Rule.MAX_PRIORITY; i <= Rule.LOW_PRIORITY; i++) {
			ActionSymbol actionSymbol = ActionSymbol.create("set_priority_" + (i + 1));
			TypedList<Variable> variables = null;
			// Add context
			variables = createTypedList(CONTEXT_VARIABLE, CONTEXT_TYPE, variables);
			
			Effect increasePriority = Effect.create(
					CEffect.create(
							PEffect.fluent(
									AssignOp.assign(), 
									FHead.create(PRIORITY_FUNCTION_SYMBOL),
									FExp.create(Number.create(i + 1))
							)
					)
			);
			
			PreGD preconditions = createPreconditionForPriorityAt(i);
			
			ActionDefBody actionDefBody = ActionDefBody.create(
					preconditions,
					increasePriority
					);
			
			ActionDef actionDef = ActionDef.create(actionSymbol, variables, actionDefBody);
			defs.add(StructureDef.create(actionDef));
		}
		
		return defs;
	}
	
	private PreGD createPreconditionForPriorityAt(int currentPriority) {

		List<GD> priorities = new ArrayList<GD>();
		
		for (State s : afsm.states){
			List<uk.ac.ucl.cs.afsm.common.predicate.Predicate> predicates = 
				new ArrayList<uk.ac.ucl.cs.afsm.common.predicate.Predicate>();
			for (Rule r : s.outGoingRules) {
				if (r.getPriority() == currentPriority) {
					predicates.add(r.getTrigger());
				}
			}
			GD stateGD = null;
			if (predicates.size() > 0) {
				Operator pInState = Operator.not(Operator.or(predicates));
				stateGD = GD.createAnd(
						createStateGDForProblem(s),
						parsePredicate(pInState)
				);
			} else {
				stateGD = createStateGDForProblem(s);
			}
			priorities.add(stateGD);
		}
		
		GD globalActivation = GD.createAnd(
				createPriorityGD(currentPriority),
				GD.createOr(priorities.toArray(new GD[priorities.size()]))
		);
		
		return PreGD.create(
				PrefGD.create(globalActivation)
		);
	}

	private GD createPriorityGD(int priority) {
		return GD.createFluent(
				FComp.create(
						BinaryComp.equals(), 
						FExp.create(FHead.create(PRIORITY_FUNCTION_SYMBOL)),
						FExp.create(Number.create(priority))));
	}

	private void parseConstrainForEffect(Constrain con,
			List<CEffect> satisfyEff, List<CEffect> unsatisfyEff) {
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
			List<CEffect> satisfyEff, List<CEffect> unsatisfyEff) {
		AtomicFormula<Term> formula = AtomicFormula.create(predicates.get(con.requiree.getName()), CONTEXT_TERM);
		PEffect pEffect = PEffect.create(formula);
		CEffect cEffect = CEffect.create(pEffect);
		unsatisfyEff.add(cEffect);
	}
	
	private void parseConstrainForEffect(Constrain.AThenNotB con,
			List<CEffect> satisfyEff, List<CEffect> unsatisfyEff) {
		AtomicFormula<Term> formula = AtomicFormula.create(predicates.get(con.requiree.getName()), CONTEXT_TERM);
		PEffect pEffect = PEffect.not(formula);
		CEffect cEffect = CEffect.create(pEffect);
		unsatisfyEff.add(cEffect);
	}
	
	private void parseConstrainForEffect(Constrain.NotAThenB con,
			List<CEffect> satisfyEff, List<CEffect> unsatisfyEff) {
		AtomicFormula<Term> formula = AtomicFormula.create(predicates.get(con.requiree.getName()), CONTEXT_TERM);
		PEffect pEffect = PEffect.create(formula);
		CEffect cEffect = CEffect.create(pEffect);
		unsatisfyEff.add(cEffect);
	}
	
	private void parseConstrainForEffect(Constrain.NotAThenNotB con,
			List<CEffect> satisfyEff, List<CEffect> unsatisfyEff) {
		AtomicFormula<Term> formula = AtomicFormula.create(predicates.get(con.requiree.getName()), CONTEXT_TERM);
		PEffect pEffect = PEffect.not(formula);
		CEffect cEffect = CEffect.create(pEffect);
		unsatisfyEff.add(cEffect);
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
		satisfyPrec.add(parsePredicate(con.required));
	}
	
	public static TypedList<Variable> createPriorityStateContextTypedList() {
		TypedList<Variable> typedList = null;
		
		// Add vontext
		typedList = createTypedList(CONTEXT_VARIABLE, CONTEXT_TYPE, typedList);
		
		// Add state as a variable
		//typedList = createTypedList(STATE_VARIABLE, STATE_TYPE, typedList);
		
		// Add priority
		//typedList = createTypedList(PRIORITY_VARIABLE, PRIORITY_TYPE, typedList);
		
		return typedList;
	}
	
	private static TypedList<Variable> createTypedList(Variable v, Type t, TypedList<Variable>  tail) {
		List<Variable> vars = new ArrayList<Variable>();
		vars.add(v);
		return TypedList.create(vars, t, tail);
	}

	private CEffect createEffectForAssignment(Assignment s) {
		Predicate predicate = predicates.get(s.getVariable().getName());
		AtomicFormula<Term> formula = AtomicFormula.create(predicate, CONTEXT_TERM);
		PEffect pEffect = (s instanceof Assignment.Negate) ? PEffect.not(formula) : PEffect.create(formula);
		CEffect cEffect = CEffect.create(pEffect);
		return cEffect;
	}
	
	private CEffect createEffectForState(State s) {
		//Predicate predicate = predicates.get(s.getName());
		//AtomicFormula<Term> formula = AtomicFormula.create(predicate, STATE_TERM);	
		
		PEffect pEffect = PEffect.fluent(AssignOp.assign(),
				FHead.create(STATE_FUNCTION_SYMBOL), 
				FExp.create(stateNumber.get(s)));
		CEffect cEffect = CEffect.create(pEffect);
		return cEffect;
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
				FComp fComp = FComp.create(BinaryComp.equals(),
						FExp.create(FHead.create(STATE_FUNCTION_SYMBOL)),
						FExp.create(stateNumber.get(s)));
				states.add(GD.createFluent(fComp));
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
	
	public InitEl createStateInitEl(State s) {
		//return AtomicFormula.create(predicates.get(s.getName()), STATE_VARIABLE_NAME);
		return InitEl.create(FHead.create(STATE_FUNCTION_SYMBOL), stateNumber.get(s));
	}
	
	public InitEl createPriotityInitEl(int p) {
		//return AtomicFormula.create(predicates.get(s.getName()), STATE_VARIABLE_NAME);
		return InitEl.create(FHead.create(PRIORITY_FUNCTION_SYMBOL), Number.create(p));
	}
	
	public GD createStateGDForProblem(State s) {
		FComp fComp = FComp.create(BinaryComp.equals(),
				FExp.create(FHead.create(STATE_FUNCTION_SYMBOL)),
				FExp.create(stateNumber.get(s)));
		return GD.createFluent(fComp);
	}
	
	public GD createInStateAssumptionGDForProblem(State s) {
		return parsePredicate(s.getInStateAssumption(), Term.create(CONTEXT_VARIABLE_NAME), true);
	}
	
	public List<Literal<Name>> createInitiContextLiteralsForProblem() {
		List<Literal<Name>> list = new ArrayList<Literal<Name>>();
		//list.add(Literal.createFormula(AtomicFormula.create(existPredicate, CONTEXT_VARIABLE_NAME)));
		for (String key : afsm.variables.keySet()) {
			Predicate p = predicates.get(key);
			AtomicFormula<Name> f = AtomicFormula.create(p, CONTEXT_VARIABLE_NAME);
			list.add(Literal.createNot(f));
		}
		return list;
	}
}
