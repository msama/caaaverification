/**
 * 
 */
package falsePositive;

import uk.ac.ucl.cs.afsm.common.AdaptationFiniteStateMachine;
import uk.ac.ucl.cs.afsm.common.AfsmBuilder;
import uk.ac.ucl.cs.afsm.common.Context;
import uk.ac.ucl.cs.afsm.common.Rule;
import uk.ac.ucl.cs.afsm.common.State;
import uk.ac.ucl.cs.afsm.common.predicate.Constrain;
import uk.ac.ucl.cs.afsm.common.predicate.Predicate;
import uk.ac.ucl.cs.afsm.common.predicate.Variable;

import static uk.ac.ucl.cs.afsm.common.Assignment.*;
import static uk.ac.ucl.cs.afsm.common.predicate.Operator.*;

/**
 * @author -RAX- (Michele Sama)
 *
 */
public class GPSAFSM implements AfsmBuilder {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new GPSAFSM().getAdaptationFiniteStateMachine();
	}

	@Override
	public AdaptationFiniteStateMachine getAdaptationFiniteStateMachine() {
		// Create the AFSM
		AdaptationFiniteStateMachine afsm = new AdaptationFiniteStateMachine("FalsePositiveGPS");

		// Contexts
		Context gps = new Context("Gps");
		
		// Variables
		Variable gpsEnabled = afsm.variable("GpsEnabled", gps);
		Variable gpsHome = afsm.variable("GpsHome", gps);
		Variable gpsEnd = afsm.variable("GpsEnd", gps);
		afsm.constrain(Constrain.createNotAThenNotB(gpsEnabled, gpsHome));
		
		// States
		State initial = afsm.state("Initial", true, false);
		State recording = afsm.state("Recording", false, false);
		State home = afsm.state("Home", false, false);
		State end = afsm.state("End", false, true);
		
		// Rules
		Predicate canRecord = and(gpsEnabled, not(gpsHome));
		Predicate atHome = and(gpsEnabled, gpsHome);
		Predicate atEnd = and(gpsEnabled, gpsEnd);
		
		Rule activateRecording = afsm.rule("ActivateRecording", canRecord, recording);
		//Rule deactivateRecording = afsm.rule("DeactivateRecording", not(canRecord), initial);
		
		Rule activateHome = afsm.rule("ActivateHome", atHome, home);
		Rule startVisit = afsm.rule("StartVisit", canRecord, recording);
		Rule endVisit = afsm.rule("EndVisit", atEnd, end);
		
		//add rules into states
		
		//(initial,{activateDayMode, activateNightMode})
		initial.outGoingRules.add(activateRecording);
		initial.outGoingRules.add(activateHome);
		
		//recording.outGoingRules.add(deactivateRecording);
		home.outGoingRules.add(startVisit);
		recording.outGoingRules.add(endVisit);

		recording.setInStateAssumption(not(atHome));
		
		return afsm;
	}

}
