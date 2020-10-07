/**
* Program: Bell.java
* Student ID: #3145381
* Description: Bell Event, rings bell
* @author Chad Cromwell
* @date December 20 2015
*/

import java.util.concurrent.*;

/**
* <b>Bell Class</b> - Creates Bell Event.
*/
public class Bell extends Event {
	 			int rings;

/**
* <b>Bell Constructor</b>
* Creates Bell Event and SystemState Tuple.
* 
* @param delayTime The delay time for the event.
*/
		 public Bell(long delayTime) { 
		 		super(delayTime); //Call parent constructor (Event(delayTime)) and create a new Bell Event
				 GreenhouseControls.addTuple(new SystemState<String, String>("Bell", "Ring")); //Create and add SystemState Tuple to HashMap
		}

/**
* <b>action() Method</b>
* Prints Bell Event, which calls toString override below.
*/	
		public void action() {
				System.out.println(this);
	    }
/**
* <b>toString() Method</b> - Override
* returns "Bing!".
* @return String
*/
	 public String toString() { return "Bing!"; }
}