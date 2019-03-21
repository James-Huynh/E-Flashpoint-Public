package lobby;

import java.io.Serializable;
// Start of user code for imports
import java.util.ArrayList;

import game.BoardTwo;
import game.FamilyGame;
import game.TemplateGame;
import server.Player;

public class Lobby implements Serializable  {

	private boolean isClickable;
	private ArrayList<Player> players;
	private ArrayList<token.Colour> assignableColours;

	private String mode, name, difficulty, board;
	private int capacity;

	private TemplateGame template;
	private static final long serialVersionUID = 1L;


	public Lobby(){

		//dummy
		players = new ArrayList<Player>();

//		assignColours();

	}

	public void assignColours(){
		assignableColours = new ArrayList<token.Colour>();
		assignableColours.add(token.Colour.GREEN);
		assignableColours.add(token.Colour.BLACK);
		assignableColours.add(token.Colour.WHITE);
		assignableColours.add(token.Colour.RED);
		assignableColours.add(token.Colour.PURPLE);
		assignableColours.add(token.Colour.BLUE);
		for(int i = 0; i<players.size(); i++) {
			if(this.players.get(i) != null) {
				this.players.get(i).setColour(this.assignableColours.get(i));
			}
		}
	}

	public boolean isClickable() {
		return isClickable;
	}

	public void setClickable(boolean isClickable) {
		this.isClickable = isClickable;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public String getType() {
		return mode;
	}

	public void setType(String type) {
		this.mode = type;
	}

	public TemplateGame getTemplate() {
		return template;
	}

	public void setTemplete(TemplateGame template) {
		this.template = template;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public void addPlayer(Player newPlayer) {
		this.players.add(newPlayer);
		assignColours();
	}

	public void setDifficulty(String difficulty) {
		if(!difficulty.equals("notSelected"))
			this.difficulty = difficulty;
		
	}
	public String getDifficulty() {
		return difficulty;
	}
	public void setFamilyGame() {
		System.out.println("IN FAMILY GAME EVER GETTING CALLED?");
		template = new FamilyGame();
	}

	public String getBoard() {
		return board;
	}

	public void setBoard(String board) {
		this.board = board;
	}
	
	public void createTemplate() {
		if(this.board.equals("Board 1")){
			if(this.difficulty.equals("Family")) {
				template = new FamilyGame();
			}
			else if(this.difficulty.equals("Experienced")) {
				//Same as below
			}
		}
		
		else if(this.board.equals("Board 2")) {
			if(this.difficulty.equals("Family")) {
				template = new BoardTwo("Family");
			}
			else if(this.difficulty.equals("Experienced")) {
				template = new BoardTwo("Experienced");
			}
		}
		
		else {
			//Random Board
		}
	}

}
