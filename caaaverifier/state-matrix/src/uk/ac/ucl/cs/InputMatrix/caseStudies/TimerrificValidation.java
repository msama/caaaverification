/**
 * Creates an instance of TimerrificValidation scenario. 
 * 
 * http://code.google.com/p/autosettings/
 */
package uk.ac.ucl.cs.InputMatrix.caseStudies;

import static uk.ac.ucl.cs.InputMatrix.NotPredicate.*;
import static uk.ac.ucl.cs.InputMatrix.OrPredicate.*;
import static uk.ac.ucl.cs.InputMatrix.AndPredicate.*;
import static uk.ac.ucl.cs.InputMatrix.PredicateVariable.*;
import static uk.ac.ucl.cs.InputMatrix.ContextVariable.*;



import java.io.FileNotFoundException;
import java.io.PrintWriter;

import uk.ac.ucl.cs.InputMatrix.AdaptationFSM;
import uk.ac.ucl.cs.InputMatrix.AdaptationRule;
import uk.ac.ucl.cs.InputMatrix.ContextVariable;
import uk.ac.ucl.cs.InputMatrix.Fault;
import uk.ac.ucl.cs.InputMatrix.Predicate;
import uk.ac.ucl.cs.InputMatrix.Rule;
import uk.ac.ucl.cs.InputMatrix.State;
import uk.ac.ucl.cs.InputMatrix.validation.ContextHazardDetectionOptimized;
import uk.ac.ucl.cs.InputMatrix.validation.DeadConditionsDetection;
import uk.ac.ucl.cs.InputMatrix.validation.ForbiddenStatesViolation;
import uk.ac.ucl.cs.InputMatrix.validation.MinimumSafeDelayGenerator;
import uk.ac.ucl.cs.InputMatrix.validation.NondeterministicActivation;
import uk.ac.ucl.cs.InputMatrix.validation.RaceAndCycle;
import uk.ac.ucl.cs.InputMatrix.validation.weakCondition.WeakConditionValidationAlgorithm;

/**
 * @author rax
 *
 */
public class TimerrificValidation {

	public final static String OUTPUT_FOLDER="out/timerrific";
	
	
	public final static String FORBIDDEN_STATES_VIOLATION_FILE = 
		OUTPUT_FOLDER + "/forbidden.txt";
	public final static String FORBIDDEN_STATES_VIOLATION_FILE_STATISTICS = 
		OUTPUT_FOLDER+"/forbidden_statistics.txt";
	
	public final static String DEAD_CONDITIONS_FILE = 
		OUTPUT_FOLDER+"/DeadConditions.txt";
	public final static String DEAD_CONDITIONS_FILE_STATISTICS = 
		OUTPUT_FOLDER+"/DeadConditions_statistics.txt";
	
	public final static String NONDETERMINISTIC_ADAPTATION_FILE = 
		OUTPUT_FOLDER+"/nondeterministic.txt";
	public final static String NONDETERMINISTIC_ADAPTATION_FILE_STATISTICS = 
		OUTPUT_FOLDER+"/nondeterministic_statistics.txt";
	
	public final static String RACE_FILE =
		OUTPUT_FOLDER+"/race.txt";
	public final static String RACE_FILE_STATISTICS =
		OUTPUT_FOLDER+"/race_statistics.txt";
	
	public final static String HAZARD_FILE = 
		OUTPUT_FOLDER+"/hazard.txt";
	public final static String HAZARD_FILE_STATISTICS =
		OUTPUT_FOLDER+"/hazard_statistics.txt";
	public final static String MINIMUM_SAFE_DELAYS_FILE =
		OUTPUT_FOLDER+"/minimum_safe_delays.txt";
	
	public final static String WEAK_CONDITIONS_FILE = 
		OUTPUT_FOLDER+"/WeakConditions.txt";
	public final static String WEAK_CONDITIONS_FILE_STATISTICS = 
		OUTPUT_FOLDER+"/WeakConditions_statistics.txt";
	
	/**
	 * Creates a model to test autosettings/timerrific. The applications adapts according to this code
	 * 
	 * <code>
	 * int start = prefs.startHourMin();
     * int stop = prefs.stopHourMin();
     *   
     * boolean inRange;
     * if (start <= stop) {
     *       inRange = hourMin >= start && hourMin < stop;
     * } else {
     *       inRange = hourMin < stop || hourMin >= start;
     * }
     *   
     * SettingsHelper helper = new SettingsHelper(context);
     * if (inRange) {
     *       prefs.appendToLog("Apply START settings");
     *       helper.applyStartSettings();
     *       scheduleAlarm(context, prefs, stop); // schedule alarm at stop time
     * } else {
     *       prefs.appendToLog("Apply STOP settings");
     *       helper.applyStopSettings();
     *       scheduleAlarm(context, prefs, start); // schedule alarm at start time
     * }
	 * </code>
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args){
		
		AdaptationFSM afsm = new AdaptationFSM();
		System.out.println("A-FSM created.");
		
		//ContextVariables
		ContextVariable time = context(afsm, "Time", 1);
		ContextVariable alarm = context(afsm, "Alarm", 2000);
		
		//variable definition
		Predicate startLesserEqualStop = null;
		Predicate beforeStop = null;
		Predicate afterEqualStart = null;
		Predicate enableSound = null;
		Predicate enableVibration = null;
		try {
			startLesserEqualStop = variable(afsm, "StartLesserEqualStop", time); // start <= stop
			beforeStop = variable(afsm, "BeforeStop", time); // time < stop_time
			afterEqualStart = variable(afsm, "AfterEqualStart", time); // time >= start_time
	
			enableSound = variable(afsm, "EnableSound", alarm);
			enableVibration = variable(afsm, "EnableVibration", alarm);
            
			System.out.println(afsm.getInputDimension() + " variables created.");
		} catch (Exception e) {
			System.err.println("Error in variables definition: " + e + ".");
			System.exit(1);
		}
		
		//constraints
		//time 
		//Constraint c_time=new Constraint(afsm.getVariableByName("BeforeStop"),afsm.getVariableByName("AfterEqualStart"));
		//afsm.addConstrain(c_time);
		
		//states definition
		State initial = new State("Initial");
		State day = new State("Day");
		State night = new State("Night");

		//add states to the afsm
		try {
			afsm.addState(initial);
			afsm.addState(day);
			afsm.addState(night);
			
			afsm.setInitialState(initial);
		} catch (Exception e) {
			System.err.println("Error in states definition.");
			System.exit(1);
		}
		System.out.println("States definied.");
		
		//predicates & rules definition	
		// time >= start_time && time < stop_time
		Predicate isDay  = or(and(startLesserEqualStop, afterEqualStart, beforeStop),
				and(not(startLesserEqualStop), or(beforeStop, afterEqualStart)));
		Predicate isNight = not(isDay);
		
		Predicate dayMode = and(enableSound, not(enableVibration));
		Predicate nightMode = and(enableVibration, not(enableSound));
		
		AdaptationRule activateDayMode = new AdaptationRule("ActivateDayMode", day);
		activateDayMode.setCondition(isDay);
		activateDayMode.setAppliedAction(dayMode);
		activateDayMode.setPriority(Rule.DEFAULT_PRIORITY);
		
		// time < stop_time || time >= start_time
		AdaptationRule activateNightMode = new AdaptationRule("ActivateNightMode", night);
		activateNightMode.setCondition(isNight);
		activateNightMode.setAppliedAction(nightMode);
		activateNightMode.setPriority(Rule.DEFAULT_PRIORITY);
		
		System.out.println("Predicates definied.");
		
		//add rules into states
		
		//(initial,{activateDayMode, activateNightMode})
		initial.addRule(activateDayMode);
		initial.addRule(activateNightMode);

        //(day,{activateNightMode})
		day.addRule(activateNightMode);
		//day.setHoldCondition(day_predicate);
		day.setHoldCondition(isDay);
		day.setInStateAssumptions(enableSound);

        //(night,{activateDayMode})
		night.addRule(activateDayMode);
		night.setHoldCondition(isNight);
		night.setInStateAssumptions(not(enableSound));
		
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
				PrintWriter pw = new PrintWriter(TimerrificValidation.NONDETERMINISTIC_ADAPTATION_FILE);
				pw.println("PhoneAdapter");
				nondeterministicActivation.printFaultsToStream(faults, pw);
				pw.close();
				System.out.println("Instances saved in "+TimerrificValidation.NONDETERMINISTIC_ADAPTATION_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}
		}
		System.out.println("Saving statistics.");
		try {
			PrintWriter pw = new PrintWriter(TimerrificValidation.NONDETERMINISTIC_ADAPTATION_FILE_STATISTICS);
			pw.println("PhoneAdapter");
			nondeterministicActivation.printStatisticsToStream(pw);
			pw.close();
			System.out.println("Instances saved in "+TimerrificValidation.NONDETERMINISTIC_ADAPTATION_FILE_STATISTICS);
		} catch (FileNotFoundException e) {
			System.err.println("Error while saving on file.");
		}
		System.out.println("");		
		
		//WE MUST SOLVE NONDETERMINISIC
		//--------------------------------------------------------------------------------
		//Nondeterminstic point must be removed first
		
		//activateHome.setPriority(6);
		//afsm.loadInputSpaces();
		
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
				PrintWriter pw=new PrintWriter(TimerrificValidation.FORBIDDEN_STATES_VIOLATION_FILE);
				pw.println("PhoneAdapter");
				forbiddenStatesViolation.printFaultsToStream(faults, pw);
				pw.flush();
				pw.close();
				System.out.println("Instances saved in "+TimerrificValidation.FORBIDDEN_STATES_VIOLATION_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}
		}
		System.out.println("Saving statistics.");
		try {
			PrintWriter pw=new PrintWriter(TimerrificValidation.FORBIDDEN_STATES_VIOLATION_FILE_STATISTICS);
			pw.println("PhoneAdapter");
			forbiddenStatesViolation.printStatisticsToStream(pw);
			pw.flush();
			pw.close();
			System.out.println("Instances saved in "+TimerrificValidation.FORBIDDEN_STATES_VIOLATION_FILE_STATISTICS);
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
				PrintWriter pw=new PrintWriter(TimerrificValidation.DEAD_CONDITIONS_FILE);
				pw.println("PhoneAdapter");
				deadConditionsDetection.printFaultsToStream(faults, pw);
				pw.close();
				System.out.println("Instances saved in "+TimerrificValidation.DEAD_CONDITIONS_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}
		}
		System.out.println("Saving statistics.");
		try {
			PrintWriter pw=new PrintWriter(TimerrificValidation.DEAD_CONDITIONS_FILE_STATISTICS);
			pw.println("PhoneAdapter");
			deadConditionsDetection.printStatisticsToStream(pw);
			pw.close();
			System.out.println("Instances saved in "+TimerrificValidation.DEAD_CONDITIONS_FILE_STATISTICS);
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
				PrintWriter pw = new PrintWriter(TimerrificValidation.RACE_FILE);
				pw.println("PhoneAdapter");
				racencycle.printFaultsToStream(faults, pw);
				pw.flush();
				pw.close();
				System.out.println("Instances saved in "+TimerrificValidation.RACE_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}
		}
		System.out.println("Saving statistics.");
		try {
			PrintWriter pw = new PrintWriter(TimerrificValidation.RACE_FILE_STATISTICS);
			pw.println("PhoneAdapter");
			racencycle.printStatisticsToStream(pw);
			pw.flush();
			pw.close();
			System.out.println("Instances saved in "+TimerrificValidation.RACE_FILE_STATISTICS);
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
				PrintWriter pw=new PrintWriter(TimerrificValidation.HAZARD_FILE);
				pw.println("PhoneAdapter");
				contextHazardDetection.printFaultsToStream(faults, pw);
				pw.flush();
				pw.close();
				System.out.println("Instances saved in "+TimerrificValidation.HAZARD_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}
			
			//MinimumSafeDelays
			//*******************
			MinimumSafeDelayGenerator minimumSafeDelaysGenerator = new MinimumSafeDelayGenerator();
			minimumSafeDelaysGenerator.generateDelays(faults);
			try {
				PrintWriter pw=new PrintWriter(TimerrificValidation.MINIMUM_SAFE_DELAYS_FILE);
				minimumSafeDelaysGenerator.printToStream(pw);
				pw.flush();
				pw.close();
				System.out.println("Instances saved in "+TimerrificValidation.MINIMUM_SAFE_DELAYS_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}
			
		}
		System.out.println("Saving statistics.");
		try {
			PrintWriter pw=new PrintWriter(TimerrificValidation.HAZARD_FILE_STATISTICS);
			pw.println("PhoneAdapter");
			contextHazardDetection.printStatisticsToStream(pw);
			pw.flush();
			pw.close();
			System.out.println("Instances saved in "+TimerrificValidation.HAZARD_FILE_STATISTICS);
		} catch (FileNotFoundException e) {
			System.err.println("Error while saving on file: " + e.getMessage());
		}
		System.out.println("");	
		
		
		//Weak in state Conditions
		//--------------------------------------------------------------------------------
		System.out.println("Performing Weak In-State Condition detection:");
		WeakConditionValidationAlgorithm weakCondition = new WeakConditionValidationAlgorithm();
		weakCondition.setAFSMUnderTest(afsm);
		faults = weakCondition.detectFaults();
		
		if (faults.length == 0) {
			System.out.println("SUCCESS: No WWeak In-State Conditions.");
		} else {
			System.out.println("FAULT: " + faults.length + " instances detected.");
			
			try {
				PrintWriter pw=new PrintWriter(TimerrificValidation.WEAK_CONDITIONS_FILE);
				pw.println("AutoSettings Timerrific");
				weakCondition.printFaultsToStream(faults, pw);
				pw.close();
				System.out.println("Instances saved in "+TimerrificValidation.WEAK_CONDITIONS_FILE);
			} catch (FileNotFoundException e) {
				System.err.println("Error while saving on file.");
			}
		}
		System.out.println("Saving statistics.");
		try {
			PrintWriter pw = new PrintWriter(TimerrificValidation.WEAK_CONDITIONS_FILE_STATISTICS);
			pw.println("AutoSettings Timerrific");
			weakCondition.printStatisticsToStream(pw);
			pw.close();
			System.out.println("Instances saved in "+TimerrificValidation.WEAK_CONDITIONS_FILE_STATISTICS);
		} catch (FileNotFoundException e) {
			System.err.println("Error while saving on file.");
		}
		System.out.println("");
	}

}
