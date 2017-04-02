package RootElement.ROVU_System;

import simbad.gui.*;
import simbad.sim.*;

import javax.vecmath.Vector3d;

public class Main {
		
	public static void main(String[] args) {
        System.setProperty("j3d.implicitAntialiasing", "true");
        
        CentralStation cs = CentralStation.getInstance();  
        cs.setMission("Catch them all.");
        
        EnvironmentDescription environment = new Environment(10, 10);
        
        cs.setEnvironment((Environment)environment);
        cs.preRoverLaunch();
        
        Vector3d[] initPos = cs.getInitPositions();
        
        Rover sr1 = cs.getRoverFactory().getRover(RoverEnum.SCOUTING_ROVER, initPos[0], "Scout-1", cs, Rover.NORTH);
        Rover sr2 = cs.getRoverFactory().getRover(RoverEnum.SCOUTING_ROVER, initPos[1], "Scout-2", cs, Rover.NORTH);
        Rover sr3 = cs.getRoverFactory().getRover(RoverEnum.SCOUTING_ROVER, initPos[2], "Scout-3", cs, Rover.SOUTH);
        Rover sr4 = cs.getRoverFactory().getRover(RoverEnum.SCOUTING_ROVER, initPos[3], "Scout-4", cs, Rover.SOUTH);

        Rover cr1 = cs.getRoverFactory().getRover(RoverEnum.CAMERA_ROVER, initPos[4], "Camera-1", cs, Rover.NORTH);
        Rover cr2 = cs.getRoverFactory().getRover(RoverEnum.CAMERA_ROVER, initPos[5], "Camera-2", cs, Rover.NORTH);
        Rover cr3 = cs.getRoverFactory().getRover(RoverEnum.CAMERA_ROVER, initPos[6], "Camera-3", cs, Rover.SOUTH);
        Rover cr4 = cs.getRoverFactory().getRover(RoverEnum.CAMERA_ROVER, initPos[7], "Camera-4", cs, Rover.SOUTH);

        environment.add(sr1);
        environment.add(sr2);
        environment.add(sr3);
        environment.add(sr4);
        
        environment.add(cr1);
        environment.add(cr2);
        environment.add(cr3);
        environment.add(cr4);
        
        cs.preMissionLaunch();
        
        Simbad frame = new Simbad(environment, false);
        frame.update(frame.getGraphics()); 
	}

}
