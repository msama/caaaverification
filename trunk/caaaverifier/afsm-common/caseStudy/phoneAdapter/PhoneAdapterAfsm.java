/**
 * 
 */
package phoneAdapter;

import static uk.ac.ucl.cs.afsm.common.Assignment.negate;
import static uk.ac.ucl.cs.afsm.common.Assignment.satisfy;
import static uk.ac.ucl.cs.afsm.common.Assignment.*;
import static uk.ac.ucl.cs.afsm.common.predicate.Operator.*;

import uk.ac.ucl.cs.afsm.common.AdaptationFiniteStateMachine;
import uk.ac.ucl.cs.afsm.common.AfsmBuilder;
import uk.ac.ucl.cs.afsm.common.Context;
import uk.ac.ucl.cs.afsm.common.Rule;
import uk.ac.ucl.cs.afsm.common.State;
import uk.ac.ucl.cs.afsm.common.predicate.Predicate;
import uk.ac.ucl.cs.afsm.common.predicate.Variable;

/**
 * @author -RAX- (Michele Sama)
 *
 */
public class PhoneAdapterAfsm implements AfsmBuilder {

	@Override
	public AdaptationFiniteStateMachine getAdaptationFiniteStateMachine() {
		// Create the AFSM
		AdaptationFiniteStateMachine afsm = new AdaptationFiniteStateMachine("PhoneAdapter");
		
		// Contexts
		Context time = new Context("Time");
		Context bt = new Context("Bluetooth");
		Context gps = new Context("GPS");
		
		Variable varGpsA = afsm.variable("A_gps", gps); //GPS.isValid();
		Variable varGpsB = afsm.variable("B_gps", gps); //GPS_home
		Variable varGpsC = afsm.variable("C_gps", gps); //GPS_office
		Variable varGpsD = afsm.variable("D_gps",gps); //GPS.speed()>5
		Variable varGpsE = afsm.variable("E_gps",gps); //GPS.speed()>70
		Variable varBtA = afsm.variable("A_bt", bt); //BT={"Car_handsfree"}
		Variable varBtB = afsm.variable("B_bt", bt); //BT={home_pc}
		Variable varBtC = afsm.variable("C_bt", bt); // Office pc
		Variable varBtD = afsm.variable("D_bt", bt); // Any office bt device
		Variable varBtE = afsm.variable("E_bt", bt);
		Variable varTimeA = afsm.variable("A_t", time);
		Variable varTimeB = afsm.variable("B_t", time);
		
		State idle = afsm.state("Idle", true, false);
		State outdoor = afsm.state("Outdoor", false, false);
		State jogging = afsm.state("Jogging", false, false);
		State driving = afsm.state("Driving", false, false);
		State drivingFast = afsm.state("DrivingFast", false, false);
		State home = afsm.state("Home", false, false);
		State office = afsm.state("Office", false, false);
		State meeting = afsm.state("Meeting", false, false);
		State synch = afsm.state("Synch", false, false);
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
		
		
		// TODO(rax): we could insert effects here
		
		Predicate triggerActivateOutdoor = and(varGpsA, and(not(varGpsB), not(varGpsC)));
		Rule activateOutdoor = afsm.rule("ActivateOutdoor", triggerActivateOutdoor, outdoor);
		
		Predicate triggerDeactivateOutdoor = not(triggerActivateOutdoor);
		Rule deactivateOutdoor = afsm.rule("DeactivateOutdoor", triggerDeactivateOutdoor, idle);
		
		//(ActivateJogging,A_gps \AND D_gps)
		Predicate triggerActivateJogging = and(varGpsA, varGpsD);
		Rule activateJogging = afsm.rule("ActivateJogging", triggerActivateJogging, jogging);

		//(DeactivateJogging,\NOT(A_gps \AND D_gps))
		Predicate triggerDeactivateJogging = not(triggerActivateJogging);
		Rule deactivateJogging = afsm.rule("DeactivateJogging", triggerDeactivateJogging, idle);
		
		Predicate triggerActivateHome = or(varBtB, and(varGpsA, varGpsB));
		Rule activateHome = afsm.rule("ActivateHome", triggerActivateHome, home);
		
		Predicate triggerDeactivateHome = not(triggerActivateHome);
		Rule deactivateHome = afsm.rule("DeactivateHome", triggerDeactivateHome, idle);
		
		Predicate triggerActivateOffice = or(varBtC, and(varBtD, varGpsA, varGpsC));
		Rule activateOffice = afsm.rule("ActivateOffice", triggerActivateOffice, office);	
		
		Predicate triggerDeactivateOffice = not(triggerActivateOffice);
		Rule deactivateOffice = afsm.rule("DeactivateOffice", triggerDeactivateOffice, idle);
		
		Predicate triggerActivateMeeting = and(varTimeA, varBtE);
		Rule activateMeeting = afsm.rule("ActivateMeeting", triggerActivateMeeting, meeting);
		activateMeeting.setPriority(4);
		
		Rule deactivateMeeting = afsm.rule("DeactivateMeeting", varTimeB, office);
		activateMeeting.setPriority(4);
		
		Rule activateDriving = afsm.rule("ActivateDriving", varBtA, driving);
		activateDriving.setPriority(1);
		
		Rule deactivateDriving = afsm.rule("DeactivateDriving", not(varBtA), idle);
		deactivateDriving.setPriority(1);
		
		Predicate triggerActivateDrivingFast = and(varGpsA, varGpsE);
		Rule activateDrivingFast = afsm.rule("ActivateDrivingFast", triggerActivateDrivingFast, drivingFast);
		activateDrivingFast.setPriority(0);
		
		Predicate triggerDeactivateDrivingFast = not(triggerActivateDrivingFast);
		Rule deactivateDrivingFast = afsm.rule("DeactivateDrivingFast", triggerDeactivateDrivingFast, driving);
		deactivateDrivingFast.setPriority(0);	
		
		Predicate triggerActivateSync = or(varBtB, varBtC);
		Rule activateSynch = afsm.rule("ActivateSynch", triggerActivateSync, synch);
		activateSynch.setPriority(9);
		
		Predicate triggerDeactivateSync = not(triggerActivateSync); // TODO(rax): add a condition sync is over.
		Rule deactivateSynch = afsm.rule("DeactivateSynch", triggerDeactivateSync, idle);
		deactivateSynch.setPriority(9);
		

		// Adding outgoing rules
		idle.outGoingRules.add(activateOutdoor);
		idle.outGoingRules.add(activateDriving);
		idle.outGoingRules.add(activateHome);
		idle.outGoingRules.add(activateOffice);
		idle.outGoingRules.add(activateSynch);
		
        //(Outdoor,{DeactivateOutdoor,ActivateJogging})
		outdoor.outGoingRules.add(deactivateOutdoor);
		outdoor.outGoingRules.add(activateJogging);
		outdoor.outGoingRules.add(activateDriving);
		
        //(Jogging,{DeactivateJogging})
		jogging.outGoingRules.add(deactivateJogging);
		
        //(Driving,{DeactivateDriving,ActivateDrivingFast})
		driving.outGoingRules.add(deactivateDriving);
		driving.outGoingRules.add(activateDrivingFast);
        
		//(DrivingFast,{DeactivateDrivingFast})
		drivingFast.outGoingRules.add(deactivateDrivingFast);
        
		//(Home,{DeactivateHome,ActivateDriving})
		home.outGoingRules.add(deactivateHome);
		home.outGoingRules.add(activateDriving);
        
		//(Office,{DeactivateOffice,ActivateMeeting,ActivateSynch,ActivateDriving})
		office.outGoingRules.add(deactivateOffice);
		office.outGoingRules.add(activateMeeting);
		office.outGoingRules.add(activateDriving);
        
		//(Meeting,{DeactivateMeeting})
		meeting.outGoingRules.add(deactivateMeeting);
        
		//(Synch,{DeactivateSynch})  
		synch.outGoingRules.add(deactivateSynch);
		
		// TODO(rax): we could insert in state conditions and assumptions here
		
		
		return afsm;
	}

}
