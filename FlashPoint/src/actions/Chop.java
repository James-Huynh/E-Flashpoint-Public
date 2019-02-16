package m5_final;

// Start of user code for imports
import java.util.*;
// End of user code

/**
 * Chop class definition.
 * Generated by the TouchCORE code generator.
 * and modified by @matekrk
 */
public class Chop extends Action {
    
    protected int direction;
    
    public int getDirection() {
        return direction;
    }

    public void perform() {
        Firefighter playingFirefighter = availableActions.getPlayingFirefighter();
        int dmgCounter = availableActions.getDmgCounter();
        availableActions.updateDmgCounter();
        int aP = playingFirefighter.getAP();
        playingFirefighter.setSavedAP();
        Tile currPosition = playingFirefighter.getCurrPosition();
        Edge edge = currPosition.getEdge(this.direction);
        Wall wall = edge.getType();
        wall.chop();
    }

    public boolean validate() {
        boolean flag = false;
        Firefighter playingFirefighter = availableActions.getPlayingFirefighter();
        int dmgCounter = availableActions.getDmgCounter();
        Tile currPosition = playingFirefighter.getCurrPosition();
        int aP = playingFirefighter.getAP();
        Edge edge = currPosition.getEdge(this.direction);
        //Wall wall = edge.getType();
        //if (if wall == 'Wall') {
        if (edge.isWall) {
            int damage = wall.getDamage();
            if (if damage > 0) {
                if (if aP >= 2) {
                    if (if dmgCounter + 1 < MAX_WALL_DMGD) {
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }
}
