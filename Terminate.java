/**
* Program: Terminate.java
* Student ID: #3145381
* Purpose: To stop the execution of all currently running Threads and shut the program down normally
* Description: Terminate Event
* Based off of Bruce Eckels GreenhouseScheduler.java/Terminate.class and Matt Atwood's Terminate.java
* @author Chad Cromwell
* @date December 20 2015
*/

import java.util.concurrent.*;

/**
* <b>Terminate Class</b> - Terminates the Events that are currently running and shuts down the system normally.
*/
public class Terminate extends Event {

/**
* <b>Terminate Constructor</b>
* Stops execution and shuts system down.
* Creates a Terminate Event, SystemState Tuple, stops execution, returns shudown reason.
*
* @param delayTime The delay time for the event.
*/
	    public Terminate(long delayTime) { 
	    	super(delayTime); //Call parent constructor (Event(delayTime)) and create a new Event
			GreenhouseControls.addTuple(new SystemState<String, String>("Terminate", "System Terminated")); //Create and add SystemState Tuple to HashMap
	    }

/**
* <b>action() Method</b>
* Stops execution and prints/throws RuntimeException as "Error Code 0: The system is shutting down normally".
*/
	    public void action() {
	    	Event.stopExecution();
			System.out.println(this);
	    	throw new RuntimeException("Error Code 0: The system is shutting down normally", new Throwable("Terminate"));
	    }

/**
* <b>toString() Method</b>
* Returns "Terminating...".
* @return String
*/	    
	    public String toString() { 
			return "Terminating...";
		}		
}