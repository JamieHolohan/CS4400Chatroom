package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientThread implements Runnable {
    private Socket socket;
    private PrintWriter clientOut;
    private ChatServer server;
    private String chatroomName;
    private String clientName;
    private String clientIP;
    private String portNumber;
    private InetAddress ip;

    public ClientThread(ChatServer server, Socket socket){
        this.server = server;
        this.socket = socket;
    }

    private PrintWriter getWriter(){
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
    
    public void setPortNum(String s) {
    	this.portNumber = s;
    }
    
    public void setClientName(String s) {
    	this.clientName = s;
    }
    
    public void setChatroomName(String s) {
    	this.chatroomName = s;
    }
    
    public void setClientIP(String s) {
    	this.clientIP = s;
    }

    @Override
    public void run() {
        try{
            // setup
            this.clientOut = new PrintWriter(socket.getOutputStream(), false);
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            getInitialMessage(br, this.clientOut);

            String input = null;
            //clientOut.write("Enter Message: ");
            // start communicating
            while(!socket.isClosed()){
            	//clientOut.write("Enter Message: ");
            	input = br.readLine();
            	//System.out.println(input);
            	for(ClientThread thatClient : server.getClients()){
            		PrintWriter thatClientOut = thatClient.getWriter();
            		if(thatClientOut != null){
            			thatClientOut.write(input + "\r\n");
            			thatClientOut.flush();
            		}
            	}
            }
            this.socket.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    static boolean lineCorrect = false;
    static String initLine1 = "JOIN_CHATROOM:";
    static String initLine2 = "CLIENT_IP:";
    static String initLine3 = "PORT:";
    static String initLine4 = "CLIENT_NAME:";
    
    public void getInitialMessage(BufferedReader br, PrintWriter pw) throws IOException{
	 	
    	boolean lineCorrect = false;
    	pw.println("Enter First Line (JOIN_CHATROOM: [chatroom name]):");

    	while(!lineCorrect){
    		this.setChatroomName(br.readLine());
    		if (chatroomName.regionMatches(0, initLine1, 0, 14)){
    			pw.println("Line Correct :)");
    			chatroomName = chatroomName.substring(15);
    			pw.println(chatroomName);
    			lineCorrect = true;
			}else{
				pw.println("Line Incorrect: " + chatroomName);
				pw.println("Enter First Line (JOIN_CHATROOM: [chatroom name]):");
			}
		}
		
    	lineCorrect = false;
    	pw.println("Enter Second Line (CLIENT_IP: 0):");
		
    	while(!lineCorrect){
    		this.setClientIP(br.readLine());//clientIP = br.readLine();
    		if (clientIP.regionMatches(0, initLine2, 0, 10)){
    			pw.println("Line Correct :)");
    			clientIP = clientIP.substring(11);
    			ip = InetAddress.getLocalHost();
    			pw.println(ip);
    			lineCorrect = true;
    		}else{
    			pw.println("Line Incorrect: " + clientIP);
    			pw.println("Enter Second Line (CLIENT_IP: 0):");
    		}
    	}
		
    	lineCorrect = false;
    	pw.println("Enter Third Line (PORT: 0):");
	
    	while(!lineCorrect){
    		this.setPortNum(br.readLine());//portNum = br.readLine();
    		if (portNumber.regionMatches(0, initLine3, 0, 5)){
    			pw.println("Line Correct :)");
    			portNumber = portNumber.substring(6);
    			lineCorrect = true;
    		}else{
    			pw.println("Line Incorrect: " + portNumber);
    			pw.println("Enter Third Line (PORT: 0):");
    		}
    	}
		
    	lineCorrect = false;
    	pw.println("Enter Fourth Line ( CLIENT_NAME: [string Handle to identifier client user]:");
		
    	while(!lineCorrect){
    		this.setClientName(br.readLine());//clientName = br.readLine();
    		if (clientName.regionMatches(0, initLine4, 0, 11)){
    			pw.println("Line Correct :)");
    			clientName = clientName.substring(12);
    			lineCorrect = true;
    		}else{
    			pw.println("Line Incorrect: " + clientName);
    			pw.println("Enter Fourth Line ( CLIENT_NAME: [string Handle to identifier client user]:");
    		}
    	}
    }
}