package uk.ac.ucl.cs.afsm2obdd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.BDDPairing;
import net.sf.javabdd.BDDVarSet;
import net.sf.javabdd.JDDFactory;
import net.sf.javabdd.JFactory;
import uk.ac.ucl.cs.afsm.common.AdaptationFiniteStateMachine;
import uk.ac.ucl.cs.afsm.common.Assignment;
import uk.ac.ucl.cs.afsm.common.Rule;
import uk.ac.ucl.cs.afsm.common.State;
import uk.ac.ucl.cs.afsm.common.predicate.Constant;
import uk.ac.ucl.cs.afsm.common.predicate.Operator;
import uk.ac.ucl.cs.afsm.common.predicate.Predicate;
import uk.ac.ucl.cs.afsm.common.predicate.Variable;

/**
 * The boolean variables will be ordered as:
 * <ul>
 * <li>Variables: one boolean for each variable
 * <li>States: one boolean for each variable
 * <li>Rules: one boolean for each variable
 * 
 * @author -RAX- (Michele Sama)
 *
 */
public class AfsmParser {
	
	/**
	 * @param afsm
	 * @param encodeStates
	 * @param encodeRules
	 */
	public AfsmParser(AdaptationFiniteStateMachine afsm, boolean encodeAfsmBDD) {
		this.encodeAfsmBDD = encodeAfsmBDD;
		parseAfsm(afsm);
	}

	private AdaptationFiniteStateMachine afsm;
	
	private BDDFactory factory;

	/** VarSet containing all the variables used for predicates*/
	BDDVarSet varVarSet;
	
	/** Start index for the boolean representing the variables */
	private int varStartIndex;
	/**
	 * Number of boolean used to encode the variables. 
	 * Each variable will be encoded with a boolean.
	 */
	private int varNumBoolean;
	/** Collects all the BDDs representing a variable */
	public final Map<String, BDD> variablesPositive = new HashMap<String, BDD>();
	public final Map<String, BDD> variablesNegative = new HashMap<String, BDD>();

	/** VarSet containing all the variables used for states*/
	BDDVarSet stateVarSet;
	BDDVarSet futureStateVarSet;
	BDDPairing statePairingFutureToPast;
	BDDPairing statePairingPastToFuture;

	
	private StateBDD initialState;
	/** Start index of the boolean variables representing the states. */
	private int stateStartIndex;
	/** Number of boolean variables used to encode the states */
	private int stateNumBoolean;

	/** Collect all the {link StateBDD}s */
	public final ArrayList<StateBDD> states = new ArrayList<StateBDD>();
	
	/** VarSet containing all the variables used for rules*/
	BDDVarSet ruleVarSet;
	
	/** Start index of the boolean variables representing the rules. */
	private int ruleStartIndex; 
	/** Number of boolean variables used to encode the rules */
	private int ruleNumBoolean;

	private boolean encodeAfsmBDD = false;
	/** Collect all the {link RuleBDD}s */
	public final ArrayList<RuleBDD> rules = new ArrayList<RuleBDD>();
	
	
	
	/** VarSet containing all the variables used for actions*/
	BDDVarSet actionVarSet;
	BDDPairing actionPairingToVariables;
	
	/** Start index for the boolean representing the variables */
	private int actionStartIndex;

	/** Collects all the BDDs representing a variable */
	public final Map<String, BDD> actionVariablesPositive = new HashMap<String, BDD>();
	public final Map<String, BDD> actionVariablesNegative = new HashMap<String, BDD>();
	
	
	private BDD globalActivation;
	
	private void init() {
		// TODO(rax): tune this two parameters.
		factory = JFactory.init(1000, 100000);
		factory.enableReorder();
		factory.autoReorder(BDDFactory.REORDER_SIFT);
		// The number of variables will be increased while processing the AFSM
		//factory.setVarNum(0);
		
		states.clear();
		rules.clear();
		variablesPositive.clear();
		variablesNegative.clear();
	}
	
	private void parseAfsm(final AdaptationFiniteStateMachine afsm) {
		if (afsm == null) {
			throw new IllegalArgumentException("The AFSM cannot be null.");
		}
		if (this.afsm != null) {
			throw new IllegalStateException("An AFSM was already set.");
		}
		this.afsm = afsm;
		init();
		if (factory.varNum() != 0) {
			throw new IllegalStateException("No variable should have been defined yet!");
		}
		
		// Encode variables.
		varVarSet = factory.emptySet();
		varStartIndex = factory.varNum();
		varNumBoolean = afsm.variables.size();
		if (varNumBoolean != 0) {
			factory.setVarNum(varStartIndex + varNumBoolean);
		}
		int varIndex = varStartIndex;
		for (String name : afsm.variables.keySet()) {
			variablesPositive.put(name, factory.ithVar(varIndex));
			variablesNegative.put(name, factory.nithVar(varIndex));
			varVarSet = varVarSet.union(varIndex);
			varIndex ++;
		}
		if (varIndex != varStartIndex + varNumBoolean) {
			throw new IllegalStateException("An error occurred while generating BDDs for the" +
					"variables: " + varIndex + " != " + varStartIndex + " + " +
					varNumBoolean + ".");
		}
		if (afsm.variables.size() != variablesPositive.size() ||
				afsm.variables.size() != variablesNegative.size()) {
			throw new IllegalStateException("The number of created variables " +
					"does not match the size in the AFSM : " +
					afsm.variables.size() + " != " + variablesPositive.size() + " or " +
					afsm.variables.size() + " != " + variablesNegative.size());
		}
		System.out.println("Created " + varNumBoolean + " BDD for variables.");
		
		/*
		 * Encode actions
		 */
		actionVarSet = factory.emptySet();
		actionStartIndex = factory.varNum();
		actionPairingToVariables = factory.makePair();
		// 
		if (varNumBoolean != 0) {
			factory.setVarNum(actionStartIndex + varNumBoolean);
		}
		int actionIndex = actionStartIndex;
		for (String name : afsm.variables.keySet()) {
			actionVariablesPositive.put(name, factory.ithVar(actionIndex));
			actionVariablesNegative.put(name, factory.nithVar(actionIndex));
			actionVarSet = actionVarSet.union(actionVarSet);
			actionIndex ++;
		}
		if (afsm.variables.size() != actionVariablesPositive.size() ||
				afsm.variables.size() != actionVariablesNegative.size()) {
			throw new IllegalStateException("The number of created action variables " +
					"does not match the size in the AFSM : " +
					afsm.variables.size() + " != " + actionVariablesPositive.size() + " or " +
					afsm.variables.size() + " != " + actionVariablesNegative.size());
		}
		System.out.println("Created " + varNumBoolean + " BDD for action variables.");
		for (int i = 0; i < varNumBoolean; i++) {
			int var1 = i + varStartIndex;
			int var2 = i + actionStartIndex;
			actionPairingToVariables.set(var1, var2);
		}
		// END actions
		
		// Encode states.
		// Create a set of variables to encode the states and store the relative BDDs.
		stateVarSet = factory.emptySet();
		futureStateVarSet = factory.emptySet();
		statePairingFutureToPast = factory.makePair();
		statePairingPastToFuture = factory.makePair();
		stateStartIndex = factory.varNum();
		int stateIndex =  0;
		if (true) {
			int numStates = afsm.states.size();
			if (numStates != 0) {
				// The logarithm of 0 is 1!
				// In the border case with no states the number of boolean has to be 0.
				stateNumBoolean = (int) Math.ceil(Math.log(numStates) / Math.log(2));
				factory.setVarNum(stateStartIndex + 2 * stateNumBoolean);
			} else {
				stateNumBoolean = 0;
			}
			for (State state : afsm.states) {
				BDD stateEncoding = createBDDFromBooleanEncoding(stateIndex, stateNumBoolean, stateStartIndex);
				BDD futureEncoding = createBDDFromBooleanEncoding(stateIndex, stateNumBoolean, stateStartIndex + stateNumBoolean);
				StateBDD stateBdd = new StateBDD(state.getName(), stateEncoding, futureEncoding);
				stateBdd.setInStateCondition(createBDDFromPredicate(state.getInStateCondition()));
				stateBdd.setInStateAssumption(createBDDFromPredicate(state.getInStateAssumption()));
				states.add(stateBdd);
				stateIndex ++;
			}
		}/* else {
			stateNumBoolean = afsm.states.size();
			if (stateNumBoolean != 0) {
				factory.setVarNum(stateStartIndex + 2 * stateNumBoolean);
			}
			for (State state : afsm.states) {
				BDD stateEncoding = createExclusiveBDD(stateIndex, stateNumBoolean, stateStartIndex);
				BDD futureEncoding = createExclusiveBDD(stateIndex, stateNumBoolean, stateStartIndex + stateNumBoolean);
				StateBDD stateBdd = new StateBDD(state.getName(), stateEncoding, futureEncoding);
				stateBdd.setInStateCondition(createBDDFromPredicate(state.getInStateCondition()));
				stateBdd.setInStateAssumption(createBDDFromPredicate(state.getInStateAssumption()));
				states.add(stateBdd);
				stateIndex ++;
			}
		}*/
		for (int i = 0; i < stateNumBoolean; i++) {
			int var1 = i + stateStartIndex;
			int var2 = var1 + stateNumBoolean;
			stateVarSet = stateVarSet.union(var1);
			futureStateVarSet = futureStateVarSet.union(var2);
			statePairingPastToFuture.set(var1, var2);
			statePairingFutureToPast.set(var2, var1);
		}
		if (afsm.states.size() != states.size()) {
			throw new IllegalStateException("The number of created states " +
					"does not size the variables in the AFSM: " +
					afsm.states.size() + " != " + states.size());
		}
		initialState = getStateBdd(afsm.getInitialState().getName());
		System.out.println("Created " + stateNumBoolean + " BDD for states.");
		
		// Add another set of variables for the future state.
		//factory.setVarNum(factory.varNum() + stateNumBoolean);

		// Encode rules
		ruleVarSet = factory.emptySet();
		ruleStartIndex = factory.varNum();
		int ruleIndex = 0;
		if (true) {
			int numRules = afsm.rules.size();
			if (numRules != 0) {
				// The logarithm of 0 is 1!
				// In the border case with no rules the number of boolean has to be 0.
				ruleNumBoolean = (int) Math.ceil(Math.log(numRules) / Math.log(2));
				factory.setVarNum(ruleStartIndex + ruleNumBoolean);
			} else {
				ruleNumBoolean = 0;
			}
			for (Rule rule : afsm.rules) {
				RuleBDD ruleBdd= new RuleBDD(rule.getName(),
						createBDDFromBooleanEncoding(ruleIndex, ruleNumBoolean, ruleStartIndex));
				ruleBdd.setPriority(rule.getPriority());
				rules.add(ruleBdd);
				ruleBdd.setAction(parseAssignment(rule.action));
				ruleBdd.setDestination(getStateBdd(rule.getDestination().getName()));
				ruleBdd.setTrigger(createBDDFromPredicate(rule.getTrigger()));
				ruleIndex ++;
			}
		} /*else {
			ruleNumBoolean = afsm.rules.size();
			if (ruleNumBoolean != 0) {
				factory.setVarNum(ruleStartIndex + ruleNumBoolean);
			}
			for (Rule rule : afsm.rules) {
				BDD ruleEncoding = createExclusiveBDD(ruleIndex, ruleNumBoolean, ruleStartIndex);
				RuleBDD ruleBdd= new RuleBDD(rule.getName(), ruleEncoding);
				rules.add(ruleBdd);
				// TODO(rax): ruleBdd.setAction(action);
				ruleBdd.setPriority(rule.getPriority());
				ruleBdd.setDestination(getStateBdd(rule.getDestination().getName()));
				ruleBdd.setTrigger(createBDDFromPredicate(rule.getTrigger()));
				ruleIndex ++;
			}
		}*/
		for (int i = 0; i < ruleNumBoolean; i++) {
			ruleVarSet = ruleVarSet.union(i + ruleStartIndex);
		}
		if (afsm.rules.size() != rules.size()) {
			throw new IllegalStateException("The number of created rules " +
					"does not match the size in the AFSM: " +
					afsm.rules.size() + " != " + rules.size());
		}
		System.out.println("Created " + ruleNumBoolean + " BDD for rules.");

		
		// Encode rules in state.
		// TODO(rax): remove this if not needed
		for (State state : afsm.states) {
			StateBDD stateBDD = getStateBdd(state.getName());
			for (Rule rule : state.outGoingRules) {
				RuleBDD r = getRuleBdd(rule.getName());
				stateBDD.outGoingRules.add(r);
			}
		}
		
		// reate activation BDD
		if (encodeAfsmBDD) {
			BDD activation = factory.zero(); 
			for (StateBDD state : states) {
				BDD stateActivation = state.getActivation().apply(state.getEncoding(), BDDFactory.and);
				activation = activation.apply(stateActivation, BDDFactory.or);
			}
			globalActivation = activation;
		} else {
			for (StateBDD state : states) {
				state.getActivation();
			}
		}
		factory.reorder(BDDFactory.REORDER_SIFT);
	}
	
	public StateBDD getStateBdd(String name) {
		StateBDD stateBDD = null;
		for (StateBDD st : states) {
			if (name.equals(st.getName())) {
				stateBDD = st;
				break;
			}
		}
		if (stateBDD == null) {
			throw new IllegalStateException("Intenal error: state " + name + " was not created correctly.");
		}
		return stateBDD;
	}
	
	public RuleBDD getRuleBdd(String name) {
		RuleBDD ruleBDD = null;
		for (RuleBDD rl : rules) {
			if (name.equals(rl.getName())) {
				ruleBDD = rl;
				break;
			}
		}
		if (ruleBDD == null) {
			throw new IllegalStateException("Intenal error: rule " + name + " was not created correctly.");
		}
		return ruleBDD;
	}
	
	private BDD createExclusiveBDD(int index, int varNum, int varStartIndex) {
		BDD encoding = factory.one();
		// Negate all the other variables representing a state.
		for (int i = 0; i < varNum ; i++) {
			if (index == i) {
				encoding.andWith(factory.ithVar(i + varStartIndex));
			} else {
				encoding.andWith(factory.nithVar(i + varStartIndex));
			}
		}
		return encoding;
	}
	
	private BDD createBDDFromBooleanEncoding(int index, int encodingVarNum, int varStartIndex) {
		BDD result = factory.one();
		for (int i = 0; i < encodingVarNum; i++) {
			BDD nextVar = (((index >>> i) & 1) == 0) ?
					factory.nithVar(i + varStartIndex) :
					factory.ithVar(i + varStartIndex);
			result = result.apply(nextVar, BDDFactory.and);
		}
		return result;
	}
	
	private BDD createBDDFromPredicate(Predicate predicate) {
		if (predicate == Constant.TRUE) {
			return factory.one();
		} else if (predicate == Constant.FALSE) {
			return factory.zero();
		} if (predicate instanceof Variable) {
			Variable var = (Variable) predicate;
			return variablesPositive.get(var.getName());
		} else if (predicate instanceof Operator.Not) {
			Operator.Not not = (Operator.Not) predicate;
			Predicate p = not.getPredicate();
			if (p instanceof Variable) {
				Variable v = (Variable) p;
				return variablesNegative.get(v.getName());
			} else {
				return createBDDFromPredicate(p).not();
			}
		} else if (predicate instanceof Operator.And) {
			Operator.And and = (Operator.And) predicate;
			BDD predicateBDD = factory.one();
			for (Predicate p : and) {
				//predicateBDD.andWith(createBDDFromPredicate(p));
				predicateBDD = predicateBDD.apply(createBDDFromPredicate(p), BDDFactory.and);
			}
			return predicateBDD;
		} else if (predicate instanceof Operator.Or) {
			Operator.Or or = (Operator.Or) predicate;
			BDD predicateBDD = factory.zero();
			for (Predicate p : or) {
				//predicateBDD.orWith(createBDDFromPredicate(p));
				predicateBDD = predicateBDD.apply(createBDDFromPredicate(p), BDDFactory.or);
			}
			return predicateBDD;
		}
		throw new IllegalStateException(
				"This method should have already returned a value: " + predicate);
	}

	private BDD parseAssignment(List<Assignment> action) {
		BDD effect = factory.one();
		for (Assignment a : action) {
			if (a instanceof Assignment.Negate) {
				Assignment.Negate negate = (Assignment.Negate)a;
				BDD var = actionVariablesNegative.get(negate.getVariable().getName());
				effect = effect.apply(var, BDDFactory.and);
			} else if (a instanceof Assignment.Satisfy) {
				Assignment.Satisfy satisfy = (Assignment.Satisfy)a;
				BDD var = actionVariablesPositive.get(satisfy.getVariable().getName());
				effect = effect.apply(var, BDDFactory.and);
			}
		}
		return effect;
	}

	public boolean isEncodeAfsmBDD() {
		return encodeAfsmBDD;
	}

	public BDDFactory getFactory() {
		return factory;
	}
	
	public BDD getStateMask() {
		BDD filter = factory.one();
		for (int i = 0; i < varNumBoolean; i++) {
			filter = filter.apply(factory.nithVar(varStartIndex + i), BDDFactory.and);
		}
		for (int i = 0; i < ruleNumBoolean; i++) {
			filter = filter.apply(factory.nithVar(ruleStartIndex + i), BDDFactory.and);
		}
		/*for (int i = 0; i < stateNumBoolean; i++) {
			filter = filter.apply(factory.ithVar(stateStartIndex + i), BDDFactory.and);
		}*/
		return filter;
	}

	public BDDVarSet getStateVarSet() {
		return stateVarSet;
	}

	public BDDVarSet getFutureStateVarSet() {
		return futureStateVarSet;
	}

	public BDDVarSet getRuleVarSet() {
		return ruleVarSet;
	}
	
	public BDDVarSet getVarVarSet() {
		return varVarSet;
	}
	
	public BDDVarSet getActionVarSet() {
		return actionVarSet;
	}

	public BDD getGlobalActivation() {
		return globalActivation;
	}

	public BDDPairing getStatePairingFutureToPast() {
		return statePairingFutureToPast;
	}
	
	public BDDPairing getStatePairingPastToFuture() {
		return statePairingPastToFuture;
	}

	/**
	 * @return the initialState
	 */
	public StateBDD getInitialState() {
		return initialState;
	}

	/**
	 * @return the actionPairingToVariables
	 */
	public BDDPairing getActionPairingToVariables() {
		return actionPairingToVariables;
	}
}
