/**
* Program: FansOff.java
* Student ID: #3145381
* Description: FansOff Event, turns fans off
* @author Chad Cromwell
* @date December 20 2015
*/

import java.util.concurrent.*;

/**
* <b>FansOff Class</b> - Creates FansOff Event.
*/
public class FansOff extends Event {

/**
* <b>FansOff Constructor</b>
* Creates FansOff Event and SystemState Tuple.
* 
* @param delayTime The delay time for the event.
*/
	public FansOff(long delayTime) { 
	 	super(delayTime); //Call parent constructor (Event(delayTime)) and create a new FansOff Event
		GreenhouseControls.addTuple(new SystemState<String, String>("Fans", "Off")); //Create and add SystemState Tuple to HashMap
	}

/**
* <b>action() Method</b>
* Prints FansOff Event, which calls toString override below.
*/
	public void action() { 
		System.out.println(this); 
	}

/**
* <b>toString() Method</b> - Override
* returns "Fans are Off".
* @return String
*/
	public String toString() { 
		return "Fans are Off"; 
	}
}