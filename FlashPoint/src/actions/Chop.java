package actions;

import edge.Edge;
import game.GameState;
import tile.Tile;
import token.Firefighter;
import token.Speciality;

/**
 * Chop class definition.
 * Generated by the TouchCORE code generator.
 * and modified by @matekrk
 */
public class Chop extends Action {
    
    protected int direction;
    protected ActionList title = ActionList.Chop;
    private static final long serialVersionUID = 1L;
    
    // constr
    public Chop(int direction, int cost) {
    	this.APcost = cost;
    	this.direction = direction;
    }
    
    //for advanced
    @Override
    public void adjustAction(GameState gs) {
    	Firefighter current = gs.getPlayingFirefighter();

    }
    
    /*
     * GETTERS
     */
    
    public int getDirection() {
        return direction;
    }
    
    public ActionList getTitle() {
    	return this.title;
    }

    /*
     * @Override
     */
    
    @Override
    public void perform(GameState gs) {
        Firefighter playingFirefighter = gs.getPlayingFirefighter();
        Tile currPosition = playingFirefighter.getCurrentPosition();
        int aP = playingFirefighter.getAP();
        Edge edge = currPosition.getEdge(this.direction);
        
        if (this.APcost == 2 || (this.APcost == 1 && playingFirefighter.speciality == (Speciality.RESCUE_SPECIALIST))) {
        	edge.chop();
        	gs.updateDamageCounter();
        }
        else if(this.APcost == 4 || (this.APcost == 2 && playingFirefighter.speciality == (Speciality.RESCUE_SPECIALIST))) { // == 4 or (rescue and ==2)
        	edge.chop();
        	edge.chop();
        	gs.updateDamageCounter();
        	gs.updateDamageCounter();
        }
        
        playingFirefighter.setAP(aP - this.APcost);
    }

    @Override
    public boolean validate(GameState gs) {
        boolean flag = false;
        Firefighter playingFirefighter = gs.getPlayingFirefighter();
        
    	if (playingFirefighter.getSpeciality() == (Speciality.RESCUE_SPECIALIST)) {
			this.APcost = this.APcost/2;
		}
        
        Tile currPosition = playingFirefighter.getCurrentPosition();
        int aP = playingFirefighter.getAP();
        Edge edge = currPosition.getEdge(this.direction);
        int dmgCounter = gs.getDamageCounter();//edge.getDamage();
        
        if(edge.isWall()) {
        	int damage = edge.getDamage();
        	if(damage == 2) {
        		if(aP >= APcost) {
        			switch(APcost) {
        			case 4:
        				if(dmgCounter + 2 < gs.MAX_WALL_DMGD) {
        					flag = true;
        				}
        			case 2:
        				if (playingFirefighter.speciality == (Speciality.RESCUE_SPECIALIST)) {
        					if(dmgCounter + 2 < gs.MAX_WALL_DMGD) {
            					flag = true;
            				}
        				}
        				else {
        					if(dmgCounter + 1 < gs.MAX_WALL_DMGD) {
        						flag = true;
        					}
        				}
        			case 1:
        				assert playingFirefighter.speciality == (Speciality.RESCUE_SPECIALIST);
        				if(dmgCounter + 1 < gs.MAX_WALL_DMGD) {
    						flag = true;
    					}
        			}
        		}
        	}
        	else if(damage == 1) {
        		if(aP >= APcost) {
        			switch(APcost) {
        			case 4:
        				break;
        			case 2:
        				if (playingFirefighter.speciality == (Speciality.RESCUE_SPECIALIST)) {
        					flag = false;
        				}
        				else {
        					if(dmgCounter + 1 < gs.MAX_WALL_DMGD) {
        						flag = true;
        					}
        				}
        			case 1:
        				assert playingFirefighter.speciality == (Speciality.RESCUE_SPECIALIST);
        				if(dmgCounter + 1 < gs.MAX_WALL_DMGD) {
    						flag = true;
    					}
        			}
        		}
        	}
        }
        
        return flag;
    }

	@Override
	public String toString() {
		return "Chop [direction=" + direction + ", title=" + title + ", APcost=" + APcost + "]";
	}

	
    
    
}
