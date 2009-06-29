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
public class PowerManager {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Afsm afsm = new Afsm("PowerManager");
		
		// States
		State stateBattery = new State("battery-high");
		State stateLowBattery = new State("battery-low");
		State stateRecharging = new State("recharging-ac");
		State stateUsbRecharging = new State("recharging-usb");
		State stateKeyboard = new State("keyboard-open");
		
		afsm.states.add(stateBattery);
		afsm.states.add(stateLowBattery);
		afsm.states.add(stateRecharging);
		afsm.states.add(stateUsbRecharging);
		afsm.states.add(stateKeyboard);
		
		WiFi varWifi = new WiFi("var_wifi");
		Gps varGps = new Gps("var_gps");
		Bluetooth varBluetooth = new Bluetooth("var_bluetooth");
		CellLocation varCellLocation = new CellLocation("var_cell_location");
		BatteryLow varBatteryLow = new BatteryLow("var_battery_low");
		RechargingAC varRechargingAc = new RechargingAC("var_recharging_ac");
		RechargingUSB varRechargingUSB = new RechargingUSB("var_recharging_usb");
		StayAwakeWhileCalling varAwakeWhileCalling = new StayAwakeWhileCalling("var_awake_while_calling");
		StayAwakeWhileKeyboard varAwakeWhileKeyboard = new StayAwakeWhileKeyboard("var-awake-while-keyboard-open");
		KeyboardOpen varKeyboardOpen = new KeyboardOpen("var_keyboard_open");
		
		//Preconditions
		// The battery cannot become low while recharging
		varBatteryLow.setTrue().andPrecontion(and(varRechargingAc.isFalse(), varRechargingUSB.isFalse()));
		// The battery can become high only if the device is recharging
		varBatteryLow.setFalse().andPrecontion(or(varRechargingAc.isTrue(), varRechargingUSB.isTrue()));
		// USB and AC power cannot be active together
		varRechargingAc.setTrue().andPrecontion(varRechargingUSB.isFalse());
		varRechargingUSB.setTrue().andPrecontion(varRechargingAc.isFalse());
		
		// If we want to enable external events
		//afsm.events.add(varWifi.setTrue());
		//afsm.events.add(varWifi.setFalse());
		//afsm.events.add(varGps.setTrue());
		//afsm.events.add(varGps.setFalse());
		//afsm.events.add(varBluetooth.setTrue());
		//afsm.events.add(varBluetooth.setFalse());
		//afsm.events.add(varCellLocation.setTrue());
		//afsm.events.add(varCellLocation.setFalse());
		afsm.events.add(varBatteryLow.setTrue());
		afsm.events.add(varBatteryLow.setFalse());
		afsm.events.add(varRechargingAc.setTrue());
		afsm.events.add(varRechargingAc.setFalse());
		afsm.events.add(varRechargingUSB.setTrue());
		afsm.events.add(varRechargingUSB.setFalse());
		//afsm.events.add(varAwakeWhileCalling.setTrue());
		//afsm.events.add(varAwakeWhileCalling.setFalse());
		//afsm.events.add(varAwakeWhileKeyboard.setTrue());
		//afsm.events.add(varAwakeWhileKeyboard.setFalse());
		afsm.events.add(varKeyboardOpen.setTrue());
		afsm.events.add(varKeyboardOpen.setFalse());
		
		// State conditions
		// In battery high not all the devices can be on simultaneously
		stateBattery.setInStateCondition(not(and(varWifi.isTrue(), varGps.isTrue(), varBluetooth.isTrue())));
		// In battery low none of the devices can be on
		stateLowBattery.setInStateCondition(not(or(varWifi.isTrue(), varGps.isTrue(), varBluetooth.isTrue())));
		stateRecharging.setInStateCondition(and(varWifi.isTrue(), varGps.isTrue(), varBluetooth.isTrue()));
		stateUsbRecharging.setInStateCondition(and(varWifi.isTrue(), varGps.isTrue(), varBluetooth.isTrue()));
		stateKeyboard.setInStateCondition(varAwakeWhileKeyboard.isTrue());
		
		// Rules
		afsm.rules.add(new RuleActivateBattery(stateBattery, varBatteryLow, varRechargingAc, varRechargingUSB));
		afsm.rules.add(new RuleActivateBatteryLow(stateLowBattery, varBatteryLow, varRechargingAc, varRechargingUSB, varWifi, varGps, varBluetooth, varCellLocation, varAwakeWhileKeyboard));
		afsm.rules.add(new RuleActivateRechargingAC(stateRecharging, varRechargingAc, varWifi, varGps, varBluetooth, varCellLocation));
		afsm.rules.add(new RuleActivateRechargingUSB(stateUsbRecharging, varRechargingUSB, varWifi, varGps, varBluetooth, varCellLocation));
		afsm.rules.add(new RuleActivateKeyboardMode(stateKeyboard, varKeyboardOpen, varAwakeWhileKeyboard));
		
		// Initial assumptions
		afsm.initialObjects.add(State.S);
		afsm.initialObjects.add(varWifi);
		afsm.initialObjects.add(varGps);
		afsm.initialObjects.add(varBluetooth);
		afsm.initialObjects.add(varCellLocation);
		afsm.initialObjects.add(varBatteryLow);
		afsm.initialObjects.add(varRechargingAc);
		afsm.initialObjects.add(varRechargingUSB);
		afsm.initialObjects.add(varAwakeWhileCalling);
		afsm.initialObjects.add(varAwakeWhileKeyboard);
		afsm.initialObjects.add(varKeyboardOpen);
		afsm.initialAssumptions.add((VariablePredicate) varAwakeWhileKeyboard.isTrue());
		//afsm.initialAssumptions.add((VariablePredicate) varBatteryLow.isTrue());
		//afsm.initialAssumptions.add((VariablePredicate) varRechargingAc.isFalse());
		//afsm.initialAssumptions.add((VariablePredicate) varRechargingUSB.isFalse());
		
		afsm.applyStatesToRulePredicates();
		afsm.applyStatesToEventPredicates();
		
		String folder = "out/powermanager";
		
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

	static class WiFi extends BooleanVariable {
		public WiFi(String name) {
			super("wifi", name);
		}
	}
	
	static class Gps extends BooleanVariable {
		public Gps(String name) {
			super("gps", name);
		}
	}
	
	static class Bluetooth extends BooleanVariable {
		public Bluetooth(String name) {
			super("bluetooth", name);
		}
	}

	static class CellLocation extends BooleanVariable {
		public CellLocation(String name) {
			super("cell-location", name);
		}
	}
	
	static class BatteryLow extends BooleanVariable {
		public BatteryLow(String name) {
			super("battery-low", name);
		}
	}
	
	static class RechargingAC extends BooleanVariable {
		public RechargingAC(String name) {
			super("recharging-ac", name);
		}
	}
	
	static class RechargingUSB extends BooleanVariable {
		public RechargingUSB(String name) {
			super("recharging-usb", name);
		}
	}
	
	static class KeyboardOpen extends BooleanVariable {
		public KeyboardOpen(String name) {
			super("keyboard-open", name);
		}
	}
	
	static class StayAwakeWhileCalling extends BooleanVariable {
		public StayAwakeWhileCalling(String name) {
			super("stay-awake-while-calling", name);
		}
	}
	
	static class StayAwakeWhileKeyboard extends BooleanVariable {
		public StayAwakeWhileKeyboard(String name) {
			super("stay-awake-while-keyboard", name);
		}
	}
	
	static class AutoSync extends BooleanVariable {
		public AutoSync(String name) {
			super("auto-sync", name);
		}
	}
	
	static class Ringtone extends Variable {
		public Ringtone(String name) {
			super("ringtone", name);
		}
	}
	
	static class DisplayTimeout extends Variable {
		public DisplayTimeout(String name) {
			super("display-timeout", name);
		}
	}
	
	static class DisplayBrightness extends Variable {
		public DisplayBrightness(String name) {
			super("display-brightness", name);
		}
	}
	
	
	// ******************
	// Rules
	// ******************
	static class RuleActivateBattery extends Rule {
		public RuleActivateBattery(State destination, BatteryLow battery, RechargingAC ac, RechargingUSB usb) {
			super("Rule_Activate_Battery", State.any(), destination);
			precontion = and(battery.isFalse(), ac.isFalse(), usb.isFalse());
			effect = and(); 	
		}	
	}
	
	// Assume that we fill forget about gps
	static class RuleActivateBatteryLow extends Rule {
		public RuleActivateBatteryLow(State destination, BatteryLow battery, RechargingAC ac, RechargingUSB usb, WiFi wifi, Gps gps, Bluetooth bt, CellLocation cell, StayAwakeWhileKeyboard awake) {
			super("Rule_Activate_Battery_Low", State.any(), destination);
			precontion = and(battery.isTrue(), ac.isFalse(), usb.isFalse());
			effect = and(wifi.isFalse(), /*gps.isFalse(),*/ bt.isFalse(), cell.isFalse(), awake.isFalse()); 	
		}	
	}
	
	static class RuleActivateRechargingAC extends Rule {
		public RuleActivateRechargingAC(State destination, RechargingAC ac, WiFi wifi, Gps gps, Bluetooth bt, CellLocation cell) {
			super("Rule_Activate_Recharging_AC", State.any(), destination);
			precontion = ac.isTrue();
			effect = and(wifi.isTrue(), gps.isTrue(), bt.isTrue(), cell.isTrue()); 	
		}	
	}
	
	static class RuleActivateRechargingUSB extends Rule {
		public RuleActivateRechargingUSB(State destination, RechargingUSB usb, WiFi wifi, Gps gps, Bluetooth bt, CellLocation cell) {
			super("Rule_Activate_Recharging_USB", State.any(), destination);
			precontion = usb.isTrue();
			effect = and(wifi.isTrue(), gps.isTrue(), bt.isTrue(), cell.isTrue()); 	
		}	
	}
	
	static class RuleActivateKeyboardMode extends Rule {
		public RuleActivateKeyboardMode(State destination, KeyboardOpen keyboard, StayAwakeWhileKeyboard awake) {
			super("Rule_Activate_Keyboard_Mode", State.any(), destination);
			precontion = keyboard.isTrue();
			effect = and(awake.isTrue()); 	
		}	
	}
}
