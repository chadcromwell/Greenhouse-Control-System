/**
* Program: ThermostatNight.java
* Student ID: #3145381
* Description: ThermostatNight Event, sets thermostat to night
* @author Chad Cromwell
* @date December 20 2015
*/

import java.util.concurrent.*;

/**
* <b>ThermostatNight Class</b> - Creates ThermostatNight Event.
*/
public class ThermostatNight extends Event {

/**
* <b>ThermostatNight Constructor</b>
* Creates ThermostatNight Event and SystemState Tuple.
* 
* @param delayTime The delay time for the event.
*/
	    public ThermostatNight(long delayTime) {
	      super(delayTime); //Call parent constructor (Event(delayTime)) and create a new ThermostatNight Event
	      GreenhouseControls.addTuple(new SystemState<String, String>("Thermostat", "Night")); //Create and add SystemState Tuple to HashMap
	    }

/**
* <b>action() Method</b>
* Prints ThermostatNight Event, which calls toString override below.
*/
	    public void action() {
			System.out.println(this);
		}

/**
* <b>toString() Method</b> - Override.
* returns "Thermostat on night setting".
* @return String
*/
	    public String toString() {
			return "Thermostat on night setting";
		}
	       	
	     
}