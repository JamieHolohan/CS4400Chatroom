package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
	
	final static int portNumber = 4242;

	Socket socket = null;

	public static void main(String args[]) throws IOException {
		
		System.out.println("Creating server socket on port " + portNumber);
		ServerSocket serverSocket = new ServerSocket(portNumber);
		while (true) {
			Socket socket = serverSocket.accept();
			Runnable r = new Client(socket);
			Thread t = new Thread(r);
			t.start();
			System.out.println("New Client" + Client.clientName);
			System.out.println("SERVER IP: " + Client.ip);
			System.out.println("PORT: " + Client.portNum);
			System.out.println("ROOM_REF: " + 1);
			System.out.println("JOIN_ID: " + 1);
		}
	}
}