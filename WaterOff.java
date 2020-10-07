/**
* Program: WaterOff.java
* Student ID: #3145381
* Description: WaterOff Event, turns water off
* @author Chad Cromwell
* @date December 20 2015
*/

import java.util.concurrent.*;

/**
* <b>WaterOff Class</b> - Creates WaterOff Event.
*/
public class WaterOff extends Event {

/**
* <b>WaterOff Constructor</b>
* Creates WaterOff Event and SystemState Tuple.
* 
* @param delayTime The delay time for the event.
*/
	    public WaterOff(long delayTime) { 
	    	super(delayTime); //Call parent constructor (Event(delayTime)) and create a new WaterOff Event
	    	GreenhouseControls.addTuple(new SystemState<String, String>("Water", "Off")); //Create and add SystemState Tuple to HashMap
	     }

/**
* <b>action() Method</b>
* Prints WaterOff Event, which calls toString override below.
*/
	    public void action() {
			System.out.println(this);
		}

/**
* <b>toString() Method</b> - Override.
* returns "Greenhouse water is off".
* @return String
*/
	    public String toString() {
			return "Water is off";
		}
}