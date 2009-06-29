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
import uk.ac.ucl.cs.InputMatrix.Constraint;
import uk.ac.ucl.cs.InputMatrix.ContextVariable;
import uk.ac.ucl.cs.InputMatrix.ExternalRule;
import uk.ac.ucl.cs.InputMatrix.Fault;
import uk.ac.ucl.cs.InputMatrix.NotPredicate;
import uk.ac.ucl.cs.InputMatrix.OrPredicate;
import uk.ac.ucl.cs.InputMatrix.Rule;
import uk.ac.ucl.cs.InputMatrix.State;
import uk.ac.ucl.cs.InputMatrix.validation.ContextHazardDetection;
import uk.ac.ucl.cs.InputMatrix.validation.ContextHazardDetectionOptimized;
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
public class PhoneAdapterSubset {

	public final static String OUTPUT_FOLDER="D:/j2me-test/fault-detection-output";
	
	public final static String FORBIDDEN_STATES_VIOLATION_FILE=OUTPUT_FOLDER+"/PhoneAdapter_forbidden.txt";
	public final static String FORBIDDEN_STATES_VIOLATION_FILE_STATISTICS=OUTPUT_FOLDER+"/PhoneAdapter_forbidden_statistics.txt";
	//public final static String FORBIDDEN_STATES_VIOLATION_REDUCED_FILE=OUTPUT_FOLDER+"/MeetingReminderValidation_reduced_forbidden.txt";
	
	public final static String DEAD_CONDITIONS_FILE=OUTPUT_FOLDER+"/PhoneAdapter_DeadConditions.txt";
	public final static String DEAD_CONDITIONS_FILE_STATISTICS=OUTPUT_FOLDER+"/PhoneAdapter_DeadConditions_statistics.txt";
	
	public final static String NONDETERMINISTIC_ADAPTATION_FILE=OUTPUT_FOLDER+"/PhoneAdapter_nondeterministic.txt";
	//public final static String NONDETERMINISTIC_ADAPTATION_REDUCED_FILE=OUTPUT_FOLDER+"/PhoneAdapter_reduced_nondeterministic.txt";
	public final static String NONDETERMINISTIC_ADAPTATION_FILE_STATISTICS=OUTPUT_FOLDER+"/PhoneAdapter_nondeterministic_statistics.txt";
	
	public final static String RACE_FILE=OUTPUT_FOLDER+"/PhoneAdapter_race.txt";
	//public final static String RACE_REDUCED_FILE=OUTPUT_FOLDER+"/PhoneAdapter_reduced_race.txt";
	public final static String RACE_FILE_STATISTICS=OUTPUT_FOLDER+"/PhoneAdapter_race_statistics.txt";
	
	public final static String HAZARD_FILE=OUTPUT_FOLDER+"/PhoneAdapter_hazard.txt";
	public final static String HAZARD_FILE_STATISTICS=OUTPUT_FOLDER+"/PhoneAdapter_hazard_statistics.txt";
	//public final static String HAZARD_REDUCED_FILE=OUTPUT_FOLDER+"/MeetingReminderValidation_reduced_hazard.txt";
	public final static String MINIMUM_SAFE_DELAYS_FILE=OUTPUT_FOLDER+"/PhoneAdapter_minimum_safe_delays.txt";
	
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
		//ContextVariable s=new ContextVariable("Synch",10000);
		afsm.addContextVariable(t);
		afsm.addContextVariable(bt);
		afsm.addContextVariable(gps);
		//afsm.addContextVariable(s);
		
		//variable definition
		try {
			
			afsm.createVariable("A_gps",gps); //GPS.isValid();
            afsm.createVariable("B_gps",gps); //GPS_home
            afsm.createVariable("C_gps",gps); //GPS_office
            afsm.createVariable("A_bt",bt); //BT={home_pc}
            afsm.createVariable("B_bt",bt); //BT={office_pc}
            afsm.createVariable("C_bt",bt); //bt=office_pc_*
            afsm.createVariable("D_bt",bt); //bt=car_handsfree
            afsm.createVariable("E_bt",bt); //bt.count>=3
            afsm.createVariable("A_t",t); //time>=meeting_start
            afsm.createVariable("B_t",t);
			
			System.out.println(afsm.getInputDimension() +" variables created.");
		} catch (Exception e) {
			System.err.println("Error in variables definition: "+e+".");
			System.exit(1);
		}
		/*
		//constraints
		//Gps.isValid
		Constraint c_gpsAB=new Constraint(new NotPredicate(afsm.getVariableByName("A_gps")),new NotPredicate(afsm.getVariableByName("B_gps")));
		Constraint c_gpsAC=new Constraint(new NotPredicate(afsm.getVariableByName("A_gps")),new NotPredicate(afsm.getVariableByName("C_gps")));
		Constraint c_gpsAD=new Constraint(new NotPredicate(afsm.getVariableByName("A_gps")),new NotPredicate(afsm.getVariableByName("D_gps")));
		Constraint c_gpsAE=new Constraint(new NotPredicate(afsm.getVariableByName("A_gps")),new NotPredicate(afsm.getVariableByName("E_gps")));
		afsm.addConstrain(c_gpsAB);
		afsm.addConstrain(c_gpsAC);
		afsm.addConstrain(c_gpsAD);
		afsm.addConstrain(c_gpsAE);
		
		//home != office
		Constraint c_home=new Constraint(afsm.getVariableByName("B_gps"),new NotPredicate(afsm.getVariableByName("C_gps")));
		afsm.addConstrain(c_home);
		Constraint c_office=new Constraint(afsm.getVariableByName("C_gps"),new NotPredicate(afsm.getVariableByName("B_gps")));
		afsm.addConstrain(c_office);
		
		//speed>70 -> speed>5
		Constraint c_speed=new Constraint(afsm.getVariableByName("E_gps"),afsm.getVariableByName("D_gps"));
		afsm.addConstrain(c_speed);
		
		//time 
		Constraint c_time=new Constraint(afsm.getVariableByName("B_t"),afsm.getVariableByName("A_t"));
		afsm.addConstrain(c_time);
		*/
		//states definition
		State idle =new State("Default");
		State driving=new State("Driving");
		State home=new State("Home");
		State office=new State("Office");
		State meeting=new State("Meeting");


		//add states to the afsm
		try {
			afsm.addState(idle);
			afsm.addState(driving);
			afsm.addState(home);
			afsm.addState(office);
			afsm.addState(meeting);

			
			afsm.setInitialState(idle);
		} catch (Exception e) {
			System.err.println("Error in states definition.");
			System.exit(1);
		}
		System.out.println("States definied.");
		
		//predicates & rules definition	
		
		AdaptationRule activateHome=new AdaptationRule("ActivateHome",home);
		OrPredicate activateHome_predicate=new OrPredicate(afsm.getVariableByName("A_bt"), new AndPredicate(afsm.getVariableByName("A_gps"), afsm.getVariableByName("B_gps")));
		activateHome.setCondition(activateHome_predicate);
		activateHome.setPriority(5);
		
		AdaptationRule deactivateHome=new AdaptationRule("DeactivateHome",idle);
		deactivateHome.setCondition(new NotPredicate(activateHome_predicate));
		deactivateHome.setPriority(5);
		

		AdaptationRule activateOffice=new AdaptationRule("ActivateOffice",office);
		OrPredicate activateOffice_predicate=new OrPredicate(afsm.getVariableByName("B_bt"), new OrPredicate(afsm.getVariableByName("C_bt"), new AndPredicate(afsm.getVariableByName("A_gps"), afsm.getVariableByName("C_gps"))));
		activateOffice.setCondition(activateOffice_predicate);
		activateOffice.setPriority(5);
		
		
		AdaptationRule deactivateOffice=new AdaptationRule("DeactivateOffice",idle);
		deactivateOffice.setCondition(new NotPredicate(activateOffice_predicate));
		deactivateOffice.setPriority(5);
		
		AdaptationRule activateMeeting=new AdaptationRule("ActivateMeeting",meeting);
		AndPredicate activateMeeting_predicate=new AndPredicate(afsm.getVariableByName("A_t"), afsm.getVariableByName("E_bt"));
		activateMeeting.setCondition(activateMeeting_predicate);
		activateMeeting.setPriority(4);
		
		AdaptationRule deactivateMeeting=new AdaptationRule("DeactivateMeeting",office);
		deactivateMeeting.setCondition(afsm.getVariableByName("B_t"));
		deactivateMeeting.setPriority(4);
		
		AdaptationRule activateDriving=new AdaptationRule("ActivateDriving",driving);
		activateDriving.setCondition(afsm.getVariableByName("D_bt"));
		activateDriving.setPriority(1);
		
		AdaptationRule deactivateDriving=new AdaptationRule("DeactivateDriving",idle);
		deactivateDriving.setCondition(new NotPredicate(afsm.getVariableByName("D_bt")));
		deactivateDriving.setPriority(1);

		
		
		
		
		
		System.out.println("Predicates definied.");
		
		//add rules into states
		
		//(Idle,{ActivateOutdoor,ActivateDriving,ActivateHome,ActivateOffice})

		idle.addRule(activateDriving);
		idle.addRule(activateHome);
		idle.addRule(activateOffice);

		
        //(Driving,{DeactivateDriving,ActivateDrivingFast})
		driving.addRule(deactivateDriving);

        
		//(Home,{DeactivateHome,ActivateDriving})
		home.addRule(deactivateHome);
		home.addRule(activateDriving);
        
		//(Office,{DeactivateOffice,ActivateMeeting,ActivateSynch,ActivateDriving})
		office.addRule(deactivateOffice);
		office.addRule(activateMeeting);
		office.addRule(activateDriving);
        
		//(Meeting,{DeactivateMeeting})
		meeting.addRule(deactivateMeeting);
        

		
		
		System.out.println("States configured.");
		
		afsm.loadInputSpaces();
		System.out.println("Input spaces evaluated.");
		
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
				PrintWriter pw = new PrintWriter(PhoneAdapterValidation.NONDETERMINISTIC_ADAPTATION_FILE);
				pw.println("PhoneAdapter");
				nondeterministicActivation.printFaultsToStream(faults, pw);
				pw.close();
				System.out.println("Instances saved in "+PhoneAdapterValidation.NONDETERMINISTIC_ADAPTATION_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}
			
			/*
			System.out.println("Applying regression...");
			faults=nondeterministicActivation.applyRegression(faults);
			System.out.println("Faults reduced to: "+faults.length+".");
			
			try {
				PrintWriter pw = new PrintWriter(PhoneAdapterValidation.NONDETERMINISTIC_ADAPTATION_REDUCED_FILE);
				pw.println("PhoneAdapter");
				nondeterministicActivation.printFaultsToStream(faults, pw);
				pw.close();
				System.out.println("Instances saved in "+PhoneAdapterValidation.NONDETERMINISTIC_ADAPTATION_REDUCED_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}*/
		}
		System.out.println("Saving statistics.");
		try {
			PrintWriter pw = new PrintWriter(PhoneAdapterValidation.NONDETERMINISTIC_ADAPTATION_FILE_STATISTICS);
			pw.println("PhoneAdapter");
			nondeterministicActivation.printStatisticsToStream(pw);
			pw.close();
			System.out.println("Instances saved in "+PhoneAdapterValidation.NONDETERMINISTIC_ADAPTATION_FILE_STATISTICS);
		} catch (FileNotFoundException e) {
			System.err.println("Error while saving on file.");
		}
		System.out.println("");
		
		//WE MUST SOLVE NONDETERMINISIC
		//--------------------------------------------------------------------------------
		//Nondeterminstic point must be removed first
		
		activateHome.setPriority(6);
		deactivateHome.setPriority(6);
		
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
				PrintWriter pw=new PrintWriter(PhoneAdapterValidation.FORBIDDEN_STATES_VIOLATION_FILE);
				pw.println("PhoneAdapter");
				forbiddenStatesViolation.printFaultsToStream(faults, pw);
				pw.flush();
				pw.close();
				System.out.println("Instances saved in "+PhoneAdapterValidation.FORBIDDEN_STATES_VIOLATION_FILE);
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
		System.out.println("Saving statistics.");
		try {
			PrintWriter pw=new PrintWriter(PhoneAdapterValidation.FORBIDDEN_STATES_VIOLATION_FILE_STATISTICS);
			pw.println("PhoneAdapter");
			forbiddenStatesViolation.printStatisticsToStream(pw);
			pw.flush();
			pw.close();
			System.out.println("Instances saved in "+PhoneAdapterValidation.FORBIDDEN_STATES_VIOLATION_FILE_STATISTICS);
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
				PrintWriter pw=new PrintWriter(PhoneAdapterValidation.DEAD_CONDITIONS_FILE);
				pw.println("PhoneAdapter");
				deadConditionsDetection.printFaultsToStream(faults, pw);
				pw.close();
				System.out.println("Instances saved in "+PhoneAdapterValidation.DEAD_CONDITIONS_FILE);
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
		System.out.println("Saving statistics.");
		try {
			PrintWriter pw=new PrintWriter(PhoneAdapterValidation.DEAD_CONDITIONS_FILE_STATISTICS);
			pw.println("PhoneAdapter");
			deadConditionsDetection.printStatisticsToStream(pw);
			pw.close();
			System.out.println("Instances saved in "+PhoneAdapterValidation.DEAD_CONDITIONS_FILE_STATISTICS);
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
				PrintWriter pw = new PrintWriter(PhoneAdapterValidation.RACE_FILE);
				pw.println("PhoneAdapter");
				racencycle.printFaultsToStream(faults, pw);
				pw.flush();
				pw.close();
				System.out.println("Instances saved in "+PhoneAdapterValidation.RACE_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}
			
			/*
			System.out.println("Applying regression...");
			faults=racencycle.applyRegression(faults);
			System.out.println("Faults reduced to: "+faults.length+".");
			
			
			try {
				PrintWriter pw = new PrintWriter(PhoneAdapterValidation.RACE_REDUCED_FILE);
				pw.println("PhoneAdapter");
				racencycle.printFaultsToStream(faults, pw);
				pw.flush();
				pw.close();
				System.out.println("Instances saved in "+PhoneAdapterValidation.RACE_REDUCED_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}
			*/
			
		}
		System.out.println("Saving statistics.");
		try {
			PrintWriter pw = new PrintWriter(PhoneAdapterValidation.RACE_FILE_STATISTICS);
			pw.println("PhoneAdapter");
			racencycle.printStatisticsToStream(pw);
			pw.flush();
			pw.close();
			System.out.println("Instances saved in "+PhoneAdapterValidation.RACE_FILE_STATISTICS);
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
				PrintWriter pw=new PrintWriter(PhoneAdapterValidation.HAZARD_FILE);
				pw.println("PhoneAdapter");
				contextHazardDetection.printFaultsToStream(faults, pw);
				pw.flush();
				pw.close();
				System.out.println("Instances saved in "+PhoneAdapterValidation.HAZARD_FILE);
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
				PrintWriter pw=new PrintWriter(PhoneAdapterValidation.MINIMUM_SAFE_DELAYS_FILE);
				minimumSafeDelaysGenerator.printToStream(pw);
				pw.flush();
				pw.close();
				System.out.println("Instances saved in "+PhoneAdapterValidation.MINIMUM_SAFE_DELAYS_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}
			
		}
		System.out.println("Saving statistics.");
		try {
			PrintWriter pw=new PrintWriter(PhoneAdapterValidation.HAZARD_FILE_STATISTICS);
			pw.println("PhoneAdapter");
			contextHazardDetection.printStatisticsToStream(pw);
			pw.flush();
			pw.close();
			System.out.println("Instances saved in "+PhoneAdapterValidation.HAZARD_FILE_STATISTICS);
		} catch (FileNotFoundException e) {
			System.err.println("Error while saving on file.");
		}
		System.out.println("");
				
		
		
			
		
	}

}
