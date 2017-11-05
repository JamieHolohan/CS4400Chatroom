package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Client implements Runnable {
		
	static String initLine1 = "JOIN_CHATROOM:";
	static String initLine2 = "CLIENT_IP:";
	static String initLine3 = "PORT:";
	static String initLine4 = "CLIENT_NAME:";
	static boolean lineCorrect = false;
	Socket socket = null;
	OutputStream os = null;
	PrintWriter pw = null;
	BufferedReader br = null;
	
	String chatroomName, clientName;
	String clientIP, portNum;
	InetAddress ip;
    
    public Client(Socket s) throws IOException{  
    	socket = s;
		os = socket.getOutputStream();
		pw = new PrintWriter(os, true);
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		getInitialMessage(os, pw, br);
		
		pw.println("JOINED_CHATROOM: " + chatroomName);
		pw.println("SERVER IP: " + 1);
		pw.println("PORT: " + portNum);
		pw.println("ROOM_REF: " + 1);
		pw.println("JOIN_ID: " + 1);
    	pw.println("Enter Message:");
    }
    
    @Override
    public void run() {		
    	String msg = null;
    	try{msg = br.readLine();}
    	catch(IOException e1){e1.printStackTrace();}
    	while(msg != "quit") {
    		pw.println(clientName + ": " + msg);
    		System.out.println(clientName + ": " + msg);
    		pw.println("Enter Message:");
        	try{msg = br.readLine();}
        	catch(IOException e){e.printStackTrace();}
        	}
    	try{socket.close();}
    	catch(IOException e){e.printStackTrace();}
    }
    
    public void getInitialMessage(OutputStream os, PrintWriter pw, BufferedReader br ) throws IOException{
		pw.println("Enter First Line (JOIN_CHATROOM: [chatroom name]):");

		while(!lineCorrect){
			chatroomName = br.readLine();
			if (chatroomName.regionMatches(0, initLine1, 0, 14)){
				pw.println("Line Correct :)");
				chatroomName = chatroomName.substring(15);
				pw.println(chatroomName);
				lineCorrect = true;
			}else {
				pw.println("Line Incorrect: " + chatroomName);
				pw.println("Enter First Line (JOIN_CHATROOM: [chatroom name]):");
				}
			}
		lineCorrect = false;
		pw.println("Enter Second Line (CLIENT_IP: 0):");
		
		while(!lineCorrect){
			clientIP = br.readLine();
			if (clientIP.regionMatches(0, initLine2, 0, 10)){
				pw.println("Line Correct :)");
				clientIP = clientIP.substring(11);
				ip = InetAddress.getLocalHost();
				pw.println(ip);
				lineCorrect = true;
			}else {
				pw.println("Line Incorrect: " + clientIP);
				pw.println("Enter Second Line (CLIENT_IP: 0):");
				}
			}
		
		lineCorrect = false;
		pw.println("Enter Third Line (PORT: 0):");
		
		while(!lineCorrect){
			portNum = br.readLine();
			if (portNum.regionMatches(0, initLine3, 0, 5)){
				pw.println("Line Correct :)");
				portNum = portNum.substring(6);
				lineCorrect = true;
			}else {
				pw.println("Line Incorrect: " + portNum);
				pw.println("Enter Third Line (PORT: 0):");
				}
			}
		
		lineCorrect = false;
		pw.println("Enter Fourth Line ( CLIENT_NAME: [string Handle to identifier client user]:");
		
		while(!lineCorrect){
			clientName = br.readLine();
			if (clientName.regionMatches(0, initLine4, 0, 11)){
				pw.println("Line Correct :)");
				clientName = clientName.substring(12);
				lineCorrect = true;
			}else {
				pw.println("Line Incorrect: " + clientName);
				pw.println("Enter Fourth Line ( CLIENT_NAME: [string Handle to identifier client user]:");
				}
			}
	}
}

