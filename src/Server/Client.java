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
	PrintWriter pw;
	BufferedReader br = null;
	

	
	static String chatroomName, clientName;
	static String clientIP, portNum;
	static InetAddress ip;
    
    public Client(Socket s) throws IOException{  
    	socket = s;
		os = socket.getOutputStream();
		pw = new PrintWriter(os, true);
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		getInitialMessage(os, pw, br);
		
		pw.println("JOINED_CHATROOM: " + Client.chatroomName);
		pw.println("SERVER IP: " + 1);
		pw.println("PORT: " + Client.portNum);
		pw.println("ROOM_REF: " + 1);
		pw.println("JOIN_ID: " + 1);
    }
    
    @Override
    public void run() {	
    	String msg = null;
    	pw.println("Enter Message:");
    	try{msg = br.readLine();}
    	catch(IOException e1){e1.printStackTrace();}
    	while(msg != "quit") {
    		pw.println(Client.clientName + ": " + msg);
    		System.out.println(Client.clientName + ": " + msg);
    		pw.println("Enter Message:");
        	try{msg = br.readLine();}
        	catch(IOException e){e.printStackTrace();}
        	}
    	try{socket.close();}
    	catch(IOException e){e.printStackTrace();}
    }
    
    public static void getInitialMessage(OutputStream os, PrintWriter pw, BufferedReader br ) throws IOException {
		pw.println("Enter First Line (JOIN_CHATROOM: [chatroom name]):");

		while(!lineCorrect){
			Client.chatroomName = br.readLine();
			if (Client.chatroomName.regionMatches(0, initLine1, 0, 14)){
				pw.println("Line Correct :)");
				Client.chatroomName = Client.chatroomName.substring(15);
				pw.println(Client.chatroomName);
				lineCorrect = true;
			}else {
				pw.println("Line Incorrect: " + Client.chatroomName);
				pw.println("Enter First Line (JOIN_CHATROOM: [chatroom name]):");
				}
			}
		lineCorrect = false;
		pw.println("Enter Second Line (CLIENT_IP: 0):");
		
		while(!lineCorrect){
			Client.clientIP = br.readLine();
			if (Client.clientIP.regionMatches(0, initLine2, 0, 10)){
				pw.println("Line Correct :)");
				Client.clientIP = Client.clientIP.substring(11);
				ip = InetAddress.getLocalHost();
				pw.println(ip);
				lineCorrect = true;
			}else {
				pw.println("Line Incorrect: " + Client.clientIP);
				pw.println("Enter Second Line (CLIENT_IP: 0):");
				}
			}
		
		lineCorrect = false;
		pw.println("Enter Third Line (PORT: 0):");
		
		while(!lineCorrect){
			Client.portNum = br.readLine();
			if (Client.portNum.regionMatches(0, initLine3, 0, 5)){
				pw.println("Line Correct :)");
				Client.portNum = portNum.substring(6);
				Client.lineCorrect = true;
			}else {
				pw.println("Line Incorrect: " + portNum);
				pw.println("Enter Third Line (PORT: 0):");
				}
			}
		
		lineCorrect = false;
		pw.println("Enter Fourth Line ( CLIENT_NAME: [string Handle to identifier client user]:");
		
		while(!lineCorrect){
			Client.clientName = br.readLine();
			if (Client.clientName.regionMatches(0, initLine4, 0, 10)){
				pw.println("Line Correct :)");
				Client.clientName = Client.clientName.substring(11);
				lineCorrect = true;
			}else {
				pw.println("Line Incorrect: " + Client.clientName);
				pw.println("Enter Fourth Line ( CLIENT_NAME: [string Handle to identifier client user]:");
				}
			}
	}
}

