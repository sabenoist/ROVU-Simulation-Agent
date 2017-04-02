// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package RootElement.ROVU_System;

import javax.vecmath.Vector3d;

import RootElement.ROVU_System.Rover;

/************************************************************/
/**
 * 
 */
public class RoverFactory {

	private static RootElement.ROVU_System.RoverFactory instance = new RoverFactory();
	private RoverFactory() {
	}

	public Rover getRover(RoverEnum type, Vector3d initPos, String name, Subject s, int initDirection) {
		
		if(type == RoverEnum.SCOUTING_ROVER){
			return new ScoutingRover(initPos, name, s, initDirection);
		}else if(type == RoverEnum.CAMERA_ROVER){
			return new CameraRover(initPos, name, s, initDirection);
		}else{
			System.out.printf("getRover(): ERROR: rover type not found.\n");
		}
		
		return null;
	}

	public static RootElement.ROVU_System.RoverFactory getInstance() {
		return instance;
	}
};
