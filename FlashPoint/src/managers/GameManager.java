package managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
// Start of user code for imports
import java.util.*;
// End of user code

import actions.Action;
import actions.Chop;
import actions.Extinguish;
import actions.Finish;
import actions.Handle;
import actions.Move;
import actions.MoveWithVictim;
import game.GameState;
import tile.ParkingSpot;
import tile.Tile;
import token.*;
import edge.*;

/**
 * GameManager class definition.
 * Generated by the TouchCORE code generator.
 * modified by @matekrk @eric
 */
public class GameManager {
    
	private final GameState gs = GameState.getInstance();
	private Set<Action> possibleActions = generateAllPossibleActions();
	
	// MAIN
    public void runFlashpoint() {
    	gs.updateGameStateFromLobby();
    	setup();
    	doTurns();
    }
    
    public void setup() {
    	// we have board at this stage
    	// in order:
    	// DOOR, FIRE, POI, DAMAGE_TOKENS, PLAYERS' POSITION
    	
        /* TODO: No message view defined */
    }
	
    public void doTurns() {
    	while(!gs.isGameTerminated()) {
    		gs.getPlayingFirefighter().setAP( Math.min(8, gs.getPlayingFirefighter().getAP() + 4) );
    		takeATurn();
    		advanceFire();
    		gs.setActiveFireFighterIndex( (gs.getActiveFireFighterIndex() + 1)%(gs.getFireFighterList().size()) );
    	}
    	System.out.println(gs.isGameWon());
    }
    
    public void takeATurn() {
    	Set<Action> availableActions = getAllAvailableActions();
    	// pass to GUI
    	// GUI passes which action
    	// perform 
    	// if it was end of turn die, if not recursion (but GameState is different now!)	
    }
    
    public Set<Action> generateAllPossibleActions(){
    	
    	Set<Action> allPossibleActions = new HashSet<Action>(30);
    	
    	//move + chop
    	for (int dir : new int[]{0,1,2,3,} ) {
    		allPossibleActions.add(new Move(dir));
    		allPossibleActions.add(new MoveWithVictim(dir));
    		allPossibleActions.add(new Chop(dir));
    	}
    	
    	//extinguish
    	for (int dir : new int[]{-1,0,1,2,3,} ) {
    		allPossibleActions.add(new Extinguish(dir, 2)); 
    		allPossibleActions.add(new Extinguish(dir, 4)); //4? Shouldn't it be 1 or 2, smoke or fire? - Z
    	}
 
    	
    	//handle
    	allPossibleActions.add(new Handle());
    	
    	//finish
    	allPossibleActions.add(new Finish());
    	
    	//
    	return allPossibleActions;
    }
    
    //TODO: Zaid + Mat based on validations
    public Set<Action> getAllAvailableActions() {
    	Set<Action> allValidActions = new HashSet<Action>(30);
        for (Action a : possibleActions) {
        	if (a.validate(gs)) {
        		allValidActions.add(a);
        	}
        }
        return allValidActions;
    }
    
    
	//Ben and eric, ready for testing
    public void explosion(Tile targetTile) {
        /*
         * Ben: Can u remember how we consider the tile check? From the first tile or the 0th Tile?
         * I can not remember here need some modification
         * By Eric
         * 
         * */
    	for(int direction = 0; direction < 4; direction++) {
    		
    		
    		boolean checkBarriers = targetTile.checkBarriers(direction);
    		
    		Edge targetEdge;
    		
    		if(checkBarriers == true) {
    			targetEdge = targetTile.getEdge(direction);
    			
    			if(targetEdge.isDoor()) {
    				
    				targetEdge.destroyDoor();
    				//continue instead of break, to check the next direction but continue the explosion
    				if(!targetEdge.getStatus()) {
						//may need to be a continue
							continue;
						}
    			} 
    			else if(targetEdge.isWall()) {
    				//wall is only damaged 1 for family game
    				targetEdge.chop();
    				gs.updateDamageCounter();
    				//continue instead of break, to check the next direction but continue the explosion
    				continue;
    			}
    			
    			Tile tempTile = gs.getNeighbour(targetTile, direction);
    			
    			
    			while(true) {
    				if(tempTile.getFire()<2) {
    					
    					tempTile.increaseToFire();
    					
    					break;
    				}
    				
    				boolean barCheck = tempTile.checkBarriers(direction);
    				
    				if(barCheck == true) {
    					targetEdge = tempTile.getEdge(direction);
    					
    					if(targetEdge.isDoor()) {
    						
    						targetEdge.destroyDoor();
    						break;
    					} 
    					else if(targetEdge.isWall()) {
    						
    						targetEdge.chop();
    						
    						gs.updateDamageCounter();
    						
    						if(!targetEdge.getStatus()) {
    						//may need to be a continue
    							break;
    						}
    					}
    				}
    				tempTile = gs.getNeighbour(targetTile, direction);
    			}
    		}
    	}
    }

  //Ben and eric, ready for testing
    public void resolveFlashOver() {
        /* TODO: No message view defined */
    	Tile targetTile = gs.returnTile(1,1);
    	int[] tempCoords = targetTile.getCoords();
    	while(tempCoords[0] != 8 && tempCoords[1] != 10) {       // so only inner Tile count in the game right now?@Eric
    		int curFire = targetTile.getFire();
    		
    		if(curFire == 1) {
    			
    			for(int direction=0; direction<4;direction++) {
    				
    				boolean checkBarriers = targetTile.checkBarriers(direction);
    				if(checkBarriers == false) {
    					Tile adjTile =  gs.getNeighbour(targetTile,direction);
        				int fireCheck = adjTile.getFire();
        				
        				if(fireCheck == 2) {
        					targetTile.setFire(2);
        					
        					targetTile = gs.returnTile(0,1);
        		
        					continue;
        				}
    				}
    			}
    			tempCoords = targetTile.getCoords();
        		if(tempCoords[0] == 6 && tempCoords[1] == 8) {
        			break;
        		}
        		targetTile = gs.returnTile(tempCoords[0], tempCoords[1]);
    		}	
    		
    	}
    	
    }

  //Ben and eric, skeleton code 
    public void checkKnockDowns() {
        /* TODO: No message view defined */
    	//Select Tile
    	Tile targetTile = gs.returnTile(1, 1);
    	
    	//Check Tile contents
    	boolean containsFireFighter = targetTile.containsFirefighter();
    	boolean containsPOI = targetTile.containsPOI();
    	
    	//cycle through all the tile, need a better check.
    	int[] coords = targetTile.getCoords();
    	boolean check = true;
    
    	while(check){	
    		int curFire = targetTile.getFire();
    		
    		//knockdown all firefighters on tiles with fire
    		
    		if(containsFireFighter == true) {
    			
    			if(curFire == 2) {
    				ParkingSpot respawnTile = targetTile.getNearestAmbulance();
    				
    				Firefighter tempFire = targetTile.removeFirstFireFighter();
    				
    				tempFire.updateLocation(respawnTile);
    			}
    		}
    		//kill and remove all POI found on tiles with fire
    		if(containsPOI == true) {
    			if(curFire == 2) {
    				POI tempPOI = targetTile.removeFirstPoi();
    				gs.removePOI(tempPOI);
    				if(tempPOI.isVictim()) {
    					gs.updateLostCount();
    				}
    			}
    		}
    		
    		//check if this tile still have POI or firefighters
    		containsFireFighter = targetTile.containsFirefighter();
        	containsPOI = targetTile.containsPOI();
        	
        	//select next tile if current tile is empty
        	if(containsFireFighter == false && containsPOI == false) {
        		coords = targetTile.getCoords();
        		if(coords[0] == 6 && coords[1] == 8) {
        			//break the loop
        			check = false;
        		}
        		targetTile = gs.returnTile(coords[0], coords[1]);
        		containsFireFighter = targetTile.containsFirefighter();
            	containsPOI = targetTile.containsPOI();
        	}
    		
    	}
    }

  //Ben and eric, skeleton code 
    public void placePOI() {
        int currentPOI = gs.getCurrentPOI();
        while (currentPOI < 3) {
        	Tile targetTile = gs.rollForTile();
        	POI newPOI = gs.generatePOI();
        	boolean containsPOI = targetTile.containsPOI();
        	boolean containsFireFighter = targetTile.containsFirefighter();
        	int curFire = targetTile.getFire();
        	if(containsPOI == false) {
        		//could skip this if else and just set fire to 0 every time, could also write this to check tile and so on and only add the poi if the tile does not contain a firefighter or if it does and the poi is a victim.
        		if(curFire != 0) {
        			targetTile.setFire(0);
        			targetTile.addPoi(newPOI);
        			gs.updatePOI(newPOI);
        			
        			if(containsFireFighter == true) {
        				newPOI.reveal();
        				if(newPOI.isVictim() == false) {
        					//change to remove POI
        					targetTile.removeFromPoiList(newPOI);
        					gs.removePOI(newPOI);
        					newPOI.destroy();
        				}
        			}
        		}
        		else if(curFire == 0) {
        			targetTile.addPoi(newPOI);
        			gs.updatePOI(newPOI);
        			if(containsFireFighter == true) {
        				newPOI.reveal();
        				if(newPOI.isVictim() == false) {
        					//change to remove POI
        					targetTile.removeFromPoiList(newPOI);
        					gs.removePOI(newPOI);
        					newPOI.destroy();
        				}
        			}
        		}
        		currentPOI = gs.getCurrentPOI();
        	}
        }
    }

    //Ben and eric, skeleton code 
    public void advanceFire() {
        /* TODO: No message view defined */
    	//gs.endTurn();
    	
    	Tile targetTile = gs.rollForTile();
    	
    	int curFire = targetTile.getFire();
    	
    	if(curFire < 2) {
    		targetTile.increaseToFire();
    	}
    	  else {
    		explosion(targetTile);
    	}
    	resolveFlashOver();
    	checkKnockDowns();
    	placePOI();
    	
    	
    	int wallCheck = gs.getDamageCounter();//should this running the same time with the main process? @Eric
    	//int victimCheck = gs.getVictims();
    	
    	
    	if(wallCheck == 24 /*|| victimCheck == 4*/) {
    		//gs.setGameOver(true);
    	}
    }
   
    public int[] nextTile(int x, int y, int direction) {
    	int[] result = new int[2];
    	if(y==8) {
    		if(x == 6) {
    			result[0] = 1;
    			result [1] = 1;
    			return result;
    		}
    		result[0] = x+1;
    		result[1] = 1;
    		return result;
    	}
    	result[0] = x;
    	result[1] = y+1;
    	return result;
    }

    /*
     * SAVING AND READING
     */
    
    //This one I save for our next meeting. key word *serialization* @matekrk
    public void saveGame() {
    	try {
			FileOutputStream fo = new FileOutputStream(new File("myGameState.txt"));
			ObjectOutputStream oo = new ObjectOutputStream(fo);

			// Write object to file
			oo.writeObject(gs);

			oo.close();
			fo.close();


		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream");
		}
    }
    
    public GameState loadGame() {
    	try {
    		FileInputStream fi = new FileInputStream(new File("myGameState.txt"));
			ObjectInputStream oi = new ObjectInputStream(fi);
			// Read objects
			GameState gs1 = (GameState) oi.readObject();

			System.out.println(gs1.toString());

			oi.close();
			fi.close();
			
			gs.updateGameStateFromObject(gs1);
			return gs1; //if not void
			
			} catch (FileNotFoundException e) {
				System.out.println("File not found");
			} catch (IOException e) {
				System.out.println("Error initializing stream");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	return null;
    }
    
    

    //any volunteers? I guess we can do that when GUI is done
    public void setOptions() {
        /* TODO: No message view defined */
    }
 
}
