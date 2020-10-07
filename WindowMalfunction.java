/**
* Program: WindowMalfunction.java
* Student ID: #3145381
* Description: WindowMalfunction Event
* @author Chad Cromwell
* @date December 20 2015
*/

/**
* <b>WindowMalfunction Class</b> - Creates WindowMalfunction Event.
*/
public class WindowMalfunction extends Event {

/**
* <b>WindowMalfunction Constructor</b>
* Creates WindowMalfunction Event and SystemState Tuple.
* 
* @param delayTime The delay time for the event.
*/
	public WindowMalfunction(long delayTime) {
		super(delayTime); //Call parent constructor (Event(delayTime)) and create a new WindowMalfunction Event
		GreenhouseControls.addTuple(new SystemState<String, String>("Windows", "Malfunction")); //Create and add SystemState Tuple to HashMap
	}

/**
* <b>action() Method</b>
* Prints/throws RuntimeException as "Error Code 2: The windows malfunctioned"
*/
	public void action() throws RuntimeException {
		throw new RuntimeException("Error Code 1: The windows malfunctioned", new Throwable());
	}

/**
* <b>toString() Method</b> - Override.
* returns "Error Code 1: Window Malfunction".
* @return String
*/
	public String toString() {
		return "Error Code 1: Window Malfunction";
	}
}