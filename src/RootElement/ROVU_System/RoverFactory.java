// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package RootElement.ROVU_System;

import RootElement.ROVU_System.Rover;

/************************************************************/
/**
 * 
 */
public class RoverFactory {

	private static RootElement.ROVU_System.RoverFactory instance = new RoverFactory();
	private RoverFactory() {
	}

	public Rover getRover(String s) {
		
		if(s.compareToIgnoreCase("scouting") == 0){
			// how would this even work with the super() constructor towards Agent requiring initpos...
			//return new ScoutingRover();
		}else if(s.compareToIgnoreCase("camera") == 0){
			return new CameraRover();
		}else{
			System.out.printf("getRover(): ERROR: rover type not found.\n");
		}
		
		return null;
	}

	public static RootElement.ROVU_System.RoverFactory getInstance() {
		return instance;
	}
};
