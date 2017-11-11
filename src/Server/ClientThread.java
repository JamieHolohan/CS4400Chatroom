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
    private String chatroomName, clientName, clientIP, portNumber;
    private InetAddress ip;
    private int joinID, roomRef;
    private String msg, input;
    Random random = new Random();

    public ClientThread(ChatServer server, Socket socket) throws IOException{
        this.server = server;
        this.socket = socket;
        this.joinID = random.nextInt(999);
        this.roomRef = random.nextInt(999);
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
    
    public String getClientIP() {
    	return clientIP;
    }
    
    public String getPortNum() {
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
            getInitialMessage();
            
            for(ClientThread thatClient : server.getClients()){
        		int thatClientRoomRef = thatClient.getRoomRef();
        		String thatClientRoomName = thatClient.getChatroomName();
        		System.out.println(thatClientRoomName);
        		System.out.println(this.chatroomName);
        		if(thatClientRoomName == this.chatroomName){
        			this.roomRef = thatClientRoomRef;
        		}
            }

            // start communicating
            while(!socket.isClosed()){
            	getMessage();
            	this.clientOut.println("\n");
            	for(ClientThread thatClient : server.getClients()){
            		PrintWriter thatClientOut = thatClient.getWriter();
            		int thatClientRoomRef = thatClient.getRoomRef();
            		if(thatClientOut != null && thatClientRoomRef == this.getRoomRef()){
            			printMessage();
            		}
            	}
            }
            this.socket.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    static String initLine1 = "JOIN_CHATROOM:";
    static String initLine2 = "CLIENT_IP:";
    static String initLine3 = "PORT:";
    static String initLine4 = "CLIENT_NAME:";
    
    public void getInitialMessage() throws IOException{
	 	
    	boolean lineCorrect = false;
    	//this.clientOut.println("Enter First Line (JOIN_CHATROOM: [chatroom name]):");
    	System.out.println("Enter First Line (JOIN_CHATROOM: [chatroom name]):");

    	while(!lineCorrect){
    		this.chatroomName = this.br.readLine();
    		if (chatroomName.regionMatches(0, initLine1, 0, 14)){
    			//this.clientOut.println("Line Correct :)");
    			System.out.println("Line Correct :)");
    			chatroomName = chatroomName.substring(15);
    			//this.clientOut.println(chatroomName);
    			lineCorrect = true;
			}else{
				//this.clientOut.println("Line Incorrect: " + chatroomName);
				System.out.println("Line Incorrect: " + chatroomName);
				//this.clientOut.println("Enter First Line (JOIN_CHATROOM: [chatroom name]):");
				System.out.println("Enter First Line (JOIN_CHATROOM: [chatroom name]):");
			}
		}
		
    	lineCorrect = false;
    	//this.clientOut.println("Enter Second Line (CLIENT_IP: 0):");
    	System.out.println("Enter Second Line (CLIENT_IP: 0):");
		
    	while(!lineCorrect){
    		this.clientIP = this.br.readLine();//clientIP = br.readLine();
    		if (clientIP.regionMatches(0, initLine2, 0, 10)){
    			//this.clientOut.println("Line Correct :)");
    			System.out.println("Line Correct :)");
    			clientIP = clientIP.substring(11);
    			ip = InetAddress.getLocalHost();
    			//this.clientOut.println(ip);
    			lineCorrect = true;
    		}else{
    			//this.clientOut.println("Line Incorrect: " + clientIP);
				System.out.println("Line Incorrect: " + clientIP);
				//this.clientOut.println("Enter Second Line (CLIENT_IP: 0):");
				System.out.println("Enter Second Line (CLIENT_IP: 0):");
    		}
    	}
		
    	lineCorrect = false;
    	//this.clientOut.println("Enter Third Line (PORT: 0):");
    	System.out.println("Enter Third Line (PORT: 0):");
	
    	while(!lineCorrect){
    		this.portNumber= this.br.readLine();//portNum = br.readLine();
    		if (portNumber.regionMatches(0, initLine3, 0, 5)){
    			//this.clientOut.println("Line Correct :)");
    			System.out.println("Line Correct :)");
    			portNumber = portNumber.substring(6);
    			lineCorrect = true;
    		}else{
    			//this.clientOut.println("Line Incorrect: " + portNumber);
				System.out.println("Line Incorrect: " + portNumber);
				//this.clientOut.println("Enter Third Line (PORT: 0):");
				System.out.println("Enter Third Line (PORT: 0):");
    		}
    	}
		
    	lineCorrect = false;
    	//this.clientOut.println("Enter Fourth Line ( CLIENT_NAME: [string Handle to identifier client user]:");
    	System.out.println("Enter Fourth Line ( CLIENT_NAME: [string Handle to identifier client user]:");
    	
    	while(!lineCorrect){
    		this.clientName = this.br.readLine();
    		if (clientName.regionMatches(0, initLine4, 0, 11)){
    			//this.clientOut.println("Line Correct :)");
    			System.out.println("Line Correct :)\n");
    			clientName = clientName.substring(12);
    			lineCorrect = true;
    		}else{
    			//this.clientOut.println("Line Incorrect: " + portNumber);
				System.out.println("Line Incorrect: " + portNumber);
				//this.clientOut.println("Enter Fourth Line (CLIENT_NAME: [string Handle to identifier client user]:");
				System.out.println("Enter Fourth Line (CLIENT_NAME: [string Handle to identifier client user]:");
    		}
    	}
    	
    	printJoinMessage();
    	
    	for(ClientThread thatClient : server.getClients()){
    		PrintWriter thatClientOut = thatClient.getWriter();
    		if(thatClientOut != null){
    			thatClientOut.println(this.clientName + " has joined chatroom" + this.chatroomName + "\n");
    			thatClientOut.flush();
    		}
    	}
    }
    
    public void printJoinMessage() {
    	clientOut.println("JOINED_CHATROOM: " + chatroomName);
    	clientOut.println("SERVER_IP: " + this.server.getServerIP());
    	clientOut.println("PORT: " + this.server.getPortNumber());
    	clientOut.println("ROOM_REF: " + this.roomRef);
    	clientOut.println("JOIN_ID: " + this.joinID + "\n");
    	System.out.println("JOINED_CHATROOM: " + chatroomName);
    	System.out.println("SERVER_IP: " + this.server.getServerIP());
    	System.out.println("PORT: " + this.server.getPortNumber());
    	System.out.println("ROOM_REF: " + this.roomRef);
    	System.out.println("JOIN_ID: " + this.joinID);
    }
    
    private static String msgLine1 = "CHAT:";
    private static String msgLine2 = "JOIN_ID:";
    private static String msgLine3 = "CLIENT_NAME:";
    private static String msgLine4 = "MESSAGE:";

    public void getMessage() throws IOException{
	 	
    	boolean lineCorrect = false;
    	//this.clientOut.println("Enter First Line (CHAT: [ROOM_REF])");
    	System.out.println("Enter First Line (CHAT: [ROOM_REF])");

    	while(!lineCorrect){
    		this.input = this.br.readLine();
    		//if (input.regionMatches(0, msgLine1, 0, 5) && input.regionMatches(0, this.roomRef, 6, 10)){
    		if (this.input.regionMatches(0, msgLine1, 0, 5)){
    			//this.clientOut.println("Line Correct :)");
    			System.out.println("Line Correct :)");
    			lineCorrect = true;
			}else{
				//this.clientOut.println("Line Incorrect: " + this.input);
				System.out.println("Line Incorrect: " + this.input);
				//this.clientOut.println("Enter First Line (CHAT: [ROOM_REF]):");
				System.out.println("Enter First Line (CHAT: [ROOM_REF]):");
			}
		}
		
    	lineCorrect = false;
    	//this.clientOut.println("Enter Second Line (JOIN_ID: [JOIN_ID])");
    	System.out.println("Enter Second Line (JOIN_ID: [JOIN_ID])");

    	while(!lineCorrect){
    		this.input = this.br.readLine();
    		//if (input.regionMatches(0, msgLine1, 0, 5) && input.regionMatches(0, this.roomRef, 6, 10)){
    		if (this.input.regionMatches(0, msgLine2, 0, 8)){
    			//this.clientOut.println("Line Correct :)");
    			System.out.println("Line Correct :)");
    			lineCorrect = true;
			}else{
				//this.clientOut.println("Line Incorrect: " + this.input);
				System.out.println("Line Incorrect: " + this.input);
				//this.clientOut.println("Enter Second Line (JOIN_ID: [JOIN_ID])");
				System.out.println("Enter Second Line (JOIN_ID: [JOIN_ID])");
			}
		}
		
    	lineCorrect = false;
    	//this.clientOut.println("Enter Third Line (CLIENT_NAME: [Name])");
    	System.out.println("Enter Third Line (CLIENT_NAME: [Name])");

    	while(!lineCorrect){
    		this.input = this.br.readLine();
    		//if (input.regionMatches(0, msgLine1, 0, 5) && input.regionMatches(0, this.roomRef, 6, 10)){
    		if (this.input.regionMatches(0, msgLine3, 0, 12)){
    			//this.clientOut.println("Line Correct :)");
    			System.out.println("Line Correct :)");
    			lineCorrect = true;
			}else{
				//this.clientOut.println("Line Incorrect: " + this.input);
				System.out.println("Line Incorrect: " + this.input);
				//this.clientOut.println("Enter Third Line (CLIENT_NAME: [Name])");
				System.out.println("Enter Third Line (CLIENT_NAME: [Name])");
			}
		}
		
    	lineCorrect = false;
    	//this.clientOut.println("Enter Fourth Line (MESSAGE: [message contents])");
    	System.out.println("Enter Fourth Line (MESSAGE: [message contents])");

    	while(!lineCorrect){
    		this.input = this.br.readLine();
    		//if (input.regionMatches(0, msgLine1, 0, 5) && input.regionMatches(0, this.roomRef, 6, 10)){
    		if (input.regionMatches(0, msgLine4, 0, 8)){
    			//this.clientOut.println("Line Correct :) \n");
    			System.out.println("Line Correct :)");
    			msg = input.substring(9);
    			lineCorrect = true;
			}else{
				//this.clientOut.println("Line Incorrect: " + this.input);
				System.out.println("Line Incorrect: " + this.input);
				//this.clientOut.println("Enter Fourth Line (MESSAGE: [message contents])");
				System.out.println("Enter Fourth Line (MESSAGE: [message contents])");
			}
    	}
    }
    
    public void printMessage() {
    	for(ClientThread thatClient : server.getClients()){
    		PrintWriter thatClientOut = thatClient.getWriter();
    		if(thatClientOut != null){
    			//thatClientOut.println("\n");
    			thatClientOut.println("CHAT:" + this.roomRef);
    			thatClientOut.println("CLIENT_NAME:" + this.clientName);
    			thatClientOut.println("MESSAGE:" + this.msg);
    			thatClientOut.flush();
    		}
    	}
    }
}