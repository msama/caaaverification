package uk.ac.ucl.cs.InputMatrix.caseStudies;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import uk.ac.ucl.cs.InputMatrix.AdaptationFSM;
import uk.ac.ucl.cs.InputMatrix.AdaptationRule;
import uk.ac.ucl.cs.InputMatrix.ContextVariable;
import uk.ac.ucl.cs.InputMatrix.Fault;
import uk.ac.ucl.cs.InputMatrix.Predicate;
import uk.ac.ucl.cs.InputMatrix.PredicateVariable;
import uk.ac.ucl.cs.InputMatrix.Rule;
import uk.ac.ucl.cs.InputMatrix.State;
import uk.ac.ucl.cs.InputMatrix.validation.ContextHazardDetection;
import uk.ac.ucl.cs.InputMatrix.validation.ContextHazardDetectionOptimized;
import uk.ac.ucl.cs.InputMatrix.validation.ContextHazardFault;
import uk.ac.ucl.cs.InputMatrix.validation.DeadConditionFault;
import uk.ac.ucl.cs.InputMatrix.validation.DeadConditionsDetection;
import uk.ac.ucl.cs.InputMatrix.validation.ForbiddenStateViolationFault;
import uk.ac.ucl.cs.InputMatrix.validation.ForbiddenStatesViolation;
import uk.ac.ucl.cs.InputMatrix.validation.InStateFault;
import uk.ac.ucl.cs.InputMatrix.validation.MinimumSafeDelayGenerator;
import uk.ac.ucl.cs.InputMatrix.validation.NondeterministicActivation;
import uk.ac.ucl.cs.InputMatrix.validation.RaceAndCycle;
import uk.ac.ucl.cs.InputMatrix.validation.UnrechableStatesDetection;

import static uk.ac.ucl.cs.InputMatrix.NotPredicate.not;
import static uk.ac.ucl.cs.InputMatrix.OrPredicate.or;
import static uk.ac.ucl.cs.InputMatrix.AndPredicate.and;


class RuleEdge {
	State src;
	State dest;

	public RuleEdge(State src, State dest){
		this.src = src;
		this.dest = dest;
	}
}

public class AfsmBenchMark {

	public final static String OUTPUT_FOLDER="out";
	
	public final static String FORBIDDEN_STATES_VIOLATION_FILE=OUTPUT_FOLDER+"/becnhmarkforbidden.txt";
	public final static String FORBIDDEN_STATES_VIOLATION_FILE_STATISTICS=OUTPUT_FOLDER+"/becnhmarkforbidden_statistics.txt";
	//public final static String FORBIDDEN_STATES_VIOLATION_REDUCED_FILE=OUTPUT_FOLDER+"/MeetingReminderValidation_reduced_forbidden.txt";
	
	public final static String DEAD_CONDITIONS_FILE=OUTPUT_FOLDER+"/becnhmarkDeadConditions.txt";
	public final static String DEAD_CONDITIONS_FILE_STATISTICS=OUTPUT_FOLDER+"/becnhmarkDeadConditions_statistics.txt";
	
	public final static String NONDETERMINISTIC_ADAPTATION_FILE=OUTPUT_FOLDER+"/becnhmarknondeterministic.txt";
	//public final static String NONDETERMINISTIC_ADAPTATION_REDUCED_FILE=OUTPUT_FOLDER+"/becnhmarkreduced_nondeterministic.txt";
	public final static String NONDETERMINISTIC_ADAPTATION_FILE_STATISTICS=OUTPUT_FOLDER+"/becnhmarknondeterministic_statistics.txt";
	
	public final static String UNREACHABLE_FILE=OUTPUT_FOLDER+"/PhoneAdapter_unreachable.txt";
	public final static String UNREACHABLE_STATISTICS=OUTPUT_FOLDER+"/PhoneAdapter_unreachable_statistics.txt";
	
	public final static String RACE_FILE=OUTPUT_FOLDER+"/becnhmarkrace.txt";
	//public final static String RACE_REDUCED_FILE=OUTPUT_FOLDER+"/becnhmarkreduced_race.txt";
	public final static String RACE_FILE_STATISTICS=OUTPUT_FOLDER+"/becnhmarkrace_statistics.txt";
	
	public final static String HAZARD_FILE=OUTPUT_FOLDER+"/becnhmarkhazard.txt";
	public final static String HAZARD_FILE_STATISTICS=OUTPUT_FOLDER+"/becnhmarkhazard_statistics.txt";
	//public final static String HAZARD_REDUCED_FILE=OUTPUT_FOLDER+"/MeetingReminderValidation_reduced_hazard.txt";
	public final static String MINIMUM_SAFE_DELAYS_FILE=OUTPUT_FOLDER+"/becnhmarkminimum_safe_delays.txt";
	
	
/*
tried the following (STATE_COUNT, RULE_COUNT, VAR_COUNT) combination:
base: (10, 50, 10)
only change state count: (20, 50, 10), (30, 50, 10)
only change rule count: (10, 60, 10), (10, 70, 10)
only change var count: (10, 50, 20), (10, 50, 30)
 */

	private final int STATE_COUNT; // state node
	private final int RULE_COUNT; // rule edge
	private final int VAR_COUNT; // var

	int MAX_VAR_PER_RULE = 5;
	int MAX_OUT_RULE_PER_STATE = 8;

	private Random random = new Random();
	private ArrayList<RuleEdge> edges;
	private PredicateVariable[] vars;
	private ArrayList[] varList;
	//private Predicate[] predicates;
	private String[] labels;

	AdaptationFSM afsm;

	/**
	 * @param state_count
	 * @param rule_count
	 * @param var_count
	 */
	public AfsmBenchMark(int state_count, int rule_count, int var_count) {
		STATE_COUNT = state_count;
		RULE_COUNT = rule_count;
		VAR_COUNT = var_count;
		edges = new ArrayList<RuleEdge>(RULE_COUNT);
		vars = new PredicateVariable[VAR_COUNT];
		varList = new ArrayList[RULE_COUNT];
		//predicates = new Predicate[RULE_COUNT];
		labels = new String[RULE_COUNT];
	}
	
	private void randomize() {
		try {
			Thread.sleep(1); //sleep to get different seed
		}catch (InterruptedException ex){
			
		}
		random.setSeed(System.nanoTime());
	}
	
	public int getRandomInt(int i){
		randomize();
		return random.nextInt(i);
	}

	public boolean getRandomBoolean(){
		int i;
		randomize();
		i = random.nextInt(10);
		if(i <= 4)
			return true;
		else
			return false;
	}

	public boolean isDuplicatedEdge(RuleEdge e){
		boolean found = false;
		for(int i = 0; i < edges.size(); i++){
			State src = edges.get(i).src;
			State dest = edges.get(i).dest;
			if(src.equals(e.src) && dest.equals(e.dest)){
				found = true;
				break;
			}
		}
		return found;
	}
	public boolean isDuplicatedVar(ArrayList<PredicateVariable> list, PredicateVariable v){
		boolean found = false;
		for(int i = 0; i < list.size(); i++){
			PredicateVariable var = list.get(i);
			if(var.equals(v)){
				found = true;
				break;
			}
		}
		return found;
	}
	public boolean sanityGraphCheck(){
		boolean isValid = false;
		if((RULE_COUNT >= STATE_COUNT - 1) &&
				(RULE_COUNT <= STATE_COUNT * (STATE_COUNT - 1)) &&
				(VAR_COUNT >= MAX_VAR_PER_RULE) &&
				(STATE_COUNT > 0) &&
				(RULE_COUNT > 0) &&
				(VAR_COUNT > 0) &&
				(MAX_OUT_RULE_PER_STATE > 0) &&
				(MAX_OUT_RULE_PER_STATE < RULE_COUNT) &&
				(STATE_COUNT * MAX_OUT_RULE_PER_STATE >= RULE_COUNT ))
			isValid = true;

		return isValid;
	}

	public AdaptationFSM generateAfsm(){
		afsm = new AdaptationFSM();
		State nodes[] = new State[STATE_COUNT];
		int idx, priority;
		int[] outDegree = new int[STATE_COUNT];

		// create states
		for(int i = 0; i <= STATE_COUNT - 1; i++){
			boolean initial = (i == 0);
			nodes[i] = new State("S" + i);
			if (initial) {
				nodes[i].setStartState(initial);
			}
			//nodes[i] = afsm.state("S" + i, initial, false);
			afsm.addState(nodes[i]);
			if (initial) {
				afsm.setInitialState(nodes[i]);
			}
			outDegree[i] = 0;
		}
		System.out.println("Creat states - DONE");

		//create edges
		// 1. connect all node in sequence to satisfy the graph connectivity
		// 2. randomly connected the nodes using the left edges
		System.out.println("Creating edges...");
		for(int i = 0; i <= STATE_COUNT - 2; i++){
			edges.add(new RuleEdge(nodes[i], nodes[i + 1]));
			outDegree[i]++;
		}
		// randomly determine the out degree of each node
		int leftEdgeCount = RULE_COUNT - (STATE_COUNT - 1);
		idx = 0;
		while(leftEdgeCount > 0){
			System.out.println("leftEdgeCount=" + leftEdgeCount);
			//if(getRandomBoolean() == true){
				int stateIdx = getRandomInt(STATE_COUNT);
				if(outDegree[stateIdx] < MAX_OUT_RULE_PER_STATE){
					outDegree[stateIdx]++;
					leftEdgeCount--;
				}
			//}
		}

		for(int i = 0; i < STATE_COUNT; i++){
			int degree;
			if(i != STATE_COUNT - 1)
				degree = outDegree[i] - 1;
			else
				degree = outDegree[i]; // the last state didn't connect to others previously
			while(degree > 0){
		    	int j = getRandomInt(STATE_COUNT);
		    	if(i == j) //no self-cycle
		        	continue;
		    	RuleEdge e = new RuleEdge(nodes[i], nodes[j]);
		    	if(isDuplicatedEdge(e)){ // no multiple edges to the same destination
		    		//System.out.println("duplicated edge");
		    		continue;
		    	}
		    	edges.add(e);
		    	System.out.println("ADD EDGE: i=" + i + ", j=" + j);
		    	degree--;
			}
	    }
		System.out.println("Creat edges - DONE");
		// Contexts
		ContextVariable time = new ContextVariable("Time", 1);
		ContextVariable bt = new ContextVariable("Bluetooth", 60000);
		ContextVariable gps = new ContextVariable("GPS", 1000);
		
		afsm.addContextVariable(time);
		afsm.addContextVariable(bt);
		afsm.addContextVariable(gps);

		//create variables: randomly assign the context var
		System.out.println("Creating variables...");
		for(int i = 0; i < VAR_COUNT; i++){
			switch(getRandomInt(3)){
			case 0:
				vars[i] = afsm.createVariable("Vt" + i, gps);
				break;
			case 1:
				vars[i] = afsm.createVariable("Vbt" + i, bt);
				break;
			case 2:
				vars[i] = afsm.createVariable("Vgps" + i, time);
				break;
			default:
			}
		}

		int varsForPredicate[] = new int[RULE_COUNT];
		//randomly select variables
		for(int i = 0; i < RULE_COUNT; i++){
			varList[i] = new ArrayList<PredicateVariable>();
			varsForPredicate[i] = 0;
		}
		for(int i = 0; i < VAR_COUNT; i++){ // guarantee to cover all variables
			varList[i % RULE_COUNT].add(vars[i]); // get mod because #var may be greater than the #rule
			varsForPredicate[i % RULE_COUNT]++;
		}

		for(int i = 0; i < RULE_COUNT; i++){ // randomly determine how many vars in a rule
			if(varsForPredicate[i] < MAX_VAR_PER_RULE){
				varsForPredicate[i] += getRandomInt(MAX_VAR_PER_RULE - varsForPredicate[i]) + 1; //start from 0, so add 1
			}
			// add real var
			while(varList[i].size() < varsForPredicate[i]){
				idx = getRandomInt(vars.length);
				if(isDuplicatedVar((ArrayList<PredicateVariable>)varList[i], vars[idx]))
					continue;
				varList[i].add(vars[idx]);
			}
			//System.out.println("ADD VARS to the rule - " + i);
		}
		System.out.println("Creat variables - DONE");

		//randomly assign and/or/not
		System.out.println("Creating predicates for each rule...");
		Predicate predicate = null;
		PredicateVariable v;
		int selector;
		for(int i = 0; i < RULE_COUNT; i++){ // for each rule
			idx = 0;
			String label = "";
			while(idx < varList[i].size()){ // for each var in the rule
				Predicate currentPredicate = null;
				v = (PredicateVariable)varList[i].get(idx);
				if(getRandomBoolean() == true){
					currentPredicate = not(v);
					label = "not(" + v.getName() + ")"; // for visualization purpose
				}else{
					currentPredicate = v;
					label = v.getName();
				}
				if(getRandomBoolean() == true)
					selector = 1;
				else
					selector = 0;
				switch(selector){
				case 0: // and
					if(idx == 0){
						predicate = currentPredicate;
						labels[i] = label;
						break;
					}
					else{
						predicate = and(predicate, currentPredicate);
						labels[i] = "and(" + labels[i] + ", " + label + ")";
					}
					break;
				case 1:  // or
					if(idx == 0){
						predicate = currentPredicate;
						labels[i] = label;
						break;
					}
					else{
						predicate = or(predicate, currentPredicate);
						labels[i] = "or(" + labels[i] + ", " + label + ")";
					}
					break;
				default:
				}
				idx++;
			}
			priority = getRandomInt(10); // assign random priority, from 0 ~ 9
			labels[i] = "R" + i + " = " + labels[i] + "; Pri=" + priority;
			AdaptationRule r = new AdaptationRule("R" + i, edges.get(i).dest);
			r.setCondition(predicate);
			//Rule r = afsm.rule("R" + i, predicate, edges.get(i).dest);
			r.setPriority(priority);
			edges.get(i).src.addRule(r);
			//System.out.println("ADD: predicate to the rule - " + i);
		}
		//System.out.println("Creat predicates for each rule - DONE");

		return afsm;

	}


// main routine is for testing purpose
	public static void main(String[] args) {
		long modelGen = 0;
		double[] averageTimes = new double[7];
		long t = 0;
		
		int attempts = 5;
		for (int i = 0; i < attempts; i++) {
		
			AfsmBenchMark abm = new AfsmBenchMark(15, 45, 15);
			if(abm.sanityGraphCheck() == false){
				System.out.println("sanityGraphCheck failes: state count, rule count, or var count is not appropriate!");
				return;
			}
			AdaptationFSM afsm = abm.generateAfsm();
			//abm.printAfsm(); 
			abm = null;
			System.gc();
			
			long time = -System.currentTimeMillis();
			afsm.loadInputSpaces();
			time += System.currentTimeMillis();
			modelGen += time;
			System.out.println("Input spaces evaluated in: " + time);
			
			System.out.println("Performing nondeterministic activation detection:");
			NondeterministicActivation nondeterministicActivation=new NondeterministicActivation();
			nondeterministicActivation.setAFSMUnderTest(afsm);
			t = -System.currentTimeMillis();
			nondeterministicActivation.detectFaults();
			t += System.currentTimeMillis();
			averageTimes[0] += t;
			
			//System.out.println("Performing fobiddend states violation detection:");
			//ForbiddenStatesViolation forbiddenStatesViolation=new ForbiddenStatesViolation();
			//forbiddenStatesViolation.setAFSMUnderTest(afsm);
			//t = -System.currentTimeMillis();
			//forbiddenStatesViolation.detectFaults();
			//t += System.currentTimeMillis();
			//averageTimes[1] += t;
			
			System.out.println("Performing Dead Condition detection:");
			DeadConditionsDetection deadConditionsDetection=new DeadConditionsDetection();
			deadConditionsDetection.setAFSMUnderTest(afsm);
			t = -System.currentTimeMillis();
			deadConditionsDetection.detectFaults();
			t += System.currentTimeMillis();
			averageTimes[2] += t;
			
			System.out.println("Performing Dead Condition detection:");
			UnrechableStatesDetection unrechableStatesDetection = new UnrechableStatesDetection();
			unrechableStatesDetection.setAFSMUnderTest(afsm);
			t = -System.currentTimeMillis();
			unrechableStatesDetection.detectFaults();
			t += System.currentTimeMillis();
			averageTimes[3] += t;
			
			System.out.println("Performing races and cycles detection:");
			RaceAndCycle racencycle=new RaceAndCycle();
			racencycle.setAFSMUnderTest(afsm);
			t = -System.currentTimeMillis();
			racencycle.detectFaults();
			t += System.currentTimeMillis();
			averageTimes[4] += t;
			
			//System.out.println("Performing context hazards detection:");
			//ContextHazardDetectionOptimized contextHazardDetection=new ContextHazardDetectionOptimized();
			//contextHazardDetection.setAFSMUnderTest(afsm);
			//contextHazardDetection.setOutput(false);
			//t = -System.currentTimeMillis();
			//Fault[] faults=contextHazardDetection.detectFaults();
			//t += System.currentTimeMillis();
			//averageTimes[5] += t;
			//MinimumSafeDelayGenerator minimumSafeDelaysGenerator = new MinimumSafeDelayGenerator();
			//t = -System.currentTimeMillis();
			//minimumSafeDelaysGenerator.generateDelays(faults);
			//t += System.currentTimeMillis();
			//averageTimes[6] += t;
			
			abm = null;
			System.gc();
		}
		
		modelGen = modelGen / attempts;
		for (int i = 0; i < averageTimes.length; i++) {
			averageTimes[i] = averageTimes[i] / attempts;
		}
		System.out.println("Average time to generate the model(ms): " + modelGen);
		System.out.println("Average time NondeterministicActivationAlgorithm (ms): " + averageTimes[0]);
		//System.out.println("Average time Forbidden state Violation (ms): " + averageTimes[1]);
		System.out.println("Average time DeadCondition (ms): " + averageTimes[2]);
		System.out.println("Average time Unreachable (ms): " + averageTimes[3]);
		System.out.println("Average time MetastabilityAlgorithm (ms): " + averageTimes[4]);
		//System.out.println("Average time Hazard Detection (ms): " + averageTimes[5]);
		//System.out.println("Average time Minimum Safe Delays (ms): " + averageTimes[6]);
		
	}

	public AdaptationFSM getAdaptationFiniteStateMachine() {
		//AfsmBenchMark abm = new AfsmBenchMark(STATE_COUNT, RULE_COUNT, VAR_COUNT);
		if(sanityGraphCheck() == false){
			System.out.println("sanityGraphCheck failes: state count, rule count, or var count is not appropriate!");
			return null;
		}
		generateAfsm();
		printAfsm();
		return afsm;
	}

	public void printAfsm(){
		System.out.println("\n\n\n-----------The following can be the input for Graphviz for visualization-----------\n");
		System.out.println("digraph \"AFSM\" {");
		//System.out.println("size=\"1000, 1000\"");
		System.out.println("node [shape=circle]");
		// node->node
		for(int i = 0; i < edges.size(); i++){
			State src = edges.get(i).src;
			State dest = edges.get(i).dest;

			String varStr = "";
			varStr = labels[i];
			System.out.println("\"" + src.getName() + "\"->" + "\"" + dest.getName() + "\" [label=\"" + varStr + "\"]");
		}
		System.out.println("}");
	}

	private static void perfomValidation(AdaptationFSM afsm) {

		Fault[] faults;
		
		//NondeterministicActivation
		//--------------------------------------------------------------------------------
		System.out.println("Performing nondeterministic activation detection:");
		NondeterministicActivation nondeterministicActivation=new NondeterministicActivation();
		nondeterministicActivation.setAFSMUnderTest(afsm);
		faults=nondeterministicActivation.detectFaults();
		
		if(faults.length==0)
		{
			System.out.println("SUCCESS: No nondeterministic activation found.");
		}else
		{
			System.out.println("FAULT: "+faults.length+" instances detected.");
			try {
				PrintWriter pw = new PrintWriter(NONDETERMINISTIC_ADAPTATION_FILE);
				pw.println("PhoneAdapter");
				nondeterministicActivation.printFaultsToStream(faults, pw);
				pw.close();
				System.out.println("Instances saved in "+ NONDETERMINISTIC_ADAPTATION_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}
			
		}
		System.out.println("Saving statistics.");
		try {
			PrintWriter pw = new PrintWriter(NONDETERMINISTIC_ADAPTATION_FILE_STATISTICS);
			pw.println("PhoneAdapter");
			nondeterministicActivation.printStatisticsToStream(pw);
			pw.close();
			System.out.println("Instances saved in "+ NONDETERMINISTIC_ADAPTATION_FILE_STATISTICS);
		} catch (FileNotFoundException e) {
			System.err.println("Error while saving on file.");
		}
		System.out.println("");
		
		
		
		//WE MUST SOLVE NONDETERMINISIC
		//--------------------------------------------------------------------------------
		//Nondeterminstic point must be removed first
		
		afsm.loadInputSpaces();
		//-------
		//check double check
		faults=nondeterministicActivation.detectFaults();
		if(faults.length!=0)
		{
			System.out.println("FAILURE! Remove nondeterministic points before continuing.");
			System.exit(1);
		}
		
		
		//ForbiddenStatesViolation
		//--------------------------------------------------------------------------------
		System.out.println("Performing fobiddend states violation detection:");
		ForbiddenStatesViolation forbiddenStatesViolation=new ForbiddenStatesViolation();
		forbiddenStatesViolation.setAFSMUnderTest(afsm);
		faults=forbiddenStatesViolation.detectFaults();
		
		if(faults.length==0)
		{
			System.out.println("SUCCESS: No Forbidden states violated.");
		}else
		{
			System.out.println("FAULT: "+faults.length+" instances detected.");
			
			try {
				PrintWriter pw=new PrintWriter(FORBIDDEN_STATES_VIOLATION_FILE);
				pw.println("PhoneAdapter");
				forbiddenStatesViolation.printFaultsToStream(faults, pw);
				pw.flush();
				pw.close();
				System.out.println("Instances saved in "+ FORBIDDEN_STATES_VIOLATION_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}

		}
		System.out.println("Saving statistics.");
		try {
			PrintWriter pw=new PrintWriter(FORBIDDEN_STATES_VIOLATION_FILE_STATISTICS);
			pw.println("PhoneAdapter");
			forbiddenStatesViolation.printStatisticsToStream(pw);
			pw.flush();
			pw.close();
			System.out.println("Instances saved in "+ FORBIDDEN_STATES_VIOLATION_FILE_STATISTICS);
		} catch (FileNotFoundException e) {
			System.err.println("Error while saving on file.");
		}
		System.out.println("");
		
		
		//Dead Conditions
		//--------------------------------------------------------------------------------
		System.out.println("Performing Dead Condition detection:");
		DeadConditionsDetection deadConditionsDetection=new DeadConditionsDetection();
		deadConditionsDetection.setAFSMUnderTest(afsm);
		faults=deadConditionsDetection.detectFaults();
		
		if(faults.length==0)
		{
			System.out.println("SUCCESS: No Dead Conditions.");
		}else
		{
			System.out.println("FAULT: "+faults.length+" instances detected.");
			
			try {
				PrintWriter pw=new PrintWriter(DEAD_CONDITIONS_FILE);
				pw.println("PhoneAdapter");
				deadConditionsDetection.printFaultsToStream(faults, pw);
				pw.close();
				System.out.println("Instances saved in " + DEAD_CONDITIONS_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}

		}
		System.out.println("Saving statistics.");
		try {
			PrintWriter pw=new PrintWriter(DEAD_CONDITIONS_FILE_STATISTICS);
			pw.println("PhoneAdapter");
			deadConditionsDetection.printStatisticsToStream(pw);
			pw.close();
			System.out.println("Instances saved in "+ DEAD_CONDITIONS_FILE_STATISTICS);
		} catch (FileNotFoundException e) {
			System.err.println("Error while saving on file.");
		}
		System.out.println("");
		
		
		//Races and cycles
		//--------------------------------------------------------------------------------
		System.out.println("Performing races and cycles detection:");
		RaceAndCycle racencycle=new RaceAndCycle();
		racencycle.setAFSMUnderTest(afsm);
		faults=racencycle.detectFaults();
		
		if(faults.length==0)
		{
			System.out.println("SUCCESS: No races od cycles found.");
		}else
		{
			System.out.println("FAULT: "+faults.length+" instances detected.");
			try {
				PrintWriter pw = new PrintWriter(RACE_FILE);
				pw.println("PhoneAdapter");
				racencycle.printFaultsToStream(faults, pw);
				pw.flush();
				pw.close();
				System.out.println("Instances saved in "+ RACE_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}
			
		}
		System.out.println("Saving statistics.");
		try {
			PrintWriter pw = new PrintWriter(RACE_FILE_STATISTICS);
			pw.println("PhoneAdapter");
			racencycle.printStatisticsToStream(pw);
			pw.flush();
			pw.close();
			System.out.println("Instances saved in "+ RACE_FILE_STATISTICS);
		} catch (FileNotFoundException e) {
			System.err.println("Error while saving on file.");
		}
		System.out.println("");
		
		
		System.out.println("Performing context hazards detection:");
		ContextHazardDetectionOptimized contextHazardDetection=new ContextHazardDetectionOptimized();
		contextHazardDetection.setAFSMUnderTest(afsm);
		contextHazardDetection.setOutput(false);
		faults=contextHazardDetection.detectFaults();
		
		if(faults.length==0)
		{
			System.out.println("SUCCESS: No context hazards.");
		}else
		{
			System.out.println("FAULT: "+faults.length+" instances detected.");
			
			try {
				PrintWriter pw=new PrintWriter(HAZARD_FILE);
				pw.println("PhoneAdapter");
				contextHazardDetection.printFaultsToStream(faults, pw);
				pw.flush();
				pw.close();
				System.out.println("Instances saved in "+ HAZARD_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}
			
			//MinimumSafeDelays
			//*******************
			MinimumSafeDelayGenerator minimumSafeDelaysGenerator = new MinimumSafeDelayGenerator();
			minimumSafeDelaysGenerator.generateDelays(faults);
			try {
				PrintWriter pw=new PrintWriter( MINIMUM_SAFE_DELAYS_FILE);
				minimumSafeDelaysGenerator.printToStream(pw);
				pw.flush();
				pw.close();
				System.out.println("Instances saved in "+ MINIMUM_SAFE_DELAYS_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}
			
		}
		System.out.println("Saving statistics.");
		try {
			PrintWriter pw=new PrintWriter(HAZARD_FILE_STATISTICS);
			pw.println("PhoneAdapter");
			contextHazardDetection.printStatisticsToStream(pw);
			pw.flush();
			pw.close();
			System.out.println("Instances saved in "+ HAZARD_FILE_STATISTICS);
		} catch (FileNotFoundException e) {
			System.err.println("Error while saving on file.");
		}
		System.out.println("");
		
	}
}
