package tile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import edge.Edge;
import token.Firefighter;
import token.Hazmat;
import token.POI;
import token.Vehicle;

/**
 * Tile class definition.
 * Generated by the TouchCORE code generator.
 * modified by @matekrk
 */
public class Tile implements Serializable{
    
	private static final long serialVersionUID = 1L;
	protected int fire;
    protected int[] coords;
    protected int hotspot;
    protected Edge[] adjacentEdges;
    protected ArrayList<Firefighter> listOfFirefighters;
    protected int x;
    protected int y;
    protected ArrayList<POI> poiList;
    protected ArrayList<Hazmat> hazmatList;
    protected boolean isInterior;
    protected ParkingSpot pointerParkingSpot; 
    protected ParkingSpot nearestAmbulance;
    protected Vehicle ParkingType; 
    
    public Tile(boolean isInterior, int[] coords) {
        this.x = coords[0];
        this.y = coords[1];
        this.isInterior = isInterior;
        adjacentEdges = new Edge[4];
        poiList = new ArrayList<POI>();
        hazmatList = new ArrayList<Hazmat>();
        listOfFirefighters = new ArrayList<Firefighter>();
        this.pointerParkingSpot = null;
    }

    /*
     * GETTERS
     */
    
    public int getFire() {
        return fire;
    }

    public boolean containsFirefighter() {
        /* TODO: No message view defined */
        return !(listOfFirefighters.isEmpty());
    }

    public boolean containsPOI() {
        /* TODO: No message view defined */
    	return !(poiList.isEmpty());
    }

    public ArrayList<POI> getPoiList() {
        return poiList;
    }

    public ArrayList<Firefighter> getFirefighterList() {
        return listOfFirefighters;
    }
    
    public int[] getCoords() {
        return new int[] {x,y};
    }

    public boolean checkInterior() {
		return this.isInterior;
    	
    }
    
    
    public void setEdge(int direction, Edge e) {
    	adjacentEdges[direction] = e;
    }
    //left: 0, top: 1, right: 2, down: 3
    public Edge getEdge(int direction) {
    	return adjacentEdges[direction];
    }
    
    public int getX() {
    	return x;
    }
    
    public int getY() {
    	return y;
    }
    
    public ParkingSpot getParkingSpot() {
    	return pointerParkingSpot;
    }
    
    /*
     * SETTERS
     */
    
    public void setFire(int amount) {
    	if (amount <= 2 && amount >=0) {
    		fire = amount;
    	}
    }

    public void setPOIList(ArrayList<POI> poiList) {
    	this.poiList = poiList;
    }
    
    public void addPoi(POI newPoi) {
        poiList.add(newPoi);
    }
    
    public POI removeFirstPoi() {
    	return poiList.remove(0);
    }

    public void removeFromFirefighterList(Firefighter target) {
        listOfFirefighters.remove(target);
    }
    
    
    public void addToFirefighterList(Firefighter target) {
    	listOfFirefighters.add(target);
    }
    
    public Firefighter removeFirstFireFighter() {
    	return listOfFirefighters.remove(0);
    }

    public void setCoords(int[] coords) {
        x = coords[0];
        y = coords[1];
    }
    
    public void setParkingType(Vehicle parkingType) {
    	this.ParkingType = parkingType;
    }
    
    public Vehicle getParkingType() {
    	return this.ParkingType;
    }
    
    public void setParkingSpot(ParkingSpot pointerParkingSpot) {
    	this.pointerParkingSpot = pointerParkingSpot;
    }
    
    public void setNearestAmbulance(ParkingSpot nearestAmbulance) {
    	this.nearestAmbulance = nearestAmbulance;
    }
    
    /*
     * some algos/methods
     */
    

    public boolean checkBarriers(int direction) {
        /* TODO: No message view defined */
    	if(this.getEdge(direction).isWall()) {
    		if(this.getEdge(direction).getDamage() == 0) {
    			return false;
    		}
    		return true;
    	}
    	//Doors do not stop explosions
    	else if(this.getEdge(direction).isDoor()) {
    		if(this.getEdge(direction).isDestroyed()) {
    			return false;
    		} else if(this.getEdge(direction).getStatus()) {
    			return false;
    		}
    		return true;
    	}
        return false;
    }

    //good shit! thank you
    //may need to have this give two, in the chance that the player is equally in between two parking spots and should be given the choice of respawning.
    public ParkingSpot getNearestAmbulance() {
        return this.nearestAmbulance;
    }


    public void removeFromPoiList(POI target) {
        poiList.remove(target);
    }

	public void increaseToFire() {
		// TODO Auto-generated method stub
		
	}
	public int getHotSpot() {
		return this.hotspot;
	}
	
	public void setHotSpot(int number) {
		this.hotspot = number; 
	}
	
	public boolean containsHotSpot() {
		if(this.hotspot > 0) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Tile [fire=" + fire + ", coords=" + Arrays.toString(coords) + ", hotspot=" + hotspot
				+ ", adjacentEdges=" + Arrays.toString(adjacentEdges) + ", listOfFirefighters=" + listOfFirefighters
				+ ", x=" + x + ", y=" + y + ", poiList=" + poiList + ", hazmatList=" + hazmatList + ", isInterior="
				+ isInterior + ", pointerParkingSpot=" + pointerParkingSpot.toString() + ", nearestAmbulance=" + nearestAmbulance.toString()
				+ ", ParkingType=" + ParkingType.toString() + "]";
	}
	
	public void setHazmat(Hazmat hazmat) {
		hazmatList.add(hazmat);
		hazmat.setCurrentLocation(this);
	}
	
	public boolean containsHazmat() {
		if(this.hazmatList.size() > 0) {
			return true;
		}
		
		return false;
	}
	
	public boolean containsHazmat(Hazmat hazmat) {
		return hazmatList.contains(hazmat);
	}
	
	public Hazmat obtainHazmat() {
		if(!hazmatList.isEmpty()) {
			return hazmatList.get(hazmatList.size()-1);
		}
		return null;
	}

	public Hazmat popHazmat() {
		Hazmat hazmat = hazmatList.remove(hazmatList.size()-1);
		return hazmat;
	}
	
	public void popHazmat(Hazmat h) {
		hazmatList.remove(h);
	}
}
