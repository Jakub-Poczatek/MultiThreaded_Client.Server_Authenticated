import java.io.*;
import java.sql.ResultSet;
import java.net.*;

// ClientHandler class 
class ClientHandler extends Thread 
{
	final private DataInputStream fromClient;
	final private DataOutputStream toClient;
	final private Socket socket;
	final private Retrieve database;
	private String clientName;


	// Constructor 
	public ClientHandler(Socket socket, DataInputStream fromClient, DataOutputStream toClient, Retrieve database, Long clientNumber)
	{ 
		this.socket = socket;
		this.fromClient = fromClient;
		this.toClient = toClient;
		this.database = database;
		this.clientName = "Client " + clientNumber;
	}

	@Override
	public void run() 
	{ 
		String receivedString;
		String[] messageParts = new String[2];
		int studentId;
		ResultSet students;
		boolean idFound = false;

		try {
			toClient.writeUTF("Hello " + clientName + ", please log in with your Student Id...");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		while (!socket.isClosed())
		{ 
			try {
				// receive the answer from client 
				receivedString = fromClient.readUTF();

				// Splice the received string into conditional and value
				System.arraycopy(receivedString.split(" "), 0, messageParts, 0, receivedString.split(" ").length);

				toClient.writeUTF("Server: Processing...");

				// write on output stream based on the
				// answer from the client

				switch (messageParts[0]) {
					case "Exit" :
						System.out.println(clientName + " Disconnected: " + socket);
						this.socket.close();
						break;

					case "Calculate" :
						try {
							double result = Math.PI * (Double.parseDouble(messageParts[1]) * Double.parseDouble(messageParts[1]));
							toClient.writeUTF(clientName +  ": the area of a circle for radius(" + messageParts[1] + ") is " + result + "."
															+ "\nPlease enter the Radius of the Circle...");
							break;
						} catch (Exception ex){
							toClient.writeUTF(clientName + ": incorrect radius, please enter a double...");
							break;
						}
						
					case "Login" :

						// Try to convert id to int
						try{
							studentId = Integer.parseInt(messageParts[1]);
						} catch (Exception ex){
							System.out.println(ex);
							toClient.writeUTF(clientName + ": incorrect id, please enter an int...");
							break;
						}

						// Get list of students
						try {
							students = database.getStudents();
							while(students.next()){
								if (studentId == students.getInt("STUD_ID")){
									toClient.writeUTF("true");
									clientName = students.getString("FNAME") + " " + students.getString("SNAME");
									toClient.writeUTF("Hello " + clientName + ", please enter the radius of a circle...");
									database.increaseTot_Req(studentId);
									idFound = true;
									break;
								}
							}
							if(!idFound) {
								toClient.writeUTF(clientName + ": ID not found in database, please try again...");
								break;
							}
						} catch (Exception ex){
							System.out.println(ex);
							toClient.writeUTF("Server: a problem occurred, please try again...");
							break;
						}
						break;

					default:
						toClient.writeUTF(clientName + ": invalid input, please try again...");
						break; 
				} 
			} catch (IOException e) { 
				System.out.println(e);
			} 
		}
	} 
} 
