package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

    private static final int portNumber = 4242;

    private int serverPort;
    private List<ClientThread> clients; // or "protected static List<ClientThread> clients;"

    public static void main(String[] args){
        ChatServer server = new ChatServer(portNumber);
        server.startServer();
    }

    public ChatServer(int portNumber){
        this.serverPort = portNumber;
    }

    public List<ClientThread> getClients(){
        return clients;
    }

    private void startServer(){
        clients = new ArrayList<ClientThread>();
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(serverPort);
            acceptClients(serverSocket);
        }catch (IOException e){
            System.err.println("Could not listen on port: "+serverPort);
            System.exit(1);
        }
    }

    private void acceptClients(ServerSocket serverSocket){

        System.out.println("server starts port = " + serverSocket.getLocalSocketAddress());
        while(true){
            try{
                Socket socket = serverSocket.accept();
                System.out.println("accepts : " + socket.getRemoteSocketAddress());
                ClientThread client = new ClientThread(this, socket);
                Thread thread = new Thread(client);
                thread.start();
                //client.getWriter().write("Enter Message: ");
                clients.add(client);
                //getInitialMessage(client);
            } catch (IOException ex){
                System.out.println("Accept failed on : "+serverPort);
            }
        }
    }
    
//    public void getInitialMessage(ClientThread client) throws IOException{
//	 	
//	 boolean lineCorrect = false;
//	 pw.println("Enter First Line (JOIN_CHATROOM: [chatroom name]):");
//
//	 while(!lineCorrect){
//		 client.setChatroomName(br.readline());
//		 if (chatroomName.regionMatches(0, initLine1, 0, 14)){
//			 pw.println("Line Correct :)");
//			 chatroomName = chatroomName.substring(15);
//			 pw.println(chatroomName);
//			 lineCorrect = true;
//			}else{
//				pw.println("Line Incorrect: " + chatroomName);
//				pw.println("Enter First Line (JOIN_CHATROOM: [chatroom name]):");
//			}
//		}
//		
//	 lineCorrect = false;
//	 pw.println("Enter Second Line (CLIENT_IP: 0):");
//		
//	 while(!lineCorrect){
//		clientIP = br.readLine();
//		if (clientIP.regionMatches(0, initLine2, 0, 10)){
//			pw.println("Line Correct :)");
//			clientIP = clientIP.substring(11);
//			ip = InetAddress.getLocalHost();
//			pw.println(ip);
//			lineCorrect = true;
//		}else{
//			pw.println("Line Incorrect: " + clientIP);
//			pw.println("Enter Second Line (CLIENT_IP: 0):");
//		}
//	}
//		
//	lineCorrect = false;
//	pw.println("Enter Third Line (PORT: 0):");
//	
//	while(!lineCorrect){
//		portNum = br.readLine();
//		if (portNum.regionMatches(0, initLine3, 0, 5)){
//			pw.println("Line Correct :)");
//			portNum = portNum.substring(6);
//			lineCorrect = true;
//		}else{
//			pw.println("Line Incorrect: " + portNum);
//			pw.println("Enter Third Line (PORT: 0):");
//		
//		}
//	}
//		
//	lineCorrect = false;
//	pw.println("Enter Fourth Line ( CLIENT_NAME: [string Handle to identifier client user]:");
//		
//	while(!lineCorrect){
//		clientName = br.readLine();
//		if (clientName.regionMatches(0, initLine4, 0, 11)){
//			pw.println("Line Correct :)");
//			clientName = clientName.substring(12);
//			lineCorrect = true;
//		}else{
//			pw.println("Line Incorrect: " + clientName);
//			pw.println("Enter Fourth Line ( CLIENT_NAME: [string Handle to identifier client user]:");
//		}
//	}
//   }
}




//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.InputStream;
//import java.io.PrintWriter;
//import java.net.InetAddress;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.ArrayList;
//
//public class ChatServer {
//	
//	final static int portNumber = 4242;
//	static boolean lineCorrect = false;
//	static String initLine1 = "JOIN_CHATROOM:";
//	static String initLine2 = "CLIENT_IP:";
//	static String initLine3 = "PORT:";
//	static String initLine4 = "CLIENT_NAME:";
//	
//	String chatroomName;
//	String clientName;
//	String clientIP;
//	String portNum;
//	InetAddress ip;
//	
//	//private static ArrayList<Client> clientList;
//
//	Socket socket = null;
//
//	public static void main(String args[]) throws IOException {
//		
//		ArrayList<Client> clientList;
//		
//		System.out.println("Creating server socket on port " + portNumber);
//		@SuppressWarnings("resource")
//		ServerSocket serverSocket = new ServerSocket(portNumber);
//		while (true) {
//			Socket socket = serverSocket.accept();
//			clientList.add(new Client(socket));
//			InputStream is = socket.getInputStream();
//			InputStreamReader isr = new InputStreamReader(is);
//			BufferedReader br = new BufferedReader(isr);
//			Runnable r = new Client(socket);
//			new Thread(r).start();
//			getInitialMessage(bw , br, socket);
//			//pw.println("JOINED_CHATROOM: " + chatroomName);
//			//pw.println("SERVER IP: " + 1);
//			//pw.println("PORT: " + portNum);
//			//pw.println("ROOM_REF: " + 1);
//			//pw.println("JOIN_ID: " + 1);
//	    	//pw.println("Enter Message:");
//		}
//	}
//	
//	 public void getInitialMessage(PrintWriter pw, BufferedReader br, Socket s ) throws IOException{
//		 	
//		 boolean lineCorrect = false;
//		 pw.println("Enter First Line (JOIN_CHATROOM: [chatroom name]):");
//
//		 while(!lineCorrect){
//			 chatroomName = br.readLine();
//			 if (chatroomName.regionMatches(0, initLine1, 0, 14)){
//				 pw.println("Line Correct :)");
//				 chatroomName = chatroomName.substring(15);
//				 pw.println(chatroomName);
//				 lineCorrect = true;
//				}else{
//					pw.println("Line Incorrect: " + chatroomName);
//					pw.println("Enter First Line (JOIN_CHATROOM: [chatroom name]):");
//				}
//			}
//			
//		 lineCorrect = false;
//		 pw.println("Enter Second Line (CLIENT_IP: 0):");
//			
//		 while(!lineCorrect){
//			clientIP = br.readLine();
//			if (clientIP.regionMatches(0, initLine2, 0, 10)){
//				pw.println("Line Correct :)");
//				clientIP = clientIP.substring(11);
//				ip = InetAddress.getLocalHost();
//				pw.println(ip);
//				lineCorrect = true;
//			}else{
//				pw.println("Line Incorrect: " + clientIP);
//				pw.println("Enter Second Line (CLIENT_IP: 0):");
//			}
//		}
//			
//		lineCorrect = false;
//		pw.println("Enter Third Line (PORT: 0):");
//		
//		while(!lineCorrect){
//			portNum = br.readLine();
//			if (portNum.regionMatches(0, initLine3, 0, 5)){
//				pw.println("Line Correct :)");
//				portNum = portNum.substring(6);
//				lineCorrect = true;
//			}else{
//				pw.println("Line Incorrect: " + portNum);
//				pw.println("Enter Third Line (PORT: 0):");
//			
//			}
//		}
//			
//		lineCorrect = false;
//		pw.println("Enter Fourth Line ( CLIENT_NAME: [string Handle to identifier client user]:");
//			
//		while(!lineCorrect){
//			clientName = br.readLine();
//			if (clientName.regionMatches(0, initLine4, 0, 11)){
//				pw.println("Line Correct :)");
//				clientName = clientName.substring(12);
//				lineCorrect = true;
//			}else{
//				pw.println("Line Incorrect: " + clientName);
//				pw.println("Enter Fourth Line ( CLIENT_NAME: [string Handle to identifier client user]:");
//			}
//		}
//	}
//}