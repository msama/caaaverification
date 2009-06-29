package benchmark;

import static uk.ac.ucl.cs.afsm.common.Assignment.negate;
import static uk.ac.ucl.cs.afsm.common.Assignment.satisfy;
import static uk.ac.ucl.cs.afsm.common.Assignment.*;
import static uk.ac.ucl.cs.afsm.common.predicate.Operator.*;

import java.util.ArrayList;
import java.util.Random;

import timerrific.TimerrificAfsm;
import uk.ac.ucl.cs.afsm.common.AdaptationFiniteStateMachine;
import uk.ac.ucl.cs.afsm.common.AfsmBuilder;
import uk.ac.ucl.cs.afsm.common.Context;
import uk.ac.ucl.cs.afsm.common.Rule;
import uk.ac.ucl.cs.afsm.common.State;
import uk.ac.ucl.cs.afsm.common.predicate.Predicate;
import uk.ac.ucl.cs.afsm.common.predicate.Variable;


class RuleEdge {
	State src;
	State dest;

	public RuleEdge(State src, State dest){
		this.src = src;
		this.dest = dest;
	}
}

public class AfsmBenchMark implements AfsmBuilder {

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
	private Variable[] vars;
	private ArrayList[] varList;
	private Predicate[] predicates;
	private String[] labels;

	AdaptationFiniteStateMachine afsm;

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
		vars = new Variable[VAR_COUNT];
		varList = new ArrayList[RULE_COUNT];
		predicates = new Predicate[RULE_COUNT];
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
	public boolean isDuplicatedVar(ArrayList<Variable> list, Variable v){
		boolean found = false;
		for(int i = 0; i < list.size(); i++){
			Variable var = list.get(i);
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

	public AdaptationFiniteStateMachine generateAfsm(){
		afsm = new AdaptationFiniteStateMachine();
		State nodes[] = new State[STATE_COUNT];
		int idx, priority;
		int[] outDegree = new int[STATE_COUNT];

		// create states
		for(int i = 0; i <= STATE_COUNT - 1; i++){
			boolean initial = (i == 0);
			nodes[i] = afsm.state("S" + i, initial, false);
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
		Context time = new Context("Time");
		Context bt = new Context("Bluetooth");
		Context gps = new Context("GPS");

		//create variables: randomly assign the context var
		System.out.println("Creating variables...");
		for(int i = 0; i < VAR_COUNT; i++){
			switch(getRandomInt(3)){
			case 0:
				vars[i] = afsm.variable("Vt" + i, gps);
				break;
			case 1:
				vars[i] = afsm.variable("Vbt" + i, bt);
				break;
			case 2:
				vars[i] = afsm.variable("Vgps" + i, time);
				break;
			default:
			}
		}

		int varsForPredicate[] = new int[RULE_COUNT];
		//randomly select variables
		for(int i = 0; i < RULE_COUNT; i++){
			varList[i] = new ArrayList<Variable>();
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
				if(isDuplicatedVar((ArrayList<Variable>)varList[i], vars[idx]))
					continue;
				varList[i].add(vars[idx]);
			}
			//System.out.println("ADD VARS to the rule - " + i);
		}
		System.out.println("Creat variables - DONE");

		//randomly assign and/or/not
		System.out.println("Creating predicates for each rule...");
		Predicate predicate = null;
		Variable v;
		int selector;
		for(int i = 0; i < RULE_COUNT; i++){ // for each rule
			idx = 0;
			String label = "";
			while(idx < varList[i].size()){ // for each var in the rule
				Predicate currentPredicate = null;
				v = (Variable)varList[i].get(idx);
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
			Rule r = afsm.rule("R" + i, predicate, edges.get(i).dest);
			//r.setPriority(priority);
			r.setPriority(priority);
			edges.get(i).src.outGoingRules.add(r);
			//System.out.println("ADD: predicate to the rule - " + i);
		}
		//System.out.println("Creat predicates for each rule - DONE");

		return afsm;

	}


// main routine is for testing purpose
	public static void main(String[] args) {
		AfsmBenchMark abm = new AfsmBenchMark(10, 50, 10);
		if(abm.sanityGraphCheck() == false){
			System.out.println("sanityGraphCheck failes: state count, rule count, or var count is not appropriate!");
			return;
		}
		abm.generateAfsm();
		abm.printAfsm();
	}

	@Override
	public AdaptationFiniteStateMachine getAdaptationFiniteStateMachine() {
		//AfsmBenchMark abm = new AfsmBenchMark(STATE_COUNT, RULE_COUNT, VAR_COUNT);
		if(sanityGraphCheck() == false){
			System.out.println("sanityGraphCheck failes: state count, rule count, or var count is not appropriate!");
			return null;
		}
		generateAfsm();
		//printAfsm();
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

}
