/**
* Program: GreenhouseControls.java
* Student ID: #3145381
* Purpose: To control a greenhouse and it's various components
* Description: This class provides functionality to controls various components within a Greenhouse. It also handles various random events such as power outages and windows malfunctioning. If one of these events occurs, 
* 			   it will serialize the GreenhouseControls object so that you can deserialize it later and continue from where you left off.
* Based off of Bruce Eckels and Matt Atwood's versions of GreenhouseControls.java
* Known issues: Everything works fine the first time, when trying to suspend or resume when running events for a second time, it will not work. Need to find solution.
*			   -New window output goes to original window. Need to find solution.
*			   -Intermittent issues where some threads keep running even though they've all been set to wait. Need to find solution. Note: due to timing calculations in Events?
* @author Chad Cromwell
* @date December 20 2015
*/

import java.util.*;
import java.io.*;
import java.lang.reflect.*;
import java.lang.*;

/**
* <b>GreenhouseControls Class</b> - Controls the Greenhouse and contains the entry point for the application.
*/
public class GreenhouseControls implements Serializable {
	private int errorCode = 0; // Holds errorCode for use in exception and shutdown
	private ArrayList<Event> gcEvents = new ArrayList<Event>(); //ArrayList holds Events
	transient private ArrayList<Thread> gc = new ArrayList<Thread>(); //ArrayList of Threads, transient so it's not serialized
	transient private GreenhouseGUI gui; //GUI, transient so it is now serialized
	transient static private HashMap<String, String> gcStates = new HashMap<String, String>(); //ArrayList to hold each Event and what it did
  
/**
* <b>Main</b>
* @param args  Command line arguments
* @throws 	Exception
* 			if error occurs Exception is thrown and StackTrace is printed
* @see		java.lang.Exception#Exception()
*/
	public static void main(String[] args) throws Exception {
		try {
		 new GreenhouseControls();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

/** 
* <b>GreenhouseControls Constructor</b>
* Loads the GUI, sends output to textArea in GUI through GUIOutput's .setOut() method
* and assigns a custom exception handler for Threads.
*/
	public GreenhouseControls() {
		try {
			gui = new GreenhouseGUI(this); //Load GUI
			System.setOut(new GUIOutput(gui)); //Sends output to textArea through setOut
		}
		catch(Exception e){
			System.out.println("Can't create GUI");
		}
		
		Thread.setDefaultUncaughtExceptionHandler(new GreenhouseControls.threadException()); //Assign the default Thread exception handler
	}

/**
* <b>addTuple() Method</b>
* Takes SystemState Tuple and adds it to the HashMap to save the Tuple's hardware as the key and the action it performed.
* Example: Key = "Power", Value = "Out".
* 
* @param state  The tuple you wish to add to the HashhMap.
* @see #makeSystemState(String, Event)
*/
	public static synchronized void addTuple(SystemState state) {
		gcStates.put((String)state.hardware, (String)state.state);
	}

/**
* <b>makeSystemState() Method</b>
* Takes a String and Event object to create a SystemState Tuple and adds it to gcState HashMap by calling addTuple() method.
* Example: makeSystemState("Bell", Bell) would create a SystemState Tuple (Bell, ring) and add it to the HashMap as Key = Bell, Value = Ring.
* Not used anymore within the program, Tuples are added directly from within event classes, however this can be used during restart()
* to create and add Tuples to the HashMap that have been dynamically created.
* IMPORTANT: If you choose to use this for Dynamic Events, be sure to call gcStates.clear() in shutdown or HashMap will continue to fill with duplicates
*
* @param s  The name of the class/event as a string.
* @param e  The event object itself.
* @see #addTuple(SystemState)
*/
	public void makeSystemState(String s, Event e){
		String event = s;
		Event ev = e;
			SystemState<String, String> ss = new SystemState<String, String>(event, ev.toString());
				addTuple(ss); //Add the tuple to the gcStates HashMap
	}

/**
* <b>getSystemState() Method</b>
* Takes a String, uses it as a Key and iterates through the gcStates HashMap to print the corresponding value
* Example: getSystemState("Bell") would iterate through the HashMap looking for the Key "Bell" and print it's findings as:
* "Event: Bell - Action performed: Ring"
*
* @param s  The Key you wish to use to iterate through the HashMap
*/
	public void getSystemState(String s) {
		Iterator<Map.Entry<String, String>> iterator = gcStates.entrySet().iterator(); //Iterator for HashMap
		while(iterator.hasNext()) { //Iterate through HashMap
			Map.Entry<String, String> entry = iterator.next(); //Assign current entry to entry
			if(entry.getKey() == s) { //Compare current entry Key to parameter, if it matches
				System.out.println("Event : " + entry.getKey() + " - Action performed: " +entry.getValue()); //Print the Key and Value in formatted text
			}
		}
	}

/**
* <b>gcSuspend() Method</b>
* Suspends Events
* Iterates through all Event objects in gcEvents and calls suspendEvent() method on them all
*
* @see Event#suspendEvent()
*/
	public void gcSuspend() {
		for(Event ev : gcEvents) {
			ev.suspendEvent(); //Call suspendEvent method
		}
	}

/**
* <b>gcResume() Method</b>
* Resumes Events
* Iterates through all Event objects in gcEvents and calls resumeEvent() method on them all
*
* @see Event#resumeEvent()
*/
	public void gcResume() {
		for(Event ev : gcEvents) {
			ev.resumeEvent(); //Call resumeEvent method
		}
	}

/**
* <b>gcRunning() Method</b>
* Checks if Threads are running
* Iterates through all Threads in gc and calls isAlive() method on them all, if true then it returns true otherwise it returns false
*
* @return boolean.
* @see java.lang.Thread#isAlive()
*/
	public boolean gcRunning() {
		for(Thread t : gc){ //For every thread in GC
			if(t.isAlive()) //If isAlive returns true, then
				return true; //Return true
		}
		return false; //Else return false
	}

/**
* <b>terminate() Method</b>
* Terminates system normally
* Threads a new instance of Terminate() which proceeds to stop all threads and shuts down the system normally.
*
* @param delay  How long to wait until terminate is run.
* @see Terminate#Terminate(long)
*/
	public void terminate(int delay) {
		Thread t = new Thread(new Terminate(delay)); //Create new Thread of Terminate
		t.start(); //Run Thread
	}

/**
* <b>shutdown() Method</b>
* Shuts the system down when an error takes place
* Serializes gc to dump.out and logs the error that took place to error.log
* Prints to textArea what problem occured.
* Example: 
* "The power went out on Wed Dec 30 15:15:00 EST 2015 (Error Code: 2)"
* 
* @param message  The error message given by error.
*/
	public void shutdown(String message) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("error.log")); //Write out to file error.log
			out.write(message + " on " + new Date() + " (Error Code: " + errorCode + ")"); //Write formatted text to error.log
			out.close(); //Close writer out
			System.out.println(message + " on " + new Date() + " (Error Code: " + errorCode + ")"); //Print to textArea the error that took place
			Event.stopExecution(); //Stop all events
			
			//Serialization
            try {
              	ObjectOutputStream dump = new ObjectOutputStream(new FileOutputStream("dump.out")); //Output to dump.out file
              	dump.writeObject(GreenhouseControls.this); //Serialize and write Greenhousecontrols object to dump.dout
              	dump.close(); //Close output stream
            }
            catch(Exception e){
				System.out.println("There was an issue with serialization. Dump.out may be unusable.");
            }
	  			gui.shutdownGreenhouse(errorCode); //Call shutdownGreenhouse() on gui which sets buttons as they should be		 				
			}
			catch(Exception e){
				System.out.println("There was an issue while shutting down the system. Restoring from dump.out might not work.");
			}
			gc.clear(); //Clear gc so that next time it's empty
			gcEvents.clear(); //Clear gcEvents so that next time it's empty
	}

/**
* <b>restoreGreenhouseControls() Method</b>
* Restores object from dump.out file
* Uses ObjectInputStream to restore serialized object
*
* @param dumpFile  The dump.out file you want to restore from
* @throws StreamCorruptedException
*			if the stream header is incorrect.
* @throws IOException
*			if and I/O error occurs while writing stream header.
* @throws SecurityException
*			if untrusted subclass illegally overrides security-sensitive methods.
* @throws NullPointerException
*			if in is null.
* @throws ClassNotFoundException
*			Class of a serialized object cannot be found.
* @throws InvalidClassException
*			Something is wrong with a class used by serialization.
* @throws StreamCorruptedException
*			Control information in the stream is inconsistent.
* @throws OptionalDataException
*			Primitive data was found in the stream instead of objects.
* @throws IOException
*			Any of the usual Input/Output related exceptions.
* @return  GreenhouseControls object
*
*/
	public static GreenhouseControls restoreGreenhouseControls(String dumpFile) throws Exception {
		ObjectInputStream dump = new ObjectInputStream( new FileInputStream(dumpFile)); //Inputstream from dump.out
		GreenhouseControls dumpGC = (GreenhouseControls)dump.readObject(); //Assign dump.out to dumpGC
		dump.close(); //Close Inputstream
		BufferedWriter out = new BufferedWriter(new FileWriter("fix.log"));
		out.write("The Power was restored on " + new Date());
		out.close(); //Close BufferedWriter
		System.out.println("The Power was restored on " + new Date());
		return dumpGC; //Return the restored gc
	}

/**
* <b>restartEvents() Method</b>
* Starts the Events again that were loaded from the dump.out file
* Creates a new ArrayList of Threads, iterates through gcEvents to see if any events haven't been attempted,
* if they haven't been attempted then they will be Threaded and ran.
*
*/
	public void restartEvents(){
		gc = new ArrayList<Thread>(); //New thread list
		Event.allowExecution();	//Allow run to happen
	
		for(Event ev : gcEvents){ //For every event in gcEvents
			if(ev.getAttempt() == false){ //If they weren't attempted yet
				ev.restartEvent(); //Call restartEvent on the event, lets events continue, calculates when to execute
				threadIt(ev); //Call threadIt on ev
			}
		}
	}

/**
* <b>restart() Method</b>
* Adds Events dynamically from a text file and runs them as seperate Threads
* 
* @param	eventsFile  The file to read the Events from
* @throws 	NullPointerException
*			if the pathname argument is null.
* @see 		java.io.File#File(String)
* @throws 	NoSuchElementException
*			if no more tokens are available.
* @see		java.util.Scanner#next()
* @throws 	IllegalStateException
*			if the scanner is closed.
* @see		java.util.Scanner#next()
* @throws 	LinkageError
*			if the linkage fails.
* @see		java.lang.Class#forName(String)
* @throws 	ExceptionInInitializerError
*			if the initialization provoked by this method fails.
* @see		java.lang.Class#forName(String)
* @throws 	NumberFormatException
*			if the string does not contain a parsable long.
* @see		java.lang.Long#parseLong(String)
* @throws	NumberFormatException
*			if the string does not contain a parsable integer.
* @see		java.lang.Integer#parseInt(String)
* @throws 	NullPointerException
*			if s is null.
* @see		java.lang.String#contains(CharSequence)
* @throws	SecurityException
*			if a security manager, s, is present and any of the following conditions is met:
*			incovation of s.checkMemberAccess(this, Member.PUBLIC) denies access to the constructor
*			the caller's class loader is not the same as or an acnestor of the class loader for the
*			current class and invocation of s.checkPackageAccess() denies acces to the package of this class.
* @see		java.lang.Class#getConstructor(Class... parameterTypes)
* @throws	ExceptionInInitializerError
*			if the initialization provoked by this method fails.
* @see		java.lang.Class#newInstance()
* @throws	SecurityException
*			if a security manager, s, is present and any of the following conditions is met:
*			incovation of s.checkMemberAccess(this, Member.PUBLIC) denies access to the constructor
*			the caller's class loader is not the same as or an acnestor of the class loader for the
*			current class and invocation of s.checkPackageAccess() denies acces to the package of this class.			
* @see		java.lang.Class#newInstance()
*/
	public void restart(String eventsFile){
		String event; //Hold name of event used to compare and perform respective threading
		String delayString; //Hold delayTime as string from text file
		long delayTime; //Hold delayTime for each event as long to be converted from delayString
		int rings; //Hold number of rings for bell
	  	BufferedReader in; //Reader used to parse events file
		Event ev; //Hold event
		Class<?> classy; //Hold Class
		Constructor<?> classConstructor; //Hold constructor
		
		gcEvents.clear(); //Clear gcEvents so it's fresh
		gc.clear(); //Clear gc so it's fresh
		Event.allowExecution();	//Allow execution to happen
		
		try {
			Scanner ringScanner = new Scanner(new File(eventsFile)); //Scanner to scan for rings
			int pos1 = 0; //Hold position 1
			int pos2 = 0; //Hold position 2
			int pos3 = 0; //Hold position 3
			long delayLong = 0; //Hold delay
			long ringDelay = 0; //Hold delay for each extra ring
			
			in = new BufferedReader(new FileReader(eventsFile)); //Read from eventsFile
			
			//Where the magic happens ----------------------------------------------------------------------------
			while((event = in.readLine()) != null) { //While there are lines in the file, for each line
				Scanner scanner = new Scanner(event); //Assign event to scanner
				scanner.useDelimiter("\\s*,\\s*"); //Delimiter to parse text
				event = scanner.next().split("=")[1]; //Assign what's after = to event String
				
				classy = Class.forName(event); //Get/Create Class that is the same as the read event from the file
				classConstructor = classy.getConstructor(long.class); //Get/create that Class's constructor
				delayString = scanner.next().split("=")[1]; //Assign what comes after the first = to delayString
				delayTime = Long.parseLong(delayString); //Convert delayString to a Long and assign it to delayTime
				ev = (Event)classConstructor.newInstance(delayTime); //Create event called ev and assign a new instance of the dynamic class from dynamic constructor
				
				gcEvents.add(ev); //Add the event to gcEvents list
				threadIt(ev); //Call threadIt on ev
			}
			//-----------------------------------------------------------------------------------------------------
			in.close(); //Close reader in
			
			//If events file has bell that rings multiple times, this will be used to capture it
			while(ringScanner.hasNextLine()){ //While there are lines in the file, for each line
				String line = ringScanner.nextLine(); //Assing next line to line
				if(line.contains("Bell") && line.contains("rings")) { //If the line contains bell and rings
					pos1 = line.indexOf("time=")+5; //Setting the proper index of pos1 so that the integer for time can be read
					pos2 = line.lastIndexOf(",rings="); //Setting the proper index of pos2 so that the integer for time can be read
					pos3 = pos2+7; //Setting the proper index of pos3 so that the integer for rings can be read
					delayLong = Long.parseLong(line.substring(pos1, pos2)); //Assigning the delayInt which is located between pos1 and pos2
					rings = Integer.parseInt(line.substring(pos3)); //Assigning the number of rings which is located after pos3
					
					classy = Class.forName("Bell"); //Get/Create Class that is Bell
					classConstructor = classy.getConstructor(long.class); //Get/create the Bell constructor
					ev = (Event)classConstructor.newInstance(delayLong); //Create event called ev and assign a new instance Bell from it's constructor
					
					for (int i = 0; i < rings-1; i++) { //For each number of rings
						ringDelay = ringDelay+2000; //Increase delay on each iteration, so that it rings every 2000ms
						ev = (Event)classConstructor.newInstance(ringDelay); //Create event called ev just like above but with proper delayTime on each iteration
						gcEvents.add(ev); //Add the event to gcEvents list
						threadIt(ev); //Call threadIt on ev
					}
				}
			}
		}
		catch(ClassNotFoundException e) {
			 e.printStackTrace();
		 }
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(NoSuchMethodException e) {
			e.printStackTrace();
		}
		catch(InstantiationException e) {
			e.printStackTrace();
		}
		catch(IllegalAccessException e) {
			e.printStackTrace();
		}
		catch(InvocationTargetException e) {
			e.printStackTrace();
		}
	}

/**
* <b>threadIt() Method</b>
* Takes an Event object, Threads it, adds it to gc and runs it
*/
	private void threadIt(Event ev) {
		Event event = ev;
		Thread t = new Thread(event); //Create new Thread of Event
		gc.add(t); //Add thread to gc
		t.start(); //Run Thread
	}

/**
* <b>threadException Class</b> - Custom handler for uncaught exceptions
*/
	class threadException implements Thread.UncaughtExceptionHandler {
		
/**
* <b>uncaughtException() Method</b>
* Handles printing to textArea the reason why an error occured and calls shutdown() method
* errorCode used with switch to find corresponding reason and call shutdown() method with it
*
* @param t  The thread which caused the error
* @param exception The exception that was thrown
* @throws	IndexOutOfBoundsException
*			if the beginIndex is negative, or endIndex is larger than the length of this String object,
*			or beginIndex is larger than endIndex.
* @see		java.lang.String#substring(int beginIndex, int endIndex)
* @throws	NumberFormatException
*			if the string does not contain a parsable integer
* @see		java.lang.Integer#parseInt(String s)
*/
		public void uncaughtException(Thread t, Throwable exception){
			String code = exception.getMessage().substring(39,40); //Assigns errorcode from exception to code, exception message "java.lang.RuntimeException: Error Code #: reason"
			String errorReason; //To hold the reason for the error
			
			try {
				errorCode = Integer.parseInt(code); //Assign int from code to errorCode to be used in switch below
			} 
			catch(Exception e) {
				errorCode = -1; //If an exception occurs, assign errocode as -1 so it can be handled seperately
				System.out.println("thread exception occured");
			}
			switch(errorCode){ //Switch to give proper response
				case 0: //If 0, nothing is wrong
					errorReason = "The system shut down normally";
					break;
				case 1: //If 1, window malfunctioned
					errorReason = "A window malfunctioned";
					break;
				case 2: //If 2, power went out
					errorReason = "The power went out";
					break;
				default: //If something else, unknown issue (add more to this switch to accomodate new issues that could take place)
					errorReason = "An unknown issue occured";
			}
			shutdown(errorReason); //Call shutdown method
		}
	}
}