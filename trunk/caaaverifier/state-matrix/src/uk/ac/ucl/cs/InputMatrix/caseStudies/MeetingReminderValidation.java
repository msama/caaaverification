/**
 * This class creates an instance of MeetingReminderValidation scenario. The same result can be obtained by loading the opportune file. 
 * This class is an example of hard coded usage of AFSM with the only purpose of helping to implement a parser
 */
package uk.ac.ucl.cs.InputMatrix.caseStudies;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import uk.ac.ucl.cs.InputMatrix.AdaptationFSM;
import uk.ac.ucl.cs.InputMatrix.AdaptationRule;
import uk.ac.ucl.cs.InputMatrix.AndPredicate;
import uk.ac.ucl.cs.InputMatrix.ContextVariable;
import uk.ac.ucl.cs.InputMatrix.ExternalRule;
import uk.ac.ucl.cs.InputMatrix.Fault;
import uk.ac.ucl.cs.InputMatrix.NotPredicate;
import uk.ac.ucl.cs.InputMatrix.OrPredicate;
import uk.ac.ucl.cs.InputMatrix.Rule;
import uk.ac.ucl.cs.InputMatrix.State;
import uk.ac.ucl.cs.InputMatrix.validation.ContextHazardDetection;
import uk.ac.ucl.cs.InputMatrix.validation.ContextHazardFault;
import uk.ac.ucl.cs.InputMatrix.validation.DeadConditionFault;
import uk.ac.ucl.cs.InputMatrix.validation.DeadConditionsDetection;
import uk.ac.ucl.cs.InputMatrix.validation.ForbiddenStateViolationFault;
import uk.ac.ucl.cs.InputMatrix.validation.ForbiddenStatesViolation;
import uk.ac.ucl.cs.InputMatrix.validation.MinimumSafeDelayGenerator;
import uk.ac.ucl.cs.InputMatrix.validation.NondeterministicActivation;
import uk.ac.ucl.cs.InputMatrix.validation.InStateFault;
import uk.ac.ucl.cs.InputMatrix.validation.RaceAndCycle;
import uk.ac.ucl.cs.InputMatrix.validation.RaceFault;

/**
 * @author rax
 *
 */
public class MeetingReminderValidation {

	public final static String OUTPUT_FOLDER="out";
	
	public final static String FORBIDDEN_STATES_VIOLATION_FILE=OUTPUT_FOLDER+"/MeetingReminderValidation_forbidden.txt";
	//public final static String FORBIDDEN_STATES_VIOLATION_REDUCED_FILE=OUTPUT_FOLDER+"/MeetingReminderValidation_reduced_forbidden.txt";
	public final static String DEAD_CONDITIONS_FILE=OUTPUT_FOLDER+"/MeetingReminderValidation_DeadConditions.txt";
	
	public final static String NONDETERMINISTIC_ADAPTATION_FILE=OUTPUT_FOLDER+"/MeetingReminderValidation_nondeterministic.txt";
	public final static String NONDETERMINISTIC_ADAPTATION_REDUCED_FILE=OUTPUT_FOLDER+"/MeetingReminderValidation_reduced_nondeterministic.txt";
	
	public final static String RACE_FILE=OUTPUT_FOLDER+"/MeetingReminderValidation_race.txt";
	public final static String RACE_REDUCED_FILE=OUTPUT_FOLDER+"/MeetingReminderValidation_reduced_race.txt";
	
	public final static String HAZARD_FILE=OUTPUT_FOLDER+"/MeetingReminderValidation_hazard.txt";
	//public final static String HAZARD_REDUCED_FILE=OUTPUT_FOLDER+"/MeetingReminderValidation_reduced_hazard.txt";
	public final static String MINIMUM_SAFE_DELAYS_FILE=OUTPUT_FOLDER+"/MeetingReminderValidation_minimum_safe_delays.txt";
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args){
		
		AdaptationFSM afsm=new AdaptationFSM();
		System.out.println("A-FSM created.");
		
		//ContextVariables
		ContextVariable t=new ContextVariable("Time",1);
		ContextVariable bt=new ContextVariable("Bluetooth",60000);
		ContextVariable gps=new ContextVariable("GPS",1000);
		afsm.addContextVariable(t);
		afsm.addContextVariable(bt);
		afsm.addContextVariable(gps);
		
		
		//variable definition
		try {
			afsm.createVariable("A_bt",bt);
			afsm.createVariable("B_t",t);
			afsm.createVariable("C_bt",bt);
			afsm.createVariable("D_gps",gps);
			afsm.createVariable("E_t",t);
			afsm.createVariable("F_bt",bt);
			afsm.createVariable("G_gps",gps);
			afsm.createVariable("H_t",t);
			afsm.createVariable("I_bt",bt);
			afsm.createVariable("L_gps",gps);
			afsm.createVariable("M_t",t);
			afsm.createVariable("N_bt",bt);
			afsm.createVariable("O_gps",gps);
			System.out.println(afsm.getInputDimension() +" variables created.");
		} catch (Exception e) {
			System.err.println("Error in variables definition.");
			System.exit(1);
		}
		
		
		//states definition
		State s0=new State("S0");
		State s1=new State("S1");
		State s2=new State("S2");
		State s3=new State("S3");
		s3.setForbiddenState(true);
		//add states to the afsm
		try {
			afsm.addState(s0);
			afsm.addState(s1);
			afsm.addState(s2);
			afsm.addState(s3);
			
			afsm.setInitialState(s0);
		} catch (Exception e) {
			System.err.println("Error in states definition.");
			System.exit(1);
		}
		System.out.println("States definied.");
		
		//predicates & rules definition
		ExternalRule rule_t1=new ExternalRule("T1");
		rule_t1.setCondition(afsm.getVariableByName("A_bt"));
		
		ExternalRule rule_t2=new ExternalRule("T2");
		OrPredicate or_t2=new OrPredicate(afsm.getVariableByName("C_bt"),afsm.getVariableByName("D_gps"));
		AndPredicate and_t2=new AndPredicate(afsm.getVariableByName("B_t"),or_t2);
		rule_t2.setCondition(and_t2);
		rule_t2.setPriority(Rule.DEFAULT_PRIORITY+1);//lower then normal
		
		AdaptationRule rule_a3_s0=new AdaptationRule("A3",s1);
		AdaptationRule rule_a3_s2=new AdaptationRule("A3",s3);
		OrPredicate or_a3=new OrPredicate(afsm.getVariableByName("F_bt"),afsm.getVariableByName("G_gps"));
		AndPredicate and_a3=new AndPredicate(afsm.getVariableByName("E_t"),new NotPredicate(or_a3));
		rule_a3_s0.setCondition(and_a3);
		rule_a3_s2.setCondition(and_a3);
		rule_a3_s0.setPriority(Rule.MAX_PRIORITY);
		rule_a3_s2.setPriority(Rule.MAX_PRIORITY);
		
		AdaptationRule rule_d3=new AdaptationRule("D3",s0);
		OrPredicate or_d3=new OrPredicate(afsm.getVariableByName("H_t"), or_a3);
		rule_d3.setCondition(or_d3);
		rule_d3.setPriority(Rule.MAX_PRIORITY);
		
		ExternalRule rule_t3=new ExternalRule("T3");
		OrPredicate or_t3=new OrPredicate(new NotPredicate(afsm.getVariableByName("I_bt")),new NotPredicate(afsm.getVariableByName("L_gps")));
		rule_t3.setCondition(or_t3);
		rule_t3.setPriority(Rule.LOW_PRIORITY);
		
		AdaptationRule rule_a4_s0=new AdaptationRule("A4",s2);
		AdaptationRule rule_a4_s1=new AdaptationRule("A4",s3);
		OrPredicate or_a4=new OrPredicate(afsm.getVariableByName("N_bt"),afsm.getVariableByName("O_gps"));
		AndPredicate and_a4=new AndPredicate(afsm.getVariableByName("M_t"),new NotPredicate(or_a4));
		rule_a4_s0.setCondition(and_a4);
		rule_a4_s0.setPriority(Rule.MAX_PRIORITY);
		rule_a4_s1.setCondition(and_a4);
		rule_a4_s1.setPriority(Rule.MAX_PRIORITY);
		AdaptationRule rule_d4=new AdaptationRule("D4",s0);
		OrPredicate or_d4=new OrPredicate(afsm.getVariableByName("E_t"), or_a4);
		rule_d4.setCondition(or_d4);
		rule_d4.setPriority(Rule.MAX_PRIORITY);
		
		ExternalRule rule_t4=new ExternalRule("T4");
		rule_t4.setCondition(or_t3);
		rule_t4.setPriority(Rule.LOW_PRIORITY);
		
		System.out.println("Predicates definied.");
		
		//add rules into states
		s0.addRule(rule_a3_s0);
		s0.addRule(rule_a4_s0);
		s0.addRule(rule_t1);
		s0.addRule(rule_t2);
		
		s1.addRule(rule_a4_s1);
		s1.addRule(rule_d3);
		s1.addRule(rule_t1);
		s1.addRule(rule_t2);
		s1.addRule(rule_t3);
		
		s2.addRule(rule_a3_s2);
		s2.addRule(rule_d4);
		s2.addRule(rule_t1);
		s2.addRule(rule_t2);
		s2.addRule(rule_t4);
		
		System.out.println("States configured.");
		
		afsm.loadInputSpaces();
		System.out.println("Input spaces evaluated.");
		
		Fault[] faults;
		
		
		
		
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
				PrintWriter pw=new PrintWriter(MeetingReminderValidation.FORBIDDEN_STATES_VIOLATION_FILE);
				pw.println("Meeting reminder "+faults.length+" instances of forbidden states violation.");
				pw.println("State\tForbidden\tRule\tInput");
				for(Fault f:faults)
				{
					ForbiddenStateViolationFault ff=(ForbiddenStateViolationFault)f;
					pw.println(ff.getState().getName()+"\t"+ff.getForbiddenState().getName()+"\t"+ff.getRule().getName()+"\t"+ff.getInput());
				}
				pw.flush();
				pw.close();
				System.out.println("Instances saved in "+MeetingReminderValidation.FORBIDDEN_STATES_VIOLATION_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}

			/*
			System.out.println("Applying regression...");
			faults=forbiddenStatesViolation.applyRegression(faults);
			System.out.println("Faults reduced to: "+faults.length+".");
			
			
			try {
				PrintWriter pw=new PrintWriter(MeetingReminderValidation.FORBIDDEN_STATES_VIOLATION_REDUCED_FILE);
				pw.println("Meeting reminder "+faults.length+" instances of forbidden states violation.");
				pw.println("State\tForbidden\tRule\tInput");
				for(Fault f:faults)
				{
					ForbiddenStateViolationFault ff=(ForbiddenStateViolationFault)f;
					pw.println(ff.getState().getName()+"\t"+ff.getForbiddenState().getName()+"\t"+ff.getRule().getName()+"\t"+ff.getInput());
				}
				pw.flush();
				pw.close();
				System.out.println("Instances saved in "+MeetingReminderValidation.FORBIDDEN_STATES_VIOLATION_REDUCED_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}*/
		}
		System.out.println("");
		
		
		//ForbiddenStatesViolation
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
				PrintWriter pw=new PrintWriter(MeetingReminderValidation.DEAD_CONDITIONS_FILE);
				pw.println("Meeting reminder "+faults.length+" instances of dead conditions.");
				pw.println("State\tRule");
				for(Fault f:faults)
				{
					DeadConditionFault ff=(DeadConditionFault)f;
					pw.println(ff.getState().getName()+"\t"+ff.getRule().getName());
				}
				pw.flush();
				pw.close();
				System.out.println("Instances saved in "+MeetingReminderValidation.DEAD_CONDITIONS_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}

			/*
			System.out.println("Applying regression...");
			faults=forbiddenStatesViolation.applyRegression(faults);
			System.out.println("Faults reduced to: "+faults.length+".");
			
			
			try {
				PrintWriter pw=new PrintWriter(MeetingReminderValidation.FORBIDDEN_STATES_VIOLATION_REDUCED_FILE);
				pw.println("Meeting reminder "+faults.length+" instances of forbidden states violation.");
				pw.println("State\tForbidden\tRule\tInput");
				for(Fault f:faults)
				{
					ForbiddenStateViolationFault ff=(ForbiddenStateViolationFault)f;
					pw.println(ff.getState().getName()+"\t"+ff.getForbiddenState().getName()+"\t"+ff.getRule().getName()+"\t"+ff.getInput());
				}
				pw.flush();
				pw.close();
				System.out.println("Instances saved in "+MeetingReminderValidation.FORBIDDEN_STATES_VIOLATION_REDUCED_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}*/
		}
		System.out.println("");
		
		
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
				PrintWriter pw = new PrintWriter(MeetingReminderValidation.NONDETERMINISTIC_ADAPTATION_FILE);
				pw.println("Meeting reminder "+faults.length+" instances of nondeterministic activation.");
				pw.println("State\tInput\tRules");
				for(Fault f:faults)
				{
					InStateFault ff=(InStateFault)f;
					pw.print(ff.getState().getName()+"\t"+ff.getInput()+"\t[");
					for(int i=0;i<ff.getNondeterministicRules().length;i++)
					{
						Rule r=ff.getNondeterministicRules()[i];
						pw.print(r.getName());
						if(i!=ff.getNondeterministicRules().length-1)
						{
							pw.print(",");
						}
					}
					pw.println("]");
				}
				pw.flush();
				pw.close();
				System.out.println("Instances saved in "+MeetingReminderValidation.NONDETERMINISTIC_ADAPTATION_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}
			
			System.out.println("Applying regression...");
			faults=nondeterministicActivation.applyRegression(faults);
			System.out.println("Faults reduced to: "+faults.length+".");
			
			try {
				PrintWriter pw = new PrintWriter(MeetingReminderValidation.NONDETERMINISTIC_ADAPTATION_REDUCED_FILE);
				pw.println("Meeting reminder "+faults.length+" instances of nondeterministic activation.");
				pw.println("State\tInput\tRules");
				for(Fault f:faults)
				{
					InStateFault ff=(InStateFault)f;
					pw.print(ff.getState().getName()+"\t"+ff.getInput()+"\t[");
					for(int i=0;i<ff.getNondeterministicRules().length;i++)
					{
						Rule r=ff.getNondeterministicRules()[i];
						pw.print(r.getName());
						if(i!=ff.getNondeterministicRules().length-1)
						{
							pw.print(",");
						}
					}
					pw.println("]");
				}
				pw.flush();
				pw.close();
				System.out.println("Instances saved in "+MeetingReminderValidation.NONDETERMINISTIC_ADAPTATION_REDUCED_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}
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
				PrintWriter pw = new PrintWriter(MeetingReminderValidation.RACE_FILE);
				pw.println("Meeting reminder");
				racencycle.printFaultsToStream(faults, pw);
				pw.flush();
				pw.close();
				System.out.println("Instances saved in "+MeetingReminderValidation.RACE_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}
			
			System.out.println("Applying regression...");
			faults=racencycle.applyRegression(faults);
			System.out.println("Faults reduced to: "+faults.length+".");
			
			
			try {
				PrintWriter pw = new PrintWriter(MeetingReminderValidation.RACE_REDUCED_FILE);
				pw.println("Meeting reminder ");
				racencycle.printFaultsToStream(faults, pw);
				pw.flush();
				pw.close();
				System.out.println("Instances saved in "+MeetingReminderValidation.RACE_REDUCED_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}
		}
		System.out.println("");
		
		//ContextHazardDetection
		//--------------------------------------------------------------------------------
		System.out.println("Performing context hazards detection:");
		ContextHazardDetection contextHazardDetection=new ContextHazardDetection();
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
				PrintWriter pw=new PrintWriter(MeetingReminderValidation.HAZARD_FILE);
				pw.println("Meeting reminder "+faults.length+" instances of context hazard.");
				pw.println("State\tType\tConfiguration\tCommutations\tRules");
				for(Fault f:faults)
				{
					ContextHazardFault ff=(ContextHazardFault)f;
					pw.print(ff.getState().getName()+"\t"+ff.getName()+"\t"+ff.getInput()+"\t");
					
					pw.print("{");
					for(int i=0;i<ff.getCriticalPath().length;i++)
					{
						pw.print(ff.getCriticalPath()[i].getName());
						if(i!=ff.getCriticalPath().length-1)
						{
							pw.print(",");
						}
					}
					pw.print("}\t");
					
					pw.print("{");
					for(int i=0;i<ff.getRules().length;i++)
					{
						pw.print(ff.getRules()[i].getName());
						if(i!=ff.getRules().length-1)
						{
							pw.print(",");
						}
					}
					pw.print("}");
					
					pw.println("");
				}
				pw.flush();
				pw.close();
				System.out.println("Instances saved in "+MeetingReminderValidation.HAZARD_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}

			/*
			System.out.println("Applying regression...");
			faults=contextHazardDetection.applyRegression(faults);
			System.out.println("Faults reduced to: "+faults.length+".");
			
			
			try {
				PrintWriter pw=new PrintWriter(MeetingReminderValidation.HAZARD_REDUCED_FILE);
				pw.println("Meeting reminder "+faults.length+" instances of forbidden states violation.");
				pw.println("State\tConfiguration\tCommutations");
				for(Fault f:faults)
				{
					ContextHazardFault ff=(ContextHazardFault)f;
					pw.print(ff.getState().getName()+"\t"+ff.getName()+"\t"+ff.getInput()+"\t");
					
					pw.print("{");
					for(int i=0;i<ff.getCriticalPath().length;i++)
					{
						pw.print(ff.getCriticalPath()[i].getName());
						if(i!=ff.getCriticalPath().length-1)
						{
							pw.print(",");
						}
					}
					pw.print("}\t");
					
					pw.print("{");
					for(int i=0;i<ff.getRules().length;i++)
					{
						pw.print(ff.getRules()[i].getName());
						if(i!=ff.getRules().length-1)
						{
							pw.print(",");
						}
					}
					pw.print("}");
					
					pw.println("");
				}
				pw.flush();
				pw.close();
				System.out.println("Instances saved in "+MeetingReminderValidation.HAZARD_REDUCED_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}
			*/
			
			//MinimumSafeDelays
			//*******************
			MinimumSafeDelayGenerator minimumSafeDelaysGenerator = new MinimumSafeDelayGenerator();
			minimumSafeDelaysGenerator.generateDelays((ContextHazardFault[]) faults);
			try {
				PrintWriter pw=new PrintWriter(MeetingReminderValidation.MINIMUM_SAFE_DELAYS_FILE);
				minimumSafeDelaysGenerator.printToStream(pw);
				pw.flush();
				pw.close();
				System.out.println("Instances saved in "+MeetingReminderValidation.MINIMUM_SAFE_DELAYS_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}
			
		}
		System.out.println("");
		
			
		
	}

}
