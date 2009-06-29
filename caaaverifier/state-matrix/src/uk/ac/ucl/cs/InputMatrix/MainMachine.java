package uk.ac.ucl.cs.InputMatrix;

import java.util.*;
import java.io.*;
import uk.ac.ucl.cs.InputMatrix.myparser.parser.*;
import uk.ac.ucl.cs.InputMatrix.myparser.lexer.*;
import uk.ac.ucl.cs.InputMatrix.myparser.node.*;

public class MainMachine {
	
	public ArrayList<State> state_list = null;
	public ArrayList trans_list = null;
	public Hashtable trigger_table = null;
	public Hashtable priority_table = null;
	public Hashtable context_types_table = null;
	public Hashtable context_variables_table = null;
	public Hashtable predicate_abbr_table = null;
	public Hashtable predicates_for_state_table = null;
	
	public MainMachine()
	{
		state_list = new ArrayList<State>();
		trans_list = new ArrayList();
		trigger_table = new Hashtable();
		priority_table = new Hashtable();
		context_types_table = new Hashtable();
		context_variables_table = new Hashtable();
		predicate_abbr_table = new Hashtable();
		predicates_for_state_table = new Hashtable();
		state_list.clear();
		trans_list.clear();
	}
	public static void main(String[] args)
	{
		if (args.length < 1)
		{
			System.out.println("Usage: java uk.ac.ucl.cs.InputMatrix.MainMachine <spec>");
			return;
		}

		try
		{
			// create lexer
			Lexer lexer = new Lexer (new PushbackReader(new BufferedReader(new FileReader(args[0])), 1024));

			// parser program
			Parser parser = new Parser(lexer);

			Start ast = parser.parse();

			// check program semantics
			MainMachine machine = new MainMachine();
			Interpreter interpreter = new Interpreter(machine.state_list, machine.trans_list, 
					machine.trigger_table, machine.priority_table, machine.context_types_table,
					machine.context_variables_table, machine.predicate_abbr_table, machine.predicates_for_state_table);
			
			ast.apply(interpreter);
			
			/*
			// testing:
			// the following statements are used to show the above variables
			if(machine.state_list == null)
				System.out.println("state list is null!!");
			System.out.println("\nList all states:");
			for(int i = 0; i < machine.state_list.size(); i++)
			{
				State state = (State)machine.state_list.get(i);
				System.out.print(state.getName() + "(start=" + state.isStartState() + ", forbidden=" + state.isForbiddenState() + "), ");
			}
			
			System.out.println("\n\nList all transitions:");
			for(int i = 0; i < machine.trans_list.size(); i++)
			{
				Transition trans = (Transition)machine.trans_list.get(i);
				System.out.println("(" + trans.getSrc() + "," + trans.getInput() + ")->" + trans.getDest());
			}			
			System.out.println("");
			System.out.println("");
			//System.out.println("\n\nList all triggers:");
			//...
			*/
			
			// old code generation
			//machine.generateCodeFromFSM();
			
			/* Get instance of a AdaptationFSM*/
			AdaptationFSM afsm = interpreter.getAdaptationFSM();
			
			/*
			 * Add fault detection method here using the instance afsm as parameter
			 */
			
			
			
			
		

		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
	public void generateCodeFromFSM(){
		Enumeration e;
		Iterator itr;
		
		//context types, e.g.  ContextVariable t=new ContextVariable("Time");afsm.addContextVariable(t);
		System.out.println("//----------------------context types---------------------------");
		e = context_types_table.keys();
		while(e.hasMoreElements()){
			String mykey = (String)e.nextElement();
			String myvalue = (String)context_types_table.get(mykey);
			
			System.out.println("ContextVariable " + mykey + " = new ContextVariable(\"" + myvalue + "\");");
			System.out.println("afsm.addContextVariable(" + mykey + ");");
		}
		
		//context variable definition
		System.out.println("//----------------------context variable definition---------------------------");
		System.out.println("try{");
		e = context_variables_table.keys();
		while(e.hasMoreElements()){
			String mykey = (String)e.nextElement();
			String myvalue = (String)context_variables_table.get(mykey);

			System.out.println("afsm.createVariable(\"" + mykey + "\"," + myvalue + ");");
		}
		System.out.println("} catch (Exception e) {");
		System.out.println("System.err.println(\"Error in variables definition.\");");
		System.out.println("System.exit(1);");
		System.out.println("}");
		
		//states definition
		System.out.println("//----------------------states definition---------------------------");
		for(int i = 0; i < state_list.size(); i++){
			State s = (State)state_list.get(i);
			String str = s.getName();
			System.out.println("State s" + str + "=new State(\"S" + str + "\");");
			if(s.isForbiddenState())
				System.out.println("s" + str + ".setForbiddenState(true);");
		}
		
		//add states to the afsm
		System.out.println("//----------------------add states to the afsm---------------------------");
		System.out.println("try{");
		for(int i = 0; i < state_list.size(); i++){
			State s = (State)state_list.get(i);
			String str = s.getName();
			System.out.println("afsm.addState(s" + str + ");");			
		}
		System.out.println("} catch (Exception e) {");
		System.out.println("System.err.println(\"Error when add states to afsm definition.\");");
		System.out.println("System.exit(1);");
		System.out.println("}");
		
		//predicates & rules definition
		System.out.println("//----------------------predicates & rules definition---------------------------");
		System.out.println("//triggers:");
		//get unique triggers T
		e = trigger_table.keys();
		Set trigger_set = new HashSet();
		while(e.hasMoreElements()){
			String state_name = (String)e.nextElement();
			trigger_set.addAll((ArrayList)trigger_table.get(state_name));
		}
		itr = trigger_set.iterator();
		while(itr.hasNext()){
			String trigger_name = (String)itr.next();
			String var_name = "rule_" + trigger_name;
			var_name = var_name.toLowerCase();
			System.out.println("ExternalRule " + var_name + "= new ExternalRule(\"" + trigger_name + "\");");
			String predicate_abbr = (String)predicate_abbr_table.get(trigger_name);
			System.out.println(var_name + ".setPredicate(" + predicate_abbr + ");");
			String priority_setting = (String)priority_table.get(trigger_name);
			if(priority_setting != null){
				System.out.println(var_name + ".setPriority(" + priority_setting + ");");
			}
		}
		
		System.out.println("//adaptations:");
		for(int i = 0; i < trans_list.size(); i++){
			Transition t = (Transition)trans_list.get(i);
			String src_state, input, dest_state, var_name;
			src_state = t.getSrc();
			input = t.getInput();
			dest_state = t.getDest();
			var_name = "rule_" + input + "_s" + src_state;
			var_name = var_name.toLowerCase();
			System.out.println("AdaptationRule " + var_name + " = new AdaptationRule(\"" + input + "\"" + ", s" + dest_state + ");");
		}
		e = predicate_abbr_table.keys();
		while(e.hasMoreElements()){
			String mykey = (String)e.nextElement();
			String myvalue = (String)predicate_abbr_table.get(mykey);
			if(myvalue.startsWith("new AndPredicate") && isAdaptation(mykey)){
				System.out.println("AndPredicate " + mykey.toLowerCase() + " = " + myvalue + ";");
			}else if(myvalue.startsWith("new OrPredicate") && isAdaptation(mykey)){
				System.out.println("OrPredicate " + mykey.toLowerCase() + " = " + myvalue + ";");
			}else if(myvalue.startsWith("new NotPredicate") && isAdaptation(mykey)){
				System.out.println("NotPredicate " + mykey.toLowerCase() + " = " + myvalue + ";");
			}
		}
		for(int i = 0; i < trans_list.size(); i++){
			Transition t = (Transition)trans_list.get(i);
			String src_state, input, dest_state, var_name;
			src_state = t.getSrc();
			input = t.getInput();
			dest_state = t.getDest();
			var_name = "rule_" + input + "_s" + src_state;
			var_name = var_name.toLowerCase();
			System.out.println(var_name + ".setPredicate(" + input.toLowerCase() + ");");
			String priority_setting = (String)priority_table.get(input);
			if(priority_setting != null){
				System.out.println(var_name + ".setPriority(" + priority_setting + ");");
			}
		}
		
		//add rules into states
		System.out.println("//----------------------add rules into states---------------------------");
		//loop on state, get name of T from trigger_table, get name of adaptation from PredicatesForState table
		for(int i = 0; i < state_list.size(); i++){
			State s = (State)state_list.get(i);
			String state_name = s.getName();
			ArrayList trigger_list = (ArrayList)trigger_table.get(state_name);
			ArrayList predicate_list = (ArrayList)predicates_for_state_table.get(state_name);
			for(int j = 0; trigger_list != null && j < trigger_list.size(); j++){
				String trigger_name = (String)trigger_list.get(j);
				System.out.println("s" + state_name + ".addRule(rule_" + trigger_name.toLowerCase() + ");");
			}
			for(int j = 0; predicate_list != null && j < predicate_list.size(); j++){
				String adaptation_name = (String)predicate_list.get(j);
				System.out.println("s" + state_name + ".addRule(rule_" + adaptation_name.toLowerCase() + "_s" + state_name + ");");
			}
			
			
			System.out.println("");
		}
	}
	public boolean isAdaptation(String mykey){
		boolean isFound = false;	
		for(int i = 0; i < trans_list.size(); i++){
			Transition t = (Transition)trans_list.get(i);
			if(mykey.equals(t.getInput()))
			{
				isFound = true;
				break;
			}
		}
		return isFound;
	
	}
}
