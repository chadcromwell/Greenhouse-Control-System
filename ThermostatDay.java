/**
* Program: ThermostatDay.java
* Student ID: #3145381
* Description: ThermostatDay Event, sets thermostat to day
* @author Chad Cromwell
* @date December 20 2015
*/

import java.util.concurrent.*;

/**
* <b>ThermostatDay Class</b> - Creates ThermostatDay Event.
*/
public class ThermostatDay extends Event {

/**
* <b>ThermostatDay Constructor</b>
* Creates ThermostatDay Event and SystemState Tuple.
* 
* @param delayTime The delay time for the event.
*/
	    public ThermostatDay(long delayTime) {
	      super(delayTime); //Call parent constructor (Event(delayTime)) and create a new ThermostatDay Event
	      GreenhouseControls.addTuple(new SystemState<String, String>("Thermostat", "Day")); //Create and add SystemState Tuple to HashMap
	    }

/**
* <b>action() Method</b>
* Prints ThermostatDay Event, which calls toString override below.
*/
	    public void action() {
			System.out.println(this);
		}

/**
* <b>toString() Method</b> - Override.
* returns "Thermostat on day setting".
* @return String
*/
	    public String toString() {
			return "Thermostat on day setting";
		}
	       	
	     
}