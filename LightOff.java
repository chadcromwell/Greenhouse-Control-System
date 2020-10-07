/**
* Program: LightOff.java
* Student ID: #3145381
* Description: LightOff Event, turns light off
* @author Chad Cromwell
* @date December 20 2015
*/

import java.util.concurrent.*;

/**
* <b>LightOff Class</b> - Creates LightOff Event.
*/
public class LightOff extends Event {

/**
* <b>LightOff Constructor</b>
* Creates LightOff Event and SystemState Tuple.
* 
* @param delayTime The delay time for the event.
*/
	public LightOff(long delayTime) { 
	 	super(delayTime); //Call parent constructor (Event(delayTime)) and create a new LightOff Event
	  	GreenhouseControls.addTuple(new SystemState<String, String>("Lights", "Off")); //Create and add SystemState Tuple to HashMap
	}

/**
* <b>action() Method</b>
* Prints LightOff Event, which calls toString override below.
*/
	public void action() {
		System.out.println(this);
	}

/**
* <b>toString() Method</b> - Override.
* returns "Light is off".
* @return String
*/
	public String toString() {
		return "Light is off";
	}
}