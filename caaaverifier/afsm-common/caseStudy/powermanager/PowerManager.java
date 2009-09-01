/**
 * 
 */
package powermanager;

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
public class PowerManager implements AfsmBuilder {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new PowerManager().getAdaptationFiniteStateMachine();
	}

	@Override
	public AdaptationFiniteStateMachine getAdaptationFiniteStateMachine() {
		// Create the AFSM
		AdaptationFiniteStateMachine afsm = new AdaptationFiniteStateMachine("PowerManager");

		// Contexts
		Context gps = new Context("Gps");
		Context bt = new Context("BT");
		Context wifi = new Context("WiFi");
		Context backLight = new Context("BackLight");
		Context keypad = new Context("Keypad");
		Context battery = new Context("battery");
		
		// Variables
		Variable gpsEnabled = afsm.variable("GpsEnabled", gps);
		Variable btEnabled = afsm.variable("BtEnabled", bt);
		Variable wifiEnabled = afsm.variable("WiFiEnabled", wifi);
		
		Variable backLightOff = afsm.variable("BackLightOff", backLight);
		Variable backLightLow = afsm.variable("BackLightLow", backLight);
		Variable backLightMedium = afsm.variable("BackLightMedium", backLight);
		Variable backLightFull = afsm.variable("BackLightFull", backLight);
		afsm.constrain(Constrain.createAThenNotB(backLightOff, backLightLow));
		afsm.constrain(Constrain.createAThenNotB(backLightOff, backLightMedium));
		afsm.constrain(Constrain.createAThenNotB(backLightOff, backLightFull));
		afsm.constrain(Constrain.createAThenNotB(backLightLow, backLightOff));
		afsm.constrain(Constrain.createAThenNotB(backLightLow, backLightMedium));
		afsm.constrain(Constrain.createAThenNotB(backLightLow, backLightFull));
		afsm.constrain(Constrain.createAThenNotB(backLightMedium, backLightOff));
		afsm.constrain(Constrain.createAThenNotB(backLightMedium, backLightLow));
		afsm.constrain(Constrain.createAThenNotB(backLightMedium, backLightFull));
		afsm.constrain(Constrain.createAThenNotB(backLightFull, backLightOff));
		afsm.constrain(Constrain.createAThenNotB(backLightFull, backLightLow));
		afsm.constrain(Constrain.createAThenNotB(backLightFull, backLightMedium));
		
		Variable keypadOpen = afsm.variable("KeypadOpen", keypad);
		Variable keypadEnlighted = afsm.variable("KeypadEnlighted", keypad);
		afsm.constrain(Constrain.createNotAThenNotB(keypadOpen, keypadEnlighted));
		
		Variable batteryLow = afsm.variable("BatteryLow", battery);
		Variable batteryMedium = afsm.variable("BatteryMedium", battery);
		Variable batteryFull = afsm.variable("BatteryFull", battery);
		Variable onACCharge = afsm.variable("OnACCharge", battery);
		Variable onUSBCharge = afsm.variable("OnUSBCharge", battery);
		afsm.constrain(Constrain.createAThenNotB(batteryLow, batteryMedium));
		afsm.constrain(Constrain.createAThenNotB(batteryLow, batteryFull));
		afsm.constrain(Constrain.createAThenNotB(batteryLow, onACCharge));
		afsm.constrain(Constrain.createAThenNotB(batteryLow, onUSBCharge));
		afsm.constrain(Constrain.createAThenNotB(batteryMedium, batteryLow));
		afsm.constrain(Constrain.createAThenNotB(batteryMedium, batteryFull));
		afsm.constrain(Constrain.createAThenNotB(batteryMedium, onACCharge));
		afsm.constrain(Constrain.createAThenNotB(batteryMedium, onUSBCharge));
		afsm.constrain(Constrain.createAThenNotB(batteryFull, batteryLow));
		afsm.constrain(Constrain.createAThenNotB(batteryFull, batteryMedium));
		afsm.constrain(Constrain.createAThenNotB(batteryFull, onACCharge));
		afsm.constrain(Constrain.createAThenNotB(batteryFull, onUSBCharge));
		afsm.constrain(Constrain.createAThenNotB(onACCharge, batteryLow));
		afsm.constrain(Constrain.createAThenNotB(onACCharge, batteryMedium));
		afsm.constrain(Constrain.createAThenNotB(onACCharge, batteryFull));
		afsm.constrain(Constrain.createAThenNotB(onACCharge, onUSBCharge));
		afsm.constrain(Constrain.createAThenNotB(onUSBCharge, batteryLow));
		afsm.constrain(Constrain.createAThenNotB(onUSBCharge, batteryMedium));
		afsm.constrain(Constrain.createAThenNotB(onUSBCharge, batteryFull));
		afsm.constrain(Constrain.createAThenNotB(onUSBCharge, onACCharge));
		
		
		// States
		State initial = afsm.state("Initial", true, false);
		State sBatteryLow = afsm.state("BatteryLow", false, false);
		State sBatteryFull = afsm.state("BatteryFull", false, false);
		State sAcCharging = afsm.state("AcCharging", false, false);
		State sUsbCharging = afsm.state("UsbCharging", false, false);
		//State sTyping = afsm.state("Typing", false, false);
		
		// Rules
		Predicate pBatteryLow = and(batteryLow, not(or(onACCharge, onUSBCharge)));
		Predicate pBatteryFull = and(batteryFull, not(or(onACCharge, onUSBCharge)));

		Rule activateBatteryLow = afsm.rule("ActivateBatteryLow", pBatteryLow, sBatteryLow);
		Rule activateBatteryFull = afsm.rule("ActivateBatteryFull", pBatteryLow, sBatteryFull);
		Rule activateChargingUsb = afsm.rule("ActivateChargingUsb", onUSBCharge, sUsbCharging);
		Rule activateChargingAc = afsm.rule("ActivateChargingAc", onACCharge, sAcCharging);
		
		
		//add rules into states
		initial.outGoingRules.add(activateBatteryLow);
		initial.outGoingRules.add(activateBatteryFull);
		initial.outGoingRules.add(activateChargingUsb);
		initial.outGoingRules.add(activateChargingAc);
		
		sBatteryLow.outGoingRules.add(activateBatteryFull);
		sBatteryLow.outGoingRules.add(activateChargingUsb);
		sBatteryLow.outGoingRules.add(activateChargingAc);
		
		sBatteryFull.outGoingRules.add(activateBatteryLow);
		sBatteryFull.outGoingRules.add(activateChargingUsb);
		sBatteryFull.outGoingRules.add(activateChargingAc);
		
		sAcCharging.outGoingRules.add(activateBatteryLow);
		sAcCharging.outGoingRules.add(activateBatteryFull);
		sAcCharging.outGoingRules.add(activateChargingUsb);

		sUsbCharging.outGoingRules.add(activateBatteryLow);
		sUsbCharging.outGoingRules.add(activateBatteryFull);
		sUsbCharging.outGoingRules.add(activateChargingAc);
		
		sBatteryLow.setInStateAssumption(not(or(gpsEnabled, btEnabled, wifiEnabled)));
		sBatteryFull.setInStateAssumption(not(and(gpsEnabled, btEnabled, wifiEnabled)));
		
		return afsm;
	}

}
