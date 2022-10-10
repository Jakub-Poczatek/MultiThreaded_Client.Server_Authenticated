import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*; 

// Client class 
public class Client extends JFrame {
	// IO Streams
	private DataOutputStream toServer;
	private DataInputStream fromServer;

	public static void main(String[] args){
		new Client();
	}

	public Client(){
		// Is user validated
		boolean isValidated = false;
		// Create the GUI Components
		JPanel topPane = new JPanel(new BorderLayout());
		JLabel loginLabel = new JLabel("Log In");
		JTextField loginTextField = new JTextField();
		JButton loginButton = new JButton("Log In");
		JPanel bottomPane = new JPanel();
		JLabel areaLabel = new JLabel("Calculate the Area");
		JTextField areaTextField = new JTextField();
		JButton sendButton = new JButton("Send");
		JTextField resultTextField = new JTextField();

		// Assign Properties
		topPane.setLayout(new BorderLayout());
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					// Get message
					String message = loginTextField.getText();

					// Send message
					toServer.writeUTF("Login " + message.trim().strip());

				} catch (Exception ex){
					System.err.println(ex);
				}
			}
		});
		bottomPane.setLayout(new BorderLayout());
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					// Get message
					String message = areaTextField.getText();

					// Send message
					toServer.writeUTF("Calculate " + message.trim().strip());

				} catch (Exception ex){
					System.err.println(ex);
				}
			}
		});

		// Add Components
		topPane.add(loginLabel, BorderLayout.NORTH);
		topPane.add(loginTextField, BorderLayout.CENTER);
		topPane.add(loginButton, BorderLayout.EAST);
		bottomPane.add(areaLabel, BorderLayout.NORTH);
		bottomPane.add(areaTextField, BorderLayout.CENTER);
		bottomPane.add(sendButton, BorderLayout.EAST);
		bottomPane.add(resultTextField, BorderLayout.SOUTH);
		add(topPane, BorderLayout.NORTH);
		add(bottomPane, BorderLayout.SOUTH);

		// Set parameters after adding
		resultTextField.setPreferredSize(new Dimension(300, 50));
		resultTextField.setHorizontalAlignment(JTextField.CENTER);
		resultTextField.setEditable(false);

		areaTextField.setHorizontalAlignment(JTextField.CENTER);
		areaTextField.setEditable(false);

		loginTextField.setHorizontalAlignment(JTextField.CENTER);

		sendButton.setEnabled(false);

		// Set window parameters
		setTitle("Multithreaded Area of Circle Calculator");
		setSize(300, 200);
		setLocation(300, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		try {
			// Create a socket to connect to the server
			Socket socket = new Socket("localhost", 5056);

			// Create an input stream to receive data from the server
			fromServer = new DataInputStream(socket.getInputStream());

			// Create an output stream to send data to the server
			toServer = new DataOutputStream(socket.getOutputStream());
		} catch (Exception ex){
			System.err.println(ex);
		}

		while(true){
			try{
				String message = fromServer.readUTF();
				if(!isValidated) {
					if (message.equals("true")) {
						// Enable area components
						areaTextField.setEditable(true);
						sendButton.setEnabled(true);

						// Disable login in components
						loginButton.setEnabled(false);
						loginTextField.setEditable(false);
						isValidated = true;
					} else if (message.equals("false")) {
						message = "Invalid Login";
					}
				}
				resultTextField.setText(message);
			} catch (Exception ex){
				System.err.println(ex);
			}
		}
	}
}

/*
if(tosend.equals("Exit"))
		{
		System.out.println("Closing this connection : " + s);
		s.close();
		System.out.println("Connection closed");
		break;
}
*/