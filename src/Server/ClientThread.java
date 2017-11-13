package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

public class ClientThread implements Runnable {
    private Socket socket;
    private PrintWriter clientOut;
    private BufferedReader br;
    private ChatServer server;
    private String chatroomName, clientName;
    private InetAddress ip;
    private int clientIP, portNumber, joinID, roomRef;
    private String msg, input;
    private boolean hasConnected;
    Random random = new Random();

    public ClientThread(ChatServer server, Socket socket) throws IOException{
        this.server = server;
        this.socket = socket;
        this.joinID = random.nextInt(999);
        this.roomRef = random.nextInt(999);
        this.hasConnected = false;
        this.ip = InetAddress.getLocalHost();
    }

    public PrintWriter getWriter(){
        return clientOut;
    }
    
    public String getClientName() {
    	return clientName;
    }
    
    public String getChatroomName() {
    	return chatroomName;
    }
    
    public int getClientIP() {
    	return clientIP;
    }
    
    public int getPortNum() {
    	return portNumber;
    }
    
    public int getRoomRef() {
    	return roomRef;
    }

    @Override
    public void run() {
        try{
            // setup
            this.clientOut = new PrintWriter(socket.getOutputStream(), false);
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            
            // start communicating
            while(!socket.isClosed()){
            	getMessage();
            }
            this.socket.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static String HELOtext = "HELO text";
    private static String killService = "KILL_SERVICE";
    private static String initLine1 = "JOIN_CHATROOM:";
    private static String initLine2 = "CLIENT_IP:";
    private static String initLine3 = "PORT:";
    private static String initLine4 = "CLIENT_NAME:";
    private static String finalLine1 = "LEAVE_CHATROOM:";
    private static String msgLine1 = "CHAT:";
    private static String msgLine2 = "JOIN_ID:";
    private static String msgLine3 = "CLIENT_NAME:";
    private static String msgLine4 = "MESSAGE:";
    
    //Retrieves first line of message and defines which type it is (Join, Leave, Chatroom)
    public void getMessage() throws IOException {
    	while(true) {
    		if (!this.hasConnected) {
    			System.out.println("Enter First Line of Join Message (JOIN_CHATROOM: [NAME]): ");
        		this.input = this.br.readLine();
        		this.input.trim();
        		
        		//Respond to HELO text
        		if (this.input.regionMatches(0, HELOtext, 0, 9)) {
        			printHeloText();
        			break;
        		}
        		
        		//Respond to KILL_SERVICE
        		if (this.input.regionMatches(0, killService, 0, 12)) {
        			System.exit(1);
        		}
        		if (this.input.regionMatches(0, initLine1, 0, 14)) {
        			getInitialMessage();
        			this.hasConnected = true;
        			break;
        		}
    		}else{
    			System.out.println("Enter First Line of New Message: ");
    			//this.clientOut.println("Enter First Line of New Message: ");
    			this.input = this.br.readLine();
    		}
    		
    		//Respond to HELO text
    		if (this.input.regionMatches(0, HELOtext, 0, 9)) {
    			printHeloText();
    			break;
    		}
    		
    		//Respond to kill_service
    		if (this.input.regionMatches(0, killService, 0, 12)) {
    			System.exit(1);
    		}
    			
    		//If first line is "JOIN_CHATROOM:" 
    		if (this.input.regionMatches(0, initLine1, 0, 14)){
    			getInitialMessage();
    			break;
    		}
    		
    		//If first line is "CHAT:"
    		if (input.regionMatches(0, msgLine1, 0, 5)){
    			getChatMessage();
    			for(ClientThread thatClient : server.getClients()){
            		PrintWriter thatClientOut = thatClient.getWriter();
            		int thatClientRoomRef = thatClient.getRoomRef();
            		if(thatClientOut != null && thatClientRoomRef == this.getRoomRef()){
            			printMessage();
            		}
            	}
    			break;
    		}
    		
    		//If first line is "LEAVE_CHATROOM:" 
    		if (this.input.regionMatches(0, finalLine1, 0, 14)){
    			getFinalMessage();
    			break;
    		}
    		
    		System.err.println("ERROR: Line is not recognised: \"" + this.input + "\"");
    	}
    }
    
    public void getInitialMessage() throws IOException{
	 	
    	//Check name from client, reject incorrect format
    	while(true){
    		
    		//Respond to kill_service
    		if (this.input.regionMatches(0, killService, 0, 12)) {
    			System.exit(1);
    		}
    		
    		//Respond to HELO text
    		if (this.input.regionMatches(0, HELOtext, 0, 9)) {
    			printHeloText();
    			this.input = this.br.readLine();
    		}
    		
    		//If correctly formatted, set chatroom name for this client, else return error
    		if (this.input.regionMatches(0, initLine1, 0, 14)){
    			chatroomName = this.input.substring(15);
    			chatroomName.trim();
    			break;
			}else{
				System.err.println("ERROR: Line Incorrect:\" " + this.input + "\"");
				this.clientOut.println("ERROR: Line Incorrect:\" " + this.input + "\"");
				this.input = this.br.readLine();
			}
    	}


    	//Get client ip from client, reject incorrect format, 0 in this case for TCP connection
    	//this.clientOut.println("Enter Second Line (CLIENT_IP: 0):");
    	System.out.println("Enter Second Line (CLIENT_IP: 0):");
    	this.input = this.br.readLine();
    	while(true){
    		
    		//Respond to kill_service
    		if (this.input.regionMatches(0, killService, 0, 12)) {
    			System.exit(1);
    		}
    		
    		//Respond to HELO text
    		if (this.input.regionMatches(0, HELOtext, 0, 9)) {
    			printHeloText();
    			this.input = this.br.readLine();
    		}
    		
    		//If formatted correctly, set client ip, else return error
    		if (this.input.regionMatches(0, initLine2, 0, 10)){
    			String inputClientIPString = this.input.substring(11);
    			try{
    				this.clientIP = Integer.parseInt(inputClientIPString);
    				break;
    			}catch (Exception e){ 
    				System.err.println("ERROR: Couldn't parse int \"" + inputClientIPString + "\"");
    				this.input = this.br.readLine();
    			}
    		}else{
    			this.clientOut.println("ERROR: Line Incorrect: \"" + this.input + "\"");
				System.err.println("ERROR: Line Incorrect: \"" + this.input + "\"");
				this.input = this.br.readLine();
    		}
    	}
    	
    	//Get port number from client, reject incorrect format, 0 in this case for TCP connection	
    	//this.clientOut.println("Enter Third Line (PORT: 0):");
    	System.out.println("Enter Third Line (PORT: 0):");
    	this.input = this.br.readLine();
    	while(true){
    		
    		//Respond to kill_service
    		if (this.input.regionMatches(0, killService, 0, 12)) {
    			System.exit(1);
    		}
    		
    		//Respond to HELO text
    		if (this.input.regionMatches(0, HELOtext, 0, 9)) {
    			printHeloText();
    			this.input = this.br.readLine();
    		}
    		
    		//If formatted correctly, set port num, else return error
    		if (this.input.regionMatches(0, initLine3, 0, 5)){
    			String inputPortString = this.input.substring(6);
    			try{
    				this.clientIP = Integer.parseInt(inputPortString);
    				break;
    			}catch (Exception e){ 
    				System.err.println("ERROR: Couldn't parse int");
    				this.input = this.br.readLine();
    			}
    		}else{
    			this.clientOut.println("ERROR: Line Incorrect: \"" + this.input + "\"");
				System.err.println("ERROR: Line Incorrect: \"" + this.input + "\"");
				this.input = this.br.readLine();
    		}
    	}
    	
    	
    	//Get client name from client, reject incorrect format
    	//this.clientOut.println("Enter Fourth Line ( CLIENT_NAME: [string Handle to identifier client user]:");
    	System.out.println("Enter Fourth Line (CLIENT_NAME: [string Handle to identifier client user]:");
    	this.input = this.br.readLine();
    	while(true){
    		
    		//Respond to kill_service
    		if (this.input.regionMatches(0, killService, 0, 12)) {
    			System.exit(1);
    		}
    		
    		//Respond to HELO text
    		if (this.input.regionMatches(0, HELOtext, 0, 9)) {
    			printHeloText();
    			this.input = this.br.readLine();
    		}
    		
    		//If formatted correctly, set client name, else return error
    		if (this.input.regionMatches(0, initLine4, 0, 12)){
    			clientName = this.input.substring(13);
    			clientName.trim();
    			break;
			}else{
				System.err.println("ERROR: Line Incorrect:\" " + this.input + "\"");
				this.clientOut.println("ERROR: Line Incorrect:\" " + this.input + "\"");
				this.input = this.br.readLine();
			}
		}
    	
    	//Prints join message to client and server
    	printJoinMessage();
    }

    public void getChatMessage() throws IOException{
	 	
    	int inputRoomRef, inputJoinID;
    	String inputClientNameString;
    	
    	//Get first line from chat message, reject if wrong format
    	while(true){
    		
    		//Respond to kill_service
    		if (this.input.regionMatches(0, killService, 0, 12)) {
    			System.exit(1);
    		}
    		
    		//Respond to HELO text
    		if (this.input.regionMatches(0, HELOtext, 0, 9)) {
    			printHeloText();
    			this.input = this.br.readLine();
    		}
    		
    		//Get first line (CHAT: [room ref], else return error
    		if (input.regionMatches(0, msgLine1, 0, 5)){
    			String inputRoomRefString = this.input.substring(6);
    			try{
    				inputRoomRef = Integer.parseInt(inputRoomRefString);
    				if (inputRoomRef == this.roomRef) {
    					break;
    				}else {
    					System.err.println("ERROR: Room reference number incorrect: \"" + inputRoomRef + "\" should be: " + this.roomRef );
    					this.clientOut.println("ERROR: Room reference number incorrect: \"" + inputRoomRef + "\" should be: " + this.roomRef );
    					this.input = this.br.readLine();
    				}
    			}catch (Exception e){ 
    				System.err.println("ERROR: Couldn't parse int \"" + inputRoomRefString + "\"");
    				this.clientOut.println("ERROR: Couldn't parse int \"" + inputRoomRefString + "\"");
    				this.input = this.br.readLine();
    			}
    		}else{
    			System.err.println("ERROR: Line incorrect: \"" + this.input + "\"");
    			this.clientOut.println("ERROR: Line incorrect: \"" + this.input + "\"");
    			this.input = this.br.readLine();
    		}
    	}
		
    	//Get second line of message, reject if wrong format
    	this.clientOut.println("Enter Second Line (JOIN_ID: [JOIN_ID])");
    	while(true){
    		System.out.println("Enter Second Line (JOIN_ID: [JOIN_ID])");
        	this.input = this.br.readLine();
    		
    		//Respond to kill_service
    		if (this.input.regionMatches(0, killService, 0, 12)) {
    			System.exit(1);
    		}
    		
    		//Respond to HELO text
    		if (this.input.regionMatches(0, HELOtext, 0, 9)) {
    			printHeloText();
    			this.input = this.br.readLine();
    		}

    		if (input.regionMatches(0, msgLine2, 0, 8)){
    			String inputJoinIDString = this.input.substring(9);
    			try{
    				inputJoinID= Integer.parseInt(inputJoinIDString);
    				if (inputJoinID == this.joinID) {
    					break;
    				}else {
    					System.err.println("ERROR: JoinID number incorrect: \"" + inputJoinID + "\" should be: " + this.joinID );
    					this.clientOut.println("ERROR: JoinID number incorrect: \"" + inputJoinID + "\" should be: " + this.joinID );
    				}
    			}catch (Exception e){ 
    				System.err.println("ERROR: Couldn't parse int \"" + inputJoinIDString + "\"");
    				this.clientOut.println("ERROR: Couldn't parse int \"" + inputJoinIDString + "\"");
    			}
    		}else{
    			System.err.println("ERROR: Line incorrect: \"" + this.input + "\"");
    			this.clientOut.println("ERROR: Line incorrect: \"" + this.input + "\"");
    		}
		}
		
    	//Get third line of message, reject if wrong format
    	this.clientOut.println("Enter Third Line (CLIENT_NAME: [Name])");
    	//System.out.println("Enter Third Line (CLIENT_NAME: [Name])");
    	//this.input = this.br.readLine();
    	while(true){
    		System.out.println("Enter Third Line (CLIENT_NAME: [Name])");
        	this.input = this.br.readLine();
        	
    		//Respond to kill_service
    		if (this.input.regionMatches(0, killService, 0, 12)) {
    			System.exit(1);
    		}
    		
    		//Respond to HELO text
    		if (this.input.regionMatches(0, HELOtext, 0, 9)) {
    			printHeloText();
    			this.input = this.br.readLine();
    		}

    		if (this.input.regionMatches(0, msgLine3, 0, 12)){
    			inputClientNameString = this.input.substring(13);
    			inputClientNameString.trim();
    			if (inputClientNameString.equals(this.clientName)) {
    				break;
    			}else {
    				System.err.println("ERROR: Client name incorrect \"" + inputClientNameString + "\" should be: \"" + this.clientName + "\"");
    				this.clientOut.println("ERROR: Client name incorrect \"" + inputClientNameString + "\" should be: \"" + this.clientName + "\"");
    			}
			}else{
				System.err.println("ERROR: Line Incorrect: " + this.input);
				this.clientOut.println("ERROR: Line Incorrect: " + this.input);
			}
		}
		
    	//Get fourth line of message, reject if wrong format
    	this.clientOut.println("Enter Fourth Line (MESSAGE: [message contents])");
    	while(true){
    		System.out.println("Enter Fourth Line (MESSAGE: [message contents])");
    		this.input = this.br.readLine();
    		
    		//Respond to kill_service
    		if (this.input.regionMatches(0, killService, 0, 12)) {
    			System.exit(1);
    		}
    		
    		//Respond to HELO text
    		if (this.input.regionMatches(0, HELOtext, 0, 9)) {
    			printHeloText();
    			this.input = this.br.readLine();
    		}
    		
    		if (input.regionMatches(0, msgLine4, 0, 8)){
    			msg = input.substring(9);
    			break;
			}else{
				this.clientOut.println("Line Incorrect: " + this.input);
				System.err.println("ERROR: Line Incorrect: " + this.input);
			}
   		}
   	}
    
	@SuppressWarnings("null")
	public void getFinalMessage() throws IOException{
    	
    	int inputRoomRef, inputJoinID;
    	
    	//Check name from client, reject incorrect format
    	while(true){
    		
    		//Respond to kill_service
    		if (this.input.regionMatches(0, killService, 0, 12)) {
    			System.exit(1);
    		}
    		
    		//Respond to HELO text
    		if (this.input.regionMatches(0, HELOtext, 0, 9)) {
    			printHeloText();
    			this.input = this.br.readLine();
    		}
    		
    		//If leave message correctly formatted, continue to next line else return error
    		if (input.regionMatches(0, finalLine1, 0, 15)){
    			String inputRoomRefString = this.input.substring(16);
    			try{
    				inputRoomRef = Integer.parseInt(inputRoomRefString);
    				if (inputRoomRef == this.roomRef) {
    					break;
    				}else {
    					System.err.println("ERROR: Room reference number incorrect: \"" + inputRoomRef + "\" should be: " + this.roomRef );
    					this.clientOut.println("ERROR: Room reference number incorrect: \"" + inputRoomRef + "\" should be: " + this.roomRef );
    					this.input = this.br.readLine();
    				}
    			}catch (Exception e){ 
    				System.err.println("ERROR: Couldn't parse int \"" + inputRoomRefString + "\"");
    				this.clientOut.println("ERROR: Couldn't parse int \"" + inputRoomRefString + "\"");
    				this.input = this.br.readLine();
    			}
    		}else{
    			System.err.println("ERROR: Line incorrect: \"" + this.input + "\"");
    			this.clientOut.println("ERROR: Line incorrect: \"" + this.input + "\"");
    			this.input = this.br.readLine();
    		}
    	}
    	
    	//Get second line of message, reject if wrong format
    	this.clientOut.println("Enter Second Line (JOIN_ID: [JOIN_ID])");
    	while(true){
    		System.out.println("Enter Second Line (JOIN_ID: [JOIN_ID])");
        	this.input = this.br.readLine();
    		
    		//Respond to kill_service
    		if (this.input.regionMatches(0, killService, 0, 12)) {
    			System.exit(1);
    		}
    		
    		//Respond to HELO text
    		if (this.input.regionMatches(0, HELOtext, 0, 9)) {
    			printHeloText();
    			this.input = this.br.readLine();
    		}

    		if (input.regionMatches(0, msgLine2, 0, 8)){
    			String inputJoinIDString = this.input.substring(9);
    			try{
    				inputJoinID= Integer.parseInt(inputJoinIDString);
    				if (inputJoinID == this.joinID) {
    					this.roomRef = (Integer) null;
    					this.chatroomName = null;
    					break;
    				}else {
    					System.err.println("ERROR: JoinID number incorrect: \"" + inputJoinID + "\" should be: " + this.joinID );
    					this.clientOut.println("ERROR: JoinID number incorrect: \"" + inputJoinID + "\" should be: " + this.joinID );
    				}
    			}catch (Exception e){ 
    				System.err.println("ERROR: Couldn't parse int \"" + inputJoinIDString + "\"");
    				this.clientOut.println("ERROR: Couldn't parse int \"" + inputJoinIDString + "\"");
    			}
    		}else{
    			System.err.println("ERROR: Line incorrect: \"" + this.input + "\"");
    			this.clientOut.println("ERROR: Line incorrect: \"" + this.input + "\"");
    		}
		}

    	
    	//Prints join message to client
    	printLeaveMessage();
    	
    	//Prints join message to other clients in server
    	for(ClientThread thatClient : server.getClients()){
    		PrintWriter thatClientOut = thatClient.getWriter();
    		if(thatClientOut != null && thatClient.roomRef == this.roomRef){
    			thatClientOut.println(this.clientName + " has left chatroom " + this.chatroomName + "\n");
    			thatClientOut.flush();
    		}
    	}
    }
    
    public void printJoinMessage() {
    	for(ClientThread thatClient : server.getClients()){
    		PrintWriter thatClientOut = thatClient.getWriter();
    		
    		int thatClientRoomRef = thatClient.getRoomRef();
    		String thatClientRoomName = thatClient.getChatroomName();
    		
    		//Checks if chatroom name already exists, sets roomref accordingly
    		if(thatClientRoomName.equals(this.chatroomName)){
    			this.roomRef = thatClientRoomRef;
    		}else {
    			this.roomRef = random.nextInt(999);
    		}
    		
    		//Print join message to chatroom clients
       		if(thatClientOut != null && thatClient.roomRef == this.roomRef  && thatClient.joinID != this.joinID){
    			thatClientOut.println(this.clientName + " has joined chatroom " + this.chatroomName + "\n");
    			thatClientOut.flush();
    		}
    		
        }
    	
    	//Needed to print messages to this screen, default message doesn't appear otherwise (not sure why) 
    	for(ClientThread thatClient : server.getClients()){
    		PrintWriter thatClientOut = thatClient.getWriter();
    		if(thatClientOut != null && thatClient.roomRef == this.roomRef  && thatClient.joinID == this.joinID){
    			thatClientOut.println("JOINED_CHATROOM: " + chatroomName);
    			thatClientOut.println("SERVER_IP: " + this.server.getServerIP());
    	    	thatClientOut.println("PORT: " + this.server.getPortNumber());
    	    	thatClientOut.println("ROOM_REF: " + this.roomRef);
    	    	thatClientOut.println("JOIN_ID: " + this.joinID + "\n");
    			thatClientOut.flush();
    		}
    	}
    }
    
    public void printLeaveMessage() {
    	
    	//Checks if chatroom name already exists, sets roomref accordingly
    	for(ClientThread thatClient : server.getClients()){
    		PrintWriter thatClientOut = thatClient.getWriter();
    		
    		//Print left message to chatroom clients
       		if(thatClientOut != null && thatClient.roomRef == this.roomRef){
    			thatClientOut.println(this.clientName + " has left chatroom " + this.chatroomName + "\n");
    			thatClientOut.flush();
    		}
        }
    	
    	//Print Left message to client
    	this.clientOut.println("LEFT CHATROOM: " + this.roomRef);
    	this.clientOut.println("JOIN_ID: " + this.joinID);
    }
    
    
    public void printHeloText() {
    	this.clientOut.println("HELO text\nIP:" + this.clientIP +"\nPort:" + this.portNumber + "\nStudentID:" + 13325757 + "\n");
		System.out.println("HELO text\nIP:" + this.clientIP +"\nPort:" + this.portNumber + "\nStudentID:" + 13325757 + "\n");
    }
    
    
    public void printMessage() {
    	for(ClientThread thatClient : server.getClients()){
    		PrintWriter thatClientOut = thatClient.getWriter();	
    		int thatClientRoomRef = thatClient.getRoomRef();
    		
    		if(thatClientOut != null && thatClientRoomRef == this.roomRef  && thatClient.joinID != this.joinID){
    			thatClientOut.println("CHAT:" + this.roomRef);
    			thatClientOut.println("CLIENT_NAME:" + this.clientName);
    			thatClientOut.println("MESSAGE:" + this.msg);
    			thatClientOut.flush();
    		}
    	}
    }
}