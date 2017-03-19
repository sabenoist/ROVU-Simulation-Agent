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
        
        // add at least one rover otherwise the environment crashes...
        
        ScoutingRover sr1 = new ScoutingRover(cs.getInitPositions()[0], "Scouting Rover 1", cs, 0);
        environment.add(sr1);
        ScoutingRover sr2 = new ScoutingRover(cs.getInitPositions()[1], "Scouting Rover 2", cs, 0);
        environment.add(sr2);
        ScoutingRover sr3 = new ScoutingRover(cs.getInitPositions()[2], "Scouting Rover 3", cs, 2);
        environment.add(sr3);
        ScoutingRover sr4 = new ScoutingRover(cs.getInitPositions()[3], "Scouting Rover 4", cs, 2);
        environment.add(sr4);
                
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
	}

}
