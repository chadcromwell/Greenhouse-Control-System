/**
* Program: Event.java
* Student ID: #3145381
* Purpose: Run events as threads and control them
* Description: Provides methods for events to be ran as threads
* Based off of Bruce Eckels GreenhouseControls.java, which was modified by Steve Leung and Matt Atwood
* Known issues: Some concurrency issues where threads keep running after they've been called to wait.
* @author Chad Cromwell
* @date December 20 2015
*/


import java.io.*;
import java.util.concurrent.*;

/**
* <b>Event Class</b> - Provides methods to control the execution of Events as Threads
*/
public abstract class Event implements Serializable, Runnable {
	transient SystemState state; //SystemState Tuple, transient so it doesn't get serialized
	private long delayTime; //Hold delaytime
	private long startTime; //Hold startTime
	private long executionTime; //Hold executionTime
	private long runTime = 0; //Hold runTime
	private static long suspendTime; //Hold suspendTime
	private static boolean stopAll = false; //Boolean to flag if run should be stopped or not
	private boolean attemptAction = false; //Boolean to flag if action was attempted yet or not
	private boolean suspended = false; //Boolean to flag if suspended is called, causes wait() to take place below

/**
* <b>Event Default Constructor</b>
* Allows for initialization of empty Events
*/
	public Event() { //Default constructor
		delayTime = 0;
	}

/**
* <b>Event Constructor</b>
* Creates an Event object.
* Captures a delayTime to calculate when to run the Threads.
* Calculates executionTime and the time the Event started.
* @param delayTime  The Event objects delayTime
*/
	public Event(long delayTime) { //Constructor
		this.delayTime = delayTime; //Set delayTime
    	executionTime = System.currentTimeMillis() + delayTime; //Reset time to execute
    	startTime =  System.currentTimeMillis(); //Set time the event started
  	}

/**
* <b>resumeEvent() Method</b>
* Resumes Events from suspension.
* Calculates the execution time, sets suspended to false, and uses synchronized() to notify() all threads to start.
*/
	public void resumeEvent() {
		try{
			executionTime = System.currentTimeMillis() + (delayTime - runTime); //Calculate time to execute
			suspended = false; //Suspended to false

			synchronized(Event.this){ //Synchronized
				notify(); //Start threads again
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
  	
/**
* <b>suspendEvent() Method</b>
* Suspends Events.
* Calculates the time it was suspended at and assigns it to suspendTime.
* Calculates the run time and assigns it to runTime.
* Sets suspended to true.
*/
	public void suspendEvent(){
		suspendTime = System.currentTimeMillis(); //Calculate suspend time
		runTime = suspendTime - startTime; //Calculate run time
		suspended = true; //Suspend threads
	}  

/**
* <b>stopExecution() Method</b>
* Stops Events from executing.
* Sets stopAll to true.
*/
	public static void stopExecution(){
		stopAll = true; //Set stopAll to true, which breaks out of run below
	}

/**
* <b>allowExecution() Method</b>
* Allows Events to be executed.
* Sets stopAll to false.
*/
	public static void allowExecution(){
		stopAll = false; //Sets stopAll to false, allows run to take place
	}

/**
* <b>restartEvent() Method</b>
* Resets executionTime and startTime.
* Sets stopAll to false.
* Calculates and assigns executionTime and startTime.
*/
  	public void restartEvent(){
  		stopAll = false;
  		executionTime = System.currentTimeMillis() + delayTime;
    	startTime =  System.currentTimeMillis();
  	}

/**
* <b>setAttempt() Method</b>
* Sets attemptAction to true
*/
	  public void setAttempt(){
	  		attemptAction = true;
	  }
/**
* <b>getState() Method</b>
* Returns the state from a SystemState Tuple
* @return SystemState Tuple's state
*/	  
	  //Returns the state tuple of the event.
	  public SystemState getState(){
	  	return state;
	  }

/**
* <b>getAttempt() Method</b>
* Returns an Events attemptAction
* Used to check if an Event has attempted to run yet or not
* @return Boolean attemptAction
*/
	public boolean getAttempt(){
		return attemptAction;
	}

/**
* <b>action() Default Method</b> - Needs to be declared
* @throws 	Exception
*			if error occurs Exception is thrown
* @see		java.lang.Exception#Exception()
*/
	public abstract void action() throws Exception;
 
/**
* <b>run() Method</b>
* Controls the execution of the event
* While system's current time is before the Event's execution time, run.
* If stopAll is true, then stop running.
* If suspended is true, then synchronize() wait() all threads
*/
  	public void run() {
  	
  	//Controls the execution of the event
  	while(System.currentTimeMillis() < executionTime || stopAll == true || suspended == true){
			if(stopAll == true) {//If stopAll is true, stop running
				break;
			}
			
  			if(suspended == true){ //If it has been suspended
  				try{
  					synchronized(Event.this){
  						wait(); //Cause threads to wait
  					}  			
  				}
				catch(Exception ex){
					System.out.println(ex.toString());
					System.out.println("run error");
				}
  			}
  	}

	try {
  		if(stopAll == false){
  			setAttempt(); //Set attemptAction to true
  			action(); //Call the action
  		}		
	} catch (Exception e) {
		throw new RuntimeException(e.toString());
	}
  };
}