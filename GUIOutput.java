/**
* Program: GUIOutput.java
* Student ID: #3145381
* Purpose: PrintStream to send output to the GUI, overrides println for strings and objects, sends them to GUI
* Description: Provides functionality for GreenhouseGUI and Greenhousecontrols so that text prints to textArea in GUI
* Based off of Bruce Eckels and Matt Atwood's versions of GreenhouseControls.java
* Known issues: New window output goes to original window. Need to find solution.
* @author Chad Cromwell
* @date December 20 2015
*/

import java.io.*;

/**
* <b>GUIOutput</b> - Sends output to the GUI
*/

public class GUIOutput extends PrintStream {
	GreenhouseGUI toGUI; //To hold GreenhouseGUI objects

/**
* <b>GUIOutput Constructor</b>
* Takes GreenhouseGUI objects and captures it to toGUI
* then overrides println for String and Object to call toTextArea() method
*
* @param toGUI 	GreenhouseGUI Object you want to override println for
* @see 			GreenhouseGUI#toTextArea(String)
*/	
	public GUIOutput(GreenhouseGUI toGUI){ //Accepts GreenhouseGUI objects
		super(System.out); //GreenhouseGUI System.out method
		this.toGUI = toGUI; //Captures toGUI and assigns it to toGUI, to be used in overrides below
	}
	
	@Override //For strings
	public void println(String s) {
		toGUI.toTextArea(s.toString()); //Calls toTextArea method, prints string to textArea
	}
	
	@Override //For objects
	public void println(Object o) {
		toGUI.toTextArea(o.toString()); //Calls toTextArea method, prints object to textArea
	}

}