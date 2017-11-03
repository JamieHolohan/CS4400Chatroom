package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
	
	static String initLine1 = "JOIN_CHATROOM:";
	static String initLine2 = "CLIENT_IP:";
	static String initLine3 = "PORT:";
	static String initLine4 = "CLIENT_NAME:";
	static boolean lineCorrect = false;
	
	static String chatroomName, clientName;
	static String clientIP, portNum;

	public static void main(String args[]) throws IOException {
		final int portNumber = 4242;
		System.out.println("Creating server socket on port " + portNumber);
		ServerSocket serverSocket = new ServerSocket(portNumber);
		while (true) {
			Socket socket = serverSocket.accept();
			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os, true);
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
				if (clientName.regionMatches(0, initLine4, 0, 10)){
					pw.println("Line Correct :)");
					clientName = clientName.substring(11);
					lineCorrect = true;
				}else {
					pw.println("Line Incorrect: " + clientName);
					pw.println("Enter Fourth Line ( CLIENT_NAME: [string Handle to identifier client user]:");
					}
				}

			pw.println("JOINED_CHATROOM: " + chatroomName);
			pw.println("SERVER IP: " + 1);
			pw.println("PORT: " + portNum);
			pw.println("ROOM_REF: " + 1);
			pw.println("JOIN_ID: " + 1);
			pw.close();
			socket.close();

			System.out.println("Just said hello to:" + clientName);
		}
	}
}