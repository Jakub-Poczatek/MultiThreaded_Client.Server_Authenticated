import java.io.*;
import java.util.*;
import java.net.*; 

// ClientHandler class 
class ClientHandler extends Thread 
{
	final DataInputStream fromClient;
	final DataOutputStream toClient;
	final Socket socket;
	

	// Constructor 
	public ClientHandler(Socket socket, DataInputStream fromClient, DataOutputStream toClient)
	{ 
		this.socket = socket;
		this.fromClient = fromClient;
		this.toClient = toClient;
	} 

	@Override
	public void run() 
	{ 
		String receivedString;
		String[] messageParts = new String[2];

		int id = 20088704;

		while (true)
		{ 
			try {
				// receive the answer from client 
				receivedString = fromClient.readUTF();

				// Splice the recieved string into conditional and value
				for(int i = 0; i < receivedString.split(" ").length; i++){
					messageParts[i] = receivedString.split(" ")[i];
				}

				// write on output stream based on the
				// answer from the client

				switch (messageParts[0]) {
					case "Calculate" :
						System.out.println(receivedString);
						try {
							double result = 3.14 * (Integer.parseInt(messageParts[1]) ^ 2);
							toClient.writeUTF(Double.toString(result));
							break;
						} catch (Exception ex){
							toClient.writeUTF("Invalid radius");
							break;
						}
						
					case "Login" :
						System.out.println("Attempting to Login");
						boolean validUser;
						try {
							validUser = Integer.parseInt(messageParts[1]) == id;
						} catch(Exception ex){
							validUser = false;
						}
						toClient.writeUTF(Boolean.toString(validUser));
						break; 
						
					default:
						System.out.println(receivedString);
						toClient.writeUTF("Invalid input");
						break; 
				} 
			} catch (IOException e) { 
				e.printStackTrace(); 
			} 
		}
	} 
} 
