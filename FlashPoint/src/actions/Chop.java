package actions;

// Start of user code for imports
import java.util.*;
// End of user code

import edge.Edge;
import game.GameState;
import tile.Tile;
import token.Firefighter;

/**
 * Chop class definition.
 * Generated by the TouchCORE code generator.
 * and modified by @matekrk
 */
public class Chop extends Action {
    
    protected int direction;
    protected ActionList title = ActionList.Chop;
    
    public Chop(int direction) {
    	this.APcost = 2;
    	this.direction = direction;
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
        
        if (this.APcost == 2) {
        	edge.chop();
        	gs.updateDamageCounter();
        }
        else { // == 4
        	edge.destroyDoor();
        	gs.updateDamageCounter();
        	gs.updateDamageCounter();
        }
        
        playingFirefighter.setAP(aP - this.APcost);
    }

    @Override
    public boolean validate(GameState gs) {
        boolean flag = false;
        Firefighter playingFirefighter = gs.getPlayingFirefighter();
        Tile currPosition = playingFirefighter.getCurrentPosition();
        int aP = playingFirefighter.getAP();
        Edge edge = currPosition.getEdge(this.direction);
        int dmgCounter = edge.getDamage();
        
        if (edge.isWall()) {
            int damage = edge.getDamage();
            if (damage > 0) {
                if (aP >= 2) {
                    if (dmgCounter + 1 < gs.MAX_WALL_DMGD) {
                        flag = true;
                    }
                }
                if (APcost == 4 && damage == 2 && aP >= 4) {
                	if (dmgCounter + 1 < gs.MAX_WALL_DMGD) {
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }

	@Override
	public String toString() {
		return "Chop [direction=" + direction + ", APcost=" + APcost + "]";
	}
    
    
}
