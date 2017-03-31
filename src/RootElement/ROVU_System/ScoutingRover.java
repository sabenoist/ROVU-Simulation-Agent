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
public class ScoutingRover extends Rover {

	private int proximity;
	boolean running = true;
	int proxcheck = 0;
	int zonecheck = 0;
	int currentDirection;
	RangeSensorBelt sonar;
	
	//	camera rover stuff
	Coordinate[][] zoneGrid;
	int grid_i = 1;
	int grid_j = 0;
	
	// gps req
	Coordinate currentPosition;
	
	public ScoutingRover(Vector3d position, String name, Subject s, int initdir) {
		super(position, name);
		this.setInitialPosition(new Coordinate(position.x, position.y, position.z));
		this.setRoverName(name);
		this.setSubject(s);
		this.getSubject().attach(this);
		this.setInitialDirection(initdir);
		this.setType(RoverEnum.SCOUTING_ROVER);
		sonar = RobotFactory.addSonarBeltSensor(this, 4);
		// gps req 
		currentPosition = new Coordinate(position.x, 0.3, position.z);
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
    	
    	//System.out.printf("Min:(%.1f,%.1f) Max:(%.1f,%.1f)\n", zoneGrid[0][0].getX(),zoneGrid[0][0].getZ(), zoneGrid[zoneGrid.length-1][zoneGrid.length-1].getX(), zoneGrid[zoneGrid.length-1][zoneGrid.length-1].getZ());
    	if( this.getCounter() > zonecheck )
    	{
    	Point3d cur = new Point3d();
        this.getCoords(cur);
        //System.out.printf("The current direction is: %d\n", currentDirection);
        switch(currentDirection){
        case 0:{ // north
        	
        	boolean outOfBounds = false;
        	switch(this.getZone().getID()){
        		case 0: outOfBounds = checkOutOfBoundsMaxX(cur); break;
        		case 1: outOfBounds = checkOutOfBoundsMaxX(cur); break;
        		case 2: outOfBounds = checkOutOfBoundsMinX(cur); break;
        		case 3: outOfBounds = checkOutOfBoundsMinX(cur); break;
        	}
        	if(outOfBounds){
        		turnAway();
                //return;
        	}
        	break;
        }
        case 1:{ // east

        	boolean outOfBounds = false;
        	switch(this.getZone().getID()){
        		case 0: outOfBounds = checkOutOfBoundsMinZ(cur); break;
        		case 1: outOfBounds = checkOutOfBoundsMaxZ(cur); break;
        		case 2: outOfBounds = checkOutOfBoundsMinZ(cur); break;
        		case 3: outOfBounds = checkOutOfBoundsMaxZ(cur); break;
        	}
        	if(outOfBounds){
        		turnAway();
                //return;
        	}
        	break;
        }
        case 2:{ // south

        	boolean outOfBounds = false;
        	switch(this.getZone().getID()){
        		case 0: outOfBounds = checkOutOfBoundsMinX(cur); break;
        		case 1: outOfBounds = checkOutOfBoundsMinX(cur); break;
        		case 2: outOfBounds = checkOutOfBoundsMaxX(cur); break;
        		case 3: outOfBounds = checkOutOfBoundsMaxX(cur); break;
        	}
        	
        	if(outOfBounds){
        		turnAway();
                //return;
        	}
        	break;
        }
        case 3:{ // west
        	
        	boolean outOfBounds = false;
        	switch(this.getZone().getID()){
        		case 0: outOfBounds = checkOutOfBoundsMaxZ(cur); break;
        		case 1: outOfBounds = checkOutOfBoundsMinZ(cur); break;
        		case 2: outOfBounds = checkOutOfBoundsMaxZ(cur); break;
        		case 3: outOfBounds = checkOutOfBoundsMinZ(cur); break;
        	}
        	
        	if(outOfBounds){
        		turnAway();
                //return;
        	}
        	break;
        }
        }
    	}
    	
    	if( enableRotate && this.getCounter() > 0 && this.getCounter() > zonecheck && this.getCounter() % 80 == 0 ) // every 2 meter with 0.5ms
    	{
            Point3d loc = new Point3d();
            this.getCoords(loc);
    		Random rand = new Random();
        	int randomValue = rand.nextInt(2); // 0-3: left/right/straight
        	
        	
        	double l_x = Math.round(loc.getX() * 10) / 10;
        	double l_z = Math.round(loc.getZ() * 10) / 10;
        	
        	System.out.printf("x: %f, z: %f\n", l_x, l_z);
        	if(l_x % 0.5 == 0 && l_z % 0.5 == 0){
        	System.out.printf("Turn allowed\n");
        	switch(randomValue){
        		case 0: rotateY(-(Math.PI / 2)); currentDirection = (currentDirection + 1) % 4; break;
        		case 1: rotateY(Math.PI / 2); currentDirection = currentDirection-1; if(currentDirection<0)currentDirection=3;break;
        		default: break;
        	}
        	}
            
        	/*
        	switch(this.getZone().getID()){
        	case 0: rotateY(Math.PI / 2); currentDirection = currentDirection-1; if(currentDirection<0)currentDirection=3;break;
        	case 1: rotateY(-(Math.PI / 2)); currentDirection = (currentDirection + 1) % 4; break;
        	case 2: rotateY(Math.PI / 2); currentDirection = currentDirection-1; if(currentDirection<0)currentDirection=3;break;
        	case 3: rotateY(-(Math.PI / 2)); currentDirection = (currentDirection + 1) % 4; break;
        	}*/
        	
        	//System.out.printf("changed direction\n");

            //System.out.printf("%s[%d]: [X(%.1f) Y(%.1f) Z(%.1f)]\n", this.getName(), this.getFramesPerSecond(), loc.getX(), loc.getY(), loc.getZ());
    	}
    	
    	
    	if( this.getCounter() > proxcheck && sonar.getMeasurement(0) <= 0.2 ){
    		//System.out.printf("Counter before: %d\n", this.getCounter());
			Point3d loc = new Point3d();
            this.getCoords(loc);
            System.out.printf("Obj Detected from: [X(%.1f) Y(%.1f) Z(%.1f)]\n", loc.getX(), loc.getY(), loc.getZ());
            // do something to store the fact
            double x = loc.getX();
            double y = loc.getY();
            double z = loc.getZ();
            switch(currentDirection){
            	case 0: x-=1; break;
            	case 1: z-=1; break;
            	case 2: x+=1; break;
            	case 3: z+=1; break;
            }
            //x = Math.round(x*2) / 2.0f;
            //z = Math.round(z*2) / 2.0f;
            System.out.printf("Object at: [X(%.1f) Y(%.1f) Z(%.1f)]\n", x, y, z);
            this.setTranslationalVelocity(0);
            rotateY(-(Math.PI / 2));
            //rotateY((Math.PI));
            currentDirection = (currentDirection + 1) % 4;
            // do not instantly check again otherwise itll turn twice
            proxcheck = this.getCounter() + 10;
    		//System.out.printf("Counter after: %d\n", this.getCounter());
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
    	
    	if(this.getCounter() % 20 == 0 ){
    		Point3d loc = new Point3d();
            this.getCoords(loc);            
    		System.out.printf("(%s) GPS position: [%.1f - %.1f - %.1f]\n", this.getName(), loc.getX(), loc.getY(), loc.getZ());
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
	    	} else {
	    		this.setStatus("forward");
	    	}
	        
	    	
	    	if(this.getStatus() == "forward") {
	    		// the robot's speed is always 0.5 m/s
	            this.setTranslationalVelocity(0.5);  
	        } else {
	        	// collision detected -> do sth
	        	Point3d loc = new Point3d();
	            this.getCoords(loc);
	            System.out.printf("CollDet: [X(%.1f) Y(%.1f) Z(%.1f)]\n", loc.getX(), loc.getY(), loc.getZ());
	        }
    	}
    	
    }
    
    boolean checkOutOfBoundsMaxX(Point3d pos){
    	if(Math.abs(pos.getX()) > Math.abs(zoneGrid[zoneGrid.length-1][zoneGrid.length-1].getX())){
    		return true;
    	}
    	return false;
    }
    boolean checkOutOfBoundsMinX(Point3d pos){
    	if(Math.abs(pos.getX()) < Math.abs(zoneGrid[0][0].getX())){
    		return true;
    	}
    	return false;
    }
    boolean checkOutOfBoundsMaxZ(Point3d pos){
    	if(Math.abs(pos.getZ()) > Math.abs(zoneGrid[zoneGrid.length-1][zoneGrid.length-1].getZ())){
    		return true;
    	}
    	return false;
    }
    boolean checkOutOfBoundsMinZ(Point3d pos){
    	if(Math.abs(pos.getZ()) < Math.abs(zoneGrid[0][0].getZ())){
    		return true;
    	}
    	return false;
    }

    void turnAway(){
		this.setTranslationalVelocity(0);
		rotateY(-(Math.PI / 2));
        currentDirection = (currentDirection + 1) % 4;
        zonecheck = this.getCounter() + 10;
    }
    
	@Override
	public void update() {
		System.out.printf("i updated lol\n");
		
	}
};
