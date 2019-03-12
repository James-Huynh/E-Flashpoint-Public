package client;

import java.io.IOException;

import actions.Action;
import commons.bean.User;
import commons.tran.bean.TranObject;
import commons.tran.bean.TranObjectType;
import game.GameState;
import lobby.Lobby;

public class ClientManager {
	
	private ClientInputThread inputThread;
	private ClientOutputThread outputThread;
	private User requestObject; 
//	private ObjectInputStream ois;
//	private ObjectOutputStream oos;
	
	public ClientManager(ClientInputThread input, ClientOutputThread output) {
		this.inputThread = input;
		this.outputThread = output;
		
	}
	
	public boolean readMessage() throws IOException, ClassNotFoundException {
		boolean flag = false;
		System.out.println("Reading on Client side Started");
		Object readObject = inputThread.readInputStream();
		if (readObject != null && readObject instanceof TranObject) {
			TranObject read_tranObject = (TranObject) readObject;
			switch(read_tranObject.getType()) {
			case SUCCESS:
				System.out.println("Succesuful return");
				System.out.println(read_tranObject.getType());
				requestObject = (User) read_tranObject.getObject();
				flag = true;
				break;
			case LOGINSUCCESS:
				System.out.println("Succesuful login request");
				requestObject = (User) read_tranObject.getObject();
				flag = true;
				break;
			case REGISTERSUCCESS:
				System.out.println("Succesuful register request");
				requestObject = (User) read_tranObject.getObject();
				flag = true;
				break;
			case FFPLACEMENTSUCCESS:
				System.out.println("Succesuful placement request");
				requestObject = (User) read_tranObject.getObject();
				flag = true;
				break;
			case ACTIONSUCCESS:
				System.out.println("Succesuful action request");
				requestObject = (User) read_tranObject.getObject();
				flag = true;
				break;
			case LOBBYCREATIONSUCCESS:
				System.out.println("Successful lobby request");
				requestObject = (User) read_tranObject.getObject();
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	public boolean connectionRequest(User inputClient) {
		boolean flag = false;
		System.out.println("Getting here");
		requestObject = inputClient;
		System.out.println(requestObject.getId());
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.CONNECT);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		System.out.println("check!");
		try {
			while(readMessage() != true) {
				
			}
			flag = true;
		}
		catch(ClassNotFoundException l) {
			
		}
		catch(IOException k) {
			
		}
		System.out.println("|2|" + requestObject.getId()); 
//		System.out.println("|3|" + client.getCurrentState().returnTile(5, 1).getPoiList().get(0).isRevealed()); 
		return flag;
	}

	public boolean loginRequest(String username, String password) {
		boolean flag = false;
		requestObject.setName(username);
		requestObject.setPassword(password.toString());
		
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.LOGIN);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		
		try {
			while(readMessage() != true) {
				
			}
			if(requestObject.getIsOnline() == 1) {
				flag = true;
			}
			//if client.getIsOnline() == 0 flag = false;	
		}
		catch(ClassNotFoundException l) {
			
		}
		catch(IOException k) {
			
		}
		return flag;
	
	}

	public boolean registerRequest(String username, String password) {
		boolean flag = false;
		requestObject.setName(username);
		requestObject.setPassword(password.toString());
		
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.REGISTER);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		
		try {
			while(readMessage() != true) {
				
			}
			flag = requestObject.getIsRegistered();
			
			//if client.getIsOnline() == 0 flag = false;
		}
		catch(ClassNotFoundException l) {
			
		}
		catch(IOException k) {
			
		}
		return flag;
	}
	
	// James
	/**
	 * Asks the server to create a lobby
	 * @return boolean indicating the status of the operation
	 */
	public boolean createLobbyRequest(String name, String mode, int capacity) {
		boolean flag = false;
		
//		Lobby lobby = requestObject.getCurrentLobby();
		Lobby lobby = new Lobby();
		lobby.setName(name);
		lobby.setMode(mode);
		lobby.setCapacity(capacity);
		
		requestObject.setCurrentLobby(lobby);
		
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.LOBBYCREATION);
		
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);

		try {
			while(readMessage() != true) {
				
			}
			flag = true;
		}
		
		catch(Exception E) {
			System.out.println("Exception occured during createLobbyRequest.");
		}
		
		
		return flag;
	}
	
	public GameState gameStateRequest(User userOne) {
		boolean flag = false;
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.GAMESTATEUPDATE);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		System.out.println("test check");
		try {
			while(readMessage() != true) {
				
			}
			flag = true;
		}
		catch(ClassNotFoundException l) {
			
		}
		catch(IOException k) {
			
		}
		System.out.println("|3|" + requestObject.getCurrentState().returnTile(5, 1).getPoiList().get(0).isRevealed()); 
		return requestObject.getCurrentState();
		
	}

	public GameState placeFFRequest(int[] coords) {
		boolean flag = false;
		requestObject.setCoords(coords);
		requestObject.setPlaced(false);
		
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.FIREFIGHTERPLACEMENT);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		
		System.out.println("place check");
		try {
			while(readMessage() != true) {
				
			}
			flag = true;
		}
		catch(ClassNotFoundException l) {
			
		}
		catch(IOException k) {
			
		}
		
		return requestObject.getCurrentState();
	}
	
	public GameState getUsersGameState() {
		return this.requestObject.getCurrentState();
	}

	public GameState ActionRequest(Action a) {
		boolean flag = false;
		requestObject.setAction(a);
//		requestObject.setActionCommited(false);
		
		TranObject<User> objectToSend = new TranObject<User>(TranObjectType.ACTIONREQUEST);
		objectToSend.setObject(requestObject);
		outputThread.setMsg(objectToSend);
		
		System.out.println("place check");
		try {
			while(readMessage() != true) {
				
			}
			flag = true;
		}
		catch(ClassNotFoundException l) {
			
		}
		catch(IOException k) {
			
		}
		
		return requestObject.getCurrentState();
	}
	
}