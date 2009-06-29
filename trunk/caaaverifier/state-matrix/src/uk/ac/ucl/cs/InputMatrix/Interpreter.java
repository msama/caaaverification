package uk.ac.ucl.cs.InputMatrix;

import uk.ac.ucl.cs.InputMatrix.myparser.node.*;
import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;
import java.util.*;

public class Interpreter extends DepthFirstAdapter {

	public ArrayList state_list;
	public ArrayList trans_list;
	public Hashtable trigger_table;
	public Hashtable priority_table;
	public Hashtable context_types_table;
	public Hashtable context_variables_table;
	public Hashtable predicate_abbr_table;
	public Hashtable predicates_for_state_table;
	
	public String current_node;

	public String factor_str;
	public String src_state, input, dest_state;
	//public Hashtable state_table;
	public Hashtable input_table;
	public ArrayList trigger_list;
	public ArrayList predicate_name_list;

	public Hashtable logic_exp_table;
	
	// create instance/object table for each element
	
	/*
	 * add(type_name, new ContextVariable(type_name))
	 */
	public Hashtable<String,ContextVariable> obj_ctx_type_table = new Hashtable<String,ContextVariable>();
	
	/*
	 * add(state_name, new State(state_name))
	 */
	public Hashtable<String,State> obj_state_table = new Hashtable<String,State>();
	
	/*
	 * predicate abbr
	 */
	// obj_logic_exp_table is used to store intermediate logic objects in AST
	// associate each node with BooleanPredicate. when creating, pass child's value to its parent
	public Hashtable<Object,Predicate> obj_logic_exp_table = new Hashtable<Object,Predicate>();
	
	// retrieve relevant value from obj_logic_exp_table and build table matching with PredicateAbbr section
	public Hashtable<String,Predicate> obj_predicate_abbr_table = new Hashtable<String,Predicate>();

	public ArrayList<ArrayList<Predicate>> obj_constraint_pair_list = new ArrayList<ArrayList<Predicate>>(); 
	/*
	 * rule table: rule name, rule obj
	 */
	
	public Hashtable<String, Rule> obj_rule_table = new Hashtable<String, Rule>();
	
	AdaptationFSM afsm;
	
	public Interpreter(ArrayList state_list, ArrayList trans_list, Hashtable trigger_table, 
			Hashtable priority_table, Hashtable context_types_table, Hashtable context_variables_table, 
			Hashtable predicate_abbr_table, Hashtable predicates_for_state_table)
	{
		this.state_list = state_list;
		this.trans_list = trans_list;
		this.trigger_table = trigger_table;
		this.priority_table = priority_table;
		this.context_types_table = context_types_table;
		this.context_variables_table = context_variables_table;
		this.predicate_abbr_table = predicate_abbr_table;
		this.predicates_for_state_table = predicates_for_state_table;
		
		current_node = "";
		factor_str = "";
		//state_table = new Hashtable();
		input_table = new Hashtable();
		logic_exp_table = new Hashtable(); // used to store (AST node, predicate string), 
											// also store the calculated value to the prarent of current node 
	}
	
	public AdaptationFSM getAdaptationFSM(){
		return afsm;
	}
	
	public void outAFsmName(AFsmName node){
		TName name = node.getName();
		System.out.println(name.getText());
		
		TString str = node.getString();
		System.out.println(str.getText());
		
		// new instance of FSM
		afsm=new AdaptationFSM();
		System.out.println("A-FSM created.");
		
		current_node = "fsm_state";
	}
	public void outAFsmState(AFsmState node)
    {
		//before leaving State section, add state obj to fsm
		Enumeration e;
		try{
			e = obj_state_table.keys();
			while(e.hasMoreElements()){
				String state_name = (String)e.nextElement();
				State s = (State)obj_state_table.get(state_name);
				afsm.addState(s);
				System.out.println("afsm.addState(" + s.getName() + ");");			
			}
		}catch (Exception ex) {
			System.err.println("Error in states definition.");
			System.exit(1);
		}
		
		current_node = "fsm_input_alpha";
    }

	public void outAStateScope(AStateScope node)
    {
		if(current_node.equals("fsm_state"))
		{
			TScopeChar sc = node.getScopeChar();
			AddScope2StateList(sc.getText());
			System.out.println("fsm_state node: " + sc.getText());
		}
    }
	public void AddScope2StateList(String scope_str)
	{
		int idx, first_int, last_int, i;
		String state_str;
		char first_char, last_char;
		String first_str, last_str;
		
         	scope_str = scope_str.trim();
         	idx = scope_str.indexOf("-");
         	if(idx < 0)  //not range
         		return;
         	else // range
         	{
         		first_str = scope_str.substring(0,idx);
         		last_str = scope_str.substring(idx + 1);
         		
         		if(first_str.length() == 1 && last_str.length() == 1) // 0-9 or a-z or A-Z
         		{
         			first_char = first_str.charAt(0);
         			last_char = last_str.charAt(0);
         			if(first_char >= last_char)
         			{
         				System.out.println("Define state " + scope_str + " Error!");
					System.exit(0);
         			}
	         		if(first_char == ' ' || first_char == '-' || last_char == ' ' || last_char == '-')
	         		{
					System.out.println("AddScope2List: Syntax Error"); 
					System.exit(0);       			
	         		}
	         		for(i = first_char; i <= last_char; i++)
	         		{
	         			state_str = String.valueOf((char)i);
	         			if(obj_state_table.containsKey(state_str))
	         			{
						System.out.println("state " + state_str + " is redefined!");
						System.exit(0);
	         			}
	         			else
	         			{
	         				State state = new State(state_str);
	         				state_list.add(state);  
	         				
	         				//add state name and state obj to obj_state_table
	         				obj_state_table.put(state_str, state);  
	         					         			         				
	         				System.out.println("add state: " + state_str);   				
	         			}
	         		}
	         	}
	         	else //0-1000....
	         	{
	         		try
	         		{
	         			first_int = Integer.parseInt(first_str);
	         			last_int = Integer.parseInt(last_str);
	         			if(first_int >= last_int)
	         			{
						System.out.println("Define state " + scope_str + " Error!");
						System.exit(0);
		         		}	
	         			for(i = first_int; i <= last_int; i++)
	         			{
		         			state_str = Integer.toString(i);
		         			if(obj_state_table.containsKey(state_str))
		         			{
							System.out.println("state " + state_str + " is redefined!");
							System.exit(0);
		         			}
		         			else
		         			{
		         				State state = new State(state_str);
		         				state_list.add(state);  
		         				//add state name and state obj to obj_state_table
		         				obj_state_table.put(state_str, state);  
		         				System.out.println("add state: " + state_str);   				
		         			}
	         			}
	         		}
	         		catch(Exception e)
	         		{
	         			System.out.println("Define state " + scope_str + "Error!");
	         			System.exit(0);
	         		}	         		
	         	}
         	}		
	}
	
	public void outAFsmInputAlpha(AFsmInputAlpha node)
    {
		current_node = "fsm_start";
    }
	
	public void outAInputScope(AInputScope node)
	{
		//System.out.println("in outAInputScope, current_node=" + current_node);
		if(current_node.equals("fsm_input_alpha"))
		{
			TScopeChar sc = node.getScopeChar();
			int idx;
			String scope_str = sc.getText();
			String input_str;
			char first_char, last_char;
	         	scope_str = scope_str.trim();
	         	idx = scope_str.indexOf("-");
	         	if(idx < 0)  //not range
	         		return;
	         	else // range
	         	{
	         		first_char = scope_str.charAt(0);
	         		last_char = scope_str.charAt(2);
	         		if(first_char == ' ' || first_char == '-' || last_char == ' ' || last_char == '-')
	         		{
					System.out.println("outAInputScope: Syntax Error"); 
					System.exit(0);       			
	         		}
         			for(int i = first_char; i <= last_char; i++)
         			{
         				input_str = String.valueOf((char)i);
	         			if(input_table.containsKey(input_str))
	         			{
						System.out.println("fsm_input_alpha: " + input_str + " is redefined!");
						System.exit(0);
	         			}
	         			else
	         			{    
	         				input_table.put(input_str, input_str);  
	         				System.out.println("add input: " + input_str);   				
         				}
         			}
         		}
		}
	}	
	
	public void outAFsmStart(AFsmStart node)
    {
		// before leaving FsmStart section, set start state
		
		
		current_node = "fsm_forbidden";
    }
	
	public void outAFsmForbidden(AFsmForbidden node)
    {
		current_node = "fsm_transition";
    }
	
	public void outAFsmTransition(AFsmTransition node)
    {        		
		current_node = "fsm_trigger";
    }
	
	//new transition obj, also collect (state, predicate list (input))
	public void outAFsmOneTransition(AFsmOneTransition node) 
    {
		Transition trans = new Transition(src_state, input, dest_state);
		trans_list.add(trans);
		System.out.println("add transition: (" + src_state + ", " + input + ")->" + dest_state);
		
		// collect (state name, predicate name list (input))
		ArrayList predicates_name_list = (ArrayList)predicates_for_state_table.get(src_state);
		if(predicates_name_list == null){
			predicates_name_list = new ArrayList();
			predicates_name_list.add(input);
			predicates_for_state_table.put(src_state, predicates_name_list);
		}else{
			if(!predicates_name_list.contains(input))
				predicates_name_list.add(input);
		}
			
    }
	
	public void outAMyStateAlpha1(AMyStateAlpha1 node)
    {
		src_state = factor_str;
		if(!obj_state_table.containsKey(src_state))
     	{
		System.out.println("in transition function state " + src_state + " is not defined!");
		System.exit(0);
     	}
		System.out.println("src_state: " + src_state);
    }
	
	public void outAMyTransitionInputAlpha(AMyTransitionInputAlpha node)
    {
		input = factor_str;
		if(!input_table.containsKey(input)){
			System.out.println("input " + input + " is not defined!");
			System.exit(-1);
		}
			
		System.out.println("input on transition: " + input);
    }
	
	public void outAMyStateAlpha2(AMyStateAlpha2 node)
    {
		dest_state = factor_str;
		if(!obj_state_table.containsKey(dest_state))
     	{
		System.out.println("in transition function state " + dest_state + " is not defined!");
		System.exit(0);
     	}
		System.out.println("dest_state: " + dest_state);
    }
	
	public void outAStatetriggerMyTrigger(AStatetriggerMyTrigger node)
    {
        System.out.println("---"+node.getState().toString().trim()); //add to table
        String state_name = node.getState().toString().trim();
        
        if(!obj_state_table.containsKey(state_name)){
        	System.out.println("state " + state_name + " is not defined!");
        	System.exit(-1);
        }
        trigger_table.put(state_name, trigger_list);
    }
	
	public void inASingleTriggers(ASingleTriggers node)
    {
		System.out.println("trigger--node.getAtrigger()");//new list
		trigger_list = new ArrayList();
    }
	
	public void outAAtrigger(AAtrigger node)
    {
		// collect T and add to list
		System.out.println("trigger: "+node.getFactor().toString().trim());
		trigger_list.add(node.getFactor().toString().trim());
    }
	
	public void outAFsmTrigger(AFsmTrigger node)
    {
		current_node = "fsm_priority";
    }
	
	public void outATriggerpriorityMyPriority(ATriggerpriorityMyPriority node)
    {
        System.out.println("trigger=" + node.getTrigger().toString().trim() + ", priority=" + node.getPrioritySetting().toString().trim());
        priority_table.put(node.getTrigger().toString().trim(), node.getPrioritySetting().toString().trim());
    }
	
	public void outAFsmPriority(AFsmPriority node)
    {
		current_node = "fsm_context_types";
    }
	
	public void outAFsmContextTypes(AFsmContextTypes node)
	{
		//before leaving ContextTypes section, add types to fsm
		Enumeration<String> e;
		e = obj_ctx_type_table.keys();
		while(e.hasMoreElements()){
			String mykey = (String)e.nextElement();
			ContextVariable myvalue = (ContextVariable)obj_ctx_type_table.get(mykey);
			afsm.addContextVariable(myvalue);
		}
		
		current_node = "fsm_context_variables";
	}
	
	public void outAMyType(AMyType node)
    {
		PTypeAbbr pta1 = node.getShorttypename();
		PTypeName pta2 = node.getFulltypename();
		int refresh_rate = 0;
		try{
			refresh_rate = Integer.parseInt(node.getRefreshRate().toString().trim());
		}catch(Exception e){
			System.out.println("refresh_rate should be integer");
			System.exit(-1);
		}
		context_types_table.put(pta1.toString().trim(), pta2.toString().trim());
		
		// store ctx var name and its obj pair, with refresh rate
		obj_ctx_type_table.put(pta1.toString().trim(), new ContextVariable(pta2.toString().trim(), refresh_rate));
		
		System.out.println(pta1.toString().trim() + "--" + pta2.toString().trim() + "--" + refresh_rate);		
    }
	
	public void outAFsmContextVariables(AFsmContextVariables node){
		// before leaving ContextVariables section, create var for fsm: afsm.createVariable("A_bt",bt);
		Enumeration<String> e;
		try {
			e = context_variables_table.keys();
			while(e.hasMoreElements()){
				String var_str = (String)e.nextElement();
				String type_str = (String)context_variables_table.get(var_str); //variable name
				ContextVariable ctx_type = (ContextVariable)obj_ctx_type_table.get(type_str); //variable type
				afsm.createVariable(var_str, ctx_type);
				
				System.out.println("afsm.createVariable(\"" + var_str + "\"," + ctx_type + ");");
			}
		}catch (Exception ex) {
			System.err.println("Error in variables definition.");
			System.exit(1);
		}
		
		
		current_node = "fsm_predicate_abbr";
	}
	
	public void outAFsmPredicateAbbr(AFsmPredicateAbbr node){
		//before leaving PredicateAbbr section, assign predicate to fsm
		/* ***** and remember to clear the relevant table, which may be used in the later grammar */
		
		Enumeration e;
		Iterator itr;
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
			ExternalRule er = new ExternalRule(trigger_name);
			System.out.println("trigger_name = " + trigger_name);
			Predicate bp = (Predicate)obj_predicate_abbr_table.get(trigger_name);
			if(bp == null){
				System.out.println("no predicate abbr defined for trigger: " + trigger_name);
				System.exit(-1);
			}
			System.out.println("bp = " + bp.toString());
			er.setCondition(bp);
			String priority_setting = (String)priority_table.get(trigger_name);
			if(priority_setting != null){
				er.setPriority(Integer.parseInt(priority_setting));
				System.out.println(var_name + ".setPriority(" + priority_setting + ");");
			}
			obj_rule_table.put(var_name, er);
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
			
			State s = obj_state_table.get(dest_state);
			if(s == null){
				System.out.println("state " + dest_state + " is undefined!");
				System.exit(-1);
			}
			obj_rule_table.put(var_name, new AdaptationRule(input, s));
		}

		for(int i = 0; i < trans_list.size(); i++){
			Transition t = (Transition)trans_list.get(i);
			String src_state, input, var_name;
			src_state = t.getSrc();
			input = t.getInput();
			var_name = "rule_" + input + "_s" + src_state;
			var_name = var_name.toLowerCase();
			
			Rule r = obj_rule_table.get(var_name);
			Predicate p = obj_predicate_abbr_table.get(input);
			if(p == null){
				System.out.println("predicate abbr for " + input + " is missing!");
				System.exit(-1);
			}
			r.setCondition(p);
			String priority_setting = (String)priority_table.get(input);
			if(priority_setting == null){
				System.out.println("priority " + input + " is missing!");
				System.exit(-1);
			}
			try{
				r.setPriority(Integer.parseInt((String)priority_table.get(input)));
			}catch(Exception ex){
				System.out.println("priority should be integer!");
			}
			
			
		}
		
		// clear relevant tables which will be used in the ConstraintPair section
		//predicate_abbr_table.clear();
		logic_exp_table.clear();
		//obj_predicate_abbr_table.clear();
		obj_logic_exp_table.clear();
		
		
		//before leaving PredicateAbbr section, add rule to states
		//loop on state, get name of T from trigger_table, get name of adaptation from PredicatesForState table
		
		e = obj_state_table.keys();
		while(e.hasMoreElements()){
			String state_name = (String)e.nextElement();
			State s = obj_state_table.get(state_name);
			ArrayList trigger_list = (ArrayList)trigger_table.get(state_name);
			ArrayList predicate_list = (ArrayList)predicates_for_state_table.get(state_name);
			for(int j = 0; trigger_list != null && j < trigger_list.size(); j++){
				String trigger_name = (String)trigger_list.get(j);
				String var_name = "rule_" + trigger_name;
				var_name = var_name.toLowerCase();
				s.addRule(obj_rule_table.get(var_name));
			}
			for(int j = 0; predicate_list != null && j < predicate_list.size(); j++){
				String adaptation_name = (String)predicate_list.get(j);
				String var_name = "rule_" + adaptation_name + "_s" + state_name;
				var_name = var_name.toLowerCase();
				s.addRule(obj_rule_table.get(var_name));				
			}
		}
		
		
		current_node = "fsm_constraint_pair";		
	}
	
	public void outAMyVariable(AMyVariable node)
    {
		System.out.println(node.getVarName().toString().trim() + " " + node.getShorttypename().toString().trim());
		context_variables_table.put(node.getVarName().toString().trim(), node.getShorttypename().toString().trim());
		
    }
	
	public void outALogicOrExp(ALogicOrExp node)
    {
		
		//System.out.println(node.getAnd1().toString() + " or " + node.getAnd2().toString());
		/*
		System.out.println(node.getClass());
		System.out.println(node.getAnd1().getClass());
		System.out.println(node.getAnd2().getClass());
		System.out.println(node.getAnd1().parent().getClass());
		System.out.println(node.getAnd1().parent().parent().getClass());
		System.out.println(node.getAnd1().parent().parent().parent().getClass());
		System.out.println(node.getAnd1().parent().parent().parent().parent().getClass());
		System.out.println(node.getAnd1().parent().parent().parent().parent().parent().getClass());
		System.out.println(node.getAnd1().parent().parent().parent().parent().parent().parent().getClass());
		System.out.println(node.getAnd1().parent().parent().parent().parent().parent().parent().parent().getClass());
		*/

		if(!logic_exp_table.containsKey(node.getAnd1()) && !logic_exp_table.containsKey(node.getAnd2())){
			String str1, str2, str3;
			str1 = "afsm.getVariableByName(\"" + node.getAnd1().toString().trim() + "\")";
			str2 = "afsm.getVariableByName(\"" + node.getAnd2().toString().trim() + "\")";
			str3 = "new OrPredicate(" + str1 + "," + str2 + ")";
			System.out.println("00:" + str3);
			logic_exp_table.put(node.getAnd1(), str1);
			logic_exp_table.put(node.getAnd2(), str2);
			logic_exp_table.put(node, str3);			
			logic_exp_table.put(node.parent(), str3);
			
			//store AST object and logic object pair
			PredicateVariable var1 = afsm.getVariableByName(node.getAnd1().toString().trim());
			PredicateVariable var2 = afsm.getVariableByName(node.getAnd2().toString().trim());
			obj_logic_exp_table.put(node.getAnd1(), var1);
			obj_logic_exp_table.put(node.getAnd2(), var2);
			OrPredicate or_pred = new OrPredicate(var1, var2);
			obj_logic_exp_table.put(node, or_pred);
			obj_logic_exp_table.put(node.parent(), or_pred);

			
		}else if(!logic_exp_table.containsKey(node.getAnd1()) && logic_exp_table.containsKey(node.getAnd2())){
			String str1, str2, str3;
			str1 = "afsm.getVariableByName(\"" + node.getAnd1().toString().trim() + "\")";
			str2 = (String)logic_exp_table.get(node.getAnd2());
			str3 = "new OrPredicate(" + str1 + "," + str2 + ")";
			System.out.println("01:" + str3);
			logic_exp_table.put(node.getAnd1(), str1);
			logic_exp_table.put(node, str3);
			logic_exp_table.put(node.parent(), str3);
			
			//store AST object and logic object pair
			PredicateVariable var1 = afsm.getVariableByName(node.getAnd1().toString().trim());
			obj_logic_exp_table.put(node.getAnd1(), var1);
			OrPredicate or_pred = new OrPredicate(var1, obj_logic_exp_table.get(node.getAnd2()));
			obj_logic_exp_table.put(node, or_pred);
			obj_logic_exp_table.put(node.parent(), or_pred);
			
		}else if(logic_exp_table.containsKey(node.getAnd1()) && !logic_exp_table.containsKey(node.getAnd2())){
			String str1, str2, str3;
			str1 = (String)logic_exp_table.get(node.getAnd1());
			str2 = "afsm.getVariableByName(\"" + node.getAnd2().toString().trim() + "\")";
			str3 = "new OrPredicate(" + str1 + "," + str2 + ")";
			System.out.println("10:" + str3);
			logic_exp_table.put(node.getAnd2(), str2);
			logic_exp_table.put(node, str3);
			logic_exp_table.put(node.parent(), str3);
			
			//store AST object and logic object pair
			PredicateVariable var2 = afsm.getVariableByName(node.getAnd2().toString().trim());
			obj_logic_exp_table.put(node.getAnd2(), var2);
			OrPredicate or_pred = new OrPredicate(obj_logic_exp_table.get(node.getAnd1()), var2);
			obj_logic_exp_table.put(node, or_pred);
			obj_logic_exp_table.put(node.parent(), or_pred);
			
		}else if(logic_exp_table.containsKey(node.getAnd1()) && logic_exp_table.containsKey(node.getAnd2())){
			String str1, str2, str3;
			str1 = (String)logic_exp_table.get(node.getAnd1());
			str2 = (String)logic_exp_table.get(node.getAnd2());
			str3 = "new OrPredicate(" + str1 + "," + str2 + ")";
			System.out.println("11:" + str3);
			logic_exp_table.put(node, str3);
			logic_exp_table.put(node.parent(), str3);
			
			//store AST object and logic object pair
			OrPredicate or_pred = new OrPredicate(obj_logic_exp_table.get(node.getAnd1()), obj_logic_exp_table.get(node.getAnd2()));
			obj_logic_exp_table.put(node, or_pred);
			obj_logic_exp_table.put(node.parent(), or_pred);
		}
		
    }
	
	public void outANotunaryUnaryExp(ANotunaryUnaryExp node)
    {
		if(!logic_exp_table.containsKey(node.getUnaryExp())){
			String str1, str2;
			str1 = "afsm.getVariableByName(\"" + node.getUnaryExp().toString().trim() + "\")";			
			str2 = "new NotPredicate(" + str1  + ")";
			System.out.println("00:" + str2);
			logic_exp_table.put(node.getUnaryExp(), str1);
			logic_exp_table.put(node, str2);			
			logic_exp_table.put(node.parent(), str2);	
			
			//store AST object and logic object pair
			PredicateVariable var = afsm.getVariableByName(node.getUnaryExp().toString().trim());
			NotPredicate not_pred = new NotPredicate(var);
			obj_logic_exp_table.put(node.getUnaryExp(), var);
			obj_logic_exp_table.put(node, not_pred);
			obj_logic_exp_table.put(node.parent(), not_pred);
			
		}else{
			String str1, str2;
			str1 = (String)logic_exp_table.get(node.getUnaryExp());
			str2 = "new NotPredicate(" + str1  + ")";
			logic_exp_table.put(node, str2);
			logic_exp_table.put(node.parent(), str2);
			
			//store AST object and logic object pair
			NotPredicate not_pred = new NotPredicate(obj_logic_exp_table.get(node.getUnaryExp()));
			obj_logic_exp_table.put(node, not_pred);
			obj_logic_exp_table.put(node.parent(), not_pred);
		}
    }
	
	public void outALogicAndExp(ALogicAndExp node)
    {
		if(!logic_exp_table.containsKey(node.getUnary1()) && !logic_exp_table.containsKey(node.getUnary2())){
			String str1, str2, str3;
			str1 = "afsm.getVariableByName(\"" + node.getUnary1().toString().trim() + "\")";
			str2 = "afsm.getVariableByName(\"" + node.getUnary2().toString().trim() + "\")";
			str3 = "new AndPredicate(" + str1 + "," + str2 + ")";
			System.out.println("00:" + str3);
			logic_exp_table.put(node.getUnary1(), str1);
			logic_exp_table.put(node.getUnary2(), str2);
			logic_exp_table.put(node, str3);			
			logic_exp_table.put(node.parent(), str3);
			
			//store AST object and logic object pair
			PredicateVariable var1 = afsm.getVariableByName(node.getUnary1().toString().trim());
			PredicateVariable var2 = afsm.getVariableByName(node.getUnary2().toString().trim());
			obj_logic_exp_table.put(node.getUnary1(), var1);
			obj_logic_exp_table.put(node.getUnary2(), var2);
			AndPredicate and_pred = new AndPredicate(var1, var2);
			obj_logic_exp_table.put(node, and_pred);
			obj_logic_exp_table.put(node.parent(), and_pred);

			
		}else if(!logic_exp_table.containsKey(node.getUnary1()) && logic_exp_table.containsKey(node.getUnary2())){
			String str1, str2, str3;
			str1 = "afsm.getVariableByName(\"" + node.getUnary1().toString().trim() + "\")";
			str2 = (String)logic_exp_table.get(node.getUnary2());
			str3 = "new AndPredicate(" + str1 + "," + str2 + ")";
			System.out.println("01:" + str3);
			logic_exp_table.put(node.getUnary1(), str1);
			logic_exp_table.put(node, str3);
			logic_exp_table.put(node.parent(), str3);
			
			//store AST object and logic object pair
			PredicateVariable var1 = afsm.getVariableByName(node.getUnary1().toString().trim());
			obj_logic_exp_table.put(node.getUnary1(), var1);
			AndPredicate and_pred = new AndPredicate(var1, obj_logic_exp_table.get(node.getUnary2()));
			obj_logic_exp_table.put(node, and_pred);
			obj_logic_exp_table.put(node.parent(), and_pred);
			
		}else if(logic_exp_table.containsKey(node.getUnary1()) && !logic_exp_table.containsKey(node.getUnary2())){
			String str1, str2, str3;
			str1 = (String)logic_exp_table.get(node.getUnary1());
			str2 = "afsm.getVariableByName(\"" + node.getUnary2().toString().trim() + "\")";
			str3 = "new AndPredicate(" + str1 + "," + str2 + ")";
			System.out.println("10:" + str3);
			logic_exp_table.put(node.getUnary2(), str2);
			logic_exp_table.put(node, str3);
			logic_exp_table.put(node.parent(), str3);
			
			//store AST object and logic object pair
			PredicateVariable var2 = afsm.getVariableByName(node.getUnary2().toString().trim());
			obj_logic_exp_table.put(node.getUnary2(), var2);
			AndPredicate and_pred = new AndPredicate(obj_logic_exp_table.get(node.getUnary1()), var2);
			obj_logic_exp_table.put(node, and_pred);
			obj_logic_exp_table.put(node.parent(), and_pred);
			
		}else if(logic_exp_table.containsKey(node.getUnary1()) && logic_exp_table.containsKey(node.getUnary2())){
			String str1, str2, str3;
			str1 = (String)logic_exp_table.get(node.getUnary1());
			str2 = (String)logic_exp_table.get(node.getUnary2());
			str3 = "new AndPredicate(" + str1 + "," + str2 + ")";
			System.out.println("11:" + str3);
			logic_exp_table.put(node, str3);
			logic_exp_table.put(node.parent(), str3);
			
			//store AST object and logic object pair
			AndPredicate and_pred = new AndPredicate(obj_logic_exp_table.get(node.getUnary1()), obj_logic_exp_table.get(node.getUnary2()));
			obj_logic_exp_table.put(node, and_pred);
			obj_logic_exp_table.put(node.parent(), and_pred);
		}
    }
	
	public void outAParenExp(AParenExp node)
    {
		System.out.println("in parenExp: " + node.toString().trim());
		if(logic_exp_table.get(node)==null)
			System.out.println("outAParenExp logic_exp_table.get(node)=null");
		else{
			logic_exp_table.put(node.parent(), logic_exp_table.get(node));
		
			obj_logic_exp_table.put(node.parent(), obj_logic_exp_table.get(node));
		}
    }
	public void outAExpPrimaryExp(AExpPrimaryExp node) //terminal node, store node (not parent) in the tables
    {
		System.out.println("in outAExpPrimaryExp");
		/*
		System.out.println("-------------------------------");
		System.out.println(node.getFactor().getClass());
		System.out.println(node.getFactor().parent().getClass());
		System.out.println(node.getFactor().parent().parent().getClass());
		System.out.println(node.getFactor().parent().parent().parent().getClass());
		System.out.println(node.getFactor().parent().parent().parent().parent().getClass());
		System.out.println(node.getFactor().parent().parent().parent().parent().parent().getClass());
		System.out.println(node.getFactor().parent().parent().parent().parent().parent().parent().getClass());
		System.out.println(node.getFactor().parent().parent().parent().parent().parent().parent().parent().getClass());
		System.out.println("-------------------------------");
		*/
		if(logic_exp_table.get(node)==null) //node not existing in table, add it to table
		{
			System.out.println("outAExpPrimaryExp logic_exp_table.get(node)=null");
			System.out.println("node=" + node);
			
			
			String str = "afsm.getVariableByName(\"" + node.getFactor().toString().trim() + "\")";
			logic_exp_table.put(node, str);
			System.out.println("str=" + str);
			PredicateVariable var = afsm.getVariableByName(node.getFactor().toString().trim());
			if(var != null)
				obj_logic_exp_table.put(node, var);
			else{
				System.out.println("The variable " + node.getFactor().toString().trim() + " is not defined in ContextVariables section!");
				System.exit(-1);
			}
		}
		// in terminal node, always assign value to parent, and store parent in the tables
		logic_exp_table.put(node.parent(), logic_exp_table.get(node));
		obj_logic_exp_table.put(node.parent(), obj_logic_exp_table.get(node));
    }
	
	public void outAPrimexpUnaryExp(APrimexpUnaryExp node) //non-terminal node, just pass node value to its parent
    {
		if(logic_exp_table.get(node)==null)
			System.out.println("outAPrimexpUnaryExp logic_exp_table.get(node)=null");
		else{
			logic_exp_table.put(node.parent(), logic_exp_table.get(node));
			
			obj_logic_exp_table.put(node.parent(), obj_logic_exp_table.get(node));
		}
    }
    
	public void outAEmptyLogicAndExp(AEmptyLogicAndExp node) //non-terminal node, just pass node value to its parent
    {
		if(logic_exp_table.get(node)==null)
			System.out.println("outAEmptyLogicAndExp logic_exp_table.get(node)=null");
		else{
			logic_exp_table.put(node.parent(), logic_exp_table.get(node));
			
			obj_logic_exp_table.put(node.parent(), obj_logic_exp_table.get(node));
		}
    }
	public void outAEmptyLogicOrExp(AEmptyLogicOrExp node) //non-terminal node, just pass node value to its parent
    {
		if(logic_exp_table.get(node)==null)
			System.out.println("outAEmptyLogicOrExp logic_exp_table.get(node)=null");
		else{
			logic_exp_table.put(node.parent(), logic_exp_table.get(node));
			
			obj_logic_exp_table.put(node.parent(), obj_logic_exp_table.get(node));
		}
    }
	public void outAParenexpPrimaryExp(AParenexpPrimaryExp node) //non-terminal node, just pass node value to its parent
    {
		if(logic_exp_table.get(node)==null)
			System.out.println("outAParenexpPrimaryExp logic_exp_table.get(node)=null");
		else{
			logic_exp_table.put(node.parent(), logic_exp_table.get(node));
			
			obj_logic_exp_table.put(node.parent(), obj_logic_exp_table.get(node));
		}
    }
	public void outAPredicateLogic(APredicateLogic node) //non-terminal node, just pass node value to its parent
    {
		if(logic_exp_table.get(node)==null)
			System.out.println("outAPredicateLogic logic_exp_table.get(node)=null");
		else{
			logic_exp_table.put(node.parent(), logic_exp_table.get(node));
			
			obj_logic_exp_table.put(node.parent(), obj_logic_exp_table.get(node));
		}
    }

	public void outAMyPredicate(AMyPredicate node) // collect pair of predicate name and predicate
    {
        System.out.println("----------" + node.getPredicateName().toString().trim() + ": " + logic_exp_table.get(node));
        if(logic_exp_table.get(node) == null)
        	System.out.println("outAMyPredicate logic_exp_table.get(node)=null");
        else{
	        predicate_abbr_table.put(node.getPredicateName().toString().trim(), logic_exp_table.get(node));
	        
	        obj_predicate_abbr_table.put(node.getPredicateName().toString().trim(), obj_logic_exp_table.get(node));
        }
    }
/*
	public void outAMyStateName(AMyStateName node)
    {
		predicate_name_list = new ArrayList();
    }
	
	public void outAMyPredicatesForState(AMyPredicatesForState node)
    {
		System.out.println(node.getMyStateName().toString().trim());
        predicates_for_state_table.put(node.getMyStateName().toString().trim(), predicate_name_list);
    }
	
	public void outAFsmPredicatesForState(AFsmPredicatesForState node)
    {
        //before leaving PredicateForState section, add rule to states
		//loop on state, get name of T from trigger_table, get name of adaptation from PredicatesForState table
		Enumeration e;
		e = obj_state_table.keys();
		while(e.hasMoreElements()){
			String state_name = (String)e.nextElement();
			State s = obj_state_table.get(state_name);
			ArrayList trigger_list = (ArrayList)trigger_table.get(state_name);
			ArrayList predicate_list = (ArrayList)predicates_for_state_table.get(state_name);
			for(int j = 0; trigger_list != null && j < trigger_list.size(); j++){
				String trigger_name = (String)trigger_list.get(j);
				s.addRule(obj_rule_table.get("rule_" + trigger_name.toLowerCase()));
			}
			for(int j = 0; predicate_list != null && j < predicate_list.size(); j++){
				String adaptation_name = (String)predicate_list.get(j);
				s.addRule(obj_rule_table.get("rule_" + adaptation_name.toLowerCase() + "_s" + state_name));				
			}
		}
		
    }
	
	public void outAOnePredicateForState(AOnePredicateForState node)
    {
        predicate_name_list.add(node.getFactor().toString().trim());
    }
	*/
	
	public void outAFsmConstraintPair(AFsmConstraintPair node)
    {
		// before leaving ConstraintPair, don't forget to instantiate elements in afsm
        if(node.getConstraintPairList() == null)
        	System.out.println("*********** constraint pair list is empty!!");
        else{
        	// new Constraint and add to afsm
        	for(int i = 0; i < obj_constraint_pair_list.size(); i++){
        		ArrayList<Predicate> pair = obj_constraint_pair_list.get(i);
        		Constraint c = new Constraint(pair.get(0), pair.get(1));
        		afsm.addConstrain(c);
        	}
        }
    }
	
	public void outAMyConstraintPair(AMyConstraintPair node)
    {
        Node condition_node = node.getConditionPredicate();
        Node effect_node = node.getEffectPredicate();
        
        Predicate condition_predicate = (Predicate)obj_logic_exp_table.get(condition_node);
        Predicate effect_predicate = (Predicate)obj_logic_exp_table.get(effect_node);
        
        if(condition_predicate == null || effect_predicate == null){
        	System.out.println("a pair in ConstraintPair is not complete!");
        	System.exit(-1);
        }
        ArrayList<Predicate> pair = new ArrayList<Predicate>();
        pair.add(condition_predicate);
        pair.add(effect_predicate);
        obj_constraint_pair_list.add(pair);
        
        /*
        Variable vars[];
        vars = condition_predicate.getVariables();
        for(int i = 0; i < vars.length; i++)
        	System.out.println("condition---" + vars[i].getName());
        vars = effect_predicate.getVariables();
        for(int i = 0; i < vars.length; i++)
        	System.out.println("effect---" + vars[i].getName());
        //System.out.println("in outAMyConstraintPair: condition_predicate = " + condition_predicate.getVariables());
        //System.out.println("in outAMyConstraintPair: effect_predicate = " + effect_predicate.getVariables());
        */
        
    }
	
	public void outAIntegerFactor(AIntegerFactor node)
    {
		TInt int_node;
		String int_str;
		System.out.println("in outAIntegerFactor, current_node=" + current_node);
		if(current_node.equals("fsm_state"))
		{			
			int_node = node.getInt();
			int_str = int_node.getText();
         		if(obj_state_table.containsKey(int_str))
         		{
				System.out.println("state " + int_str + " is redefined!");
				System.exit(0);
         		}
         		else
         		{
         			State state = new State(int_str);
         			state_list.add(state); 
         			//add state name and state obj to obj_state_table
         			obj_state_table.put(int_str, state);  
         			System.out.println("add state:" + int_str);   				
         		}
			
		}else if(current_node.equals("fsm_input_alpha"))
		{
			int_node = node.getInt();
			int_str = int_node.getText();
			if(input_table.containsKey(int_str))
         		{
				System.out.println("input alpha " + int_str + " is redefined!");
				System.exit(0);
         		}
         		else
         		{   
         			input_table.put(int_str, int_str);  
         			System.out.println("add input_alpha:" + int_str);				
         		}				
		}else if(current_node.equals("fsm_start"))
		{
			int_node = node.getInt();
			int_str = int_node.getText();
			System.out.println("start state: " + int_str);
			int size = state_list.size();
			if(!obj_state_table.containsKey(int_str))
         	{
				System.out.println("start state alpha " + int_str + " is not defined!");
				System.exit(0);
         	}		
			
			Enumeration e;
			e = obj_state_table.keys();
			while(e.hasMoreElements()){
				String state_name = (String)e.nextElement();
				if(int_str.equals(state_name)){
					State s = (State)obj_state_table.get(state_name);
					s.setStartState(true);
					afsm.setInitialState(s);
					break;
				}
					
			}
			
			for(int i = 0; i < size; i++)
			{
				State state = (State)state_list.get(i);
				
				if(int_str.equals(state.getName()))
				{
					state.setStartState(true);
					state_list.set(i, state);
					break;
				}			
			}
			//System.out.println("fsm_start:" + int_str); 
		} 
		else if(current_node.equals("fsm_forbidden"))
		{
			int_node = node.getInt();
			int_str = int_node.getText();
			int size = state_list.size();
			if(!obj_state_table.containsKey(int_str))
         	{
				System.out.println("forbidden state alpha " + int_str + " is not defined!");
				System.exit(0);
         	}
			for(int i = 0; i < size; i++)
			{
				State state = (State)state_list.get(i);
				if(int_str.equals(state.getName()))
				{
					state.setForbiddenState(true);
					state_list.set(i, state);
					break;
				}				
			}
			System.out.println("forbidden state:" + int_str); 
		}
		else if(current_node.equals("fsm_transition"))
		{
			int_node = node.getInt();
			factor_str = int_node.getText();
			//System.out.println("factor_str:" + factor_str); 			
		}
    }
	
	public void outAIdentifierFactor(AIdentifierFactor node)
	{
		TId id_node;
		String id_str;
		System.out.println("in outAIdentifierFactor, current_node=" + current_node);
		if(current_node.equals("fsm_state"))
		{
			id_node = node.getId();
			id_str = id_node.getText();
         		if(obj_state_table.containsKey(id_str))
         		{
				System.out.println("state " + id_str + " is redefined!");
				System.exit(0);
         		}
         		else
         		{
         			State state = new State(id_str);
         			state_list.add(state); 
         			//add state name and state obj to obj_state_table
         			obj_state_table.put(id_str, state);    
         			//System.out.println("add state:" + id_str); 				
         		}			
		}
		else if(current_node.equals("fsm_input_alpha"))
		{
			id_node = node.getId();
			id_str = id_node.getText();
			if(input_table.containsKey(id_str))
         		{
				System.out.println("input alpha " + id_str + " is redefined!");
				System.exit(0);
         		}
         		else
         		{   
         			input_table.put(id_str, id_str);  
         			System.out.println("add input_alpha:" + id_str);				
         		}			
		}
		else if(current_node.equals("fsm_start"))
		{
			id_node = node.getId();
			id_str = id_node.getText();
			int size = state_list.size();
			if(!obj_state_table.containsKey(id_str))
         	{
				System.out.println("start state alpha " + id_str + " is not defined!");
				System.exit(0);
         	}
			Enumeration e;
			e = obj_state_table.keys();
			while(e.hasMoreElements()){
				String state_name = (String)e.nextElement();
				if(id_str.equals(state_name)){
					State s = (State)obj_state_table.get(state_name);
					s.setStartState(true);
					afsm.setInitialState(s);
					break;
				}
					
			}
			for(int i = 0; i < size; i++)
			{
				State state = (State)state_list.get(i);
				if(id_str.equals(state.getName()))
				{
					state.setStartState(true);
					state_list.set(i, state);
					break;
				}			
			}
			System.out.println("start state:" + id_str); 
		}
		else if(current_node.equals("fsm_forbidden"))
		{
			id_node = node.getId();
			id_str = id_node.getText();
			int size = state_list.size();
			if(!obj_state_table.containsKey(id_str))
         		{
				System.out.println("forbidden state alpha " + id_str + " is not defined!");
				System.exit(0);
         		}
			for(int i = 0; i < size; i++)
			{
				State state = (State)state_list.get(i);
				if(id_str.equals(state.getName()))
				{
					state.setForbiddenState(true);
					state_list.set(i, state);
					break;
				}				
			}
			System.out.println("forbidden state: " + id_str); 
		}
		else if(current_node.equals("fsm_transition"))
		{
			id_node = node.getId();
			factor_str = id_node.getText();
			//System.out.println("factor_str:" + factor_str); 			
		}/*
		else if(current_node.equals("fsm_predicate_abbr"))
		{
			//logic_exp_table.put(node, node.toString().trim());
			
			System.out.println("in factor");
			
			//System.out.println(node.getClass());
			//System.out.println(node.parent().getClass());
			//System.out.println(node.parent().parent().getClass());
			//System.out.println(node.parent().parent().parent().getClass());
			//System.out.println(node.parent().parent().parent().parent().getClass());
			//System.out.println(node.parent().parent().parent().parent().parent().getClass());
			//System.out.println(node.parent().parent().parent().parent().parent().parent().getClass());
			//System.out.println(node.parent().parent().parent().parent().parent().parent().parent().getClass());
			
			String str;
			str = "afsm.getVariableByName(\"" + node.toString().trim() + "\")";
			logic_exp_table.put(node.parent(), str);
			System.out.println("str=" + str);
			obj_logic_exp_table.put(node.parent(), afsm.getVariableByName(node.toString().trim()));
			
			
		}*/
		
	}
}
