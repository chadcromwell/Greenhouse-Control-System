/**
* Program: GreenhouseGUI.java
* Student ID: #3145381
* Purpose: To provide a GUI for GreenhouseControls.java
* Description: A GUI that a user can use to control various aspects of a Greenhouse.
* @author Chad Cromwell
* @date December 20 2015
*/

import javax.swing.*;
import javax.swing.UIManager.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

/**
* <b>GreenhouseGUI Class</b> - A GUI to be used with GreenhouseControls.
*/
public class GreenhouseGUI extends JFrame implements ActionListener{
	//Variables, Buttons, Menus, etc.
	GreenhouseControls gc;
	
	String eventsFile;
	Boolean isDumpFile = false;
	
	JMenuBar menuBar;
	JMenu fileMenu;
	JMenuItem newWindow;
	JMenuItem closeWindow;
	JMenuItem openEvents;
	JMenuItem restore;
	JMenuItem exit;
	
	JMenuItem startPopup;
	JMenuItem restartPopup;
	JMenuItem terminatePopup;
	JMenuItem suspendPopup;
	JMenuItem resumePopup;
	
	JPopupMenu popupMenu;
	
	JLabel eventsFileLabel;
	JLabel filename;
	
	JTextArea textArea;
	JScrollPane scrollArea;
	
	JButton chooseFile;
	JButton restoreDump;
	JButton start;
	JButton restart;
	JButton terminate;
	JButton suspend;
	JButton resume;
	
	JFileChooser openEventsDialogue;
	JFileChooser restoreDialogue;
	
	GridBagConstraints constraints = new GridBagConstraints(); //Constraints for use with GridBagLayout

/**
* <b>GreenhouseGUI Constructor</b>
* Creates GUI, its Menu Items, Popup Menu Items, Menu Bar, Text Area, Buttons, etc.
* Attempts to use Nimbus Look And Feel, if it's not installed then it attemps to use default system Look And Feel.
*
* @param gc  The GreenhouseControls object to use with GUI
* @throws	Exception
*			if error occurs Exception is thrown
* @see		java.lang.Exception#Exception()
*/
	public GreenhouseGUI(GreenhouseControls gc) throws Exception{
		super("Greenhouse Controller"); //Window title
		this.gc = gc;
		
		//Try to use Nimbus Look and Feel
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		}
		catch (Exception ex) { //If Numbus isn't installed, use default Look and Feel
			System.out.println("Nimbus not installed, attempting to use default Look and Feel");
			
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}
			catch (Exception ex2) {
				System.out.println("Default Look and Feel not found.");
			}
		}
		
		setSize(700,400); //Default window size
		setLocationRelativeTo(null); //Window opens in centre screen
		setLayout(new GridBagLayout()); //Use GridBagLayout
		
		menuBar = new JMenuBar(); //Create menubar
		fileMenu = new JMenu("File"); //Create fileMEnu with "File" label
		
//File Menu Items---------------------------------------------
		newWindow = new JMenuItem("New window");
		newWindow.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())); //Ctrl in Windows/Linux or Command in Mac OSX + N opens new window
		closeWindow = new JMenuItem("Close window");
		closeWindow.setAccelerator(KeyStroke.getKeyStroke('W', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())); //Ctrl in Windows/Linux or Command in Mac OSX  + W closes window
		openEvents = new JMenuItem("Open Events File");
		openEvents.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())); //Ctrl in Windows/Linux or Command in Mac OSX + O opens file browser to open events file
		restore = new JMenuItem("Restore Dump File");
		restore.setAccelerator(KeyStroke.getKeyStroke('R', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())); //Ctrl in Windows/Linux or Command in Mac OSX + R opens file browser to restore from dump file
		exit = new JMenuItem("Exit");
		exit.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())); //Ctrl in Windows/Linux or Command in Mac OSX + Q exits the application
		
		//Action Listeners
		newWindow.addActionListener(new NewWindow());
		closeWindow.addActionListener(new CloseWindow());
		openEvents.addActionListener(new OpenEvents());
		restore.addActionListener(new Restore());
		exit.addActionListener(new CloseWindow());
		
//Popup Menu Items--------------------------------------------
		startPopup = new JMenuItem("Start");
		restartPopup = new JMenuItem("Restart");
		terminatePopup = new JMenuItem("Terminate");
		suspendPopup = new JMenuItem("Suspend");
		resumePopup = new JMenuItem("Resume");
		startPopup.setEnabled(false);
		restartPopup.setEnabled(false);
		terminatePopup.setEnabled(false);
		suspendPopup.setEnabled(false);
		resumePopup.setEnabled(false);
		
		//Action Listeners
		startPopup.addActionListener(new startGreenhouse());
		restartPopup.addActionListener(new restartGreenhouse());
		terminatePopup.addActionListener(new terminateGreenhouse());
		suspendPopup.addActionListener(new suspendGreenhouse());
		resumePopup.addActionListener(new resumeGreenhouse());
		
//Add Items to Menu Bar---------------------------------------
		fileMenu.add(newWindow);
		fileMenu.add(closeWindow);
		fileMenu.add(openEvents);
		fileMenu.add(restore);
		fileMenu.add(exit);
		
		setJMenuBar(menuBar);
		menuBar.add(fileMenu);
		
//Add Items to Popup Menu-------------------------------------
		popupMenu = new JPopupMenu();
		PopupListener popupListener = new PopupListener();
		addMouseListener(popupListener);
			popupMenu.add(startPopup);
			popupMenu.add(restartPopup);
			popupMenu.add(terminatePopup);
			popupMenu.add(suspendPopup);
			popupMenu.add(resumePopup);
			
//Event File Label---------------------------------------------
		eventsFileLabel = new JLabel("Event File: ");
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.weightx = 1;
			constraints.weighty = 1;
			constraints.gridwidth = 5;
		add(eventsFileLabel, constraints);
		
//Event Filename Label-----------------------------------------
		filename = new JLabel("");
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 1;
			constraints.gridy = 0;
			constraints.gridwidth = 2;
		add(filename, constraints);
		
//TextArea-----------------------------------------------------
		textArea = new JTextArea(5, 5);
		scrollArea = new JScrollPane(textArea);
		scrollArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); //Vertcal Scrollbar
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.gridwidth = 5;
			constraints.gridx = 0;
			constraints.gridy = 1;
			constraints.weightx = 1;
			constraints.weighty = 1;
			constraints.ipady = 150;
		add(scrollArea, constraints);
		
//Buttons------------------------------------------------------
		//Open Events File Button
		chooseFile = new JButton("Choose Event File");
			constraints.gridx = 3;
			constraints.gridy = 0;
			constraints.weightx = 1;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			constraints.ipady = 3;
			chooseFile.setEnabled(true);
			chooseFile.addActionListener(new OpenEvents()); //When button is pressed, construct OpenEvents()
		add(chooseFile, constraints);

		//Restore Dump File Button
		restoreDump = new JButton("Restore Dump File");
			constraints.gridx = 4;
			constraints.gridy = 0;
			constraints.weightx = 1;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			constraints.ipady = 3;
			restoreDump.setEnabled(true);
			restoreDump.addActionListener(new Restore()); //When button is pressed, construct Restore()
		add(restoreDump, constraints);
			
		//Start Button
		start = new JButton("Start");
			constraints.gridx = 0;
			constraints.gridy = 2;
			constraints.weightx = 1;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			constraints.ipady = 40;
			start.setEnabled(false);
			start.addActionListener(new startGreenhouse()); //When button is pressed, call startGreenhouse() method
		add(start, constraints);
		
		//Restart Button
		restart = new JButton("Restart");
			constraints.gridx = 1;
			constraints.gridy = 2;
			constraints.weightx = 1;
			restart.setEnabled(false);
			restart.addActionListener(new restartGreenhouse()); //When button is pressed, call restartGreenhouse() method
		add(restart, constraints);
		
		//Terminate Button
		terminate = new JButton("Terminate");
			constraints.gridx = 2;
			constraints.gridy = 2;
			constraints.weightx = 1;
			terminate.setEnabled(false);
			terminate.addActionListener(new terminateGreenhouse()); //When button is pressed, call terminateGreenhouse() method
		add(terminate, constraints);
		
		//Suspend Button
		suspend = new JButton("Suspend");
			constraints.gridx = 3;
			constraints.gridy = 2;
			constraints.weightx = 1;
			suspend.setEnabled(false);
			suspend.addActionListener(new suspendGreenhouse()); //When button is pressed, call suspendGreenhouse() method
		add(suspend, constraints);
		
		//Resume Button
		resume = new JButton("Resume");
			constraints.gridx = 4;
			constraints.gridy = 2;
			constraints.weightx = 1;
			resume.setEnabled(false);
			resume.addActionListener(new resumeGreenhouse()); //When button is pressed, call resumeGreenhouse() method
		add(resume, constraints);
		
		//Allows closing of windows, closing last window will exit application
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//Show window
		setVisible(true);
	}
	
/**
* <b>actionPerformed() Default Method</b> - Can be ignored.
*
* @param e	ActionEvent
*/	
	public void actionPerformed(ActionEvent e) { //Base actionPerformed for GUI, can be ignored

	}
	
/**
* <b>PopupListener Class</b> - Listener for when mouse right click takes place.
*/
	//Popup Listener
	class PopupListener extends MouseAdapter {
/**
* <b>mousePressed() Method</b>
* Takes a MouseEvent and calls showPopup() method when MouseEvent occurs.
*
* @param e MouseEvent.
* @see #showPopup(e)
*/
		public void mousePressed(MouseEvent e) { //When right click is pressed, show the popup window
			showPopup(e);
		}

/**
* <b>mouseReleased() Method</b>
* Takes a MouseEvent and calls showPopup() method when MouseEvent occurs.
*
* @param e MouseEvent.
* @see #showPopup(e)
*/
		public void mouseReleased(MouseEvent e) { //When right click is released, close popup window
			showPopup(e);
		}

/**
* <b>showPopup() Method</b>
* Makes the right click popup menu appear.
* Takes a MouseEvent and calls show for popupMenu at the location where MouseEvent took place.
*
* @param e MouseEvent.
*/
		public void showPopup(MouseEvent e) { //Show popup window
			if(e.isPopupTrigger())
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

/**
* <b>shutdownGreenhouse() Method</b>
* Sets the buttons (to enabled or disabled) depending on what state the system is currently in.
* Takes errorCode and then sets corresponsing buttons through if statements.
*
* @param errorCode	The error code generated from shutdown.
*/	
	public void shutdownGreenhouse(int errorCode) {
		if(errorCode == 0) { //If shutdown normally, then go back to normal beginning buttons enabled
			start.setEnabled(false);
			startPopup.setEnabled(false);
			restart.setEnabled(true);
			restartPopup.setEnabled(true);
			terminate.setEnabled(false);
			terminatePopup.setEnabled(false);
			suspend.setEnabled(false);
			suspendPopup.setEnabled(false);
			resume.setEnabled(false);
			resumePopup.setEnabled(false);
			chooseFile.setEnabled(true);
		}
		else { //Shutdown happened because of an issue, so only allow user to restore from a dump file (only enable restore button)
			start.setEnabled(false);
			startPopup.setEnabled(false);
			restart.setEnabled(false);
			restartPopup.setEnabled(false);
			terminate.setEnabled(false);
			terminatePopup.setEnabled(false);
			suspend.setEnabled(false);
			suspendPopup.setEnabled(false);
			resume.setEnabled(false);
			resumePopup.setEnabled(false);
			openEvents.setEnabled(false);
			chooseFile.setEnabled(false);
			restoreDump.setEnabled(true);
		}
	}

/**
* <b>toTextArea() Method</b>
* For use in GUIOutput, sets the text in the textArea of the GUI.
* 
* @param s String that you want printed to the textArea of the GUI.
*/
	public void toTextArea(String s) { //Method to update the text area, For use in GUIOutput to allow System.out.println to print to text area
		textArea.setText(textArea.getText() + s + "\n");
	}
	
//File Menu Events------------------------------------------------------
/**
* <b>NewWindow Class</b> - Creates a new GUI window.
*/
	class NewWindow implements ActionListener {

/**
* <b>actionPerformed() Method</b>
* Construct a new GreenhouseGUI
*
* @param e ActionEvent
*/
		public void actionPerformed(ActionEvent e) {
			try{
				GreenhouseGUI newGUI = new GreenhouseGUI(gc); //Create new GUI window
			}
			catch(Exception ex){
				System.out.println("Exception was thrown when calling new window"); //If exception occurs, tell user window could not be created
			}
		}
	}
	
/**
* <b>CloseWindow Class</b> - Closes GUI window.
*/
	class CloseWindow implements ActionListener {

/**
* <b>actionPerformed() Method</b>
* Close the window and application.
* If Threads are running, it will ask if you are sure you'd like to close the window.
*
* @param e ActionEvent
*/
		public void actionPerformed(ActionEvent e) {
			if(gc.gcRunning()){ //If gc.gcRunning returns true then ask the user if they really want to quit
				int result = JOptionPane.showConfirmDialog(GreenhouseGUI.this, "System is currently running. Do you really want to exit?", "EXIT", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if(result == JOptionPane.YES_OPTION){ //If yes then close the window
					GreenhouseGUI.this.dispose();
				}
			}
			else {
				GreenhouseGUI.this.dispose();
			}
		}
	}
	
/**
* <b>OpenEvents Class</b> - Allows user to navigate through FileChooser and open an events text file
*/
	class OpenEvents implements ActionListener {
		String event;

/**
* <b>actionPerformed() Method</b>
* Opens FileChooser window, lets user choose and open an events text file.
* Checks if it is of the right format, if not it will tell the user to pick another file.
*
* @param e ActionEvent
*/
		public void actionPerformed(ActionEvent e) {
			int length; //To hold length of the directory String, used to truncate String so it fits in the program window nicely
			
			openEventsDialogue = new JFileChooser(); //Assign JFileChooser as openEventsDialogue
			int returnVal = openEventsDialogue.showOpenDialog(GreenhouseGUI.this); //Assign returnVal to see if file has been picked or not
			
			if(returnVal == JFileChooser.APPROVE_OPTION) { //If OK is clicked, do this
				eventsFile = openEventsDialogue.getCurrentDirectory().toString() + "/" + openEventsDialogue.getSelectedFile().getName(); //Assigning the directory as a string called eventsFile
				length = eventsFile.length(); //Find length of eventsFile and assign it to length
				filename.setText(eventsFile.substring(0, 10) + "..." + eventsFile.substring(length-14)); //Set the text for filename label, truncated version so it fits nicely
				
				//Parse file to see if it is an Events file (meets the proper formatting requirements)
				try {
					textArea.setText(""); //Clear text area
					BufferedReader in = new BufferedReader(new FileReader(eventsFile)); //Read eventsFile
					
					while((event = in.readLine()) != null) { //For each line in the file
						Scanner scan = new Scanner(event); //Assign line to scanner
						scan.useDelimiter("\\s*,\\s*"); //Delimiter to parse text
						event = scan.next().split("=")[1]; //Use what comes after "="
					}
					in.close();
					start.setEnabled(true); //Enable start button
					startPopup.setEnabled(true);
				}
				catch(Exception ex) {
					eventsFile = ""; //Clear eventsFile
					textArea.setText("Invalid File - Please choose a proper Events text file."); //Print to text area that the file is invalid.
					start.setEnabled(false);
					startPopup.setEnabled(false);
				}
			}
		}
	}
	
/**
* <b>Restore Class</b> - Allows user to navigate through FileChooser and open a dump.out file to restore events from.
* Works a lot like OpenEvents Class.
*
* @see #OpenEvents
*/
	class Restore implements ActionListener { //Works just like Open Event but with a dump file, see Open Event comments for details

/**
* <b>actionPerformed() Method</b>
* Opens FileChooser window, lets user choose and open a dump.out file.
* Checks if it is of the right format, if not it will tell the user to pick another file.
* Sets isDumpFile to true and enables Start button.
*
* @param e 	ActionEvent
*/
		public void actionPerformed(ActionEvent e) {
			int length;
			
			restoreDialogue = new JFileChooser();
			int returnVal = restoreDialogue.showOpenDialog(GreenhouseGUI.this);
			
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				eventsFile = restoreDialogue.getCurrentDirectory().toString() + "/" + restoreDialogue.getSelectedFile().getName();
				length = eventsFile.length();
				filename.setText(eventsFile.substring(0, 10) + "..." + eventsFile.substring(length-14));
				try {
					textArea.setText("");
					ObjectInputStream dump = new ObjectInputStream(new FileInputStream(eventsFile));
					GreenhouseControls dumpGC = (GreenhouseControls)dump.readObject();
					isDumpFile = true;
					start.setEnabled(true);
					startPopup.setEnabled(true);
				}
				catch(Exception ex) {
					eventsFile = "";
					textArea.setText("Invalid File - Please choose a dump.out file.");
					start.setEnabled(false);
					startPopup.setEnabled(false);
				}
			}
		}
	}
	
//Button Events--------------------------------------------------------
/**
* <b>StartGreenhouse Class</b> - Start Button event.
*/
	class startGreenhouse implements ActionListener {

/**
* <b>actionPerformed() Method</b>
* Clears textArea.
* Disables Start, Restart, Resume buttons/popup.
* Enables Terminate, Suspend buttons/popup.
* If isDumpFile is true, it will restore the events from the dump.out file by calling restartEvents() method.
* If isDumpfile is false, it will start events from scratch by calling restart() method.
*
* @param e 	ActionEvent
* @see		GreenhouseControls#restartEvents()
* @see		GreenhouseControls#restart(String)
*/	
		public void actionPerformed(ActionEvent e) {
			textArea.setText(""); //Clear text area
			//Set buttons
			start.setEnabled(false);
			startPopup.setEnabled(false);
			restart.setEnabled(false);
			restartPopup.setEnabled(false);
			terminate.setEnabled(true);
			terminatePopup.setEnabled(true);
			suspend.setEnabled(true);
			suspendPopup.setEnabled(true);
			resume.setEnabled(false);
			resumePopup.setEnabled(false);
			
			try {
				if(isDumpFile == true){ //If isDumpFile is true/If user chose a dumpfile
					gc = GreenhouseControls.restoreGreenhouseControls(eventsFile); //Call restoreGreenhouseControls method on eventsFile, assign to gc
					gc.restartEvents(); //Call restartEvents method on gc
					isDumpFile = false;
				}
				else{ //Else, it's an events file
					gc.restart(eventsFile); //Run normal restart method on the eventsFile
				}
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
/**
* <b>restartGreenhouse Class</b> - Restart Button event.
*/
	class restartGreenhouse implements ActionListener {

/**
* <b>actionPerformed() Method</b>
* Clears textArea.
* Disables Start, Restart, Resume buttons/popup.
* Enables Terminate, Suspend buttons/popup.
* Starts events from scratch by calling restart() method.
*
* @param e 	ActionEvent
* @see 		GreenhouseControls#restart(String)
*/	
		public void actionPerformed(ActionEvent e) {
			textArea.setText("");
			start.setEnabled(false);
			startPopup.setEnabled(false);
			restart.setEnabled(false);
			restartPopup.setEnabled(false);
			terminate.setEnabled(true);
			terminatePopup.setEnabled(true);
			suspend.setEnabled(true);
			suspendPopup.setEnabled(true);
			resume.setEnabled(false);
			resumePopup.setEnabled(false);
			gc.restart(eventsFile);
		}
	}
	
/**
* <b>terminateGreenhouse Class</b> - Terminate Button event.
*/
	class terminateGreenhouse implements ActionListener {

/**
* <b>actionPerformed() Method</b>
* Brings up window and asks user to enter a time in milliseconds for when they'd like the program to terminate.
* If the user enters something other than an int it will not accept it.
* If an int is entered, it then:
* Disables Start, Restart, Terminate, Suspend buttons/popup.
* Enables Restart, Choose File buttons/popup.
* Terminates events by calling terminate() method.
* 
* @param e 	ActionEvent
* @see		GreenhouseControls#terminate(int)
*/	
		public void actionPerformed(ActionEvent e) {
			boolean isInt = false;
			String delay = "";
			int delayTime;
			
				try {
					delay = JOptionPane.showInputDialog(GreenhouseGUI.this, "Enter a time in milliseconds", 0); //Ask user to input time
					if(delay == null) { //If cancel is clicked, just continue running the program as normal
						return;
					}
					if(delay != null){ //If user enters something and clicks okay
						isInt = true; //Set isInt to true
						delayTime = Integer.parseInt(delay); //Assign int from delay to delayTime
						//Set buttons
						start.setEnabled(false);
						startPopup.setEnabled(false);
						restart.setEnabled(false);
						restartPopup.setEnabled(false);
						terminate.setEnabled(false);
						terminatePopup.setEnabled(false);
						suspend.setEnabled(false);
						suspendPopup.setEnabled(false);
						restart.setEnabled(true);
						restartPopup.setEnabled(true);
						chooseFile.setEnabled(true);
						gc.terminate(delayTime); //Terminate Events
					}
				}
				catch (Exception ex) {
					isInt = false; //If it's not an int, then set isInt to false
				}
		}
	}

/**
* <b>suspendGreenhouse Class</b> - Suspend Button event.
*/
	class suspendGreenhouse implements ActionListener {

/**
* <b>actionPerformed() Method</b>
* Disables Start, Restart, Terminate, Suspend buttons/popup.
* Enables Resume buttons/popup.
* Suspends events by calling gcSuspend() method.
*
* @param e 	ActionEvent
* @see 		GreenhouseControls#gcSuspend()
*/	
		public void actionPerformed(ActionEvent e) {
			//Set buttons
			start.setEnabled(false);
			startPopup.setEnabled(false);
			restart.setEnabled(false);
			restartPopup.setEnabled(false);
			terminate.setEnabled(false);
			terminatePopup.setEnabled(false);
			suspend.setEnabled(false);
			suspendPopup.setEnabled(false);
			resume.setEnabled(true);
			resumePopup.setEnabled(true);
			gc.gcSuspend(); //Call gcSuspend() method on gc
			
		}
	}

/**
* <b>resumeGreenhouse Class</b> - Resume Button event.
*/
	class resumeGreenhouse implements ActionListener {

/**
* <b>actionPerformed() Method</b>
* Disables Start, Restart, Resume buttons/popup.
* Enables Terminate, Suspend,  buttons/popup.
* Resumes events by calling gcResume() method.
*
* @param e ActionEvent
* @see GreenhouseControls#gcResume()
*/	
		public void actionPerformed(ActionEvent e) {
			//Set buttons
			start.setEnabled(false);
			startPopup.setEnabled(false);
			restart.setEnabled(false);
			restartPopup.setEnabled(false);
			terminate.setEnabled(true);
			terminatePopup.setEnabled(true);
			suspend.setEnabled(true);
			suspendPopup.setEnabled(true);
			resume.setEnabled(false);
			resumePopup.setEnabled(false);
			gc.gcResume(); //Call gcResume() method on gc
		}
	}
}