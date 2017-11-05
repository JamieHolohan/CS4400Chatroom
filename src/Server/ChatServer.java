package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
	
	final static int portNumber = 4242;

	Socket socket = null;

	public static void main(String args[]) throws IOException {
		
		System.out.println("Creating server socket on port " + portNumber);
		@SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(portNumber);
		while (true) {
			Socket socket = serverSocket.accept();
			Runnable r = new Client(socket);
			new Thread(r).start();
		}
	}
}