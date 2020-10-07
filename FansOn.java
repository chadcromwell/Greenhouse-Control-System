/**
* Program: FansOn.java
* Student ID: #3145381
* Description: FansOn Event, turns water on
* @author Chad Cromwell
* @date December 20 2015
*/

import java.util.concurrent.*;

/**
* <b>FansOn Class</b> - Creates FansOn Event.
*/
public class FansOn extends Event {

/**
* <b>FansOn Constructor</b>
* Creates FansOn Event and SystemState Tuple.
* 
* @param delayTime The delay time for the event.
*/
	public FansOn(long delayTime) { 
	 	super(delayTime); //Call parent constructor (Event(delayTime)) and create a new FansOn Event
	 	GreenhouseControls.addTuple(new SystemState<String, String>("Fans", "On")); //Create and add SystemState Tuple to HashMap
	}

/**
* <b>action() Method</b>
* Prints FansOn Event, which calls toString override below.
*/
	public void action() { 
		System.out.println(this); 
	}

/**
* <b>toString() Method</b> - Override.
* returns "Fans are On".
* @return String
*/
	public String toString() { 
		return "Fans are On"; 
	}
}