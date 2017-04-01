// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package RootElement.ROVU_System;

import java.util.Random;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import RootElement.ROVU_System.Rover;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;

/************************************************************/
/**
 * 
 */
public class CameraRover extends Rover {

	int proximity;
	boolean running = true;
	int proxcheck = 0;
	int zonecheck = 0;
	int currentDirection;
	RangeSensorBelt sonar;
	int lastPicture = 0;
	int picturesTaken = 0;
	int moveuntil = 0;
	
	//	camera rover stuff
	Coordinate[][] zoneGrid;
	int grid_i = 0;
	int grid_j = 0;
	boolean traverseUp = true;
	Coordinate lastCoord;
	
	// gps req
	Coordinate currentPosition;
	
	public CameraRover(Vector3d position, String name, Subject s, int initdir) {
		super(position, name);
		this.setInitialPosition(new Coordinate(position.x, position.y, position.z));
		this.setRoverName(name);
		this.setSubject(s);
		this.getSubject().attach(this);
		this.setInitialDirection(initdir);
		this.setType(RoverEnum.CAMERA_ROVER);
		sonar = RobotFactory.addSonarBeltSensor(this, 4);
		// gps req 
		currentPosition = new Coordinate(position.x, 0.3, position.z);
		lastCoord = new Coordinate(position.x, 0.3, position.z);
	}
	/*public int getProximity(){
		return proximity;
	}*/
	public void setProximity(int p){
		proximity = p;
	}
	
	public void genRandomDirection(int direction) {
	}

	public void startScouting() {
	}

	/** This method is called by the simulator engine on reset. */
    public void initBehavior() {
        System.out.printf("I exist and my name is %s\n", this.getName());
        switch(this.getInitialDirection()) {
        	case 0: rotateY(-Math.PI); currentDirection = 0; break; // north
        	case 2: currentDirection = 2; break; // south (default)
        	default: break;
        }   
       // camera rover stuff
        zoneGrid = this.getZone().getZoneGrid();
        zonecheck = 0;
        proxcheck = 0;        
    }

    boolean enableRotate = true;
    
    /** This method is call cyclically (20 times per second) by the simulator engine. */
    public void performBehavior() {
    	
    	if(!running){
			return;
		}
    	
    	if(this.getCounter() % 100 == 0){
			CentralStation cs = (CentralStation)this.getSubject();
    		System.out.printf("Progress: %.1f\n", cs.getProgress());
    	}
    	 
    	
    	if(this.getCounter() > 0 && this.getTranslationalVelocity() > 0){
    		Coordinate oldPos = currentPosition;
    		switch(currentDirection){
    		// getvelocity/20 = 0.025
    		case 0: // north
    			currentPosition = new Coordinate(oldPos.getX()-0.025, oldPos.getY(), oldPos.getZ());
    			break;
    		case 1: // east
    			currentPosition = new Coordinate(oldPos.getX(), oldPos.getY(), oldPos.getZ()-0.025);
    			break;
    		case 2: // south
    			currentPosition = new Coordinate(oldPos.getX()+0.025, oldPos.getY(), oldPos.getZ());
    			break;
    		case 3: // west
    			currentPosition = new Coordinate(oldPos.getX(), oldPos.getY(), oldPos.getZ()+0.025);
    			break;
    		}
    	}
    	
    	if( this.getCounter() <= moveuntil ){
    		this.setStatus("forward");
    	}
    	else{
    		this.setStatus("transitioning to taking pictures idk");
    		this.setTranslationalVelocity(0);
    		
    		double x = currentPosition.getX()*100;
    		double z = currentPosition.getZ()*100;
    		x = Math.round(x);
    		z = Math.round(z);
    		x /= 100;
    		z /= 100;
    		
    		lastCoord = new Coordinate(x, z);
    		//System.out.printf("trying to find: %f - %f\n", this.zoneGrid[1][0].getX(), this.zoneGrid[1][0].getZ());
    	}
    	
    	Coordinate zoneCoord = this.getZone().getZoneCoord(lastCoord.getX(), lastCoord.getZ());
    	if( zoneCoord != null ){
    		if(!zoneCoord.isCovered()){
    			this.setTranslationalVelocity(0);
    			this.setStatus("TakingPictures");
    			if(this.getCounter() >= lastPicture){
    				takePicture();
    				rotateY(-(Math.PI / 2));
    				currentDirection = (currentDirection + 1) % 4;
    				lastPicture = this.getCounter() + 5;
    				picturesTaken++;
    				
    				if(picturesTaken == 4){
    					zoneCoord.setCovered(true);
    					picturesTaken = 0;
    					
						CentralStation cs = (CentralStation)this.getSubject();
						cs.updateProgess(cs.getProgress()+1);
    					
    					if(grid_i == zoneGrid.length-1 && grid_j == zoneGrid.length-1){
    						System.out.printf("DONE!\n");
    						this.setStatus("finished");		
    						cs.updateFinishedRovers();
    						running = false;
    						return;
    					}
    					
    			    	if(traverseUp){
    			    		grid_i++;
    			    		if( grid_i == zoneGrid.length ){
    			    			grid_i--;
    			    			grid_j++;
    			    			traverseUp=false;
    			    		}
    			    	}else{
    			    		grid_i--;
    			    		if(grid_i < 0){
    			    			grid_i++;
    			    			grid_j++;
    			    			traverseUp=true;
    			    		}
    			    	}
    			    	
    					Coordinate nextDest = zoneGrid[grid_i][grid_j];
    					//System.out.printf("Dest: [%d][%d]: %f ~ %f   ... current: %f ~ %f\n", grid_i, grid_j, nextDest.getX(), nextDest.getZ(), currentPosition.getX(), currentPosition.getZ());
    					
    					// afronden
    					double cur_x = currentPosition.getX()*100;
    		    		double cur_z = currentPosition.getZ()*100;
    		    		cur_x = Math.round(cur_x);
    		    		cur_x /= 100;
    		    		cur_z = Math.round(cur_z);
    		    		cur_z /= 100;
    		    		
    		    		double dest_x = nextDest.getX() * 100;
    		    		dest_x = Math.round(dest_x);
    		    		dest_x /= 100;
    		    		double dest_z = nextDest.getZ() * 100;
    		    		dest_z = Math.round(dest_z);
    		    		dest_z /= 100;
    		    		//System.out.printf("dest: %f~%f ... cur: %f~%f\n", dest_x, dest_z, cur_x, cur_z);
    					
    					if(dest_x > cur_x){
    						//System.out.printf("GO SOUTH\n");
    						// go south
    						switch(currentDirection){
    							case 0: rotateY(-(Math.PI)); currentDirection = (currentDirection + 2) % 4; break;
    							case 1: rotateY(-(Math.PI)/2); currentDirection = (currentDirection + 1) % 4; break;
    							case 2: break; // already south
    							case 3: rotateY((Math.PI)/2); currentDirection = currentDirection - 1; if(currentDirection<0)currentDirection=3; break;
    						}
    					}
    					else if(dest_x < cur_x){
    						//System.out.printf("GO NORTH\n");
    						// go north
    						switch(currentDirection){
								case 0: break; // already north
								case 1: rotateY((Math.PI)/2); currentDirection = currentDirection - 1; if(currentDirection<0)currentDirection=3; break;
								case 2: rotateY(-(Math.PI)); currentDirection = (currentDirection + 2) % 4; break;
								case 3: rotateY(-(Math.PI)/2); currentDirection = (currentDirection + 1) % 4; break;
    						}
        					//System.out.printf("My new direction is: %d\n", currentDirection);
    					}
    					else if(dest_z > cur_z){
    						//System.out.printf("GO WEST\n");
    						// go west
    						switch(currentDirection){
								case 0: rotateY((Math.PI)/2); currentDirection = currentDirection - 1; if(currentDirection<0)currentDirection=3; break;
								case 1: rotateY(-(Math.PI)); currentDirection = (currentDirection + 2) % 4; break;
								case 2: rotateY(-(Math.PI)/2); currentDirection = (currentDirection + 1) % 4; break;
								case 3: break; // already west
    						}
    					}
    					else if(dest_z < cur_z){
    						//System.out.printf("GO EAST\n");
    						// go east
    						switch(currentDirection){
								case 0: rotateY(-(Math.PI)/2); currentDirection = (currentDirection + 1) % 4; break;
								case 1: break; // already east
								case 2: rotateY((Math.PI)/2); currentDirection = currentDirection - 1; if(currentDirection<0)currentDirection=3; break;
								case 3: rotateY(-(Math.PI)); currentDirection = (currentDirection + 2) % 4; break;
    						}
    					}
    					moveuntil = this.getCounter() + 39;
    					this.setStatus("forward");
    				}
    			}
    		}
    		else{
    			//moveToNextPosition();
    		}
    	}else{
    		Point3d loc = new Point3d();
            this.getCoords(loc);  
    		System.out.printf("Not found: %f ~ %f ... (%f ~ %f)\n", currentPosition.getX(), currentPosition.getZ(), loc.getX(), loc.getZ());
    		
    	}
    	
    	if(this.getCounter() % 20 == 0 ){
    		Point3d loc = new Point3d();
            this.getCoords(loc);            
    		//System.out.printf("(%s) GPS position: [%.1f - %.1f - %.1f]\n", this.getName(), loc.getX(), loc.getY(), loc.getZ());
    		//System.out.printf("(%s) New position: [%.1f - %.1f - %.1f]\n", this.getName(), currentPosition.getX(), currentPosition.getY(), currentPosition.getZ());
    	}
    	
    	// perform the following actions every 5 virtual seconds
    	if(this.getCounter() % 5 == 0) {
    		/* camera rover stuff
    		Point3d loc = new Point3d();
            this.getCoords(loc);
            System.out.printf("Looking for x: %f\n", zoneGrid[grid_i][grid_j].getX());
            if(loc.getX() <= zoneGrid[grid_i][grid_j].getX())
            {
            	System.out.printf("Caught at: %f\n", loc.getX());
    			this.setTranslationalVelocity(0);
    			grid_i++;
    			running = false;
    			count = this.getCounter() + 100;
            	return;
            }
            */
    		//System.out.printf("Proximity: %f\n", sonar.getMeasurement(0));
	    	if(this.collisionDetected()) {
	    		this.setStatus("avoidObstacle");
	    	}
	        
	    	if(this.getStatus() == "forward") {
	    		//System.out.printf("moving bih\n");
	            this.setTranslationalVelocity(0.5);  
	        } else if (this.getStatus() == "avoidObstacle"){
	        	Point3d loc = new Point3d();
	            this.getCoords(loc);
	            System.out.printf("CollDet: [X(%.1f) Y(%.1f) Z(%.1f)]\n", loc.getX(), loc.getY(), loc.getZ());
	        }
    	}
    	
    }
    
	@Override
	public void update() {
		System.out.printf("i updated lol\n");
	}
	
	void takePicture(){
		//System.out.printf("[%d] Picture taken!\n", this.getCounter());
	}
};
