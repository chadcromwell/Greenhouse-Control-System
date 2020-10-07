/**
* Program: PowerOut.java
* Student ID: #3145381
* Description: PowerOut Event
* @author Chad Cromwell
* @date December 20 2015
*/

/**
* <b>PowerOut Class</b> - Creates PowerOut Event
*/
public class PowerOut extends Event {

/**
* <b>PowerOut Constructor</b>
* Creates PowerOut Event and SystemState Tuple.
* 
* @param delayTime The delay time for the event.
*/
  public PowerOut(long delayTime) { 
	  super(delayTime); //Call parent constructor (Event(delayTime)) and create a new PowerOut Event
	  GreenhouseControls.addTuple(new SystemState<String, String>("Power", "Out")); //Create and add SystemState Tuple to HashMap
  }
  
/**
* <b>action() Method</b>
* Prints/throws RuntimeException as "Error Code 2: There was a power outage".
*/
	public void action() throws RuntimeException {
		throw new RuntimeException("Error Code 2: There was a power outage", new Throwable());
	}

/**
* <b>toString() Method</b> - Override.
* returns "Error Code 2: Power Outage".
* @return String
*/
	public String toString() { 
		return "Error Code 2: Power Outage";
	}
}
    
    