/**
 * 
 */
package uk.ac.ucl.cs.afsm.pddl.examples;

import uk.ac.ucl.cs.afsm.pddl.*;
import uk.ac.ucl.cs.afsm.pddl.visitors.DeadRuleViolationVisitor;
import uk.ac.ucl.cs.afsm.pddl.visitors.DeadStateViolationVisitor;
import uk.ac.ucl.cs.afsm.pddl.visitors.NonDeterministicActivationVisitor;
import uk.ac.ucl.cs.afsm.pddl.visitors.PddlGenerator;
import uk.ac.ucl.cs.afsm.pddl.visitors.StateViolationVisitor;

import static uk.ac.ucl.cs.afsm.pddl.Predicates.*;

/**
 * @author rax
 *
 */
public class Timerrific {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Afsm afsm = new Afsm("Timerrific");
		
		// States
		State stateDay = new State("daymode");
		State stateNight = new State("nightmode");
		
		Ringtone r = new Ringtone("r");
		
		stateDay.setInStateCondition(r.isTrue());
		stateNight.setInStateCondition(r.isFalse());
		
		afsm.states.add(stateDay);
		afsm.states.add(stateNight);
		
		// Events
		afsm.events.add(new Timerrific.EventBecomesDay());
		afsm.events.add(new Timerrific.EventBecomesNight());
		
		Event enableSound = new Timerrific.EventEnableSound();
		Event disableSound = new Timerrific.EventDisableSound();
		
		afsm.events.add(enableSound);
		afsm.events.add(disableSound);
		
		enableSound.exclusionList.add(stateNight);
		disableSound.exclusionList.add(stateDay);
		
		// Rules
		afsm.rules.add(new RuleActivateDay(stateDay));
		afsm.rules.add(new RuleActivateNight(stateNight));
		
		// Initial assumptions
		afsm.initialObjects.add(State.S);
		DayTime daytime = new DayTime("t");
		afsm.initialObjects.add(daytime);
		afsm.initialAssumptions.add((VariablePredicate) daytime.isTrue());
		afsm.initialObjects.add(r);
		afsm.initialAssumptions.add((VariablePredicate) r.isTrue());
		afsm.initialObjects.add(new Vibration("v"));

		
		afsm.applyStatesToRulePredicates();
		afsm.applyStatesToEventPredicates();
		
		String folder = "out/timerrific";
		
		AfsmVisitor visitor = new PddlGenerator(folder);
		afsm.accept(visitor);
		
		visitor = new StateViolationVisitor(folder + "/in-state-violation");
		afsm.accept(visitor);
		
		visitor = new DeadStateViolationVisitor(folder + "/dead-states");
		afsm.accept(visitor);
		
		visitor = new DeadRuleViolationVisitor(folder + "/dead-rules");
		afsm.accept(visitor);
		
		visitor = new NonDeterministicActivationVisitor(folder + "/nondeterministic-activations");
		afsm.accept(visitor);
	}

	static class DayTime extends BooleanVariable {

		public DayTime(String name) {
			super("daytime", name);
		}
		
	}
	
	static class Ringtone extends BooleanVariable {

		public Ringtone(String name) {
			super("ringtone", name);
		}
	}
	
	static class Vibration extends BooleanVariable {

		public Vibration(String name) {
			super("vibration", name);
		}
		
	}
	
	// ******************
	// Predicates
	// ******************
	
	
	
	// ******************
	// Events
	// ******************
	
	static class EventBecomesDay extends Event {
		public EventBecomesDay() {
			super("EventBecomesDay");
			DayTime t = new DayTime("t");
			precontion = t.isFalse();
			effect = t.isTrue();
		}	
	}
	
	static class EventBecomesNight extends Event {
		public EventBecomesNight() {
			super("EventBecomesNight");
			DayTime t = new DayTime("t");
			precontion = t.isTrue();
			effect = t.isFalse();
		}	
	}
	
	static class EventEnableSound extends Event {
		public EventEnableSound() {
			super("EventEnableSound");
			Ringtone r = new Ringtone("r");
			precontion = r.isFalse();
			effect = r.isTrue();
		}	
	}
	
	static class EventDisableSound extends Event {
		public EventDisableSound() {
			super("EventDisableSound");
			Ringtone r = new Ringtone("r");
			precontion = r.isTrue();
			effect = r.isFalse();
		}	
	}
	
	static class EventEnableVibration extends Event {
		public EventEnableVibration() {
			super("EventEnableVibration");
			Vibration v = new Vibration("v");
			precontion = v.isFalse();
			effect = v.isTrue();
		}	
	}
	
	static class EventDisableVibration extends Event {
		public EventDisableVibration() {
			super("EventDisableVibration");
			Vibration v = new Vibration("v");
			precontion = v.isTrue();
			effect = v.isFalse();
		}	
	}
	
	// ******************
	// Rules
	// ******************
	
	static class RuleActivateDay extends Rule {
		public RuleActivateDay(State destination) {
			super("RuleActivateDay", State.any(), destination);
			DayTime t = new DayTime("t");
			Ringtone s = new Ringtone("r");
			Vibration v = new Vibration("v");
			precontion = t.isTrue();
			// Bug existing in the code represented by this model
			effect = and(s.isFalse(), v.isFalse()); 
			// Correct
			//effect = and(s.isTrue(), v.isTrue());
			
		}	
	}
	
	static class RuleActivateNight extends Rule {
		public RuleActivateNight(State destination) {
			super("RuleActivateNight", State.any(), destination);
			DayTime t = new DayTime("t");
			Ringtone s = new Ringtone("r");
			Vibration v = new Vibration("v");
			precontion = t.isFalse();
			// Bug existing in the code represented by this model
			effect = and(s.isTrue(), v.isTrue());
			// Correct
			// effect = and(s.isFalse(), v.isFalse()); 
		}	
	}
}
