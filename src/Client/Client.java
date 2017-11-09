package Client;

//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.net.InetAddress;
//import java.net.Socket;
//
//public class Client implements Runnable {
//		
//	//static String initLine1 = "JOIN_CHATROOM:";
//	//static String initLine2 = "CLIENT_IP:";
//	//static String initLine3 = "PORT:";
//	//static String initLine4 = "CLIENT_NAME:";
//	//static boolean lineCorrect = false;
//	Socket socket = null;
//	OutputStream os = null;
//	PrintWriter pw = null;
//	BufferedReader br = null;
//	OutputStreamWriter osw = null;
//    BufferedWriter bw = null;
//	
//	String chatroomName, clientName;
//	String clientIP, portNum;
//	InetAddress ip;
//    
//    public Client(Socket s) throws IOException{  
//    	socket = s;
//    	os = socket.getOutputStream();
//    	osw = new OutputStreamWriter(os);
//        bw = new BufferedWriter(osw);
//    }
//    
//    @Override
//    public void run() {		
//    	String msg = null;
//    	try{msg = br.readLine();}
//    	catch(IOException e1){e1.printStackTrace();}
//    	while(msg != "quit" || msg != "QUIT") {
//    		try {bw.write(msg);}
//    		catch(IOException e1){e1.printStackTrace();}
//    		System.out.println(clientName + ": " + msg);
//    		pw.println("Enter Message:");
//        	try{msg = br.readLine();}
//        	catch(IOException e){e.printStackTrace();}
//        	}
//    	try{socket.close();}
//    	catch(IOException e){e.printStackTrace();}
//    }
//}