package uk.ac.ucl.cs.InputMatrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PushbackReader;
import java.util.HashSet;
import java.util.Set;

import uk.ac.ucl.cs.InputMatrix.AdaptationFSM;
import uk.ac.ucl.cs.InputMatrix.ContextVariable;
import uk.ac.ucl.cs.InputMatrix.Interpreter;
import uk.ac.ucl.cs.InputMatrix.MainMachine;
import uk.ac.ucl.cs.InputMatrix.Rule;
import uk.ac.ucl.cs.InputMatrix.State;
import uk.ac.ucl.cs.InputMatrix.PredicateVariable;
import uk.ac.ucl.cs.InputMatrix.myparser.lexer.Lexer;
import uk.ac.ucl.cs.InputMatrix.myparser.node.Start;
import uk.ac.ucl.cs.InputMatrix.myparser.parser.Parser;
import junit.framework.TestCase;

public class AdaptiveFSMInstanceTest extends TestCase {

	private AdaptationFSM afsm;
	private final String spec_path = "D:/mypaper/icse08submit/src/src/uk/ac/ucl/cs/InputMatrix";
	protected void setUp() throws Exception {
		super.setUp();

		try
		{
			// create lexer
			Lexer lexer = new Lexer (new PushbackReader(new BufferedReader(new FileReader(spec_path + "/new-fsm.txt")), 1024));

			// parser program
			Parser parser = new Parser(lexer);

			Start ast = parser.parse();

			// check program semantics
			MainMachine machine = new MainMachine();
			Interpreter interpreter = new Interpreter(machine.state_list, machine.trans_list, 
					machine.trigger_table, machine.priority_table, machine.context_types_table,
					machine.context_variables_table, machine.predicate_abbr_table, machine.predicates_for_state_table);
			
			ast.apply(interpreter);			
			afsm = interpreter.getAdaptationFSM();
		}
		catch (Exception e)
		{
			System.out.println(e);
		}

	}

	public void testState(){
		int size;
		Set<String> set = new HashSet<String>();
		size = afsm.stateSize();
		for(int i = 0; i < size; i++){
			String str = afsm.stateAt(i).getName();
			set.add(str);
		}
		//Idle,Outdoor,Jogging,Driving,DrivingFast,Home,Office,Meeting,Synch
		assertTrue(size == 9);
		assertTrue(set.contains("Idle"));
		assertTrue(set.contains("Outdoor"));
		assertTrue(set.contains("Jogging"));
		assertTrue(set.contains("Driving"));
		assertTrue(set.contains("DrivingFast"));
		assertTrue(set.contains("Home"));
		assertTrue(set.contains("Office"));
		assertTrue(set.contains("Meeting"));
		assertTrue(set.contains("Synch"));		
	}
	
	public void testStart(){
		assertTrue(afsm.getInitialState().getName().equals("Idle"));
	}
	
	public void testForbidden(){
		boolean hasForbidden = false;
		int size = afsm.stateSize();
		for(int i = 0; i < size; i++){
			State s = afsm.stateAt(i);
			if(s.isForbiddenState()){
				hasForbidden = true;
				break;
			}
		}
		assertTrue(hasForbidden == false);
	}
	
	public void testTransition(){
		int size = afsm.stateSize();
		for(int i = 0; i < size; i++){
			State s = afsm.stateAt(i);
			//Idle,Outdoor,Jogging,Driving,DrivingFast,Home,Office,Meeting,Synch
			if(s.getName().equals("Idle")){
				assertTrue(s.ruleSize() == 4);
			}else if(s.getName().equals("Outdoor")){
				assertTrue(s.ruleSize() == 5);
			}else if(s.getName().equals("Jogging")){
				assertTrue(s.ruleSize() == 1);
			}else if(s.getName().equals("Driving")){
				assertTrue(s.ruleSize() == 2);
			}else if(s.getName().equals("DrivingFast")){
				assertTrue(s.ruleSize() == 1);
			}else if(s.getName().equals("Home")){
				assertTrue(s.ruleSize() == 2);
			}else if(s.getName().equals("Office")){
				assertTrue(s.ruleSize() == 4);
			}else if(s.getName().equals("Meeting")){
				assertTrue(s.ruleSize() == 1);
			}else if(s.getName().equals("Synch")){
				assertTrue(s.ruleSize() == 2);
			}
							
		}
	}
	/*
	public void testPriority(){
		
	}
	
	public void testContextTypes(){
		
	}
	
	public void testContextVariables(){
		
	}
	
	public void testPredicateAbbr(){
		
	}
	
	public void testConstraintPair(){

	}*/
}
