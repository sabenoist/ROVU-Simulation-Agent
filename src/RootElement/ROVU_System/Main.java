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
        
        Rover r1 = new ScoutingRover(cs.getInitPositions()[0], "Scouting Rover 1", cs, 0);
        environment.add(r1);
        Rover r2 = new ScoutingRover(cs.getInitPositions()[1], "Scouting Rover 2", cs, 0);
        environment.add(r2);
        Rover r3 = new ScoutingRover(cs.getInitPositions()[2], "Scouting Rover 3", cs, 2);
        environment.add(r3);
        Rover r4 = new ScoutingRover(cs.getInitPositions()[3], "Scouting Rover 4", cs, 2);
        environment.add(r4);
        
        // add at least one rover otherwise the environment crashes...
        /*
        CameraRover cr1 = new CameraRover(cs.getInitPositions()[0], "Camera Rover 1", cs, 0);
        environment.add(cr1);
        CameraRover cr2 = new CameraRover(cs.getInitPositions()[1], "Camera Rover 2", cs, 0);
        environment.add(cr2);
        CameraRover cr3 = new CameraRover(cs.getInitPositions()[2], "Camera Rover 3", cs, 2);
        environment.add(cr3);
        CameraRover cr4 = new CameraRover(cs.getInitPositions()[3], "Camera Rover 4", cs, 2);
        environment.add(cr4);*/
        
        /*ScoutingRover sr1 = new ScoutingRover(cs.getInitPositions()[0], "Scouting Rover 1", cs, 0);
        environment.add(sr1);
        ScoutingRover sr2 = new ScoutingRover(cs.getInitPositions()[1], "Scouting Rover 2", cs, 0);
        environment.add(sr2);
        ScoutingRover sr3 = new ScoutingRover(cs.getInitPositions()[2], "Scouting Rover 3", cs, 2);
        environment.add(sr3);
        ScoutingRover sr4 = new ScoutingRover(cs.getInitPositions()[3], "Scouting Rover 4", cs, 2);
        environment.add(sr4);*/
                
        cs.preMissionLaunch();
        
        //cs.getEnvironment().printGrid(sr1.getZone().getZoneGrid());
        
        /*
		cs.getEnvironment().utilBoxZone(sr1.getZone().getZoneGrid(), Color.BLUE);
		cs.getEnvironment().utilBoxZone(sr2.getZone().getZoneGrid(), Color.RED);
		cs.getEnvironment().utilBoxZone(sr3.getZone().getZoneGrid(), Color.YELLOW);
		cs.getEnvironment().utilBoxZone(sr4.getZone().getZoneGrid(), Color.PINK);
        */
        
        Simbad frame = new Simbad(environment, false);
        frame.update(frame.getGraphics());
        
        while(cs.getState() != 1){
        	/* wait */
        }
        
        r1 = new CameraRover(cs.getInitPositions()[0], "Camera Rover 1", cs, 0);
        environment.add(r1);
        r2 = new CameraRover(cs.getInitPositions()[1], "Camera Rover 2", cs, 0);
        environment.add(r2);
        r3 = new CameraRover(cs.getInitPositions()[2], "Camera Rover 3", cs, 2);
        environment.add(r3);
        r4 = new CameraRover(cs.getInitPositions()[3], "Camera Rover 4", cs, 2);
        environment.add(r4);
        
        frame = new Simbad(environment, false);
        frame.update(frame.getGraphics());
        System.out.printf("CAMERA ROVERS GO!\n");
        
	}

}
