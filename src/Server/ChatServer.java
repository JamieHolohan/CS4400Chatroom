package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

    private static final int portNumber = 4242;

    private int serverPort;
    private List<ClientThread> clients; // or "protected static List<ClientThread> clients;"
    private static InetAddress serverIP;

    public static void main(String[] args) throws UnknownHostException{
        ChatServer server = new ChatServer(portNumber);
        serverIP = InetAddress.getLocalHost();
        server.startServer();
    }

    public ChatServer(int portNumber){
        this.serverPort = portNumber;
    }

    public List<ClientThread> getClients(){
        return clients;
    }
    
    public int getPortNumber() {
    	return portNumber;
    }
    
    public InetAddress getServerIP() {
    	return serverIP;
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
                clients.add(client);
            }catch (IOException ex){
                System.out.println("Accept failed on : "+serverPort);
            }
        }
    }  
}
