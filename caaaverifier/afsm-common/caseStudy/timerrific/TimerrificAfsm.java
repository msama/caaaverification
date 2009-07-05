/**
 * 
 */
package timerrific;

import uk.ac.ucl.cs.afsm.common.AdaptationFiniteStateMachine;
import uk.ac.ucl.cs.afsm.common.AfsmBuilder;
import uk.ac.ucl.cs.afsm.common.Context;
import uk.ac.ucl.cs.afsm.common.Rule;
import uk.ac.ucl.cs.afsm.common.State;
import uk.ac.ucl.cs.afsm.common.predicate.Predicate;
import uk.ac.ucl.cs.afsm.common.predicate.Variable;

import static uk.ac.ucl.cs.afsm.common.Assignment.*;
import static uk.ac.ucl.cs.afsm.common.predicate.Operator.*;

/**
 * @author -RAX- (Michele Sama)
 *
 */
public class TimerrificAfsm implements AfsmBuilder {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TimerrificAfsm().getAdaptationFiniteStateMachine();
	}

	@Override
	public AdaptationFiniteStateMachine getAdaptationFiniteStateMachine() {
		// Create the AFSM
		AdaptationFiniteStateMachine afsm = new AdaptationFiniteStateMachine("Timerrific");

		// Contexts
		Context time = new Context("Time");
		Context ringtone = new Context("Ringtone");
		
		// Variables
		Variable startLesserEqualStop = afsm.variable("StartLesserEqualStop", time); // start <= stop
		Variable beforeStop = afsm.variable("BeforeStop", time); // time < stop_time
		Variable afterEqualStart = afsm.variable("AfterEqualStart", time); // time >= start_time
		Variable soundEnabled = afsm.variable("SoundEnabled", ringtone);
		Variable vibrationEnabled = afsm.variable("VibrationEnabled", ringtone);
		
		// States
		State initial = afsm.state("Initial", true, false);
		State day = afsm.state("Day", false, false);
		State night = afsm.state("Night", false, false);
		
		// Rules
		Predicate isDay  = or(and(startLesserEqualStop, afterEqualStart, beforeStop),
				and(not(startLesserEqualStop), or(beforeStop, afterEqualStart)));
		Predicate isNight = not(isDay);
		
		Rule activateDayMode = afsm.rule("ActivateDayMode", isDay, day,
				satisfy(soundEnabled), negate(vibrationEnabled));
		
		// time < stop_time || time >= start_time
		Rule activateNightMode = afsm.rule("ActivateNightMode", isNight, night,
				satisfy(vibrationEnabled), negate(soundEnabled));
		
		//add rules into states
		
		//(initial,{activateDayMode, activateNightMode})
		initial.outGoingRules.add(activateDayMode);
		initial.outGoingRules.add(activateNightMode);

        //(day,{activateNightMode})
		day.outGoingRules.add(activateNightMode);
		//day.setHoldCondition(day_predicate);
		day.setInStateCondition(isDay);
		day.setInStateAssumption(soundEnabled);

        //(night,{activateDayMode})
		night.outGoingRules.add(activateDayMode);
		night.setInStateCondition(isNight);
		night.setInStateAssumption(not(soundEnabled));
		
		return afsm;
	}

}
