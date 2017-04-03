// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package RootElement.ROVU_System;

import javax.vecmath.Vector3d;

import RootElement.ROVU_System.Rover;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;

/************************************************************/
/**
 * 
 */
public class CameraRover extends Rover {

	boolean running = true;
	int currentDirection;
	RangeSensorBelt sonar;
	int lastPicture = 0;
	int picturesTaken = 0;
	int moveuntil = 0;
	int start = 0;
	boolean started = false;
	
	// zone traversal
	Coordinate[][] zoneGrid;
	int grid_i = 0;
	int grid_j = 0;
	boolean traverseUp = true;
	Coordinate lastCoord;
	
	// obstacle avoidance
	Coordinate[] avoidMoves;
	int currentMove;
	int movesLeft;
	int movescheck;
	boolean obstacleDelay = false;
	
	// no GPS requirement
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
		// no GPS requirement
		currentPosition = new Coordinate(position.x, 0.3, position.z);
		lastCoord = new Coordinate(position.x, 0.3, position.z);
		avoidMoves = new Coordinate[10];
	}

	/** This method is called by the simulator engine on reset. */
    public void initBehavior() {
        System.out.printf("Initial behavior of %s is executed\n", this.getName());
        switch(this.getInitialDirection()) {
        	case NORTH: rotateY(-Math.PI); currentDirection = NORTH; break;
        	case SOUTH: currentDirection = SOUTH; break;
        	default: break;
        }   
        zoneGrid = this.getZone().getZoneGrid();
        movesLeft = 0;
        movescheck = 0;
        currentMove = 0;
        
        // obstacle cheating :^)
        /** 
        if(this.getZone().getID() % 4 == 0){
        	Coordinate c1 = this.getZone().getZoneCoord(-4.5, 0.5);
        	Coordinate c2 = this.getZone().getZoneCoord(-0.5, 2.5);
        	c1.setObstacle(true);
        	c2.setObstacle(true);
        }
        if(this.getZone().getID() % 4 == 1){
        	Coordinate c1 = this.getZone().getZoneCoord(-4.5, -0.5);
        	Coordinate c2 = this.getZone().getZoneCoord(-0.5, -2.5);
        	c1.setObstacle(true);
        	c2.setObstacle(true);
        }
        **/
    }
    
    /** This method is call cyclically (20 times per second) by the simulator engine. */
    public void performBehavior() {
    	
    	if(this.getSubject().getState() == 0){
    		running = false;
    		start = this.getCounter();
    	}
    	else if(this.getSubject().getState() == 1 && this.getCounter() > start + 40 && !started){
    		switch(this.getZone().getID()){
    		case 0: {
    			this.moveToPosition(new Vector3d(-0.5, 0, 0.5));
    			currentPosition = new Coordinate(-0.5, 0.5);
    			switch(this.getInitialDirection()) {
            	case NORTH: rotateY(-Math.PI); currentDirection = NORTH; break;
            	case SOUTH: currentDirection = SOUTH; break;
            	default: break;
    			}  
    			running = true;
    			started = true;
    			break;
    		}
    		case 1:{
    			this.moveToPosition(new Vector3d(-0.5, 0, -0.5));
    			currentPosition = new Coordinate(-0.5, -0.5);
    			switch(this.getInitialDirection()) {
            	case NORTH: rotateY(-Math.PI); currentDirection = NORTH; break;
            	case SOUTH: currentDirection = SOUTH; break;
            	default: break;
    			}
    			running = true;
    			started = true;
    			break;
    		}
    		case 2:{
    			this.moveToPosition(new Vector3d(0.5, 0, 0.5));
    			currentPosition = new Coordinate(0.5, 0.5);
    			switch(this.getInitialDirection()) {
            	case NORTH: rotateY(-Math.PI); currentDirection = NORTH; break;
            	case SOUTH: currentDirection = SOUTH; break;
            	default: break;
    			}
    			running = true;
    			started = true;
    			break;
    		}
    		case 3:{
    			this.moveToPosition(new Vector3d(0.5, 0, -0.5));
    			currentPosition = new Coordinate(0.5, -0.5);
    			switch(this.getInitialDirection()) {
            	case NORTH: rotateY(-Math.PI); currentDirection = NORTH; break;
            	case SOUTH: currentDirection = SOUTH; break;
            	default: break;
    			}
    			running = true;
    			started = true;
    			break;
    		}
    		}
    	}
    	else if( this.getSubject().getState() == 2){
    		running = false;
    	}
    	
    	if(!running){
			return;
		}
    	    	 
		if (this.getCounter() > 0 && this.getTranslationalVelocity() > 0) {
			Coordinate oldPos = currentPosition;
			double distance = this.getTranslationalVelocity() / TICK_RATE;
			switch (currentDirection) {
    			case NORTH:
    				currentPosition = new Coordinate(oldPos.getX()-distance, oldPos.getY(), oldPos.getZ());
    				break;
    			case EAST:
    				currentPosition = new Coordinate(oldPos.getX(), oldPos.getY(), oldPos.getZ()-distance);
    				break;
    			case SOUTH:
    				currentPosition = new Coordinate(oldPos.getX()+distance, oldPos.getY(), oldPos.getZ());
    				break;
    			case WEST:
    				currentPosition = new Coordinate(oldPos.getX(), oldPos.getY(), oldPos.getZ()+distance);
    				break;
			}
		}
    	
		if(this.getCounter() <= movescheck && movesLeft > 0){
			if(movesLeft == 1){
				movesLeft--;
				obstacleDelay = true;
			}
			else{			
				// round numbers
				double cur_x = currentPosition.getX()*100;
				double cur_z = currentPosition.getZ()*100;
				double dest_x = avoidMoves[currentMove].getX() * 100;
				double dest_z = avoidMoves[currentMove].getZ() * 100;
				cur_x = Math.round(cur_x);
				cur_x /= 100;
				cur_z = Math.round(cur_z);
				cur_z /= 100;
				dest_x = Math.round(dest_x);
				dest_x /= 100;
				dest_z = Math.round(dest_z);
				dest_z /= 100;
			
				moveFromTo(cur_x, cur_z, dest_x, dest_z, false);
				moveuntil += 4;
				currentMove++;
				movesLeft--;
			}
		}
		
    	if( this.getCounter() <= moveuntil ){
    		this.setStatus("forward");
    	}
    	else{
    		this.setStatus("taking picture preparation");
    		this.setTranslationalVelocity(0);
    		// round numbers
    		double x = currentPosition.getX()*100;
    		double z = currentPosition.getZ()*100;
    		x = Math.round(x);
    		z = Math.round(z);
    		x /= 100;
    		z /= 100;
    		lastCoord = new Coordinate(x, z);
    		movescheck = this.getCounter() + 1;
    	}
    	
    	if(movesLeft == 0){
    	Coordinate zoneCoord = this.getZone().getZoneCoord(lastCoord.getX(), lastCoord.getZ());
    	if( zoneCoord != null ){
    		if(!zoneCoord.isCovered()){
    			this.setTranslationalVelocity(0);
    			this.setStatus("taking pictures");
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
						cs.updateProgressPlusOne();
						
    					if(grid_i == zoneGrid.length-1 && grid_j == zoneGrid.length-1){
    						System.out.printf("%s has finished!\n", this.getName());
    						this.setStatus("finished");	
    						cs.updateFinishedRovers();
    						running = false;
    						return;
    					}
    					traverseNextPoint();    			    	
    					Coordinate nextDest = zoneGrid[grid_i][grid_j];

    					// round numbers
    					double cur_x = currentPosition.getX()*100;
    		    		double cur_z = currentPosition.getZ()*100;
    		    		double dest_x = nextDest.getX()*100;
    		    		double dest_z = nextDest.getZ()*100;
    		    		cur_x = Math.round(cur_x);
    		    		cur_x /= 100;
    		    		cur_z = Math.round(cur_z);
    		    		cur_z /= 100;
    		    		dest_x = Math.round(dest_x);
    		    		dest_x /= 100;
    		    		dest_z = Math.round(dest_z);
    		    		dest_z /= 100;
    					
    		    		boolean nextIsObstacle = nextDest.isObstacle();
    		    		if(nextIsObstacle){    		    			
    		    			traverseNextPoint();
    		    			Coordinate nextSkipObstacle = zoneGrid[grid_i][grid_j];
    		    			// two obstacles in a row not supported..
    		    			
    		    			// round numbers
    		    			double new_x = nextSkipObstacle.getX() * 100;
    		    			new_x = Math.round(new_x);
    		    			new_x /= 100;
        		    		double new_z = nextSkipObstacle.getZ() * 100;
        		    		new_z = Math.round(new_z);
        		    		new_z /= 100;
        		    		
        		    		double north = 0, east = 0, south = 0, west = 0;
        		    		if(new_x > cur_x){ // the new destination is south of current        		    			//System.out.printf("New destination is: %f to the south\n", new_x-cur_x);
        		    			south = new_x-cur_x;
        		    		}
        		    		else if(new_x < cur_x){ // the new destination is north of current
        		    			north = cur_x-new_x;
        		    		}
        		    		if(new_z > cur_z){ // the new destination is west of current
        		    			west = new_z-cur_z;
        		    		}
        		    		else if(new_z < cur_z){ // the new destination is east of current
        		    			east = cur_z-new_z;
        		    		}
        		    		// rotate to the right direction
        		    		moveFromTo(cur_x, cur_z, dest_x, dest_z, true);
        		    		if(north > 0){
        		    			if(west == 0 && east == 0){
        		    				Coordinate toLeft = this.getZone().getZoneCoord(cur_x, cur_z+1);
        		    				if( toLeft != null && !toLeft.isObstacle()){
        		    					currentMove = 0;
        		    					// go left
        		    					avoidMoves[currentMove] = new Coordinate(cur_x, cur_z+1);
        		    					currentMove++;
        		    					// go up for *north
        		    					for(int i = 1; i <= (int)north; i++){
        		    						avoidMoves[currentMove] = new Coordinate(cur_x-i, cur_z+1);
            		    					currentMove++;
        		    					}
        		    					// go right
        		    					avoidMoves[currentMove] = new Coordinate(cur_x-(int)north, cur_z);
    		    						currentMove = 0;
    		    						movesLeft = 1 + (int)north + 1 + 1;
    		    						movescheck = this.getCounter() + 1;
        		    				}
        		    				else{
        		    					Coordinate toRight = this.getZone().getZoneCoord(cur_x, cur_z-1);
        		    					if( toRight != null && !toRight.isObstacle()){
            		    					currentMove = 0;
            		    					// go right
            		    					avoidMoves[currentMove] = new Coordinate(cur_x, cur_z-1);
            		    					currentMove++;
            		    					// go up for *north
            		    					for(int i = 1; i <= (int)north; i++){
            		    						avoidMoves[currentMove] = new Coordinate(cur_x-i, cur_z-1);
                		    					currentMove++;
            		    					}
            		    					// go right
            		    					avoidMoves[currentMove] = new Coordinate(cur_x-(int)north, cur_z);
        		    						currentMove = 0;
        		    						movesLeft = 1 + (int)north + 1 + 1;
        		    						movescheck = this.getCounter() + 1;
        		    					}
        		    				}
        		    			}
        		    			else if (west > 0){
        		    				switch(currentDirection){
        		    				case NORTH:{
        		    					currentMove = 0;
            		    				// go west
    		    						avoidMoves[currentMove] = new Coordinate(cur_x, cur_z+1);
    		    						currentMove++;
    		    						// go north
    		    						avoidMoves[currentMove] = new Coordinate(cur_x-1, cur_z+1);
    		    						currentMove = 0;
    	    							movesLeft = 1 + 1 + 1;
    	    							movescheck = this.getCounter() + 1;
        		    					break;
        		    				}
        		    				case WEST:{
        		    					currentMove = 0;
            		    				// go north
    		    						avoidMoves[currentMove] = new Coordinate(cur_x-1, cur_z);
    		    						currentMove++;
    		    						// go west
    		    						avoidMoves[currentMove] = new Coordinate(cur_x-1, cur_z+1);
    		    						currentMove = 0;
    	    							movesLeft = 1 + 1 + 1;
    	    							movescheck = this.getCounter() + 1;
        		    					break;
        		    				}
        		    				}
        		    			}
        		    			else if( east > 0){
        		    				switch(currentDirection){
        		    				case NORTH:{
        		    					currentMove = 0;
            		    				// go east
    		    						avoidMoves[currentMove] = new Coordinate(cur_x, cur_z-1);
    		    						currentMove++;
    		    						// go north
    		    						avoidMoves[currentMove] = new Coordinate(cur_x-1, cur_z-1);
    		    						currentMove = 0;
    	    							movesLeft = 1 + 1 + 1;
    	    							movescheck = this.getCounter() + 1;
        		    					break;
        		    				}
        		    				case EAST:{
        		    					currentMove = 0;
            		    				// go east
    		    						avoidMoves[currentMove] = new Coordinate(cur_x-1, cur_z);
    		    						currentMove++;
    		    						// go north
    		    						avoidMoves[currentMove] = new Coordinate(cur_x-1, cur_z-1);
    		    						currentMove = 0;
    	    							movesLeft = 1 + 1 + 1;
    	    							movescheck = this.getCounter() + 1;
        		    					break;
        		    				}
        		    				}
        		    			}
        		    			
        		    		}else if( south > 0){
        		    			if(west == 0 && east == 0){        		    				
       		    					Coordinate toLeft = this.getZone().getZoneCoord(cur_x, cur_z-1);
       		    					if( toLeft != null && !toLeft.isObstacle()){
       		    						currentMove = 0;
       		    						// go left
       		    						avoidMoves[currentMove] = new Coordinate(cur_x, cur_z-1);
       		    						currentMove++;
       		    						// go down for *south
       		    						for(int i = 1; i <= (int)south; i++){
       		    							avoidMoves[currentMove] = new Coordinate(cur_x+i, cur_z-1);
           		    						currentMove++;
       		    						}
       		    						// go right
       		    						avoidMoves[currentMove] = new Coordinate(cur_x+(int)south, cur_z);
        		    					currentMove = 0;
    		    						movesLeft = 1 + (int)south + 1 + 1;
    		    						movescheck = this.getCounter() + 1;
        		    				}
        		    				else{
        		    					Coordinate toRight = this.getZone().getZoneCoord(cur_x, cur_z+1);
        		    					if( toRight != null && !toRight.isObstacle()){
        		    						currentMove = 0;
           		    						// go right
           		    						avoidMoves[currentMove] = new Coordinate(cur_x, cur_z+1);
           		    						currentMove++;
           		    						// go down for *south
           		    						for(int i = 1; i <= (int)south; i++){
           		    							avoidMoves[currentMove] = new Coordinate(cur_x+i, cur_z+1);
               		    						currentMove++;
           		    						}
           		    						// go left
           		    						avoidMoves[currentMove] = new Coordinate(cur_x+(int)south, cur_z);
            		    					currentMove = 0;
        		    						movesLeft = 1 + (int)south + 1 + 1;
        		    						movescheck = this.getCounter() + 1;
        		    					}
        		    				}	
        		    			}else if (west > 0){
        		    				switch(currentDirection){
        		    				case WEST:{
        		    					currentMove = 0;
            		    				// go south
    		    						avoidMoves[currentMove] = new Coordinate(cur_x+1, cur_z);
    		    						currentMove++;
    		    						// go west
    		    						avoidMoves[currentMove] = new Coordinate(cur_x+1, cur_z+1);
    		    						currentMove = 0;
    	    							movesLeft = 1 + 1 + 1;
    	    							movescheck = this.getCounter() + 1;
        		    					break;
        		    				}
        		    				case SOUTH:
        		    					currentMove = 0;
        		    					// go west
    		    						avoidMoves[currentMove] = new Coordinate(cur_x, cur_z+1);
    		    						currentMove++;
    		    						// go south
    		    						avoidMoves[currentMove] = new Coordinate(cur_x+1, cur_z+1);
    		    						currentMove = 0;
    	    							movesLeft = 1 + 1 + 1;
    	    							movescheck = this.getCounter() + 1;
        		    					break;
        		    				}
        		    			}
        		    			else if( east > 0){
        		    				switch(currentDirection){
        		    				case EAST:{
        		    					currentMove = 0;
            		    				// go south
    		    						avoidMoves[currentMove] = new Coordinate(cur_x+1, cur_z);
    		    						currentMove++;
    		    						// go east
    		    						avoidMoves[currentMove] = new Coordinate(cur_x+1, cur_z-1);
    		    						currentMove = 0;
    	    							movesLeft = 1 + 1 + 1;
    	    							movescheck = this.getCounter() + 1;
        		    					break;
        		    				}
        		    				case SOUTH:{
        		    					currentMove = 0;
            		    				// go east
    		    						avoidMoves[currentMove] = new Coordinate(cur_x, cur_z-1);
    		    						currentMove++;
    		    						// go south
    		    						avoidMoves[currentMove] = new Coordinate(cur_x+1, cur_z-1);
    		    						currentMove = 0;
    	    							movesLeft = 1 + 1 + 1;
    	    							movescheck = this.getCounter() + 1;
        		    					break;
        		    				}
        		    				}
        		    			}
        		    		}
    		    			this.setStatus("avoid obstacle");
    		    		}
    		    		else{
    		    			moveFromTo(cur_x, cur_z, dest_x, dest_z, false);
    		    			if(obstacleDelay){
    		    				// movement after avoiding an obstacle is delayed by 4
    		    				moveuntil += 4;
    		    				obstacleDelay = false;
    		    			}
    		    		}
    				}
    			}
    		}
    	}
    	}
    	    	
    	if(this.getCounter() % 5 == 0) {
	    	if(this.getStatus() == "forward") {
	    		this.setVelocity(0.5);
	            this.setTranslationalVelocity(this.getVelocity());  
	        }
    	}
    }
    
    void moveFromTo(double cur_x, double cur_z, double dest_x, double dest_z, boolean onlyRotate){
    	if(dest_x > cur_x){
			switch(currentDirection){
				case NORTH: rotateY(-(Math.PI)); currentDirection = (currentDirection + 2) % 4; break;
				case EAST: rotateY(-(Math.PI)/2); currentDirection = (currentDirection + 1) % 4; break;
				case SOUTH: break; // already south
				case WEST: rotateY((Math.PI)/2); currentDirection = (currentDirection + 3) % 4; break;
			}
		}
		else if(dest_x < cur_x){
			switch(currentDirection){
				case NORTH: break; // already north
				case EAST: rotateY((Math.PI)/2); currentDirection = (currentDirection + 3) % 4; break;
				case SOUTH: rotateY(-(Math.PI)); currentDirection = (currentDirection + 2) % 4; break;
				case WEST: rotateY(-(Math.PI)/2); currentDirection = (currentDirection + 1) % 4; break;
			}
		}
		else if(dest_z > cur_z){
			switch(currentDirection){
				case NORTH: rotateY((Math.PI)/2); currentDirection = (currentDirection + 3) % 4; break;
				case EAST: rotateY(-(Math.PI)); currentDirection = (currentDirection + 2) % 4; break;
				case SOUTH: rotateY(-(Math.PI)/2); currentDirection = (currentDirection + 1) % 4; break;
				case WEST: break; // already west
			}
		}
		else if(dest_z < cur_z){
			switch(currentDirection){
				case NORTH: rotateY(-(Math.PI)/2); currentDirection = (currentDirection + 1) % 4; break;
				case EAST: break; // already east
				case SOUTH: rotateY((Math.PI)/2); currentDirection = (currentDirection + 3) % 4; break;
				case WEST: rotateY(-(Math.PI)); currentDirection = (currentDirection + 2) % 4; break;
			}
		}
    	if(!onlyRotate){
    		moveuntil = this.getCounter() + 39;
			// initial movement is delayed by 4
			if(grid_i == 1 && grid_j == 0){
				moveuntil += 4;
			}
			this.setStatus("forward");
    	}
    }
    
    void traverseNextPoint(){
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
    }
    
	@Override
	public void update() {
		// update
	}
	
	void takePicture(){
		// take picture
	}
};
