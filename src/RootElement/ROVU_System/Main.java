package RootElement.ROVU_System;

import simbad.gui.*;
import simbad.sim.*;

import java.awt.Color;

import javax.vecmath.Vector3d;

import nl.vu.cs.s2.simbadtest.ExampleEnvironment;
import nl.vu.cs.s2.simbadtest.ExampleRobot;

public class Main {
		
	public static void main(String[] args) {
        // request antialising so that diagonal lines are not "stairy"
        System.setProperty("j3d.implicitAntialiasing", "true");
        
        CentralStation cs = CentralStation.getInstance();  
        cs.setMission("Catch them all.");
        EnvironmentDescription environment = new Environment(10, 10);
        
        cs.setEnvironment((Environment)environment);
        cs.preRoverLaunch();
                
        ScoutingRover sr1 = new ScoutingRover(cs.getInitPositions()[0], "Scouting Rover 1", cs, 0);
        environment.add(sr1);
        ScoutingRover sr2 = new ScoutingRover(cs.getInitPositions()[1], "Scouting Rover 2", cs, 0);
        environment.add(sr2);
        ScoutingRover sr3 = new ScoutingRover(cs.getInitPositions()[2], "Scouting Rover 3", cs, 2);
        environment.add(sr3);
        ScoutingRover sr4 = new ScoutingRover(cs.getInitPositions()[3], "Scouting Rover 4", cs, 2);
        environment.add(sr4);
        
        CameraRover cr1 = new CameraRover(new Vector3d(environment.worldSize/2+2, 0, environment.worldSize/2+2), "Camera Rover 1", cs, 0);
        environment.add(cr1);
        CameraRover cr2 = new CameraRover(new Vector3d(environment.worldSize/2+1, 0, environment.worldSize/2+2), "Camera Rover 2", cs, 0);
        environment.add(cr2);
        CameraRover cr3 = new CameraRover(new Vector3d(environment.worldSize/2+2, 0, environment.worldSize/2+1), "Camera Rover 3", cs, 2);
        environment.add(cr3);
        CameraRover cr4 = new CameraRover(new Vector3d(environment.worldSize/2+1, 0, environment.worldSize/2+1), "Camera Rover 4", cs, 2);
        environment.add(cr4);
        
        cs.preMissionLaunch();
        
        Simbad frame = new Simbad(environment, false);
        frame.update(frame.getGraphics());
        
	}

}
