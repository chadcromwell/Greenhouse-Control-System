/**
* Program: LightOn.java
* Student ID: #3145381
* Description: LightOn Event, turns light on
* @author Chad Cromwell
* @date December 20 2015
*/

import java.util.concurrent.*;

/**
* <b>LightOn Class</b> - Creates LightOn Event.
*/
public class LightOn extends Event {

/**
* <b>LightOn Constructor</b>
* Creates LightOn Event and SystemState Tuple.
* 
* @param delayTime The delay time for the event.
*/
	public LightOn(long delayTime) { 
	  	super(delayTime); //Call parent constructor (Event(delayTime)) and create a new LightOn Event
	   	GreenhouseControls.addTuple(new SystemState<String, String>("Lights", "On")); //Create and add SystemState Tuple to HashMap
	}

/**
* <b>action() Method</b>
* Prints LightOn Event, which calls toString override below.
*/
	public void action() {
		System.out.println(this);
	}

/**
* <b>toString() Method</b> - Override.
* returns "Light is on".
* @return String
*/
	public String toString() {
		return "Light is on";
	}
}