package actions;

import java.io.Serializable;

import game.GameState;
import token.Speciality;

/**
 * Action class definition.
 * Generated by the TouchCORE code generator.
 * and modified by @matekrk
 */
public abstract class Action implements Serializable {
    
    protected int APcost;
    public abstract void perform(GameState gs);
    public abstract boolean validate(GameState gs);
    public abstract void adjustAction(GameState gs);
    protected ActionList title;
    //placeholder for the GUI 
    protected int direction;
    private static final long serialVersionUID = 1L;
    // public abstract boolean validate();
    

    /*
     * GETTERS
     */
    
    public int getCost() {
        return APcost;
    }
    public ActionList getTitle() {
    	return title;
    }
    //placeholder for the GUI.
    public int getDirection() {
    	return direction;
    }
    
    public boolean isAmbulance(){
    	return false;
    }
    
    public boolean canMove() {
    	return false;
    }
    
    /*
     * INTERACTION WITH GAMESTATE
     */

    boolean removeAvailableActions(GameState a) {
        /* TODO: No message view defined */
        if (a.getAvailableActions().contains(this)) {
        	a.getAvailableActions().remove(this);
        	return true;
        }
        else {
        	return false;
        }
    }

    boolean addAvailableActions(GameState a) {
    	a.getAvailableActions().add(this);
        return true;
    }
	public Speciality getToSpecialty() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int[] getTileLocation() {
		return null;
	}
	
	public int getFirefighterIndex() {
		return -1;
	}
	
	public Action getAction() {
		return null;
	}
	
	public int[] getResult() {
		return null;
	}
	
	public void setResult(int[] coords) {
		//Chillin'
	}

}
