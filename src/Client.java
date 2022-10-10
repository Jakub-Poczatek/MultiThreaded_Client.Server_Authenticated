import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*; 
import java.util.Scanner; 

// Client class 
public class Client extends JFrame {
	// IO Streams
	private DataOutputStream toServer;
	private DataInputStream fromServer;

	public static void main(String[] args){
		new Client();
	}

	public Client(){
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
					toServer.writeUTF(message);

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
					toServer.writeUTF(message);

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
		add(topPane);

		// Set parameters after adding


		// Set window parameters
		setTitle("Multithreaded Area of Circle Calculator");
		setSize(200, 150);
		setLocation(300, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		loginTextField.setPreferredSize(new Dimension(100, 20));
		loginTextField.setMinimumSize(new Dimension(100, 100));

		try {
			// Get the localhost ip
			InetAddress ip = InetAddress.getByName("localhost");

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