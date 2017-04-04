// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package RootElement.ROVU_System;

import javax.vecmath.Vector3d;

import RootElement.ROVU_System.Coordinate;
import RootElement.ROVU_System.Environment;
import RootElement.ROVU_System.Rover;
import RootElement.ROVU_System.RoverFactory;
import RootElement.ROVU_System.Subject;
import RootElement.ROVU_System.Zone;

/************************************************************/
/**
 * 
 */
public class CentralStation extends Subject {

	private String mission;
	private double progress;
	private Environment environment;
	private RoverFactory roverFactory;
	private Vector3d[] initPositions;
	private int finishedRovers;
	private static CentralStation instance = new CentralStation();

	private CentralStation() {
		roverFactory = RoverFactory.getInstance();
		finishedRovers = 0;
	}
	
	public void preRoverLaunch(){
		initPositions = new Vector3d[8];
		initPositions[0] = new Vector3d(-0.5, 0, 0.5);
		initPositions[1] = new Vector3d(-0.5, 0, -0.5);
		initPositions[2] = new Vector3d(0.5, 0, 0.5);
		initPositions[3] = new Vector3d(0.5, 0, -0.5);
		initPositions[4] = new Vector3d(environment.worldSize/2+2, 0, environment.worldSize/2+2);
		initPositions[5] = new Vector3d(environment.worldSize/2+1, 0, environment.worldSize/2+2);
		initPositions[6] = new Vector3d(environment.worldSize/2+2, 0, environment.worldSize/2+1);
		initPositions[7] = new Vector3d(environment.worldSize/2+1, 0, environment.worldSize/2+1);
		
	}
	
	public void preMissionLaunch(){
		genGrid();
		genZones();
		for(int i = 0; i < this.observers.size(); i++ ){
			assignZone((Rover)this.observers.get(i), i % 4);
		}
		
		/** add obstacles **/
		// zone 0
//		environment.addBox(-4.5, 0.5);
		environment.addBox(-4.5, 1.5);
		environment.addBox(-1.5, 1.5);
		environment.addBox(-2.5, 1.5);
		environment.addBox(-3.5, 1.5);
		environment.addBox(-2.5, 3.5);
		
		// zone 1
		//environment.addBox(-4.5, -0.5);
		//environment.addBox(-0.5, -2.5);
	}

	public Vector3d[] getInitPositions() {
		return initPositions;
	}

	public void genGrid(){
		int[] areaSize = environment.getAreaSize();
		Coordinate[][] grid = new Coordinate[areaSize[0]][areaSize[1]];
		
		for(int i = 0; i < areaSize[0]; i++ ){
			for(int j = 0; j < areaSize[1]; j++ ){				
				grid[i][j] = new Coordinate((i-areaSize[0]/2+1)-0.5, (j-areaSize[1]/2+1)-0.5);
			}
		}
		environment.setGrid(grid);
	}
	
	public void genZones() {
		Zone z1 = new Zone(0);
		Zone z2 = new Zone(1);
		Zone z3 = new Zone(2);
		Zone z4 = new Zone(3);
		
		int[] areaSize = environment.getAreaSize();
		
		Coordinate[][] zoneGrid1 = new Coordinate[areaSize[0]/2][areaSize[1]/2];
		Coordinate[][] zoneGrid2 = new Coordinate[areaSize[0]/2][areaSize[1]/2];
		Coordinate[][] zoneGrid3 = new Coordinate[areaSize[0]/2][areaSize[1]/2];
		Coordinate[][] zoneGrid4 = new Coordinate[areaSize[0]/2][areaSize[1]/2];
		
		for(int i = 0; i < areaSize[0]/2; i++){
			for(int j = 0; j < areaSize[1]/2; j++){
				zoneGrid1[i][j] = new Coordinate(((i+0.5)*-1), (j+0.5));
				zoneGrid2[i][j] = new Coordinate(((i+0.5)*-1), ((j+0.5)*-1));
				zoneGrid3[i][j] = new Coordinate((i+0.5), (j+0.5));
				zoneGrid4[i][j] = new Coordinate((i+0.5), ((j+0.5)*-1));
			}
		}
		
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

	public void storeResults() {
		// store results
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
	
	public boolean isObstacle(Coordinate c){
		return c.isObstacle();
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
	
	public RoverFactory getRoverFactory(){
		return roverFactory;
	}	
	
	public void updateFinishedRovers(){
		finishedRovers++;
		if(finishedRovers == this.observers.size()/2){
			this.setState(2);
			double total = environment.getGrid().length * environment.getGrid()[0].length;
			
			int obstacles = 0;
			for(int k = 0; k < environment.getZones().length; k++){
				for(int i = 0; i < environment.getZone(k).getZoneGrid().length; i++){
					for(int j = 0; j < environment.getZone(k).getZoneGrid()[0].length; j++){
						if(environment.getZone(k).getZoneGrid()[i][j].isObstacle())
							obstacles++;
					}
				}
			}
			System.out.printf("Mission finished!\n");
			System.out.printf("Total grid points: %.0f, Covered grid points: %.0f, Obstacles: %d\n", total, progress, obstacles);
			System.out.printf("%.0f%% of the area covered, %.0f%% considering obstacles\n", (progress/total)*100, ((progress+obstacles)/total)*100);
			if(((progress+obstacles)/total*100) > 70){
				System.out.printf("Mission successful!\n");
			}else{
				System.out.printf("Mission failed.\n");
				this.setState(3);
			}
			
			storeResults();
		}
	}
	public void finishScouting(){
		this.setState(1);
	}
};
