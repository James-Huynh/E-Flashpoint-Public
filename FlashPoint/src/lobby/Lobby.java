package lobby;

// Start of user code for imports
import java.util.*;

import game.FamilyGame;
import game.GameState;
// End of user code

import game.GameState;

/**
 * Lobby class definition.
 * Generated by the TouchCORE code generator.
 * not modified by @matekrk because: ¯\_(ツ)_/¯
 */
public class Lobby {
    
    protected boolean isClickable;
    protected GameState gameInstance = new GameState();
    
    public void createNewGame(int numberOfPlayers) {
    	FamilyGame familyTemplate = new FamilyGame();
    	
    	gameInstance.loadTemplate(familyTemplate);
		//gameInstance.updateGameStateFromTemplate();

    }
}
