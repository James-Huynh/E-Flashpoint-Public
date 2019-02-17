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
    
    public Chop() {
    	this.APcost = 2;
    }
    
    public Chop(int cost) {
    	this.APcost = cost;
    }
    
    /*
     * GETTERS
     */
    
    public int getDirection() {
        return direction;
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
        
        gs.updateDamageCounter();
        playingFirefighter.setSavedAP(aP - this.APcost);
        edge.chop();
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
            }
        }
        return flag;
    }
}
