package actions;

import java.util.ArrayList;

import edge.Edge;
import game.GameState;
import tile.Tile;
import token.Firefighter;
import token.POI;
import token.Speciality;

/**
 * Move class definition.
 * Generated by the TouchCORE code generator.
 * and modified by @matekrk
 */
public class Move extends Action {

	private static final long serialVersionUID = 1L;
	protected int direction;
	protected ActionList title = ActionList.Move;
	
	public Move(int direction) {
		this.direction = direction;
		this.APcost = 1;
	}
    
    public int getDirection() {
        return direction;
    }
    
    public ActionList getTitle() {
    	return this.title;
    }

    @Override
    public boolean validate(GameState gs) {
        boolean flag = false;
        Firefighter playingFirefighter = gs.getPlayingFirefighter();
        int extraPoints = 0;
        if (playingFirefighter.getSpeciality() == Speciality.RESCUE_SPECIALIST) {
        	extraPoints = playingFirefighter.getSP();
        }
        
        Tile currentPosition = playingFirefighter.getCurrentPosition();
        Edge edge = currentPosition.getEdge(direction);
        Tile neighbour = gs.getNeighbour(currentPosition, direction);
        if(neighbour == null) { //If exterior, this direction isn't valid
        	return false;
        }
        int fire = neighbour.getFire();
        int aP = playingFirefighter.getAP() + extraPoints;
        
        if ( edge.isDoor() ) {
        	boolean status = edge.getStatus();
        	boolean desStatus = edge.isDestroyed();
        	if(status == true || desStatus == true) {
        		if (fire < 2) {
        			if( aP >= 1) {
        				flag = true;
        			}
        		}
        		
        	   else if (fire == 2 && aP > 2 && playingFirefighter.getSpeciality() != Speciality.DOG) {
        		   flag = true;
        		   this.APcost = 2;
        	   }
        		
        	}
        }
        
        else if( edge.isBlank() ) {
        	if( fire < 2 && aP >= 1) {
        		flag = true;
        	}
        	
        	else if( fire == 2 && aP > 2 && playingFirefighter.getSpeciality() != Speciality.DOG) {
        		flag = true;
        		this.APcost = 2;
        	}
        }
        
        else if( edge.isWall() ) {
        	int damage = edge.getDamage();
        	if(damage == 0) {
        		if( fire < 2 && aP >= 1) {
        			flag = true;
        		}
        		
        		else if( fire == 2 && aP > 2 && playingFirefighter.getSpeciality() != Speciality.DOG) {
        				flag = true;
        				this.APcost = 2;
        			}
        	}
        	else if (damage == 1) {
        		if (playingFirefighter.getSpeciality() == Speciality.DOG && fire < 2) {
        			this.APcost = 2;
        			flag = true;
        		}
        	}
        }
        
        if (playingFirefighter.getCarriedPOI() != null && neighbour.getFire() == 2) {
        	flag = false;
        }
        
        return flag;
    }

    @Override
    public void perform(GameState gs) {
        Firefighter playingFirefighter = gs.getPlayingFirefighter();
        Tile currentPosition = playingFirefighter.getCurrentPosition();
        int aP = playingFirefighter.getAP();
        
        if (playingFirefighter.getSpeciality() == Speciality.RESCUE_SPECIALIST) {
        	if (playingFirefighter.getSP() >= APcost) {
        		playingFirefighter.setSP(playingFirefighter.getSP() - APcost);
        	}
        	else if (playingFirefighter.getSP() > 0) {
        		int difference = APcost - playingFirefighter.getSP();
        		playingFirefighter.setSP(0);
        		playingFirefighter.setAP(playingFirefighter.getAP() - difference);
        		playingFirefighter.setUsedAP(true);
        	}
        	else {
        		playingFirefighter.setAP(aP - this.APcost);
        		playingFirefighter.setUsedAP(true);
        	}
        }
        else {
        	playingFirefighter.setAP(aP - this.APcost);
        	playingFirefighter.setUsedAP(true);
        }
        

        Tile neighbour = gs.getNeighbour(currentPosition, this.direction);
        playingFirefighter.setCurrentPosition(neighbour);
        currentPosition.removeFromFirefighterList(playingFirefighter);
        neighbour.addToFirefighterList(playingFirefighter);
        if (!playingFirefighter.getCanDodge()) {
        	gs.vicinity(playingFirefighter);
        }
        
        //@matekrk - logic POI move/poiFlip
        if(neighbour.containsPOI() == true) {
        	ArrayList<POI> Pois = neighbour.getPoiList(); //where we going
        	ArrayList<POI> POIStoRemove = new ArrayList<POI>(); //exploration
        	
        	for(POI poi:Pois) {
        		if(!poi.isRevealed()){
        			if(poi.isVictim()){
        				poi.reveal();
        			} 
        			else {
        				POIStoRemove.add(poi);
        			}
        		}
        	}
        	
        	for(POI poi:POIStoRemove) {
        		gs.removePOI(poi);
        		Pois.remove(poi);
        		gs.getRevealedFalseAlarmsList().add(poi);
        	}
        }
        
        if (playingFirefighter.getSpeciality() == Speciality.DOG) {
			for (int inn=0; inn<4; inn++) {
				Tile neighbourNeighbour = gs.getNeighbour(neighbour, inn);
				if(neighbourNeighbour != null && neighbourNeighbour.containsPOI()) {
					ArrayList<POI> Pois = neighbourNeighbour.getPoiList(); //where we going
		        	ArrayList<POI> POIStoRemove = new ArrayList<POI>(); //exploration
		        	
		        	for(POI poi:Pois) {
		        		if(!poi.isRevealed()){
		        			if(poi.isVictim()){
		        				poi.reveal();
		        			} 
		        			else {
		        				POIStoRemove.add(poi);
		        			}
		        		}
		        	}
		        	
		        	for(POI poi:POIStoRemove) {
		        		gs.removePOI(poi);
		        		Pois.remove(poi);
		        		gs.getRevealedFalseAlarmsList().add(poi);
		        	}
				}
			}
		}
        
      //speciality paramedic
//        if (playingFirefighter.getSpeciality() == Speciality.PARAMEDIC && playingFirefighter.getFollow() != null) {
//        	playingFirefighter.getFollow().setCurrentPosition(neighbour);
//        	currentPosition.getPoiList().remove(playingFirefighter.getFollow());
//        	neighbour.getPoiList().add(playingFirefighter.getFollow());
//        }
        
        
        if (playingFirefighter.getCarriedPOI() != null) {
        	playingFirefighter.getCarriedPOI().setCurrentPosition(neighbour);
//        	currentPosition.getPoiList().remove(playingFirefighter.getCarriedPOI());
        	for(int i=0; i<currentPosition.getPoiList().size();i++) {
        		POI p = currentPosition.getPoiList().get(i);
//        		System.out.println(p.getLeader());
        		if(p.getLeader().getColour() == playingFirefighter.getColour()) {
//        			currentPosition.getPoiList().remove(i);
        			neighbour.getPoiList().add(currentPosition.getPoiList().remove(i));
        			if(!neighbour.checkInterior()) {
        				gs.removePOI(neighbour.getPoiList().get(0));
        				gs.updateSavedCount(neighbour.getPoiList().get(0));
        				neighbour.getPoiList().remove(0);
        			}
        		}
        	}
        }

//        	neighbour.getPoiList().add(playingFirefighter.getCarriedPOI());s
    }
    
    @Override
	public void adjustAction(GameState gs) {
		
	}
    
    @Override
	public String toString() {
		return "Move [direction=" + direction + ", title=" + title + ", APcost=" + APcost + "]";
	}
}
