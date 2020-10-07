/**
* Program: SystemState.java
* Student ID: #3145381
* Description: SystemState Tuple to hold Event and Action it performed
* @author Chad Cromwell
* @date December 20 2015
*/

/**
* <b>SystemState Class</b> - Creates a tuple
*/
public class SystemState<A, B>{
	public final A hardware;
	public final B state;
	
	public SystemState(A hardware, B state){
		this.hardware = hardware;
		this.state = state;
	}	
}