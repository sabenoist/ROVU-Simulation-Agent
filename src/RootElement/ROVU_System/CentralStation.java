// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package RootElement.ROVU_System;

import java.awt.Color;

import javax.vecmath.Vector3d;

import RootElement.ROVU_System.Coordinate;
import RootElement.ROVU_System.Environment;
import RootElement.ROVU_System.Rover;
import RootElement.ROVU_System.RoverFactory;
import RootElement.ROVU_System.Subject;
import RootElement.ROVU_System.TaskFactory;
import RootElement.ROVU_System.Zone;

/************************************************************/
/**
 * 
 */
public class CentralStation extends Subject {

	private String mission;
	private double progress;
	private Environment environment;
	private TaskFactory taskFactory;
	private RoverFactory roverFactory;
	private Vector3d[] initPositions;
	private int finishedRovers;
	private static RootElement.ROVU_System.CentralStation instance = new CentralStation();

	private CentralStation() {
		taskFactory = TaskFactory.getInstance();
		roverFactory = RoverFactory.getInstance();
	}
	
	public void preRoverLaunch(){
		finishedRovers = 0;
		initPositions = new Vector3d[4];
	
		initPositions[0] = new Vector3d(-0.5, 0, 0.5);
		initPositions[1] = new Vector3d(-0.5, 0, -0.5);
		initPositions[2] = new Vector3d(0.5, 0, 0.5);
		initPositions[3] = new Vector3d(0.5, 0, -0.5);
		
		// zone 0
		environment.addBox(-4.5, 0.5);
		environment.addBox(-0.5, 2.5);
		
		// zone 1
		environment.addBox(-4.5, -0.5);
		environment.addBox(-0.5, -2.5);
	}
	
	public void preMissionLaunch(){
		genGrid();
		genZones();
		for(int i = 0; i < this.observers.size(); i++ ){
			assignZone((Rover)this.observers.get(i), i % 4);
		}
		//assignZone((Rover)this.observers.get(0), 2);
		//assignZone((Rover)this.observers.get(1), 1);
		//assignZone((Rover)this.observers.get(2), 2);
		//assignZone((Rover)this.observers.get(3), 3);
	}

	public Vector3d[] getInitPositions() {
		return initPositions;
	}

	public void genGrid(){
		int[] areaSize = environment.getAreaSize();
		Coordinate[][] grid = new Coordinate[areaSize[0]][areaSize[1]];
		
		int xAxis = areaSize[1]/2;
		int yAxis = areaSize[0]/2;
		
		for(int i = 0; i < areaSize[0]; i++ ){
			for(int j = 0; j < areaSize[1]; j++ ){				
				grid[i][j] = new Coordinate((i-areaSize[0]/2+1)-0.5, (j-areaSize[1]/2+1)-0.5);
			}
		}
		environment.printGrid(grid);
		//environment.utilBoxGrid(grid);
		environment.setGrid(grid);
	}
	
	public void genZones() {
		
		Zone z1 = new Zone(0);
		Zone z2 = new Zone(1);
		Zone z3 = new Zone(2);
		Zone z4 = new Zone(3);
		
		// generate grid zones here
		int[] areaSize = environment.getAreaSize();
		
		Coordinate[][] zoneGrid1 = new Coordinate[areaSize[0]/2][areaSize[1]/2];
		Coordinate[][] zoneGrid2 = new Coordinate[areaSize[0]/2][areaSize[1]/2];
		Coordinate[][] zoneGrid3 = new Coordinate[areaSize[0]/2][areaSize[1]/2];
		Coordinate[][] zoneGrid4 = new Coordinate[areaSize[0]/2][areaSize[1]/2];
		
		// z1: (-1, 1) ... (-5, 5)
		// z2: (-1, -1) ... (-5, -5)
		// z3: (1, 1) ... (5, 5)
		// z4: (1, -1) ... (5, -5)
		
		for(int i = 0; i < areaSize[0]/2; i++){
			for(int j = 0; j < areaSize[1]/2; j++){
				zoneGrid1[i][j] = new Coordinate(((i+0.5)*-1), (j+0.5));
				zoneGrid2[i][j] = new Coordinate(((i+0.5)*-1), ((j+0.5)*-1));
				zoneGrid3[i][j] = new Coordinate((i+0.5), (j+0.5));
				zoneGrid4[i][j] = new Coordinate((i+0.5), ((j+0.5)*-1));
			}
		}
		
		//util
		environment.printGrid(zoneGrid1);
		environment.printGrid(zoneGrid2);
		environment.printGrid(zoneGrid3);
		environment.printGrid(zoneGrid4);
		
		//util
		//environment.utilBoxZone(zoneGrid1, Color.BLUE);
		//environment.utilBoxZone(zoneGrid2, Color.RED);
		//environment.utilBoxZone(zoneGrid3, Color.YELLOW);
		//environment.utilBoxZone(zoneGrid4, Color.PINK);

		z1.setZoneGrid(zoneGrid1);
		z2.setZoneGrid(zoneGrid2);
		z3.setZoneGrid(zoneGrid3);
		z4.setZoneGrid(zoneGrid4);
		
		environment.setZone(z1, z1.getID());
		environment.setZone(z2, z2.getID());
		environment.setZone(z3, z3.getID());
		environment.setZone(z4, z4.getID());

	}

	public void assignZone(Rover r, int zoneID) {
		r.setZone(environment.getZone(zoneID));
	}

	public Zone getRoverZone(Rover r) {
		return r.getZone();
	}

	public boolean isObstacle(Coordinate c) {
		return c.isObstacle();
	}

	public void storeResults() {
	}

	public void startRover(Rover r) {
	}

	public void stopRover(Rover r) {
	}

	public void changeMission(String s) {
		if(!mission.equals(s)){ // change mission
			mission = s;
		}
	}

	public static RootElement.ROVU_System.CentralStation getInstance() {
		return instance;
	}
	
	public String getMission(){
		return mission;
	}
	public void setMission(String s){
		mission = s;
	}
	
	public double getProgress(){
		return progress;
	}
	public void updateProgressPlusOne(){
		progress++;
	}
	
	public Environment getEnvironment(){
		return environment;
	}
	public void setEnvironment(Environment env){
		environment = env;
	}
	
	public TaskFactory getTaskFactory(){
		return taskFactory;
	}
	
	public RoverFactory getRoverFactory(){
		return roverFactory;
	}	
	
	public void updateFinishedRovers(){
		finishedRovers++;
		if(finishedRovers == this.observers.size()/2){
			double total = environment.getGrid().length * environment.getGrid()[0].length;
			
			int obstacles = 0;;
			for(int i = 0; i < environment.getGrid().length; i++){
				for(int j = 0; j < environment.getGrid()[0].length; j++){
					if(environment.getGrid()[i][j].isObstacle())
						obstacles++;
				}
			}
			
			System.out.printf("Mission complete. Covered: %.0f%%/100%% --> %d obstacles\n", progress/total*100, obstacles);
		}
	}
	public void finishScouting(){
		this.setState(1);
	}
};
