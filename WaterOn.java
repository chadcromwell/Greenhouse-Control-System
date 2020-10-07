/**
* Program: WaterOn.java
* Student ID: #3145381
* Description: WaterOn Event, turns water on
* @author Chad Cromwell
* @date December 20 2015
*/

import java.util.concurrent.*;

/**
* <b>WaterOn Class</b> - Creates WaterOn Event.
*/
public class WaterOn extends Event {

/**
* <b>WaterOn Constructor</b>
* Creates WaterOn Event and SystemState Tuple.
* 
* @param delayTime The delay time for the event.
*/
	public WaterOn(long delayTime) { 
		super(delayTime); //Call parent constructor (Event(delayTime)) and create a new WaterOn Event
			GreenhouseControls.addTuple(new SystemState<String, String>("Water", "On")); //Create and add SystemState Tuple to HashMap
		}

/**
* <b>action() Method</b>
* Prints WaterOn Event, which calls toString override below.
*/
		public void action() {
			System.out.println(this);
		}

/**
* <b>toString() Method</b> - Override.
* returns "Greenhouse water is on".
* @return String
*/
		public String toString() {
			return "Water is on";
		}
}